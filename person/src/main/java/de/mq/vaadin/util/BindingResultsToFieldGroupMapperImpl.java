package de.mq.vaadin.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

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
		 for(Field<?> field : group.getFields()){
		   	((AbstractComponent)field).setComponentError(null);
		   }
		   if( bindingResults==null){
		   	return group; 
		   }
			for(final FieldError error :  bindingResults.getFieldErrors() ) {
				final AbstractComponent field = (AbstractComponent) group.getField(error.getField());
				field.setComponentError(new UserError(getString(error.getCode(), field.getLocale())));
			}
		
			return group; 
	}

	/* (non-Javadoc)
	 * @see de.mq.vaadin.util.BindingResultsToFieldGroupMapper#convert(com.vaadin.data.fieldgroup.FieldGroup)
	 */
	
	@Override
	public Map<String, ?> convert(final FieldGroup group) {
		final Map<String,String> results = new HashMap<>();
		for(final Field<?> field : group.getFields() ) {
			group.getPropertyId(field);
			results.put((String) group.getPropertyId(field) , (String) field.getValue());
		}
		return results;
	} 
	
	private String getString(final String key, final Locale locale) {
		return messageSource.getMessage(key, null, locale);
	}

}
