package de.mq.phone.domain.person.support;

public interface BankingAccount extends AccountStringAware{

	String iBan();

	String bankIdentifierCode();

	void assignBankIdentifierCode(final String bankIdentifierCode);

	

}