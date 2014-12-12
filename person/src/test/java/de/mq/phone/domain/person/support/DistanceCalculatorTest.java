package de.mq.phone.domain.person.support;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.phone.domain.person.GeoCoordinates;

public class DistanceCalculatorTest {
	
	final DistanceCalculator distanceCalculator = new SimpleDistanceCalculatorIml();
	final GeoCoordinates c1 = new GeoDegreesCoordinatesImpl(51.317222, 6.571111);
	
	@Test
	public final void dy() {
	
		Assert.assertEquals(10.6,  round(distanceCalculator.distance(c1, new GeoDegreesCoordinatesImpl(51.23916, 6.658888))));
		Assert.assertEquals(144.8 , round(distanceCalculator.angle(c1, new GeoDegreesCoordinatesImpl(51.23916, 6.658888))));
	}
	
	
	@Test
	public final void lma() {
	
		Assert.assertEquals(13.4,  round(distanceCalculator.distance(c1, new GeoDegreesCoordinatesImpl(51.375, 6.401388))));
		Assert.assertEquals(-61.3 , round(distanceCalculator.angle(c1, new GeoDegreesCoordinatesImpl(51.375, 6.401388))));
	}
	
	@Test
	public final void due() {
	
		Assert.assertEquals(8.2,  round(distanceCalculator.distance(c1, new GeoDegreesCoordinatesImpl(51.308333, 6.68805))));
		Assert.assertEquals(96.9 , round(distanceCalculator.angle(c1, new GeoDegreesCoordinatesImpl(51.308333, 6.68805))));
	}
	
	@Test
	public final void l1() {
	
		Assert.assertEquals(22.6,  round(distanceCalculator.distance(c1, new GeoDegreesCoordinatesImpl(51.359722, 6.889166))));
		Assert.assertEquals(77.80, round(distanceCalculator.angle(c1, new GeoDegreesCoordinatesImpl(51.359722, 6.889166))));
	}


	private double round(double result) {
		return (double) Math.round(10* result)/10;
	}
	
	@Test
	public final void stg() {
		Assert.assertEquals(88.6, round(distanceCalculator.angle(new GeoDegreesCoordinatesImpl(52.0 + 31.0/60 + 10.0/3600, 13.0 + 24.0/60 + 24.0/3600 ), new GeoDegreesCoordinatesImpl(48.0 + 42.0/60, 44.0 + 29.0/60))));
		Assert.assertEquals(2215.6,  round(distanceCalculator.distance(new GeoDegreesCoordinatesImpl(52.0 + 31.0/60 + 10.0/3600, 13.0 + 24.0/60 + 24.0/3600 ), new GeoDegreesCoordinatesImpl(48.0 + 42.0/60, 44.0 + 29.0/60))));
	}

}
