package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class BuyerConstructorTest{
	private static final String  nif= "508235674";
	private static final String name = "Carlos Marques";
	private static final String address = "Rua do Alecrim nº32 1º Direito, Lisboa";
	
	@Test
	public void success() {
		Buyer buyer = new Buyer(nif, name, address);
		assertEquals(nif, buyer.getNif());
		assertEquals(name, buyer.getName());
		assertEquals(address, buyer.getAddress());
		assertEquals(1, IRS._contribuintes.size());
	}
	
	 @Test(expected = TaxException.class)
	    public void nullNif() {
	        new Buyer(null, name, address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nifLengthNot9() {
	        new Buyer("50823567", name, address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nifEmpty() {
	        new Buyer("", name, address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nifWhiteSpace() {
	        new Buyer("  ", name, address);
	    }
	 
	 @Test
	    public void nifAlreadyExisting() {
	        Buyer buyer = new Buyer(nif, name, address);
	        assertEquals(nif, buyer.getNif());
			assertEquals(name, buyer.getName());
			assertEquals(address, buyer.getAddress());
			assertEquals(1, IRS._contribuintes.size());
			
			try {
				new Buyer(nif, "Jorge Silva", "Rua das Cantigas, Porto");
				fail();
			}catch(TaxException e) {
				assertEquals(1, IRS._contribuintes.size());
			}
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nifWithUpperCaseLetters() {
	        new Buyer("46AL34258", name, address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nifWithLowerCaseLetters() {
	        new Buyer("46al3e258", name, address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nifWithLowerCaseAndUpperCaseLetters() {
	        new Buyer("46al3E258", name, address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nullName() {
	        new Buyer(nif, null, address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nameEmpty() {
	        new Buyer(nif, "", address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nameWhiteSpace() {
	        new Buyer(nif, "   ", address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nullAddress() {
	        new Buyer(nif, name, null);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void addressEmpty() {
	        new Buyer(nif, name, "");
	    }
	 
	 @Test(expected = TaxException.class)
	    public void addressWhiteSpace() {
	        new Buyer(nif, name, "  ");
	    }
	 
	 @After
		public void tearDown() {
			IRS._contribuintes.clear();
		}
}
