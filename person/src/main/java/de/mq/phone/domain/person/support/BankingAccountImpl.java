package de.mq.phone.domain.person.support;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import de.mq.vaadin.util.BeforeSave;

class BankingAccountImpl implements BankingAccount {
	
	private String bankIdentifierCode;
	
	private String iBan;
	
	@SuppressWarnings("unused")
	private String account;
	
	public BankingAccountImpl(final String iBan ) {
		this.iBan=StringUtils.trimAllWhitespace(iBan);
	}
	
	public BankingAccountImpl(final String iBan, final String bankIdentifierCode  ) {
		this(iBan);
		this.bankIdentifierCode=bankIdentifierCode;
	}
	
	
	@SuppressWarnings("unused")
	private BankingAccountImpl() {
		this(null);
	}
	
	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.BankingAccount#iBan()
	 */
	@Override
	public final String iBan() {
		return iBan;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.BankingAccount#bankIdentifierCode()
	 */
	@Override
	public final String bankIdentifierCode() {
		return bankIdentifierCode;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.phone.domain.person.support.BankingAccount#assignBankIdentifierCode(java.lang.String)
	 */
	@Override
	public final void assignBankIdentifierCode(final String bankIdentifierCode) {
		this.bankIdentifierCode=bankIdentifierCode;
	}
	
	@Override
	public final String account() {
		Assert.hasText(iBan);
		final StringBuilder result = new StringBuilder();
		result.append(StringUtils.trimAllWhitespace(iBan));
		if( StringUtils.hasText(StringUtils.trimAllWhitespace(bankIdentifierCode))) {
			result.append(String.format(" (%s)" , StringUtils.trimAllWhitespace(bankIdentifierCode)));;
		}
		return result.toString();
	}
	
	@BeforeSave
	void beforeSave() {
		this.account=account();
	}

	@Override
	public int hashCode() {
		 if(! StringUtils.hasText(iBan)){
			return super.hashCode();
		}
		return StringUtils.trimAllWhitespace(iBan).hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof BankingAccount)) {
			return false;
			
		}
		final BankingAccount other = (BankingAccount) obj;
		return StringUtils.trimAllWhitespace(iBan).equals(StringUtils.trimAllWhitespace(other.iBan()));
	}
	
	

}
