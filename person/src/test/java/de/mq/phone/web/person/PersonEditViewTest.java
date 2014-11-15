package de.mq.phone.web.person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.support.PersonEntities;
import de.mq.phone.web.person.UserModel.EventType;
import de.mq.vaadin.util.BindingResultsToFieldGroupMapper;
import de.mq.vaadin.util.Observer;
import de.mq.vaadin.util.TestConstants;
import de.mq.vaadin.util.VaadinOperations;
import de.mq.vaadin.util.ViewNav;

public class PersonEditViewTest {

	private static final String ID = "19680528";
	private static final String CHANGED_CONTACT = "girlGoneWild@mdna.tv";
	private static final String CONTACT_AS_STRING = "madonna@mdna.tv";
	private final PersonEditController personEditController = Mockito.mock(PersonEditController.class);
	private final PersonEditModel personEditModel = Mockito.mock(PersonEditModel.class);
	private final UserModel userModel = Mockito.mock(UserModel.class);
	private final ViewNav viewNav = Mockito.mock(ViewNav.class);
	private final BindingResultsToFieldGroupMapper bindingResultMapper = Mockito.mock(BindingResultsToFieldGroupMapper.class);
	private final MessageSource messageSource = Mockito.mock(MessageSource.class);
	private ContactMapper contactMapper = Mockito.mock(ContactMapper.class); ;
	private final ContactEditorView contactEditor = Mockito.mock(ContactEditorView.class);
	private final ClickEvent clickEvent = Mockito.mock(ClickEvent.class);
	private VaadinOperations vaadinOperations = Mockito.mock(VaadinOperations.class);
	
	private  PersonEditView personEditView = new PersonEditView(personEditController, personEditModel, userModel, viewNav, bindingResultMapper, messageSource, contactMapper, contactEditor, vaadinOperations);; 

	private final Map<String, Component> components = new HashMap<>();
	private Observer<UserModel.EventType> localeObserver;

	Map<PersonEditModel.EventType, Observer<PersonEditModel.EventType>> observers  = new HashMap<>();
	@SuppressWarnings("unchecked")
	@Before
	public final void setup() {
		
		Mockito.when(contactMapper.convert(Mockito.anyCollection())).thenReturn(null);
		
	
		Iterator<Component> it = Mockito.mock(Iterator.class);
		Mockito.when(contactEditor.iterator()).thenReturn(it);
		Mockito.when(userModel.getLocale()).thenReturn(Locale.GERMAN);
		Arrays.stream(PersonEditView.Fields.values()).map(field -> PersonEditView.I18N_EDIT_PERSON_PREFIX + field.property().toLowerCase()).forEach(i18n -> Mockito.when(messageSource.getMessage(i18n, null, Locale.GERMAN)).thenReturn(i18n));

		Mockito.doAnswer(invocation -> localeObserver = (Observer<EventType>) invocation.getArguments()[0]).when(userModel).register(Mockito.any(Observer.class), Mockito.any(UserModel.EventType.class));
		Mockito.doAnswer(invocation -> observers.put((PersonEditModel.EventType)invocation.getArguments()[1],  (Observer<PersonEditModel.EventType>)invocation.getArguments()[0])).when(personEditModel).register(Mockito.any(Observer.class), Mockito.any(PersonEditModel.EventType.class));
		
		Mockito.when(messageSource.getMessage(PersonEditView.I18N_EDIT_PERSON_HEADLINE, null, Locale.GERMAN)).thenReturn(PersonEditView.I18N_EDIT_PERSON_HEADLINE);
		Mockito.when(messageSource.getMessage(PersonEditView.I18N_EDIT_PERSON_CANCEL, null, Locale.GERMAN)).thenReturn(PersonEditView.I18N_EDIT_PERSON_CANCEL);
		Mockito.when(messageSource.getMessage(PersonEditView.I18N_EDIT_PERSON_SAVE, null, Locale.GERMAN)).thenReturn(PersonEditView.I18N_EDIT_PERSON_SAVE);
		Mockito.when(messageSource.getMessage(PersonEditView.I18N_CONTACT_TYPE, null, Locale.GERMAN)).thenReturn(PersonEditView.I18N_CONTACT_TYPE);
		Mockito.when(messageSource.getMessage(PersonEditView.I18N_CONTACT_ADD, null, Locale.GERMAN)).thenReturn(PersonEditView.I18N_CONTACT_ADD);
		Mockito.when(messageSource.getMessage(ContactMapperImpl.I18N_TYPE_PHONE, null, Locale.GERMAN)).thenReturn(ContactMapperImpl.I18N_TYPE_PHONE);
		Mockito.when(messageSource.getMessage(ContactMapperImpl.I18N_TYPE_MAIL, null, Locale.GERMAN)).thenReturn(ContactMapperImpl.I18N_TYPE_MAIL);
		Mockito.when(messageSource.getMessage(PersonEditView.I18N_CONTACTS_CAPTION, null, Locale.GERMAN)).thenReturn(PersonEditView.I18N_CONTACTS_CAPTION);
		Mockito.when(messageSource.getMessage(PersonEditView.I18N_DELETE_BUTTON_CAPTION, null, Locale.GERMAN)).thenReturn(PersonEditView.I18N_DELETE_BUTTON_CAPTION);
		
		final Container container =  new ContactMapperImpl(messageSource).convert(Locale.GERMAN);
		Mockito.when(contactMapper.convert(Locale.GERMAN)).thenReturn(container);
		ReflectionTestUtils.setField(personEditView, "contactMapper",contactMapper);
		// ... touched for the very first time ...
		personEditView.init();
		localeObserver.process(UserModel.EventType.LocaleChanges);
		components.clear();
		ComponentTestHelper.components(personEditView, components);
		
	}

