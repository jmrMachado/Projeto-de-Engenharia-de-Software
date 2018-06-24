package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelBulkBookingMethodTest extends RollbackTestAbstractClass{
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;
	
	private String IBAN_HOTEL ="IBAN";
	private String NIF_HOTEL = "123456789";
	private String IBAN2_HOTEL ="IBAN2";
	private String NIF2_HOTEL = "123456787";
	private String IBAN3_BUYER ="IBAN3";
	private String NIF3_BUYER = "123456785";
	private double priceSingle = 50;
	private double priceDouble = 60;

	@Override
	public void populate4Test() {
		this.hotel = new Hotel("XPTO123", "Paris", NIF_HOTEL, IBAN_HOTEL, priceSingle, priceDouble);
		new Room(this.hotel, "01", Type.DOUBLE);
		new Room(this.hotel, "02", Type.SINGLE);
		new Room(this.hotel, "03", Type.DOUBLE);
		new Room(this.hotel, "04", Type.SINGLE);

		this.hotel = new Hotel("XPTO124", "Paris", NIF2_HOTEL, IBAN2_HOTEL, priceSingle, priceDouble);
		new Room(this.hotel, "01", Type.DOUBLE);
		new Room(this.hotel, "02", Type.SINGLE);
		new Room(this.hotel, "03", Type.DOUBLE);
		new Room(this.hotel, "04", Type.SINGLE);
	}

	@Test
	public void success() {
		Set<String> references = Hotel.bulkBooking(2, this.arrival, this.departure, NIF3_BUYER, IBAN3_BUYER);

		assertEquals(2, references.size());
	}

	@Test(expected = HotelException.class)
	public void zeroNumber() {
		Hotel.bulkBooking(0, this.arrival, this.departure, NIF3_BUYER, IBAN3_BUYER);
	}

	@Test(expected = HotelException.class)
	public void noRooms() {
	
		this.hotel = new Hotel("XPTO124", "Paris", NIF2_HOTEL, IBAN2_HOTEL, priceSingle, priceDouble);

		Hotel.bulkBooking(3, this.arrival, this.departure, NIF3_BUYER, IBAN3_BUYER);
	}

	@Test
	public void OneNumber() {
		Set<String> references = Hotel.bulkBooking(1, this.arrival, this.departure, NIF3_BUYER, IBAN3_BUYER);

		assertEquals(1, references.size());
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		Hotel.bulkBooking(2, null, this.departure, NIF3_BUYER, IBAN3_BUYER);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		Hotel.bulkBooking(2, this.arrival, null, NIF3_BUYER, IBAN3_BUYER);
	}

	@Test
	public void reserveAll() {
		Set<String> references = Hotel.bulkBooking(8, this.arrival, this.departure, NIF3_BUYER, IBAN3_BUYER);

		assertEquals(8, references.size());
	}

	@Test
	public void reserveAllPlusOne() {
		try {
			Hotel.bulkBooking(9, this.arrival, this.departure,NIF3_BUYER, IBAN3_BUYER);
			fail();
		} catch (HotelException he) {
			assertEquals(8, Hotel.getAvailableRooms(8, this.arrival, this.departure).size());
		}
	}

	

}
