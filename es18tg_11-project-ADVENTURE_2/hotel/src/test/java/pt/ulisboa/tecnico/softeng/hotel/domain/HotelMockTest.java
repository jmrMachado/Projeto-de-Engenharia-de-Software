package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Delegate;
import mockit.Expectations;
import mockit.FullVerifications;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

@RunWith(JMockit.class)
public class HotelMockTest {
	
	
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
	
	private static final String NIF_OF_RENT = "666666666";
	
	private static final String IBAN = "IBAN";
	private static final String NIF = "123456789";
	
	
	private static final String CANCEL_PAYMENT_REFERENCE = "CancelPaymentReference";
	private static final String INVOICE_REFERENCE = "InvoiceReference";
	private static final String PAYMENT_REFERENCE = "PaymentReference";
	
	@Before
	public void setUp() {
				
		this.hotel = new Hotel(HOTEL_CODE, HOTEL_NAME, NIF , IBAN_HOTEL, P_SINGLE, P_DOUBLE);
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
		this.hotel.getProcessorHotel().submitRenting(booking);
		
		new FullVerifications() {
			{
			}
		};
	}
	

	@Test
	public void oneTaxFailureOnSubmitInvoice(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				this.result = PAYMENT_REFERENCE;
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = new TaxException();
				this.result = INVOICE_REFERENCE;
			}
		};

		Booking booking = this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);
		this.hotel.getProcessorHotel().submitRenting(booking);
		this.hotel.getProcessorHotel().submitRenting
		(new Booking(this.hotel,this.arrival,this.departure,booking.getAmount() ,booking.getIBAN(),booking.getNIF(),booking.getRoomID()));

		new FullVerifications(taxInterface) {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.times = 3;
			}
		};
	}

	@Test
	public void oneRemoteFailureOnSubmitInvoice(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				this.result = PAYMENT_REFERENCE;
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = new RemoteAccessException();
				this.result = INVOICE_REFERENCE;
			}
		};
		Booking booking = this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);
		this.hotel.getProcessorHotel().submitRenting(booking);
		this.hotel.getProcessorHotel().submitRenting
		(new Booking(this.hotel,this.arrival,this.departure,booking.getAmount() ,booking.getIBAN(),booking.getNIF(),booking.getRoomID()));

		new FullVerifications(taxInterface) {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.times = 3;
			}
		};
	}

	@Test
	public void oneBankFailureOnProcessPayment(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				this.result = new BankException();
				this.result = PAYMENT_REFERENCE;
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = INVOICE_REFERENCE;
			}
		};

		Booking booking = this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);
		this.hotel.getProcessorHotel().submitRenting(booking);
		this.hotel.getProcessorHotel().submitRenting
		(new Booking(this.hotel,this.arrival,this.departure,booking.getAmount() ,booking.getIBAN(),booking.getNIF(),booking.getRoomID()));

		new FullVerifications(bankInterface) {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				this.times = 3;
			}
		};
	}

	@Test
	public void oneRemoteFailureOnProcessPayment(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				this.result = new RemoteAccessException();
				this.result = PAYMENT_REFERENCE;
				TaxInterface.submitInvoice((InvoiceData) this.any);
				this.result = INVOICE_REFERENCE;
			}
		};

		Booking booking = this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);
		this.hotel.getProcessorHotel().submitRenting(booking);
		this.hotel.getProcessorHotel().submitRenting
		(new Booking(this.hotel,this.arrival,this.departure,booking.getAmount() ,booking.getIBAN(),booking.getNIF(),booking.getRoomID()));

		new FullVerifications(bankInterface) {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				this.times = 3;
			}
		};
	}

	@Test
	public void successCancel(@Mocked final TaxInterface taxInterface, @Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				BankInterface.processPayment(this.anyString, this.anyDouble);

				TaxInterface.cancelInvoice(this.anyString);
				BankInterface.cancelPayment(this.anyString);
			}
		};

		Booking booking = this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);
		this.hotel.getProcessorHotel().submitRenting(booking);
		booking.cancel();
		
		
		new FullVerifications() {
			{
			}
		};
	}

	@Test
	public void oneBankExceptionOnCancelPayment(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				BankInterface.processPayment(this.anyString, this.anyDouble);

				BankInterface.cancelPayment(this.anyString);
				this.result = new BankException();
				this.result = CANCEL_PAYMENT_REFERENCE;
				TaxInterface.cancelInvoice(this.anyString);
			}
		};

		Booking booking = this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);
		this.hotel.getProcessorHotel().submitRenting(booking);
		booking.cancel();
		this.hotel.getProcessorHotel().submitRenting(booking);

		new FullVerifications(bankInterface) {
			{
				BankInterface.cancelPayment(this.anyString);
				this.times = 2;
			}
		};
	}

	@Test
	public void oneRemoteExceptionOnCancelPayment(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				TaxInterface.submitInvoice((InvoiceData) this.any);
				BankInterface.processPayment(this.anyString, this.anyDouble);

				BankInterface.cancelPayment(this.anyString);
				this.result = new RemoteAccessException();
				this.result = CANCEL_PAYMENT_REFERENCE;
				TaxInterface.cancelInvoice(this.anyString);
			}
		};

		Booking booking = this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);
		this.hotel.getProcessorHotel().submitRenting(booking);
		booking.cancel();
		this.hotel.getProcessorHotel().submitRenting(booking);

		new FullVerifications(bankInterface) {
			{
				BankInterface.cancelPayment(this.anyString);
				this.times = 2;
			}
		};
	}

	@Test
	public void oneTaxExceptionOnCancelInvoice(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				TaxInterface.submitInvoice((InvoiceData) this.any);
				BankInterface.cancelPayment(this.anyString);
				this.result = CANCEL_PAYMENT_REFERENCE;
				TaxInterface.cancelInvoice(this.anyString);
				this.result = new Delegate() {
					int i = 0;

					public void delegate() {
						if (this.i < 1) {
							this.i++;
							throw new TaxException();
						}
					}
				};
			}
		};

		Booking booking = this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);
		this.hotel.getProcessorHotel().submitRenting(booking);
		booking.cancel();
		this.hotel.getProcessorHotel().submitRenting(booking);

		new FullVerifications(taxInterface) {
			{
				TaxInterface.cancelInvoice(this.anyString);
				this.times = 2;
			}
		};
	}

	@Test
	public void oneRemoteExceptionOnCancelInvoice(@Mocked final TaxInterface taxInterface,
			@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				TaxInterface.submitInvoice((InvoiceData) this.any);

				BankInterface.cancelPayment(this.anyString);
				this.result = CANCEL_PAYMENT_REFERENCE;
				TaxInterface.cancelInvoice(this.anyString);
				this.result = new Delegate() {
					int i = 0;

					public void delegate() {
						if (this.i < 1) {
							this.i++;
							throw new RemoteAccessException();
						}
					}
				};
			}
		};

		Booking booking = this.room.reserve(IBAN_CLIENTE, NIF_CLIENTE, Type.SINGLE, this.arrival, this.departure);
		this.hotel.getProcessorHotel().submitRenting(booking);
		booking.cancel();
		this.hotel.getProcessorHotel().submitRenting(booking);

		new FullVerifications(taxInterface) {
			{
				TaxInterface.cancelInvoice(this.anyString);
				this.times = 2;
			}
		};
	}
	
	
	
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
        IRS.getIRS().clearAll();
        Room.clearCounter();
		this.hotel.getProcessorHotel().clean();
	}


}
