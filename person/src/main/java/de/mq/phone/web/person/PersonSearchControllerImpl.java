package de.mq.phone.web.person;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import de.mq.phone.domain.person.GeoCoordinates;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonService;
import de.mq.phone.domain.person.support.DistanceCalculator;
import de.mq.phone.web.person.ValidatorQualifier.Type;

@Controller()
class PersonSearchControllerImpl implements PersonSearchController {
	
private static final int FRACTION_DIGITS = 2;
static final String BINDING_RESULT_NAME = "search";
private final PersonService personService;
private final DistanceCalculator distanceCalculator;
private final Validator validator; 
private final NumberFormat numberFormat = NumberFormat.getNumberInstance();

	
	@Autowired
	PersonSearchControllerImpl(final PersonService personService, DistanceCalculator distanceCalculator , @ValidatorQualifier(Type.Distance) final Validator validator) {
	this.personService = personService;
	this.distanceCalculator=distanceCalculator;
	this.validator=validator;
	numberFormat.setMaximumFractionDigits(FRACTION_DIGITS);
	numberFormat.setMinimumFractionDigits(FRACTION_DIGITS);
	numberFormat.setGroupingUsed(false);
}

	/* (non-Javadoc)
	 * @see de.mq.phone.web.person.PersonSearchController#assignPersons(de.mq.phone.web.person.PersonSearchModel)
	 */
	@Override
	public  final void assignPersons(final PersonSearchModel model, final int pageSize) {
		model.setPaging(personService.paging(model.getSearchCriteriaPerson(), model.getSearchCriteriaAddress(), model.getSearchCriteriaContact(), model.getSearchCriteriaDistance(), pageSize));
		model.setPersons(personService.persons(model.getSearchCriteriaPerson(),model.getSearchCriteriaAddress(), model.getSearchCriteriaContact(), model.getSearchCriteriaDistance(), model.getPaging()));
	}
	@Override
	public  final void incPaging(final PersonSearchModel model) {
		model.incPaging();
		model.setPersons(personService.persons(model.getSearchCriteriaPerson(),model.getSearchCriteriaAddress(), model.getSearchCriteriaContact(), model.getSearchCriteriaDistance(), model.getPaging()));
		
	}
	@Override
	public  final void endPaging(final PersonSearchModel model) {
		model.endPaging();
		model.setPersons(personService.persons(model.getSearchCriteriaPerson(),model.getSearchCriteriaAddress(), model.getSearchCriteriaContact(), model.getSearchCriteriaDistance(), model.getPaging()));
		
	}
	@Override
	public  final void decPaging(final PersonSearchModel model) {
		model.decPaging();
		model.setPersons(personService.persons(model.getSearchCriteriaPerson(),model.getSearchCriteriaAddress(), model.getSearchCriteriaContact(), model.getSearchCriteriaDistance(), model.getPaging()));
		
	}
	
	@Override
	public  final void beginPaging(final PersonSearchModel model) {
		model.beginPaging();
		model.setPersons(personService.persons(model.getSearchCriteriaPerson(),model.getSearchCriteriaAddress(), model.getSearchCriteriaContact(), model.getSearchCriteriaDistance(), model.getPaging()));
		
	}
	
	
	@Override
	public final void assignGeoKoordinates(final PersonSearchModel model) {
		final Person person = personService.defaultPerson();
		if( ! person.hasGeoCoordinates() ) {
			model.setGeoCoordinates(null);
			return;
		} 
		model.setGeoCoordinates(person.address().coordinates());
	
		
	}
	
	@Override
	public final BindingResult validate(final Map<String,Object> map) {
		final MapBindingResult bindingResult = new MapBindingResult(map, BINDING_RESULT_NAME);

		ValidationUtils.invokeValidator(validator, map, bindingResult);
		return bindingResult; 
	}
	
	@Override
	public final Collection<String> geoInfos(final GeoCoordinates geoCoordinates, final PersonSearchModel model, final Locale locale) {
		final Collection<String> results = new ArrayList<>();
		
		
		if( geoCoordinates==null){
			return  Collections.unmodifiableCollection(results);
		}
		
		results.add(geoCoordinates.location());
		if( model.hasGeoCoordinates() ) {
			results.add(String.format("%s km, %s°" , numberFormat.format(distanceCalculator.distance(model.getGeoCoordinates(), geoCoordinates)),  numberFormat.format(distanceCalculator.angle(model.getGeoCoordinates(), geoCoordinates) )));
		}
	   return  Collections.unmodifiableCollection(results);
		
	}
}
