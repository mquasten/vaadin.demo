package de.mq.vaadin.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Field;

@Component
class BindingResultsToFieldGroupMapperImpl implements BindingResultsToFieldGroupMapper {

	private final MessageSource messageSource;
	
	@Autowired
	BindingResultsToFieldGroupMapperImpl(final MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/* (non-Javadoc)
	 * @see de.mq.vaadin.util.BindingResultsToFieldGroupMapper#mapInto(org.springframework.validation.BindingResult, com.vaadin.data.fieldgroup.FieldGroup)
	 */
	@Override
	public FieldGroup mapInto(final BindingResult bindingResults, final FieldGroup group) {
		group.getFields().forEach(field -> ((AbstractComponent) field).setComponentError(null));
		if (bindingResults == null) {
			return group;
		}
		bindingResults.getFieldErrors().forEach(error -> {
			final AbstractComponent field = (AbstractComponent) group.getField(error.getField());
			field.setComponentError(new UserError(getString(error.getCode(), field.getLocale())));
		});
		return group;
	}

	/* (non-Javadoc)
	 * @see de.mq.vaadin.util.BindingResultsToFieldGroupMapper#convert(com.vaadin.data.fieldgroup.FieldGroup)
	 */
	
	@Override
	public Map<String,Object> convert(final FieldGroup group) {
		final Map<String,Object> results = new HashMap<>();
		group.getFields().forEach(field -> results.put((String) group.getPropertyId(field) , (String) field.getValue()) );
		return results;
	} 
	
	private String getString(final String key, final Locale locale) {
		return messageSource.getMessage(key, null, locale);
	}

	@SuppressWarnings("unchecked")
	@Override
	public FieldGroup mapInto(final Map<String, ?> values, final FieldGroup group) {
		group.getFields().forEach(field ->  ((Field<Object>) field).setValue(values.get((String) group.getPropertyId(field))));
		return group;
	}

	@Override
	public Map<String, ?> convert(final Item item) {
		final Map<String, Object> results = new HashMap<>();
		if( item == null){
			return results;
		}
		item.getItemPropertyIds().forEach(id -> { 
			results.put((String) id,item.getItemProperty(id).getValue());
		});
		return  results;
	}
	
	

}
