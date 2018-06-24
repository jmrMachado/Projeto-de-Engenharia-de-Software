package pt.ulisboa.tecnico.softeng.broker.domain;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;

@RunWith(JMockit.class)
public class AdventureSequenceTest {
	private static final String IBAN = "BK01987654321";
	private static final int AMOUNT = 300;
	private static final int AGE = 20;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
    private static final String TAX_CONFIRMATION = "TaxConfirmation";
	private static final LocalDate arrival = new LocalDate(2016, 12, 19);
	private static final LocalDate departure = new LocalDate(2016, 12, 21);
	private Buyer comprador = new Buyer(IRS.getIRS(), "123456789", "Pedro", "Rua");
	private Seller vendedor = new Seller(IRS.getIRS(), "987654321", "Pedro", "Rua");

    @Injectable
    private Broker broker = new Broker("BR01", "eXtremeADVENTURE", "123456789", "987654321", IBAN);
    private Client temp;

    @Before
    public void setUp() {
        this.temp = new Client(broker, IBAN, "123456789", "6TG", AGE);
    }

	@Test
	public void successSequenceOne(@Mocked final BankInterface bankInterface,
                                   @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
                BankInterface.processPayment(IBAN, this.anyInt);
                this.result = PAYMENT_CONFIRMATION;

                TaxInterface.submitInvoice((InvoiceData) any);
                this.result = TAX_CONFIRMATION;

                ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
                this.result = ACTIVITY_CONFIRMATION;

                HotelInterface.reserveRoom(this.anyString, this.anyString, Type.SINGLE, arrival, departure);
                this.result = ROOM_CONFIRMATION;

                BankInterface.getOperationData(PAYMENT_CONFIRMATION);

                ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);

                HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
			}
		};

        Adventure adventure = new Adventure(this.temp, arrival, departure, AMOUNT, false);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CONFIRMED, adventure.getState());
	}

	@Test
	public void successSequenceTwo(@Mocked final BankInterface bankInterface,
                                   @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
                BankInterface.processPayment(IBAN, this.anyInt);
				this.result = PAYMENT_CONFIRMATION;

                ActivityInterface.reserveActivity(arrival, arrival, AGE, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

                TaxInterface.submitInvoice((InvoiceData) any);
                this.result = TAX_CONFIRMATION;

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);

				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
			}
		};

        Adventure adventure = new Adventure(this.temp, arrival, arrival, AMOUNT, false);

		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CONFIRMED, adventure.getState());
	}

	@Test
    public void unsuccessSequenceThreeBank(@Mocked final BankInterface bankInterface,
                                           @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
                ActivityInterface.reserveActivity(arrival, departure, temp.getAGE(), this.anyString, this.anyString);
                this.result = ACTIVITY_CONFIRMATION;

                HotelInterface.reserveRoom(this.anyString, this.anyString, Type.SINGLE, arrival, departure);
                this.result = ROOM_CONFIRMATION;

                BankInterface.processPayment(this.anyString, this.anyInt);
                this.result = new BankException();
			}
		};

        Adventure adventure = new Adventure(this.temp, arrival, departure, AMOUNT, false);

        adventure.process();
        adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	@Test
	public void unsuccessSequenceTwo(@Mocked final BankInterface bankInterface,
                                     @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface) {

        new Expectations() {
			{
                ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
				this.result = new ActivityException();
			}
		};

        Adventure adventure = new Adventure(this.temp, arrival, departure, AMOUNT, false);

		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	@Test
	public void unsuccessSequenceThree(@Mocked final BankInterface bankInterface,
                                       @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
                ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

                HotelInterface.reserveRoom(this.anyString, this.anyString, Type.SINGLE, arrival, departure);
				this.result = new HotelException();

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

                TaxInterface.cancelInvoice(ACTIVITY_CONFIRMATION);
			}
		};

        Adventure adventure = new Adventure(this.temp, arrival, departure, AMOUNT, false);

		adventure.process();
		adventure.process();
		adventure.process();
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	@Test
	public void unsuccessSequenceFour(@Mocked final BankInterface bankInterface,
                                      @Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface roomInterface, @Mocked final TaxInterface taxInterface) {
		new Expectations() {
			{
                BankInterface.processPayment(this.anyString, this.anyInt);
				this.result = PAYMENT_CONFIRMATION;

                TaxInterface.submitInvoice((InvoiceData) any);
                this.result = TAX_CONFIRMATION;

                ActivityInterface.reserveActivity(arrival, departure, AGE, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;

                HotelInterface.reserveRoom(this.anyString, this.anyString, Type.SINGLE, arrival, departure);
				this.result = ROOM_CONFIRMATION;

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
				this.times = ConfirmedState.MAX_BANK_EXCEPTIONS;

				BankInterface.cancelPayment(PAYMENT_CONFIRMATION);
				this.result = PAYMENT_CANCELLATION;

				ActivityInterface.cancelReservation(ACTIVITY_CONFIRMATION);
				this.result = ACTIVITY_CANCELLATION;

				HotelInterface.cancelBooking(ROOM_CONFIRMATION);
				this.result = ROOM_CANCELLATION;
			}
		};

        Adventure adventure = new Adventure(this.temp, arrival, departure, AMOUNT, false);

		adventure.process();
		adventure.process();
		adventure.process();
		for (int i = 0; i < ConfirmedState.MAX_BANK_EXCEPTIONS; i++) {
			adventure.process();
		}
		adventure.process();

		Assert.assertEquals(State.CANCELLED, adventure.getState());
	}

	@After
    public void tearDown(){
        Broker.brokers.clear();
	    IRS.getIRS().clearAll();
    }

}