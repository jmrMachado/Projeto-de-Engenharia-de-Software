package pt.ulisboa.tecnico.softeng.broker.domain;

import mockit.Delegate;
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
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;

@RunWith(JMockit.class)
public class ReserveActivityStateProcessMethodTest {
	private static final String IBAN = "BK01987654321";
	private static final int AMOUNT = 300;
	private static final int AGE = 20;
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
    private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final LocalDate begin = new LocalDate(2016, 12, 19);
	private static final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;
	private Buyer comprador = new Buyer(IRS.getIRS(), "123456789", "Pedro", "Rua");
	private Seller vendedor = new Seller(IRS.getIRS(), "987654321", "Pedro", "Rua");

	@Injectable
	private Broker broker = new Broker("BR01", "eXtremeADVENTURE", "123456789", "987654321", IBAN);
	private Client temp = new Client(broker, IBAN, "123456789", "6TG", 20);

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.temp, begin, end, AMOUNT, true);
		this.adventure.setState(State.RESERVE_ACTIVITY);
	}

	@Test
    public void successNoBookRoom(@Mocked final ActivityInterface activityInterface, @Mocked final BankInterface bankInterface) {
        Adventure sameDayAdventure = new Adventure(this.temp, begin, begin, AMOUNT, false);
		sameDayAdventure.setState(State.RESERVE_ACTIVITY);

		new Expectations() {
			{
				ActivityInterface.reserveActivity(begin, begin, AGE, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;
            }
		};

		sameDayAdventure.process();

        Assert.assertEquals(State.PROCESS_PAYMENT, sameDayAdventure.getState());
        Assert.assertNull(sameDayAdventure.getRoomConfirmation());
		Assert.assertNotNull(sameDayAdventure.getActivityConfirmation());
	}

	@Test
	public void successBookRoom(@Mocked final ActivityInterface activityInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(begin, end, AGE, this.anyString, this.anyString);
				this.result = ACTIVITY_CONFIRMATION;
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.BOOK_ROOM, this.adventure.getState());
	}

	@Test
	public void activityException(@Mocked final ActivityInterface activityInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(begin, end, AGE, this.anyString, this.anyString);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void singleRemoteAccessException(@Mocked final ActivityInterface activityInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(begin, end, AGE, this.anyString, this.anyString);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(State.RESERVE_ACTIVITY, this.adventure.getState());
	}

	@Test
	public void maxRemoteAccessException(@Mocked final ActivityInterface activityInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(begin, end, AGE, this.anyString, this.anyString);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}

	@Test
	public void maxMinusOneRemoteAccessException(@Mocked final ActivityInterface activityInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(begin, end, AGE, this.anyString, this.anyString);
				this.result = new RemoteAccessException();
			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.RESERVE_ACTIVITY, this.adventure.getState());
	}

	@Test
	public void twoRemoteAccessExceptionOneSuccess(@Mocked final ActivityInterface activityInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(begin, end, AGE, this.anyString, this.anyString);
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 2) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							return ACTIVITY_CONFIRMATION;
						}
					}
				};
				this.times = 3;

			}
		};

		this.adventure.process();
		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.BOOK_ROOM, this.adventure.getState());
	}

	@Test
	public void oneRemoteAccessExceptionOneActivityException(@Mocked final ActivityInterface activityInterface) {
		new Expectations() {
			{
				ActivityInterface.reserveActivity(begin, end, AGE, this.anyString, this.anyString);
				this.result = new Delegate() {
					int i = 0;

					public String delegate() {
						if (this.i < 1) {
							this.i++;
							throw new RemoteAccessException();
						} else {
							throw new ActivityException();
						}
					}
				};
				this.times = 2;
			}
		};

		this.adventure.process();
		this.adventure.process();

		Assert.assertEquals(State.UNDO, this.adventure.getState());
	}


	@After
	public void tearDown(){
        Broker.brokers.clear();
		IRS.getIRS().clearAll();
	}
}