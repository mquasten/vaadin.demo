package de.mq.phone.web.person;

import java.util.Collection;

import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Container;

import de.mq.phone.domain.person.Contact;

public interface ContactMapper extends Converter<Collection<Contact>,Container> {

	Container convert(Collection<Contact> contacts);

}