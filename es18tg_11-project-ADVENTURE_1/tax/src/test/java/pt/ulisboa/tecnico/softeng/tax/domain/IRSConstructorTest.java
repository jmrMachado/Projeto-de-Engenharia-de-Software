package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class IRSConstructorTest {
	
	@Test
	public void success() {
		IRS.getInstance();
		Assert.assertTrue(IRS.getInstance() != null);
	}
	
}