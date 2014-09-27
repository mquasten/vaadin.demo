package de.mq.phone.domain.person.support;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonService;

public class PersonServiceTest {
	
	private final PersonRepository personRepository = Mockito.mock(PersonRepository.class);
	
	@Test
	public final void persons() {
		final PersonService personService = new PersonServiceImpl(personRepository);
		final Person person = Mockito.mock(Person.class);
		final Address address = Mockito.mock(Address.class);
		final Contact contact = Mockito.mock(Contact.class);
		final List<Person> persons = new ArrayList<>();
		persons.add(Mockito.mock(Person.class));
		Mockito.when(personRepository.forCriterias(person, address, contact)).thenReturn(persons);
		Assert.assertEquals(persons, personService.persons(person, address, contact));
		
	}
	@Test
	public final void save() {
		final PersonService personService = new PersonServiceImpl(personRepository);
		final Person person = Mockito.mock(Person.class);
		personService.save(person);
		Mockito.verify(personRepository).save(person);
	}

}
