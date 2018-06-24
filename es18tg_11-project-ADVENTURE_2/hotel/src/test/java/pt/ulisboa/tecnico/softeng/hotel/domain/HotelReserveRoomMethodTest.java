package pt.ulisboa.tecnico.softeng.hotel.domain;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;

import static junit.framework.TestCase.assertTrue;

@RunWith(JMockit.class)
public class HotelReserveRoomMethodTest {
    private final LocalDate arrival = new LocalDate(2016, 12, 19);
    private final LocalDate departure = new LocalDate(2016, 12, 24);
    private Room room;
    private Hotel hotel;
    
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
    	
    	
    	
        hotel = new Hotel(HOTEL_CODE, HOTEL_NAME, NIF_HOTEL, IBAN_HOTEL, P_SINGLE, P_DOUBLE);
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
        String ref = Hotel.reserveRoom(IBAN_CLIENTE, NIF_CLIENTE, Room.Type.SINGLE, arrival, departure);
        assertTrue(ref.startsWith("XPTO12"));
    }

    @Test(expected = HotelException.class)
    public void noHotels() {
        Hotel.hotels.clear();
        Hotel.reserveRoom(IBAN_CLIENTE, NIF_CLIENTE, Room.Type.SINGLE, arrival, departure);
    }

    @Test(expected = HotelException.class)
    public void noVacancy() {
        hotel.removeRooms();
        String ref = Hotel.reserveRoom(IBAN_CLIENTE, NIF_CLIENTE, Room.Type.SINGLE, arrival, new LocalDate(2016, 12, 25));
    }

    @Test(expected = HotelException.class)
    public void noRooms() {
        hotel.removeRooms();
        Hotel.reserveRoom(IBAN_CLIENTE, NIF_CLIENTE, Room.Type.SINGLE, arrival, new LocalDate(2016, 12, 25));
    }
	
    @After
    public void tearDown() {
        Hotel.hotels.clear();
        IRS.getIRS().clearAll();
        Room.clearCounter();
    }

}