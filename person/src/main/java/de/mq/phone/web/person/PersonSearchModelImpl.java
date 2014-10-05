package de.mq.phone.web.person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonStringAware;
import de.mq.phone.domain.person.support.PersonEntities;
import de.mq.vaadin.util.SubjectImpl;

@Component
@Scope("session")
class PersonSearchModelImpl extends SubjectImpl<PersonSearchModel, PersonSearchModel.EventType> implements PersonSearchModel {
	
	
	private final Map<Class<?>,Object> search = new HashMap<>();
	
	
	
	private final List<Person> persons;
	
	PersonSearchModelImpl() {
		this.persons = new ArrayList<>();
		search.put(PersonStringAware.class, PersonEntities.newPerson());
		search.put(AddressStringAware.class, PersonEntities.newAddressStringAware(null));
		search.put(Contact.class, PersonEntities.newContact(null));
	}


	
	
	
	/* (non-Javadoc)
	 * @see de.mq.phone.web.person.PersonSEarchModel#setPersons(java.util.List)
	 */
	@Override
	public final void setPersons(final List<Person> persons) {
		this.persons.clear();
		this.persons.addAll(persons);
		notifyObservers(this, EventType.PersonsChanges);
	}


	/* (non-Javadoc)
	 * @see de.mq.phone.web.person.PersonSEarchModel#getPersons()
	 */
	@Override
	public final  List<Person> getPersons() {
		return Collections.unmodifiableList(persons);
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.phone.web.person.PersonSEarchModel#setSearchCriteria(java.lang.Object)
	 */
	@Override
	public  final void  setSearchCriteria(final Object bean) {
		
	for(final Class<?> clazz : search.keySet()) {
			if (clazz.isInstance(bean) ) {
				search.put(clazz, bean);
				return;
			}
		} 
		throw new IllegalArgumentException("Wrong Type for search " + bean.getClass());
	}
	
	
	@Override
	public final PersonStringAware getSearchCriteriaPerson() {
		return  (PersonStringAware) search.get(PersonStringAware.class);
	}
	
	@Override
	public final Contact getSearchCriteriaContact() {
		return    (Contact) search.get(Contact.class);
	}
	@Override
	public final AddressStringAware getSearchCriteriaAddress() {
		return   (AddressStringAware) search.get(AddressStringAware.class);
	}

}
