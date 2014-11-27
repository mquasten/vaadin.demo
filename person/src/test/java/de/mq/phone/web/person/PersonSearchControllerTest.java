package de.mq.phone.web.person;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.geo.Circle;
import org.springframework.validation.Validator;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonService;
import de.mq.phone.domain.person.PersonStringAware;

public class PersonSearchControllerTest {
	
	private PersonService personService = Mockito.mock(PersonService.class);
	private final PersonSearchController personSearchController = new PersonSearchControllerImpl(personService, Mockito.mock(Validator.class));
	
	@Test
	public final void assign() {
		final PersonSearchModel model = Mockito.mock(PersonSearchModel.class);
		final PersonStringAware person = Mockito.mock(PersonStringAware.class);
		final Contact contact = Mockito.mock(Contact.class);
		final AddressStringAware addressStringAware = Mockito.mock(AddressStringAware.class);
		final Circle circle = Mockito.mock(Circle.class);
		Mockito.when(model.getSearchCriteriaPerson()).thenReturn(person);
		Mockito.when(model.getSearchCriteriaContact()).thenReturn(contact);
		Mockito.when(model.getSearchCriteriaAddress()).thenReturn(addressStringAware);
		Mockito.when(model.getSearchCriteriaDistance()).thenReturn(circle);
		final List<Person> persons = new ArrayList<>();
		persons.add(Mockito.mock(Person.class));
		Mockito.when(personService.persons(person, addressStringAware, contact,circle)).thenReturn(persons);
		personSearchController.assignPersons(model);
		Mockito.verify(model, Mockito.times(1)).setPersons(persons);
	}

}