package de.mq.phone.web.person;

import java.util.Map;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import de.mq.phone.web.person.PersonEditView.Fields;



public class PersonFieldSetValidator implements Validator{

	@Override
	public boolean supports(Class<?> clazz) {
		return Map.class.isAssignableFrom(clazz);
	}


	@Override
	public void validate(final Object target, final Errors errors) {
		@SuppressWarnings("unchecked")
		final Map<String,String> source =  (Map<String, String>) target;
		ValidationUtils.rejectIfEmptyOrWhitespace(errors,Fields.Name.property(), "Mandatory");
		System.out.println("*****");
		System.out.println(source);
	
	}

	

	
	
}
