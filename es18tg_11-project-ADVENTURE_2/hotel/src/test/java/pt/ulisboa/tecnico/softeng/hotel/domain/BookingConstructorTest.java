package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class BookingConstructorTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;
	private Room room;
	
	private static final String HOTEL_NAME = "Londres";
	private static final String HOTEL_CODE = "XPTO123";
	private String NIF_HOTEL = "123456789";
	private String IBAN_HOTEL = "IBAN_HOTEL";
	private double P_SINGLE = 10;
	private double P_DOUBLE = 20;
	
	@Before
	public void setUp() {
		this.hotel = new Hotel(HOTEL_CODE, HOTEL_NAME, NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
		this.room = new Room(hotel, "20", Type.SINGLE);
	}

	@Test
	public void success() {
		Booking booking = new Booking(this.hotel, this.arrival, this.departure, 10, "IBAN_Client","134567890", this.room.getRoomID());
		
		Assert.assertTrue(booking.getReference().startsWith(this.hotel.getCode()));
		Assert.assertTrue(booking.getReference().length() > Hotel.CODE_SIZE);
		Assert.assertEquals(this.arrival, booking.getArrival());
		Assert.assertEquals(this.departure, booking.getDeparture());
		Assert.assertEquals(this.room.getRoomID(), booking.getRoomID());
		Assert.assertTrue(booking.getAmount()==10);
		Assert.assertEquals("134567890", booking.getNIF());
		Assert.assertEquals("IBAN_Client", booking.getIBAN());
	}

	@Test(expected = HotelException.class)
	public void nullHotel() {
		new Booking(null, this.arrival, this.departure, 10, "IBAN_Client","134567890", this.room.getRoomID());
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		new Booking(this.hotel, null, this.departure, 10, "IBAN_Client","134567890", this.room.getRoomID());
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		new Booking(this.hotel, this.arrival, null, 10, "IBAN_Client","134567890", this.room.getRoomID());
	}

	@Test(expected = HotelException.class)
	public void departureBeforeArrival() {
		new Booking(this.hotel, this.arrival, this.arrival.minusDays(1), 10, "IBAN_Client","134567890", this.room.getRoomID());
	}
	
	@Test(expected = HotelException.class)
	public void negativeAmount() {
		new Booking(this.hotel, this.arrival, this.arrival, -2, "IBAN_Client","134567890", this.room.getRoomID());
	}	
	
	@Test(expected = HotelException.class)
	public void nullNif() {
		new Booking(this.hotel, this.arrival, this.arrival, 10, "IBAN_Client", null, this.room.getRoomID());
	}
	
	@Test(expected = HotelException.class)
	public void nullIBAN() {
		new Booking(this.hotel, this.arrival, this.arrival, 10, null, "134567890", this.room.getRoomID());
	}	
	
	@Test(expected = HotelException.class)
	public void nullRoomID() {
		new Booking(this.hotel, this.arrival, this.arrival, 10, "IBAN_Client", "134567890", null);
	}	
	
	@Test(expected = HotelException.class)
	public void emptyIBAN() {
		new Booking(this.hotel, this.arrival, this.arrival, 10, "", "134567890", this.room.getRoomID());
	}
	
	@Test(expected = HotelException.class)
	public void emptyNIF() {
		new Booking(this.hotel, this.arrival, this.arrival, 10, "IBAN_Client", "", this.room.getRoomID());
	}
	
	@Test(expected = HotelException.class)
	public void emptyRoomID() {
		new Booking(this.hotel, this.arrival, this.arrival, 10, "IBAN_Client", "134567890", "");
	}
	
	@Test
	public void arrivalEqualDeparture() {
		new Booking(this.hotel, this.arrival, this.arrival, 10, "134567890", "IBAN_Client", this.room.getRoomID());
	}
	
	

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
