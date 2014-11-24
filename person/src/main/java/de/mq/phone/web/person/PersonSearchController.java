package de.mq.phone.web.person;

import java.util.Map;

import org.springframework.validation.BindingResult;


interface PersonSearchController {

	void assignPersons(PersonSearchModel model);

	void assignGeoKoordinates(final PersonSearchModel model);

	BindingResult validate(final Map<String, Object> map);

	


}