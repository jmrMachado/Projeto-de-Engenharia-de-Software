package pt.ulisboa.tecnico.softeng.tax.domain;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Buyer extends Buyer_Base {
	

	public Buyer(IRS irs, String NIF, String name, String address) {
		super();
		checkArguments(irs, NIF, name, address);
		setNIF(NIF);
		setName(name);
		setAddress(address);
		setPercentage(5);

		irs.addTaxPayer(this);
	}

	public double taxReturn(int year) {
		if (year < 1970) {
			throw new TaxException();
		}

		double result = 0;
		for (Invoice invoice : getInvoiceSet()) {
			if (!invoice.getCancelled() && invoice.getDate().getYear() == year) {
				result = result + invoice.getIva() * getPercentage() / 100;
			}
		}
		return result;
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
	
	public Invoice getInvoiceByReference(String invoiceReference) {
		if (invoiceReference == null || invoiceReference.isEmpty()) {
			throw new TaxException();
		}

		for (Invoice invoice : getInvoiceSet()) {
			if (invoice.getReference().equals(invoiceReference)) {
				return invoice;
			}
		}
		return null;
	}
	
	@Override
	public void delete() {
		setIrs(null);
		
		for (Invoice in : getInvoiceSet()) {
			in.delete();
		}

		deleteDomainObject();
	}
}
