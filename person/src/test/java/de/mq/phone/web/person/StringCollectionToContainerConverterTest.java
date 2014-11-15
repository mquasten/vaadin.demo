package de.mq.phone.web.person;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Container;

public class StringCollectionToContainerConverterTest {

	private static final String CONTACT = "madonna@mdna.net";
	private final Converter<Collection<String>, Container> converter = new StringCollectionToContainerConverter();
	@Test
	public final void convert() {
		final Collection<String> contacts = new ArrayList<>();
		contacts.add(CONTACT);
		final Container result = converter.convert(contacts);
		Assert.assertEquals(1, result.getItemIds().size());
		Assert.assertEquals(CONTACT, result.getItem(result.getItemIds().iterator().next()).getItemProperty(PersonSearchView.CONTACTS).getValue());
	}
}
