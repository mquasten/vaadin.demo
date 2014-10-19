package de.mq.phone.web.person;

import java.util.Arrays;
import java.util.HashMap;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.mq.vaadin.util.BindingResultsToFieldGroupMapper;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
class ContactEditorView extends CustomComponent {

	private static final String I18N_EDIT_CONTACT_HEADLINE = "edit_contact_headline";

	private static final String I18N_EDIT_CONTACT_BUTTON = "edit_contact_change_button";

	enum Fields {
		Contact(new TextField()), CountryCode(new TextField()), NationalDestinationCode(new TextField()), SubscriberNumber(new TextField());

		
		private final TextField field; 
		Fields(TextField  field ) {
			field.setColumns(15);
			field.setNullRepresentation("");
			this.field=field;
		}
		
		String property() {
			return StringUtils.uncapitalize(name());
		}

		TextField field() {
			 field.setVisible(false);
			return field;
		}
	}

	private static final long serialVersionUID = 1L;
	
	private final PersonEditModel personEditModel;
	private final BindingResultsToFieldGroupMapper bindingResultMapper;
	private final ContactMapper contactMapper;
	private final UserModel userModel;
	private final MessageSource messageSource;
	private final PersonEditController personEditController; 
	private final String I18N_EDIT_CONTACT_PREFIX="edit_contact_"; 
	
	@Autowired
	ContactEditorView(final PersonEditModel personEditModel, final PersonEditController personEditController,final BindingResultsToFieldGroupMapper bindingResultMapper, final ContactMapper contactMapper, final UserModel userModel, final MessageSource messageSource) {
		this.personEditModel=personEditModel;
		this.personEditController=personEditController;
		this.bindingResultMapper=bindingResultMapper;
		this.contactMapper=contactMapper;
		this.userModel=userModel;
		this.messageSource=messageSource;
	}

	@PostConstruct
	void init() {
		setVisible(false);
		final VerticalLayout mainLayoout = new VerticalLayout();

		mainLayoout.setMargin(true);
		final GridLayout formLayout = new GridLayout();
		mainLayoout.addComponent(formLayout);
		
		final PropertysetItem contactItem = new PropertysetItem();
		final FieldGroup binder = new FieldGroup(contactItem);
		binder.setBuffered(true);
		
		Arrays.stream(Fields.values()).forEach(field -> { 
			formLayout.addComponent(field.field());
			contactItem.addItemProperty(field.property(), new ObjectProperty<String>(""));
			binder.bind(field.field(), field.property());
			
		});

		final HorizontalLayout buttonLayout = new HorizontalLayout();
		final Button changeButton = new Button(); 
		buttonLayout.addComponent(changeButton);
		mainLayoout.addComponent(changeButton);
		
		changeButton.addClickListener(event -> { 
			final BindingResult bindingResult = personEditController.validateAndTakeOver(bindingResultMapper.convert(binder), personEditModel);
			
			
			bindingResultMapper.mapInto(bindingResult, binder);
		});
		
		
		personEditModel.register(event -> {
			bindingResultMapper.mapInto(contactMapper.contactToMap(personEditModel.getSelectedContact()), binder); 
		   binder.getFields().forEach(field ->  field.setVisible(false));
		   setVisible(false);
		 
		   bindingResultMapper.mapInto(new MapBindingResult(new HashMap<>(),"contact"), binder);
		   
		   if( personEditModel.isMailContact() ) {
		   	Fields.Contact.field().setVisible(true);
		   	setVisible(true);
		   	return;
		   }
		   if( personEditModel.isPhoneContact()) {
		   	Arrays.stream( Fields.values()).filter(field -> field != Fields.Contact ).forEach(field -> field.field().setVisible(true));
		   	setVisible(true); 
		   }
		   
		  
		   
		}, PersonEditModel.EventType.ContactChanged );
			
	
		userModel.register(event -> { 
			setLocale(userModel.getLocale());
			changeButton.setCaption(getString(I18N_EDIT_CONTACT_BUTTON));
			formLayout.setCaption(getString(I18N_EDIT_CONTACT_HEADLINE));
			binder.getFields().forEach(field -> field.setCaption(getString((I18N_EDIT_CONTACT_PREFIX + binder.getPropertyId(field)).toLowerCase())) );
		}, UserModel.EventType.LocaleChanges);
		
		
		setCompositionRoot(mainLayoout);
	}
	
	private String getString(final String key) {
		return messageSource.getMessage(key, null, getLocale());
	}

}
