package de.mq.vaadin.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Field;

@Component
public class BindingResultsToFieldGroupMapperImpl implements BindingResultsToFieldGroupMapper {

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
				 ((AbstractComponent) group.getField(error.getField())).setComponentError(new UserError(error.getCode()));
			}
			return group; 
	}

	/* (non-Javadoc)
	 * @see de.mq.vaadin.util.BindingResultsToFieldGroupMapper#convert(com.vaadin.data.fieldgroup.FieldGroup)
	 */
	
	@Override
	public Map<String, String> convert(final FieldGroup group) {
		final Map<String,String> results = new HashMap<>();
		for(final Field<?> field : group.getFields() ) {
			group.getPropertyId(field);
			results.put((String) group.getPropertyId(field) , (String) field.getValue());
		}
		return results;
	} 

}
