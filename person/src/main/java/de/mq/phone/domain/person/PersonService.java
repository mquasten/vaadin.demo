package de.mq.phone.domain.person;

import java.util.List;

public interface PersonService {

	List<Person> persons(final PersonStringAware person,final AddressStringAware address, final Contact contact);

	void save(final Person person);

	Person person(final String id);
	

}