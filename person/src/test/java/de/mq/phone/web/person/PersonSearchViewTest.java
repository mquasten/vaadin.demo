package de.mq.phone.web.person;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.util.ReflectionTestUtils;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.ListenerMethod;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonStringAware;
import de.mq.phone.domain.person.support.BankingAccount;
import de.mq.vaadin.util.Observer;
import de.mq.vaadin.util.ViewNav;

public class PersonSearchViewTest {

	private static final String ID = "19680528";
	private static final String MAIL = "kinky.kylie@minogue.uk";
	private static final String SEARCH_FIELD_PERSON_CAPTION = "Person";
	private static final String SEARCH_PANEL_CAPTION = "suchenPanel";
	private static final String SEARCH_FIELD_CONTACT_CAPTION = "Kontakt";
	private static final String SEARCH_FIELD_ADDRESS_CAPTION = "Addresse";
	private static final String SEARCH_BUTTON_CAPTION = "suchenButton";
	private static final String NEW_BUTTON_CAPTION = PersonSearchView.I18N_NEW_BUTTON_CAPTION;
	private static final String CHANGE_BUTTON_CAPTION = "aendern";

	private static final String LANGUAGE_BOX_CAPTION = "Sprache";
	private static final String CONTACT_TABLE_PANEL_CAPTION = "Kontaktdaten";
	private static final String CONTACT_TABLE_CAPTION = "table";
	@SuppressWarnings("unchecked")
	private final Converter<Item, Collection<Object>> itemToPersonSearchSetConverter = Mockito.mock(Converter.class);;
	@SuppressWarnings("unchecked")
	private final Converter<Collection<Person>, Container> personListContainerConverter = Mockito.mock(Converter.class);
	private final MessageSource messages = Mockito.mock(MessageSource.class);
	private final PersonSearchModel personSearchModel = Mockito.mock(PersonSearchModel.class);
	private final PersonSearchController personSearchController = Mockito.mock(PersonSearchController.class);
	private final UserModel userModel = Mockito.mock(UserModel.class);

	private final ViewNav viewNav = Mockito.mock(ViewNav.class);
	private final PersonSearchView personSearchView = new PersonSearchView(personSearchModel, personSearchController, messages, userModel, personListContainerConverter, itemToPersonSearchSetConverter, new StringCollectionToContainerConverter(), viewNav);

	@SuppressWarnings("rawtypes")
	private final ArgumentCaptor<Observer> localeObserverCaptor = ArgumentCaptor.forClass(Observer.class);
	private final ArgumentCaptor<UserModel.EventType> eventCaptor = ArgumentCaptor.forClass(UserModel.EventType.class);

	@SuppressWarnings("rawtypes")
	private final ArgumentCaptor<Observer> modelChangeObserverCaptor = ArgumentCaptor.forClass(Observer.class);
	private final ArgumentCaptor<PersonSearchModel.EventType> modelEventCaptor = ArgumentCaptor.forClass(PersonSearchModel.EventType.class);

	private final Map<String, Component> components = new HashMap<>();

