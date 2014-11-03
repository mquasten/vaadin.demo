package de.mq.phone.web.person;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

import de.mq.phone.web.person.UserModel.EventType;
import de.mq.vaadin.util.BindingResultsToFieldGroupMapper;
import de.mq.vaadin.util.Observer;
import de.mq.vaadin.util.ViewNav;

public class PersonEditViewTest {

	private final PersonEditController personEditController = Mockito.mock(PersonEditController.class);
	private final PersonEditModel personEditModel = Mockito.mock(PersonEditModel.class);
	private final UserModel userModel = Mockito.mock(UserModel.class);
	private final ViewNav viewNav = Mockito.mock(ViewNav.class);
	private final BindingResultsToFieldGroupMapper bindingResultMapper = Mockito.mock(BindingResultsToFieldGroupMapper.class);
	private final MessageSource messageSource = Mockito.mock(MessageSource.class);
	private final ContactMapper contactMapper = Mockito.mock(ContactMapper.class);
	private final ContactEditorView contactEditor = Mockito.mock(ContactEditorView.class);

	private final PersonEditView personEditView = new PersonEditView(personEditController, personEditModel, userModel, viewNav, bindingResultMapper, messageSource, contactMapper, contactEditor);

	private final Map<String, Component> components = new HashMap<>();
	private Observer<UserModel.EventType> localeObserver;

	@SuppressWarnings("unchecked")
	@Before
	public final void setup() {
		Iterator<Component> it = Mockito.mock(Iterator.class);
		Mockito.when(contactEditor.iterator()).thenReturn(it);
		Mockito.when(userModel.getLocale()).thenReturn(Locale.GERMAN);
		Arrays.stream(PersonEditView.Fields.values()).map(field -> PersonEditView.I18N_EDIT_PERSON_PREFIX + field.property().toLowerCase()).forEach(i18n -> Mockito.when(messageSource.getMessage(i18n, null, Locale.GERMAN)).thenReturn(i18n));

		Mockito.doAnswer(invocation -> localeObserver = (Observer<EventType>) invocation.getArguments()[0]).when(userModel).register(Mockito.any(Observer.class), Mockito.any(UserModel.EventType.class));

		Mockito.when(messageSource.getMessage(PersonEditView.I18N_EDIT_PERSON_HEADLINE, null, Locale.GERMAN)).thenReturn(PersonEditView.I18N_EDIT_PERSON_HEADLINE);
		Mockito.when(messageSource.getMessage(PersonEditView.I18N_EDIT_PERSON_CANCEL, null, Locale.GERMAN)).thenReturn(PersonEditView.I18N_EDIT_PERSON_CANCEL);
		Mockito.when(messageSource.getMessage(PersonEditView.I18N_EDIT_PERSON_SAVE, null, Locale.GERMAN)).thenReturn(PersonEditView.I18N_EDIT_PERSON_SAVE);
		Mockito.when(messageSource.getMessage(PersonEditView.I18N_CONTACT_TYPE, null, Locale.GERMAN)).thenReturn(PersonEditView.I18N_CONTACT_TYPE);
		Mockito.when(messageSource.getMessage(PersonEditView.I18N_CONTACT_ADD, null, Locale.GERMAN)).thenReturn(PersonEditView.I18N_CONTACT_ADD);

		// ... touched for the very first time ...
		personEditView.init();
		localeObserver.process(UserModel.EventType.LocaleChanges);
		components.clear();
		ComponentTestHelper.components(personEditView, components);
	}

	@Test
	public final void init() {
		Assert.assertEquals(15, components.size());

		Arrays.stream(PersonEditView.Fields.values()).map(field -> PersonEditView.I18N_EDIT_PERSON_PREFIX + field.property().toLowerCase()).forEach(i18n -> Assert.assertEquals(i18n, components.get(i18n).getCaption()));

		Arrays.stream(PersonEditView.Fields.values()).filter(field -> field.row != 3).map(field -> PersonEditView.I18N_EDIT_PERSON_PREFIX + field.property().toLowerCase()).forEach(i18n -> Assert.assertTrue(components.get(i18n) instanceof TextField));
		Assert.assertEquals(PersonEditView.I18N_EDIT_PERSON_HEADLINE, components.get(PersonEditView.I18N_EDIT_PERSON_HEADLINE).getCaption());
		Assert.assertTrue(components.get(PersonEditView.I18N_EDIT_PERSON_HEADLINE) instanceof Panel);
		Assert.assertTrue(components.get(PersonEditView.I18N_EDIT_PERSON_PREFIX + PersonEditView.Fields.Contacts.property().toLowerCase()) instanceof ListSelect);
		Assert.assertEquals(PersonEditView.I18N_CONTACT_TYPE, components.get(PersonEditView.I18N_CONTACT_TYPE).getCaption());
		Assert.assertTrue(components.get(PersonEditView.I18N_CONTACT_TYPE) instanceof ComboBox);
		Arrays.stream(new String[] { PersonEditView.I18N_CONTACT_ADD, PersonEditView.I18N_EDIT_PERSON_SAVE, PersonEditView.I18N_EDIT_PERSON_CANCEL }).forEach(i18n -> {
			Assert.assertEquals(i18n, components.get(i18n).getCaption());
			Assert.assertTrue(components.get(i18n) instanceof Button);
		});
	}

}
