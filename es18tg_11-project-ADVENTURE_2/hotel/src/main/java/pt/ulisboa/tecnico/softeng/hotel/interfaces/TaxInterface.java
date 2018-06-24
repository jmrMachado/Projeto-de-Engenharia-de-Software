package pt.ulisboa.tecnico.softeng.hotel.interfaces;

import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;

public class TaxInterface {
	
	public static String submitInvoice(InvoiceData data){
        return IRS.submitInvoice(data);
    }
	
	public static void cancelInvoice(String invoiceReference) {
		IRS.getIRS().cancelInvoice(invoiceReference);
	}
}
