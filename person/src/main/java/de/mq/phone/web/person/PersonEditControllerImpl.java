package de.mq.phone.web.person;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.PersonService;
import de.mq.phone.domain.person.support.PersonEntities;
import de.mq.phone.web.person.ValidatorQualifier.Type;

@Controller
public class PersonEditControllerImpl implements PersonEditController {
	static final String PERSON_BINDING_NAME = "person";
	private final Validator personItemSetValidator;
	private final Validator mailValidator;
	private final Validator phoneValidator;
	private final MapToPersonMapper map2Person;
	private final PersonService personService;
	private final ContactMapper contactMapper;
	@Autowired
	public PersonEditControllerImpl(final PersonService personService, MapToPersonMapper map2Person,@ValidatorQualifier(Type.Person) final Validator personItemSetValidator, @ValidatorQualifier(Type.EMail) final Validator mailValidator,@ValidatorQualifier(Type.Phone) final Validator phoneValidator, final ContactMapper contactMapper) {
		this.personItemSetValidator = personItemSetValidator;
		this.map2Person=map2Person;
		this.personService=personService;
		this.contactMapper=contactMapper;
		this.mailValidator=mailValidator;
		this.phoneValidator=phoneValidator;
	}

	/* (non-Javadoc)
	 * @see de.mq.phone.web.person.PersonEditController#validateAndSave(java.util.Map)
	 */
	@Override
	public final BindingResult validateAndSave(final Map<String,?> map, final PersonEditModel personEditModel ) {
		final MapBindingResult bindingResult = new MapBindingResult(map, PERSON_BINDING_NAME);

		ValidationUtils.invokeValidator(personItemSetValidator, map, bindingResult);
		if( bindingResult.hasErrors()) {
			return bindingResult;
		}
		
		try {
			personService.save(map2Person.mapInto(map, personEditModel.getPerson()));
		} catch ( final Exception ex) {
			bindingResult.addError(new ObjectError(PERSON_BINDING_NAME, new String[] { "person_save_error"}, new String[] {ex.getMessage()} , null ));
		   ex.printStackTrace();
		}
		
		return bindingResult;
	}
	
	
	public final BindingResult validateAndTakeOver(final Map<String,?> map, final PersonEditModel personEditModel ) {
		final MapBindingResult bindingResult = new MapBindingResult(map, "contact");
		
		if(  personEditModel.getCurrentContact() == null){
			return bindingResult;
		}
		
		if( bindingResult.hasErrors()) {
			return bindingResult;
		}
		
		if( personEditModel.isMailContact() ) {
			ValidationUtils.invokeValidator(mailValidator, map, bindingResult);
		}
		
		if( personEditModel.isPhoneContact()){
			ValidationUtils.invokeValidator(phoneValidator, map, bindingResult);
		}
		
		if(bindingResult.hasErrors() ){
			return bindingResult;
		}
		
		personEditModel.setCurrentContact(contactMapper.mapInto(map, personEditModel.getCurrentContact().getValue()));
		
		return bindingResult;
		
	}
	@Override
	public final void assign(final PersonEditModel personEditModel) {
		personEditModel.setPerson(PersonEntities.newPerson());
	}
	
	@Override
	public final void assign(final PersonEditModel personEditModel, final String id) {
		personEditModel.setPerson(personService.person(id));
	}
	
	@Override
	public final void assign(final  PersonEditModel personEditModel, final Entry<UUID, Contact> contactEntry) {
		personEditModel.setCurrentContact(contactEntry);
	}
	

	@Override
	public final void assign(final  PersonEditModel personEditModel,  final Class<? extends Contact> clazz){
		assign( personEditModel ,  new AbstractMap.SimpleEntry<>(new UUID(Math.round(ContactMapper.UUID_RANDOM_SCALE *  Math.random()), Math.round(ContactMapper.UUID_RANDOM_SCALE * Math.random())) , BeanUtils.instantiateClass(clazz, Contact.class)));
	}
	
	@Override
	public final Map<String,?> person(final PersonEditModel personEditModel) {
		return map2Person.convert(personEditModel.getPerson());
	}

}
