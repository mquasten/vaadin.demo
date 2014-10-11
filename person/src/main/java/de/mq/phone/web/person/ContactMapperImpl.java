package de.mq.phone.web.person;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

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
		ic.addContainerProperty(PersonEditView.CONTACT_DOMAIN_PROPERTY, Entry.class, null);
		
		contacts.forEach(contact -> {
			final UUID id = newId();
			final Item item = ic.addItem(id);
			item.getItemProperty(PersonEditView.CONTACT_STRING_PROPERTY).setValue(contact.contact());
			item.getItemProperty(PersonEditView.CONTACT_DOMAIN_PROPERTY).setValue(new AbstractMap.SimpleEntry<UUID,Contact>(newId(), contact) );
			
		});
		return ic;
	}

	private UUID newId() {
		return new UUID(Math.round(UUID_RANDOM_SCALE * Math.random()), Math.round(UUID_RANDOM_SCALE * Math.random()));
	}

	@Override
	public Map<String, Object> contactToMap(final Entry<UUID,Contact> entry) {
		final Map<String, Object> results = new HashMap<>();
		if(entry == null){
			return results;
		}
		ReflectionUtils.doWithFields(entry.getValue().getClass(), field -> { field.setAccessible(true); results.put(field.getName(), field.get(entry.getValue()));}, field -> {
			return field.getType().equals(String.class);
			});
		
		if( ! PersonEntities.isMailContact(entry.getValue()) ) {
			results.remove(PersonEditView.CONTACT_DOMAIN_PROPERTY);
		}
		return results;
	}
	
	

}
