package de.mq.phone.domain.person.support;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.geo.Circle;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.GeoCoordinates;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonService;

public class PersonServiceTest {

	private static final String ID = "19680528";
	private final PersonRepository personRepository = Mockito.mock(PersonRepository.class);
	private final CoordinatesRepository coordinatesRepository = Mockito.mock(CoordinatesRepository.class);
	private final Paging paging = Mockito.mock(Paging.class);
	
	private final static Integer PAGE_SIZE = 10; 
	private final static Integer NUM_ROWS = 42; 

	@Test
	public final void persons() {
		final PersonService personService = new PersonServiceImpl(personRepository, coordinatesRepository);
		final Person person = Mockito.mock(Person.class);
		final Address address = Mockito.mock(Address.class);
		final Contact contact = Mockito.mock(Contact.class);
		final Circle circle = Mockito.mock(Circle.class);
		final List<Person> persons = new ArrayList<>();
		persons.add(Mockito.mock(Person.class));
		Mockito.when(personRepository.forCriterias(person, address, contact, circle, paging)).thenReturn(persons);
		Assert.assertEquals(persons, personService.persons(person, address, contact, circle, paging));

	}

	@Test
	public final void save() {
		final PersonService personService = new PersonServiceImpl(personRepository, coordinatesRepository);
		final Person person = Mockito.mock(Person.class);
		personService.save(person);
		Mockito.verify(personRepository).save(person);
	}

	@Test
	public final void saveWithCoordinates() {
		final PersonService personService = new PersonServiceImpl(personRepository, coordinatesRepository);
		final Person person = Mockito.mock(Person.class);
		Mockito.when(person.hasAddress()).thenReturn(true);
		Address address = Mockito.mock(Address.class);
		Mockito.when(person.address()).thenReturn(address);
		final GeoCoordinates geoCoordinates = Mockito.mock(GeoCoordinates.class);
		Mockito.when(coordinatesRepository.forAddress(address)).thenReturn(geoCoordinates);
		personService.save(person);
		Mockito.verify(personRepository).save(person);
		Mockito.verify(address).assign(geoCoordinates);
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

	@Test
	public final void defaultPerson() {
		final PersonService personService = new PersonServiceImpl(personRepository, coordinatesRepository);
		final Person person = Mockito.mock(Person.class);
		Mockito.when(personRepository.forId(PersonServiceImpl.DEFAULT_PERSON_ID.toString())).thenReturn(person);
		Assert.assertEquals(person, personService.defaultPerson());
	}

	@Test
	public final void defaultPersonTouchedForTheVeryFirstTime() {
		final PersonService personService = new PersonServiceImpl(personRepository, coordinatesRepository);

		final Person result = personService.defaultPerson();
		Mockito.verify(personRepository).save(result);
		Assert.assertEquals(PersonImpl.class, result.getClass());
	}

	@Test
	public final void paging() {
		final PersonService personService = new PersonServiceImpl(personRepository, coordinatesRepository);
		final Person person = Mockito.mock(Person.class);
		final Address address = Mockito.mock(Address.class);
		final Contact contact = Mockito.mock(Contact.class);
		final Circle circle = Mockito.mock(Circle.class);
		
		Mockito.when(personRepository.countFor(person, address, contact, circle)).thenReturn(NUM_ROWS);
		final Paging paging = personService.paging(person, address, contact, circle, PAGE_SIZE);
		Assert.assertEquals((long) Math.ceil((double) NUM_ROWS/PAGE_SIZE), paging.maxPages());
		Assert.assertEquals(PAGE_SIZE, paging.pageSize());
		Assert.assertEquals(1L, paging.currentPage());
	}

}
