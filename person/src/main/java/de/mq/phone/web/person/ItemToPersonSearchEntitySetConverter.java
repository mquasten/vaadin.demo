package de.mq.phone.web.person;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.vaadin.data.Item;

import de.mq.phone.domain.person.support.PersonEntities;
@Component()
@ConverterQualifier(ConverterQualifier.Type.Item2PersonSearchSet)
class ItemToPersonSearchEntitySetConverter implements Converter<Item, Collection<Object>> {

	@Override
	public Collection<Object> convert(final Item source) {
		
		final Set<Object> results = new HashSet<>();
		results.add(PersonEntities.newPerson((String) source.getItemProperty(PersonSearchView.PERSON_SEARCH_PROPERTY).getValue()));
		results.add(PersonEntities.newContact((String) source.getItemProperty(PersonSearchView.CONTACT_SEARCH_PROPERTY).getValue()));
		results.add(PersonEntities.newAddressStringAware((String) source.getItemProperty(PersonSearchView.ADDRESS_SEARCH_PROPERTY).getValue()));
		
		
		final String value = (String) source.getItemProperty(PersonSearchView.DISTANCE_SEARCH_PROPERTY).getValue();
		
		if( ! StringUtils.hasText(value) ) {
		  results.add(PersonSearchModelImpl.UNDEFINED_DISTANCE);
		} else {
			final Distance  distance = new Distance(Double.parseDouble(value) , Metrics.KILOMETERS);
			 results.add(distance);
		}
		
		return results;
		
	}


}
