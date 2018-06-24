package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ItemType {
    public static Set<Invoice> _invoices = new HashSet<>();
    
	private String _itemType;
    private int _tax;
    private IRS authority;
    

    public ItemType(String type, int tax){
        this.authority = IRS.getInstance();
        checkArgs(type, tax);
        this._itemType = type;
        this._tax= tax;
        addToIRS();
        
    }

    //############################################## Auxiliary Methods #################################################

    private void checkArgs(String type, int tax){
        if (type == null || type.trim().equals("")){
            throw new TaxException("The specified type can not be empty or null.");
        }
        if (tax < 0){
            throw new TaxException("The provided tax can not be negative.");
        }

        for (ItemType it : IRS._itemTypes.values()) {
            if (it.getTax() == tax && it.getItemType().equals(type)) {
                throw new TaxException("ItemType existente");
            }
        }
    }
    
    private void addToIRS() {
        IRS.getInstance().addItemType(this);
    }
    
    public String getItemType() {
        return this._itemType;
    }

    public int getTax() {
        return this._tax;
    }

}
