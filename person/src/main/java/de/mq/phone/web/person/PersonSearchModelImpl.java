package de.mq.phone.web.person;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.GeoCoordinates;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonStringAware;
import de.mq.phone.domain.person.support.ModifyablePaging;
import de.mq.phone.domain.person.support.Paging;
import de.mq.phone.domain.person.support.PersonEntities;
import de.mq.phone.domain.person.support.SimpleResultSetPagingImpl;
import de.mq.vaadin.util.SubjectImpl;

@Component
@Scope("session")
class PersonSearchModelImpl extends SubjectImpl<PersonSearchModel, PersonSearchModel.EventType> implements PersonSearchModel {
	
	
	private final Map<Class<?>,Object> search = new HashMap<>();
	
	private GeoCoordinates geoCoordinates ; 
	
	private final List<Person> persons;
	private ModifyablePaging paging = new SimpleResultSetPagingImpl();
	 
	
	static final Distance UNDEFINED_DISTANCE = undefinedDistance();
	static final Circle UNDEFINED_CIRCLE = undefinedCircle();
	
	PersonSearchModelImpl() {
		this.persons = new ArrayList<>();
		search.put(PersonStringAware.class, PersonEntities.newPerson());
		search.put(AddressStringAware.class, PersonEntities.newAddressStringAware(null));
		search.put(Contact.class, PersonEntities.newContact(null));
		search.put(Distance.class, UNDEFINED_DISTANCE);
	}


	private static Distance undefinedDistance() {
		final Distance distance =  new Distance(0, Metrics.KILOMETERS);
		final Field field = ReflectionUtils.findField(Distance.class, "value");
		field.setAccessible(true);
		ReflectionUtils.setField(field, distance, -1d);
		return distance;
	}
	
	private static Circle undefinedCircle() {
		final Circle circle = new Circle(new Point(0d ,  0d ), new Distance(0)); 
		final Field field = ReflectionUtils.findField(Circle.class, "radius");
		field.setAccessible(true);
		ReflectionUtils.setField(field, circle, UNDEFINED_DISTANCE);
		return circle;
		
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.phone.web.person.PersonSEarchModel#setPersons(java.util.List)
	 */
	@Override
	public final void setPersons(final List<Person> persons) {
		this.persons.clear();
		this.persons.addAll(persons);
		notifyObservers(EventType.PersonsChanges);
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

	@Override
	public void setGeoCoordinates(final GeoCoordinates geoCoordinates) {
		this.geoCoordinates=geoCoordinates;
		notifyObservers(EventType.HomeLocationChanges);
	}
	
	@Override
	public final Circle getSearchCriteriaDistance() {
		 if( geoCoordinates == null) {
			 return UNDEFINED_CIRCLE;
		 }
		 final Distance distance = (Distance) search.get(Distance.class);
		 if( distance.getValue() >= 0) {
			 return new Circle(new Point(geoCoordinates.longitude() , geoCoordinates.latitude()), distance);
		 }
		return PersonSearchModelImpl.UNDEFINED_CIRCLE;
	}
	
	@Override
	public final boolean hasGeoCoordinates() {
		return this.geoCoordinates!=null;
	}
	
	@Override
	public final GeoCoordinates getGeoCoordinates() {
		return geoCoordinates;
	}
	@Override
	public final Paging getPaging() {
		return paging;
	}

	@Override
	public final  void setPaging(final ModifyablePaging paging) {
		this.paging = paging;
		notifyObservers(EventType.PagingChanges);
	}
	
	@Override
	public  final void incPaging() {
		paging.inc();
		notifyObservers(EventType.PagingChanges);
	}
	
	@Override
	public  final void endPaging() {
		paging.last();
		notifyObservers(EventType.PagingChanges);
	}
	
	@Override
	public  final void decPaging() {
		paging.dec(); 
		notifyObservers(EventType.PagingChanges);
	}
	
	@Override
	public  final void beginPaging() {
		paging.first();
		notifyObservers(EventType.PagingChanges);
	}

}
