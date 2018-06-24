package pt.ulisboa.tecnico.softeng.tax.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class IRS extends IRS_Base{

	public static IRS getIRS() {
        if (FenixFramework.getDomainRoot().getIrs() == null) {
            FenixFramework.getDomainRoot().setIrs(new IRS());
        }
        return FenixFramework.getDomainRoot().getIrs();
    }

    private IRS() {
    }

	void addTaxPayer(TaxPayer taxPayer) {
		getTaxpayerSet().add(taxPayer);
	}

	public TaxPayer getTaxPayerByNIF(String NIF) {
		for (TaxPayer taxPayer : getTaxpayerSet()) {
			if (taxPayer.getNIF().equals(NIF)) {
				return taxPayer;
			}
		}
		return null;
	}

	void addItemType(ItemType itemType) {
		getItemtypeSet().add(itemType);
	}

	public ItemType getItemTypeByName(String name) {
		for (ItemType itemType : getItemtypeSet()) {
			if (itemType.getName().equals(name)) {
				return itemType;
			}
		}
		return null;
	}

	public static String submitInvoice(InvoiceData invoiceData) {
		IRS irs = getIRS();
		Seller seller = (Seller) irs.getTaxPayerByNIF(invoiceData.getSellerNIF());
		Buyer buyer = (Buyer) irs.getTaxPayerByNIF(invoiceData.getBuyerNIF());
		ItemType itemType = irs.getItemTypeByName(invoiceData.getItemType());
		Invoice invoice = new Invoice(invoiceData.getValue(), invoiceData.getDate(), itemType, seller, buyer);

		return invoice.getReference();
	}

	public void removeItemTypes() {
		getItemtypeSet().clear();;
	}

	public void removeTaxPayers() {
		getTaxpayerSet().clear();
	}

	public void clearAll() {
		removeItemTypes();
		removeTaxPayers();
	}

	public static void cancelInvoice(String reference) {
		if (reference == null || reference.isEmpty()) {
			throw new TaxException();
		}

		Invoice invoice = IRS.getIRS().getInvoiceByReference(reference);

		if (invoice == null) {
			throw new TaxException();
		}

		invoice.cancel();
	}

	private Invoice getInvoiceByReference(String reference) {
		for (TaxPayer taxPayer : getTaxpayerSet()) {
			Invoice invoice = taxPayer.getInvoiceByReference(reference);
			if (invoice != null) {
				return invoice;
			}
		}
		return null;
	}
	
	public void delete() {
		setRoot(null);

		for (TaxPayer tp : getTaxpayerSet()) {
			tp.delete();
		}
		
		for (ItemType it : getItemtypeSet()) {
			it.delete();
		}

		deleteDomainObject();
	}
}
