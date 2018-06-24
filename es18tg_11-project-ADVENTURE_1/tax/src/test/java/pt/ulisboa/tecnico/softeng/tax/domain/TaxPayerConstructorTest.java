package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class TaxPayerConstructorTest {

    private String TAXPAYER_NAME = "Alice";
    private String TAXPAYER_NIF = "123123123";
    private String TAXPAYER_ADDRESS = "Rua X, Lisboa";
    
    @Test
    public void success() {

        Buyer buyer = new Buyer(TAXPAYER_NIF, TAXPAYER_NAME, TAXPAYER_ADDRESS);

        Assert.assertEquals(TAXPAYER_NIF, buyer.getNIF());
        Assert.assertTrue(buyer.getNIF().length() == 9);
        Assert.assertEquals(TAXPAYER_NAME, buyer.getName());
        Assert.assertEquals(TAXPAYER_ADDRESS, buyer.getAddress());
        Assert.assertEquals(1, IRS._contribuintes.size());
    }

    @Test(expected = TaxException.class)
    public void nullNif() {
        new Buyer(null, TAXPAYER_NAME, TAXPAYER_ADDRESS);
    }

    @Test(expected = TaxException.class)
    public void nullName() {
        new Buyer(TAXPAYER_NIF, null, TAXPAYER_ADDRESS);
    }

    @Test(expected = TaxException.class)
    public void nullAddress() {
        new Buyer(TAXPAYER_NIF, TAXPAYER_NAME, null);
    }

    @Test(expected = TaxException.class)
    public void blankNif() {
        new Buyer("     ",TAXPAYER_NAME, TAXPAYER_ADDRESS);
    }

    @Test(expected = TaxException.class)
    public void blankName() {
        new Buyer(TAXPAYER_NIF,"      ", TAXPAYER_ADDRESS);
    }

    @Test(expected = TaxException.class)
    public void blankAddress() {
        new Buyer(TAXPAYER_NIF, TAXPAYER_NAME,"     ");
    }

    @Test(expected = TaxException.class)
    public void nifSizeLess() {
        new Buyer("12345678", TAXPAYER_NAME, TAXPAYER_ADDRESS);
    }

    @Test(expected = TaxException.class)
    public void nifSizeMore() {
        new Buyer("1234567891011", TAXPAYER_NAME, TAXPAYER_ADDRESS);
    }

    @Test(expected = TaxException.class)
    public void nifNotUnique() {
        Buyer A = new Buyer(TAXPAYER_NIF, TAXPAYER_NAME, TAXPAYER_ADDRESS);
        Buyer B = new Buyer(TAXPAYER_NIF, "Ana", "Rua Y, Porto");
        
        
    }
    
    @After
    public void tearDown() {
        IRS._contribuintes.clear();
    }

}
