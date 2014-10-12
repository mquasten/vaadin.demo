package de.mq.phone.web.person;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.mq.phone.web.person.ValidatorQualifier.Type;
@Component
@ValidatorQualifier(Type.EMail)
public class EMailtValidator implements Validator{
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";  

	@Override
	public boolean supports(final Class<?> clazz) {
		
		return Map.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
	
			final String value = (String) errors.getFieldValue(ContactEditorView.Fields.Contact.property());
			if( ! value.matches(EMAIL_PATTERN)) {
				errors.rejectValue(ContactEditorView.Fields.Contact.property() , "invalid_mail_address");
			}
	
	}

	

}
