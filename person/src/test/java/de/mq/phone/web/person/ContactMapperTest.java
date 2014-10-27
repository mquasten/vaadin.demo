package de.mq.phone.web.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;



import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import com.vaadin.data.Property;
import com.vaadin.data.Container;
import com.vaadin.data.Item;



import de.mq.phone.domain.person.Contact;

public class ContactMapperTest {
	
	private static final String MAIL = "kinky@kylie.tv";

	private final MessageSource messageSource = Mockito.mock(MessageSource.class);
	
	private final ContactMapper contactMapper = new ContactMapperImpl(messageSource);
	
	
	@Test
	public final void convert() {
		
		final Collection<Contact> contacts = new ArrayList<>();
		final Contact contact = Mockito.mock(Contact.class);
		Mockito.when(contact.contact()).thenReturn(MAIL);
		contacts.add(contact);
		Container container = contactMapper.convert(contacts);
		Assert.assertEquals(1, container.getItemIds().size());
		final Object id = container.getItemIds().iterator().next();
		Assert.assertEquals(2,container.getItem(id).getItemPropertyIds().size());
		
		Assert.assertTrue(container.getItem(id).getItemPropertyIds().contains(PersonEditView.CONTACT_STRING_PROPERTY));
		Assert.assertTrue(container.getItem(id).getItemPropertyIds().contains(PersonEditView.CONTACT_DOMAIN_PROPERTY));
		
		Assert.assertEquals(MAIL, container.getItem(id).getItemProperty(PersonEditView.CONTACT_STRING_PROPERTY).getValue());
		
		final Entry<?, ?> entry =  (Entry<?, ?>) container.getItem(id).getItemProperty(PersonEditView.CONTACT_DOMAIN_PROPERTY).getValue();
		Assert.assertEquals(id, entry.getKey());
		Assert.assertEquals(contact, entry.getValue());
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void convertToDomain() {
		
		final Container container = Mockito.mock(Container.class);
		@SuppressWarnings("rawtypes")
		final Collection itemIds = new ArrayList<>();
		final UUID id = UUID.randomUUID();
		itemIds.add(id);
		Mockito.when(container.getItemIds()).thenReturn(itemIds);
		Item item = Mockito.mock(Item.class);
	
		Mockito.when(container.getItem(id)).thenReturn(item);

		@SuppressWarnings("rawtypes")
		final Property property = Mockito.mock(Property.class);
		final Entry<?,Contact> entry = Mockito.mock(Entry.class);
		Contact contact = Mockito.mock(Contact.class);
		Mockito.when(entry.getValue()).thenReturn(contact);
		Mockito.when(property.getValue()).thenReturn(entry);
		
		Mockito.when(item.getItemProperty(PersonEditView.CONTACT_DOMAIN_PROPERTY)).thenReturn( property);
		
		final Collection<Contact> result = contactMapper.convert(container);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(contact, result.iterator().next());
		
	}

}
