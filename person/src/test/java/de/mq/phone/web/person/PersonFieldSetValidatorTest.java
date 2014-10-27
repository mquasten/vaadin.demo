package de.mq.phone.web.person;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.validation.MapBindingResult;

public class PersonFieldSetValidatorTest {

	private static final String BIC = "Bic-from-Kylie's-Bank";

	private static final String IBAN = "Kylies-iBan";

	private static final String HOUSE_NUMBER = "1";

	private static final String ZIP = "12345";

	private static final String STREET = "Street";

	private static final String CITY = "City";

	private static final String ALIAS = "Wild Rose";

	private static final String FIRST_NAME = "Kylie";

	private static final String NAME = "Minogue";

	private final PersonFieldSetValidator personFieldSetValidator = new PersonFieldSetValidator();

	final Map<String, Object> personAsMap = new HashMap<String, Object>();

	@Test
	public final void supports() {
		Assert.assertTrue(personFieldSetValidator.supports(Map.class));
	}

	@Test
	public final void validate() {

		personAsMap.put(PersonEditView.Fields.Name.property(), NAME);
		personAsMap.put(PersonEditView.Fields.Firstname.property(), FIRST_NAME);
		personAsMap.put(PersonEditView.Fields.Alias.property(), ALIAS);
		personAsMap.put(PersonEditView.Fields.City.property(), CITY);
		personAsMap.put(PersonEditView.Fields.Street.property(), STREET);
		personAsMap.put(PersonEditView.Fields.ZipCode.property(), ZIP);
		personAsMap.put(PersonEditView.Fields.HouseNumber.property(), HOUSE_NUMBER);
		personAsMap.put(PersonEditView.Fields.IBan.property(), IBAN);
		personAsMap.put(PersonEditView.Fields.BankIdentifierCode.property(), BIC);
		final MapBindingResult bindingResult = new MapBindingResult(personAsMap, PersonEditControllerImpl.PERSON_BINDING_NAME);

		personFieldSetValidator.validate(personAsMap, bindingResult);
		Assert.assertFalse(bindingResult.hasErrors());
	}

	@Test
	public final void validateNameOnly() {
		personAsMap.put(PersonEditView.Fields.Name.property(), NAME);
		final MapBindingResult bindingResult = new MapBindingResult(personAsMap, PersonEditControllerImpl.PERSON_BINDING_NAME);
		personFieldSetValidator.validate(personAsMap, bindingResult);
		Assert.assertFalse(bindingResult.hasErrors());
	}

	@Test
	public final void addressRequired() {
		personAsMap.put(PersonEditView.Fields.Name.property(), NAME);
		personAsMap.put(PersonEditView.Fields.HouseNumber.property(), HOUSE_NUMBER);
		final MapBindingResult bindingResult = new MapBindingResult(personAsMap, PersonEditControllerImpl.PERSON_BINDING_NAME);
		personFieldSetValidator.validate(personAsMap, bindingResult);
		Assert.assertTrue(bindingResult.hasErrors());
		Assert.assertEquals(PersonFieldSetValidator.FIELD_MANDATORY, bindingResult.getFieldError(PersonEditView.Fields.ZipCode.property()).getCode());
		Assert.assertEquals(PersonFieldSetValidator.FIELD_MANDATORY, bindingResult.getFieldError(PersonEditView.Fields.City.property()).getCode());
		Assert.assertEquals(PersonFieldSetValidator.FIELD_MANDATORY, bindingResult.getFieldError(PersonEditView.Fields.Street.property()).getCode());

	}

	@Test
	public final void addressRequiiredOther() {
		final MapBindingResult bindingResult = new MapBindingResult(personAsMap, PersonEditControllerImpl.PERSON_BINDING_NAME);
		personAsMap.put(PersonEditView.Fields.Name.property(), NAME);
		personAsMap.put(PersonEditView.Fields.City.property(), CITY);
		personAsMap.put(PersonEditView.Fields.Street.property(), STREET);
		personAsMap.put(PersonEditView.Fields.ZipCode.property(), ZIP);

		personFieldSetValidator.validate(personAsMap, bindingResult);
		Assert.assertTrue(bindingResult.hasErrors());
		Assert.assertEquals(PersonFieldSetValidator.FIELD_MANDATORY, bindingResult.getFieldError(PersonEditView.Fields.HouseNumber.property()).getCode());

	}

	@Test
	public final void accountIBanRequired() {
		final MapBindingResult bindingResult = new MapBindingResult(personAsMap, PersonEditControllerImpl.PERSON_BINDING_NAME);
		personAsMap.put(PersonEditView.Fields.Name.property(), NAME);
		personAsMap.put(PersonEditView.Fields.BankIdentifierCode.property(), BIC);
		personFieldSetValidator.validate(personAsMap, bindingResult);
		Assert.assertTrue(bindingResult.hasErrors());
		Assert.assertEquals(PersonFieldSetValidator.FIELD_MANDATORY, bindingResult.getFieldError(PersonEditView.Fields.IBan.property()).getCode());

	}

	@Test
	public final void accountWithoutBic() {
		final MapBindingResult bindingResult = new MapBindingResult(personAsMap, PersonEditControllerImpl.PERSON_BINDING_NAME);
		personAsMap.put(PersonEditView.Fields.Name.property(), NAME);
		personAsMap.put(PersonEditView.Fields.IBan.property(), IBAN);
		personFieldSetValidator.validate(personAsMap, bindingResult);
		Assert.assertFalse(bindingResult.hasErrors());
	}

	@Test
	public final void accountWithoutName() {
		final MapBindingResult bindingResult = new MapBindingResult(personAsMap, PersonEditControllerImpl.PERSON_BINDING_NAME);
		personFieldSetValidator.validate(personAsMap, bindingResult);
		Assert.assertTrue(bindingResult.hasErrors());
		Assert.assertEquals(PersonFieldSetValidator.FIELD_MANDATORY, bindingResult.getFieldError(PersonEditView.Fields.Name.property()).getCode());
	}

}
