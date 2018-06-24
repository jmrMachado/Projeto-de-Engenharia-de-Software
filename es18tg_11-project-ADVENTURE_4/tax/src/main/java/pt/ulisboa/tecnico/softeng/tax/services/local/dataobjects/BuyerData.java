package pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.TaxPayer;

public class BuyerData extends TaxPayerData{
	
	public BuyerData() {
	}
	
	public BuyerData(TaxPayer b) {
		if(b instanceof Buyer) {
			NIF = b.getNif();
			name = b.getName();
			address = b.getAddress();
		}
	}

}
