package de.mq.vaadin.util;

import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.validation.BindingResult;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;

public interface BindingResultsToFieldGroupMapper extends  Mapper<BindingResult, FieldGroup> , Converter<FieldGroup, Map<String,?>> {

	FieldGroup mapInto(final BindingResult bindingResults, final FieldGroup group);

	Map<String, Object> convert(final FieldGroup group);
	
	FieldGroup mapInto(final Map<String,?> bindingResults, final FieldGroup group);
	
	Map<String,?> convert(final Item item);

}