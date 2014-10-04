package de.mq.phone.web.person;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonService;

@Controller
public class PersonEditControllerImpl implements PersonEditController {
	static final String PERSON_BINDING_NAME = "person";
	private final Validator personItemSetValidator;
	private final @ConverterQualifier(ConverterQualifier.Type.Map2Person) Converter<Map<String,?>, Person> map2Person;
	private final PersonService personService;
	@Autowired
	public PersonEditControllerImpl(final PersonService personService, final @ConverterQualifier(ConverterQualifier.Type.Map2Person) Converter<Map<String,?>, Person> map2Person,final Validator personItemSetValidator) {
		this.personItemSetValidator = personItemSetValidator;
		this.map2Person=map2Person;
		this.personService=personService;
	}

	/* (non-Javadoc)
	 * @see de.mq.phone.web.person.PersonEditController#validateAndSave(java.util.Map)
	 */
	@Override
	public final BindingResult validateAndSave(final Map<String,?> map ) {
		final MapBindingResult bindingResult = new MapBindingResult(map, PERSON_BINDING_NAME);

		ValidationUtils.invokeValidator(personItemSetValidator, map, bindingResult);
		if( bindingResult.hasErrors()) {
			return bindingResult;
		}
		
		try {
			personService.save(map2Person.convert(map));
		} catch ( final Exception ex) {
			bindingResult.addError(new ObjectError(PERSON_BINDING_NAME, new String[] { "person_save_error"}, new String[] {ex.getMessage()} , null ));
		}
		
		return bindingResult;
	}

}
