package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;

public class AdventureConstructorMethodTest {
	private static final int AGE = 20;
	private static final int AMOUNT = 300;
	private static final String IBAN = "BK011234567";
	private Broker broker;
	private Client pessoa;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	@Before
	public void setUp() {
		Buyer comprador = new Buyer(IRS.getIRS(), "123456789", "Pedro", "Rua");
        Seller vendedor = new Seller(IRS.getIRS(), "987654321", "Pedro", "Rua");
		this.broker = new Broker("BR01", "eXtremeADVENTURE", "123456789", "987654321", IBAN);
		this.pessoa = new Client(this.broker, IBAN, "123456789", "1TG", AGE);
	}

	@Test
	public void success() {
		Adventure adventure = new Adventure(this.pessoa, this.begin, this.end, AMOUNT, true);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
        Assert.assertEquals(300, (int) adventure.getMargin());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test
	public void secondSuccess(){
		Client temp = new Client(this.broker, IBAN, "123456789", "1TG", AGE);
		Adventure adventure = new Adventure(temp, this.begin, this.end, AMOUNT, true);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(AGE, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
        Assert.assertEquals(AMOUNT, (int) adventure.getMargin());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

        Assert.assertNull(adventure.getPaymentConfirmation());
        Assert.assertNull(adventure.getActivityConfirmation());
        Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
    public void nullClient(){ new Adventure(null, begin, end, AMOUNT, true); }

	@Test(expected = BrokerException.class)
	public void nullBroker() {
		new Adventure(null, this.begin, this.end, AMOUNT, true);
	}

	@Test(expected = BrokerException.class)
	public void nullBegin() {
		new Adventure(this.pessoa, null, this.end, AMOUNT, true);
	}

	@Test(expected = BrokerException.class)
	public void nullEnd() {
		new Adventure(this.pessoa, this.begin, null, AMOUNT, true);
	}

	@Test
	public void successEqual18() {
        Client temp = new Client(this.broker, IBAN, "123456789", "1TG", 18);
		Adventure adventure = new Adventure(temp, this.begin, this.end, AMOUNT, true);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(18, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
        Assert.assertEquals(300, (int) adventure.getMargin());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void lessThan18Age() {
		new Adventure(new Client(this.broker, IBAN, "123456789", "1TG", 17), this.begin, this.end, AMOUNT, true);
	}

	@Test
	public void successEqual100() {
		Adventure adventure = new Adventure(new Client(this.broker, IBAN, "123456789", "1TG", 100), this.begin, this.end,AMOUNT, true);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(100, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
        Assert.assertEquals(300, (int) adventure.getMargin());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void over100() {
		new Adventure(new Client(this.broker, IBAN, "123456789", "1TG", 101), this.begin, this.end, AMOUNT, true);
	}

	@Test(expected = BrokerException.class)
	public void nullIBAN() {
		new Adventure(new Client(this.broker, null, "123456789", "1TG", 17), this.begin, this.end, AMOUNT, true);
	}

	@Test(expected = BrokerException.class)
	public void emptyIBAN() {
		new Adventure(new Client(this.broker, "      ", "123456789", "1TG", 17), this.begin, this.end, AMOUNT, true);
	}

	@Test(expected = BrokerException.class)
	public void negativeAmount() {
		new Adventure(this.pessoa, this.begin, this.end, -100, true);
	}

	@Test
	public void success1Amount() {
		Adventure adventure = new Adventure(this.pessoa, this.begin, this.end, 1, true);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
        Assert.assertEquals(1, (int) adventure.getMargin());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void zeroAmount() {
		new Adventure(this.pessoa, this.begin, this.end, 0, true);
	}

	@Test
	public void successEqualDates() {
		Adventure adventure = new Adventure(this.pessoa, this.begin, this.begin, AMOUNT, true);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.begin, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals(IBAN, adventure.getIBAN());
        Assert.assertEquals(300, (int) adventure.getMargin());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void inconsistentDates() {
		new Adventure(this.pessoa, this.begin, this.begin.minusDays(1), AMOUNT, true);
	}

	@After
	public void tearDown() {
		Broker.brokers.clear();
		IRS.getIRS().clearAll();
	}

}
