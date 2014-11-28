package de.mq.phone.domain.person.support;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;


import org.springframework.test.util.ReflectionTestUtils;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;



public class PersonTest {
	
	private static final String ADDRESS = "address";
	private static final String ID = "19680528";
	private static final String FIRSTNAME = "Kylie";
	private static final String NAME = "Minogue";

	@Test
	public void createWithName() {
		final Person person = new PersonImpl(NAME); 
		Assert.assertEquals(NAME, person.name());
	}
	
	@Test
	public void createWithNameAndFirstname() {
		final Person person = new PersonImpl(NAME, FIRSTNAME); 
		Assert.assertEquals(FIRSTNAME, person.firstname());
		Assert.assertEquals(NAME, person.name());
	}
	
	@Test
	public final void createDefaultConstructor() {
		final Person person =  BeanUtils.instantiateClass(PersonImpl.class, Person.class);
		Assert.assertNull(person.firstname());
		Assert.assertNull(person.name());
	}
	
	@Test
	public final void contacts() {
		final Person person = new PersonImpl(NAME); 
		final Contact contact = Mockito.mock(Contact.class);
		person.assign(contact);
		final Collection<Contact> results = person.contacts();
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(contact, results.iterator().next());
		
	}
	
	@Test
	public final void id(){
		final Person person = new PersonImpl(NAME); 
		ReflectionTestUtils.setField(person, "id", ID);
		Assert.assertEquals(ID, person.id());
	}
	
	@Test
	public final void address() {
		final Person person = new PersonImpl(NAME); 
		Assert.assertNull(person.address());
		final Address address = Mockito.mock(Address.class);
		person.assign(address);
		Assert.assertEquals(address, person.address());
	}
	
	@Test
	public final void bankingAccount() {
		final Person person = new PersonImpl(NAME); 
		Assert.assertNull(person.bankingAccount());
		final BankingAccount bankingAccount = Mockito.mock(BankingAccount.class);
		person.assign(bankingAccount);
		Assert.assertEquals(bankingAccount, person.bankingAccount());
	}
	
	@Test
	public final void alias() {
		final Person person = new PersonImpl(NAME,null, FIRSTNAME ); 
		Assert.assertEquals(FIRSTNAME, person.alias());
	}
	
	@Test
	public final void person() {
		Assert.assertEquals(String.format("%s %s", FIRSTNAME, NAME), new PersonImpl(NAME,FIRSTNAME ).person());
		Assert.assertEquals(NAME, new PersonImpl(NAME).person());
		Assert.assertEquals(String.format("%s %s (%s)", FIRSTNAME, NAME, FIRSTNAME), new PersonImpl(NAME , FIRSTNAME, FIRSTNAME).person());
		Assert.assertEquals(String.format("(%s)", FIRSTNAME), new PersonImpl(null,null,FIRSTNAME).person());
	}
	
	@Test
	public final void beforeSave() {
		final Person person = new PersonImpl(NAME, FIRSTNAME, FIRSTNAME); 
		Assert.assertNull(ReflectionTestUtils.getField(person, "person"));
		((PersonImpl)person).beforeSave();
		
		Assert.assertEquals(String.format("%s %s (%s)", FIRSTNAME, NAME, FIRSTNAME), ReflectionTestUtils.getField(person, "person"));
	}
	
	@Test
	public final void hash(){
		final Person person = new PersonImpl(null);
		ReflectionTestUtils.setField(person, "id",  ID);
		Assert.assertEquals(ID.hashCode(), person.hashCode());
		final Person nullPerson = new PersonImpl(null);
		Assert.assertEquals(System.identityHashCode(nullPerson), nullPerson.hashCode());
		Assert.assertEquals(NAME.hashCode(), new PersonImpl(NAME).hashCode());
		
	}
	
	@Test
	public final void equals() {
		Assert.assertFalse(new PersonImpl(NAME).equals(NAME));
		
		final Person personWithId = new PersonImpl(null);
		ReflectionTestUtils.setField(personWithId, "id",  ID);
		
		Assert.assertTrue(new PersonImpl(NAME).equals(new PersonImpl(NAME)));
		Assert.assertTrue(personWithId.equals(personWithId));
		Assert.assertFalse(personWithId.equals(new PersonImpl(NAME)));
	}
	
	@Test
	public final void hasAddress() {
		final Person person = new PersonImpl(NAME);
		Assert.assertFalse(person.hasAddress());
		final Address address = Mockito.mock(Address.class);
		Mockito.when(address.address()).thenReturn(ADDRESS);
		person.assign(address);
		Assert.assertTrue(person.hasAddress());
		
	}
	
	@Test
	public final void hasGeoCoordinates() {
		final Person person = new PersonImpl(NAME);
		
		Assert.assertFalse(person.hasGeoCoordinates());
		final Address address = Mockito.mock(Address.class);
		Mockito.when(address.hasGeoCoordinates()).thenReturn(true);
		person.assign(address);
		Assert.assertTrue(person.hasGeoCoordinates());
	}

}
