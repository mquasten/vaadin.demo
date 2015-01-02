package de.mq.phone.domain.person.support;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/mongo-test.xml"})
@Ignore
public class PersonRepositoryIntegrationTest {
	
	@Autowired
	private PersonMongoRepositoryImpl personMongoRepository;
	
	private Paging paging = new SimpleResultSetPagingImpl(Integer.MAX_VALUE, Integer.MAX_VALUE);
	
	
	
	@Test
	@Ignore
	public final void createTestDataBase() {
		
		personMongoRepository.dropPersons();
		final PersonImpl kylie = new PersonImpl("Minogue", "Kylie");
		kylie.assign(new EMailContact("kylie@minogue.uk"));
		kylie.assign(new EMailContact("kinky@kylie.uk"));
		
		kylie.assign(new PhoneImpl("44", "20", "19680528"));
		
		final Address address = new AddressImpl("41844", "Wegberg", "Am Telt", "4");
		address.assign(new GeoDegreesCoordinatesImpl(51.16556401970851 ,6.281634319708499 ));
		kylie.assign(address);
		
		final BankingAccount bankingAccount = new BankingAccountImpl("DE21 3012 0400 0000 0152 28");
		kylie.assign(bankingAccount);
		
		personMongoRepository.save(kylie);
		
		final PersonImpl katy = new PersonImpl("Perry", "Katy");
		katy.assign(new EMailContact("sesame.street@elmo.us"));
		katy.assign(new PhoneImpl("1", "200", "123456"));
		personMongoRepository.save(katy);
		
		final PersonImpl riri = new PersonImpl("Riri", "Rihanna");
		riri.assign(new EMailContact("riri@sm.us"));
		riri.assign(new PhoneImpl("1", "200", "98765"));
		personMongoRepository.save(riri);
		
		final PersonImpl gaGa = new PersonImpl("Gaga", "Lady");
		gaGa.assign(new EMailContact("gaga@boy.us"));
		gaGa.assign(new PhoneImpl("1", "200", "563427"));
		personMongoRepository.save(gaGa);
		
		final PersonImpl beyonce = new PersonImpl("Knowless", "Beyonce");
		beyonce.assign(new EMailContact("billion.dollar@elevator.us"));
		beyonce.assign(new PhoneImpl("1", "200", "13579"));
		personMongoRepository.save(beyonce);
	}
	
	
	@Test
	public final void forCriteria() {
		final Contact contact = new PhoneImpl("19680528");
		final Person person = new PersonImpl("Minogue ", "Kylie");
		final Address address = new AddressImpl(null, "Wegberg" , null , null);
		List<Person> results = personMongoRepository.forCriterias(person, address, contact, undefinedCircle(), paging);
		System.out.println(results.size());
		for( final Person result : results){
			System.out.println("-------------------------------------------");
		System.out.println(result.firstname());
		System.out.println(result.name());
		System.out.println(ReflectionTestUtils.getField(result, "id"));
		
		System.out.println(result.address().address());
		System.out.println(result.address().coordinates().latitude() + "°, "+ result.address().coordinates().longitude() +"°" );
		System.out.println(result.bankingAccount().iBan());
		}
	}
	
	@Test
	public final void forCriteriaGeo() {
		final Person person = BeanUtils.instantiateClass(PersonImpl.class);
		ReflectionTestUtils.setField(person, "name", "Nicki");
		//System.out.println(">>>" + person.person());
		final Contact contact = BeanUtils.instantiateClass(EMailContact.class);
		final Address address = BeanUtils.instantiateClass(AddressImpl.class);
		final Circle  circle = new Circle(new Point(6.2829833d ,  51.166913d), new Distance(67.1d, Metrics.KILOMETERS));
		final List<Person> results = personMongoRepository.forCriterias(person, address, contact,circle, paging);
	//	System.out.println(results.iterator().next().address().address());
		System.out.println(results.size());
		System.out.println(results.iterator().next().person());
		
	}
	
	private  Circle undefinedCircle() {
		final Circle circle = new Circle(new Point(0d ,  0d ), new Distance(0)); 
		final Field field = ReflectionUtils.findField(Circle.class, "radius");
		field.setAccessible(true);
		ReflectionUtils.setField(field, circle, undefinedDistance());
		return circle;
		
	}
	
	private  Distance undefinedDistance() {
		final Distance distance =  new Distance(0, Metrics.KILOMETERS);
		final Field field = ReflectionUtils.findField(Distance.class, "value");
		field.setAccessible(true);
		ReflectionUtils.setField(field, distance, -1d);
		return distance;
	}
	
	

}
