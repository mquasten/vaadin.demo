package de.mq.phone.domain.person.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.StringUtils;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.GeoCoordinates;

public class AddressTest {

	private static final String HOUSE_NUMBER = "4";
	private static final String STREET = "Am Telt";
	private static final String CITY = "Wegberg";
	private static final String ZIP_CODE = "41844";

	@Test
	public final void create() {
		final Address address = new AddressImpl(ZIP_CODE, CITY, STREET, HOUSE_NUMBER);
		Assert.assertEquals(ZIP_CODE, address.zipCode());
		Assert.assertEquals(CITY, address.city());
		Assert.assertEquals(STREET, address.street());
		Assert.assertEquals(HOUSE_NUMBER, address.houseNumber());
	}
	
	@Test
	public final void address() {
		final Address address = new AddressImpl(ZIP_CODE, CITY, STREET, HOUSE_NUMBER);
		Assert.assertEquals(String.format("%s %s, %s %s", STREET, HOUSE_NUMBER, ZIP_CODE, CITY), address.address());
	}
	
	@Test
	public final void addressCityOnly() {
		final Address address = new AddressImpl(null, CITY, null, null);
		Assert.assertEquals(CITY, address.address());
	}
	
	@Test
	public final void addressWithoutCity() {
		final Address address = new AddressImpl(null, null, STREET, HOUSE_NUMBER);
		Assert.assertEquals(String.format("%s %s", STREET, HOUSE_NUMBER), address.address());
	}
	
	@Test
	public final void addressHouseNUmberWithOutStreet() {
		final Address address = new AddressImpl(ZIP_CODE, CITY, null, HOUSE_NUMBER);
		Assert.assertEquals(String.format("%s %s", ZIP_CODE, CITY), address.address());
	}
	
	@Test
	public final void withoutZip() {
		final Address address = new AddressImpl(null, CITY, STREET, HOUSE_NUMBER);
		Assert.assertEquals(String.format("%s %s, %s", STREET, HOUSE_NUMBER, CITY), address.address());
	}
	
	@Test
	public final void defaultConstructor() {
		Assert.assertFalse(StringUtils.hasText(new AddressImpl().address()));
	}
	
	@Test
	public final void beforeSave() {
		final Address address = new AddressImpl(ZIP_CODE, CITY, STREET, HOUSE_NUMBER);
		Assert.assertNull(ReflectionTestUtils.getField(address, "address"));
		((AddressImpl)address).beforeSave();
		Assert.assertEquals(String.format("%s %s, %s %s", STREET, HOUSE_NUMBER, ZIP_CODE, CITY), ReflectionTestUtils.getField(address, "address"));
		
	}
	
	@Test
	public final void assignCoordinates() {
		final Address address = new AddressImpl(null, CITY, STREET, HOUSE_NUMBER);
		Assert.assertNull(address.coordinates());
		final GeoCoordinates geoCoordinates = Mockito.mock(GeoCoordinates.class);
		address.assign(geoCoordinates);
		Assert.assertEquals(geoCoordinates, address.coordinates());
		
	}
	
	@Test
	public final void hash() {
		final Address address = new AddressImpl(ZIP_CODE, CITY, STREET, HOUSE_NUMBER);
		Assert.assertEquals(String.format("%s %s, %s %s", STREET, HOUSE_NUMBER, ZIP_CODE, CITY).hashCode(), address.hashCode());
		final Address nullAddress = new AddressImpl(null, null, null,null);
		Assert.assertEquals(System.identityHashCode(nullAddress), nullAddress.hashCode());
		
	}
	@Test
	public final void equals() {
		final Address address = new AddressImpl(ZIP_CODE, CITY, STREET, HOUSE_NUMBER);
		final Address other = new AddressImpl(ZIP_CODE, CITY, STREET, HOUSE_NUMBER);
		
		
		Assert.assertTrue(address.equals(other));
		Assert.assertFalse(address.equals("Don't Let Me Get Me"));
		
	}
}
