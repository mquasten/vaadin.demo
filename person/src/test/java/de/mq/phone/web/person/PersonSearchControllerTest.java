package de.mq.phone.web.person;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonService;
import de.mq.phone.domain.person.PersonStringAware;

public class PersonSearchControllerTest {
	
	private PersonService personService = Mockito.mock(PersonService.class);
	private final PersonSearchController personSearchController = new PersonSearchControllerImpl(personService);
	
	@Test
	public final void assign() {
		final PersonSearchModel model = Mockito.mock(PersonSearchModel.class);
		final PersonStringAware person = Mockito.mock(PersonStringAware.class);
		final Contact contact = Mockito.mock(Contact.class);
		final AddressStringAware addressStringAware = Mockito.mock(AddressStringAware.class);
		Mockito.when(model.getSearchCriteriaPerson()).thenReturn(person);
		Mockito.when(model.getSearchCriteriaContact()).thenReturn(contact);
		Mockito.when(model.getSearchCriteriaAddress()).thenReturn(addressStringAware);
		final List<Person> persons = new ArrayList<>();
		persons.add(Mockito.mock(Person.class));
		Mockito.when(personService.persons(person, addressStringAware, contact)).thenReturn(persons);
		personSearchController.assignPersons(model);
		Mockito.verify(model, Mockito.times(1)).setPersons(persons);
	}

}