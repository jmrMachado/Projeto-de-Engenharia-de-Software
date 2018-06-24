package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;


public class RentACarConstructorTest {
	private static final String NAME = "eartz";

	private static final String NIF_OF_RENT_A_CAR = "666666666";
	private static final String IBAN_OF_RENT_A_CAR = "IBAN";
	
	@Test
	public void success() {
		RentACar rentACar1 = new RentACar(NAME, NIF_OF_RENT_A_CAR,IBAN_OF_RENT_A_CAR);
		assertEquals(NAME, rentACar1.getName());
		assertEquals(NIF_OF_RENT_A_CAR, rentACar1.getNif());
		assertEquals(IBAN_OF_RENT_A_CAR, rentACar1.getIban());
	}
	
	
	
	@Test(expected = CarException.class)
	public void nullName() {
		RentACar rentACar1 = new RentACar(null, NIF_OF_RENT_A_CAR,IBAN_OF_RENT_A_CAR);
	}
	
	
	@Test(expected = CarException.class)
	public void emptyName() {
		RentACar rentACar1 = new RentACar("", NIF_OF_RENT_A_CAR,IBAN_OF_RENT_A_CAR);
	}
		
	@Test(expected = CarException.class)
	public void nullNIF() {
		RentACar rentACar1 = new RentACar(NAME, null,IBAN_OF_RENT_A_CAR);
	}
	
	@Test(expected = CarException.class)
	public void emptyNIF() {
		RentACar rentACar1 = new RentACar(NAME, "", IBAN_OF_RENT_A_CAR);
	}
	
	@Test(expected = CarException.class)
	public void nullIBAN() {
		RentACar rentACar1 = new RentACar(NAME, NIF_OF_RENT_A_CAR, null);
	}
	
	@Test(expected = CarException.class)
	public void emptyIBAN() {
		RentACar rentACar1 = new RentACar(NAME, NIF_OF_RENT_A_CAR, "");
	}

	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Account.cleanCount();
	}
}
