package de.mq.phone.web.person;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

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
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

import de.mq.phone.domain.person.GeoCoordinates;
import de.mq.phone.domain.person.Person;
import de.mq.vaadin.util.BindingResultsToFieldGroupMapper;
import de.mq.vaadin.util.ViewNav;

@Component()
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
class PersonSearchView extends CustomComponent implements View {

	static final String I18N_CONTACT_TABLE_CAPTION = "table";
	static final String I18N_LANGUAGE_COMBOBOX_CAPTION = "language";

	static final String I18N_CHANGE_BUTTON_CAPTION = "change_button";
	static final String I18N_NEW_BUTTON_CAPTION = "new_button";
	static final String I18N_CONTACT_TABLE_PANEL_CAPTION = "contact_table";
	static final String I18N_SEARCH_BUTTON_CAPTION = "search_button";
	static final String I18N_SEARCH_ADDRESS_FIELD_CAPTION = "search_address";
	static final String I18N_SEARCH_CONTACT_FIELD_CAPTION = "search_contact";
	static final String  I18N_SEARCH_DISTANCE_FIELD_CAPTION = "search_distance";
	static final String I18N_SEARCH_PERSON_FIELD_CAPTION = "search_person";
	static final String I18N_SEARCH_PANEL_CAPTION = "search_headline";
	static final String ADDRESS_SEARCH_PROPERTY = "address";
	static final String PERSON_SEARCH_PROPERTY = "person";
	static final String CONTACT_SEARCH_PROPERTY = "contact";
	static final String DISTANCE_SEARCH_PROPERTY = "distance";

	static final String CONTACTS = "contacts";
	static final String ADDRESS = "address";
	static final String PERSON = "person";
	static final String BANKING_ACCOUNT = "bank";
	static final String COORDINATES = "coordinates";

	private final Converter<Collection<Person>, Container> personListContainerConverter;
	private final Converter<Item, Collection<Object>> itemToPersonSearchSetConverter;
	private final Converter<Collection<String>, Container> stringCollection2ContainerConverter;
	private final PersonSearchModel model;
	private final UserModel userModel;
	private final PersonSearchController personSearchController;
	private final MessageSource messages;
	private final ViewNav viewNav;
	private final MainMenuBarView mainMenuBarView;
	
	private final PagerView pagerView;
	
	private final BindingResultsToFieldGroupMapper bindingResultsToFieldGroupMapper;

	@Autowired
	PersonSearchView(final PersonSearchModel model, final PersonSearchController personSearchController, final MessageSource messages, final UserModel userModel, @ConverterQualifier(ConverterQualifier.Type.PersonList2Container) final Converter<Collection<Person>, Container> personListContainerConverter, @ConverterQualifier(ConverterQualifier.Type.Item2PersonSearchSet) final Converter<Item, Collection<Object>> itemToPersonSearchSetConverter, @ConverterQualifier(ConverterQualifier.Type.StringList2Container) final Converter<Collection<String>, Container> stringCollection2ContainerConverter, final ViewNav viewNav, final MainMenuBarView mainMenuBarView, final BindingResultsToFieldGroupMapper bindingResultsToFieldGroupMapper, final PagerView pagerView) {
		this.model = model;
		this.personSearchController = personSearchController;
		this.messages = messages;
		this.userModel = userModel;
		this.personListContainerConverter = personListContainerConverter;
		this.itemToPersonSearchSetConverter = itemToPersonSearchSetConverter;
		this.stringCollection2ContainerConverter=stringCollection2ContainerConverter;
		this.viewNav = viewNav;
		this.mainMenuBarView=mainMenuBarView;
		this.bindingResultsToFieldGroupMapper=bindingResultsToFieldGroupMapper;
		this.pagerView=pagerView;
	}

	PersonSearchView() {
		this(null, null, null, null, null, null, null, null, null, null, null);
	}

	private static final long serialVersionUID = 1L;
	

