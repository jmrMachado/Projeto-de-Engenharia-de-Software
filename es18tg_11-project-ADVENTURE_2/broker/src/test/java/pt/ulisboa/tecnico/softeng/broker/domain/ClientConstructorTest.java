package pt.ulisboa.tecnico.softeng.broker.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;

public class ClientConstructorTest {
    private static final String IBAN = "BK011234567";
    private Broker broker;
    private static final int AGE = 20;
    private static final String NIF = "123456789";
    private static final String LICENSE = "1TG";

    @Before
    public void setUp() {
        Buyer comprador = new Buyer(IRS.getIRS(), "123456789", "Pedro", "Rua");
        Seller vendedor = new Seller(IRS.getIRS(), "987654321", "Pedro", "Rua");
        this.broker = new Broker("BR01", "eXtremeADVENTURE", "123456789", "987654321", IBAN);
    }

    @Test
    public void success(){
        Client sucesso = new Client(this.broker, IBAN, NIF, LICENSE, AGE);

        Assert.assertEquals(this.broker, sucesso.getBROKER());
        Assert.assertEquals(IBAN, sucesso.getIBAN());
        Assert.assertEquals(NIF, sucesso.getNIF());
        Assert.assertEquals(LICENSE, sucesso.getLICENSE());
        Assert.assertEquals(AGE, sucesso.getAGE());
    }

    @Test(expected = BrokerException.class)
    public void nullBroker(){
        new Client(null, IBAN, NIF, LICENSE, AGE);
    }

    @Test(expected = BrokerException.class)
    public void nullIban(){
        new Client(this.broker, null, NIF, LICENSE, AGE);
    }

    @Test(expected = BrokerException.class)
    public void emptyIban(){
        new Client(this.broker, "", NIF, LICENSE, AGE);
    }

    @Test(expected = BrokerException.class)
    public void blankIban(){
        new Client(this.broker, "      ", NIF, LICENSE, AGE);
    }

    @Test(expected = BrokerException.class)
    public void nullNif(){
        new Client(this.broker, IBAN, null, LICENSE, AGE);
    }

    @Test(expected = BrokerException.class)
    public void emptyNif(){
        new Client(this.broker, IBAN, "", LICENSE, AGE);
    }

    @Test(expected = BrokerException.class)
    public void blankNif(){
        new Client(this.broker, IBAN, "        ", LICENSE, AGE);
    }

    @Test(expected = BrokerException.class)
    public void wrongSyntaxNif(){
        new Client(this.broker, IBAN, "1234567890", LICENSE, AGE);
    }

    @Test(expected = BrokerException.class)
    public void nullLicense(){
        new Client(this.broker, IBAN, NIF, null, AGE);
    }

    @Test(expected = BrokerException.class)
    public void emptyLicense(){
        new Client(this.broker, IBAN, NIF, "", AGE);
    }

    @Test(expected = BrokerException.class)
    public void blankLicense(){
        new Client(this.broker, IBAN, NIF,"       ", AGE);
    }

    @Test(expected = BrokerException.class)
    public void negativeAge(){
        new Client(this.broker, IBAN, NIF, LICENSE, -45);
    }

    @After
    public void tearDown(){
        broker.brokers.clear();
        IRS.getIRS().clearAll();
    }
}
