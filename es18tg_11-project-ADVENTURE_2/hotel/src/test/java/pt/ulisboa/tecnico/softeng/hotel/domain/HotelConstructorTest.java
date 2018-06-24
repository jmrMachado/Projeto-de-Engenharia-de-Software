package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class HotelConstructorTest {
	private static final String HOTEL_NAME = "Londres";
	private static final String HOTEL_CODE = "XPTO123";
	private String NIF_HOTEL = "123456789";
	private String IBAN_HOTEL = "IBAN_HOTEL";
	private double P_SINGLE = 10;
	private double P_DOUBLE = 20;

	
	@Test
	public void success() {
		Hotel hotel = new Hotel(HOTEL_CODE, HOTEL_NAME, NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);

		Assert.assertEquals(HOTEL_NAME, hotel.getName());
		Assert.assertTrue(hotel.getCode().length() == Hotel.CODE_SIZE);
		Assert.assertEquals(0, hotel.getNumberOfRooms());
		Assert.assertEquals(1, Hotel.hotels.size());
		Assert.assertEquals(hotel.getNIF(), NIF_HOTEL);
		Assert.assertEquals(hotel.getIBAN(), IBAN_HOTEL);
		Assert.assertEquals((int)hotel.getPrecoSingle(), (int)P_SINGLE);
		Assert.assertEquals((int)hotel.getPrecoDouble(), (int)P_DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void nullCode() {
		new Hotel(null, HOTEL_NAME, NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void blankCode() {
		new Hotel("       ", HOTEL_NAME, NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void emptyCode() {
		new Hotel("", HOTEL_NAME, NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
	}
	
	@Test(expected = HotelException.class)
	public void nullNIF() {
		new Hotel(HOTEL_CODE, HOTEL_NAME, null, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void blankNIF() {
		new Hotel(HOTEL_CODE, HOTEL_NAME, "    ", IBAN_HOTEL, P_SINGLE, P_DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void emptyNIF() {
		new Hotel(HOTEL_CODE, HOTEL_NAME, "", IBAN_HOTEL, P_SINGLE, P_DOUBLE);
	}
	
	@Test(expected = HotelException.class)
	public void nullIBAN() {
		new Hotel(HOTEL_CODE, HOTEL_NAME, NIF_HOTEL, null, P_SINGLE, P_DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void blankIBAN() {
		new Hotel(HOTEL_CODE, HOTEL_NAME, NIF_HOTEL, "     ", P_SINGLE, P_DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void emptyIBAN() {
		new Hotel(HOTEL_CODE, HOTEL_NAME, NIF_HOTEL, "", P_SINGLE, P_DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void nullName() {
		new Hotel(HOTEL_CODE, null, NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void blankName() {
		new Hotel(HOTEL_CODE, "     ", NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void emptyName() {
		new Hotel(HOTEL_CODE, "", NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void codeSizeLess() {
		new Hotel("1234", "null", NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void codeSizeMore() {
		new Hotel("123456789", "null", NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void codeNotUnique() {
		new Hotel(HOTEL_CODE, HOTEL_NAME, NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
		new Hotel(HOTEL_CODE, "nome", NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
	}
	
	@Test(expected = HotelException.class)
	public void pSingleLessThanZero() {
		new Hotel(HOTEL_CODE, HOTEL_NAME, NIF_HOTEL, IBAN_HOTEL, -1, P_DOUBLE);
	}
	
	@Test
	public void pSingleEqualZero() {
		Hotel hotel = new Hotel(HOTEL_CODE, HOTEL_NAME, NIF_HOTEL, IBAN_HOTEL, 0, P_DOUBLE);
		
		Assert.assertEquals(HOTEL_NAME, hotel.getName());
		Assert.assertTrue(hotel.getCode().length() == Hotel.CODE_SIZE);
		Assert.assertEquals(0, hotel.getNumberOfRooms());
		Assert.assertEquals(1, Hotel.hotels.size());
		Assert.assertEquals(hotel.getNIF(), NIF_HOTEL);
		Assert.assertEquals(hotel.getIBAN(), IBAN_HOTEL);
		Assert.assertEquals(0, (int)hotel.getPrecoSingle());
		Assert.assertEquals((int)hotel.getPrecoDouble(), (int)P_DOUBLE);

	}
	
	@Test(expected = HotelException.class)
	public void pDoubleLessThanZero() {
		new Hotel(HOTEL_CODE, HOTEL_NAME, NIF_HOTEL, IBAN_HOTEL, P_SINGLE, -1);
	}
	
	@Test
	public void pDoubleEqualZero() {
		Hotel hotel = new Hotel(HOTEL_CODE, HOTEL_NAME, NIF_HOTEL, IBAN_HOTEL, P_SINGLE, 0);
		
		Assert.assertEquals(HOTEL_NAME, hotel.getName());
		Assert.assertTrue(hotel.getCode().length() == Hotel.CODE_SIZE);
		Assert.assertEquals(0, hotel.getNumberOfRooms());
		Assert.assertEquals(1, Hotel.hotels.size());
		Assert.assertEquals(hotel.getNIF(), NIF_HOTEL);
		Assert.assertEquals(hotel.getIBAN(), IBAN_HOTEL);
		Assert.assertEquals((int)hotel.getPrecoSingle(), (int)P_SINGLE);
		Assert.assertEquals(0, (int)hotel.getPrecoDouble());
	}
	
	@Test
	public void codeUnique() {
		new Hotel(HOTEL_CODE, HOTEL_NAME, NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
		new Hotel("XPTO122", "nome", NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
		Assert.assertEquals(2, Hotel.hotels.size());
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
