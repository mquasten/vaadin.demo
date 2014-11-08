package de.mq.vaadin.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Field;

public class BindingResultsToFieldGroupMapperTest {

	private static final String FIELD_VALUE = "value";

	private static final String MESSAGE = "Field name: validation sucks";

	private static final String MESSAGE_CODE = "code";

	private static final String FIELD_NAME = "name";

	private final MessageSource messageSource = Mockito.mock(MessageSource.class);

	private final BindingResultsToFieldGroupMapper mapper = new BindingResultsToFieldGroupMapperImpl(messageSource);

	@SuppressWarnings("unchecked")
	@Test
	public final void mapInto() {
		final BindingResult bindingResults = Mockito.mock(BindingResult.class);
		final List<FieldError> errors = new ArrayList<>();
		final FieldError error = Mockito.mock(FieldError.class);
		Mockito.when(error.getField()).thenReturn(FIELD_NAME);
		Mockito.when(error.getCode()).thenReturn(MESSAGE_CODE);

		errors.add(error);

		Mockito.when(bindingResults.getFieldErrors()).thenReturn(errors);
		final FieldGroup fieldGroup = Mockito.mock(FieldGroup.class);
		final Collection<Field<?>> fields = new ArrayList<>();
		Mockito.when(fieldGroup.getFields()).thenReturn(fields);
		@SuppressWarnings("rawtypes")
		AbstractField field = Mockito.mock(AbstractField.class);
		fields.add(field);
		Mockito.when(field.getLocale()).thenReturn(Locale.GERMAN);
		Mockito.when(fieldGroup.getField(FIELD_NAME)).thenReturn(field);
		Mockito.when(messageSource.getMessage(MESSAGE_CODE, null, Locale.GERMAN)).thenReturn(MESSAGE);
		final ArgumentCaptor<ErrorMessage> errorMessageCaptor = ArgumentCaptor.forClass(ErrorMessage.class);

		Assert.assertEquals(fieldGroup, mapper.mapInto(bindingResults, fieldGroup));
		Mockito.verify(field, Mockito.times(2)).setComponentError(errorMessageCaptor.capture());

		Assert.assertEquals(2, errorMessageCaptor.getAllValues().size());

		Assert.assertNull(errorMessageCaptor.getAllValues().get(0));
		Assert.assertEquals(MESSAGE, errorMessageCaptor.getAllValues().get(1).toString());
	}

	@Test
	public final void mapIntoBindingResultsNUll() {
		final FieldGroup fieldGroup = Mockito.mock(FieldGroup.class);
		final Collection<Field<?>> fields = new ArrayList<>();
		Mockito.when(fieldGroup.getFields()).thenReturn(fields);
		@SuppressWarnings("rawtypes")
		final AbstractField field = Mockito.mock(AbstractField.class);
		fields.add(field);
		Assert.assertEquals(fieldGroup, mapper.mapInto((BindingResult) null, fieldGroup));

		final ArgumentCaptor<ErrorMessage> errorMessageCaptor = ArgumentCaptor.forClass(ErrorMessage.class);
		Mockito.verify(field, Mockito.times(1)).setComponentError(errorMessageCaptor.capture());
		Assert.assertNull(errorMessageCaptor.getValue());

	}
	
	@Test
	public final void convert() {
		final FieldGroup fieldGroup = Mockito.mock(FieldGroup.class);
		@SuppressWarnings("unchecked")
		final Field<String> field = Mockito.mock(Field.class);
		Mockito.when(field.getValue()).thenReturn(FIELD_VALUE);
		Mockito.when(fieldGroup.getPropertyId(field)).thenReturn(FIELD_NAME);
		Collection<Field<?>> fields = new ArrayList<>();
		fields.add(field);
		Mockito.when(fieldGroup.getFields()).thenReturn(fields);
		
		final Map<String, Object> result = mapper.convert(fieldGroup);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(FIELD_NAME, result.keySet().iterator().next());
		Assert.assertEquals(FIELD_VALUE, result.values().iterator().next());
	}
	
	@Test
	public final void mapIntoFieldGroup() {
		final FieldGroup group = Mockito.mock(FieldGroup.class);
		final List<Field<?>> fields = new ArrayList<>();
		@SuppressWarnings("unchecked")
		final Field<Object> field = Mockito.mock(Field.class);
		fields.add(field);
		Mockito.when(group.getFields()).thenReturn(fields);
		Mockito.when(group.getPropertyId(field)).thenReturn(FIELD_NAME);
		
		final Map<String,Object> map = new HashMap<>();
		map.put(FIELD_NAME, FIELD_VALUE);
		mapper.mapInto(map, group);
	
		Mockito.verify(field, Mockito.times(1)).setValue(FIELD_VALUE);
		
	}
	

	@SuppressWarnings("unchecked")
	@Test
	public final void convertItem() {
		final Item item  = Mockito.mock(Item.class);
		
		final Property<String> property = Mockito.mock(Property.class);
		Mockito.when(property.getValue()).thenReturn(FIELD_VALUE);
		Mockito.when(item.getItemProperty(FIELD_NAME)).thenReturn(property);
		@SuppressWarnings("rawtypes")
		final Collection ids = new ArrayList<>();
		ids.add(FIELD_NAME);
		Mockito.when(item.getItemPropertyIds()).thenReturn(   ids);
		
		final Map<String, ?> result = mapper.convert(item);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(FIELD_NAME, result.keySet().iterator().next());
		Assert.assertEquals(FIELD_VALUE, result.values().iterator().next());
	}
	
	@Test
	public final void convertItemNull() {
		 Assert.assertTrue(mapper.convert((Item) null).isEmpty());
	}

}
