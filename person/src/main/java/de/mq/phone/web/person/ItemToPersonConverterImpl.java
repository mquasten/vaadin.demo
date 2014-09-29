package de.mq.phone.web.person;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.vaadin.data.util.PropertysetItem;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.support.BankingAccount;
import de.mq.phone.domain.person.support.PersonEntities;

@Component
@ConverterQualifier(ConverterQualifier.Type.Item2Person)
public class ItemToPersonConverterImpl implements Converter<PropertysetItem, Person>{

	@Override
	public Person convert(final PropertysetItem items) {
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

	private <T> void  toModel(final PropertysetItem items, final T source) {
		ReflectionUtils.doWithFields(source.getClass(),  field -> { 
		
			
			if( items.getItemPropertyIds().contains(field.getName()) ) {
				field.setAccessible(true);
			   field.set(source, items.getItemProperty(field.getName()).getValue());
			}
			
			
			
			
		} );
	}

}
