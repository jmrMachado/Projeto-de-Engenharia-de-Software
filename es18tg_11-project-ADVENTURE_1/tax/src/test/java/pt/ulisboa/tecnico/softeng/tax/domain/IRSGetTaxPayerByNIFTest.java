
package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class IRSGetTaxPayerByNIFTest {

    private String TAXPAYER_NAME = "Alice";
    private String TAXPAYER_NIF = "123123123";
    private String OTHERNIF = "123111111";
    private LocalDate DATE = new LocalDate(2018, 2, 12);
    private String TAXPAYER_ADDRESS = "Rua X, Lisboa";


    IRS irs = IRS.getInstance();

    

    @Before
    public void setUp() {
    	TaxPayer person = new Seller(TAXPAYER_NIF, TAXPAYER_NAME, TAXPAYER_ADDRESS);
    
    } 
   
    //9 Nifs , correto
    @Test 
    public void successgetTaxPlayerByNIF() {
        IRS.getInstance().getTaxPayerByNIF(TAXPAYER_NIF);    
    }
    
    
    @Test(expected = TaxException.class)
    public void notNIFExistence() {
        IRS.getInstance().getTaxPayerByNIF(OTHERNIF);    
    }
    
    //Blank nif
    @Test(expected = TaxException.class)
    public void BlankNIF() {
        IRS.getInstance().getTaxPayerByNIF("");  
    }
    // 9 Spaces
    @Test(expected = TaxException.class)
    public void SpacesNIF() {
        IRS.getInstance().getTaxPayerByNIF("         ");  
    }
    // +9 nif
    @Test(expected = TaxException.class)
    public void GreaterThan9NIF() {
        IRS.getInstance().getTaxPayerByNIF("199999999999999");
    }
    // 2 nf
    @Test(expected = TaxException.class)
    public void LessThan9NIF() {
        IRS.getInstance().getTaxPayerByNIF("26");
    }
    // null
    @Test(expected = TaxException.class)
    public void NullNIF() {
        IRS.getInstance().getTaxPayerByNIF(null);    
    }
    // 4 letras not null
    @Test(expected = TaxException.class)
    public void LettersNIF() {
        IRS.getInstance().getTaxPayerByNIF("abcd");   
    }
    // multi not null
    @Test(expected = TaxException.class)
    public void MultiCharsNIF() {
        IRS.getInstance().getTaxPayerByNIF("1a2b3c");   
    }
  
    @Test(expected = TaxException.class)
    public void Letters9NIF() {
        IRS.getInstance().getTaxPayerByNIF("abcdefghi");      
    }
        

    @After
    public void tearDown() {
        IRS._contribuintes.clear();
        
    }

}