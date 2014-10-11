package de.mq.phone.web.person;

import java.util.Map.Entry;
import java.util.UUID;

import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.vaadin.util.Subject;

interface PersonEditModel extends  Subject<PersonEditModel, PersonEditModel.EventType > {
	enum EventType {
		PersonChanged,
		ContactsChanged;
	}
	
	void setPerson(final Person person);

	Person getPerson();

	void setCurrentContact(final Entry<UUID, Contact> currentContact);

	Entry<UUID, Contact> getSelectedContact();

	boolean isMailContact();

	boolean isPhoneContact();

}