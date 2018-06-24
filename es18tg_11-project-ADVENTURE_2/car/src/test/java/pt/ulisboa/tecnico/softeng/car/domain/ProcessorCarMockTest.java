package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Delegate;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.car.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

@RunWith(JMockit.class)
public class ProcessorCarMockTest {
	
	private static final String PLATE_CAR = "22-33-HZ";
	private static final String RENT_A_CAR_NAME = "Eartz";
	private static final String DRIVING_LICENSE = "lx1423";
	private static final LocalDate date1 = LocalDate.parse("2018-01-06");
	private static final LocalDate date2 = LocalDate.parse("2018-01-09");
	private Car car;
	
	private static final String NIF_OF_RENT_A_CAR = "666666666";
	
	private static final String IBAN = "IBAN";
	private static final String NIF = "123456789";
	
	
	private Seller rentACarAsSeller;
	private Account rentACarAccount;
	
	private Bank bank;
	private Client rentACarClient;
	
	private Renting renting;
	private RentACar rentACar;
	
	private static final String CANCEL_PAYMENT_REFERENCE = "CancelPaymentReference";
	private static final String INVOICE_REFERENCE = "InvoiceReference";
	private static final String PAYMENT_REFERENCE = "PaymentReference";
	
	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");		
		
		rentACarClient = new Client(bank, RENT_A_CAR_NAME);
		this.rentACarAccount = new Account(bank, rentACarClient);
		this.rentACarAccount.deposit(100);
		
		
		this.rentACarAsSeller = new Seller(IRS.getIRS(), NIF_OF_RENT_A_CAR, RENT_A_CAR_NAME, "AddressOfRentACar");
		this.rentACar = new RentACar(RENT_A_CAR_NAME, this.rentACarAsSeller.getNIF(),this.rentACarAccount.getIBAN());
		this.car = new Car(PLATE_CAR, 10, 10,this.rentACar);
	
		
	}
	
	@Test
	public void success(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};
		Renting renting = this.car.rent(DRIVING_LICENSE, this.rentACarAccount.getIBAN(), NIF, date1, date2);	
		this.rentACar.getProcessor().submitRenting(renting);

		new FullVerifications() {
			{
			}
		};
	}
	
	@Test
	public void oneTaxFailureOnSubmitInvoice(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				this.result = PAYMENT_REFERENCE;
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = new TaxException();
				this.result = INVOICE_REFERENCE;
			}
		};
		
		Renting renting = this.car.rent(DRIVING_LICENSE, this.rentACarAccount.getIBAN(), NIF, date1, date2);	
		this.rentACar.getProcessor().submitRenting(renting);
	
		this.rentACar.getProcessor().submitRenting(new Renting(DRIVING_LICENSE,date1,date2,renting.getVehicle(),NIF,renting.getIBAN()));

		new FullVerifications(taxInterface) {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.times = 3;
			}
		};
	}
	
	@Test
	public void oneRemoteFailureOnSubmitInvoice(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				this.result = PAYMENT_REFERENCE;
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = new RemoteAccessException();
				this.result = INVOICE_REFERENCE;
			}
		};
		Renting renting = this.car.rent(DRIVING_LICENSE, this.rentACarAccount.getIBAN(), NIF, date1, date2);
		this.rentACar.getProcessor().submitRenting(renting);
		this.rentACar.getProcessor().submitRenting(new Renting(DRIVING_LICENSE,date1,date2,renting.getVehicle(),NIF,renting.getIBAN()));

		new FullVerifications(taxInterface) {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.times = 3;
			}
		};

	}
		
	@Test
	public void oneRemoteFailureOnProcessPayment(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				this.result = new RemoteAccessException();
				this.result = PAYMENT_REFERENCE;
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = INVOICE_REFERENCE;
			}
		};

		Renting renting = this.car.rent(DRIVING_LICENSE, this.rentACarAccount.getIBAN(), NIF, date1, date2);
		this.rentACar.getProcessor().submitRenting(renting);
		this.rentACar.getProcessor().submitRenting(new Renting(DRIVING_LICENSE,date1,date2,renting.getVehicle(),NIF,renting.getIBAN()));

		new FullVerifications(bankInterface) {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				this.times = 3;
			}
		};
	}
	
	@Test
	public void successCancel(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				BankInterface.processPayment(this.anyString, this.anyDouble);

				TaxInterface.cancelInvoice(this.anyString);
				BankInterface.cancelPayment(this.anyString);
			}
		};

		Renting renting = this.car.rent(DRIVING_LICENSE, this.rentACarAccount.getIBAN(), NIF, date1, date2);
		this.rentACar.getProcessor().submitRenting(renting);
		renting.cancel();

		new FullVerifications() {
			{
			}
		};
	}
	
	@Test
	public void oneBankExceptionOnCancelPayment(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				BankInterface.processPayment(this.anyString, this.anyDouble);

				BankInterface.cancelPayment(this.anyString);
				this.result = new BankException();
				this.result = CANCEL_PAYMENT_REFERENCE;
				TaxInterface.cancelInvoice(this.anyString);
			}
		};

		Renting renting = this.car.rent(DRIVING_LICENSE, this.rentACarAccount.getIBAN(), NIF, date1, date2);
		this.rentACar.getProcessor().submitRenting(renting);
		renting.cancel();
		this.rentACar.getProcessor().submitRenting(renting);

		new FullVerifications(bankInterface) {
			{
				BankInterface.cancelPayment(this.anyString);
				this.times = 2;
			}
		};
	}
	
	@Test
	public void oneRemoteExceptionOnCancelPayment(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				BankInterface.processPayment(this.anyString, this.anyDouble);

				BankInterface.cancelPayment(this.anyString);
				this.result = new RemoteAccessException();
				this.result = CANCEL_PAYMENT_REFERENCE;
				TaxInterface.cancelInvoice(this.anyString);
			}
		};

		Renting renting = this.car.rent(DRIVING_LICENSE, this.rentACarAccount.getIBAN(), NIF, date1, date2);
		this.rentACar.getProcessor().submitRenting(renting);
		renting.cancel();
		this.rentACar.getProcessor().submitRenting(renting);

		new FullVerifications(bankInterface) {
			{
				BankInterface.cancelPayment(this.anyString);
				this.times = 2;
			}
		};
	}
	
	@Test
	public void oneTaxExceptionOnCancelInvoice(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				TaxInterface.submitInvoice((InvoiceData) this.any);
				BankInterface.cancelPayment(this.anyString);
				this.result = CANCEL_PAYMENT_REFERENCE;
				TaxInterface.cancelInvoice(this.anyString);
				this.result = new Delegate() {
					int i = 0;

					public void delegate() {
						if (this.i < 1) {
							this.i++;
							throw new TaxException();
						}
					}
				};
			}
		};

		Renting renting = this.car.rent(DRIVING_LICENSE, this.rentACarAccount.getIBAN(), NIF, date1, date2);
		this.rentACar.getProcessor().submitRenting(renting);
		renting.cancel();
		this.rentACar.getProcessor().submitRenting(renting);

		new FullVerifications(taxInterface) {
			{
				TaxInterface.cancelInvoice(this.anyString);
				this.times = 2;
			}
		};
	}

	@Test
	public void oneRemoteExceptionOnCancelInvoice(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				TaxInterface.submitInvoice((InvoiceData) this.any);

				BankInterface.cancelPayment(this.anyString);
				this.result = CANCEL_PAYMENT_REFERENCE;
				TaxInterface.cancelInvoice(this.anyString);
				this.result = new Delegate() {
					int i = 0;

					public void delegate() {
						if (this.i < 1) {
							this.i++;
							throw new RemoteAccessException();
						}
					}
				};
			}
		};

		Renting renting = this.car.rent(DRIVING_LICENSE, this.rentACarAccount.getIBAN(), NIF, date1, date2);
		this.rentACar.getProcessor().submitRenting(renting);
		renting.cancel();
		this.rentACar.getProcessor().submitRenting(renting);

		new FullVerifications(taxInterface) {
			{
				TaxInterface.cancelInvoice(this.anyString);
				this.times = 2;
			}
		};
	}
	
	
	
	
	
	@After
	public void tearDown() {
		//ActivityProvider.providers.clear();
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
		Bank.banks.clear();
		IRS.getIRS().clearAll();
		Account.cleanCount();
		this.rentACar.getProcessor().clean();
	}
	
	

}