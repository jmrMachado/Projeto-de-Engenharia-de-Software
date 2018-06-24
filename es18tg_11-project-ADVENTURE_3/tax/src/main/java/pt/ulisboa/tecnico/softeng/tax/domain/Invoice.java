package pt.ulisboa.tecnico.softeng.tax.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Invoice extends Invoice_Base{
		
	@Override
	public int getCounter() {
		int counter = super.getCounter() + 1;
		setCounter(counter);
		return counter;
	}

	Invoice(double value, LocalDate date, ItemType itemType, Seller seller, Buyer buyer) {
		checkArguments(value, date, itemType, seller, buyer);
		
		setReference(Integer.toString(getCounter()));
		setValue(value);
		setDate(date);
		setItemtype(itemType);
		setSeller(seller);
		setBuyer(buyer);
		setIva(value*itemType.getTax()/100);
		setCancelled(false);
		
		seller.addInvoice(this);
		buyer.addInvoice(this);
		itemType.addInvoice(this);
		
	}

	private void checkArguments(double value, LocalDate date, ItemType itemType, Seller seller, Buyer buyer) {
		if (value <= 0.0f) {
			throw new TaxException();
		}

		if (date == null || date.getYear() < 1970) {
			throw new TaxException();
		}

		if (itemType == null) {
			throw new TaxException();
		}

		if (seller == null) {
			throw new TaxException();
		}

		if (buyer == null) {
			throw new TaxException();
		}
	}

	public void cancel() {
		setCancelled(true);
	}
	
	public void delete() {
		setItemtype(null);
		setBuyer(null);
		setSeller(null);

		deleteDomainObject();
	}


}
