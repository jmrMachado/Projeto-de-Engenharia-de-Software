package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.*;


public class InterfaceCoverageTest {

    private static final String SELLER_NIF = "123456789";
    private static final String BUYER_NIF = "987654321";
    private static final String FOOD = "FOOD";
    private static final int VALUE = 16;
    private final LocalDate date = new LocalDate(2018, 02, 13);

    private IRS irs;

    @Before
    public void setUp() {
        this.irs = IRS.getIRS();
        new Seller(this.irs, SELLER_NIF, "José Vendido", "Somewhere");
        new Buyer(this.irs, BUYER_NIF, "Manuel Comprado", "Anywhere");
        new ItemType(this.irs, FOOD, VALUE);
    }

    @Test
    public void cancelInvoice(){
        InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, VALUE, this.date);
        String invoiceReference = IRS.submitInvoice(invoiceData);
        Invoice invoice = this.irs.getTaxPayerByNIF(SELLER_NIF).getInvoiceByReference(invoiceReference);
        TaxInterface.cancelInvoice(invoiceReference);

        Assert.assertNull(this.irs.getTaxPayerByNIF(SELLER_NIF).getInvoiceByReference(invoiceReference));
    }

/*    @Test
    public void getInvoice(){
        InvoiceData invoiceData = new InvoiceData(SELLER_NIF, BUYER_NIF, FOOD, VALUE, this.date);
        String invoiceReference = this.irs.submitInvoice(invoiceData);
        Invoice invoice = this.irs.getTaxPayerByNIF(SELLER_NIF).getInvoiceByReference(invoiceReference);
        ItemType fernando = TaxInterface.getInvoice(invoiceReference);

        Assert.assertNotNull(TaxInterface.getInvoice(invoiceReference));
    }*/

    @After
    public void tearDown(){
        IRS.getIRS().clearAll();
    }
}
