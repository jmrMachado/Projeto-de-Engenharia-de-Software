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

public class HotelBulkBookingMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;
	private String NIF_HOTEL = "123456789";
	private String IBAN_HOTEL = "IBAN_HOTEL";
	private String NIF_CLIENTE = "123456788";
	private String IBAN_CLIENTE = "IBAN_CLIENTE";
	private double P_SINGLE = 10;
	private double P_DOUBLE = 20;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris",NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
		new Room(this.hotel, "01", Type.DOUBLE);
		new Room(this.hotel, "02", Type.SINGLE);
		new Room(this.hotel, "03", Type.DOUBLE);
		new Room(this.hotel, "04", Type.SINGLE);

		this.hotel = new Hotel("XPTO124", "Paris",NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
		new Room(this.hotel, "01", Type.DOUBLE);
		new Room(this.hotel, "02", Type.SINGLE);
		new Room(this.hotel, "03", Type.DOUBLE);
		new Room(this.hotel, "04", Type.SINGLE);
	}

	@Test
	public void success() {
		Set<String> references = Hotel.bulkBooking(IBAN_CLIENTE, NIF_CLIENTE, 2, this.arrival, this.departure);

		assertEquals(2, references.size());
	}

	@Test(expected = HotelException.class)
	public void zeroNumber() {
		Hotel.bulkBooking(IBAN_CLIENTE, NIF_CLIENTE, 0, this.arrival, this.departure);
	}

	@Test(expected = HotelException.class)
	public void noRooms() {
		Hotel.hotels.clear();
		this.hotel = new Hotel("XPTO124", "Paris",NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);

		Hotel.bulkBooking(IBAN_CLIENTE, NIF_CLIENTE, 3, this.arrival, this.departure);
	}

	@Test
	public void OneNumber() {
		Set<String> references = Hotel.bulkBooking(IBAN_CLIENTE, NIF_CLIENTE, 1, this.arrival, this.departure);

		assertEquals(1, references.size());
	}
	
	@Test(expected = HotelException.class)
	public void nullIBAN() {
		Hotel.bulkBooking(null, NIF_CLIENTE, 2, this.arrival, this.departure);
	}
	
	@Test(expected = HotelException.class)
	public void nullNIF() {
		Hotel.bulkBooking(IBAN_CLIENTE, null, 2, this.arrival, this.departure);
	}
	
	@Test(expected = HotelException.class)
	public void emptyIBAN() {
		Hotel.bulkBooking("", NIF_CLIENTE, 2, this.arrival, this.departure);
	}
	
	@Test(expected = HotelException.class)
	public void emptyNIF() {
		Hotel.bulkBooking(IBAN_CLIENTE, "", 2, this.arrival, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		Hotel.bulkBooking(IBAN_CLIENTE, NIF_CLIENTE, 2, null, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		Hotel.bulkBooking(IBAN_CLIENTE, NIF_CLIENTE, 2, this.arrival, null);
	}
	
	@Test(expected = HotelException.class)
	public void departureBeforeArrival() {
		Hotel.bulkBooking(IBAN_CLIENTE, NIF_CLIENTE, 2, this.arrival, this.departure.minusDays(3));
	}

	@Test
	public void reserveAll() {
		Set<String> references = Hotel.bulkBooking(IBAN_CLIENTE, NIF_CLIENTE, 8, this.arrival, this.departure);

		assertEquals(8, references.size());
	}

	@Test
	public void reserveAllPlusOne() {
		try {
			Hotel.bulkBooking(IBAN_CLIENTE, NIF_CLIENTE, 9, this.arrival, this.departure);
			fail();
		} catch (HotelException he) {
			assertEquals(8, Hotel.getAvailableRooms(8, this.arrival, this.departure).size());
		}
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
