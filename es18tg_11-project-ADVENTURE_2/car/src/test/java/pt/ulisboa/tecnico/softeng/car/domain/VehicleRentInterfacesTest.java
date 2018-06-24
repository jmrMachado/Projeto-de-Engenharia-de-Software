package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;
import pt.ulisboa.tecnico.softeng.activity.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;

@RunWith(JMockit.class)
public class VehicleRentInterfacesTest {
	private static final String PLATE_CAR = "22-33-HZ";
	private static final String RENT_A_CAR_NAME = "Eartz";
	private static final String DRIVING_LICENSE = "lx1423";
	private static final LocalDate date1 = LocalDate.parse("2018-01-06");
	private static final LocalDate date2 = LocalDate.parse("2018-01-09");
	private Car car;
	
	private static final String NIF_OF_RENT_A_CAR = "666666666";
	
	private static final String NIF = "123456789";
	private Buyer rentACarAsBuyer;
	private Seller rentACarAsSeller;
	private Account rentACarAccount;
	
	private Bank bank;
	private Client rentACarClient;
	
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
		this.rentACarAsBuyer = new Buyer(IRS.getIRS(), NIF, RENT_A_CAR_NAME, "Atlantida");
		this.rentACar = new RentACar(RENT_A_CAR_NAME, this.rentACarAsSeller.getNIF(),this.rentACarAccount.getIBAN());
		this.car = new Car(PLATE_CAR, 10, 10,rentACar);
		
	}
	@Test
	public void successRent() {
				
		Renting renting = this.car.rent(DRIVING_LICENSE, this.rentACarAccount.getIBAN(), NIF, date1, date2);	
		this.rentACar.getProcessor().submitRenting(renting);
 		RentingData rentingData = RentACar.getRentingData(renting.getReference());
 		
 		assertEquals((int)this.rentACarAccount.getBalance(),
 				100 - renting.getVehicle().getPreco() * (renting.getEnd().getDayOfYear() - renting.getBegin().getDayOfYear()));
 		
 		String str = renting.getInvRef();
 		Assert.assertTrue(str != null);
 		Assert.assertNotNull(renting.isCancelled());
 		
 		assertEquals(renting.getReference(), rentingData.getReference());
 		assertEquals(DRIVING_LICENSE, rentingData.getDrivingLicense());
 		assertEquals(this.car.getPlate(), rentingData.getPlate());
 		assertEquals(this.car.getRentACar().getCode(), rentingData.getRentACarCode());
 		
 		assertEquals(date1,rentingData.getBegin());
 		assertEquals(date2,rentingData.getEnd());
 		assertEquals(NIF_OF_RENT_A_CAR,this.rentACarAsSeller.getNIF()); 
 		assertEquals("BK011",this.rentACarAccount.getIBAN());
 		
 		
		
	}
	
	@Test
	public void successCancelRent()  {	
		Renting renting = this.car.rent(DRIVING_LICENSE, this.rentACarAccount.getIBAN(), NIF, date1, date2);	
		this.rentACar.getProcessor().submitRenting(renting);
		renting.cancel();
		
		RentingData rd = RentACar.getRentingData(renting.getReference());
		
		String str = rd.getReference();
		Assert.assertTrue(str != null);
		Assert.assertTrue(renting.isCancelled());
		
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
	
	

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
		Bank.banks.clear();
		IRS.getIRS().clearAll();
		Account.cleanCount();
		this.rentACar.getProcessor().clean();
	}
}



