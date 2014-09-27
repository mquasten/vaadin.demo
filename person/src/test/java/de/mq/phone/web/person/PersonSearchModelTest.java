package de.mq.phone.web.person;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.vaadin.util.Observer;

public class PersonSearchModelTest {
	private final PersonSearchModel personSearchModel = new PersonSearchModelImpl();
	
	@Test
	public final void setPersons() {
		
		@SuppressWarnings("unchecked")
		final Observer<PersonSearchModel, PersonSearchModel.EventType> observer = Mockito.mock( Observer.class);
		personSearchModel.register(observer, PersonSearchModel.EventType.PersonsChanges);
	
		final List<Person> persons = new ArrayList<>();
		persons.add(Mockito.mock(Person.class));
		personSearchModel.setPersons(persons);
		
		Assert.assertEquals(persons, personSearchModel.getPersons());
		
		Mockito.verify(observer, Mockito.times(1)).process(personSearchModel, PersonSearchModel.EventType.PersonsChanges);
	}
	
	@Test
	public final void searchCriteriaPerson() {
		
		final Person person = Mockito.mock(Person.class);
		personSearchModel.setSearchCriteria(person);
		Assert.assertEquals(person, personSearchModel.getSearchCriteriaPerson());
	}
	
	@Test
	public final void searchCriteriaContact() {
		final Contact contact = Mockito.mock(Contact.class);
		personSearchModel.setSearchCriteria(contact);
		Assert.assertEquals(contact, personSearchModel.getSearchCriteriaContact());
	}
	
	@Test
	public final void searchCriteriaAddress() {
		final AddressStringAware address = Mockito.mock(AddressStringAware.class);
		personSearchModel.setSearchCriteria(address);
		Assert.assertEquals(address, personSearchModel.getSearchCriteriaAddress());
	
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void searchCriteriaWrongType(){
		personSearchModel.setSearchCriteria(new Date());
	}
	
	@Test
	public final void enums() {
		for(final PersonSearchModel.EventType eventType : PersonSearchModel.EventType.values()){
			PersonSearchModel.EventType.valueOf(eventType.name());
		}
	}
	
}
