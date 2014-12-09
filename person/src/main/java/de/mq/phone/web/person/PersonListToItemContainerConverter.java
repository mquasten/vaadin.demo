package de.mq.phone.web.person;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import de.mq.phone.domain.person.GeoCoordinates;
import de.mq.phone.domain.person.Person;

@Component()
@ConverterQualifier(value = ConverterQualifier.Type.PersonList2Container)
class PersonListToItemContainerConverter  implements Converter<Collection<Person>, Container> {

	

   @SuppressWarnings("unchecked")
	@Override
	public Container convert(final Collection<Person> persons) {
        final Container ic = new IndexedContainer();

	
		ic.addContainerProperty(PersonSearchView.PERSON, String.class, "");
	
		ic.addContainerProperty(PersonSearchView.ADDRESS, String.class, "");
		ic.addContainerProperty(PersonSearchView.CONTACTS, Collection.class, new ArrayList<>());
		ic.addContainerProperty(PersonSearchView.BANKING_ACCOUNT, String.class, "");
		ic.addContainerProperty(PersonSearchView.COORDINATES, GeoCoordinates.class, null);

		persons.forEach(person -> {

			final Item item = ic.addItem(person.id());

			if (person.person() != null) {
				item.getItemProperty(PersonSearchView.PERSON).setValue(person.person());
			}
			if (person.address() != null) {
				item.getItemProperty(PersonSearchView.ADDRESS).setValue(person.address().address());
			}
			
			if( person.hasGeoCoordinates() ) {
				item.getItemProperty(PersonSearchView.COORDINATES).setValue(person.address().coordinates());
			}

			final Collection<String> contacts = new ArrayList<>();
			
			person.contacts().forEach(contact -> contacts.add(contact.contact()));
			
			item.getItemProperty(PersonSearchView.CONTACTS).setValue(contacts);
			if (person.bankingAccount() != null) {
				item.getItemProperty(PersonSearchView.BANKING_ACCOUNT).setValue(person.bankingAccount().account());
			}
		});

	    return ic; 
	}

	

}
