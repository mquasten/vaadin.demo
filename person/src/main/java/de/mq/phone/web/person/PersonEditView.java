package de.mq.phone.web.person;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;



import javax.annotation.PostConstruct;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ValidationUtils;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;



import de.mq.phone.domain.person.Person;
import de.mq.vaadin.util.ViewNav;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
class PersonEditView extends CustomComponent implements View {
	

	enum Fields {
		Name(0,0),
		Firstname(0,1),
		Alias(0,2),
		
		Street(1,0),
		HouseNumber(1,1),
		ZipCode(1,2),
		City(1,3),
		
		IBan(2,0),
		BankIdentifierCode(2,1);
		
		int row;
		int col;
	
		Fields(int row, int col) {
			this.col=col;
			this.row=row;
		
		}
		
		String property() {
			return StringUtils.uncapitalize(name());
		}
		
		
		
	}
	
	
	private static final long serialVersionUID = 1L;
	private final ViewNav viewNav ; 
	private final Converter<PropertysetItem, Person> itemSet2Person;
	
	@Autowired
	PersonEditView(final ViewNav viewNav, final @ConverterQualifier(ConverterQualifier.Type.Item2Person) Converter<PropertysetItem, Person> itemSet2Person) {
		this.viewNav=viewNav;
		this.itemSet2Person=itemSet2Person;
	}
	
	
	
	@PostConstruct
	public final void init() {
		final VerticalLayout mainLayoout = new VerticalLayout();

		mainLayoout.setMargin(true);
		final Panel panel = new Panel();
	
		panel.setCaption("Person bearbeiten");
		
		
		final GridLayout editFormLayout = new GridLayout(4,3);
	
		editFormLayout.setMargin(true);
		
		final PropertysetItem personItem = new PropertysetItem();
		final FieldGroup binder = new FieldGroup(personItem);
		binder.setBuffered(true);
		//final Map<Fields, AbstractField<String>> fields = new HashMap<>();
		for(final Fields field : Fields.values()) {
			personItem.addItemProperty(field.property(), new ObjectProperty<String>(""));
			final AbstractField<String> inputField = addInputField(editFormLayout, field);
			binder.bind(inputField, field.property());
			//fields.put(field, inputField);
		}
		
		final Panel buttonPanel = new Panel();
		
		final HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonPanel.setContent(buttonLayout);
		buttonLayout.setMargin(true);
		final Button cancelButton = new Button("<Cancel>");
		
		cancelButton.addClickListener(event -> { viewNav.navigateTo(PersonSearchView.class); resetErrors(binder.getFields()); });
		final Button saveButton = new Button("<Save>");
		buttonLayout.addComponent(cancelButton);
		buttonLayout.addComponent(saveButton);
	
		saveButton.addClickListener(event -> { 
		
			resetErrors(binder.getFields());
			final MapBindingResult bindingResult = new MapBindingResult(toMap(binder), "person");
			
		
			ValidationUtils.invokeValidator(new PersonFieldSetValidator(), toMap(binder), bindingResult);
			for( FieldError error : bindingResult.getFieldErrors()) {
				System.out.println(error.getField() + ":" +error.getDefaultMessage() + ": " + error.getCode());
			}
			mandatoryFieldCheck(binder, Fields.Name.property());
			
			
			if( addressMandatory(binder) ) {
				for(final String addressFields : new String[]{Fields.City.property(), Fields.ZipCode.property(), Fields.Street.property(), Fields.HouseNumber.property()}){
					mandatoryFieldCheck(binder, addressFields);
				}
			} 
			
			if(StringUtils.hasText((String)binder.getField(Fields.BankIdentifierCode.property()).getValue()) ) {
				mandatoryFieldCheck(binder, Fields.IBan.property());
			}
			try {
				
				
				
				binder.commit();
			
				final Person person = itemSet2Person.convert(personItem);
				System.out.println(">>>" + person.person());
				if( person.address() != null)
				System.out.println("???" + person.address().address());
				if( person.bankingAccount() != null)
				System.out.println("!!!" + person.bankingAccount().account());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		});
		
		panel.setContent(editFormLayout);
		mainLayoout.addComponent(panel);
		mainLayoout.addComponent(buttonPanel);
		setCompositionRoot(mainLayoout);
	}



	private void mandatoryFieldCheck(final FieldGroup binder, String name ) {
		final AbstractField<?> field =  (AbstractField<?>) binder.getField(name);
		if ( ! StringUtils.hasText( (String) field.getValue() )) {
			field.setComponentError(new UserError("Mandatory"));
			
		}
	}



	private void resetErrors(final Collection<Field<?>> collection) {
		for(Field<?> field : collection){
			
			((AbstractField<?>)field).setComponentError(null);
		}
	}



	private boolean addressMandatory(final FieldGroup binder ) {
		for(final String addressFields : new String[]{Fields.City.property(), Fields.ZipCode.property(), Fields.HouseNumber.property(), Fields.HouseNumber.property()}){
			final AbstractField<?> field =  (AbstractField<?>) binder.getField(addressFields);
			if( StringUtils.hasText( (String) field.getValue()) ) {
				return true;
			}
		}
		return false;
	}





	private AbstractField<String> addInputField(final GridLayout editFormLayout, final Fields fieldDesc) {
		final HorizontalLayout  fieldLayout = new HorizontalLayout();
		fieldLayout.setMargin(true);
	
		
		editFormLayout.addComponent(fieldLayout,fieldDesc.col,fieldDesc.row);
		final TextField field = new TextField("<" + fieldDesc.name() +">");
		fieldLayout.addComponent(field);
		field.setId(fieldDesc.property());
		
		field.setRequiredError("Mussfeld");
		return field;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	private final Map<String,String> toMap(final FieldGroup group) {
		final Map<String,String> results = new HashMap<>();
		for(final Field<?> field : group.getFields() ) {
			group.getPropertyId(field);
			results.put((String) group.getPropertyId(field) , (String) field.getValue());
		}
		return results;
		
	}

}
