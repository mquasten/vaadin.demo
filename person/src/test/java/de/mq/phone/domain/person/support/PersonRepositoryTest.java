package de.mq.phone.domain.person.support;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.util.ReflectionTestUtils;

import com.mongodb.BasicDBObject;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;

public class PersonRepositoryTest {
	
	private static final String ID = "19680528";
	private static final String BANKING_ACCOUNT = "DE21 3012 0400 0000 0152 28";
	private static final String HOUSE_NUMBER = "27";
	private static final String STREET = "Wrights Lane";
	private static final String CITY = "London";
	private static final String ZIP = "W8 5SW";
	private static final String CONTACT = "kylie@minogue.uk";
	private static final String NAME = "Minogue";
	private static final String FIRSTNAME = "Kylie";
	private final MongoOperations mongoOperations = Mockito.mock(MongoOperations.class);
	
	@Test
	public final void save() {
		final Address address = new AddressImpl(ZIP, CITY, STREET, HOUSE_NUMBER);
		address.assign(new GeoDegreesCoordinatesImpl(51.5,  -0.1167));
		final Person person = new PersonImpl(NAME, FIRSTNAME);
		
		person.assign(address);
		person.assign(new BankingAccountImpl(BANKING_ACCOUNT));
		person.assign(new EMailContact(CONTACT));
		final PersonRepository personRepository = new PersonMongoRepositoryImpl(mongoOperations);
		personRepository.save(person);
		
		Mockito.verify(mongoOperations, Mockito.times(1)).save(person);
		
		Assert.assertEquals( person.person(), ReflectionTestUtils.getField(person, "person"));
		
		Assert.assertEquals(person.address().address(), ReflectionTestUtils.getField(person.address(), "address"));
		final Double[] results =  (Double[]) ReflectionTestUtils.getField(person.address().coordinates(), "location");
		Assert.assertEquals(2, results.length);
		Assert.assertEquals(person.address().coordinates().latitude(), results[1]);
		Assert.assertEquals(person.address().coordinates().longitude(), results[0]);
		
		Assert.assertEquals(person.contacts().iterator().next().contact(), ReflectionTestUtils.getField(person.contacts().iterator().next(), "contact"));
	    Assert.assertEquals(person.bankingAccount().account(), ReflectionTestUtils.getField(person.bankingAccount(), "account"));
	}
	
	@Test
	public final void saveOnlyPerson() {
		final Person person = Mockito.mock(Person.class);
		final PersonRepository personRepository = new PersonMongoRepositoryImpl(mongoOperations);
		personRepository.save(person);
		Mockito.verify(mongoOperations, Mockito.times(1)).save(person);
		Mockito.verify(person, Mockito.times(1)).address();
		Mockito.verify(person, Mockito.times(1)).bankingAccount();
		
		
	}
	
	@Test
	public final void savePersonWithAddress() {
		final Person person = Mockito.mock(Person.class);
		final Address address = Mockito.mock(Address.class);
		Mockito.when(person.address()).thenReturn(address);
		final PersonRepository personRepository = new PersonMongoRepositoryImpl(mongoOperations);
		personRepository.save(person);
		
		Mockito.verify(mongoOperations, Mockito.times(1)).save(person);
		Mockito.verify(person, Mockito.times(1)).address();
		Mockito.verify(person, Mockito.times(1)).bankingAccount();
		Mockito.verify(address,  Mockito.times(1)).coordinates();
	}
	
	@Test
	public final void dropPerson() {
		final PersonMongoRepositoryImpl personRepository = new PersonMongoRepositoryImpl(mongoOperations);
		personRepository.dropPersons();
		
		Mockito.verify(mongoOperations).dropCollection(PersonImpl.class);
	}
	
	
	@SuppressWarnings("unchecked")
	@Test
	public final void forCriterias() {
		final PersonRepository personRepository = new PersonMongoRepositoryImpl(mongoOperations);
		
		final Person person = Mockito.mock(Person.class);
		Mockito.when(person.person()).thenReturn(FIRSTNAME);
		
		final Address address = Mockito.mock(Address.class);
		Mockito.when(address.address()).thenReturn(CITY);
		
		final Contact contact = Mockito.mock(Contact.class);
		Mockito.when(contact.contact()).thenReturn(CONTACT);
		final ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class) ;
		@SuppressWarnings("rawtypes")
		final ArgumentCaptor<Class> classCaptor = ArgumentCaptor.forClass(Class.class) ;
		ArgumentCaptor<String> collectionCaptor = ArgumentCaptor.forClass(String.class) ;
		final List<Person> persons = new ArrayList<>();
		persons.add(Mockito.mock(Person.class));
		final Circle circle = Mockito.mock(Circle.class);
		final Distance distance = Mockito.mock(Distance.class);
		final Point point = Mockito.mock(Point.class);
		Mockito.when(circle.getRadius()).thenReturn(distance);
		Mockito.when(circle.getCenter()).thenReturn(point);
		Mockito.when(mongoOperations.find(queryCaptor.capture(), classCaptor.capture(), collectionCaptor.capture())).thenReturn(persons);
		Assert.assertEquals(persons, personRepository.forCriterias(person, address, contact, circle));
		