	@Test
	public final void init() {
		Assert.assertEquals(16, components.size());

		Arrays.stream(PersonEditView.Fields.values()).map(field -> PersonEditView.I18N_EDIT_PERSON_PREFIX + field.property().toLowerCase()).forEach(i18n -> Assert.assertEquals(i18n, components.get(i18n).getCaption()));

		Arrays.stream(PersonEditView.Fields.values()).map(field -> PersonEditView.I18N_EDIT_PERSON_PREFIX + field.property().toLowerCase()).forEach(i18n -> Assert.assertTrue(components.get(i18n) instanceof TextField));
		Assert.assertEquals(PersonEditView.I18N_EDIT_PERSON_HEADLINE, components.get(PersonEditView.I18N_EDIT_PERSON_HEADLINE).getCaption());
		Assert.assertTrue(components.get(PersonEditView.I18N_EDIT_PERSON_HEADLINE) instanceof Panel);
		Assert.assertTrue(components.get(PersonEditView.I18N_CONTACTS_CAPTION) instanceof ListSelect);
		Assert.assertEquals(PersonEditView.I18N_CONTACT_TYPE, components.get(PersonEditView.I18N_CONTACT_TYPE).getCaption());
		Assert.assertTrue(components.get(PersonEditView.I18N_CONTACT_TYPE) instanceof ComboBox);
		Arrays.stream(new String[] { PersonEditView.I18N_CONTACT_ADD, PersonEditView.I18N_EDIT_PERSON_SAVE, PersonEditView.I18N_EDIT_PERSON_CANCEL }).forEach(i18n -> {
			Assert.assertEquals(i18n, components.get(i18n).getCaption());
			Assert.assertTrue(components.get(i18n) instanceof Button);
		});
		
		final ComboBox comboBox = (ComboBox) components.get(PersonEditView.I18N_CONTACT_TYPE);
		Arrays.stream(PersonEntities.ContactType.values()).map(type -> type.type()).forEach(clazz -> Assert.assertTrue(comboBox.getItemIds().contains(clazz)));;
	   Assert.assertEquals(PersonEntities.ContactType.Phone.type(), comboBox.getValue());
	   
	}
	
	@Test
	public final void initNoContactTypes() {
		Mockito.when(contactMapper.convert(Locale.GERMAN)).thenReturn(null);
		final ComboBox comboBox = (ComboBox) components.get(PersonEditView.I18N_CONTACT_TYPE);
		localeObserver.process(UserModel.EventType.LocaleChanges);
		Assert.assertTrue(comboBox.getItemIds().isEmpty());
		Assert.assertNull(comboBox.getValue());
	}
	
	@Test
	public final void addContact() {
		final UUID uuid = UUID.randomUUID();
		final Button addButton = (Button) components.get(PersonEditView.I18N_CONTACT_ADD);
		final ListSelect listSelect = (ListSelect) components.get(PersonEditView.I18N_CONTACTS_CAPTION);
		listSelect.addItem(uuid);
		listSelect.select(uuid);
		Assert.assertEquals(uuid, listSelect.getValue());
		final ClickListener listener = (ClickListener) addButton.getListeners(ClickEvent.class).iterator().next();
		@SuppressWarnings("unchecked")
		final Entry<UUID, Contact> entry = Mockito.mock(Entry.class);
		Mockito.when(entry.getKey()).thenReturn(uuid);
		Mockito.when(personEditModel.getCurrentContact()).thenReturn(entry);

		listener.buttonClick(clickEvent);

		Assert.assertNull(listSelect.getValue());
		Mockito.verify(personEditController, Mockito.times(1)).assign(personEditModel, PersonEntities.ContactType.Phone.type());
	}
	
