package de.mq.phone.domain.person.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.BeanUtils;

import de.mq.phone.domain.person.Contact;

public class EMailContactTest {
	
	private static final String MAIL = "kinky@Kylie.uk";

	@Test
	public final void create() {
		
		final Contact contact = new EMailContact(MAIL);
		Assert.assertEquals(MAIL, contact.contact());
	}
	@Test
	public final void defaultConstructor() {
		final Contact contact = BeanUtils.instantiateClass(EMailContact.class, Contact.class);
		Assert.assertNull(contact.contact());
	}

}
