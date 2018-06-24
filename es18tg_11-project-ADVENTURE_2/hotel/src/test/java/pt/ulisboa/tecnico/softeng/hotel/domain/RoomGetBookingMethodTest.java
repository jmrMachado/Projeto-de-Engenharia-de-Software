package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.Assert;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;

@RunWith(JMockit.class)
public class RoomGetBookingMethodTest {
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
		this.room = new Room(this.hotel, "01", Type.SINGLE);
	}

	@Test
	public void success(@Mocked final BankInterface bint, @Mocked final TaxInterface tint) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				
				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};
		Booking booking = this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);
		assertEquals(booking, this.room.getBooking(booking.getReference()));
	}

	@Test
	public void successCancelled(@Mocked final BankInterface bint, @Mocked final TaxInterface tint) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				
				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};
		Booking booking = this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);
		
		booking.cancel();

		assertEquals(booking, this.room.getBooking(booking.getReference()));
		Assert.assertTrue(booking.getCancelledInvoice());
	}

	@Test
	public void doesNotExist() {
		assertNull(this.room.getBooking("XPTO"));
	}
	
	/*@Test
	public void failedCancelled(@Mocked final BankInterface bint, @Mocked final TaxInterface tint) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				
				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};
		Booking booking = this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);
		this.hotel.getProcessorHotel().submitRenting(booking);
		booking.cancel();

		assertNull(this.room.getBooking("XPTO"));
	}*/
	
	/*@Test
	public void failednotCancelled(@Mocked final BankInterface bint, @Mocked final TaxInterface tint) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				
				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};
		Booking booking = this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);

		assertNull(this.room.getBooking("XPTO"));
	}*/
	
	

	@After
	public void tearDown() {
		Hotel.hotels.clear();
		IRS.getIRS().clearAll();
		Room.clearCounter();
	}
}
