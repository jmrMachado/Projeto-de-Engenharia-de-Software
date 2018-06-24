package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemTypeConstructorTest{
	private static final String  type = "Eletrodoméstico";
	private static final int tax = 5;
	
	@Test
	public void success() {
		ItemType itemtype = new ItemType(type, tax);
		assertEquals(type, itemtype.getItemType());
		assertEquals(tax, itemtype.getTax());
		IRS.getInstance();
		assertEquals(1, IRS._itemTypes.size());
	}
	
	 @Test(expected = TaxException.class)
	    public void nullType() {
	        new ItemType(null, tax);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void typeEmpty() {
	        new ItemType("", tax);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void typeWhiteSpace() {
	        new ItemType("  ", tax);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void taxLessThanZero() {
	        new ItemType(type, -2);
	    }
	 
	 @Test
	    public void itemTypeAlreadyExisting() {
		 	ItemType itemtype = new ItemType(type, tax);
			assertEquals(type, itemtype.getItemType());
			assertEquals(tax, itemtype.getTax());
			IRS.getInstance();
			assertEquals(1, IRS._itemTypes.size());
			
			try {
				new ItemType(type, tax);
				fail();
			}catch(TaxException e) {
				assertEquals(1, IRS._itemTypes.size());
			}
	    }
	 
	 @After
		public void tearDown() {
			IRS._itemTypes.clear();
		}
}