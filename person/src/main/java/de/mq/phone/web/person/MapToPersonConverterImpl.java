package de.mq.phone.web.person;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.support.BankingAccount;
import de.mq.phone.domain.person.support.PersonEntities;



@Component
class MapToPersonConverterImpl implements  MapToPersonMapper{

	
	/* (non-Javadoc)
	 * @see de.mq.phone.web.person.MapToPersonMapper#mapInto(java.util.Map, de.mq.phone.domain.person.Person)
	 */
	@Override
	public Person mapInto(final Map<String,?> mapValues,  final Person person) {
		
		toModel(mapValues, person);
		final Address address = PersonEntities.newAddress();
		toModel(mapValues, address);
		if ( StringUtils.hasText(address.address()) ) {
			person.assign(address);
		} else {
			person.assign((Address) null); 
		}
		
		final BankingAccount  bankingAccount = PersonEntities.newBankingAccount();
		
		toModel(mapValues, bankingAccount);
	   if ( StringUtils.hasText(bankingAccount.iBan()) ) {
	   	person.assign(bankingAccount);
	   } else {
	   	person.assign((BankingAccount) null);
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

	/* (non-Javadoc)
	 * @see de.mq.phone.web.person.MapToPersonMapper#convert(de.mq.phone.domain.person.Person)
	 */
	@Override
	public final Map<String, Object> convert(final Person source) {
		final Map<String,Object> results = new HashMap<>();
		add2Map(source, results);
		if( source.address() != null){
			add2Map(source.address(), results);
		}
		if(source.bankingAccount() != null){
			add2Map(source.bankingAccount(), results);
		}
		return results;
	}

	private <T> void add2Map(final T source, final Map<String, Object> results) {
		ReflectionUtils.doWithFields(source.getClass(), field -> { field.setAccessible(true); results.put(field.getName(), field.get(source));}, field -> {
			return field.getType().equals(String.class);
			});
	}

	
	

}
