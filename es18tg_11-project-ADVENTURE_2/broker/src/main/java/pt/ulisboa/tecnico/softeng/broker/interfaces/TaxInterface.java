package pt.ulisboa.tecnico.softeng.broker.interfaces;

import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;

public class TaxInterface {

    public static String submitInvoice(InvoiceData data){
        return IRS.submitInvoice(data);
    }

    public static void cancelInvoice(String InvoiceRef) {
        IRS.getIRS().cancelInvoice(InvoiceRef);
    }

    public static ItemType getInvoice(String InvoiceRef) {
        return IRS.getIRS().getItemTypeByName(InvoiceRef);
    }
}
