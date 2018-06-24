package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;


@RunWith(JMockit.class)
public class BankInterfaceMethodTest {
	//private String _IBAN = "IBAN";
	private double _amount = 10;
	private String _clientName = "Muchacho";
	
	private String _bankName = "Medicci";
	private String _bankCode = "luta";
	
	private Bank _bank;
	private Client _client;
	private Account _account;
	
	/**
	 * new bank
	 * new account
	 * new client
	 */
	
	
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
	private Booking booking;
	private Hotel hotel;
	@Before
	public void setUp() {
		this._bank = new Bank(_bankName, _bankCode);
		this._client = new Client(this._bank, _clientName);
		this._account = new Account(this._bank, this._client);
		this._account.deposit(100);
		this.hotel = new Hotel(HOTEL_CODE, HOTEL_NAME, NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
		this.room = new Room(this.hotel, "01", Type.SINGLE);
		
	}
	
	@Test
	public void successProcessPayment() {
		BankInterface.processPayment(this._account.getIBAN(), _amount);
	}

	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
		Bank.banks.clear();
		IRS.getIRS().clearAll();
		Room.clearCounter();
	}
}
