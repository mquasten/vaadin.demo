package de.mq.phone.domain.person.support;



import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.util.ReflectionUtils;

import de.mq.phone.domain.person.Address;
import de.mq.phone.domain.person.AddressStringAware;
import de.mq.phone.domain.person.Contact;
import de.mq.phone.domain.person.Person;
import de.mq.phone.domain.person.PersonStringAware;



public  final class PersonEntities {
	
	public enum ContactType {
		Email(EMailContact.class),
		Phone(PhoneImpl.class);
		
		final Class<? extends Contact> clazz;
		ContactType(final Class<? extends Contact> clazz) {
			this.clazz=clazz;
		}
		
		public final Class<? extends Contact> type() {
			return clazz;
		}
		
	}
	

	private PersonEntities(){
		throw new IllegalStateException("Creating instance is forbidden: " + getClass().getName());
	}
	
	
	public static boolean isMailContact(Contact contact) {
		if (contact instanceof EMailContact) {
			return true;
		}
		return false;
		
	}
	
	public static boolean isPhoneContact(Contact contact) {
		if (contact instanceof PhoneImpl) {
			return true;
		}
		return false;
		
	}
	
	public static String id(final PersonStringAware person) {
		final String[] ids = { null };
		ReflectionUtils.doWithFields(person.getClass(), field -> {
			field.setAccessible(true);
			if (!field.isAnnotationPresent(Id.class)) {
				return;
			}
			ids[0] = (String) field.get(person);

		});

		return ids[0];
		
	}
	
	public static Person newPerson() {
		return BeanUtils.instantiateClass(PersonImpl.class, Person.class);
	}
	
	public static Address newAddress() {
		return BeanUtils.instantiateClass(AddressImpl.class, Address.class);
	}
	
	
	public static PersonStringAware newPerson(final String searchString) {
		return new PersonStringAware() {

			@Override
			public String person() {
				return searchString;
			}} ;
			
			
	}
	
	public static BankingAccount newBankingAccount() {
		return new BankingAccountImpl(null);
	}
	
	public static Contact newContact(final String contact) {
		return new Contact() {
			@Override
			public final String contact() {
				return contact;
			}
		};
	}
	
	public static AddressStringAware newAddressStringAware(final String address) {
		return  new AddressStringAware() {
			
			@Override
			public String address() {
				
				return address;
			}
		};
	}
	
	

}
