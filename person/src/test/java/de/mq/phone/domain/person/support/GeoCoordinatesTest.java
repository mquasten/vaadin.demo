package de.mq.phone.domain.person.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.phone.domain.person.GeoCoordinates;

public class GeoCoordinatesTest {
	private static final double LONGITUDE2 = -56.00;
	private static final double LATITUDE2 = -67.3;
	private static final double LONGITUDE = 44.5227496;
	private static final double LATITUDE = 48.7098776;
	private static final String STALINGRAD = "N 48° 42' 36'' E 44° 31' 22''";
	private static final String KAPHORN = "S 67° 18' 0'' W 56° 0' 0''";

	@Test
	public final void create() {
		final GeoCoordinates coordinates = new GeoDegreesCoordinatesImpl(LATITUDE, LONGITUDE);
		Assert.assertEquals(LATITUDE, coordinates.latitude());
		Assert.assertEquals(LONGITUDE, coordinates.longitude());
	}
	@Test
	public final void createdefault() {
		final GeoCoordinates coordinates = BeanUtils.instantiateClass(GeoDegreesCoordinatesImpl.class, GeoCoordinates.class);
		Assert.assertEquals(0D, coordinates.latitude());
		Assert.assertEquals(0D, coordinates.longitude());
	}
	
	@Test
	public final void beforeSave() {
		final GeoCoordinates coordinates = new GeoDegreesCoordinatesImpl(LATITUDE, LONGITUDE);
		Assert.assertNull(ReflectionTestUtils.getField(coordinates, "location"));
		((GeoDegreesCoordinatesImpl)coordinates).beforeSave();
		final Double[] result = (Double[]) ReflectionTestUtils.getField(coordinates, "location") ;
		Assert.assertEquals(LATITUDE, result[0]);
		Assert.assertEquals(LONGITUDE, result[1]);
	}
	@Test
	public final void  degrees() {
		final GeoCoordinates coordinates = new GeoDegreesCoordinatesImpl(LATITUDE, LONGITUDE);
		Assert.assertEquals(STALINGRAD, coordinates.location());
	}
	@Test
	public final void degrees2() {
		final GeoCoordinates coordinates = new GeoDegreesCoordinatesImpl(LATITUDE2, LONGITUDE2);
		 Assert.assertEquals(KAPHORN, coordinates.location());
	}
	@Test
	public final void hash() {
		final GeoCoordinates coordinates = new GeoDegreesCoordinatesImpl(LATITUDE2, LONGITUDE2);
		Assert.assertEquals(coordinates.latitude().hashCode() + coordinates.longitude().hashCode() ,coordinates.hashCode());
		
		final GeoCoordinates nullCoordinates= new GeoDegreesCoordinatesImpl(0,0);
		ReflectionTestUtils.setField(nullCoordinates, "longitude", null);
		Assert.assertEquals(System.identityHashCode(nullCoordinates), nullCoordinates.hashCode());
		ReflectionTestUtils.setField(nullCoordinates, "longitude", 0d);
		ReflectionTestUtils.setField(nullCoordinates, "latitude", null);
		Assert.assertEquals(System.identityHashCode(nullCoordinates), nullCoordinates.hashCode());
		ReflectionTestUtils.setField(nullCoordinates, "latitude", 0d);
		Assert.assertEquals(0, nullCoordinates.hashCode());
	}
	@Test
	public final void equals() {
		final GeoCoordinates coordinates = new GeoDegreesCoordinatesImpl(LATITUDE2, LONGITUDE2);
		final GeoCoordinates nullCoordinates = new GeoDegreesCoordinatesImpl(0, 0);
		ReflectionTestUtils.setField(nullCoordinates, "latitude", null);
		ReflectionTestUtils.setField(nullCoordinates, "longitude", null);
		Assert.assertFalse(coordinates.equals("don'tLetMeGetMe"));
		Assert.assertFalse(nullCoordinates.equals(coordinates));
		Assert.assertFalse(coordinates.equals(nullCoordinates));
		Assert.assertTrue(coordinates.equals(new GeoDegreesCoordinatesImpl(LATITUDE2, LONGITUDE2)));
		Assert.assertFalse(coordinates.equals(new GeoDegreesCoordinatesImpl(LATITUDE2, 0)));
		Assert.assertFalse(coordinates.equals(new GeoDegreesCoordinatesImpl(0, 0)));
	}

}
