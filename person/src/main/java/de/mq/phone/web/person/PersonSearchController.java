package de.mq.phone.web.person;

import java.util.Collection;
import java.util.Map;

import org.springframework.validation.BindingResult;

import de.mq.phone.domain.person.GeoCoordinates;


interface PersonSearchController {

	void assignPersons(PersonSearchModel model);

	void assignGeoKoordinates(final PersonSearchModel model);

	BindingResult validate(final Map<String, Object> map);

	Collection<String> geoInfos(final GeoCoordinates geoCoordinates, final PersonSearchModel model);

	


}