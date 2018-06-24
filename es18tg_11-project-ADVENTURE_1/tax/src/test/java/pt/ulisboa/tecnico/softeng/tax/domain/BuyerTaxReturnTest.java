package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class BuyerTaxReturnTest{
	
	private Buyer buyer;
	private Seller seller;
	private InvoiceData id;
	private ItemType it;
	private LocalDate date;
	
	private static final String bnif= "508235674";
	private static final String bnif1= "508235676";
	private static final String bname = "Carlos Marques";
	private static final String baddress = "Rua do Alecrim nº32 1º Direito, Lisboa";
	
	private static final String snif= "508235675";
	private static final String sname = "Carlos Rodriguez";
	private static final String saddress = "Rua do Alecrim nº33 1º Direito, Lisboa";
	private Buyer buyer2;
	
	@Before
	public void setUp() {
		buyer2 = new Buyer(bnif1, bname, baddress);
		this.buyer = new Buyer(bnif, bname, baddress);
		this.seller = new Seller(snif, sname, saddress);
		this.it = new ItemType("item", 10);
		this.date = new LocalDate(2016, 12, 19);
		this.id = new InvoiceData(snif, bnif, "item", 10, date);
		IRS.getInstance().submitInvoice(id);
	}
	
	@Test
	public void success() {
		buyer.taxReturn(2016);
	}

	@Test(expected = TaxException.class)
	public void yearBefore1970() {
		buyer.taxReturn(1960);
	}
	
	@Test(expected = TaxException.class)
	public void wrongBuyer() {
		buyer2.taxReturn(2016);
	}
	
	@Test(expected = TaxException.class)
	public void yearWithNotTax() {
		buyer.taxReturn(1980);
	}
	
	@After
	public void tearDown() {

		IRS._itemTypes.clear();
		IRS._contribuintes.clear();
		TaxPayer._invoices.clear();
	}
}