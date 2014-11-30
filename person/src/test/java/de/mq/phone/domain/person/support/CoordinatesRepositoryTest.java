package de.mq.phone.domain.person.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.client.RestOperations;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.GeoCoordinates;

public class CoordinatesRepositoryTest {
	


	private static final String ADDRESS = "Stalingrad";

	private final RestOperations restOperations = Mockito.mock(RestOperations.class);
	
	private final CoordinatesRepository coordinatesRepository = new CoordinatesRepositoryImpl(restOperations);
	
	private Address address = Mockito.mock(Address.class);
	
	private GeoCoordinates geoCoordinates = Mockito.mock(GeoCoordinates.class);
	
	private final  Map<String,Object> response = new HashMap<>();

	private final Map<String, Object> result = new HashMap<>();
	
	final Map<String, Object> location = new HashMap<>();
	
	@Before
	public final void setup() {
		result.clear();
		location.clear();
		
		Mockito.when(geoCoordinates.longitude()).thenReturn(44.5D);
		Mockito.when(geoCoordinates.latitude()).thenReturn(48.7D);
		response.put( CoordinatesRepositoryImpl.STATUS_KEY , CoordinatesRepositoryImpl.STATUS_OK);
		
		final List<Map<String,Object>> results = new ArrayList<>();
		results.add(result);
		response.put( CoordinatesRepositoryImpl.RESULTS_KEY, results);
		final Map<String,Map<String,Object>> geometry = new HashMap<>();
		result.put(CoordinatesRepositoryImpl.GEOMETRY_KEY, geometry);
		
		geometry.put(CoordinatesRepositoryImpl.LOCATION_KEY, location);
		
		location.put(CoordinatesRepositoryImpl.LAT_KEY, geoCoordinates.latitude());
		location.put(CoordinatesRepositoryImpl.LNG_KEY, geoCoordinates.longitude());

		final List<String> types = new ArrayList<>();
		types.add(CoordinatesRepositoryImpl.STREET_ADDRESS_TYPE );
		result.put(CoordinatesRepositoryImpl.TYPES_KEY, types);
		
		Mockito.when(address.address()).thenReturn(ADDRESS);
		
		final Map<String,Object> params = new HashMap<>();
		params.put(CoordinatesRepositoryImpl.COUNTRY_PARAM, "DE");
		params.put(CoordinatesRepositoryImpl.ADDRESS_PARAM, address.address());
		params.put(CoordinatesRepositoryImpl.SENSOR_PARAM, Boolean.FALSE);		
		Mockito.when(restOperations.getForObject(CoordinatesRepositoryImpl.GOOGLE_URL, Map.class, params)).thenReturn(response);
		
	}
	
	

	@Test
	public final void forAddress() {
		final GeoCoordinates geoCoordinates = coordinatesRepository.forAddress(address);
		Assert.assertEquals(this.geoCoordinates.latitude(), geoCoordinates.latitude());
		Assert.assertEquals(this.geoCoordinates.longitude(), geoCoordinates.longitude());
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public final void forAddressWithoutType() {
		result.remove(CoordinatesRepositoryImpl.TYPES_KEY);
	   coordinatesRepository.forAddress(address);
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public final void forAddressMissingType() {
		result.put(CoordinatesRepositoryImpl.TYPES_KEY, new ArrayList<>());
		coordinatesRepository.forAddress(address);
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public final void forAddressMissingLattitude() {
		location.remove(CoordinatesRepositoryImpl.LAT_KEY);
		coordinatesRepository.forAddress(address);
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public final void forAddressMissingLongitude() {
		location.remove(CoordinatesRepositoryImpl.LNG_KEY);
		coordinatesRepository.forAddress(address);
	}
	

}
