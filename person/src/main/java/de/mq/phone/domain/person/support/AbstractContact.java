package de.mq.phone.domain.person.support;

import org.springframework.util.StringUtils;

import de.mq.phone.domain.person.Contact;
import de.mq.vaadin.util.BeforeSave;

abstract class AbstractContact {
	
	protected String contact;

	@BeforeSave
	public final void beforeSave() {
		this.contact=StringUtils.trimAllWhitespace(contact());
	}

	@Override
	public final int hashCode() {
		final String contact = contact();
		if( ! StringUtils.hasText(contact) ) {
			return super.hashCode();
		}
		return contact.hashCode();
	}

	protected abstract String contact();
	@Override
	public final boolean equals(final Object obj) {
		if (!(obj instanceof Contact)) {
			return super.equals(obj);
		}
		
		final Contact otherContact = (Contact) obj;
		
		return contact().equals(otherContact.contact());
		
	}

}