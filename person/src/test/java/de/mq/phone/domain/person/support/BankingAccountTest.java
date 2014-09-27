package de.mq.phone.domain.person.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.test.util.ReflectionTestUtils;

public class BankingAccountTest {
	
	private static final String BIC = "DEUTDEDKXXX";
	private static final String IBAN = "DE21 3012 0400 0000 0152 28";

	@Test
	public final void create() {
		final BankingAccount bankingAccount = new BankingAccountImpl(IBAN);
		Assert.assertEquals(IBAN.replaceAll("[ ]", ""), bankingAccount.iBan());
	}
	
	@Test
	public final void bic() {
		final BankingAccount bankingAccount = new BankingAccountImpl(IBAN, BIC);
		
		Assert.assertEquals(BIC, bankingAccount.bankIdentifierCode());
	}
	@Test
	public final void assignBic(){
		final BankingAccount bankingAccount = new BankingAccountImpl(IBAN);
		Assert.assertNull(bankingAccount.bankIdentifierCode());
		bankingAccount.assignBankIdentifierCode(BIC);
		Assert.assertEquals(BIC, bankingAccount.bankIdentifierCode());
	}
	
	
	@Test
	public final void defaultConstructor() {
		final BankingAccount bankingAccount = BeanUtils.instantiateClass(BankingAccountImpl.class, BankingAccount.class);
		Assert.assertNull(bankingAccount.iBan());
		Assert.assertNull(bankingAccount.bankIdentifierCode());
	}
	
	@Test
	public final void accountOnlyIBan() {
		final BankingAccount bankingAccount = new BankingAccountImpl(IBAN);
		Assert.assertEquals(IBAN.replaceAll("[ ]", ""), bankingAccount.account());
	}
	
	@Test
	public final void accountOnlyIBanAnaBic() {
		final BankingAccount bankingAccount = new BankingAccountImpl(IBAN,BIC);
		Assert.assertEquals(String.format("%s (%s)", IBAN.replaceAll("[ ]", ""), BIC), bankingAccount.account());
	}
	
	@Test
	public final void beforeSave() {
		final BankingAccount bankingAccount = new BankingAccountImpl(IBAN,BIC);
		Assert.assertNull(ReflectionTestUtils.getField(bankingAccount, "account"));
		((BankingAccountImpl)bankingAccount).beforeSave();
		Assert.assertEquals(String.format("%s (%s)", IBAN.replaceAll("[ ]", ""), BIC),ReflectionTestUtils.getField(bankingAccount, "account"));
		
	}
	
	@Test
	public final void hash() {
		final BankingAccount bankingAccount = new BankingAccountImpl(IBAN);
		Assert.assertEquals(IBAN.replaceAll("[ ]", "").hashCode(), bankingAccount.hashCode());
		final BankingAccount nullBankingAccount = new BankingAccountImpl(null);
		Assert.assertEquals(System.identityHashCode(nullBankingAccount), nullBankingAccount.hashCode());
	}
	
	@Test
	public final void equals() {
		final BankingAccount bankingAccount = new BankingAccountImpl(IBAN);
		Assert.assertTrue(bankingAccount.equals(new BankingAccountImpl(IBAN)));
		Assert.assertFalse(bankingAccount.equals("Don'tLetMeGetMe"));
	}

}
