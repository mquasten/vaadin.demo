package de.mq.phone.web.person;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonService;
import de.mq.phone.domain.person.support.PersonEntities;

public class PersonEditControllerTest {
	private static final String PERSON_ID = "19680528";
	private final PersonService personService = Mockito.mock(PersonService.class);
	private final MapToPersonMapper mapToPersonMapper = Mockito.mock(MapToPersonMapper.class);
	private Validator personItemSetValidator = Mockito.mock(Validator.class);
	private final Validator mailValidator = Mockito.mock(Validator.class);
	private final Validator phoneValidator = Mockito.mock(Validator.class);
	private final ContactMapper contactMapper = Mockito.mock(ContactMapper.class);

	private final PersonEditModel personEditModel = Mockito.mock(PersonEditModel.class);
	private final Person person = Mockito.mock(Person.class);
	
	@SuppressWarnings("unchecked")
	private final Entry<UUID, Contact> currentContact = Mockito.mock(Entry.class);
	private final Contact contact = Mockito.mock(Contact.class);

	private final PersonEditController personEditController = new PersonEditControllerImpl(personService, mapToPersonMapper, personItemSetValidator, mailValidator, phoneValidator, contactMapper);

	@SuppressWarnings("unchecked")
	private final Map<String, Object> objectAsMap = Mockito.mock(Map.class);

	@Test
	public final void validateAndSave() {

		Mockito.when(personItemSetValidator.supports(objectAsMap.getClass())).thenReturn(true);
		Mockito.when(personEditModel.getPerson()).thenReturn(person);
		Mockito.when(mapToPersonMapper.mapInto(objectAsMap, person)).thenReturn(person);

		Assert.assertFalse(personEditController.validateAndSave(objectAsMap, personEditModel).hasErrors());
		Mockito.verify(personService, Mockito.times(1)).save(person);
		Mockito.verify(personItemSetValidator, Mockito.times(1)).validate(Mockito.anyObject(), Mockito.any(Errors.class));
	}

	@Test
	public final void validateAndSaveValidationSucks() {
		Mockito.when(personItemSetValidator.supports(objectAsMap.getClass())).thenReturn(true);
		Mockito.when(personEditModel.getPerson()).thenReturn(person);

		Mockito.doAnswer(invocation -> {
			((BindingResult) invocation.getArguments()[1]).rejectValue("name", PersonFieldSetValidator.FIELD_MANDATORY);
			return null;
		}).when(personItemSetValidator).validate(Mockito.anyObject(), Mockito.any(Errors.class));

		Assert.assertTrue(personEditController.validateAndSave(objectAsMap, personEditModel).hasErrors());
	
		Mockito.verify(personService, Mockito.times(0)).save(Matchers.anyObject());

	}
	@Test
	public final void validateAndSaveSaveSucks() {
		Mockito.when(personItemSetValidator.supports(objectAsMap.getClass())).thenReturn(true);
		Mockito.when(personEditModel.getPerson()).thenReturn(person);
		Mockito.when(mapToPersonMapper.mapInto(objectAsMap, person)).thenReturn(person);
		
		Mockito.doThrow(new InvalidDataAccessApiUsageException("Don't worry only for test")).when(personService).save(person);
		BindingResult bindingResults = personEditController.validateAndSave(objectAsMap, personEditModel);
		Assert.assertTrue(bindingResults.hasErrors());
		Assert.assertEquals(1, bindingResults.getGlobalErrors().size());
		Assert.assertEquals(PersonEditControllerImpl.PERSON_SAVE_ERROR_CODE, bindingResults.getGlobalErrors().iterator().next().getCode());
		Assert.assertEquals(PersonEditControllerImpl.PERSON_BINDING_NAME, bindingResults.getGlobalErrors().iterator().next().getObjectName());
	}
	

