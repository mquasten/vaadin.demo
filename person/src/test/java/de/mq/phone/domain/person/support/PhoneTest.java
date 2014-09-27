package de.mq.phone.domain.person.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.phone.domain.person.Contact;

public class PhoneTest {
	
	private static final String SN = "19680528";
	private static final String NDC = "2434";
	private static final String CC = "49";

	@Test
	public final void createInternational() {
		final Contact phone = new PhoneImpl(CC ,NDC , SN  );
		
		Assert.assertEquals("+" +CC+ " "  +NDC+ " " +SN, phone.contact());
	}
	@Test
	public final void createNational() {
		final Contact phone = new PhoneImpl(NDC ,  SN  );
		Assert.assertEquals("0" + NDC + " " + SN , phone.contact());
	}
	
	@Test
	public final void local() {
		final Contact phone = new PhoneImpl(SN  );
		Assert.assertEquals( SN , phone.contact());
		
	}
	
	@Test
	public final void empty(){
		Contact contact = BeanUtils.instantiateClass(PhoneImpl.class, Contact.class);
		Assert.assertTrue(contact.contact().isEmpty());
	}
	@Test
	public final void  zeros() {
		final Contact phone = new PhoneImpl("00" + CC ,"0" + NDC , "0"+ SN  );
		Assert.assertEquals("+" +CC+ " "  +NDC+ " " +SN, phone.contact());
	}
	
	@Test
	public final void beforeSave() {
		final Contact phone = new PhoneImpl(CC ,NDC , SN  );
		Assert.assertNull(ReflectionTestUtils.getField(phone, "contact"));
		((AbstractContact)phone).beforeSave();
		Assert.assertEquals("+" +CC+ " "  +NDC+ " " +SN, phone.contact());
	}
	
	@Test
	public final void hash() {
		final Contact phone = new PhoneImpl(CC ,NDC , SN  );
		Assert.assertEquals(("+" +CC+ " "  +NDC+ " " +SN).hashCode(), phone.hashCode());
		final Contact nullContact = new PhoneImpl(null  );
		Assert.assertEquals(System.identityHashCode(nullContact), nullContact.hashCode());
	}
	
	@Test
	public final void equals() {
		final Contact phone = new PhoneImpl(CC ,NDC , SN  );
		final Contact otherPhone = new PhoneImpl(CC ,NDC , SN  );
		final Contact nullPhone = new PhoneImpl(null);
		
		Assert.assertTrue(phone.equals(otherPhone));
		
		Assert.assertFalse( nullPhone.equals(nullPhone.contact()));
	}

}
