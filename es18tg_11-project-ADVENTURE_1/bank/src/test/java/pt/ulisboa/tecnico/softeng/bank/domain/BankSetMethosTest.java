package pt.ulisboa.tecnico.softeng.bank.domain;

import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;

public class BankSetMethosTest {
	private BankOperationData bod;
	
	@Before
	public void setUp() {
		this.bod = new BankOperationData();
	}
	
	@Test
	public void setReferenceTest() {
		this.bod.setReference("reference");
		Assert.assertEquals(this.bod.getReference(), "reference");
	}
	
	@Test
	public void setTypeTest() {
		this.bod.setType("type");
		Assert.assertEquals(this.bod.getType(), "type");
	}
	
	@Test
	public void setIBANTest() {
		this.bod.setIban("MN012");
		Assert.assertEquals(this.bod.getIban(), "MN012");
	}
	
	@Test
	public void setValueTest() {
		this.bod.setValue(2);
		Assert.assertEquals(this.bod.getValue(), 2);
		
	}
	
	@Test
	public void setTimeTest() {
		LocalDateTime ldt = new LocalDateTime();
		this.bod.setTime(ldt);
		Assert.assertEquals(this.bod.getTime(), ldt);
	}
	
}
