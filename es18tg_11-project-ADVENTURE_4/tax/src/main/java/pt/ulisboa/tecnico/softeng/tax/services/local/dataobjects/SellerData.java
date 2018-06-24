package pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.domain.TaxPayer;

public class SellerData extends TaxPayerData{
	
	public SellerData() {
	}
	
	public SellerData(TaxPayer s) {
		if(s instanceof Seller) {
			NIF = s.getNif();
			name = s.getName();
			address = s.getAddress();
		}
	}
}
