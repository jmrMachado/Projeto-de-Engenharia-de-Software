package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;



public class VehicleConstructorTest {
	private static final String PLATE_CAR = "22-33-HZ";
	private static final String PLATE_MOTORCYCLE = "44-33-HZ";
	private static final String RENT_A_CAR_NAME = "Eartz";
	private static final String RENT_A_CAR_NAME2 = "Marz";

	private RentACar rentACar;
	private int preco = 20;

	private static final String NIF_OF_RENT_A_CAR = "666666666";
	private static final String NIF_OF_RENT_A_CAR2 = "666666667";

	private static final String IBAN_OF_RENT_A_CAR = "IBAN";
	private static final String IBAN_OF_RENT_A_CAR2 = "IBAN2";


	@Before
	public void setUp() {
		this.rentACar = new RentACar(RENT_A_CAR_NAME, NIF_OF_RENT_A_CAR, IBAN_OF_RENT_A_CAR);
	}

	@Test
	public void success() {
		Vehicle car = new Car(PLATE_CAR, 10,preco,this.rentACar);
		Vehicle motorcycle = new Motorcycle(PLATE_MOTORCYCLE, 10,  preco,this.rentACar);

		assertEquals(PLATE_CAR, car.getPlate());
		assertTrue(this.rentACar.hasVehicle(PLATE_CAR));
		assertEquals(PLATE_MOTORCYCLE, motorcycle.getPlate());
		assertTrue(this.rentACar.hasVehicle(PLATE_MOTORCYCLE));
	}
	
	@Test(expected = CarException.class)
	public void emptyLicensePlate() {
		new Car("", 10, preco,this.rentACar);
	}

	@Test(expected = CarException.class)
	public void nullLicensePlate() {
		new Car(null, 10, preco,this.rentACar);
	}

	@Test(expected = CarException.class)
	public void invalidLicensePlate() {
		new Car("AA-XX-a", 10, preco,this.rentACar);
	}
	
	@Test(expected = CarException.class)
	public void invalidLicensePlate2() {
		new Car("AA-XX-aaa", 10, preco,this.rentACar);
	}
	
	@Test(expected = CarException.class)
	public void duplicatedPlate() {
		new Car(PLATE_CAR, 0, preco,this.rentACar);
		new Car(PLATE_CAR, 0, preco,this.rentACar);
	}
	
	@Test(expected = CarException.class)
	public void duplicatedPlateDifferentRentACar() {
		new Car(PLATE_CAR, 0,preco,rentACar);
		RentACar rentACar2 = this.rentACar = new RentACar(RENT_A_CAR_NAME2, NIF_OF_RENT_A_CAR2, IBAN_OF_RENT_A_CAR2);
		new Car(PLATE_CAR, 2, preco,rentACar2);
	}
	
	@Test(expected = CarException.class)
	public void negativeKilometers() {
		new Car(PLATE_CAR, -1, preco,this.rentACar);
	}

	@Test(expected = CarException.class)
	public void noRentACar() {
		new Car(PLATE_CAR, 0, preco,null);
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
		Account.cleanCount();
		IRS.getIRS().clearAll();
	}

}
