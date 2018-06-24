package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Buyer extends TaxPayer {

    static final double IVA = 0.05;

    public Buyer(String nif, String name, String address) {
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
    
    //verificar
    public float taxReturn(int year) {
    	checkYear(year);
    	
    	float taxa = 0;
    	
    	for(Invoice i : TaxPayer._invoices) {
    		if(i.getDate().getYear() == year && i.getBuyer().equals(this)) {
    			taxa += i.getItemType().getTax() * IVA * i.getValue();
    		}
    	}
    	if(taxa==0) {
    		throw new TaxException("Erro a retornar o tax.");
    	}
    	else {
    		return taxa;
    	}
    }
    
    private void checkYear(int year) {
    	if(year < 1970) {
    		throw new TaxException("Ano menor que 1970.");
    	}
    }
}
