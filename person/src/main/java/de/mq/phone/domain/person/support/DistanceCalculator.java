package de.mq.phone.domain.person.support;

import de.mq.phone.domain.person.GeoCoordinates;

public interface DistanceCalculator {

	double distance(final GeoCoordinates c1, final GeoCoordinates c2);

	double angle(final GeoCoordinates c1, final GeoCoordinates c2);

}