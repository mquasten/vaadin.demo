package de.mq.phone.web.person;

import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.validation.BindingResult;

import de.mq.phone.domain.person.Contact;

public interface PersonEditController {

	BindingResult validateAndSave(final Map<String, ?> map,  final PersonEditModel personEditModel );
	
	void assign(final PersonEditModel personEditModel);

	void assign(final PersonEditModel personEditModel, final String id);

	Map<String, ?> person(final PersonEditModel personEditModel);

	void assign(PersonEditModel personEditModel,Entry<UUID,Contact> contactEntry);
	BindingResult validateAndTakeOver(final Map<String,?> map, final PersonEditModel personEditModel );

	void assign(final PersonEditModel personEditModel, final Class<? extends Contact> clazz);

}