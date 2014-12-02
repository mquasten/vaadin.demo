package de.mq.phone.web.person;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonService;
import de.mq.phone.domain.person.PersonStringAware;
import de.mq.phone.web.person.ValidatorQualifier.Type;

@Controller()
class PersonSearchControllerImpl implements PersonSearchController {
	
static final String BINDING_RESULT_NAME = "search";
private final PersonService personService;
private final Validator validator; 
	
	@Autowired
	PersonSearchControllerImpl(final PersonService personService, @ValidatorQualifier(Type.Distance) final Validator validator) {
	this.personService = personService;
	this.validator=validator;
}

	/* (non-Javadoc)
	 * @see de.mq.phone.web.person.PersonSearchController#assignPersons(de.mq.phone.web.person.PersonSearchModel)
	 */
	@Override
	public  final void assignPersons(final PersonSearchModel model) {
		final PersonStringAware person = model.getSearchCriteriaPerson();
		final Contact contact = model.getSearchCriteriaContact();
		final AddressStringAware address = model.getSearchCriteriaAddress();
		final Circle circle = model.getSearchCriteriaDistance();
		model.setPersons(personService.persons(person,address, contact, circle));
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
}
