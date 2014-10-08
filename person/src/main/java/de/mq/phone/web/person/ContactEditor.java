package de.mq.phone.web.person;

import java.util.Arrays;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
class ContactEditor extends CustomComponent {

	enum Fields {
		Mail, CountryCode, NationalDestinationCode, SubscriberNumber;

		String property() {
			return StringUtils.uncapitalize(name());
		}

		Field<?> field() {
			TextField result = new TextField("<" + property() + ">");
			result.setColumns(15);
			return result;
		}
	}

	private static final long serialVersionUID = 1L;

	@PostConstruct
	void init() {

		final VerticalLayout mainLayoout = new VerticalLayout();

		mainLayoout.setMargin(true);
		final GridLayout formLayout = new GridLayout();
		mainLayoout.addComponent(formLayout);
		formLayout.setCaption("<Kontact bearbeiten>");
		Arrays.stream(Fields.values()).forEach(field -> formLayout.addComponent(field.field()));

		setCompositionRoot(mainLayoout);
	}

}
