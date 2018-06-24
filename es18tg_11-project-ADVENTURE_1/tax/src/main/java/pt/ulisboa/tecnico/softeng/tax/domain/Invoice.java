package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import org.joda.time.LocalDate;

public class Invoice {

    private float _value;
    private LocalDate _date;
    private ItemType _itemType;
    private Seller _seller;
    private Buyer _buyer;
    private String _reference;
	private static int counter = 0;

    public Invoice(float value, LocalDate date, ItemType item_type, Seller seller, Buyer buyer){
	
    	checkArguments(value, date, item_type, seller, buyer);

        this._itemType = item_type;
        this._value = value + this._itemType.getTax() * value;
        this._date = date;
        this._seller = seller;
        this._buyer = buyer;
        
        this._reference = Integer.toString(++Invoice.counter);
    }
	
    private void checkArguments(float value, LocalDate date, ItemType item_type, Seller seller, Buyer buyer) {
    	if(value <= 0) {
            throw new TaxException("Valor nao aceite");
    	}
    	if(date == null || date.getYear() < 1970) {
            throw new TaxException("Data nao aceite");

    	}
    	if(item_type == null) {
            throw new TaxException("TaxPayer nao aceite");

    	}
    	if(seller == null) {
            throw new TaxException("Seller nao aceite");

    	}
    	if(buyer == null) {
            throw new TaxException("Buyer nao aceite");

    	}

    }
    
    public LocalDate getDate() {
    	return this._date;
    }

    public float getValue() {
        return this._value;
    }

    public ItemType getItemType() {
        return this._itemType;
    }

    public Seller getSeller() {
        return this._seller;
    }

    public Buyer getBuyer() {
        return this._buyer;
    }

    public String getReference() {
        return this._reference;
    }

}
