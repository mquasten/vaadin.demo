package de.mq.phone.web.person;

import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.support.PersonEntities;

@Component
class ContactMapperImpl implements ContactMapper {

	static final String TYPE_PROPERTY = "type";
	static final String I18N_TYPE_MAIL = "contact_type_mail";
	static final String I18N_TYPE_PHONE = "contact_type_phone";
	private final MessageSource messageSource;

	@Autowired
	ContactMapperImpl(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}

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
			item.getItemProperty(PersonEditView.CONTACT_DOMAIN_PROPERTY).setValue(new AbstractMap.SimpleEntry<UUID, Contact>(id, contact));

		});
		return ic;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final Collection<Contact> convert(final Container container) {
		return Collections.unmodifiableSet(container.getItemIds().stream().map(id -> {
			return ((Entry<?, Contact>) container.getItem(id).getItemProperty(PersonEditView.CONTACT_DOMAIN_PROPERTY).getValue()).getValue();
		}).collect(Collectors.toSet()));

	}

	private UUID newId() {
		return new UUID(Math.round(UUID_RANDOM_SCALE * Math.random()), Math.round(UUID_RANDOM_SCALE * Math.random()));
	}

	@Override
	public Map<String, Object> contactToMap(final Entry<UUID, Contact> entry) {
		final Map<String, Object> results = new HashMap<>();
		if (entry == null) {
			return results;
		}
		ReflectionUtils.doWithFields(entry.getValue().getClass(), field -> {
			field.setAccessible(true);
			Object value = field.get(entry.getValue());
			if (value == null) {
				value = "";
			}
			results.put(field.getName(), value);
		}, field -> {
			return field.getType().equals(String.class);
		});

		if (!PersonEntities.isMailContact(entry.getValue())) {
			results.remove(PersonEditView.CONTACT_DOMAIN_PROPERTY);
		}

		return results;
	}

	@Override
	public Contact mapInto(final Map<String, ?> map, final Contact contact) {
		ReflectionUtils.doWithFields(contact.getClass(), field -> {
			field.setAccessible(true);
			field.set(contact, map.get(field.getName()));
		}, field -> {
			return ! Modifier.isStatic(field.getModifiers()); } );
		return contact;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Container convert(final Locale locale) {
		final Container ic = new IndexedContainer();

		ic.addContainerProperty(PersonEditView.CONTACT_TYPE_PROPERTY, String.class, "");

		ic.addItem(PersonEntities.ContactType.Phone.type()).getItemProperty(TYPE_PROPERTY).setValue(messageSource.getMessage(I18N_TYPE_PHONE, null, locale));
		ic.addItem(PersonEntities.ContactType.Email.type()).getItemProperty(TYPE_PROPERTY).setValue(messageSource.getMessage(I18N_TYPE_MAIL, null, locale));
		return ic;
	}

	@Override
	@SuppressWarnings("unchecked")
	public final Container mapInto(final Entry<UUID, Contact> model, final Container container) {
		Item item = container.getItem(model.getKey());
		if (item == null) {
			item = container.addItem(model.getKey());
			item.getItemProperty(PersonEditView.CONTACT_DOMAIN_PROPERTY).setValue(model);
		}
		item.getItemProperty(PersonEditView.CONTACT_STRING_PROPERTY).setValue(model.getValue().contact());
		return container;

	}

}
