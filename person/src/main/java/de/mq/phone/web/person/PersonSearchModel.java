package de.mq.phone.web.person;

import java.util.List;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonStringAware;
import de.mq.vaadin.util.Subject;

interface PersonSearchModel extends Subject<PersonSearchModel, PersonSearchModel.EventType > {

	enum EventType {
		PersonsChanges
	}
	
	void setPersons(List<Person> persons);

	List<Person> getPersons();

	void setSearchCriteria(Object bean);

	PersonStringAware getSearchCriteriaPerson();

	Contact getSearchCriteriaContact();

	AddressStringAware getSearchCriteriaAddress();

	

}