	@SuppressWarnings("unchecked")
	@Before
	public final void setup() {
		components.clear();
		Mockito.when(messages.getMessage(PersonSearchView.I18N_SEARCH_PANEL_CAPTION, null, Locale.GERMAN)).thenReturn(SEARCH_PANEL_CAPTION);
		Mockito.when(messages.getMessage(PersonSearchView.I18N_SEARCH_PERSON_FIELD_CAPTION, null, Locale.GERMAN)).thenReturn(SEARCH_FIELD_PERSON_CAPTION);
		Mockito.when(messages.getMessage(PersonSearchView.I18N_SEARCH_CONTACT_FIELD_CAPTION, null, Locale.GERMAN)).thenReturn(SEARCH_FIELD_CONTACT_CAPTION);
		Mockito.when(messages.getMessage(PersonSearchView.I18N_SEARCH_ADDRESS_FIELD_CAPTION, null, Locale.GERMAN)).thenReturn(SEARCH_FIELD_ADDRESS_CAPTION);
		Mockito.when(messages.getMessage(PersonSearchView.I18N_SEARCH_BUTTON_CAPTION, null, Locale.GERMAN)).thenReturn(SEARCH_BUTTON_CAPTION);
		Mockito.when(messages.getMessage(PersonSearchView.I18N_NEW_BUTTON_CAPTION, null, Locale.GERMAN)).thenReturn(NEW_BUTTON_CAPTION);
		Mockito.when(messages.getMessage(PersonSearchView.I18N_CHANGE_BUTTON_CAPTION, null, Locale.GERMAN)).thenReturn(CHANGE_BUTTON_CAPTION);

		Mockito.when(messages.getMessage(PersonSearchView.I18N_LANGUAGE_COMBOBOX_CAPTION, null, Locale.GERMAN)).thenReturn(LANGUAGE_BOX_CAPTION);
		Mockito.when(messages.getMessage(PersonSearchView.I18N_CONTACT_TABLE_CAPTION, null, Locale.GERMAN)).thenReturn(CONTACT_TABLE_CAPTION);
		Mockito.when(messages.getMessage(PersonSearchView.I18N_CONTACT_TABLE_PANEL_CAPTION, null, Locale.GERMAN)).thenReturn(CONTACT_TABLE_PANEL_CAPTION);

		Mockito.when(userModel.getLocale()).thenReturn(Locale.GERMAN);
		Collection<Locale> locales = new ArrayList<>();
		locales.add(Locale.GERMAN);
		locales.add(Locale.ENGLISH);
		Mockito.when(userModel.getSupportedLocales()).thenReturn(locales);

		personSearchView.init();

		Mockito.verify(userModel, Mockito.times(1)).register(localeObserverCaptor.capture(), eventCaptor.capture());
		Mockito.verify(personSearchModel, Mockito.times(1)).register(modelChangeObserverCaptor.capture(), modelEventCaptor.capture());
		@SuppressWarnings("rawtypes")
		final Observer observer = localeObserverCaptor.getValue();
		observer.process(UserModel.EventType.LocaleChanges);
		ComponentTestHelper.components(personSearchView, components);
		Assert.assertFalse(components.isEmpty());
	}

	@Test
	public void init() {

		Assert.assertEquals(10, components.size());
		Assert.assertEquals(Panel.class, components.get(SEARCH_PANEL_CAPTION).getClass());

		Assert.assertEquals(Button.class, components.get(SEARCH_BUTTON_CAPTION).getClass());

		Assert.assertEquals(Button.class, components.get(CHANGE_BUTTON_CAPTION).getClass());
		Assert.assertEquals(Button.class, components.get(NEW_BUTTON_CAPTION).getClass());

		Assert.assertEquals(ComboBox.class, components.get(LANGUAGE_BOX_CAPTION).getClass());

		Assert.assertEquals(TextField.class, components.get(SEARCH_FIELD_CONTACT_CAPTION).getClass());
		Assert.assertEquals(TextField.class, components.get(SEARCH_FIELD_ADDRESS_CAPTION).getClass());
		Assert.assertEquals(TextField.class, components.get(SEARCH_FIELD_PERSON_CAPTION).getClass());

		Assert.assertEquals(Panel.class, components.get(CONTACT_TABLE_PANEL_CAPTION).getClass());
		Assert.assertEquals(Table.class, components.get(CONTACT_TABLE_CAPTION).getClass());

	}

