package de.mq.phone.web.person;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

public class EMailValidatorTest {
	
	private final Validator validator = new EMailValidator();
	
	@Test
	public final void accept() {
		Assert.assertTrue(validator.supports(Map.class));
	}
	
	@Test
	public final void validateValid() {
		final MapBindingResult bindingResult =  Mockito.mock(MapBindingResult.class);
		Mockito.when(bindingResult.getFieldValue(ContactEditorView.Fields.Contact.property())).thenReturn("kinky@kylie.tv");
		
		validator.validate(Mockito.mock(Object.class), bindingResult);
		
		
		Mockito.verify(bindingResult, Mockito.times(0)).rejectValue(Mockito.anyString() , Mockito.anyString() );
	}
	
	@Test
	public final void validateInValid() {
		final MapBindingResult bindingResult =  Mockito.mock(MapBindingResult.class);
		Mockito.when(bindingResult.getFieldValue(ContactEditorView.Fields.Contact.property())).thenReturn("invalid");
		
		validator.validate(Mockito.mock(Object.class), bindingResult);

		
		Mockito.verify(bindingResult, Mockito.times(1)).rejectValue(Mockito.anyString() , Mockito.anyString() );
	}

}