	@Test
	public final void addContactNothingSelected() {
		final Button addButton = (Button) components.get(PersonEditView.I18N_CONTACT_ADD);
		final ClickListener listener = (ClickListener) addButton.getListeners(ClickEvent.class).iterator().next();
		
		listener.buttonClick(clickEvent);
		Mockito.verify(personEditController, Mockito.times(1)).assign(personEditModel, PersonEntities.ContactType.Phone.type());
	}

	@Test
	public final void cancel() {
		final Button cancelButton = (Button) components.get(PersonEditView.I18N_EDIT_PERSON_CANCEL);
		final ClickListener listener = (ClickListener) cancelButton.getListeners(ClickEvent.class).iterator().next();
		
		listener.buttonClick(clickEvent);
		Mockito.verify(viewNav,  Mockito.times(1)).navigateTo(PersonSearchView.class);
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void personChanged() {
		final Person person = Mockito.mock(Person.class);
		final Contact  contact = BeanUtils.instantiateClass(PersonEntities.ContactType.Email.type(), Contact.class);
		ReflectionTestUtils.setField(contact, "contact",  CONTACT_AS_STRING);
		final Collection<Contact> contacts = new ArrayList<>();
		contacts.add(contact);
		Mockito.when(person.contacts()).thenReturn(contacts);
		Mockito.when(personEditModel.getPerson()).thenReturn(person);
		Mockito.when(contactMapper.convert(personEditModel.getPerson().contacts())).thenReturn(new ContactMapperImpl(messageSource).convert(contacts));
		
		observers.get(PersonEditModel.EventType.PersonChanged).process((PersonEditModel.EventType.PersonChanged));
		
		final ListSelect listSelect = (ListSelect) components.get(PersonEditView.I18N_CONTACTS_CAPTION);
		Assert.assertEquals(CONTACT_AS_STRING, listSelect.getItemCaption(listSelect.getItemIds().iterator().next()));
		Mockito.verify(bindingResultMapper, Mockito.times(1)).mapInto(Mockito.anyMap(), Mockito.any(FieldGroup.class));
	}
	

	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void takeOver() {
		
		personChanged();
		
		final ListSelect listSelect = (ListSelect) components.get(PersonEditView.I18N_CONTACTS_CAPTION);
		Assert.assertEquals(1, listSelect.getItemIds().size());
	
		final Entry<UUID,Contact> entry = Mockito.mock(Entry.class);
		Contact changedContact =  BeanUtils.instantiateClass(PersonEntities.ContactType.Email.type());
		ReflectionTestUtils.setField(changedContact, "contact", CHANGED_CONTACT);
		Mockito.when(entry.getValue()).thenReturn(changedContact);
		final UUID uuid = (UUID) listSelect.getItemIds().iterator().next();
		Mockito.when(entry.getKey()).thenReturn(uuid);
		Mockito.when(personEditModel.getCurrentContact()).thenReturn(entry);

		ReflectionTestUtils.setField(personEditView, "contactMapper", new ContactMapperImpl(messageSource));
		ReflectionTestUtils.setField(personEditView, "bindingResultMapper", TestConstants.newBindingResultsToFieldGroupMapper(messageSource));
		
		Assert.assertNull(listSelect.getValue());
		
		observers.get(PersonEditModel.EventType.ContactTakeOver).process(PersonEditModel.EventType.ContactTakeOver);
		
		
		Assert.assertEquals(uuid, listSelect.getValue());
		Assert.assertEquals(CHANGED_CONTACT, listSelect.getItemCaption(uuid));
		
		ArgumentCaptor<PersonEditModel> personEditModelArgumentCaptor = ArgumentCaptor.forClass(PersonEditModel.class);
		
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Entry> entryArgumentCaptor =  ArgumentCaptor.forClass(Entry.class);
		Mockito.verify(personEditController, Mockito.times(1)).assign(personEditModelArgumentCaptor.capture(),  entryArgumentCaptor.capture());
		
		Assert.assertEquals(CHANGED_CONTACT , entry.getValue().contact());
		Assert.assertEquals(uuid, entry.getKey());
	}
	
	@Test
	public final void enterNew() {
		final ViewChangeEvent  viewEvent = Mockito.mock(ViewChangeEvent.class);
		personEditView.enter(viewEvent);
		
		Mockito.verify(personEditController, Mockito.times(1)).assign(personEditModel);
	}
	
	@Test
	public final void enterModify() {
		final ViewChangeEvent  viewEvent = Mockito.mock(ViewChangeEvent.class);
		Mockito.when(viewEvent.getParameters()).thenReturn(ID);
		personEditView.enter(viewEvent);
		Mockito.verify(personEditController).assign(personEditModel, ID);
	}
	
	@Test
	public final void save() {
		final Map<String, Object> personAsMap = new HashMap<>();
		Arrays.stream(PersonEditView.Fields.values()).forEach(field -> personAsMap.put(field.property(), field.name()));
		final ArgumentCaptor<FieldGroup> fieldGroup = ArgumentCaptor.forClass(FieldGroup.class);
		Mockito.when(bindingResultMapper.convert(fieldGroup.capture())).thenReturn(personAsMap);
		final BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when(personEditController.validateAndSave(personAsMap, personEditModel)).thenReturn(bindingResult);
		final ClickListener clickListener = (ClickListener) ((Button) components.get(PersonEditView.I18N_EDIT_PERSON_SAVE)).getListeners(ClickEvent.class).iterator().next();
		clickListener.buttonClick(clickEvent);
		
		Mockito.verify(bindingResultMapper, Mockito.times(1)).mapInto(bindingResult, fieldGroup.getValue());
		Mockito.verify(viewNav).navigateTo(PersonSearchView.class);
		
		Mockito.reset(viewNav);
		Mockito.when(bindingResult.hasErrors()).thenReturn(true);
	
		
		clickListener.buttonClick(clickEvent);
		
		Mockito.verify(viewNav, Mockito.times(0)).navigateTo(PersonSearchView.class);
	
		
	}
	
	@Test
	public final void saveWithErrors() {
		Button saveButton = (Button) components.get(PersonEditView.I18N_EDIT_PERSON_SAVE);
		final ClickListener listener = (ClickListener) saveButton.getListeners(ClickEvent.class).iterator().next();
		final Map<String, Object> personAsMap = new HashMap<>();
		
		final ArgumentCaptor<FieldGroup> fieldGroup = ArgumentCaptor.forClass(FieldGroup.class);
		Mockito.when(bindingResultMapper.convert(fieldGroup.capture())).thenReturn(personAsMap);
		final BindingResult bindingResult = Mockito.mock(BindingResult.class);
		ObjectError error = Mockito.mock(ObjectError.class);
		Mockito.when(bindingResult.getGlobalError()).thenReturn(error);
		Mockito.when(error.getCode()).thenReturn(PersonEditControllerImpl.PERSON_SAVE_ERROR_CODE);
		final String[] messageArgs = new String[]{ "save sucks"};
		Mockito.when(error.getArguments()).thenReturn(messageArgs);
		Mockito.when(bindingResult.hasGlobalErrors()).thenReturn(true);
		Mockito.when(personEditController.validateAndSave(personAsMap, personEditModel)).thenReturn(bindingResult);

		Mockito.when(messageSource.getMessage(PersonEditControllerImpl.PERSON_SAVE_ERROR_CODE, messageArgs, Locale.GERMAN)).thenReturn(messageArgs[0]);
				
	   listener.buttonClick(clickEvent);
		Mockito.verify(vaadinOperations).showErrror(messageArgs[0]);
	}
	
	@Test
	public final void fieldsEnumCoverageOnly() {
		Arrays.stream(PersonEditView.Fields.values()).map(field -> field.name()).forEach(name -> Assert.assertEquals(name, PersonEditView.Fields.valueOf(name).name()));
	}
	
	
	@Test
	public final void deleteContact() {
		final UUID uuid =  UUID.randomUUID();
		ListSelect listSelect = (ListSelect) components.get(PersonEditView.I18N_CONTACTS_CAPTION);
		listSelect.addItem(uuid);
		
		Assert.assertTrue(listSelect.getItemIds().contains(uuid));
		
		@SuppressWarnings("unchecked")
		final Entry<UUID,Contact> entry = Mockito.mock(Entry.class);
		
		Mockito.when(entry.getKey()).thenReturn(uuid);
		Mockito.when(personEditModel.getCurrentContact()).thenReturn(entry);
	
		
		observers.get(PersonEditModel.EventType.ContactDeleted).process(PersonEditModel.EventType.ContactDeleted);
		
		Assert.assertFalse(listSelect.getItemIds().contains(uuid));
	}
	
	@Test
	public final void delete() {
		final Button deleteButton = (Button) components.get(PersonEditView.I18N_DELETE_BUTTON_CAPTION);
		final ClickListener listener = (ClickListener) deleteButton.getListeners(ClickEvent.class).iterator().next();
		listener.buttonClick(clickEvent);
		
		Mockito.verify(personEditController).delete(personEditModel);
		Mockito.verify(viewNav).navigateTo(PersonSearchView.class);
	}

}
