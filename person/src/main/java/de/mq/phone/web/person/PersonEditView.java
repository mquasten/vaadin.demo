package de.mq.phone.web.person;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ErrorMessage;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
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
	
		for(final Fields field : Fields.values()) {
			personItem.addItemProperty(field.property(), new ObjectProperty<String>(""));
			binder.bind(addInputField(editFormLayout, field), field.property());
		}
		
		final Panel buttonPanel = new Panel();
		
		final HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonPanel.setContent(buttonLayout);
		buttonLayout.setMargin(true);
		final Button cancelButton = new Button("<Cancel>");
		
		cancelButton.addClickListener(event ->  viewNav.navigateTo(PersonSearchView.class));
		final Button saveButton = new Button("<Save>");
		buttonLayout.addComponent(cancelButton);
		buttonLayout.addComponent(saveButton);
	
		saveButton.addClickListener(event -> { 
			try {
				
				((AbstractField<?>) binder.getField(Fields.Name.property())).setComponentError(new UserError("Bad click"));
				binder.commit();
			
				Person person = itemSet2Person.convert(personItem);
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





	private AbstractField<?> addInputField(final GridLayout editFormLayout, final Fields fieldDesc) {
		final HorizontalLayout  fieldLayout = new HorizontalLayout();
		fieldLayout.setMargin(true);
	
		
		editFormLayout.addComponent(fieldLayout,fieldDesc.col,fieldDesc.row);
		final TextField field = new TextField("<" + fieldDesc.name() +">");
		fieldLayout.addComponent(field);
		
		field.setRequiredError("Mussfeld");
		return field;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}
