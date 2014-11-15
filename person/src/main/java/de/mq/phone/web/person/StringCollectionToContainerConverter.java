package de.mq.phone.web.person;

import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
@Component()
@ConverterQualifier(value = ConverterQualifier.Type.StringList2Container)
public class StringCollectionToContainerConverter implements Converter<Collection<String>, Container> {

	@SuppressWarnings("unchecked")
	@Override
	public Container convert(Collection<String> source) {
		final Container container = new IndexedContainer();
		container.addContainerProperty(PersonSearchView.CONTACTS, String.class, "");
		source.forEach(contact -> container.getItem(container.addItem()).getItemProperty(PersonSearchView.CONTACTS).setValue(contact));
		return container; 	
	}

}
