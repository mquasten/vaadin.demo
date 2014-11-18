package de.mq.phone.domain.person.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.util.Assert;
import org.springframework.web.client.RestOperations;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.GeoCoordinates;

public class CoordinatesRepositoryImpl implements CoordinatesRepository {

	private static final String LNG_KEY = "lng";

	private static final String LAT_KEY = "lat";

	private static final String LOCATION_KEY = "location";

	private static final String GEOMETRY_KEY = "geometry";

	private static final String STREET_ADDRESS_TYPE = "street_address";

	private static final String TYPES_KEY = "types";

	private static final String RESULTS_KEY = "results";

	private static final String STATUS_KEY = "status";

	private final RestOperations restOperations;

	static final String GOOGLE_URL = "http://maps.googleapis.com/maps/api/geocode/json?address={address}&sensor={}";

	public CoordinatesRepositoryImpl(final RestOperations restOperations) {
		this.restOperations = restOperations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.mq.phone.domain.person.support.CoordinatesRepository#forAddress(de.
	 * mq.phone.domain.person.AddressStringAware)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final GeoCoordinates forAddress(final AddressStringAware address) {

		final Collection<GeoCoordinates> results = new ArrayList<>();
		final Map<String, Object> params = new HashMap<>();
		params.put("address", address.address());
		params.put("sensor", false);

		final Map<String, Object> result = restOperations.getForObject(GOOGLE_URL, Map.class, params);
		Assert.notNull(result.get(STATUS_KEY), "StatusCode expected in Response");
		Assert.isTrue(result.get(STATUS_KEY).equals("OK"), "Status OK expected");

		for (final Map<String, Object> row : ((List<Map<String, Object>>) result.get(RESULTS_KEY))) {
			final Collection<?> types = (Collection<?>) row.get(TYPES_KEY);
			if (!types.contains(STREET_ADDRESS_TYPE)) {
				continue;
			}
			final Map<String, Object> geometry = (Map<String, Object>) row.get(GEOMETRY_KEY);
			Assert.notEmpty(geometry, "Geometry expected");
			final Map<String, Object> location = (Map<String, Object>) geometry.get(LOCATION_KEY);
			Assert.notEmpty(geometry, "Location expected");
			Assert.isInstanceOf(Double.class, location.get(LAT_KEY));
			Assert.isInstanceOf(Double.class, location.get(LNG_KEY));
			results.add(new GeoDegreesCoordinatesImpl((Double) location.get(LAT_KEY), (Double) location.get(LNG_KEY)));
		}
		return DataAccessUtils.requiredSingleResult(results);

	}
}
