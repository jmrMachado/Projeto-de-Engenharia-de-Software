package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class SellerFunctionsTest{

	private Seller Jose;
	private Seller seller2;
    private static final String NIF = "111222333";
    private static final String NIF2 = "111222335";
    private static final String NAME = "Jose";
    private static final String NAME2 = "Jorge";
    private static final String ADDRESS = "Lisboa";
    private static final String ADDRESS2 = "Porto";

    private static final float VALUE = 10;
    private static final LocalDate DATE = new LocalDate(2010, 3, 12);
    private static final LocalDate DATE2 = new LocalDate(1960, 3, 12);
    private static final ItemType ITEM_TYPE = new ItemType("Roupa", 2);
    private static final ItemType ITEM_TYPE2 = new ItemType("Chocolates", 2);
    private static final ItemType ITEM_TYPE3 = new ItemType("Sumos", 2);
    private static final Buyer BUYER = new Buyer("123456788", "Alex", "Espanha");

    
    @Test
    public void SellerGetters() {

        Seller Jose = new Seller(NIF, NAME, ADDRESS);

        Assert.assertEquals(Jose.getName(), NAME);
        Assert.assertEquals(Jose.getNif(), NIF);
        Assert.assertEquals(Jose.getAddress(), ADDRESS);
        Assert.assertTrue(IRS._contribuintes.size() == 1);
    }

    @Test
    public void ToPayYearOKTest(){

        this.Jose = new Seller(NIF, NAME, ADDRESS);

        Invoice a = new Invoice(VALUE, DATE, ITEM_TYPE, Jose, BUYER);
        Invoice b = new Invoice(VALUE, DATE, ITEM_TYPE2, Jose, BUYER);
        Invoice c = new Invoice(VALUE, DATE, ITEM_TYPE3, Jose, BUYER);
        TaxPayer._invoices.add(a);
        TaxPayer._invoices.add(b);
        TaxPayer._invoices.add(c);

        float sum = Jose.toPay(DATE.getYear());
	    
        Assert.assertTrue(sum == 9);
    }

    @Test(expected = TaxException.class)
    public void ToPayYearBeforeTest() {

        this.Jose = new Seller(NIF, NAME, ADDRESS);

        Jose.toPay(DATE2.getYear());

     }
    
    @Test(expected = TaxException.class)
	public void wrongSeller() {
    	this.Jose = new Seller(NIF, NAME, ADDRESS);
    	seller2 = new Seller(NIF2, NAME2, ADDRESS2);
		seller2.toPay(2010);
	}
	
	@Test(expected = TaxException.class)
	public void yearWithNoToPay() {
		this.Jose = new Seller(NIF, NAME, ADDRESS);
		Jose.toPay(1980);
	}
	
	@Test
	public void getInvoiceByReferenceOKTest() {
		 this.Jose = new Seller(NIF, NAME, ADDRESS);
		
		 Invoice a = new Invoice(VALUE, DATE, ITEM_TYPE, Jose, BUYER);
		 String reference = a.getReference();
		 Invoice b = new Invoice(VALUE, DATE, ITEM_TYPE2, Jose, BUYER);
		 Invoice c = new Invoice(VALUE, DATE, ITEM_TYPE3, Jose, BUYER);
		 TaxPayer._invoices.add(a);
		 TaxPayer._invoices.add(b);
		 TaxPayer._invoices.add(c);
		
		 Invoice iv = Jose.getInvoiceByReference(reference);
		 Assert.assertNotNull(iv);	

	}
	
	@Test(expected = TaxException.class)
	public void wrongReferenceTest() {
		 this.Jose = new Seller(NIF, NAME, ADDRESS);
			
		 Invoice a = new Invoice(VALUE, DATE, ITEM_TYPE, Jose, BUYER);
		 Invoice b = new Invoice(VALUE, DATE, ITEM_TYPE2, Jose, BUYER);
		 Invoice c = new Invoice(VALUE, DATE, ITEM_TYPE3, Jose, BUYER);
		 TaxPayer._invoices.add(a);
		 TaxPayer._invoices.add(b);
		 TaxPayer._invoices.add(c);
		 Jose.getInvoiceByReference("ola");
		 
	}
	
	@Test(expected = TaxException.class)
	public void ReferenceDoesntExistTest() {
		 this.Jose = new Seller(NIF, NAME, ADDRESS);
			
		 Invoice a = new Invoice(VALUE, DATE, ITEM_TYPE, Jose, BUYER);
		 Invoice b = new Invoice(VALUE, DATE, ITEM_TYPE2, Jose, BUYER);
		 Invoice c = new Invoice(VALUE, DATE, ITEM_TYPE3, Jose, BUYER);
		 TaxPayer._invoices.add(a);
		 TaxPayer._invoices.add(b);
		 TaxPayer._invoices.add(c);
		 Jose.getInvoiceByReference("33");
		 
	}
	
	
	
	
    @After
	public void tearDown() {

		IRS._itemTypes.clear();
		IRS._contribuintes.clear();
		TaxPayer._invoices.clear();
		
	}
}
