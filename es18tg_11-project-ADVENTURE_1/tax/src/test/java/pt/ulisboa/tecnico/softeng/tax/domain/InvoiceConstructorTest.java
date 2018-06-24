package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class InvoiceConstructorTest {

    private static final float VALUE = 10;
    private static final LocalDate DATE = new LocalDate(2010, 3, 12);
    private static final ItemType ITEM_TYPE = new ItemType("Doces", 2);
    private static final Seller SELLER = new Seller("123456789", "Chico", "Faro");
    private static final Buyer BUYER = new Buyer("123456788", "Alex", "Espanha");

    @Test
    public void InvoiceGetters() {

        Invoice transacao = new Invoice(VALUE, DATE, ITEM_TYPE, SELLER, BUYER);
        Assert.assertEquals((double)transacao.getValue(), 30, 0);
        Assert.assertEquals(transacao.getDate(), DATE);
        Assert.assertEquals(transacao.getItemType(), ITEM_TYPE);
        Assert.assertEquals(transacao.getSeller(), SELLER);
        Assert.assertEquals(transacao.getBuyer(), BUYER);
        Assert.assertNotNull(transacao.getReference());
    }

    @Test(expected = TaxException.class)
    public void emptyVALUE() {
        Invoice i = new Invoice(0, DATE, ITEM_TYPE, SELLER, BUYER);
    }

    @Test(expected = TaxException.class)
    public void negativeVALUE() {
        Invoice i = new Invoice(-10, DATE, ITEM_TYPE, SELLER, BUYER);
    }

    @Test(expected = TaxException.class)
    public void nullDATE() {
        Invoice i = new Invoice(VALUE, null, ITEM_TYPE, SELLER, BUYER);
    }

    @Test(expected = TaxException.class)
    public void smallerDATE() { //nao pode ser anterior a 1970
        Invoice i = new Invoice(VALUE, new LocalDate(1960, 3, 12), ITEM_TYPE, SELLER, BUYER);
    }

    @Test(expected = TaxException.class)
    public void nullITEM_TYPE() {
        Invoice i = new Invoice(VALUE, DATE, null, SELLER, BUYER);
    }

    @Test(expected = TaxException.class)
    public void nullSELLER() {
        Invoice i = new Invoice(VALUE, DATE, ITEM_TYPE, null, BUYER);
    }

    @Test(expected = TaxException.class)
    public void emptySELLER() {
        Invoice i = new Invoice(VALUE, DATE, ITEM_TYPE, new Seller("     ", "   ", "   "), BUYER);
    }

    @Test(expected = TaxException.class)
    public void nullBUYER() {
        Invoice i = new Invoice(VALUE, DATE, ITEM_TYPE, SELLER, null);
    }

    @Test(expected = TaxException.class)
    public void emptyBUYER() {
        final Invoice i = new Invoice(VALUE, DATE, ITEM_TYPE, SELLER, new Buyer("     ", "   ", "   "));
    }

    @Test(expected = TaxException.class)
    public void duplicateSellerAndBuyer() {
        final Buyer dupBUYER = new Buyer("123456789", "Chico", "Faro");
        final Invoice i = new Invoice(VALUE, DATE, ITEM_TYPE, SELLER, dupBUYER);
    }
    
    @After
	public void tearDown() {
    	 TaxPayer._invoices.clear();
	}
}
