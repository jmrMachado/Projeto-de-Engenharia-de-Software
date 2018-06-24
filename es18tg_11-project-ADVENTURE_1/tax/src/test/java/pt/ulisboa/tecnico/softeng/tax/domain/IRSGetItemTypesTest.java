
package pt.ulisboa.tecnico.softeng.tax.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class IRSGetItemTypesTest {

    private String choco = "Chocolate";
    private String outro = "iogurte";
    private int valor = 2; 
    private ItemType Chocolate;
   

    @Before
    public void setUp() {
    	this.Chocolate = new ItemType(choco,valor);
    
    } 
        
    @Test 
    public void successgetItemTypeByName() {
        IRS.getInstance().getItemTypeByName(choco);    
    }
    
    @Test (expected = TaxException.class)
    public void BlankItemType() {
        IRS.getInstance().getItemTypeByName("");    
    }
    
    @Test (expected = TaxException.class)
    public void BlankSpacesItemType() {
        IRS.getInstance().getItemTypeByName("  ");    
    }
    
    @Test (expected = TaxException.class)
    public void nullItemType() {
        IRS.getInstance().getItemTypeByName(null);    
    }
    
    @Test (expected = TaxException.class)
    public void WrongItemType() {
        IRS.getInstance().getItemTypeByName("Pastel");    
    }
    
    
    // TESTES getNameByItemType e addItemType //
    
    @Test 
    public void successgetNameByItemType() {
        IRS.getInstance().getNameByItemType(this.Chocolate);    
    }
    
    
    
    @Test (expected = TaxException.class)
    public void notExistsItem() {
    	IRS._itemTypes.clear();
        IRS.getInstance().getNameByItemType(this.Chocolate);    
    }
    
    @Test (expected = TaxException.class)
    public void notEquals() {
    	IRS._itemTypes.clear();
    	ItemType cc = new ItemType ("Bolo",2);
        IRS.getInstance().getNameByItemType(this.Chocolate);    
    }
    
    @Test (expected = TaxException.class)
    public void addItemTypeNull() {
    	IRS.getInstance().addItemType(null);
    }
    
    @Test (expected = TaxException.class)
    public void EqualItemTypesStringAndTax() {
    	ItemType choco2 = new ItemType (outro,7);
    	ItemType choco3 = new ItemType (outro,7);
    }// both equal
    
    @Test 
    public void TaxDifflItemTypes() {
    	ItemType choco2 = new ItemType (outro,7);
    	ItemType choco3 = new ItemType (outro,4);
    }//name equal, tax diff
    
    @Test 
    public void NameDifflItemTypes() {
    	ItemType choco2 = new ItemType (outro,7);
    	ItemType choco3 = new ItemType ("Rissol",7);
    }//name diff, tax equal
    
    @Test 
    public void BothDifflItemTypes() {
    	ItemType choco2 = new ItemType ("Tarte",1);
    	ItemType choco3 = new ItemType ("Castanha",4);
    }//Both diff

    @After
    public void tearDown() {
        IRS._itemTypes.clear();
    }

}