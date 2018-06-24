package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import mockit.Expectations;
import mockit.Mocked;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;

public class HotelGetRoomBookingDataMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 24);
	private Hotel hotel;
	private Room room;
	private Booking booking;
	
	private static final String HOTEL_NAME = "Londres";
	private static final String HOTEL_CODE = "XPTO123";
	private String NIF_HOTEL = "123456789";
	private String IBAN_HOTEL = "IBAN_HOTEL";
	private double P_SINGLE = 10;
	private double P_DOUBLE = 20;

	private String NIF_CLIENTE = "123456788";
	private String IBAN_CLIENTE = "IBAN_CLIENTE";
	
	@Before
	public void setUp() {
		this.hotel = new Hotel(HOTEL_CODE, HOTEL_NAME, NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
		this.room = new Room(this.hotel, "01", Type.DOUBLE);
	}

	@Test
	public void success(@Mocked final BankInterface bint, @Mocked final TaxInterface tint) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				
				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};
		
		Booking booking = this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.DOUBLE, this.arrival, this.departure);

		RoomBookingData data = Hotel.getRoomBookingData(booking.getReference());

		assertEquals(booking.getReference(), data.getReference());
		assertNull(data.getCancellation());
		assertNull(data.getCancellationDate());
		assertEquals(hotel.getName(), data.getHotelName());
		assertEquals(hotel.getCode(), data.getHotelCode());
		assertEquals(room.getNumber(), data.getRoomNumber());
		assertEquals(room.getType().name(), data.getRoomType());
		assertEquals(booking.getArrival(), data.getArrival());
		assertEquals(booking.getDeparture(), data.getDeparture());
	}

	@Test
	public void successCancellation(@Mocked final BankInterface bint, @Mocked final TaxInterface tint) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				
				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};
		
		Booking booking = this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.DOUBLE, this.arrival, this.departure);

		booking.cancel();
		RoomBookingData data = Hotel.getRoomBookingData(booking.getReference());

		assertEquals(booking.getReference(), data.getReference());
		assertEquals(booking.getCancellation(), data.getCancellation());
		assertEquals(booking.getCancellationDate(), data.getCancellationDate());
		assertEquals(hotel.getName(), data.getHotelName());
		assertEquals(hotel.getCode(), data.getHotelCode());
		assertEquals(room.getNumber(), data.getRoomNumber());
		assertEquals(room.getType().name(), data.getRoomType());
		assertEquals(booking.getArrival(), data.getArrival());
		assertEquals(booking.getDeparture(), data.getDeparture());
	}

	@Test(expected = HotelException.class)
	public void nullReference() {
		Hotel.getRoomBookingData(null);
	}

	@Test(expected = HotelException.class)
	public void emptyReference() {
		Hotel.getRoomBookingData("");
	}

	@Test(expected = HotelException.class)
	public void referenceDoesNotExist() {
		Hotel.getRoomBookingData("XPTO");
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
		IRS.getIRS().clearAll();
		Room.clearCounter();
	}
}
