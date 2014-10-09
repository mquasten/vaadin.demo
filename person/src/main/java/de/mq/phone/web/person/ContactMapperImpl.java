package de.mq.phone.web.person;

import java.util.Collection;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.support.PersonEntities;

@Component
public class ContactMapperImpl implements ContactMapper {

	private static final double UUID_RANDOM_SCALE = 1e18;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.phone.web.person.ContactMapper#convert(java.util.Collection)
	 */

	@SuppressWarnings("unchecked")
	@Override
	public Container convert(final Collection<Contact> contacts) {
		final Container ic = new IndexedContainer();
		ic.addContainerProperty(PersonEditView.CONTACT_STRING_PROPERTY, String.class, "");
		ic.addContainerProperty(PersonEditView.CONTACT_ID_PROPERTY, UUID.class, null);
		ic.addContainerProperty(PersonEditView.CONTACT_DOMAIN_PROPERTY, Contact.class, PersonEntities.newContact(""));
		contacts.forEach(contact -> {
			final UUID id = newId();
			final Item item = ic.addItem(id);
			item.getItemProperty(PersonEditView.CONTACT_STRING_PROPERTY).setValue(contact.contact());
			item.getItemProperty(PersonEditView.CONTACT_DOMAIN_PROPERTY).setValue(contact);
			item.getItemProperty(PersonEditView.CONTACT_ID_PROPERTY).setValue(id);
		});
		return ic;
	}

	private UUID newId() {
		return new UUID(Math.round(UUID_RANDOM_SCALE * Math.random()), Math.round(UUID_RANDOM_SCALE * Math.random()));
	}

}
