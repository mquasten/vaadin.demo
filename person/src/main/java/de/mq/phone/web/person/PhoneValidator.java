package de.mq.phone.web.person;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import de.mq.phone.web.person.ValidatorQualifier.Type;

@Component
@ValidatorQualifier(Type.Phone)
public class PhoneValidator implements Validator{
	
	private static final String FIELD_MANDATORY = "person_field_mandatory";
	
	private static final String CC_PATTERN = "[1-9][0-9]{0,3}";
	private static final String NDC_PATTERN = "[1-9][0-9]{1,3}";
	private static final String SN_PATTERN = "[1-9][0-9]{2,10}";
	
	private static final String FIELD_CC_INVALID = "phone_cc_invalid";
	private static final String FIELD_NDC_INVALID = "phone_ndc_invalid";
	static final String FIELD_SN_INVALID = "phone_sn_invalid";


	
	@Override
	public boolean supports(final Class<?> clazz) {
		return Map.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(final Object target, final Errors errors) {
		
		
		final String cc = (String) errors.getFieldValue(ContactEditorView.Fields.CountryCode.property());
		
		if( StringUtils.hasText(cc)) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, ContactEditorView.Fields.NationalDestinationCode.property(), FIELD_MANDATORY);
		}
		
	
		
		if( ! validateIfNotEmpty(cc , CC_PATTERN )) {
			errors.rejectValue( ContactEditorView.Fields.CountryCode.property(), FIELD_CC_INVALID);
		}
		
		if( ! validateIfNotEmpty((String) errors.getFieldValue(ContactEditorView.Fields.NationalDestinationCode.property()) , NDC_PATTERN )) {
			errors.rejectValue( ContactEditorView.Fields.NationalDestinationCode.property(), FIELD_NDC_INVALID);
		}
		
		final String sn = (String) errors.getFieldValue(ContactEditorView.Fields.SubscriberNumber.property());
		if( ! sn.matches(SN_PATTERN) ) {
			errors.rejectValue( ContactEditorView.Fields.SubscriberNumber.property(),  FIELD_SN_INVALID);
		}
		
	}

	private boolean validateIfNotEmpty(final String value, final String pattern) {
		if(StringUtils.isEmpty(value)) {
			return true;
		}
		if( value.matches(pattern) ) {
			return true;
		}
		return false;
	}

}