	@PostConstruct()
	final void init() {

		final VerticalLayout screen = new VerticalLayout();
		final VerticalLayout mainLayoout = new VerticalLayout();
	
		screen.addComponent(mainMenuBarView);
		screen.addComponent(mainLayoout);
		
		final Panel panel = new Panel();

		final HorizontalLayout searchFormLayout = new HorizontalLayout();
		panel.setContent(searchFormLayout);

		//searchFormLayout.addComponent(menubar);
		searchFormLayout.setMargin(true);
		searchFormLayout.setSpacing(true);
		final FormLayout col1Layout = new FormLayout();
		final FormLayout col3Layout = new FormLayout();
		final FormLayout col4Layout = new FormLayout();
		final FormLayout col5Layout = new FormLayout();
		final FormLayout col6Layout = new FormLayout();

		searchFormLayout.addComponent(col1Layout);
		searchFormLayout.addComponent(col3Layout);
		searchFormLayout.addComponent(col4Layout);
		searchFormLayout.addComponent(col5Layout);
		searchFormLayout.addComponent(col6Layout);

		final TextField nameField = new TextField();
		col1Layout.addComponent(nameField);

		final TextField contactField = new TextField();
		col3Layout.addComponent(contactField);

		final TextField addressField = new TextField();
		col4Layout.addComponent(addressField);
		
		final TextField distanceField = new TextField();
	
		distanceField.setEnabled(false);
		col5Layout.addComponent(distanceField);

		final PropertysetItem personSearchItem = new PropertysetItem();
		personSearchItem.addItemProperty(PERSON_SEARCH_PROPERTY, new ObjectProperty<String>(""));

		personSearchItem.addItemProperty(CONTACT_SEARCH_PROPERTY, new ObjectProperty<String>(""));
		personSearchItem.addItemProperty(ADDRESS_SEARCH_PROPERTY, new ObjectProperty<String>(""));
		personSearchItem.addItemProperty(DISTANCE_SEARCH_PROPERTY, new ObjectProperty<String>(""));
		final FieldGroup binder = new FieldGroup(personSearchItem);
		binder.setBuffered(true);

		binder.bind(nameField, PERSON_SEARCH_PROPERTY);
		binder.bind(contactField, CONTACT_SEARCH_PROPERTY);
		binder.bind(addressField, ADDRESS_SEARCH_PROPERTY);
		binder.bind(distanceField, DISTANCE_SEARCH_PROPERTY);
		final Button searchButton = new Button();
		searchButton.addClickListener(event -> search(personSearchItem, binder));

		col6Layout.addComponent(searchButton);

		mainLayoout.addComponent(panel);

		final Panel tablePanel = new Panel();

		final VerticalLayout tableLayout = new VerticalLayout();

		tablePanel.setContent(tableLayout);
		final HorizontalLayout gridLayout = new HorizontalLayout();
		
		GridLayout buttonLayout = new GridLayout(2, 1);
		buttonLayout.setSpacing(true);
		buttonLayout.setMargin(true);

		final Button newButton = new Button();
		final Button updateButton = new Button();
		updateButton.setEnabled(false);
		newButton.addClickListener(event -> {
			viewNav.navigateTo(PersonEditView.class);
		});

		buttonLayout.addComponent(newButton);
		buttonLayout.addComponent(updateButton);

		gridLayout.addComponent(buttonLayout);
		gridLayout.setComponentAlignment(buttonLayout, Alignment.MIDDLE_LEFT);

		final BeanFieldGroup<UserModel> languageBinder = new BeanFieldGroup<>(UserModel.class);
		languageBinder.setBuffered(true);
		languageBinder.setItemDataSource(userModel);

		final HorizontalLayout boxLayout = new HorizontalLayout();
		boxLayout.setMargin(true);
		boxLayout.setSpacing(true);
		tableLayout.setSpacing(true);
		final FormLayout languageLaylout = new FormLayout();
		final ComboBox languageBox = new ComboBox();
		languageLaylout.addComponent(languageBox);
		languageBox.setNullSelectionAllowed(false);

		
		languageBox.setNewItemsAllowed(false);

		languageBox.setContainerDataSource(new BeanItemContainer<Locale>(Locale.class, userModel.getSupportedLocales()));

		languageBox.setImmediate(true);

		boxLayout.addComponent(languageLaylout);
		
		gridLayout.addComponent(boxLayout);
 
		
		languageBinder.bind(languageBox, "locale");

		languageBox.addValueChangeListener(event -> commitBinder(languageBinder));

		pagerView.setSpacing(true);
		gridLayout.addComponent(pagerView);	
		
		gridLayout.setComponentAlignment(pagerView, Alignment.MIDDLE_LEFT);
		
		tableLayout.addComponent(gridLayout);
	
		

		mainLayoout.addComponent(tablePanel);
		mainLayoout.setMargin(true);
		mainLayoout.setSpacing(true);

		final Table table = new Table();
		table.setSelectable(true);
		tableLayout.addComponent(table);
		table.setSizeFull();
		table.setEditable(false);

		updateButton.addClickListener(event -> {

			final String personId = (String) table.getValue();
			if (personId == null) {
				return;
			}

			viewNav.navigateTo(PersonEditView.class, personId);
		});
		
		table.addValueChangeListener(event -> updateButton.setEnabled( event.getProperty().getValue() != null));

		setCompositionRoot(screen);
		getCompositionRoot().setSizeFull();

		model.register(event -> personChangeObserver(table), PersonSearchModel.EventType.PersonsChanges);

		userModel.register(event -> {
			setLocale(userModel.getLocale());
			panel.setCaption(getString(I18N_SEARCH_PANEL_CAPTION));
			nameField.setCaption(getString(I18N_SEARCH_PERSON_FIELD_CAPTION));
			contactField.setCaption(getString(I18N_SEARCH_CONTACT_FIELD_CAPTION));
			addressField.setCaption(getString(I18N_SEARCH_ADDRESS_FIELD_CAPTION));
			searchButton.setCaption(getString(I18N_SEARCH_BUTTON_CAPTION));
			tablePanel.setCaption(getString(I18N_CONTACT_TABLE_PANEL_CAPTION));
			newButton.setCaption(getString(I18N_NEW_BUTTON_CAPTION));
			updateButton.setCaption(getString(I18N_CHANGE_BUTTON_CAPTION));

			languageBox.setCaption(getString(I18N_LANGUAGE_COMBOBOX_CAPTION));
			table.setCaption(getString(I18N_CONTACT_TABLE_CAPTION));
			table.setColumnHeader(PERSON, getString("table_person"));
			table.setColumnHeader(ADDRESS, getString("table_address"));
			table.setColumnHeader(BANKING_ACCOUNT, getString("table_bank"));
			table.setColumnHeader(CONTACTS, getString("table_contacts"));
			table.setColumnHeader(COORDINATES, getString("table_coordinates"));
			languageBox.getItemIds().forEach(itemId -> languageBox.setItemCaption(itemId, ((Locale) itemId).getDisplayLanguage(userModel.getLocale())));

			distanceField.setCaption(getString(I18N_SEARCH_DISTANCE_FIELD_CAPTION));
			mainLayoout.removeAllComponents();
			mainLayoout.addComponent(panel);
			mainLayoout.addComponent(tablePanel);

			
		}, UserModel.EventType.LocaleChanges);
		
	
		model.register(event -> distanceField.setEnabled(model.hasGeoCoordinates()), PersonSearchModel.EventType.HomeLocationChanges);
	}

