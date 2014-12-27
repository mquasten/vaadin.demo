package de.mq.phone.web.person;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import org.springframework.validation.BindingResult;

import de.mq.phone.domain.person.GeoCoordinates;


interface PersonSearchController {

	void assignPersons(final PersonSearchModel model, final int pageSize);

	void assignGeoKoordinates(final PersonSearchModel model);

	BindingResult validate(final Map<String, Object> map);

	
	Collection<String> geoInfos(final GeoCoordinates geoCoordinates, final PersonSearchModel model, final Locale locale);

	
	void incPaging(final PersonSearchModel model);
	
	void endPaging(final PersonSearchModel model);
	void decPaging(final PersonSearchModel model) ;
	void beginPaging(final PersonSearchModel model); 

}