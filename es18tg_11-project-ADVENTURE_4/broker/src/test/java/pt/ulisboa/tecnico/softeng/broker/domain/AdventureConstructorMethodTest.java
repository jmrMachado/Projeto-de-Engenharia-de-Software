package pt.ulisboa.tecnico.softeng.broker.domain;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

@RunWith(JMockit.class)
public class AdventureConstructorMethodTest extends RollbackTestAbstractClass {
	@Override
	public void populate4Test() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE", BROKER_NIF_AS_SELLER, NIF_AS_BUYER, BROKER_IBAN);
		this.client = new Client(this.broker, CLIENT_IBAN, CLIENT_NIF, DRIVING_LICENSE, AGE);
	}

	@Test
	public void success() {
		Adventure adventure = new Adventure(this.begin, this.end, this.client, AMOUNT,MARGIN);

		Assert.assertEquals(this.broker, adventure.getClient().getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(this.client, adventure.getClient());
		Assert.assertEquals(MARGIN, adventure.getMargin(), 0.0d);
		Assert.assertEquals(AMOUNT, adventure.getCurrentAmount(), 0);
		Assert.assertTrue(this.broker.getClientByNIF(CLIENT_NIF).getAdventureSet().contains(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void nullBegin() {
		new Adventure(null, this.end, this.client, AMOUNT,MARGIN);
	}

	@Test(expected = BrokerException.class)
	public void nullEnd() {
		new Adventure(this.begin, null, this.client, AMOUNT,MARGIN);
	}

	@Test
	public void successEqual18() {
		Adventure adventure = new Adventure(this.begin, this.end,
				new Client(this.broker, CLIENT_IBAN, OTHER_NIF, DRIVING_LICENSE + "1", 18), AMOUNT, MARGIN);

		Assert.assertEquals(this.broker, adventure.getClient().getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(18, adventure.getAge());
		Assert.assertEquals(CLIENT_IBAN, adventure.getIban());
		Assert.assertEquals(MARGIN, adventure.getMargin(), 0);
		Assert.assertEquals(AMOUNT, adventure.getCurrentAmount(), 0);
		Assert.assertTrue(this.broker.getClientByNIF(CLIENT_NIF).getAdventureSet().contains(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void negativeAge() {
		Client c = new Client(this.broker, CLIENT_IBAN, OTHER_NIF, DRIVING_LICENSE, -17);
		new Adventure(this.begin, this.end, c, AMOUNT, MARGIN);
	}

	@Test
	public void successEqual100() {
		Client c = new Client(this.broker, CLIENT_IBAN, OTHER_NIF, DRIVING_LICENSE + "1", 100);
		Adventure adventure = new Adventure(this.begin, this.end, c,AMOUNT, MARGIN);

		Assert.assertEquals(this.broker, adventure.getClient().getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(100, adventure.getAge());
		Assert.assertEquals(CLIENT_IBAN, adventure.getIban());
		Assert.assertEquals(MARGIN, adventure.getMargin(), 0);
		Assert.assertEquals(AMOUNT, adventure.getCurrentAmount(), 0);
		Assert.assertTrue(this.broker.getClientByNIF(CLIENT_NIF).getAdventureSet().contains(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void over100() {
		Client c = new Client(this.broker, CLIENT_IBAN, OTHER_NIF, DRIVING_LICENSE, 101);
		new Adventure(this.begin, this.end, c, AMOUNT,MARGIN);
	}

	@Test(expected = BrokerException.class)
	public void negativeMargin() {
		new Adventure(this.begin, this.end, this.client, AMOUNT,-100);
	}
	
	@Test(expected = BrokerException.class)
	public void negativeAmount() {
		new Adventure(this.begin, this.end, this.client, -1, MARGIN);
	}

	@Test
	public void success1Amount() {
		Adventure adventure = new Adventure(this.begin, this.end, this.client,AMOUNT, 1);

		Assert.assertEquals(this.broker, adventure.getClient().getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.end, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals(CLIENT_IBAN, adventure.getIban());
		Assert.assertEquals(1, adventure.getMargin(), 0);
		Assert.assertEquals(AMOUNT, adventure.getCurrentAmount(), 0);
		Assert.assertTrue(this.broker.getClientByNIF(CLIENT_NIF).getAdventureSet().contains(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void zeroMargin() {
		new Adventure(this.begin, this.end, this.client, AMOUNT, 0);
	}

	@Test
	public void successEqualDates() {
		Adventure adventure = new Adventure(this.begin, this.begin, this.client, AMOUNT, MARGIN);

		Assert.assertEquals(this.broker, adventure.getClient().getBroker());
		Assert.assertEquals(this.begin, adventure.getBegin());
		Assert.assertEquals(this.begin, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals(CLIENT_IBAN, adventure.getIban());
		Assert.assertEquals(MARGIN, adventure.getMargin(), 0);
		Assert.assertEquals(AMOUNT, adventure.getCurrentAmount(), 0);
		Assert.assertTrue(this.broker.getClientByNIF(CLIENT_NIF).getAdventureSet().contains(adventure));

		Assert.assertNull(adventure.getPaymentConfirmation());
		Assert.assertNull(adventure.getActivityConfirmation());
		Assert.assertNull(adventure.getRoomConfirmation());
	}

	@Test(expected = BrokerException.class)
	public void inconsistentDates() {
		new Adventure(this.begin, this.begin.minusDays(1), this.client, AMOUNT, MARGIN);
	}

}
