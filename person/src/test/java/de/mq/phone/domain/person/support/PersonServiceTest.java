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
	
	private static final String ID = "19680528";
	private final PersonRepository personRepository = Mockito.mock(PersonRepository.class);
	private final CoordinatesRepository coordinatesRepository = Mockito.mock(CoordinatesRepository.class);
	
	@Test
	public final void persons() {
		final PersonService personService = new PersonServiceImpl(personRepository, coordinatesRepository);
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
		final PersonService personService = new PersonServiceImpl(personRepository, coordinatesRepository);
		final Person person = Mockito.mock(Person.class);
		personService.save(person);
		Mockito.verify(personRepository).save(person);
	}
	
	@Test
	public final void byId() {
		final PersonService personService = new PersonServiceImpl(personRepository, coordinatesRepository);
		final Person person = Mockito.mock(Person.class);
		Mockito.when(personRepository.forId(ID)).thenReturn(person);
		Assert.assertEquals(person, personService.person(ID));
	}
	
	@Test
	public final void delete() {
		final PersonService personService = new PersonServiceImpl(personRepository, coordinatesRepository);
		personService.deletePerson(ID);
		Mockito.verify(personRepository).delete(ID);
		
	}

}
