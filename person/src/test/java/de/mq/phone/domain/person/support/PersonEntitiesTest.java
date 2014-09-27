package de.mq.phone.domain.person.support;

import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StringUtils;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonStringAware;

public class PersonEntitiesTest {
	
	private static final String ADDRESS = "addressString";
	private static final String CONTACT = "kinky@kylie.uk";
	private static final String PERSON_FIRSTNAME = "Kylie";
	private static final String PERSON_ID = new UUID(19680528L, 19680528L).toString();
	private static final String PERSON_NAME = "Minogue";

	@Test
	public final void id() {
		final Person person = new PersonImpl(PERSON_NAME);
		ReflectionTestUtils.setField(person, "id", PERSON_ID);
		Assert.assertEquals(PERSON_ID, PersonEntities.id(person));
	}

	@Test
	public final void newPerson() {
		final Person person = PersonEntities.newPerson();
		Assert.assertNull(person.name());
		Assert.assertNull(person.firstname());
		Assert.assertFalse(StringUtils.hasText(person.person()));
		Assert.assertNull(PersonEntities.id(person));
	}
	
	@Test
	public final void newPersonWithName() {
		final PersonStringAware person = PersonEntities.newPerson(PERSON_FIRSTNAME + " " + PERSON_NAME);
		Assert.assertEquals(String.format("%s %s", PERSON_FIRSTNAME , PERSON_NAME), person.person());
		
	}
	
	@Test
	public final void newBankingAccount() {
		final BankingAccount bankingAccount = PersonEntities.newBankingAccount();
		Assert.assertNull(bankingAccount.iBan());
		Assert.assertNull(bankingAccount.bankIdentifierCode());
	}
	@Test
	public final void newContact() {
		final Contact contact = PersonEntities.newContact(CONTACT);
		Assert.assertEquals(CONTACT, contact.contact());
	}
	
	@Test
	public final void newAddress() {
		AddressStringAware address = PersonEntities.newAddressStringAware(ADDRESS);
		Assert.assertEquals(ADDRESS, address.address());
	}
	
	@Test(expected=BeanInstantiationException.class)
	public final void create() {
		 Assert.assertNull(BeanUtils.instantiateClass(PersonEntities.class, PersonEntities.class));
	}
	

}
