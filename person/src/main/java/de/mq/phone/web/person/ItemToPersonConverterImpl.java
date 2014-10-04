package de.mq.phone.web.person;

import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;


import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.support.BankingAccount;
import de.mq.phone.domain.person.support.PersonEntities;

@Component
@ConverterQualifier(ConverterQualifier.Type.Map2Person)
public class ItemToPersonConverterImpl implements Converter<Map<String,?>, Person>{

	@Override
	public Person convert(final Map<String,?> items) {
		final Person person = PersonEntities.newPerson();
		toModel(items, person);
		final Address address = PersonEntities.newAddress();
		toModel(items, address);
		if ( StringUtils.hasText(address.address()) ) {
			person.assign(address);
		}
		
		final BankingAccount  bankingAccount = PersonEntities.newBankingAccount();
		
		toModel(items, bankingAccount);
	   if ( StringUtils.hasText(bankingAccount.iBan()) ) {
	   	person.assign(bankingAccount);
	   }
		return person;
	}

	private <T> void  toModel(final Map<String,?> items, final T source) {
		ReflectionUtils.doWithFields(source.getClass(),  field -> { 
		
			
			if( items.containsKey(field.getName()) ) {
				field.setAccessible(true);
			   field.set(source, items.get(field.getName()));
			}
			
			
			
			
		} );
	}

	

}
