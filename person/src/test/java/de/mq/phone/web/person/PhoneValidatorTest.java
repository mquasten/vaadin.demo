package de.mq.phone.web.person;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

public class PhoneValidatorTest {
	
	private static final String SN = "4711";
	private static final String NDC = "2434";
	private static final String CC = "49";
	private final Validator validator = new PhoneValidator();
	final Map<String,String> values = new HashMap<>();
	final BindingResult bindingResult =new MapBindingResult(values, "contact");
	
	@Test
	public final void supports() {
		Assert.assertTrue(validator.supports(Map.class));
	}
	
	@Test
	public final void validate() {
		
		values.put(ContactEditorView.Fields.CountryCode.property(), CC);
		values.put(ContactEditorView.Fields.NationalDestinationCode.property(), NDC);
		values.put(ContactEditorView.Fields.SubscriberNumber.property(), SN);
	
	
		validator.validate(null, bindingResult);
		
		Assert.assertFalse(bindingResult.hasErrors());
	}
	
	@Test
	public final void validateInValidSn() {
		values.put(ContactEditorView.Fields.CountryCode.property(), CC);
		values.put(ContactEditorView.Fields.NationalDestinationCode.property(), NDC);
		values.put(ContactEditorView.Fields.SubscriberNumber.property(), "12");
		validator.validate(null, bindingResult);
		Assert.assertTrue(bindingResult.hasErrors());
		Assert.assertEquals(PhoneValidator.FIELD_SN_INVALID , bindingResult.getFieldError(ContactEditorView.Fields.SubscriberNumber.property()).getCode());
	}
	@Test
	public final void missingNDC() {
		values.put(ContactEditorView.Fields.CountryCode.property(), CC);
		values.put(ContactEditorView.Fields.SubscriberNumber.property(), SN);
		validator.validate(null, bindingResult);
		
		Assert.assertTrue(bindingResult.hasErrors());
		Assert.assertEquals(PhoneValidator.FIELD_MANDATORY, bindingResult.getFieldError(ContactEditorView.Fields.NationalDestinationCode.property()).getCode());
	}
	@Test
	public final void nationNumberInvalid() {
		values.put(ContactEditorView.Fields.NationalDestinationCode.property(), "12345");
		values.put(ContactEditorView.Fields.SubscriberNumber.property(), SN);
		validator.validate(null, bindingResult);
		Assert.assertTrue(bindingResult.hasErrors());
		Assert.assertEquals(PhoneValidator.FIELD_NDC_INVALID, bindingResult.getFieldError(ContactEditorView.Fields.NationalDestinationCode.property()).getCode());
		
	}
	@Test
	public final void invalidCC() {
		values.put(ContactEditorView.Fields.CountryCode.property(), "12345");
		values.put(ContactEditorView.Fields.NationalDestinationCode.property(), NDC);
		values.put(ContactEditorView.Fields.SubscriberNumber.property(), SN);
		validator.validate(null, bindingResult);
		Assert.assertTrue(bindingResult.hasErrors());
		Assert.assertEquals(PhoneValidator.FIELD_CC_INVALID, bindingResult.getFieldError(ContactEditorView.Fields.CountryCode.property()).getCode());
		
	}

}
