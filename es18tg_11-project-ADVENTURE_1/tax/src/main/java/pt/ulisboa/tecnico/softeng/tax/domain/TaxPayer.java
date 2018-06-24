package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public abstract class TaxPayer {
	
	public static Set<Invoice> _invoices = new HashSet<>();
    
    private String _nif;
    private String _name;
    private String _address;

    public TaxPayer(String nif, String name, String address) {
    	
        checkTaxPayerArguments(nif, name, address);
        
        this._nif = nif;
        this._name = name;
        this._address = address;
        IRS.getInstance().addTaxPayer(this);
    }

    private void checkTaxPayerArguments(String nif, String name, String address) {
    	
        if (nif == null || nif.trim().equals("")) {
            throw new TaxException("Null NIF ou NIF vazio");
        }
        if (nif.length() != 9) {
            throw new TaxException("The provided NIF doesn't have 9 digits");
        }
        if (IRS.getInstance().checkForNifExistence(nif)) {
            throw new TaxException("This NIF was already used.");
        }
        for (int i = 0; i < nif.length(); i++) {
            if (Character.isLetter(nif.charAt(i))) {
                throw new TaxException("NIF in incorrect format.");
            }
        }
        if (name == null || name.trim().equals("") || address == null || address.trim().equals("")) {
            throw new TaxException("Problems with the provided name and/or address");
        }
    }

    public String getNIF() {
        return this._nif;
    }

    public String getAddress() {
        return this._address;
    }

    public String getName() {
        return this._name;
    }

}

