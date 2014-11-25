package de.mq.phone.web.person;

import java.util.Collection;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;

import com.vaadin.data.Item;
import com.vaadin.data.Property;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.PersonStringAware;



public class ItemToPersonSearchEntitySetConverterTest {

	private   Converter<Item, Collection<Object>>  converter = new ItemToPersonSearchEntitySetConverter();
	
	@SuppressWarnings("unchecked")
	@Test
	public final void convert() {
		final Item item = Mockito.mock(Item.class);
		final Property<String> personProperty = Mockito.mock(Property.class);
		Mockito.when(personProperty.getValue()).thenReturn("Kylie");
		Mockito.when(item.getItemProperty(PersonSearchView.PERSON_SEARCH_PROPERTY)).thenReturn(personProperty);
		
		final Property<String> contactProperty = Mockito.mock(Property.class);
		Mockito.when(contactProperty.getValue()).thenReturn("kinky.kylieqminogue.uk");
		Mockito.when(item.getItemProperty(PersonSearchView.CONTACT_SEARCH_PROPERTY)).thenReturn(contactProperty);
		
		final Property<String> addressProperty = Mockito.mock(Property.class);
		Mockito.when(addressProperty.getValue()).thenReturn("London");
		Mockito.when(item.getItemProperty(PersonSearchView.ADDRESS_SEARCH_PROPERTY)).thenReturn(addressProperty);
		final Property<String> distanceProperty = Mockito.mock(Property.class);
		Mockito.when(distanceProperty.getValue()).thenReturn("67.1");
		Mockito.when(item.getItemProperty(PersonSearchView.DISTANCE_SEARCH_PROPERTY)).thenReturn(distanceProperty);
		
		for(Object criteria : converter.convert(item)) {
			
			if (criteria instanceof PersonStringAware) {
				
				Assert.assertEquals(personProperty.getValue(), (((PersonStringAware)criteria).person()));
		        continue;
			}
			
			if (criteria instanceof Contact) {
				
				Assert.assertEquals(contactProperty.getValue(), (((Contact)criteria).contact()));
		        continue;
			}
			if (criteria instanceof AddressStringAware) {
				
				Assert.assertEquals(addressProperty.getValue(), (((AddressStringAware)criteria).address()));
		        continue;
			}
			
			if (criteria instanceof Distance) {
				Assert.assertEquals(new Distance(67.1D,Metrics.KILOMETERS), criteria);
				continue;
			}
			Assert.fail("Wrong type:" + criteria.getClass());
		}
	}
}
