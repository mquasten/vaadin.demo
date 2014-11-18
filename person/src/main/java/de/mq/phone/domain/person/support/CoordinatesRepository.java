package de.mq.phone.domain.person.support;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.GeoCoordinates;

public interface CoordinatesRepository {

	GeoCoordinates forAddress(final AddressStringAware address);

}