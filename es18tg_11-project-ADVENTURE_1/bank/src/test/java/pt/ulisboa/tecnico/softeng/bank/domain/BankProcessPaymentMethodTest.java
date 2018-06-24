package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankProcessPaymentMethodTest {
	private static int AMOUNT = 100;
	private static String IBAN = "MN011";
	private Bank bank;
	private Account account;
	@SuppressWarnings("unused")
	private String reference;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		Client client = new Client(this.bank, "António");
		this.account = new Account(this.bank, client);
		this.reference = this.account.deposit(AMOUNT);
	}

	@Test
	public void success() {
		String newReference = Bank.processPayment(this.account.getIBAN(),AMOUNT);
		assertNotNull(this.bank.getOperation(newReference));
		assertNotNull(Bank.banks);
		assertEquals(this.account, this.bank.getOperation(newReference).getAccount());
		assertEquals(this.account.getIBAN(), this.bank.getOperation(newReference).getAccount().getIBAN());
		assertEquals(AMOUNT, this.bank.getOperation(newReference).getValue());
	}
	
	
	
	@Test
	public void emptyBankArray() {
		Bank.banks.clear();
		try {
			Bank.processPayment(this.account.getIBAN(),50);
			fail();
		}
		catch (BankException b) {
			assertTrue(Bank.banks.isEmpty());
		}
	}
	
	
	@Test(expected = BankException.class)
	public void nullIBAN() {
		Bank.processPayment("XPTO", AMOUNT);
	}
	
	@Test(expected = BankException.class)
	public void emptyIBAN() {
		Bank.processPayment("", 100);
	}
	
	@Test(expected = BankException.class)
	public void blankIBAN() {
		Bank.processPayment("    ", 100);
	}

	@Test(expected = BankException.class)
	public void negativeAmount() {
		Bank.processPayment(this.account.getIBAN(),-100);
	}
	
	@Test(expected = BankException.class)
	public void emptyIBANNegativeAmount() {
		Bank.processPayment("", -100);
	}
	
	
	@Test(expected = BankException.class)
	public void nullIBANnegativeAmount() {
		Bank.processPayment(null, -100);
	}
	
	@Test(expected = BankException.class)
	public void AmountBiggerThenBalance() {
		Bank.processPayment(this.account.getIBAN(),200);
	}
	
	
	@Test(expected = BankException.class)
	public void nullIBANAmountBiggerThenBalance() {
		Bank.processPayment("MN012",200);
	}
	

	@Test(expected = BankException.class)
	public void IBANNotExists() {
		Bank.processPayment(IBAN,AMOUNT);
	}
	
	@Test(expected = BankException.class)
	public void IBANNotExistsAmountBiggerThanBalance() {
		Bank.processPayment(IBAN,-200);
	}
	

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}