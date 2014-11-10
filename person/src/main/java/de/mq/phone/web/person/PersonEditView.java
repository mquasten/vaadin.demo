package de.mq.phone.web.person;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import com.vaadin.data.Container;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.mq.phone.domain.person.Contact;
import de.mq.phone.web.person.PersonEditModel.EventType;
import de.mq.vaadin.util.BindingResultsToFieldGroupMapper;
import de.mq.vaadin.util.VaadinOperations;
import de.mq.vaadin.util.ViewNav;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
class PersonEditView extends CustomComponent implements View {

	private static final String CONTACTS_PROPERTY = "contacts";
	static final String I18N_CONTACT_ADD = "contact_add";
	static final String I18N_CONTACT_TYPE = "contact_type";
	static final String I18N_EDIT_PERSON_PREFIX = "edit_person_";
	static final String I18N_CONTACTS_CAPTION = I18N_EDIT_PERSON_PREFIX + CONTACTS_PROPERTY;
	static final String I18N_EDIT_PERSON_SAVE = "edit_person_save";
	static final String I18N_EDIT_PERSON_CANCEL = "edit_person_cancel";
	static final String I18N_EDIT_PERSON_HEADLINE = "edit_person_headline";
	static final String I18N_DELETE_BUTTON_CAPTION = "delete_button";

	enum Fields {
		Name(0, 0), Firstname(0, 1), Alias(0, 2),

		Street(1, 0), HouseNumber(1, 1), ZipCode(1, 2), City(1, 3),

		IBan(2, 0), BankIdentifierCode(2, 1);

		final int row;
		private final int col;
		

	 Fields(final int row, final int col) {
			this.col = col;
			this.row = row;
		} 

		String property() {
			return StringUtils.uncapitalize(name());
		}

		TextField newField() {
			final TextField field = new TextField();
			field.setNullRepresentation("");
		

		
			return field;
		} 

	}

	private static final long serialVersionUID = 1L;
	static final String CONTACT_DOMAIN_PROPERTY = "contact";
	static final String CONTACT_STRING_PROPERTY = "contactAsString";

	static final String CONTACT_TYPE_PROPERTY = "type";

	private final PersonEditController personEditController;
	private final ViewNav viewNav;
	private final BindingResultsToFieldGroupMapper bindingResultMapper;

	private final PersonEditModel personEditModel;
	private final UserModel userModel;
	private final MessageSource messageSource;
	private final ContactMapper contactMapper;
	private final ContactEditorView contactEditor;
	private final VaadinOperations vaadinOperations;

