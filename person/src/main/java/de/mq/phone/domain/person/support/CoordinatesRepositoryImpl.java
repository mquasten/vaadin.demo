package de.mq.phone.domain.person.support;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.web.client.RestOperations;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.GeoCoordinates;

@Repository
public class CoordinatesRepositoryImpl implements CoordinatesRepository {

	static final String COUNTRY_PARAM = "country";
	static final String SENSOR_PARAM = "sensor";
	static final String ADDRESS_PARAM = "address";
	static final String STATUS_OK = "OK";
	static final String LNG_KEY = "lng";
	static final String LAT_KEY = "lat";
	static final String LOCATION_KEY = "location";
	static final String GEOMETRY_KEY = "geometry";
	static final String STREET_ADDRESS_TYPE = "street_address";
	static final String TYPES_KEY = "types";
	static final String RESULTS_KEY = "results";
	static final String STATUS_KEY = "status";
	private final RestOperations restOperations;
	static final String GOOGLE_URL = "http://maps.googleapis.com/maps/api/geocode/json?address={address}&sensor={sensor}&components=country:{country}";

	@Autowired
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

		final Map<String, Object> params = new HashMap<>();
		params.put(ADDRESS_PARAM, address.address());
		params.put(SENSOR_PARAM, false);
		params.put(COUNTRY_PARAM, "DE");

		final Map<String, Object> result = restOperations.getForObject(GOOGLE_URL, Map.class, params);
		
		Assert.notNull(result.get(STATUS_KEY), "StatusCode expected in Response");
		Assert.isTrue(result.get(STATUS_KEY).equals(STATUS_OK), "Status OK expected");

		Assert.notNull(result.get(RESULTS_KEY));
		return DataAccessUtils.requiredSingleResult(((List<Map<String, Object>>) result.get(RESULTS_KEY)).stream().filter(row -> row.containsKey(TYPES_KEY) && ((Collection<?>) row.get(TYPES_KEY)).contains(STREET_ADDRESS_TYPE)).filter(row -> row.containsKey(GEOMETRY_KEY)).map(row -> (Map<String, Object>) row.get(GEOMETRY_KEY)).filter(row -> row.containsKey(LOCATION_KEY)).map(row -> (Map<String, Object>) row.get(LOCATION_KEY)).filter(row -> row.containsKey(LAT_KEY) && row.containsKey(LNG_KEY))
				.map(row -> new GeoDegreesCoordinatesImpl((Double) row.get(LAT_KEY), (Double) row.get(LNG_KEY))).collect(Collectors.toList()));

	}
}
