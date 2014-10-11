package de.mq.phone.web.person;

import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.support.PersonEntities;
import de.mq.vaadin.util.SubjectImpl;

@Component
@Scope("session")
class PersonEditModelImpl extends SubjectImpl<PersonEditModel, PersonEditModel.EventType> implements PersonEditModel {
	private Person person;
	
	private Entry<UUID,Contact> currentContact;
	
	@Override
	public final void setCurrentContact(final Entry<UUID,Contact> currentContact){
		this.currentContact=currentContact;
		notifyObservers(EventType.ContactsChanged);
	}
	@Override
	public final Entry<UUID,Contact> getSelectedContact(){
		return this.currentContact;
	}

	/* (non-Javadoc)
	 * @see de.mq.phone.web.person.PersonEditModel#setPerson(de.mq.phone.domain.person.Person)
	 */
	@Override
	public final void setPerson(final Person person) {
		this.person = person;
		notifyObservers(EventType.PersonChanged);
	}

	/* (non-Javadoc)
	 * @see de.mq.phone.web.person.PersonEditModel#getPerson()
	 */
	@Override
	public final Person getPerson() {
		return person;
	}
	
	@Override
	public final boolean isMailContact()  {
		if( currentContact==null){
			return false;
		}
		return PersonEntities.isMailContact(currentContact.getValue());
	}
	@Override
	public final boolean isPhoneContact()  {
		if( currentContact==null){
			return false;
		}
		return PersonEntities.isPhoneContact(currentContact.getValue());
	}
}
