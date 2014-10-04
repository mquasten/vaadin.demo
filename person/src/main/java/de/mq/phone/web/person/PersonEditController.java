package de.mq.phone.web.person;

import java.util.Map;

import org.springframework.validation.BindingResult;

public interface PersonEditController {

	public abstract BindingResult validateAndSave(Map<String, ?> map);

}