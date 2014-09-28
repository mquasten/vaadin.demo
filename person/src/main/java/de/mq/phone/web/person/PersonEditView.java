package de.mq.phone.web.person;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import de.mq.vaadin.util.ViewNav;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
class PersonEditView extends CustomComponent implements View {
	

	enum Fields {
		Name(0,0),
		FirstName(0,1),
		Alias(0,2),
		Street(1,0),
		HouseNumber(1,1),
		Zip(1,2),
		City(1,3),
		
		Iban(2,0),
		Bic(2,1);
		
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
	
	@Autowired
	PersonEditView(final ViewNav viewNav) {
		this.viewNav=viewNav;
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
			addInputField(editFormLayout, field,binder);
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
	
		
		panel.setContent(editFormLayout);
		mainLayoout.addComponent(panel);
		mainLayoout.addComponent(buttonPanel);
		setCompositionRoot(mainLayoout);
	}



	


	

	private AbstractField<?> addInputField(final GridLayout editFormLayout, final Fields fieldDesc, final FieldGroup binder) {
		final HorizontalLayout  fieldLayout = new HorizontalLayout();
		fieldLayout.setMargin(true);
	
		
		editFormLayout.addComponent(fieldLayout,fieldDesc.col,fieldDesc.row);
		final TextField field = new TextField("<" + fieldDesc.name() +">");
		fieldLayout.addComponent(field);
		binder.bind(field, fieldDesc.property());
		return field;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		System.out.println("***");
		System.out.println(event.getParameters());
	}

}
