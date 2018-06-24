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
public class VehicleIsFreeTest {

	private static final String PLATE_CAR = "22-33-HZ";
	private static final String RENT_A_CAR_NAME = "Eartz";
	private static final String DRIVING_LICENSE = "lx1423";
	private static final LocalDate date1 = LocalDate.parse("2018-01-06");
	private static final LocalDate date2 = LocalDate.parse("2018-01-07");
	private static final LocalDate date3 = LocalDate.parse("2018-01-08");
	private static final LocalDate date4 = LocalDate.parse("2018-01-09");
	private Car car;
	
	private static final String NIF_OF_RENT_A_CAR = "666666666";
	private static final String IBAN_OF_RENT_A_CAR = "IBAN";

	private static final String NIF_BUYER = "666666666";
	private static final String IBAN_BUYER = "IBANBUYER";


	@Before
	public void setUp() {
		RentACar rentACar = new RentACar(RENT_A_CAR_NAME, NIF_OF_RENT_A_CAR, IBAN_OF_RENT_A_CAR);
		this.car = new Car(PLATE_CAR, 10,  10,rentACar);
	}

	@Test
	public void noBookingWasMade() {
		assertTrue(car.isFree(date1, date2));
		assertTrue(car.isFree(date1, date3));
		assertTrue(car.isFree(date3, date4));
		assertTrue(car.isFree(date4, date4));
	}

	@Test
	public void bookingsWereMade(@Mocked final BankInterface bint, @Mocked final TaxInterface tint) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				
				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};	
		car.rent(DRIVING_LICENSE, NIF_BUYER, IBAN_BUYER, date2, date2);
		car.rent(DRIVING_LICENSE, NIF_BUYER, IBAN_BUYER, date3, date4);

		assertFalse(car.isFree(date1, date2));
		assertFalse(car.isFree(date1, date3));
		assertFalse(car.isFree(date3, date4));
		assertFalse(car.isFree(date4, date4));
		assertTrue(car.isFree(date1, date1));
	}
	
	@Test(expected = CarException.class)
	public void beginNullTest() {
		assertTrue(car.isFree(null, date2));
		
	}
	
	@Test(expected = CarException.class)
	public void endNullTest() {
		assertTrue(car.isFree(date2, null));
		
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
		Account.cleanCount();
		IRS.getIRS().clearAll();
	}
}
