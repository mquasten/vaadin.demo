package de.mq.phone.web.person;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonService;

public class PersonEditControllerTest {
	private final PersonService personService = Mockito.mock(PersonService.class);
	private final MapToPersonMapper mapToPersonMapper = Mockito.mock(MapToPersonMapper.class);
	private Validator personItemSetValidator = Mockito.mock(Validator.class);
	private final Validator mailValidator = Mockito.mock(Validator.class);
	private final Validator phoneValidator = Mockito.mock(Validator.class);
	private final ContactMapper contactMapper = Mockito.mock(ContactMapper.class);

	private final PersonEditModel personEditModel = Mockito.mock(PersonEditModel.class);
	private final Person person = Mockito.mock(Person.class);

	private final PersonEditController personEditController = new PersonEditControllerImpl(personService, mapToPersonMapper, personItemSetValidator, mailValidator, phoneValidator, contactMapper);

	@SuppressWarnings("unchecked")
	private final Map<String, Object> personAsMap = Mockito.mock(Map.class);

	@Test
	public final void validateAndSave() {

		Mockito.when(personItemSetValidator.supports(personAsMap.getClass())).thenReturn(true);
		Mockito.when(personEditModel.getPerson()).thenReturn(person);
		Mockito.when(mapToPersonMapper.mapInto(personAsMap, person)).thenReturn(person);

		Assert.assertFalse(personEditController.validateAndSave(personAsMap, personEditModel).hasErrors());
		Mockito.verify(personService, Mockito.times(1)).save(person);
		Mockito.verify(personItemSetValidator, Mockito.times(1)).validate(Mockito.anyObject(), Mockito.any(Errors.class));
	}

	@Test
	public final void validateAndSaveValidationSucks() {
		Mockito.when(personItemSetValidator.supports(personAsMap.getClass())).thenReturn(true);
		Mockito.when(personEditModel.getPerson()).thenReturn(person);

		Mockito.doAnswer(invocation -> {
			((BindingResult) invocation.getArguments()[1]).rejectValue("name", PersonFieldSetValidator.FIELD_MANDATORY);
			return null;
		}).when(personItemSetValidator).validate(Mockito.anyObject(), Mockito.any(Errors.class));

		Assert.assertTrue(personEditController.validateAndSave(personAsMap, personEditModel).hasErrors());
	
		Mockito.verify(personService, Mockito.times(0)).save(Matchers.anyObject());

	}
	@Test
	public final void validateAndSaveSaveSucks() {
		Mockito.when(personItemSetValidator.supports(personAsMap.getClass())).thenReturn(true);
		Mockito.when(personEditModel.getPerson()).thenReturn(person);
		Mockito.when(mapToPersonMapper.mapInto(personAsMap, person)).thenReturn(person);
		
		Mockito.doThrow(new InvalidDataAccessApiUsageException("Don't worry only for test")).when(personService).save(person);
		BindingResult bindingResults = personEditController.validateAndSave(personAsMap, personEditModel);
		Assert.assertTrue(bindingResults.hasErrors());
		Assert.assertEquals(1, bindingResults.getGlobalErrors().size());
		Assert.assertEquals(PersonEditControllerImpl.PERSON_SAVE_ERROR_CODE, bindingResults.getGlobalErrors().iterator().next().getCode());
		Assert.assertEquals(PersonEditControllerImpl.PERSON_BINDING_NAME, bindingResults.getGlobalErrors().iterator().next().getObjectName());
	}
	
	@Test
	public final void validateAndTakeOverMail() {
		
	}

}
