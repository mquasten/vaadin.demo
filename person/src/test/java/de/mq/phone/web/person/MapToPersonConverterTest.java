package de.mq.phone.web.person;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;



import org.springframework.test.util.ReflectionTestUtils;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.support.BankingAccount;
import de.mq.phone.domain.person.support.PersonEntities;
import de.mq.phone.web.person.PersonEditView.Fields;

public class MapToPersonConverterTest {

	private final MapToPersonMapper mapToPersonMapper = new MapToPersonConverterImpl();
	
	@Test
	public final void mapInto() {
		
		final Map<String,Object> map = new HashMap<>();
	
		Arrays.stream(PersonEditView.Fields.values()).forEach(value ->  map.put(value.property(), value.name()) );
		final Person person = PersonEntities.newPerson();
		mapToPersonMapper.mapInto(map, person);
		
		Assert.assertEquals(Fields.Alias.name(), person.alias());
		Assert.assertEquals(Fields.Firstname.name(), person.firstname());
		Assert.assertEquals(Fields.Name.name(), person.name());
		
		Assert.assertEquals(Fields.City.name(), person.address().city());
		Assert.assertEquals(Fields.ZipCode.name(), person.address().zipCode());
		Assert.assertEquals(Fields.Street.name(), person.address().street());
		Assert.assertEquals(Fields.HouseNumber.name(), person.address().houseNumber());
		
		Assert.assertEquals(Fields.IBan.name(), person.bankingAccount().iBan());
		Assert.assertEquals(Fields.BankIdentifierCode.name(), person.bankingAccount().bankIdentifierCode());
		Assert.assertTrue(person.contacts().isEmpty());
		
	
	}
	
	@Test
	public final void mapIntoOnlyPerson() {
		final Map<String,Object> map = new HashMap<>();
		Arrays.stream(PersonEditView.Fields.values()).filter(value -> value.row == 0  ).forEach(value ->  map.put(value.property(), value.name()) );
		final Person person = PersonEntities.newPerson();
		mapToPersonMapper.mapInto(map, person);
		Assert.assertEquals(Fields.Alias.name(), person.alias());
		Assert.assertEquals(Fields.Firstname.name(), person.firstname());
		Assert.assertEquals(Fields.Name.name(), person.name());
		
		Assert.assertNull(person.address());
		Assert.assertNull(person.bankingAccount());
	
		
	}
	
	@Test
	public final void convert() {
		final Person person = PersonEntities.newPerson();
		final Address address = PersonEntities.newAddress();
		final BankingAccount account = PersonEntities.newBankingAccount();
		assign(person, 0);
		assign(address, 1);
		assign(account, 2);
		person.assign(account);
		person.assign(address);	
		Assert.assertNotNull(person.person());
		Assert.assertNotNull(person.address().address());
		Assert.assertNotNull(person.bankingAccount().account());
		
		final Map<String,Object> personAsMap = mapToPersonMapper.convert(person);
		
		Arrays.stream(Fields.values()).forEach(field -> Assert.assertEquals(field.name(), personAsMap.get(field.property())));;
		
	}
	
	@Test
	public final void convertOnlyPerson() {
		final Person person = PersonEntities.newPerson();
		assign(person, 0);
		final Map<String,Object> personAsMap = mapToPersonMapper.convert(person);
		
		Arrays.stream(Fields.values()).filter(field ->  field.row == 0 ).forEach(field -> Assert.assertEquals(field.name(), personAsMap.get(field.property())));
		Arrays.stream(Fields.values()).filter(field ->  field.row == 1 || field.row == 2  ).forEach(field -> Assert.assertFalse(personAsMap.containsKey(field.property())));;
	}

	private <T>void  assign(final T entity, final int row) {
		Arrays.stream(PersonEditView.Fields.values()).filter(value -> value.row == row ).forEach(value ->  ReflectionTestUtils.setField(entity, value.property(), value.name()) );
	}
}
