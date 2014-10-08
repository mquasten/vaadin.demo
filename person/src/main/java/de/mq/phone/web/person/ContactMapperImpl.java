package de.mq.phone.web.person;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;

import de.mq.phone.domain.person.Contact;
import de.mq.phone.web.person.PersonEditView.Fields;
@Component
public class ContactMapperImpl implements ContactMapper {

	/* (non-Javadoc)
	 * @see de.mq.phone.web.person.ContactMapper#convert(java.util.Collection)
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Container convert(final Collection<Contact> contacts) {
		final Container ic = new IndexedContainer();		
		ic.addContainerProperty(Fields.Contacts.property(), String.class, "");
		contacts.forEach(contact -> ic.addItem(contact).getItemProperty(Fields.Contacts.property()).setValue(contact.contact()));
		return ic;
	}

	
	
}
