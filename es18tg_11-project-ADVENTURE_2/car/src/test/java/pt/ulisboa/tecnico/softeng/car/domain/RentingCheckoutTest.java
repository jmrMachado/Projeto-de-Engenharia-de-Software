package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;

@RunWith(JMockit.class)
public class RentingCheckoutTest {
	private static final String NAME1 = "eartz";
	private static final String PLATE_CAR1 = "aa-00-11";
	private static final String DRIVING_LICENSE = "br123";
	private static final LocalDate date1 = LocalDate.parse("2018-01-06");
	private static final LocalDate date2 = LocalDate.parse("2018-01-07");
	private Car car;
	private int preco = 10;

	private static final String NIF_OF_RENT_A_CAR = "666666666";
	private static final String IBAN_OF_RENT_A_CAR = "IBAN";

	@Before
	public void setUp() {
		RentACar rentACar1 = new RentACar(NAME1, NIF_OF_RENT_A_CAR, IBAN_OF_RENT_A_CAR);
		this.car = new Car(PLATE_CAR1, 10, preco, rentACar1);
	}

	
	@Test
	public void checkout(@Mocked final BankInterface bint, @Mocked final TaxInterface tint) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				
				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};
		Renting renting = car.rent(DRIVING_LICENSE, NIF_OF_RENT_A_CAR, IBAN_OF_RENT_A_CAR, date1, date2);
		renting.checkout(100);
		assertEquals(110, car.getKilometers());
		assertEquals(100, renting.getKm());
	}
	
	@Test(expected = CarException.class)
	public void failCheckout(@Mocked final BankInterface bint, @Mocked final TaxInterface tint) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				
				TaxInterface.submitInvoice((InvoiceData) this.any);
				
			}
		};
		
		Renting renting = car.rent(DRIVING_LICENSE, NIF_OF_RENT_A_CAR, IBAN_OF_RENT_A_CAR, date1, date2);
		renting.checkout(-10);
	}
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
		Account.cleanCount();
		IRS.getIRS().clearAll();
	}
}
