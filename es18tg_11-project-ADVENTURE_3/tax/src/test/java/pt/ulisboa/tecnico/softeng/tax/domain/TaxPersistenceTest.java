package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import junit.framework.Assert;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class TaxPersistenceTest {
	private static final String SELLER_NIF = "123456789";
	private static final String BUYER_NIF = "987654321";
	private static final String FOOD = "FOOD";
	private static final int VALUE = 16;
	private static final int TAX = 23;
	private final LocalDate date = new LocalDate(2018, 02, 13);

	private Seller seller;
	private Buyer buyer;
	private ItemType itemType;
	private Invoice invoice;
	private IRS irs;
	
	private static final String ADDRESS_SELLER = "Somewhere";
	private static final String NAME_SELLER = "José Vendido";
	
	private static final String NAME_BUYER = "Manuel Comprado";
	private static final String ADDRESS_BUYER = "Anywhere";
	

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		this.irs = IRS.getIRS();
		this.seller = new Seller(irs, SELLER_NIF, NAME_SELLER, ADDRESS_SELLER);
		this.buyer = new Buyer(irs, BUYER_NIF, NAME_BUYER, ADDRESS_BUYER);
		this.itemType = new ItemType(irs, FOOD, TAX);
		this.invoice = new Invoice(VALUE, this.date, this.itemType, this.seller, this.buyer);
		new Invoice(VALUE, this.date, this.itemType, this.seller, this.buyer);
	}

	
	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		//IRS Veryfing
		assertNotNull(this.irs);
		
		List<TaxPayer> tp = new ArrayList<>(this.irs.getTaxpayerSet());
		//Seller veryfing
		TaxPayer tp_buyer = tp.get(0);
		assertEquals(NAME_BUYER, tp_buyer.getName());
		assertEquals(BUYER_NIF, tp_buyer.getNIF());
		assertEquals(ADDRESS_BUYER, tp_buyer.getAddress());
	
		
		//Buyer Veryfing
		TaxPayer tp_seller = tp.get(1);
		assertEquals(NAME_SELLER, tp_seller.getName());
		assertEquals(SELLER_NIF, tp_seller.getNIF());
		assertEquals(ADDRESS_SELLER, tp_seller.getAddress());
		
		
		List<ItemType> itemTypes = new ArrayList<>(this.irs.getItemtypeSet());
		ItemType item = itemTypes.get(0);
		//itemType veryfing
		assertEquals(FOOD,item.getName());
		assertEquals(TAX,item.getTax());
		assertEquals(2,item.getInvoiceCount());
		
		List<Invoice> invoices = new ArrayList<>(item.getInvoiceSet());
		Invoice invoice = invoices.get(0);
		//invoice veryfing
		assertEquals(VALUE,invoice.getValue(),0.1);
		assertEquals(date,invoice.getDate());
		assertEquals(this.itemType,invoice.getItemtype());
		assertEquals(this.seller,invoice.getSeller());
		assertEquals(this.buyer,invoice.getBuyer());
		Assert.assertTrue(!invoice.getCancelled());
		Assert.assertNotNull(invoice.getReference());
		
		assertEquals(2,invoices.size());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		IRS.getIRS().delete();
	}

}
