package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.lang.Thread.State;

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

public class AdventureProcessMethodTest {
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Broker broker;
	private String IBAN;

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE");

		Bank bank = new Bank("Money", "BK01");
		Client client = new Client(bank, "António");
		Account account = new Account(bank, client);
		this.IBAN = account.getIBAN();
		account.deposit(1000);

		Hotel hotel = new Hotel("XPTO123", "Paris");
		new Room(hotel, "01", Type.SINGLE);

		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(provider, "Bush Walking", 18, 80, 10);
		new ActivityOffer(activity, this.begin, this.end);
		new ActivityOffer(activity, this.begin, this.begin);
	}

	@Test
	public void success() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, this.IBAN, 300);

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
		Adventure adventure = new Adventure(this.broker, this.begin, this.begin, 20, this.IBAN, 300);

		adventure.process();
		adventure.process();

		assertEquals(Adventure.State.CONFIRMED, adventure.getState());
		assertNotNull(adventure.getPaymentConfirmation());
		assertNotNull(adventure.getActivityConfirmation());
	}
	
	@Test
	public void getsIDTest() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, this.IBAN, 300);
		adventure.getID();		
	}
	
	@Test
	public void setTest() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, this.IBAN, 300);
		String payCanc = "payCanc";
		String actCanc = "activity Canc";
		String roomCan = "Room Canc";
		adventure.setPaymentCancellation(payCanc);
		assertEquals(adventure.getPaymentCancellation(), payCanc);
		adventure.setActivityCancellation(actCanc);
		assertEquals(adventure.getActivityCancellation(), actCanc);
		adventure.setRoomCancellation(roomCan);
		assertEquals(adventure.getRoomCancellation(), roomCan);	
	}
	
	
	
	@Test
	public void setState() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, this.IBAN, 300);
		adventure.setState((new UndoState()).getState());
		assertEquals(adventure.getState(), (new UndoState()).getState());
		adventure.setState((new CancelledState()).getState());
		assertEquals(adventure.getState(), (new CancelledState()).getState());
	}
	
	
	@Test
	public void RoomCancellationTest() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, this.IBAN, 300);
		/*null e null */
		adventure.setRoomCancellation(null);
		assertEquals(adventure.getRoomCancellation(), null);
		adventure.setRoomConfirmation(null);
		assertEquals(adventure.getRoomConfirmation(), null);
		assertEquals(adventure.cancelRoom(), false) ;
		
		/*null e not null*/
		adventure.setRoomCancellation("algo");
		assertEquals(adventure.getRoomCancellation(), "algo");
		adventure.setRoomConfirmation(null);
		assertEquals(adventure.getRoomConfirmation(), null);
		assertEquals(adventure.cancelRoom(), false) ;

		
		/*not null e null*/
		adventure.setRoomCancellation(null);
		assertEquals(adventure.getRoomCancellation(), null);
		adventure.setRoomConfirmation("algo");
		assertEquals(adventure.getRoomConfirmation(), "algo");
		assertEquals(adventure.cancelRoom(), true) ;

		/*not null e not null*/
		adventure.setRoomCancellation("algo");
		assertEquals(adventure.getRoomCancellation(), "algo");
		adventure.setRoomConfirmation("algo");
		assertEquals(adventure.getRoomConfirmation(), "algo");
		assertEquals(adventure.cancelRoom(), false) ;
		
	}
	
	@Test
	public void ActivityCancellationTest() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, this.IBAN, 300);
		/*null e null */
		adventure.setActivityCancellation(null);
		assertEquals(adventure.getActivityCancellation(), null);
		adventure.setActivityConfirmation(null);
		assertEquals(adventure.getActivityConfirmation(), null);
		assertEquals(adventure.cancelActivity(), false);
		
		/*null e not null*/
		adventure.setActivityCancellation("algo");
		assertEquals(adventure.getActivityCancellation(), "algo");
		adventure.setActivityConfirmation(null);
		assertEquals(adventure.getActivityConfirmation(), null);
		assertEquals(adventure.cancelActivity(), false);

		
		/*not null e null*/
		adventure.setActivityCancellation(null);
		assertEquals(adventure.getActivityCancellation(), null);
		adventure.setActivityConfirmation("algo");
		assertEquals(adventure.getActivityConfirmation(), "algo");
		assertEquals(adventure.cancelActivity(), true);

		/*not null e not null*/
		adventure.setActivityCancellation("algo");
		assertEquals(adventure.getActivityCancellation(), "algo");
		adventure.setActivityConfirmation("algo");
		assertEquals(adventure.getActivityConfirmation(), "algo");
		assertEquals(adventure.cancelActivity(), false);		
	}
	
	@Test
	public void PaymentCancellationTest() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, this.IBAN, 300);
		/*null e null */
		adventure.setPaymentCancellation(null);
		assertEquals(adventure.getPaymentCancellation(), null);
		adventure.setPaymentConfirmation(null);
		assertEquals(adventure.getPaymentConfirmation(), null);
		assertEquals(adventure.cancelPayment(), false);
		
		/*null e not null*/
		adventure.setPaymentCancellation("algo");
		assertEquals(adventure.getPaymentCancellation(), "algo");
		adventure.setPaymentConfirmation(null);
		assertEquals(adventure.getPaymentConfirmation(), null);
		assertEquals(adventure.cancelPayment(), false);

		
		/*not null e null*/
		adventure.setPaymentCancellation(null);
		assertEquals(adventure.getPaymentCancellation(), null);
		adventure.setPaymentConfirmation("algo");
		assertEquals(adventure.getPaymentConfirmation(), "algo");
		assertEquals(adventure.cancelPayment(), true);

		/*not null e not null*/
		adventure.setPaymentCancellation("algo");
		assertEquals(adventure.getPaymentCancellation(), "algo");
		adventure.setPaymentConfirmation("algo");
		assertEquals(adventure.getPaymentConfirmation(), "algo");
		assertEquals(adventure.cancelPayment(), false);
		
	}
	
	

	@After
	public void tearDown() {
		Bank.banks.clear();
		Hotel.hotels.clear();
		ActivityProvider.providers.clear();
		Broker.brokers.clear();
	}
}

