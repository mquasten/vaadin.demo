package de.mq.phone.web.person;

import java.util.Map;

import org.springframework.validation.BindingResult;

public interface PersonEditController {

	BindingResult validateAndSave(final Map<String, ?> map,  final PersonEditModel personEditModel );
	
	void assign(final PersonEditModel personEditModel);

	void assign(final PersonEditModel personEditModel, final String id);

	Map<String, ?> person(final PersonEditModel personEditModel);

}