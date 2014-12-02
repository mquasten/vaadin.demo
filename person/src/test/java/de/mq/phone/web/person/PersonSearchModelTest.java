package de.mq.phone.web.person;



import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.GeoCoordinates;
import de.mq.phone.domain.person.Person;
import de.mq.vaadin.util.Observer;

public class PersonSearchModelTest {
	private final PersonSearchModel personSearchModel = new PersonSearchModelImpl();
	
	@Test
	public final void setPersons() {
		
		@SuppressWarnings("unchecked")
		final Observer< PersonSearchModel.EventType> observer = Mockito.mock( Observer.class);
		personSearchModel.register(observer, PersonSearchModel.EventType.PersonsChanges);
	
		final List<Person> persons = new ArrayList<>();
		persons.add(Mockito.mock(Person.class));
		personSearchModel.setPersons(persons);
		
		Assert.assertEquals(persons, personSearchModel.getPersons());
		
		Mockito.verify(observer, Mockito.times(1)).process( PersonSearchModel.EventType.PersonsChanges);
	}
	
	@Test
	public final void searchCriteriaPerson() {
		
		final Person person = Mockito.mock(Person.class);
		personSearchModel.setSearchCriteria(person);
		Assert.assertEquals(person, personSearchModel.getSearchCriteriaPerson());
	}
	
	@Test
	public final void searchCriteriaContact() {
		final Contact contact = Mockito.mock(Contact.class);
		personSearchModel.setSearchCriteria(contact);
		Assert.assertEquals(contact, personSearchModel.getSearchCriteriaContact());
	}
	
	@Test
	public final void searchCriteriaAddress() {
		final AddressStringAware address = Mockito.mock(AddressStringAware.class);
		personSearchModel.setSearchCriteria(address);
		Assert.assertEquals(address, personSearchModel.getSearchCriteriaAddress());
	
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void searchCriteriaWrongType(){
		personSearchModel.setSearchCriteria(new Date());
	}
	
	@Test
	public final void enums() {
		for(final PersonSearchModel.EventType eventType : PersonSearchModel.EventType.values()){
			PersonSearchModel.EventType.valueOf(eventType.name());
		}
	}
	
	
	@Test
	public final void setGeoCoordinates() {
		final GeoCoordinates geoCoordinates = Mockito.mock(GeoCoordinates.class);
		@SuppressWarnings("unchecked")
		final Observer<PersonSearchModel.EventType> observer = Mockito.mock(Observer.class);
		personSearchModel.register(observer, PersonSearchModel.EventType.HomeLocationChanges);
		personSearchModel.setGeoCoordinates(geoCoordinates);
		Mockito.verify(observer).process(Mockito.any(PersonSearchModel.EventType.class));
		Assert.assertEquals(geoCoordinates, ReflectionTestUtils.getField(personSearchModel, "geoCoordinates"));
	}
	

	@Test
	public final void getSearchCriteriaDistance() {
		final GeoCoordinates geoCoordinates = Mockito.mock(GeoCoordinates.class);
		Mockito.when(geoCoordinates.latitude()).thenReturn(51D);
		Mockito.when(geoCoordinates.longitude()).thenReturn(6D);
		ReflectionTestUtils.setField(personSearchModel, "geoCoordinates", geoCoordinates);
		@SuppressWarnings("unchecked")
		final Map<Class<?>,Object> search = (Map<Class<?>, Object>) ReflectionTestUtils.getField(personSearchModel, "search");
		final Distance distance = Mockito.mock(Distance.class);
		search.put(Distance.class, distance );
		Mockito.when(distance.getValue()).thenReturn(42D);
		
		final Circle result = personSearchModel.getSearchCriteriaDistance();
		Assert.assertEquals(distance, result.getRadius());
		Assert.assertEquals(geoCoordinates.latitude(), result.getCenter().getY());
		Assert.assertEquals(geoCoordinates.longitude(), result.getCenter().getX());
	}
	
	@Test
	public final void getSearchCriteriaDistanceNoDistance() {
		final GeoCoordinates geoCoordinates = Mockito.mock(GeoCoordinates.class);
		Mockito.when(geoCoordinates.latitude()).thenReturn(51D);
		Mockito.when(geoCoordinates.longitude()).thenReturn(6D);
		ReflectionTestUtils.setField(personSearchModel, "geoCoordinates", geoCoordinates);
		
		Assert.assertEquals(PersonSearchModelImpl.UNDEFINED_CIRCLE, personSearchModel.getSearchCriteriaDistance());
		
	}
	
	@Test
	public final void getSearchCriteriaDistanceNoCoordinates() {
		@SuppressWarnings("unchecked")
		final Map<Class<?>,Object> search = (Map<Class<?>, Object>) ReflectionTestUtils.getField(personSearchModel, "search");
		final Distance distance = Mockito.mock(Distance.class);
		search.put(Distance.class, distance );
		Assert.assertEquals(PersonSearchModelImpl.UNDEFINED_CIRCLE, personSearchModel.getSearchCriteriaDistance());
	}
	
	@Test
	public final void hasCoordinates() {
		final GeoCoordinates geoCoordinates = Mockito.mock(GeoCoordinates.class);
		ReflectionTestUtils.setField(personSearchModel, "geoCoordinates", geoCoordinates);
		Assert.assertTrue(personSearchModel.hasGeoCoordinates());
		ReflectionTestUtils.setField(personSearchModel, "geoCoordinates", null);
		Assert.assertFalse(personSearchModel.hasGeoCoordinates());
		
	}
	
}