	private void search(final PropertysetItem personSearchItem, final FieldGroup binder) {
		
		
		final BindingResult bindingresult = personSearchController.validate(bindingResultsToFieldGroupMapper.convert(binder));
		bindingResultsToFieldGroupMapper.mapInto(bindingresult, binder);
		if( bindingresult.hasErrors()){	
			return;
		}
		commitBinder(binder);
		itemToPersonSearchSetConverter.convert(personSearchItem).forEach(criteriaBean -> model.setSearchCriteria(criteriaBean));
		personSearchController.assignPersons(model);
	}

	private void personChangeObserver(final Table table) {
		table.setContainerDataSource(personListContainerConverter.convert(model.getPersons()));
		table.removeGeneratedColumn(CONTACTS);
		table.addGeneratedColumn(CONTACTS, (source, itemId, columnId) -> contactColumnGenerator(table, itemId));
		table.removeGeneratedColumn(COORDINATES);
		table.addGeneratedColumn(COORDINATES, (source, itemId, columnId) -> newSubTable(personSearchController.geoInfos((GeoCoordinates) source.getItem(itemId).getItemProperty(COORDINATES).getValue(), model, userModel.getLocale())));
		table.setVisibleColumns(new Object[] { PERSON, CONTACTS, ADDRESS, BANKING_ACCOUNT, COORDINATES });

		Arrays.stream(table.getVisibleColumns()).forEach(col -> table.setColumnExpandRatio(col, 1f));

	}
	
	@SuppressWarnings("unchecked")
	AbstractComponent contactColumnGenerator(final Table table, final Object itemId) {
		
		final Collection<String> contacts = (Collection<String>) table.getContainerDataSource().getItem(itemId).getItemProperty(CONTACTS).getValue();	
		return  newSubTable(contacts);
	
	
	}

	private AbstractComponent newSubTable(final Collection<String> contacts) {
		if( contacts.size() == 0){
			return null;
		}
		
		if( contacts.size()==1){
			return new Label(contacts.iterator().next());
		}
		
		final  Table subTable = new  Table();
		subTable.setContainerDataSource(stringCollection2ContainerConverter.convert(contacts));
		subTable.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		subTable.setPageLength(2);
		subTable.setStyleName(Reindeer.LAYOUT_WHITE);
		subTable.setSizeFull();
		subTable.setSelectable(true);
		return subTable;
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
		personSearchController.assignGeoKoordinates(model);
		personSearchController.assignPersons(model);
		
	}

}