	@SuppressWarnings("unchecked")
	@Test
	public final void search() {
		final Button button = (Button) components.get(SEARCH_BUTTON_CAPTION);
		final ClickEvent clickEvent = Mockito.mock(ClickEvent.class);
		final Collection<Object> searchBeans = new HashSet<>();
		searchBeans.add(Mockito.mock(PersonStringAware.class));
		searchBeans.add(Mockito.mock(Contact.class));
		searchBeans.add(Mockito.mock(AddressStringAware.class));
		Mockito.when(itemToPersonSearchSetConverter.convert(Matchers.any(Item.class))).thenReturn(searchBeans);

		for (final ClickListener listener : (Collection<ClickListener>) (button.getListeners(ClickEvent.class))) {
			listener.buttonClick(clickEvent);
		}
		for (Object criteria : searchBeans) {
			Mockito.verify(personSearchModel, Mockito.times(1)).setSearchCriteria(criteria);
		}

		Mockito.verify(personSearchController, Mockito.times(1)).assignPersons(personSearchModel);

		final List<Person> persons = new ArrayList<>();
		final Person searchResult = Mockito.mock(Person.class);
		Mockito.when(searchResult.id()).thenReturn(ID);
		Mockito.when(searchResult.person()).thenReturn("Kylie Minogue");
		final Address address = Mockito.mock(Address.class);
		Mockito.when(address.address()).thenReturn("London");
		Mockito.when(searchResult.address()).thenReturn(address);
		final BankingAccount bankingAccount = Mockito.mock(BankingAccount.class);
		Mockito.when(searchResult.bankingAccount()).thenReturn(bankingAccount);
		Mockito.when(bankingAccount.account()).thenReturn("Kylie's banking account");
		final List<Contact> contacts = new ArrayList<>();
		final Contact contact = Mockito.mock(Contact.class);
		Mockito.when(contact.contact()).thenReturn(MAIL);
		contacts.add(contact);
		Mockito.when(searchResult.contacts()).thenReturn(contacts);
		persons.add(searchResult);
		Mockito.when(personSearchModel.getPersons()).thenReturn(persons);

		ReflectionTestUtils.setField(personSearchView, "personListContainerConverter", new PersonListToItemContainerConverter());
		modelChangeObserverCaptor.getValue().process(PersonSearchModel.EventType.PersonsChanges);

		final Table table = (Table) components.get(CONTACT_TABLE_CAPTION);
		Assert.assertEquals(searchResult.person(), table.getContainerProperty(searchResult.id(), PersonSearchView.PERSON).getValue());
		Assert.assertEquals(searchResult.address().address(), table.getContainerProperty(searchResult.id(), PersonSearchView.ADDRESS).getValue());
		Assert.assertEquals(searchResult.bankingAccount().account(), table.getContainerProperty(searchResult.id(), PersonSearchView.BANKING_ACCOUNT).getValue());
		final Collection<?> contactIfos = (Collection<String>) table.getContainerProperty(searchResult.id(), PersonSearchView.CONTACTS).getValue();
		Assert.assertEquals(1, contactIfos.size());

		final List<Object> cols = new ArrayList<>();
		for (final Object id : table.getVisibleColumns()) {
			Assert.assertEquals(1f, table.getColumnExpandRatio(id));
			cols.add(id);

		}
		Assert.assertEquals(4, cols.size());
		Assert.assertEquals(PersonSearchView.PERSON, cols.get(0));
		Assert.assertEquals(PersonSearchView.CONTACTS, cols.get(1));
		Assert.assertEquals(PersonSearchView.ADDRESS, cols.get(2));
		Assert.assertEquals(PersonSearchView.BANKING_ACCOUNT, cols.get(3));

	}

	@SuppressWarnings("unchecked")
	@Test
	public final void commitBinder() {
		final ValueChangeEvent valueChangeEvent = Mockito.mock(ValueChangeEvent.class);
		final ComboBox comboBox = (ComboBox) components.get(LANGUAGE_BOX_CAPTION);

		for (final ValueChangeListener listener : (Collection<ValueChangeListener>) comboBox.getListeners(ValueChangeEvent.class)) {
			listener.valueChange(valueChangeEvent);
		}

		Mockito.verify(userModel, Mockito.times(1)).setLocale(Locale.GERMAN);
	}

	@Test(expected = IllegalStateException.class)
	public final void commitBinderSucks() throws Throwable {
		final ComboBox comboBox = (ComboBox) components.get(LANGUAGE_BOX_CAPTION);
		Mockito.doThrow(new IllegalStateException()).when(userModel).setLocale(Matchers.any(Locale.class));
		try {
			comboBox.setValue(Locale.ENGLISH);
		} catch (ListenerMethod.MethodException ex) {
			throw ex.getCause();
		}
	}

	@Test
	public final void neu() {
		final Button button = (Button) components.get(PersonSearchView.I18N_NEW_BUTTON_CAPTION);
		final ClickListener listener = (ClickListener) button.getListeners(ClickEvent.class).iterator().next();

		listener.buttonClick(Mockito.mock(ClickEvent.class));
		Mockito.verify(viewNav, Mockito.times(1)).navigateTo(PersonEditView.class);
	}

