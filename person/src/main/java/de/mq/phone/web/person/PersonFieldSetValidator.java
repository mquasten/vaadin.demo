package de.mq.phone.web.person;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import de.mq.phone.web.person.PersonEditView.Fields;
import de.mq.phone.web.person.ValidatorQualifier.Type;


@Component
@ValidatorQualifier(Type.Person)
public class PersonFieldSetValidator implements Validator{

	private static final String FIELD_MANDATORY = "person_field_mandatory";
	private static final String[] BANKING_ACCOUNT_FIELDS = new String[] {Fields.IBan.property() , Fields.BankIdentifierCode.property()};
	static final String[] ADDRESS_FIELDS = new String[] { Fields.City.property(), Fields.ZipCode.property(), Fields.Street.property(), Fields.HouseNumber.property()};


	@Override
	public boolean supports(Class<?> clazz) {
		return Map.class.isAssignableFrom(clazz);
	}


	@Override
	public void validate(final Object target, final Errors errors) {
		@SuppressWarnings("unchecked")
		final Map<String,String> source =  (Map<String, String>) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors,Fields.Name.property(), FIELD_MANDATORY);
		
		validateAddress(source, errors);
		
		validateAccount(source, errors);
		
	}


	private void validateAccount(final Map<String, String> source, final Errors errors) {
		if( ! required(source, BANKING_ACCOUNT_FIELDS)) {
			return;
		}
		for(final String key :  new String[] {Fields.IBan.property()}){
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, key,FIELD_MANDATORY);
		}
	}


	private void validateAddress(final Map<String, String> source, final Errors errors) {
		if( ! required(source , ADDRESS_FIELDS) ) {
			return;
		}
		for(final String key : ADDRESS_FIELDS){
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, key,FIELD_MANDATORY);
		}
	}


	private boolean required(final Map<String, String> source, String[] keys) {
		for(final String addressFields : keys){
			 if ( StringUtils.hasText(source.get(addressFields))) {
				 return true;
			 }
		}
		return false;
	}

	

	
	
}