	@Autowired
	PersonEditView(final PersonEditController personEditController, final PersonEditModel personEditModel, final UserModel userModel, final ViewNav viewNav, final BindingResultsToFieldGroupMapper bindingResultMapper, final MessageSource messageSource, final ContactMapper contactMapper, final ContactEditorView contactEditor, final VaadinOperations vaadinOperations) {
		this.viewNav = viewNav;
		this.bindingResultMapper = bindingResultMapper;
		this.userModel = userModel;
		this.messageSource = messageSource;
		this.personEditController = personEditController;
		this.personEditModel = personEditModel;
		this.contactMapper = contactMapper;
		this.contactEditor = contactEditor;
		this.vaadinOperations=vaadinOperations;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	public final void init() {
		final VerticalLayout mainLayoout = new VerticalLayout();
		mainLayoout.setMargin(true);
		final Panel panel = new Panel();
		final GridLayout editFormLayout = new GridLayout(4, 4);
		editFormLayout.setMargin(true);

		final PropertysetItem personItem = new PropertysetItem();
		final FieldGroup binder = new FieldGroup(personItem);
		binder.setBuffered(true);

		Arrays.stream(Fields.values()).forEach(field -> {
			final AbstractField<?> inputField = addInputField(editFormLayout, field);
			personItem.addItemProperty(field.property(), new ObjectProperty<String>(""));
			
				binder.bind(inputField, field.property());
				personItem.addItemProperty(field.property(), new ObjectProperty<String>(""));
			

		});
		
		final HorizontalLayout listLayout = new HorizontalLayout();
		listLayout.setMargin(true);

		editFormLayout.addComponent(listLayout, 0, 3);
		final ListSelect contactList = new ListSelect();

		contactList.setWidth("15em");
		
		contactList.setNullSelectionAllowed(false);
		contactList.setItemCaptionPropertyId(CONTACT_STRING_PROPERTY);
		contactList.setImmediate(true);
		
		listLayout.addComponent(contactList);
		

		final VerticalLayout typeLayout = new VerticalLayout();
		typeLayout.setMargin(true);

		final ComboBox typeComboBox = new ComboBox();
		typeComboBox.setNullSelectionAllowed(false);
		typeComboBox.setNewItemsAllowed(false);
		typeComboBox.setItemCaptionPropertyId(CONTACT_TYPE_PROPERTY);

		typeLayout.addComponent(typeComboBox);

		final Button addButton = new Button();

		addButton.addClickListener(event -> personEditController.assign(personEditModel, (Class<Contact>) typeComboBox.getValue()));

		typeLayout.addComponent(addButton);
		typeLayout.setComponentAlignment(addButton, Alignment.BOTTOM_LEFT);

		editFormLayout.addComponent(typeLayout, 2, 3);

		contactList.addValueChangeListener(event -> personEditController.assign(personEditModel, (Entry<UUID, Contact>) bindingResultMapper.convert(contactList.getItem(event.getProperty().getValue())).get(CONTACT_DOMAIN_PROPERTY)));

		final HorizontalLayout fieldLayout = new HorizontalLayout();

		fieldLayout.addComponent(contactEditor);
		editFormLayout.addComponent(fieldLayout, 1, 3);

		final Panel buttonPanel = new Panel();

		final HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonPanel.setContent(buttonLayout);
		buttonLayout.setMargin(true);
		final Button cancelButton = new Button();

		cancelButton.addClickListener(event -> viewNav.navigateTo(PersonSearchView.class));
		final Button saveButton = new Button();
		buttonLayout.addComponent(cancelButton);
		buttonLayout.addComponent(saveButton);
		final Button deleteButton = new Button();
		deleteButton.addClickListener(event -> {personEditController.delete(personEditModel); viewNav.navigateTo(PersonSearchView.class); });
		buttonLayout.addComponent(deleteButton);

		saveButton.addClickListener(event -> {

			final Container contactContainer = contactList.getContainerDataSource();
			final Map<String, Object> personAsMap = bindingResultMapper.convert(binder);
			personAsMap.put(CONTACTS_PROPERTY, contactMapper.convert(contactContainer));
			final BindingResult bindingResult = personEditController.validateAndSave(personAsMap, personEditModel);
			bindingResultMapper.mapInto(bindingResult, binder);
			if (bindingResult.hasGlobalErrors()) {
				vaadinOperations.showErrror(getString(bindingResult.getGlobalError().getCode(), bindingResult.getGlobalError().getArguments()));
			} 
			if( ! bindingResult.hasErrors()) {
				viewNav.navigateTo(PersonSearchView.class);
			}
		});

		userModel.register(event -> {
			setLocale(userModel.getLocale());
			binder.getFields().forEach(field -> field.setCaption(getString((I18N_EDIT_PERSON_PREFIX + binder.getPropertyId(field)).toLowerCase())));
			contactList.setCaption(getString(I18N_CONTACTS_CAPTION));
			panel.setCaption(getString(I18N_EDIT_PERSON_HEADLINE));
			cancelButton.setCaption(getString(I18N_EDIT_PERSON_CANCEL));
			saveButton.setCaption(getString(I18N_EDIT_PERSON_SAVE));
			typeComboBox.setContainerDataSource(contactMapper.convert(getLocale()));
			typeComboBox.setCaption(getString(I18N_CONTACT_TYPE));
			deleteButton.setCaption(getString(I18N_DELETE_BUTTON_CAPTION));

			if (!typeComboBox.getContainerDataSource().getItemIds().isEmpty()) {

				typeComboBox.setValue(typeComboBox.getContainerDataSource().getItemIds().iterator().next());
			}

			addButton.setCaption(getString(I18N_CONTACT_ADD));

		}, UserModel.EventType.LocaleChanges);
		panel.setContent(editFormLayout);
		mainLayoout.addComponent(panel);
		mainLayoout.addComponent(buttonPanel);
		setCompositionRoot(mainLayoout);

		personEditModel.register(event -> {
			bindingResultMapper.mapInto(personEditController.person(personEditModel), binder);
			
			contactList.setContainerDataSource(contactMapper.convert(personEditModel.getPerson().contacts()));
			
			deleteButton.setVisible( personEditModel.isIdAware());
			

		}, EventType.PersonChanged);

		personEditModel.register(event -> {
			
		
			contactMapper.mapInto(personEditModel.getCurrentContact(), contactList.getContainerDataSource());
			contactList.setValue(personEditModel.getCurrentContact().getKey());

		}, EventType.ContactTakeOver);
	}

	private AbstractField<?> addInputField(final GridLayout editFormLayout, final Fields fieldDesc) {
		final HorizontalLayout fieldLayout = new HorizontalLayout();
		fieldLayout.setMargin(true);

		editFormLayout.addComponent(fieldLayout, fieldDesc.col, fieldDesc.row);
		final AbstractField<?> field = fieldDesc.newField();

		field.setWidth("15em");
		fieldLayout.addComponent(field);
		field.setId(fieldDesc.property());

		return field;
	}

	@Override
	public void enter(final ViewChangeEvent event) {
		if (!StringUtils.hasText(event.getParameters())) {
			personEditController.assign(personEditModel);
		} else {
			personEditController.assign(personEditModel, event.getParameters());
		}
	}

	private String getString(final String key) {
		return messageSource.getMessage(key, null, getLocale());
	}

	private String getString(final String key, Object[] args) {
		return messageSource.getMessage(key, args, getLocale());
	}

}
