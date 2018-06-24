package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Seller extends TaxPayer {
	
    static final double IVA = 0.05;

    public Seller(String nif, String name, String address) {
        super(nif, name, address);
    }

    public String getNif() {
        return super.getNIF();
    }

    public String getName() {
        return super.getName();
    }

    public String getAddress() {
        return super.getAddress();
    }
   
    public float toPay(int year) {
		checkYear(year);
		    	
		float pagar = 0;
		    	
		for(Invoice i : TaxPayer._invoices) {
		    float TAX = i.getItemType().getTax();
		    double VALOR_BASE = i.getValue();

			if(i.getDate().getYear() == year && i.getSeller().equals(this)) {
				pagar += IVA * TAX * VALOR_BASE;
			}
		}
		if(pagar==0) {
    		throw new TaxException("Erro no que tem a pagar.");
    	}
    	else {
    		return pagar;
    	}
    }
    
    private void checkYear(int year) {
    	if(year < 1970) {
    		throw new TaxException("Ano menor que 1970.");
    	}
    }

    public Invoice getInvoiceByReference(String reference) {
    	checkReference(reference);
    	
    	for(Invoice i : TaxPayer._invoices) {
			if(i.getReference().equals(reference)) {
				return i;
			}
		}
    	throw new TaxException("Referencia nao encontrada");
    }
    
    private void checkReference(String reference) {
    	if (!reference.matches("[0-9]+")) {
    		throw new TaxException("Referencia invalida");
    	}
    }
}