	@Test
	public final void update() {
		final Button button = (Button) components.get(CHANGE_BUTTON_CAPTION);
		final Table table = (Table) components.get(CONTACT_TABLE_CAPTION);
		table.addItem(ID);
		table.setValue(ID);

		final ClickListener listener = (ClickListener) button.getListeners(ClickEvent.class).iterator().next();
		listener.buttonClick(Mockito.mock(ClickEvent.class));
		Mockito.verify(viewNav, Mockito.times(1)).navigateTo(PersonEditView.class, ID);
		table.setValue(null);
		listener.buttonClick(Mockito.mock(ClickEvent.class));

		Mockito.verifyNoMoreInteractions(viewNav);
	}

	@Test
	public final void defaultConructor() {
		Assert.assertNotNull(new PersonSearchView());
	}

	@Test
	public final void columnGenerator() {
		final String itemId = ID;
		final Table table = Mockito.mock(Table.class);
		final Container container = Mockito.mock(Container.class);
		final Item item = Mockito.mock(Item.class);
		@SuppressWarnings("unchecked")
		final Property<Object> property = Mockito.mock(Property.class);
		Mockito.when(container.getItem(itemId)).thenReturn(item);
		Mockito.when(item.getItemProperty(PersonSearchView.CONTACTS)).thenReturn(property);
		Mockito.when(table.getContainerDataSource()).thenReturn(container);
		final Collection<String> contacts = new ArrayList<>();
		contacts.add(MAIL);
		contacts.add("12345");
		contacts.add("skype:kinkyKylie");
		contacts.add("kylie.minogue@fever.net");
		Mockito.when(property.getValue()).thenReturn(contacts);
		checkSubTable((Table) personSearchView.contactColumnGenerator(table, itemId), contacts);

		contacts.remove(MAIL);
		contacts.remove("12345");

		contacts.remove("skype:kinkyKylie");
		final Label field = (Label) personSearchView.contactColumnGenerator(table, itemId);
		Assert.assertEquals("kylie.minogue@fever.net", field.getValue());

		contacts.clear();
		Assert.assertNull(personSearchView.contactColumnGenerator(table, itemId));
	}

	private void checkSubTable(final Table subtable, final Collection<String> contacts) {

		Assert.assertEquals(contacts.size(), subtable.getItemIds().size());
		subtable.getItemIds().forEach(id -> Assert.assertTrue(contacts.contains(subtable.getItem(id).getItemProperty(PersonSearchView.CONTACTS).getValue())));
		Assert.assertEquals(2, subtable.getPageLength());
	}

	@SuppressWarnings("unchecked")
	@Test
	public final void executeColumnGenerator() {

		final Table table = (Table) components.get(PersonSearchView.I18N_CONTACT_TABLE_CAPTION);
		final List<Person> persons = new ArrayList<>();
		Mockito.when(personSearchModel.getPersons()).thenReturn(persons);
		Converter<Collection<Person>, Container> converter = new PersonListToItemContainerConverter();
		final Person person = Mockito.mock(Person.class);
		Mockito.when(person.id()).thenReturn(ID);
		persons.add(person);
		final Container container = converter.convert(persons);
		Mockito.when(personListContainerConverter.convert(persons)).thenReturn(container);

		modelChangeObserverCaptor.getValue().process(PersonSearchModel.EventType.PersonsChanges);
		final ColumnGenerator generator = table.getColumnGenerator(PersonSearchView.CONTACTS);
		Assert.assertNotNull(generator);
		Assert.assertNull(generator.generateCell(table, ID, null));

	}

	@Test
	public final void enterView() {
		final ViewChangeEvent event = Mockito.mock(ViewChangeEvent.class);
		personSearchView.enter(event);
	}
	
	@Test
	public final void rowSelected() { 
		final Table table = (Table) components.get(CONTACT_TABLE_CAPTION);
		
		final Button button = (Button) components.get(CHANGE_BUTTON_CAPTION);

		Assert.assertFalse(button.isEnabled());
		final ValueChangeListener listener = (ValueChangeListener) table.getListeners(ValueChangeEvent.class).iterator().next();
		final ValueChangeEvent event = Mockito.mock(ValueChangeEvent.class);
		@SuppressWarnings("unchecked")
		final Property<String> property = Mockito.mock(Property.class);
		Mockito.when(event.getProperty()).thenReturn(property);
		Mockito.when(property.getValue()).thenReturn(ID);
		listener.valueChange(event);
		Assert.assertTrue(button.isEnabled());
		Mockito.when(property.getValue()).thenReturn(null);
		listener.valueChange(event);
		Assert.assertFalse(button.isEnabled());
	}

}
