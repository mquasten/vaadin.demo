package de.mq.phone.web.person;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.TextField;

import de.mq.phone.domain.person.Contact;
import de.mq.phone.web.person.ContactEditorView.Fields;
import de.mq.phone.web.person.PersonEditModel.EventType;
import de.mq.vaadin.util.BindingResultsToFieldGroupMapper;
import de.mq.vaadin.util.Observer;

public class ContactEditorViewTest {
	
	
	private final PersonEditModel personEditModel = Mockito.mock(PersonEditModel.class);
	private final PersonEditController personEditController = Mockito.mock(PersonEditController.class);
	private final BindingResultsToFieldGroupMapper bindingResultMapper = Mockito.mock(BindingResultsToFieldGroupMapper.class);
	private final ContactMapper contactMapper =  Mockito.mock(ContactMapper.class);
	private final UserModel userModel = Mockito.mock(UserModel.class);
	private final MessageSource messageSource = Mockito.mock(MessageSource.class);

	private final ContactEditorView contactEditorView = new ContactEditorView(personEditModel, personEditController, bindingResultMapper, contactMapper, userModel,messageSource  );

	private final Map<String, Component> components = new HashMap<>();
	
	Observer<UserModel.EventType> localeChangedObserver; 
	Observer<PersonEditModel.EventType> contactChangedObserver;
	@SuppressWarnings("unchecked")
	@Before
	public final void setup() {
		Mockito.when(userModel.getLocale()).thenReturn(Locale.GERMAN);
		Mockito.when(messageSource.getMessage(ContactEditorView.I18N_EDIT_CONTACT_BUTTON, null, Locale.GERMAN)).thenReturn(ContactEditorView.I18N_EDIT_CONTACT_BUTTON);
		Mockito.when(messageSource.getMessage(ContactEditorView.I18N_EDIT_CONTACT_HEADLINE, null, Locale.GERMAN)).thenReturn(ContactEditorView.I18N_EDIT_CONTACT_HEADLINE);
	
		Arrays.stream(Fields.values()).forEach(field -> Mockito.when(messageSource.getMessage( (ContactEditorView.I18N_EDIT_CONTACT_PREFIX + field.property()).toLowerCase(), null, Locale.GERMAN)).thenReturn(field.property()));
		
		Mockito.doAnswer( invocation -> {
			localeChangedObserver = (Observer<UserModel.EventType>) invocation.getArguments()[0];
			return null;
		} ).when(userModel).register(Mockito.any(Observer.class), Mockito.any(UserModel.EventType.class) ); 
		
		
		Mockito.doAnswer(invocation -> { 
			contactChangedObserver = (Observer<PersonEditModel.EventType>) invocation.getArguments()[0];
			return null;
		}).when(personEditModel).register(Mockito.any(Observer.class), Mockito.any(PersonEditModel.EventType.class));
		
		contactEditorView.init();
		
		localeChangedObserver.process(UserModel.EventType.LocaleChanges);
		
		components.clear();
		components( contactEditorView, components);
	}
	
	
	@Test
	public final void init() {
		
		Arrays.stream(Fields.values()).forEach(field -> { 
			Assert.assertTrue(components.containsKey(field.property()));
			Assert.assertFalse(components.get(field.property()).isVisible());
		});
		
		Assert.assertTrue(components.containsKey(ContactEditorView.I18N_EDIT_CONTACT_BUTTON));
		Assert.assertTrue(components.containsKey(ContactEditorView.I18N_EDIT_CONTACT_HEADLINE));
		Assert.assertEquals(2 + Fields.values().length, components.size());
		
		Assert.assertFalse(contactEditorView.isVisible());
	}
	

	@Test
	public final void initPhone() {
		final Map<String,Object> entryAsMap = new HashMap<>();
		Arrays.stream(Fields.values()).filter( field -> Fields.Contact != field).forEach(field -> entryAsMap.put(field.property(), field.name()));
		prepare(entryAsMap);
	
		contactChangedObserver.process(EventType.PersonChanged);
		Arrays.stream(Fields.values()).filter(field -> field != Fields.Contact).forEach(field -> Assert.assertTrue(components.get(field.property()).isVisible()));
		Assert.assertFalse(components.get(Fields.Contact.property()).isVisible());
	
		Arrays.stream(Fields.values()).filter( field -> Fields.Contact != field).forEach(field -> Assert.assertEquals(field.name(), ((TextField)components.get(field.property())).getValue()));
	}
	
