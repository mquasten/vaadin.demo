package de.mq.phone.web.person;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.mq.phone.web.person.ValidatorQualifier.Type;




@Component
@ValidatorQualifier(Type.Distance)
public class DistanceValidator  implements Validator {
	
	static final String I18N_FIELD_NDC_INVALID = "distance_invalid";

	@Override
	public boolean supports(Class<?> clazz) {
		return Map.class.isAssignableFrom(clazz);
	
	}

	
	@Override
	public void validate(final Object target, Errors errors) {
		@SuppressWarnings("unchecked")
		final String value = ((Map<String,String>) target).get(PersonSearchView.DISTANCE_SEARCH_PROPERTY);
		if( ! StringUtils.hasText(value)) {
			return ;
		}
		try {
			Double x = Double.parseDouble(value);
			if( x < 0 ) {
				errors.rejectValue( PersonSearchView.DISTANCE_SEARCH_PROPERTY,  I18N_FIELD_NDC_INVALID);
			}
		} catch ( final NumberFormatException nf){
			errors.rejectValue( PersonSearchView.DISTANCE_SEARCH_PROPERTY,  I18N_FIELD_NDC_INVALID);
		}
		
	}

}
