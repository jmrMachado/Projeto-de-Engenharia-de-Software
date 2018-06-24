package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
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
import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

@RunWith(JMockit.class)
public class VehicleRentTest {
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
	public void successRent(@Mocked final BankInterface bint, @Mocked final TaxInterface tint) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				
				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};
		
		Renting renting = this.car.rent(DRIVING_LICENSE, IBAN, NIF, date1, date2);		
 		RentingData rentingData = RentACar.getRentingData(renting.getReference());
 		
 		assertEquals(renting.getReference(), rentingData.getReference());
 		assertEquals(DRIVING_LICENSE, rentingData.getDrivingLicense());
 		assertEquals(this.car.getPlate(), rentingData.getPlate());
 		assertEquals(this.car.getRentACar().getCode(), rentingData.getRentACarCode());
 		
 		assertEquals(date1,rentingData.getBegin());
 		assertEquals(date2,rentingData.getEnd());
 		assertEquals(NIF_OF_RENT_A_CAR,this.rentACarAsSeller.getNIF()); 
 		assertEquals("BK011",this.rentACarAccount.getIBAN());
 		
 		String str = renting.getReference();
		Assert.assertTrue(str != null);
		
		
	}
	
	@Test(expected = CarException.class)
	public void doubleRent(@Mocked final BankInterface bankInterface, @Mocked final TaxInterface tint)  {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				TaxInterface.submitInvoice((InvoiceData) this.any);				
			}
		};
		this.car.rent(DRIVING_LICENSE, IBAN, NIF, date1, date2);
		this.car.rent(DRIVING_LICENSE, IBAN, NIF, date1, date2);
	} 
	
	
	@Test(expected = CarException.class)
	public void beginIsNull() {	
		this.car.rent(DRIVING_LICENSE, IBAN, NIF, null, date2);
		
	}

	@Test(expected = CarException.class)
	public void endIsNull() {
		this.car.rent(DRIVING_LICENSE, IBAN, NIF, date1, null);
	}
	
	@Test(expected = CarException.class)
	public void nullIBAN() {
		this.car.rent(DRIVING_LICENSE, null, NIF, date1, date2 );
	}
	
	@Test(expected = CarException.class)
	public void licenseNull() {
		this.car.rent(null, IBAN, NIF, date1, date2);
	}
	
	@Test(expected = CarException.class)
	public void NIFNull() {
		this.car.rent(DRIVING_LICENSE, IBAN, null, date1, date2);
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
