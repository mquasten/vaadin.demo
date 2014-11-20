package de.mq.phone.domain.person.support;

import java.util.List;

import org.springframework.data.geo.Circle;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonStringAware;

public interface PersonRepository {

	void save(Person person);

	//List<Person> forCriterias(final PersonStringAware person, final AddressStringAware address, Contact contact);

	Person forId(final String id);

	void delete(final String id);

	List<Person> forCriterias(PersonStringAware person, AddressStringAware address, Contact contact, Circle circle);
	

	
}