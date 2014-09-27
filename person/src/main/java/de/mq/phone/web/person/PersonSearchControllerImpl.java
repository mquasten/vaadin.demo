package de.mq.phone.web.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
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
		
		model.setPersons(personService.persons(person,address, contact));
	}

}
