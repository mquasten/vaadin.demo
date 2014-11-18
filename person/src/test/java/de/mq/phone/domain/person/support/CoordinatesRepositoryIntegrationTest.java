package de.mq.phone.domain.person.support;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestOperations;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.GeoCoordinates;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/rest-test.xml"})
public class CoordinatesRepositoryIntegrationTest {
	
	private static final String POSITION = "N 51° 10' 1'' E 6° 16' 59''";
	@Autowired
	private RestOperations restOperations;
	
	@Test
	public final void forAddress() {


		final Address address = new AddressImpl("41844", "Wegberg", "Am Telt" , "4");
		final CoordinatesRepository repository = new CoordinatesRepositoryImpl(restOperations);
		final GeoCoordinates  result =  repository.forAddress(address);
		Assert.assertEquals(POSITION, result.location());
		
	}

}
