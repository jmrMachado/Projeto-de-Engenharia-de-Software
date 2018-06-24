package pt.ulisboa.tecnico.softeng.tax.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class SellerConstructorTest{
	private static final String  nif= "508235674";
	private static final String name = "Carlos Marques";
	private static final String address = "Rua do Alecrim nº32 1º Direito, Lisboa";
	
	@Test
	public void success() {
		Seller seller = new Seller(nif, name, address);
		assertEquals(nif, seller.getNif());
		assertEquals(name, seller.getName());
		assertEquals(address, seller.getAddress());
		assertEquals(1, IRS._contribuintes.size());
	}
	
	 @Test(expected = TaxException.class)
	    public void nullNif() {
	        new Seller(null, name, address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nifLengthNot9() {
	        new Seller("50823567", name, address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nifEmpty() {
	        new Seller("", name, address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nifWhiteSpace() {
	        new Seller("  ", name, address);
	    }
	 
	 @Test
	    public void nifAlreadyExisting() {
	        Seller seller = new Seller(nif, name, address);
	        assertEquals(nif, seller.getNif());
			assertEquals(name, seller.getName());
			assertEquals(address, seller.getAddress());
			assertEquals(1, IRS._contribuintes.size());
			
			try {
				new Seller(nif, "Jorge Silva", "Rua das Cantigas, Porto");
				
			}catch(TaxException e) {
				assertEquals(1, IRS._contribuintes.size());
			}
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nifWithUpperCaseLetters() {
	        new Seller("46AL34258", name, address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nifWithLowerCaseLetters() {
	        new Seller("46al3e258", name, address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nifWithLowerCaseAndUpperCaseLetters() {
	        new Seller("46al3E258", name, address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nullName() {
	        new Seller(nif, null, address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nameEmpty() {
	        new Seller(nif, "", address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nameWhiteSpace() {
	        new Seller(nif, "   ", address);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void nullAddress() {
	        new Seller(nif, name, null);
	    }
	 
	 @Test(expected = TaxException.class)
	    public void addressEmpty() {
	        new Seller(nif, name, "");
	    }
	 
	 @Test(expected = TaxException.class)
	    public void addressWhiteSpace() {
	        new Seller(nif, name, "  ");
	    }
	 
	 @After
		public void tearDown() {
			IRS._contribuintes.clear();
		}
}