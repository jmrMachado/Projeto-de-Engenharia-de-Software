package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.car.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;


@RunWith(JMockit.class)
public class RentACarGetAllAvailableVehiclesTest {

	private static final String NAME1 = "eartz";
	private static final String NAME2 = "eartz";
	private static final String PLATE_CAR1 = "aa-00-11";
	private static final String PLATE_CAR2 = "aa-00-22";
	private static final String PLATE_MOTORCYCLE = "44-33-HZ";
	private static final String DRIVING_LICENSE = "br123";
	
	private static final LocalDate date1 = LocalDate.parse("2018-01-06");
	private static final LocalDate date2 = LocalDate.parse("2018-01-07");
	private static final LocalDate date3 = LocalDate.parse("2018-01-08");
	private static final LocalDate date4 = LocalDate.parse("2018-01-09");
	private RentACar rentACar1;
	private RentACar rentACar2;
	private int preco = 10;

	private static final String NIF_OF_RENT_A_CAR1 = "666666666";
	private static final String NIF_OF_RENT_A_CAR2 = "666666667";
	private static final String IBAN_OF_RENT_A_CAR1 = "IBAN1";
	private static final String IBAN_OF_RENT_A_CAR2 = "IBAN2";

	
	
	@Before
	public void setUp() {
		this.rentACar1 = new RentACar(NAME1, NIF_OF_RENT_A_CAR1, IBAN_OF_RENT_A_CAR1);
		this.rentACar2 = new RentACar(NAME2, NIF_OF_RENT_A_CAR2, IBAN_OF_RENT_A_CAR2);
	}
	
	
	@Test
	public void onlyCars(@Mocked final BankInterface bint, @Mocked final TaxInterface tint) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				
				TaxInterface.submitInvoice((InvoiceData) this.any);
				
			}
		};	
				
		Vehicle car1 = new Car(PLATE_CAR1, 10, preco, rentACar1);
		car1.rent(DRIVING_LICENSE, IBAN_OF_RENT_A_CAR1,NIF_OF_RENT_A_CAR1 ,date1, date2);
		
		Vehicle car2 = new Car(PLATE_CAR2, 10, preco, rentACar2);
		Vehicle motorcycle = new Motorcycle(PLATE_MOTORCYCLE, 10, preco, rentACar1);

		Set<Vehicle> cars = RentACar.getAllAvailableCars(date3, date4);
		assertTrue(cars.contains(car1));
		assertTrue(cars.contains(car2));
		assertFalse(cars.contains(motorcycle));
	
	}
	
	
	@Test
	public void onlyAvailableCars(@Mocked final BankInterface bint, @Mocked final TaxInterface tint) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				
				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};
		
		Vehicle car1 = new Car(PLATE_CAR1, 10, preco, rentACar1);
		Vehicle car2 = new Car(PLATE_CAR2, 10, preco, rentACar2);

		car1.rent(DRIVING_LICENSE, IBAN_OF_RENT_A_CAR1,NIF_OF_RENT_A_CAR1 ,date1, date2);
		Set<Vehicle> cars = RentACar.getAllAvailableCars(date1, date2);

		assertFalse(cars.contains(car1));
		assertTrue(cars.contains(car2));
	}
	
	@Test
	public void onlyMotorcycles() {
		Vehicle car = new Car(PLATE_CAR1, 10, preco, rentACar1);
		Vehicle motorcycle = new Motorcycle(PLATE_MOTORCYCLE, 10, preco, rentACar1);

		Set<Vehicle> cars = RentACar.getAllAvailableMotorcycles(date3, date4);
		assertTrue(cars.contains(motorcycle));
		assertFalse(cars.contains(car));
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
		Account.cleanCount();
		IRS.getIRS().clearAll();
		
	}
}