	@Test
	public final void initMail() {
		final Map<String,Object> entryAsMap = new HashMap<>();
		entryAsMap.put(Fields.Contact.property(), Fields.Contact.name());
		prepare(entryAsMap);
		contactChangedObserver.process(EventType.PersonChanged);
		Arrays.stream(Fields.values()).filter(field -> field != Fields.Contact).forEach(field -> Assert.assertFalse(components.get(field.property()).isVisible()));
		Assert.assertTrue(components.get(Fields.Contact.property()).isVisible());
	   Assert.assertEquals(Fields.Contact.name(), (((TextField)components.get(Fields.Contact.property())).getValue()));
	}
	
	@Test
	public final void initNothing() {
		final Map<String,Object> entryAsMap = new HashMap<>();
		prepare(entryAsMap);
		Mockito.when(personEditModel.isPhoneContact()).thenReturn(false);
		Mockito.when(personEditModel.isMailContact()).thenReturn(false);
		
		contactChangedObserver.process(EventType.PersonChanged);
		Arrays.stream(Fields.values()).forEach(field -> Assert.assertFalse(components.get(field.property()).isVisible()));
		Arrays.stream(Fields.values()).forEach(field ->  Assert.assertFalse(StringUtils.hasText(((TextField)components.get(field.property())).getValue())));
	}

	@SuppressWarnings("unchecked")
	private void prepare(Map<String,Object> entryAsMap) {
		final Entry<UUID, Contact> entry = Mockito.mock(Entry.class);
		Mockito.when(personEditModel.getCurrentContact()).thenReturn(entry);
		final Contact contact = Mockito.mock(Contact.class);
		Mockito.when(entry.getValue()).thenReturn(contact);
		
		if( entryAsMap.containsKey(Fields.Contact.property())) {
			Mockito.when(personEditModel.isMailContact()).thenReturn(true);
		} else {
			Mockito.when(personEditModel.isPhoneContact()).thenReturn(true);
		}
		
		Mockito.when(contactMapper.contactToMap(entry)).thenReturn(entryAsMap);
		Mockito.doAnswer(invocation -> {
			
			Assert.assertEquals(entryAsMap, invocation.getArguments()[0]);
			((Map<String,?>)invocation.getArguments()[0]).keySet().forEach(fieldName -> ((TextField) ((FieldGroup) invocation.getArguments()[1]).getField(fieldName)).setValue((String) entryAsMap.get(fieldName)));
			
			return null;
		}).when(bindingResultMapper).mapInto(Mockito.anyMap(), Mockito.any(FieldGroup.class));
	}
	
	@Test
	public final void change() {
		final Button button = (Button) components.get(ContactEditorView.I18N_EDIT_CONTACT_BUTTON);
		com.vaadin.ui.Button.ClickListener listener =   (com.vaadin.ui.Button.ClickListener) button.getListeners(ClickEvent.class).iterator().next();
		final ClickEvent event = Mockito.mock(ClickEvent.class);
		final Map<String,Object> asMap = new HashMap<>();
		ArgumentCaptor<FieldGroup> fieldGroupArgumentCaptor = ArgumentCaptor.forClass(FieldGroup.class);
		Mockito.when(bindingResultMapper.convert(fieldGroupArgumentCaptor.capture())).thenReturn(asMap);
		final BindingResult bindingResult = Mockito.mock(BindingResult.class);
		Mockito.when( personEditController.validateAndTakeOver(asMap, personEditModel)).thenReturn(bindingResult);
		listener.buttonClick(event);
		
		Mockito.verify(bindingResultMapper, Mockito.times(1)).mapInto(bindingResult, fieldGroupArgumentCaptor.getValue());
	}
	
	void components(final Component component, final Map<String, Component> components) {

	
		if (component.getCaption() != null) {
			
			components.put(component.getCaption(), component);
		}

		if (!(component instanceof HasComponents)) {
			return;
		}
		final Iterator<Component> it = ((HasComponents) component).iterator();

		while (it.hasNext()) {
			final Component child = it.next();
			
			components(child, components);
		}

	}
	
}
