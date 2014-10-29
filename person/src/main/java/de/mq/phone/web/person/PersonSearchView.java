package de.mq.phone.web.person;


import java.util.Collection;
import java.util.Locale;


import javax.annotation.PostConstruct;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.mq.phone.domain.person.Person;
import de.mq.vaadin.util.ViewNav;

@Component()
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
class PersonSearchView extends CustomComponent implements View  {

	static final String I18N_CONTACT_TABLE_CAPTION = "table";
	static final String I18N_LANGUAGE_COMBOBOX_CAPTION = "language";
	static final String I18N_DELETE_BUTTON_CAPTION = "delete_button";
	static final String I18N_CHANGE_BUTTON_CAPTION = "change_button";
	static final String I18N_NEW_BUTTON_CAPTION = "new_button";
	static final String I18N_CONTACT_TABLE_PANEL_CAPTION = "contact_table";
	static final String I18N_SEARCH_BUTTON_CAPTION = "search_button";
	static final String I18N_SEARCH_ADDRESS_FIELD_CAPTION = "search_address";
	static final String I18N_SEARCH_CONTACT_FIELD_CAPTION = "search_contact";
	static final String I18N_SEARCH_PERSON_FIELD_CAPTION = "search_person";
	static final String I18N_SEARCH_PANEL_CAPTION = "search_headline";
	static final String ADDRESS_SEARCH_PROPERTY = "address";
	static final String PERSON_SEARCH_PROPERTY = "person";
	static final String CONTACT_SEARCH_PROPERTY = "contact";
	
	static final String CONTACTS = "contacts";
	static final String ADDRESS = "address";
	static final String PERSON = "person";
	static final String BANKING_ACCOUNT= "bank";

	private final Converter<Collection<Person>, Container> personListContainerConverter;
	private final Converter<Item, Collection<Object>> itemToPersonSearchSetConverter;
	private final PersonSearchModel model;
	private final UserModel userModel;
	private final PersonSearchController personSearchController;
	private final MessageSource messages;
	private final ViewNav viewNav;

	@Autowired
	PersonSearchView(final PersonSearchModel model, final PersonSearchController personSearchController, final MessageSource messages, final UserModel userModel, @ConverterQualifier(ConverterQualifier.Type.PersonList2Container) final Converter<Collection<Person>, Container> personListContainerConverter, @ConverterQualifier(ConverterQualifier.Type.Item2PersonSearchSet) final Converter<Item, Collection<Object>> itemToPersonSearchSetConverter, final ViewNav viewNav) {
		this.model = model;
		this.personSearchController = personSearchController;
		this.messages = messages;
		this.userModel = userModel;
		this.personListContainerConverter = personListContainerConverter;
		this.itemToPersonSearchSetConverter = itemToPersonSearchSetConverter;
		this.viewNav=viewNav;
	}

	PersonSearchView() {
		this(null, null, null, null, null, null, null);
	}

	private static final long serialVersionUID = 1L;