		Assert.assertEquals(Person.class, classCaptor.getValue());
		Assert.assertEquals("person", collectionCaptor.getValue());
		final Query query = queryCaptor.getValue();
		
		final Map<String, Criteria> results = (Map<String, Criteria>) ReflectionTestUtils.getField(query, "criteria");
		Assert.assertEquals(4, results.size());
		Assert.assertTrue(results.containsKey("person"));
	
		Assert.assertTrue(results.containsKey("contacts.contact"));
		Assert.assertTrue(results.containsKey("address.address"));
		Assert.assertTrue(results.containsKey("address.geoCoordinates.location"));
		
		Assert.assertEquals(FIRSTNAME, results.get("person").getCriteriaObject().get("person").toString());
		Assert.assertEquals(CONTACT, results.get("contacts.contact").getCriteriaObject().get("contacts.contact").toString());
		Assert.assertEquals(CITY, results.get("address.address").getCriteriaObject().get("address.address").toString());
		BasicDBObject distanceResult = (BasicDBObject) results.get("address.geoCoordinates.location").getCriteriaObject();
		Assert.assertEquals(1, distanceResult.keySet().size());
		Assert.assertEquals("address.geoCoordinates.location", distanceResult.keySet().iterator().next());
		
		
		
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public final void forCriteriasNoCriteria() {
		final PersonRepository personRepository = new PersonMongoRepositoryImpl(mongoOperations);
		
		final Person person = Mockito.mock(Person.class);
		final Address address = Mockito.mock(Address.class);
		final Contact contact = Mockito.mock(Contact.class);
		final Circle circle = Mockito.mock(Circle.class);
		final Distance radius = Mockito.mock(Distance.class);
		Mockito.when(circle.getRadius()).thenReturn(radius);
		Mockito.when(radius.getValue()).thenReturn(-1D);
		final Point point = Mockito.mock(Point.class);
		Mockito.when(circle.getCenter()).thenReturn(point);
		ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class) ;
		@SuppressWarnings("rawtypes")
		ArgumentCaptor<Class> classCaptor = ArgumentCaptor.forClass(Class.class) ;
		ArgumentCaptor<String> collectionCaptor = ArgumentCaptor.forClass(String.class) ;
		final List<Person> persons = new ArrayList<>();
		persons.add(Mockito.mock(Person.class));
		Mockito.when(mongoOperations.find(queryCaptor.capture(), classCaptor.capture(), collectionCaptor.capture())).thenReturn(persons);
		
		
		Assert.assertEquals(persons, personRepository.forCriterias(person, address, contact, circle));
		
		final Map<String, Criteria> results = (Map<String, Criteria>) ReflectionTestUtils.getField(queryCaptor.getValue(), "criteria");
		Assert.assertTrue(results.isEmpty());
	}
	
	@Test
	public final void forId() {
		final PersonRepository personRepository = new PersonMongoRepositoryImpl(mongoOperations);
		final Person result = Mockito.mock(Person.class);
		Mockito.when(personRepository.forId(ID)).thenReturn(result);
		Assert.assertEquals(result, personRepository.forId(ID));
	}
	
	@Test
	public final void delete() {
		final PersonRepository personRepository = new PersonMongoRepositoryImpl(mongoOperations);
		final Person person = Mockito.mock(Person.class);
		Mockito.when(personRepository.forId(ID)).thenReturn(person);
		personRepository.delete(ID);
		Mockito.verify(mongoOperations).remove(person);
	}

}
