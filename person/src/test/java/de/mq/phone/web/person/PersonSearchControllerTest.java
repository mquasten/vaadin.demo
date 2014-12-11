package de.mq.phone.web.person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.data.geo.Circle;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.GeoCoordinates;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonService;
import de.mq.phone.domain.person.PersonStringAware;
import de.mq.phone.domain.person.support.DistanceCalculator;

public class PersonSearchControllerTest {
	
	private static final String COORDINATES = "48° 42′ N, 44° 29′ O";
	private  final Validator validator = Mockito.mock(Validator.class);
	private PersonService personService = Mockito.mock(PersonService.class);
	private DistanceCalculator distanceCalculator = Mockito.mock(DistanceCalculator.class);
	private final PersonSearchController personSearchController = new PersonSearchControllerImpl(personService, distanceCalculator, validator);
	
	@Test
	public final void assign() {
		final PersonSearchModel model = Mockito.mock(PersonSearchModel.class);
		final PersonStringAware person = Mockito.mock(PersonStringAware.class);
		final Contact contact = Mockito.mock(Contact.class);
		final AddressStringAware addressStringAware = Mockito.mock(AddressStringAware.class);
		final Circle circle = Mockito.mock(Circle.class);
		Mockito.when(model.getSearchCriteriaPerson()).thenReturn(person);
		Mockito.when(model.getSearchCriteriaContact()).thenReturn(contact);
		Mockito.when(model.getSearchCriteriaAddress()).thenReturn(addressStringAware);
		Mockito.when(model.getSearchCriteriaDistance()).thenReturn(circle);
		final List<Person> persons = new ArrayList<>();
		persons.add(Mockito.mock(Person.class));
		Mockito.when(personService.persons(person, addressStringAware, contact,circle)).thenReturn(persons);
		personSearchController.assignPersons(model);
		Mockito.verify(model, Mockito.times(1)).setPersons(persons);
	}
	
	@Test
	public final void assignGeoKoordinates() {
		final PersonSearchModel model = Mockito.mock(PersonSearchModel.class);
		final Person person = Mockito.mock(Person.class);
		Mockito.when(person.hasGeoCoordinates()).thenReturn(true);
		final Address address = Mockito.mock(Address.class);
		final GeoCoordinates geoCoordinates = Mockito.mock(GeoCoordinates.class);
		Mockito.when(address.coordinates()).thenReturn(geoCoordinates);
		Mockito.when(person.address()).thenReturn(address);
		Mockito.when(personService.defaultPerson()).thenReturn(person);
		personSearchController.assignGeoKoordinates(model);
		Mockito.verify(model, Mockito.times(1)).setGeoCoordinates(geoCoordinates);
	}
	
	@Test
	public final void assignGeoKoordinatesNotAware() {
		final PersonSearchModel model = Mockito.mock(PersonSearchModel.class);
		final Person person = Mockito.mock(Person.class);
		Mockito.when(person.hasGeoCoordinates()).thenReturn(false);
		Mockito.when(personService.defaultPerson()).thenReturn(person);
		personSearchController.assignGeoKoordinates(model);
		Mockito.verify(model, Mockito.times(1)).setGeoCoordinates(null);
	}
	
	@Test
	public final void validate() {
		final Map<String,Object> map = new HashMap<>();
		Mockito.when(validator.supports(map.getClass())).thenReturn(true);
		BindingResult result = personSearchController.validate(map);
		Assert.assertEquals(PersonSearchControllerImpl.BINDING_RESULT_NAME, result.getObjectName());
		Mockito.verify(validator, Mockito.times(1)).validate(map, result);
	}
	
	@Test
	public final void geoInfos() {
		final GeoCoordinates geoCoordinates = Mockito.mock(GeoCoordinates.class);
		final GeoCoordinates homeCoordinates = Mockito.mock(GeoCoordinates.class);
		
		Mockito.when(geoCoordinates.location()).thenReturn(COORDINATES);
		PersonSearchModel model = Mockito.mock(PersonSearchModel.class);
		Mockito.when(model.hasGeoCoordinates()).thenReturn(true);
		Mockito.when(model.getGeoCoordinates()).thenReturn(homeCoordinates);
		Mockito.when(distanceCalculator.distance(homeCoordinates, geoCoordinates)).thenReturn(2216D);
		Mockito.when(distanceCalculator.angle(homeCoordinates, geoCoordinates)).thenReturn(89D);
		final List<String> results = new ArrayList<>();
		results.addAll(personSearchController.geoInfos(geoCoordinates, model, Locale.GERMAN));
		Assert.assertEquals(2, results.size());
		
		Assert.assertEquals(COORDINATES, results.get(0));
		Assert.assertEquals("2216,00 km, 89,00°", results.get(1));
		results.clear();
		Mockito.when(model.hasGeoCoordinates()).thenReturn(false);
		results.addAll(personSearchController.geoInfos(geoCoordinates, model, Locale.GERMAN));
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(COORDINATES, results.get(0));
		
		Assert.assertTrue(personSearchController.geoInfos(null, model, Locale.GERMAN).isEmpty());
	}

}