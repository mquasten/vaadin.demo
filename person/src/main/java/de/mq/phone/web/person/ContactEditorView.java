package de.mq.phone.web.person;

import java.util.Arrays;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.mq.vaadin.util.BindingResultsToFieldGroupMapper;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
class ContactEditorView extends CustomComponent {

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
			 field.setCaption("<" + property() + ">");
			return field;
		}
	}

	private static final long serialVersionUID = 1L;
	
	private final PersonEditModel personEditModel;
	private final BindingResultsToFieldGroupMapper bindingResultMapper;
	private final ContactMapper contactMapper;
	
	@Autowired
	ContactEditorView(final PersonEditModel personEditModel, final BindingResultsToFieldGroupMapper bindingResultMapper, final ContactMapper contactMapper) {
		this.personEditModel=personEditModel;
		this.bindingResultMapper=bindingResultMapper;
		this.contactMapper=contactMapper;
	}

	@PostConstruct
	void init() {

		final VerticalLayout mainLayoout = new VerticalLayout();

		mainLayoout.setMargin(true);
		final GridLayout formLayout = new GridLayout();
		mainLayoout.addComponent(formLayout);
		formLayout.setCaption("<Kontakt bearbeiten>");
		final PropertysetItem contactItem = new PropertysetItem();
		final FieldGroup binder = new FieldGroup(contactItem);
		binder.setBuffered(true);
		
		Arrays.stream(Fields.values()).forEach(field -> { 
			formLayout.addComponent(field.field());
			contactItem.addItemProperty(field.property(), new ObjectProperty<String>(""));
			binder.bind(field.field(), field.property());
			
		});

		
		personEditModel.register(event -> bindingResultMapper.mapInto(contactMapper.contactToMap(personEditModel.getSelectedContact()), binder) , PersonEditModel.EventType.ContactsChanged );
		
		setCompositionRoot(mainLayoout);
	}

}
