package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public abstract class TaxPayer extends TaxPayer_Base {

	public TaxPayer(IRS irs, String NIF, String name, String address) {
		checkArguments(irs, NIF, name, address);

		setNIF(NIF);
		setName(name);
		setAddress(address);
	
		irs.addTaxPayer(this);
	}

	protected TaxPayer(){

	}
	
	

	private void checkArguments(IRS irs, String NIF, String name, String address) {
		if (NIF == null || NIF.length() != 9) {
			throw new TaxException();
		}

		if (name == null || name.length() == 0) {
			throw new TaxException();
		}

		if (address == null || address.length() == 0) {
			throw new TaxException();
		}

		if (irs.getTaxPayerByNIF(NIF) != null) {
			throw new TaxException();
		}

	}


	public abstract Invoice getInvoiceByReference(String invoiceReference);
	
	public void delete() {
		setIrs(null);
		
		deleteDomainObject();
	}

}
