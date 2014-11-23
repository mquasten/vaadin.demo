package de.mq.phone.web.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.stereotype.Controller;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonService;
import de.mq.phone.domain.person.PersonStringAware;

@Controller()
class PersonSearchControllerImpl implements PersonSearchController {
	
	
private final PersonService personService;
	
	@Autowired
	PersonSearchControllerImpl(final PersonService personService) {
	this.personService = personService;
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
}
