package de.mq.phone.domain.person;



public interface Address extends AddressStringAware {

	String zipCode();

	String city();

	String street();

	String houseNumber();

	void assign(GeoCoordinates geoCoordinates);

	GeoCoordinates coordinates();


}