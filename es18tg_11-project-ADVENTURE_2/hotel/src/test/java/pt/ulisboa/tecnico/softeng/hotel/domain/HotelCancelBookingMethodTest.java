package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;


@RunWith(JMockit.class)
public class HotelCancelBookingMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);
	private Hotel hotel;
	private Room room;
	private String NIF_HOTEL = "123456789";
	private String IBAN_HOTEL = "IBAN_HOTEL";
	private String NIF_CLIENTE = "123456788";
	private String IBAN_CLIENTE = "IBAN_CLIENTE";
	private double P_SINGLE = 10;
	private double P_DOUBLE = 20;
	private Booking booking;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris",NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
		this.room = new Room(this.hotel, "01", Type.DOUBLE);
		this.booking = this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.DOUBLE, this.arrival, this.departure);
	}

	@Test
	public void success() {
		Booking booking = this.booking;
		this.hotel.getProcessorHotel().submitRenting(booking);
		booking.cancel();

		RoomBookingData rbd = new RoomBookingData(this.room,this.booking);
		String str = rbd.getReference();
				
		Assert.assertTrue(str != null);
		Assert.assertTrue(this.booking.isCancelled());
	}
	@Test
	public void nullBooking(@Mocked final BankInterface bint, @Mocked final TaxInterface tint) {
		new Expectations() {
			{
				BankInterface.cancelPayment(this.anyString);
				
				TaxInterface.cancelInvoice(this.anyString);
			}
		};
		Hotel.cancelBooking(this.booking.getReference());
		
		Assert.assertTrue(this.booking.getCancelledInvoice());
		Assert.assertTrue(this.booking.getReference() != null);
	
	}

	@Test(expected = HotelException.class)
	public void doesNotExist() {
		Hotel.cancelBooking("XPTO");
	}

	@Test(expected = HotelException.class)
	public void nullReference() {
		Hotel.cancelBooking(null);
	}

	@Test(expected = HotelException.class)
	public void emptyReference() {
		Hotel.cancelBooking("");
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
