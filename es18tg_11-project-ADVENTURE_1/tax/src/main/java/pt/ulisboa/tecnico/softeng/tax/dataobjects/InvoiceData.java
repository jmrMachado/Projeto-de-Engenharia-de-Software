package pt.ulisboa.tecnico.softeng.tax.dataobjects;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class InvoiceData {

    static final int NIF_SIZE = 9;

    private String sellerNIF;
    private String buyerNIF;
    private String itemType;
    private float value;
    private LocalDate date;

    public InvoiceData(String sellerNIF, String buyerNIF, String itemType, float value, LocalDate date) {

        checkArguments(sellerNIF, buyerNIF, itemType, value, date);

        this.sellerNIF = sellerNIF;
        this.buyerNIF = buyerNIF;
        this.itemType = itemType;
        this.value = value;
        this.date = date;
    }

    private void checkArguments(String sellerNIF, String buyerNIF, String itemType, float value, LocalDate date) {

        if (sellerNIF == null || buyerNIF == null || itemType == null || value < 0 || date == null) {
            throw new TaxException("Input must not be null.");
        }

        if (sellerNIF.length() != InvoiceData.NIF_SIZE || buyerNIF.length() != InvoiceData.NIF_SIZE) {
            throw new TaxException("NIF must have 9 digits.");
        }

        if (date.getYear() < 1970) {
            throw new TaxException("Date can not be older than 1970.");
        }
    }
    
    public String getSellerNIF() {
    	return this.sellerNIF;
    }
    
    public String getBuyerNIF() {
    	return this.buyerNIF;
    }
    
    public String getItemType() {
    	return this.itemType;
    }
    
    public float getValue() {
    	return this.value;
    }
    
    public LocalDate getDate() {
    	return this.date;
    }

}
