package de.mq.phone.web.person;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.validation.Errors;

public class DistanceValidatorTest {
	
	private DistanceValidator distanceValidator = new DistanceValidator();
	
	@Test
	public final void supports() {
		Assert.assertTrue(distanceValidator.supports(Map.class));
	}
	
	@Test
	public final void validate() {
		final Map<String,Object> map = new HashMap<>();
		map.put(PersonSearchView.DISTANCE_SEARCH_PROPERTY, "42.0");
		final Errors errors = Mockito.mock(Errors.class);
		distanceValidator.validate(map, errors);
		Mockito.verifyZeroInteractions(errors);
	}
	
	@Test
	public final void validateError() {
		final Map<String,Object> map = new HashMap<>();
		map.put(PersonSearchView.DISTANCE_SEARCH_PROPERTY, "-1");
		final Errors errors = Mockito.mock(Errors.class);
		distanceValidator.validate(map, errors);
		errors.rejectValue( PersonSearchView.DISTANCE_SEARCH_PROPERTY,  DistanceValidator.I18N_FIELD_NDC_INVALID);
		
	}
	
	@Test
	public final void validateErrorNumberformat() {
		final Map<String,Object> map = new HashMap<>();
		map.put(PersonSearchView.DISTANCE_SEARCH_PROPERTY, "x");
		final Errors errors = Mockito.mock(Errors.class);
		
		distanceValidator.validate(map, errors);
		errors.rejectValue( PersonSearchView.DISTANCE_SEARCH_PROPERTY,  DistanceValidator.I18N_FIELD_NDC_INVALID);
	}
	
	@Test
	public final void validateEmpty() {
		final Map<String,Object> map = new HashMap<>();
		map.put(PersonSearchView.DISTANCE_SEARCH_PROPERTY, "");
		final Errors errors = Mockito.mock(Errors.class);
		distanceValidator.validate(map, errors);
		
		Mockito.verifyZeroInteractions(errors);
	}

}
