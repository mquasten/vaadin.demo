package de.mq.phone.web.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;



import java.util.UUID;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;

import com.vaadin.data.Property;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;


import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.support.PersonEntities;

public class ContactMapperTest {
	
	

	private static final String CONTACT = "contact";

	private static final String SUBSCRIBER_NUMBER = "subscriberNumber";

	private static final String NATIONAL_DESTINATION_CODE = "nationalDestinationCode";

	private static final String COUNTRY_CODE = "countryCode";

	private static final String SN = "19680528";

	private static final String NDC = "2434";


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
	
	
	@Test
	public final void contactToMapPhone() {
	
		final Contact  contact= BeanUtils.instantiateClass(PersonEntities.ContactType.Phone.type()); 
		final UUID uuid = UUID.randomUUID();
		@SuppressWarnings("unchecked")
		final Entry<UUID, Contact> entry = Mockito.mock(Entry.class);
		ReflectionTestUtils.setField(contact, CONTACT, "xxx");
		
		ReflectionTestUtils.setField(contact, NATIONAL_DESTINATION_CODE, NDC);
		ReflectionTestUtils.setField(contact, SUBSCRIBER_NUMBER, SN);
		
		Mockito.when(entry.getKey()).thenReturn(uuid);
		Mockito.when(entry.getValue()).thenReturn(contact);
		
		final Map<String,Object> results = contactMapper.contactToMap(entry); 
		
		Assert.assertEquals("", results.get(COUNTRY_CODE));
		Assert.assertEquals(NDC, results.get(NATIONAL_DESTINATION_CODE));
		Assert.assertEquals(SN, results.get(SUBSCRIBER_NUMBER));
		Assert.assertFalse(results.containsKey(CONTACT));
		
	}
	@Test
	public final void contactToMapMail() {
		final Contact  contact= BeanUtils.instantiateClass(PersonEntities.ContactType.Email.type()); 
		final UUID uuid = UUID.randomUUID();
		@SuppressWarnings("unchecked")
		final Entry<UUID, Contact> entry = Mockito.mock(Entry.class);
		Mockito.when(entry.getKey()).thenReturn(uuid);
		Mockito.when(entry.getValue()).thenReturn(contact);
		ReflectionTestUtils.setField(contact, CONTACT, MAIL);
		final Map<String,Object> results = contactMapper.contactToMap(entry); 
		Assert.assertEquals(MAIL, results.get(CONTACT));
	}
	
	@Test
	public final void contactToMapNull() {
		Assert.assertEquals(0, contactMapper.contactToMap(null).size()); 
	}
	
	@Test
	public final void mapInto() {
		final Contact  contact= BeanUtils.instantiateClass(PersonEntities.ContactType.Email.type()); 
		final Map<String,Object> map = new HashMap<String,Object>();
		map.put(CONTACT, MAIL);
		Assert.assertEquals(contact, contactMapper.mapInto(map, contact));
		Assert.assertEquals(MAIL, contact.contact());
	}
	@Test
	public final void convertLocale() {
		Mockito.when(messageSource.getMessage(ContactMapperImpl.I18N_TYPE_MAIL, null, Locale.GERMAN)).thenReturn(ContactMapperImpl.I18N_TYPE_MAIL);
		Mockito.when(messageSource.getMessage(ContactMapperImpl.I18N_TYPE_PHONE, null, Locale.GERMAN)).thenReturn(ContactMapperImpl.I18N_TYPE_PHONE);
		final Container container = contactMapper.convert(Locale.GERMAN);
		
		Assert.assertTrue(container.getItemIds().contains(PersonEntities.ContactType.Email.type()));
		Assert.assertTrue(container.getItemIds().contains(PersonEntities.ContactType.Phone.type()));
		
		Assert.assertEquals(ContactMapperImpl.I18N_TYPE_MAIL, container.getItem(PersonEntities.ContactType.Email.type()).getItemProperty(ContactMapperImpl.TYPE_PROPERTY).getValue());
		Assert.assertEquals(ContactMapperImpl.I18N_TYPE_PHONE,container.getItem(PersonEntities.ContactType.Phone.type()).getItemProperty(ContactMapperImpl.TYPE_PROPERTY).getValue());
	}
	
	@Test
	public final void  mapIntoContainer() {
		final Contact  contact= BeanUtils.instantiateClass(PersonEntities.ContactType.Email.type()); 
		ReflectionTestUtils.setField(contact, CONTACT, MAIL);
		final UUID uuid = UUID.randomUUID();
		@SuppressWarnings("unchecked")
		final Entry<UUID, Contact> entry = Mockito.mock(Entry.class);
		Mockito.when(entry.getKey()).thenReturn(uuid);
		Mockito.when(entry.getValue()).thenReturn(contact);
		final Container container = new IndexedContainer();
		container.addContainerProperty(PersonEditView.CONTACT_DOMAIN_PROPERTY, Entry.class, null);
		container.addContainerProperty(PersonEditView.CONTACT_STRING_PROPERTY, String.class, "");
		Assert.assertEquals(container,contactMapper.mapInto(entry, container ));
		
		Assert.assertEquals(entry, container.getItem(uuid).getItemProperty(PersonEditView.CONTACT_DOMAIN_PROPERTY).getValue());
		Assert.assertEquals(MAIL, container.getItem(uuid).getItemProperty(PersonEditView.CONTACT_STRING_PROPERTY).getValue());
		
	}
	@SuppressWarnings("unchecked")
	@Test
	public final void  mapIntoContainerTakeOverValue() {
		final Contact  contact= BeanUtils.instantiateClass(PersonEntities.ContactType.Email.type()); 
		ReflectionTestUtils.setField(contact, CONTACT, MAIL);
		final UUID uuid = UUID.randomUUID();
		
		final Entry<UUID, Contact> entry = Mockito.mock(Entry.class);
		Mockito.when(entry.getKey()).thenReturn(uuid);
		Mockito.when(entry.getValue()).thenReturn(contact);
		
		
		final Entry<UUID,Contact> existing = Mockito.mock(Entry.class);
		final Container container = new IndexedContainer();
		container.addContainerProperty(PersonEditView.CONTACT_DOMAIN_PROPERTY, Entry.class, null);
		container.addContainerProperty(PersonEditView.CONTACT_STRING_PROPERTY, String.class, "");
		final Item item = container.addItem(uuid);
		item.getItemProperty(PersonEditView.CONTACT_DOMAIN_PROPERTY).setValue(existing);
		item.getItemProperty(PersonEditView.CONTACT_STRING_PROPERTY).setValue("xxx");;
		
		
		Assert.assertEquals(container,contactMapper.mapInto(entry, container ));
		Assert.assertEquals(existing, container.getItem(uuid).getItemProperty(PersonEditView.CONTACT_DOMAIN_PROPERTY).getValue());
		Assert.assertEquals(MAIL, container.getItem(uuid).getItemProperty(PersonEditView.CONTACT_STRING_PROPERTY).getValue());
	}

}
