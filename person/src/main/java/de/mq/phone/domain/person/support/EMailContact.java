package de.mq.phone.domain.person.support;

import de.mq.phone.domain.person.Contact;

 class EMailContact implements Contact {
	
	private String contact;
	
	EMailContact(final String contact) {
		
		this.contact=contact;
	}
	
	@SuppressWarnings("unused")
	private EMailContact() {
		
	}

	@Override
	public String contact() {
		return contact;
	}

}