	@Test
	public final void validateAndTakeOverMail() {
		Mockito.when(personEditModel.isMailContact()).thenReturn(true);
		Mockito.when(mailValidator.supports(objectAsMap.getClass())).thenReturn(true);
		Mockito.when(currentContact.getValue()).thenReturn(contact);
		Mockito.when(personEditModel.getCurrentContact()).thenReturn(currentContact);
		Mockito.when(contactMapper.mapInto(objectAsMap, contact)).thenReturn(contact);
		
		Assert.assertFalse(personEditController.validateAndTakeOver(objectAsMap, personEditModel).hasErrors());
		
		Mockito.verify(mailValidator, Mockito.times(1)).validate(Matchers.anyObject(), Mockito.any(Errors.class));
		Mockito.verify(personEditModel, Mockito.times(1)).setCurrentContact(contact);
	}
	
	@Test
	public final void validateAndTakeOverPhoneValidationSucks() {
		Mockito.when(personEditModel.isPhoneContact()).thenReturn(true);
		Mockito.when(phoneValidator.supports(objectAsMap.getClass())).thenReturn(true);
		Mockito.when(currentContact.getValue()).thenReturn(contact);
		Mockito.when(personEditModel.getCurrentContact()).thenReturn(currentContact);
		
		Mockito.doAnswer(invocation -> {
			((BindingResult) invocation.getArguments()[1]).rejectValue(ContactEditorView.Fields.SubscriberNumber.property(), PhoneValidator.FIELD_SN_INVALID);
			return invocation;
			
		}).when(phoneValidator).validate(Matchers.anyObject(), Mockito.any(Errors.class));
		
		Assert.assertTrue(personEditController.validateAndTakeOver(objectAsMap, personEditModel).hasErrors());
		
		Mockito.verify(personEditModel, Mockito.times(0)).setCurrentContact(contact);
	}
	
	@Test
	public final void  validateAndTakeOverContactIsNull() {
		Assert.assertFalse(personEditController.validateAndTakeOver(objectAsMap, personEditModel).hasErrors());
		
		Mockito.verifyZeroInteractions(phoneValidator, mailValidator,contactMapper);
		Mockito.verify(personEditModel, Mockito.times(0)).setCurrentContact(contact);
	}
	
	@Test
	public final void assignNewPerson() {
		personEditController.assign(personEditModel);
		
		ArgumentCaptor<Person> personArgumentCaptor = ArgumentCaptor.forClass(Person.class);
		Mockito.verify(personEditModel).setPerson(personArgumentCaptor.capture());
	   Assert.assertTrue( (personArgumentCaptor.getValue().getClass().equals(PersonEntities.newPerson().getClass())));
	 
	}
	
	@Test
	public final void assignExistingPerson() {
		Mockito.when(personService.person(PERSON_ID)).thenReturn(person);
		personEditController.assign(personEditModel, PERSON_ID);
		
		Mockito.verify(personEditModel, Mockito.times(1)).setPerson(person);
	}
	
	@Test
	public final void assignContactEntry() {
		personEditController.assign(personEditModel, currentContact);
		
		Mockito.verify(personEditModel, Mockito.times(1)).setCurrentContact(currentContact);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void assignNewContact() {
		personEditController.assign(personEditModel, PersonEntities.ContactType.Phone.type());
		
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Entry> entryArgumentCaptor =  ArgumentCaptor.forClass(Entry.class);
		Mockito.verify(personEditModel).setCurrentContact(entryArgumentCaptor.capture());
		
		Assert.assertNotNull(entryArgumentCaptor.getValue().getKey());
		Assert.assertEquals(PersonEntities.ContactType.Phone.type(), entryArgumentCaptor.getValue().getValue().getClass());
	}
	
	@Test
	public final void personAsmap() {
		Mockito.when(personEditModel.getPerson()).thenReturn(person);
		Mockito.when(mapToPersonMapper.convert(person)).thenReturn(objectAsMap);
		Assert.assertEquals(objectAsMap, personEditController.person(personEditModel));
	}
	@Test
	public final void delete() {
		Mockito.when(personEditModel.isIdAware()).thenReturn(true);
		
		Mockito.when(personEditModel.getPerson()).thenReturn(person);
		Mockito.when(person.id()).thenReturn(PERSON_ID);
		
		personEditController.delete(personEditModel);
		Mockito.verify(personService, Mockito.times(1)).deletePerson(PERSON_ID);
	}
	
	@Test
	public final void deleteNotPersistent() {
		personEditController.delete(personEditModel);
	   Mockito.verifyZeroInteractions(personService);
	}

}
