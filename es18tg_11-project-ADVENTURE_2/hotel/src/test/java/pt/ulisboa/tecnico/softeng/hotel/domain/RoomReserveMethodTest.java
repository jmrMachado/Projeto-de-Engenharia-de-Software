package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.fail;

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
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;

@RunWith(JMockit.class)
public class RoomReserveMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 24);
	private Room room;

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
		Hotel hotel = new Hotel(HOTEL_CODE, HOTEL_NAME, NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
		this.room = new Room(hotel, "01", Type.SINGLE);
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

		Assert.assertEquals(1, this.room.getNumberOfBookings());
		Assert.assertTrue(booking.getReference().length() > 0);
		Assert.assertEquals(this.arrival, booking.getArrival());
		Assert.assertEquals(this.departure, booking.getDeparture());
	}

	@Test(expected = HotelException.class)
	public void noDouble() {
		this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.DOUBLE, this.arrival, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullType() {
		this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, null, this.arrival, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, null, this.departure);
	}
	
	@Test(expected = HotelException.class)
	public void nullNIF() {
		this.room.reserve(IBAN_CLIENTE, null, Type.SINGLE, this.arrival, this.departure);
	}
	
	@Test(expected = HotelException.class)
	public void noLengthNIF() {
		this.room.reserve(IBAN_CLIENTE, "", Type.SINGLE, this.arrival, this.departure);
	}
	
	@Test(expected = HotelException.class)
	public void spaceNIF() { 
		this.room.reserve(IBAN_CLIENTE, "        ", Type.SINGLE, this.arrival, this.departure);
	}
	
	@Test(expected = HotelException.class)
	public void nullIBAN() {
		this.room.reserve(null, NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);
	}
	
	@Test(expected = HotelException.class)
	public void noLengthIBAN() {
		this.room.reserve("", NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);
	}
	
	@Test(expected = HotelException.class)
	public void spaceIBAN() { 
		this.room.reserve("        ", NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, this.arrival, null);
	}

	@Test
	public void allConflict(@Mocked final BankInterface bint, @Mocked final TaxInterface tint) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				
				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};
		this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);

		try {
			this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);
			fail();
		} catch (HotelException he) {
			Assert.assertEquals(1, this.room.getNumberOfBookings());
		}
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
		IRS.getIRS().clearAll();
		Room.clearCounter();
	}
}
