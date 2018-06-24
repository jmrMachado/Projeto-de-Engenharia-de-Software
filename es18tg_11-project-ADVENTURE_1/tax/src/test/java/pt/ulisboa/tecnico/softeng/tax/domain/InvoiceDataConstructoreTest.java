package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class InvoiceDataConstructoreTest {
	
	private static final String bnif= "508235674";
	private static final String snif= "508235675";
	private static final String itemTypeString= "item";
	private LocalDate date = new LocalDate(2016, 12, 19);

	@Before
	public void setUp() {
		this.date = new LocalDate(2016, 12, 19);
	}
	
	@Test
	public void success() {
		InvoiceData id = new InvoiceData(snif, bnif, itemTypeString, 10 , date);
		Assert.assertEquals(snif, id.getSellerNIF());
		Assert.assertEquals(bnif, id.getBuyerNIF());
		Assert.assertEquals(itemTypeString, id.getItemType());
		Assert.assertEquals(date, id.getDate());
		Assert.assertTrue(10 == id.getValue());	
	}
	
	@Test(expected = TaxException.class) 
	public void sellerNifNullTest() {
		InvoiceData id = new InvoiceData(null, bnif, itemTypeString, 10 , date);		
	}
	
	@Test(expected = TaxException.class) 
	public void sellerNifLengthTooSmallTest() {
		InvoiceData id = new InvoiceData("50823567", bnif, itemTypeString, 10 , date);		
	}
	
	@Test(expected = TaxException.class) 
	public void sellerNifLengthTooLongTest() {
		InvoiceData id = new InvoiceData("5082356751", bnif, itemTypeString, 10 , date);		
	}
	
	@Test(expected = TaxException.class) 
	public void buyerNifNullTest() {
		InvoiceData id = new InvoiceData(snif, null, itemTypeString, 10 , date);
	}
	
	@Test(expected = TaxException.class) 
	public void buyerNifLengthTooSmallTest() {
		InvoiceData id = new InvoiceData(snif, "50823567", itemTypeString, 10 , date);		
	}
	
	@Test(expected = TaxException.class) 
	public void buyerNifLengthTooLongTest() {
		InvoiceData id = new InvoiceData(snif, "5082356741", itemTypeString, 10 , date);		
	}
	
	@Test(expected = TaxException.class) 
	public void itemTypeNullTest() {
		InvoiceData id = new InvoiceData(snif, bnif, null, 10 , date);		
	}
	
	@Test(expected = TaxException.class) 
	public void valueLowerThanZeroTest() {
		InvoiceData id = new InvoiceData(snif, bnif, itemTypeString, -1 , date);		
	}
	
	@Test(expected = TaxException.class) 
	public void DateNullTest() {
		InvoiceData id = new InvoiceData(snif, bnif, itemTypeString, 10 , null);		
	}
	
	@Test(expected = TaxException.class) 
	public void DateBefore1970Test() {
		LocalDate dbefore = new LocalDate(1969, 12, 19);
		InvoiceData id = new InvoiceData(snif, bnif, itemTypeString, 10 , dbefore);		
	}
	
	@Test 
	public void DateEqual1970Test() {
		LocalDate dcool = new LocalDate(1970, 12, 19);
		InvoiceData id = new InvoiceData(snif, bnif, itemTypeString, 10 , dcool);	
		Assert.assertEquals(snif, id.getSellerNIF());
		Assert.assertEquals(bnif, id.getBuyerNIF());
		Assert.assertEquals(itemTypeString, id.getItemType());
		Assert.assertEquals(dcool, id.getDate());
		Assert.assertEquals(1970, id.getDate().getYear());
		Assert.assertTrue(10 == id.getValue());	
	}
	
	@Test 
	public void DateAfter1970Test() {
		LocalDate dafter = new LocalDate(1971, 12, 19);
		InvoiceData id = new InvoiceData(snif, bnif, itemTypeString, 10 , dafter);	
		Assert.assertEquals(snif, id.getSellerNIF());
		Assert.assertEquals(bnif, id.getBuyerNIF());
		Assert.assertEquals(itemTypeString, id.getItemType());
		Assert.assertEquals(dafter, id.getDate());
		Assert.assertEquals(1971, id.getDate().getYear());
		Assert.assertTrue(10 == id.getValue());	
	}
	
	@After
	public void tearDown() {	
	}
	
}