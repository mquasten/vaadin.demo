package de.mq.phone.web.person;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.vaadin.data.util.PropertysetItem;

import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.support.PersonEntities;

@Component
@ConverterQualifier(ConverterQualifier.Type.Item2Person)
public class PersonItemToPersonConverterImpl implements Converter<PropertysetItem, Person>{

	@Override
	public Person convert(final PropertysetItem items) {
		final Person person = PersonEntities.newPerson();
		ReflectionUtils.doWithFields(person.getClass(),  field -> { 
		
			
			if( items.getItemPropertyIds().contains(field.getName()) ) {
				field.setAccessible(true);
			   field.set(person, items.getItemProperty(field.getName()).getValue());
			}
			
			
		} );
		
		
		return person;
	}

}
