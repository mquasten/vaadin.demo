package de.mq.phone.domain.person;

import java.util.Collection;

import de.mq.phone.domain.person.support.BankingAccount;

public interface Person extends PersonStringAware {
	
	String id();
	
	String firstname();
	
	String name();

	Collection<Contact> contacts();

	void assign(final Contact contact);

	Address address();
	
	void assign(final Address address);

	void assign(BankingAccount bankingAccount);

	BankingAccount bankingAccount();

	String alias();

	boolean hasGeoCoordinates();
	
}