	@PostConstruct()
	final void init()  {

		
	
      
      
		final VerticalLayout mainLayoout = new VerticalLayout();

		final Panel panel = new Panel();

		final HorizontalLayout searchFormLayout = new HorizontalLayout();
		panel.setContent(searchFormLayout);

		searchFormLayout.setMargin(true);
		final FormLayout col1Layout = new FormLayout();
		final FormLayout col3Layout = new FormLayout();
		final FormLayout col4Layout = new FormLayout();
		final FormLayout col5Layout = new FormLayout();

		searchFormLayout.addComponent(col1Layout);
		searchFormLayout.addComponent(col3Layout);
		searchFormLayout.addComponent(col4Layout);
		searchFormLayout.addComponent(col5Layout);

		final TextField nameField = new TextField();
		col1Layout.addComponent(nameField);

		final TextField contactField = new TextField();
		col3Layout.addComponent(contactField);

		final TextField addressField = new TextField();
		col4Layout.addComponent(addressField);

		final PropertysetItem personSearchItem = new PropertysetItem();
		personSearchItem.addItemProperty(PERSON_SEARCH_PROPERTY, new ObjectProperty<String>(""));

		personSearchItem.addItemProperty(CONTACT_SEARCH_PROPERTY, new ObjectProperty<String>(""));
		personSearchItem.addItemProperty(ADDRESS_SEARCH_PROPERTY, new ObjectProperty<String>(""));
		final FieldGroup binder = new FieldGroup(personSearchItem);
		binder.setBuffered(true);

		binder.bind(nameField, PERSON_SEARCH_PROPERTY);
		binder.bind(contactField, CONTACT_SEARCH_PROPERTY);
		binder.bind(addressField, ADDRESS_SEARCH_PROPERTY);

		final Button searchButton = new Button();
		searchButton.addClickListener(event -> search(personSearchItem, binder));

		col5Layout.addComponent(searchButton);

		searchFormLayout.setSpacing(true);
		searchFormLayout.setMargin(true);
		mainLayoout.addComponent(panel);

		final Panel tablePanel = new Panel();

		final VerticalLayout tableLayout = new VerticalLayout();
		tablePanel.setContent(tableLayout);
		final HorizontalLayout gridLayout = new HorizontalLayout();
		gridLayout.setSpacing(true);

		GridLayout buttonLayout = new GridLayout(3, 1);
		buttonLayout.setSpacing(true);
		buttonLayout.setMargin(true);
		final Button newButton = new Button();
		final Button updateButton = new Button();
		final Button deleteButton = new Button();
	
		newButton.addClickListener(event -> {
			viewNav.navigateTo(PersonEditView.class);
		});
		
		buttonLayout.addComponent(newButton);
		buttonLayout.addComponent(updateButton);
		buttonLayout.addComponent(deleteButton);
		gridLayout.addComponent(buttonLayout);

		final BeanFieldGroup<UserModel> languageBinder = new BeanFieldGroup<>(UserModel.class);
		// languageBinder.setBuffered(true);
		languageBinder.setItemDataSource(userModel);

		final FormLayout boxLayout = new FormLayout();
		final ComboBox languageBox = new ComboBox();

		languageBox.setNullSelectionAllowed(false);

		languageBox.setNewItemsAllowed(false);

		languageBox.setContainerDataSource(new BeanItemContainer<Locale>(Locale.class, userModel.getSupportedLocales()));

		languageBox.setImmediate(true);

		boxLayout.addComponent(languageBox);
		gridLayout.addComponent(boxLayout);

		languageBinder.bind(languageBox, "locale");

		languageBox.addValueChangeListener(event -> commitBinder(languageBinder));

		tableLayout.addComponent(gridLayout);

		mainLayoout.addComponent(tablePanel);
		mainLayoout.setMargin(true);

		final Table table = new Table();

		table.setSelectable(true);
		tableLayout.addComponent(table);
		table.setSizeFull();
		table.setEditable(false);
		
		updateButton.addClickListener(event -> {
			
			final String personId = (String) table.getValue();
			if(personId==null){
				return;
			}
			
			viewNav.navigateTo(PersonEditView.class, personId);
		});

		setCompositionRoot(mainLayoout);
		getCompositionRoot().setSizeFull();

		model.register(event -> personChangeObserver(table), PersonSearchModel.EventType.PersonsChanges);

		userModel.register( event -> {
			setLocale(userModel.getLocale());
			panel.setCaption(getString(I18N_SEARCH_PANEL_CAPTION));
			nameField.setCaption(getString(I18N_SEARCH_PERSON_FIELD_CAPTION));
			contactField.setCaption(getString(I18N_SEARCH_CONTACT_FIELD_CAPTION));
			addressField.setCaption(getString(I18N_SEARCH_ADDRESS_FIELD_CAPTION));
			searchButton.setCaption(getString(I18N_SEARCH_BUTTON_CAPTION));
			tablePanel.setCaption(getString(I18N_CONTACT_TABLE_PANEL_CAPTION));
			newButton.setCaption(getString(I18N_NEW_BUTTON_CAPTION));
			updateButton.setCaption(getString(I18N_CHANGE_BUTTON_CAPTION));
			deleteButton.setCaption(getString(I18N_DELETE_BUTTON_CAPTION));
			languageBox.setCaption(getString(I18N_LANGUAGE_COMBOBOX_CAPTION));
			table.setCaption(getString(I18N_CONTACT_TABLE_CAPTION));
			table.setColumnHeader(PERSON, getString("table_person"));
			table.setColumnHeader(ADDRESS, getString("table_address"));
			table.setColumnHeader(BANKING_ACCOUNT, getString("table_bank"));
			table.setColumnHeader(CONTACTS, getString("table_contacts"));
			languageBox.getItemIds().forEach(itemId -> languageBox.setItemCaption(itemId, ((Locale) itemId).getDisplayLanguage(userModel.getLocale())));
			
			mainLayoout.removeAllComponents();
			mainLayoout.addComponent(panel);
			mainLayoout.addComponent(tablePanel);

		}, UserModel.EventType.LocaleChanges);

	}

	private void search(final PropertysetItem personSearchItem, final FieldGroup binder) {
		commitBinder(binder);
		itemToPersonSearchSetConverter.convert(personSearchItem).forEach(criteriaBean -> model.setSearchCriteria(criteriaBean));
		personSearchController.assignPersons(model);
	}

	private void personChangeObserver(final Table table) {
		table.setContainerDataSource(personListContainerConverter.convert(model.getPersons()));
		table.removeGeneratedColumn(CONTACTS);
		table.addGeneratedColumn(CONTACTS, (source, itemId, ColumnId) -> contactColumnGenerator(table, itemId));

		table.setVisibleColumns(new Object[] { PERSON, CONTACTS, ADDRESS, BANKING_ACCOUNT });
	
		for (final Object itemId : table.getVisibleColumns()) {
			table.setColumnExpandRatio(itemId, 1f);
		}
	}

	Field<?> contactColumnGenerator(final Table table, final Object itemId) {
		final Collection<?> contacts = (Collection<?>) table.getContainerDataSource().getItem(itemId).getItemProperty(CONTACTS).getValue();
		
		if( contacts.size() == 0 ) {
			return  null;
		}
		if( contacts.size() ==  1){
		  final TextField field =   new TextField();	
		  field.setSizeFull();
		  field.setValue(contacts.iterator().next().toString());
		  field.setReadOnly(true);
		  return field;
		}
		
		final ListSelect listSelect = new ListSelect();
		contacts.forEach(contact -> listSelect.addItem(contact) );
		
		
		listSelect.setSizeFull();
		listSelect.setNullSelectionAllowed(false);

		listSelect.setNewItemsAllowed(false);
		listSelect.setRows(contacts.size());

		if (contacts.size() > 3) {
			listSelect.setRows(3);
		}
		return listSelect;
	}

	private String getString(final String key) {
		return messages.getMessage(key, null, getLocale());
	}

	private void commitBinder(final FieldGroup binder) {
		try {
			binder.commit();
		} catch (final CommitException ex) {
			throw new IllegalStateException(ex);
		}
	}

	@Override
	public void enter(final ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
