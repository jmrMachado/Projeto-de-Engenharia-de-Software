package pt.ulisboa.tecnico.softeng.broker.domain;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;

@RunWith(JMockit.class)
public class ProcessPaymentStateProcessMethodTest {
	private static final String IBAN = "BK01987654321";
	private static final int AMOUNT = 300;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String TAX_CONFIRMATION = "TaxConfirmation";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;
	private Buyer comprador = new Buyer(IRS.getIRS(), "123456789", "Pedro", "Rua");
	private Seller vendedor = new Seller(IRS.getIRS(), "987654321", "Pedro", "Rua");

	@Injectable
	private Broker broker = new Broker("BR01", "eXtremeADVENTURE", "123456789", "987654321", IBAN);
	private Client temp = new Client(broker, IBAN, "123456789", "6TG", 20);

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.temp, this.begin, this.end, AMOUNT, true);
		this.adventure.setState(State.PROCESS_PAYMENT);
	}

	@Test
	public void success(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
                BankInterface.processPayment(this.anyString, this.anyInt);
				this.result = PAYMENT_CONFIRMATION;

				TaxInterface.submitInvoice((InvoiceData) any);
				this.result = TAX_CONFIRMATION;
			}
		};

		this.adventure.process();

        Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
        Assert.assertNotNull(this.adventure.getPaymentConfirmation());
	}

	@Test
	public void bankException(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
                BankInterface.processPayment(IBAN, this.anyInt);
				this.result = new BankException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void singleRemoteAccessException(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
                BankInterface.processPayment(this.anyString, this.anyInt);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.PROCESS_PAYMENT, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessException(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
                BankInterface.processPayment(IBAN, this.anyInt);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessException(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
                BankInterface.processPayment(this.anyString, this.anyInt);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.PROCESS_PAYMENT, this.adventure.getState());
	}

	@Test
	public void twoRemoteAccessExceptionOneSuccess(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
                BankInterface.processPayment(this.anyString, this.anyInt);
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 2) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							return PAYMENT_CONFIRMATION;
						}
					}
				};
				this.times = 3;

				TaxInterface.submitInvoice((InvoiceData) any);
				this.result = TAX_CONFIRMATION;
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

        Assert.assertEquals(State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void oneRemoteAccessExceptionOneBankException(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
                BankInterface.processPayment(IBAN, this.anyInt);

				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 1) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							throw new BankException();
						}
					}
				};
				this.times = 2;

			}
		};

		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.CANCELLED, this.adventure.getState());
	}

	@After
	public void tearDown(){
		Broker.brokers.clear();
		IRS.getIRS().clearAll();
	}

}