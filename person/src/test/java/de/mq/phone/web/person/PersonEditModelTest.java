package de.mq.phone.web.person;

import java.util.Map.Entry;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.support.PersonEntities;
import de.mq.phone.web.person.PersonEditModel.EventType;
import de.mq.vaadin.util.Observer;


public class PersonEditModelTest {
	
	private final PersonEditModel personEditModel = new PersonEditModelImpl();

	@SuppressWarnings("unchecked")
	private final Observer<EventType> observer = Mockito.mock(Observer.class);
	

	@Test
	public final void currentContact() {
		@SuppressWarnings("unchecked")
		final Entry<UUID, Contact> currentContact = Mockito.mock(Entry.class);
		personEditModel.register(observer, EventType.ContactChanged);
		personEditModel.setCurrentContact(currentContact);
		Assert.assertEquals(currentContact, personEditModel.getCurrentContact());
		Mockito.verify(observer, Mockito.times(1)).process(EventType.ContactChanged);
	}
	
	@Test
	public final void person() {
		final Person person = Mockito.mock(Person.class);
		personEditModel.register(observer, EventType.PersonChanged);
		personEditModel.setPerson(person);
		Assert.assertEquals(person, personEditModel.getPerson());
		Mockito.verify(observer, Mockito.times(1)).process(EventType.PersonChanged);
	}
	
	@Test
	public final void isMailContact() {
		@SuppressWarnings("unchecked")
		final Entry<UUID, Contact> currentContact = Mockito.mock(Entry.class);
		Mockito.when(currentContact.getValue()).thenReturn(BeanUtils.instantiateClass(PersonEntities.ContactType.Email.type()));
		personEditModel.setCurrentContact(currentContact);
		Assert.assertTrue(personEditModel.isMailContact());
		personEditModel.setCurrentContact((Entry<UUID, Contact>) null);
		Assert.assertFalse(personEditModel.isMailContact());
		
		
	}
	
	@Test
	public final void isPhoneContact() {
		@SuppressWarnings("unchecked")
		final Entry<UUID, Contact> currentContact = Mockito.mock(Entry.class);
		Mockito.when(currentContact.getValue()).thenReturn(BeanUtils.instantiateClass(PersonEntities.ContactType.Phone.type()));
		personEditModel.setCurrentContact(currentContact);
		Assert.assertTrue(personEditModel.isPhoneContact());
		personEditModel.setCurrentContact((Entry<UUID, Contact>) null);
		Assert.assertFalse(personEditModel.isPhoneContact());
	}
	
	@Test
	public final void setCurrentContact() {
		@SuppressWarnings("unchecked")
		final Entry<UUID, Contact> currentContact = Mockito.mock(Entry.class);
		ReflectionTestUtils.setField(personEditModel, "currentContact", currentContact);
		final Contact contact = Mockito.mock(Contact.class);
		personEditModel.register(observer, EventType.ContactTakeOver);
		personEditModel.setCurrentContact(contact);
		
		Mockito.verify(currentContact, Mockito.times(1)).setValue(contact);
		
		Mockito.verify(observer,  Mockito.times(1)).process(EventType.ContactTakeOver);
		
	}
	
	@Test
	public final void eventTypes() {
		for(final EventType type : EventType.values() ) {
			Assert.assertEquals(type, EventType.valueOf(type.name()));
		}
	}
	
}
