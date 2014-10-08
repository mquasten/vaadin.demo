package de.mq.phone.web.person;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.mq.phone.web.person.PersonEditModel.EventType;
import de.mq.vaadin.util.BindingResultsToFieldGroupMapper;
import de.mq.vaadin.util.ViewNav;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
class PersonEditView extends CustomComponent implements View {

	
	private static final String I18N_EDIT_PERSON_PREFIX = "edit_person_";
	private static final String I18N_EDIT_PERSON_SAVE = "edit_person_save";
	private static final String I18N_EDIT_PERSON_CANCEL = "edit_person_cancel";
	private static final String I18N_EDIT_PERSON_HEADLINE = "edit_person_headline";

	enum Fields {
		Name(0, 0, new TextField()), Firstname(0, 1, new TextField()), Alias(0, 2, new TextField()),

		Street(1, 0,new TextField()), HouseNumber(1, 1,new TextField()), ZipCode(1, 2,new TextField()), City(1, 3,new TextField()),

		IBan(2, 0,new TextField()), BankIdentifierCode(2, 1,new TextField()),
		
		Contacts(3,0, new ListSelect());

		private final int row;
		private final int col;
		private AbstractField<?> field;

		Fields(int row, int col, AbstractField<?> field) {
			this.col = col;
			this.row = row;
			this.field=field;
		}

		String property() {
			return StringUtils.uncapitalize(name());
		}
		
		AbstractField<?> field(){
			if (field instanceof AbstractTextField) {
				 ((AbstractTextField) field).setNullRepresentation("");
			}
			
			if (field instanceof ListSelect) {
				((ListSelect) field).setNullSelectionAllowed(false);
				((ListSelect) field).setItemCaptionPropertyId(property());
			}
			
			return this.field;
		}

	}

	private static final long serialVersionUID = 1L;
	private final PersonEditController personEditController;
	private final ViewNav viewNav;
	private final BindingResultsToFieldGroupMapper bindingResultMapper;

	private final PersonEditModel personEditModel;
	private final UserModel userModel;
	private final MessageSource messageSource;
	private final ContactMapper contactMapper;
	private final ContactEditor contactEditor;
	
	@Autowired
	PersonEditView(final PersonEditController personEditController, final PersonEditModel personEditModel, final UserModel userModel, final ViewNav viewNav, final BindingResultsToFieldGroupMapper bindingResultMapper, final MessageSource messageSource, final ContactMapper contactMapper, final ContactEditor contactEditor) {
		this.viewNav = viewNav;
		this.bindingResultMapper = bindingResultMapper;
		this.userModel = userModel;
		this.messageSource = messageSource;
		this.personEditController=personEditController;
		this.personEditModel=personEditModel;
		this.contactMapper=contactMapper;
		this.contactEditor=contactEditor;
	}

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
			if (inputField instanceof TextField) {
				binder.bind(inputField, field.property());
				personItem.addItemProperty(field.property(), new ObjectProperty<String>(""));
			}
			
		});
		
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

		saveButton.addClickListener(event -> {
			final BindingResult  bindingResult = personEditController.validateAndSave(bindingResultMapper.convert(binder), personEditModel);
			
			bindingResultMapper.mapInto(bindingResult, binder);
			if( bindingResult.hasGlobalErrors() ) {
				Notification.show(getString(bindingResult.getGlobalError().getCode() , bindingResult.getGlobalError().getArguments()), Type.ERROR_MESSAGE);
			}

		});

		userModel.register((model, event) -> {
			setLocale(userModel.getLocale());
			binder.getFields().forEach(field -> field.setCaption(getString((I18N_EDIT_PERSON_PREFIX + binder.getPropertyId(field)).toLowerCase())) );
			Fields.Contacts.field().setCaption(getString((I18N_EDIT_PERSON_PREFIX + Fields.Contacts.property().toLowerCase())));
			panel.setCaption(getString(I18N_EDIT_PERSON_HEADLINE));
			cancelButton.setCaption(getString(I18N_EDIT_PERSON_CANCEL));
			saveButton.setCaption(getString(I18N_EDIT_PERSON_SAVE));

		}, UserModel.EventType.LocaleChanges);
		panel.setContent(editFormLayout);
		mainLayoout.addComponent(panel);
		mainLayoout.addComponent(buttonPanel);
		setCompositionRoot(mainLayoout);
		
		personEditModel.register((model, event) -> { 
			bindingResultMapper.mapInto(personEditController.person(personEditModel), binder);
			((ListSelect) Fields.Contacts.field()).setContainerDataSource(contactMapper.convert(personEditModel.getPerson().contacts()));
			
		}, EventType.PersonChanged);
	}

	private AbstractField<?> addInputField(final GridLayout editFormLayout, final Fields fieldDesc) {
		final HorizontalLayout fieldLayout = new HorizontalLayout();
		fieldLayout.setMargin(true);

		editFormLayout.addComponent(fieldLayout, fieldDesc.col, fieldDesc.row);
		final AbstractField<?> field = fieldDesc.field();
		
		field.setWidth("15em");
		fieldLayout.addComponent(field);
		field.setId(fieldDesc.property());

		return field;
	}

	@Override
	public void enter(final ViewChangeEvent event) {
		if(! StringUtils.hasText(event.getParameters())) {
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
