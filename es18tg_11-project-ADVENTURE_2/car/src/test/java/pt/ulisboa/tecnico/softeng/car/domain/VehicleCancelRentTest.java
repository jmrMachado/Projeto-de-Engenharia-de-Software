package pt.ulisboa.tecnico.softeng.car.domain;


import static org.junit.Assert.assertNotNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.integration.junit4.JMockit;
import mockit.Expectations;
import mockit.Mocked;
import pt.ulisboa.tecnico.softeng.car.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

@RunWith(JMockit.class)
public class VehicleCancelRentTest {
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
	
	
	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");		
		
		rentACarClient = new Client(bank, RENT_A_CAR_NAME);
		this.rentACarAccount = new Account(bank, rentACarClient);
		this.rentACarAccount.deposit(100);
		
		
		this.rentACarAsSeller = new Seller(IRS.getIRS(), NIF_OF_RENT_A_CAR, RENT_A_CAR_NAME, "AddressOfRentACar");
		RentACar rentACar = new RentACar(RENT_A_CAR_NAME, this.rentACarAsSeller.getNIF(),this.rentACarAccount.getIBAN());
		this.car = new Car(PLATE_CAR, 10, 10,rentACar);
		
	}
	
	@Test
	public void successCancelRent(@Mocked final BankInterface bint, @Mocked final TaxInterface tint) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				
				TaxInterface.submitInvoice((InvoiceData) this.any);
				
				BankInterface.cancelPayment(this.anyString);
				
				TaxInterface.cancelInvoice(this.anyString);
			}
		};
		
		Renting renting = this.car.rent(DRIVING_LICENSE, IBAN, NIF, date1, date2);	
		
		String cancelRenting = this.car.cancelRent(renting.getReference());
 		
 		assertNotNull(cancelRenting);		
	}
	
	@Test(expected = CarException.class)
	public void referenceNotFoundCancelRent(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface tint)  {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				TaxInterface.submitInvoice((InvoiceData) this.any);				
			}
		};
		this.car.rent(DRIVING_LICENSE, IBAN, NIF, date1, date2);
		this.car.cancelRent("xpto");
	} 
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
		Bank.banks.clear();
		Account.cleanCount();
		IRS.getIRS().clearAll();
	}
}
