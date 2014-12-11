package de.mq.phone.web.person;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.GeoCoordinates;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.support.BankingAccount;

public class PersonListToItemContainerConverterTest {

	private final Converter<Collection<Person>, Container> converter = new PersonListToItemContainerConverter();
	
	@Test
	public final void convert() {
		final Collection<Person> persons = new ArrayList<>();
		final Person person = Mockito.mock(Person.class);
		Mockito.when(person.person()).thenReturn("Kylie Minogue");
		Mockito.when(person.id()).thenReturn("19680528");
		final Address address = Mockito.mock(Address.class);
		Mockito.when(person.address()).thenReturn(address);
		final Contact contact = Mockito.mock(Contact.class);
		Mockito.when(contact.contact()).thenReturn("kinky.kylie@minogue.de");
		final Collection<Contact> contacts = new ArrayList<>();
		contacts.add(contact);
		Mockito.when(person.contacts()).thenReturn(contacts);
		
		final GeoCoordinates geoCoordinates = Mockito.mock(GeoCoordinates.class);
		Mockito.when(address.coordinates()).thenReturn(geoCoordinates);
		Mockito.when(person.hasGeoCoordinates()).thenReturn(true);
		
		final BankingAccount bankingAccount = Mockito.mock(BankingAccount.class);
		Mockito.when(bankingAccount.account()).thenReturn("bankingAccountString");
		
		Mockito.when(person.bankingAccount()).thenReturn(bankingAccount);
		persons.add(person);
		final Container container = converter.convert(persons);
		
		Assert.assertEquals(1, container.getItemIds().size());
		final String id = (String) container.getItemIds().iterator().next();
		Assert.assertEquals(person.id(), id);
		final Item item = container.getItem(id);
		Assert.assertEquals(5, item.getItemPropertyIds().size());
		Assert.assertTrue(item.getItemPropertyIds().contains(PersonSearchView.PERSON));
		Assert.assertTrue(item.getItemPropertyIds().contains(PersonSearchView.ADDRESS));
		Assert.assertTrue(item.getItemPropertyIds().contains(PersonSearchView.CONTACTS));
		Assert.assertTrue(item.getItemPropertyIds().contains(PersonSearchView.BANKING_ACCOUNT));
		
		Assert.assertEquals(person.person(), item.getItemProperty(PersonSearchView.PERSON).getValue());
		Assert.assertEquals(person.address().address(), item.getItemProperty(PersonSearchView.ADDRESS).getValue());
		
		Assert.assertEquals( 1, ((Collection<?>) item.getItemProperty(PersonSearchView.CONTACTS).getValue()).size());
		Assert.assertEquals(contact.contact(),((Collection<?>) item.getItemProperty(PersonSearchView.CONTACTS).getValue()).iterator().next());
		
		Assert.assertEquals(bankingAccount.account(), item.getItemProperty(PersonSearchView.BANKING_ACCOUNT).getValue());
		
		Assert.assertEquals(geoCoordinates, item.getItemProperty(PersonSearchView.COORDINATES).getValue());
	}
	
	@Test
	public final void convertAllEmpty() {
		final Collection<Person> persons = new ArrayList<>();
		final Person person = Mockito.mock(Person.class);
		Mockito.when(person.id()).thenReturn("19680528");
		persons.add(person);
		final Container container = converter.convert(persons);
		final Item item = container.getItem(person.id());
		for(final Object itemId : item.getItemPropertyIds()){
			
				final Object value = item.getItemProperty(itemId).getValue();
				if (value instanceof Collection) {
					Assert.assertTrue(((Collection<?>) value).isEmpty());
					continue;
				}
				if (value instanceof String) {
					Assert.assertEquals("", value);
					continue;
				}
				Assert.assertNull(value);
		}
				
	}

}
