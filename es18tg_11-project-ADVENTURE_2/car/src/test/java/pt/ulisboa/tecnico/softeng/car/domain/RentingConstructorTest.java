package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;



public class RentingConstructorTest {
	private static final String RENT_A_CAR_NAME = "Eartz";
	private static final String PLATE_CAR = "22-33-HZ";
	private static final String DRIVING_LICENSE = "br112233";
	private static final LocalDate date1 = LocalDate.parse("2018-01-06");
	private static final LocalDate date2 = LocalDate.parse("2018-01-07");
	private Car car;
	private int preco = 10;

	
	private static final String NIF_OF_RENT_A_CAR = "666666666";
	private static final String IBAN_OF_RENT_A_CAR = "IBAN";
	private static final String NIF_BUYER = "111111111";
	private static final String IBAN_BUYER = "IBANBUYER";
	
	@Before
	public void setUp() {
		RentACar rentACar = new RentACar(RENT_A_CAR_NAME, NIF_OF_RENT_A_CAR, IBAN_OF_RENT_A_CAR);
		this.car = new Car(PLATE_CAR, 10,  preco,rentACar);
	}

	@Test
	public void success() {
		Renting renting = new Renting(DRIVING_LICENSE, date1, date2, car,NIF_BUYER,IBAN_BUYER);
		assertEquals(DRIVING_LICENSE, renting.getDrivingLicense());
		assertEquals(date1, renting.getBegin());
		assertEquals(date2, renting.getEnd());
		assertEquals(car, renting.getVehicle());
		assertEquals(NIF_BUYER,renting.getNIF());
		assertEquals(IBAN_BUYER,renting.getIBAN());
	}

	@Test(expected = CarException.class)
	public void nullDrivingLicense() {
		new Renting(null, date1, date2, car,NIF_BUYER,IBAN_BUYER);
	}

	@Test(expected = CarException.class)
	public void emptyDrivingLicense() {
		new Renting("", date1, date2, car,NIF_BUYER,IBAN_BUYER);
	}

	@Test(expected = CarException.class)
	public void invalidDrivingLicense() {
		new Renting("12", date1, date2, car,NIF_BUYER,IBAN_BUYER);
	}

	@Test(expected = CarException.class)
	public void nullBegin() {
		new Renting(DRIVING_LICENSE, null, date2, car,NIF_BUYER,IBAN_BUYER);
	}

	@Test(expected = CarException.class)
	public void nullEnd() {
		new Renting(DRIVING_LICENSE, date1, null, car,NIF_BUYER,IBAN_BUYER);
	}
	
	@Test(expected = CarException.class)
	public void endBeforeBegin() {
		new Renting(DRIVING_LICENSE, date2, date1, car,NIF_BUYER,IBAN_BUYER);
	}

	@Test(expected = CarException.class)
	public void nullCar() {
		new Renting(DRIVING_LICENSE, date1, date2, null,NIF_BUYER,IBAN_BUYER);
	}
	
	
	@Test(expected = CarException.class)
	public void nullNIF() {
		new Renting(DRIVING_LICENSE, date1, date2, car,null,IBAN_BUYER);
	}
	
	@Test(expected = CarException.class)
	public void whiteNIF() {
		new Renting(DRIVING_LICENSE, date1, date2, car,"",IBAN_BUYER);
	}
	
	@Test(expected = CarException.class)
	public void nullIBAN() {
		new Renting(DRIVING_LICENSE, date1, date2, car ,NIF_BUYER,null);
	}
	
	@Test(expected = CarException.class)
	public void whiteIBAN() {
		new Renting(DRIVING_LICENSE, date1, date2, car ,NIF_BUYER,"");
	}
	
	

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
		Account.cleanCount();
		IRS.getIRS().clearAll();
	}

}
