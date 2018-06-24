package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AdventureProcessMethodTest {
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Broker broker;
	private pt.ulisboa.tecnico.softeng.broker.domain.Client temp;
	private String IBAN = "BK011234567";

	@Before
	public void setUp() {
        Buyer comprador = new Buyer(IRS.getIRS(), "123456789", "Pedro", "Rua");
		Seller vendedor = new Seller(IRS.getIRS(), "987654321", "Pedro", "Rua");

		Bank bank = new Bank("Money", "BK01");
		Client client = new Client(bank, "António");
		Account account = new Account(bank, client);
		this.IBAN = account.getIBAN();
        account.deposit(100000);
        this.broker = new Broker("BR01", "eXtremeADVENTURE", "987654321", "123456789", IBAN);

		Hotel hotel = new Hotel("XPTO123", "Paris", vendedor.getNIF(), broker.getIBAN(), 10.0, 15.0);
		new Room(hotel, "01", Type.SINGLE);

		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure", "987654321", IBAN);
		Activity activity = new Activity(provider, "Bush Walking", 18, 80, 10);
		new ActivityOffer(activity, this.begin, this.end, 30);
		new ActivityOffer(activity, this.begin, this.begin, 30);
		this.temp = new pt.ulisboa.tecnico.softeng.broker.domain.Client(this.broker, IBAN, "123456789", "1TG", 50);
	}

	@Test
	public void success() {
		Adventure adventure = new Adventure(this.temp, this.begin, this.end, 300, false);

		adventure.process();
		adventure.process();
		adventure.process();

		assertEquals(Adventure.State.CONFIRMED, adventure.getState());
		assertNotNull(adventure.getPaymentConfirmation());
		assertNotNull(adventure.getRoomConfirmation());
		assertNotNull(adventure.getActivityConfirmation());
	}

	@Test
	public void successNoHotelBooking() {
		Adventure adventure = new Adventure(this.temp, this.begin, this.begin, 300, false);

		adventure.process();
		adventure.process();

		assertEquals(Adventure.State.CONFIRMED, adventure.getState());
		assertNotNull(adventure.getPaymentConfirmation());
		assertNotNull(adventure.getActivityConfirmation());
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
		Hotel.hotels.clear();
		ActivityProvider.providers.clear();
		Broker.brokers.clear();
		IRS.getIRS().clearAll();
	}
}
