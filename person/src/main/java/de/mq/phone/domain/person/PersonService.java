package de.mq.phone.domain.person;

import java.util.List;

import org.springframework.data.geo.Circle;

import de.mq.phone.domain.person.support.Paging;


public interface PersonService {

	List<Person> persons(final PersonStringAware person,final AddressStringAware address, final Contact contact, final Circle distance);

	void save(final Person person);

	Person person(final String id);

	void deletePerson(final String id);

	Person defaultPerson();

	Paging paging(final PersonStringAware person, final AddressStringAware address, final Contact contact, final Circle circle);
	

}