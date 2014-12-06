package de.mq.phone.domain.person.support;



import org.springframework.util.Assert;

import de.mq.phone.domain.person.GeoCoordinates;

public class SimpleDistanceCalculatorIml implements DistanceCalculator {
	
	private static final double RE = 6371;

	private double  e12(final double b1,final double b2, final double l1,final double l2 ) {
		  return  Math.acos(Math.sin(b1)*Math.sin(b2) + Math.cos(b1)*Math.cos(b2)*Math.cos(l2-l1));
	}
	
	private double  rad(final double x) {
	
		return x*Math.PI/180;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.DistanceCalculator#distance(de.mq.phone.domain.person.GeoCoordinates, de.mq.phone.domain.person.GeoCoordinates)
	 */
	@Override
	public double distance(final GeoCoordinates c1, final GeoCoordinates c2) {
		coordinatesGuard(c1);
		coordinatesGuard(c2);
		return RE * e12(rad(c1.latitude()), rad(c2.latitude()), rad(c1.longitude()), rad(c2.longitude()));
	}

	private void coordinatesGuard(final GeoCoordinates geocoordinates) {
		Assert.notNull(geocoordinates);
		Assert.notNull(geocoordinates.latitude());
		Assert.notNull(geocoordinates.longitude());
	}
	
	private double angle(final double b1,final double b2, final double l1,final double l2) {
		 final double e12 = e12(b1,b2,l1,l2);
		 double abs = Math.abs(Math.acos((Math.sin(b2) - Math.sin(b1) * Math.cos(e12)) /Math.cos(b1)/Math.sin(e12)));
		 double signum = Math.signum(Math.sin(l2-l1)*Math.cos(b2) / Math.sin(e12));
		 return abs*signum;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.DistanceCalculator#angle(de.mq.phone.domain.person.GeoCoordinates, de.mq.phone.domain.person.GeoCoordinates)
	 */
	@Override
	public double angle(final GeoCoordinates c1, final GeoCoordinates c2) {
		coordinatesGuard(c1);
		coordinatesGuard(c2);
		return 180/Math.PI * angle(rad(c1.latitude()), rad(c2.latitude()), rad(c1.longitude()), rad(c2.longitude()));
	}
}
