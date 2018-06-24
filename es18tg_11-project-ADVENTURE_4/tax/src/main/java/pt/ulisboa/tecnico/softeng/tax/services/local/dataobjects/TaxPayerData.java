package pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.TaxPayer;

public abstract class TaxPayerData {
	
	String NIF;
	String name;
	String address;
	
	public TaxPayerData() {
	}
	
	public TaxPayerData(TaxPayer tp) {
		NIF = tp.getNif();
		name = tp.getName();
		address = tp.getAddress();
		
	}
	
	public String getName() {
		return name;
	}
	
	public String getNIF() {
		return NIF;
	}

	public String getAddress() {
		return address;
	}
	
	public void setName(String n) {
		name = n;
	}
	
	public void setNIF(String n) {
		NIF = n;
	}
	
	public void setAddress(String a) {
		address = a;
	}
	

	
}
