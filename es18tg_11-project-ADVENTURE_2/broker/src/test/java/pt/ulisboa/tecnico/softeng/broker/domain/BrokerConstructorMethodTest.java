package pt.ulisboa.tecnico.softeng.broker.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;

public class BrokerConstructorMethodTest {

	private static final String IBAN = "BK01987654321";
	private static final String NIF = "123456789";
	private Buyer comprador = new Buyer(IRS.getIRS(), "123456789", "Pedro", "Rua");
	private Seller vendedor = new Seller(IRS.getIRS(), "987654321", "Pedro", "Rua");

	@Test
	public void success() {
		Broker broker = new Broker("BR01", "WeExplore", "987654321","123456789", IBAN);

		Assert.assertEquals("BR01", broker.getCode());
		Assert.assertEquals("WeExplore", broker.getName());
		Assert.assertEquals(0, broker.getNumberOfAdventures());
		Assert.assertEquals("123456789", broker.getBuyerNIF());
		Assert.assertEquals("987654321", broker.getSellerNIF());
		Assert.assertTrue(Broker.brokers.contains(broker));
	}

	@Test
	public void nullCode() {
		try {
			new Broker(null, "WeExplore", "123456789", "987654321", IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyCode() {
		try {
			new Broker("", "WeExplore", "123456789", "987654321", IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankCode() {
		try {
			new Broker("  ", "WeExplore", "123456789", "987654321", IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void uniqueCode() {
		Broker broker = new Broker("BR01", "WeExplore", "123456789", "987654321", IBAN);

		try {
			new Broker("BR01", "WeExploreX", "123456789", "987654321", IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
			Assert.assertTrue(Broker.brokers.contains(broker));
		}
	}

	@Test
	public void nullName() {
		try {
			new Broker("BR01", null, "123456789", "987654321", IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyName() {
		try {
			new Broker("BR01", "", "123456789", "987654321", IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankName() {
		try {
			new Broker("BR01", "    ", "123456789", "987654321", IBAN);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
    public void nullBuyer(){
	    try {
            new Broker("BR01", "WeExplore", null, "987654321", IBAN);
            Assert.fail();
        }catch (BrokerException be){
	        Assert.assertEquals(0, Broker.brokers.size());
        }
    }

    @Test
    public void emptyBuyer(){
        try {
            new Broker("BR01", "WeExplore", "", "987654321", IBAN);
            Assert.fail();
        }catch (BrokerException be){
            Assert.assertEquals(0, Broker.brokers.size());
        }
    }

    @Test
    public void blankBuyer(){
        try {
            new Broker("BR01", "WeExplore", "      ", "987654321", IBAN);
            Assert.fail();
        }catch (BrokerException be){
            Assert.assertEquals(0, Broker.brokers.size());
        }
    }

    @Test
    public void nonExistingBuyer(){
        try {
            new Broker("BR01", "WeExplore", "163469353", "987654321", IBAN);
            Assert.fail();
        }catch (BrokerException be){
            Assert.assertEquals(0, Broker.brokers.size());
        }
    }

    @Test
    public void nullSeller(){
        try {
            new Broker("BR01", "WeExplore", "123456789", null, IBAN);
            Assert.fail();
        }catch (BrokerException be){
            Assert.assertEquals(0, Broker.brokers.size());
        }
    }

    @Test
    public void emptySeller(){
        try {
            new Broker("BR01", "WeExplore", "123456789", "", IBAN);
            Assert.fail();
        }catch (BrokerException be){
            Assert.assertEquals(0, Broker.brokers.size());
        }
    }

    @Test
    public void blankSeller(){
        try {
            new Broker("BR01", "WeExplore", "123456789", "       ", IBAN);
            Assert.fail();
        }catch (BrokerException be){
            Assert.assertEquals(0, Broker.brokers.size());
        }
    }

    @Test
    public void nonExistingSeller(){
        try {
            new Broker("BR01", "WeExplore", "123456789", "175937504", IBAN);
            Assert.fail();
        }catch (BrokerException be){
            Assert.assertEquals(0, Broker.brokers.size());
        }
    }

	@After
	public void tearDown() {
		Broker.brokers.clear();
		IRS.getIRS().clearAll();
	}

}
