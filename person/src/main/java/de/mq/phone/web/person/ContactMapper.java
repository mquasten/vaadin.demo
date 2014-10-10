package de.mq.phone.web.person;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Container;

import de.mq.phone.domain.person.Contact;

public interface ContactMapper extends Converter<Collection<Contact>,Container> {

	Container convert(Collection<Contact> contacts);
	
	public Map<String, Object> contactToMap(final Entry<UUID,Contact> source);

}