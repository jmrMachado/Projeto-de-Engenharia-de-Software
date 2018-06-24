package pt.ulisboa.tecnico.softeng.tax.services.local;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Invoice;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;
import pt.ulisboa.tecnico.softeng.tax.domain.TaxPayer;
import pt.ulisboa.tecnico.softeng.tax.presentation.IRSController;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.BuyerData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.ItemTypeData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.SellerData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData;

public class IRSInterface {
	
	@Atomic(mode = TxMode.WRITE)
	public static List<InvoiceData> getInvoices() {
		return /*FenixFramework.getDomainRoot().getIrs()*/IRS.getIRSInstance().getInvoiceSet().stream().map(h -> new InvoiceData(h))
				.collect(Collectors.toList());
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createInvoice(InvoiceData invoiceData) {
		Seller s = (Seller)/*FenixFramework.getDomainRoot().getIrs()*/IRS.getIRSInstance().getTaxPayerByNIF(invoiceData.getSellerNIF());
		Buyer b = (Buyer)/*FenixFramework.getDomainRoot().getIrs()*/IRS.getIRSInstance().getTaxPayerByNIF(invoiceData.getBuyerNIF());
		ItemType it = /*FenixFramework.getDomainRoot().getIrs()*/IRS.getIRSInstance().getItemTypeByName(invoiceData.getItemType());

		new Invoice(invoiceData.getValue(), invoiceData.getDate(), it, s, b);
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createItemType(ItemTypeData itemTypeData) {
		new ItemType(IRS.getIRSInstance(), itemTypeData.getName(), itemTypeData.getTax());
	}
	

	@Atomic(mode = TxMode.READ)
	public static InvoiceData getInvoiceDataByCode(String code) {
		Invoice invoice = getInvoiceByReference(code);

		if (invoice != null) {
			return new InvoiceData(invoice);
		}

		return null;
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createItemType(String invoiceReference, ItemTypeData itemTypeData) {
		new ItemType(/*FenixFramework.getDomainRoot().getIrs()*/ IRS.getIRSInstance(), itemTypeData.getName(), itemTypeData.getTax());
	}

	/*@Atomic(mode = TxMode.WRITE)
	public static void createTaxPayer(TaxPayerData taxPayerData) {
		new TaxPayer(FenixFramework.getDomainRoot().getIrs(), taxPayerData.getNIF(), taxPayerData.getName(), taxPayerData.getAddress());
	}*/
	@Atomic(mode = TxMode.WRITE)
	public static void createBuyer(BuyerData buyerData) {
		new Buyer(IRS.getIRSInstance(), buyerData.getNIF(), buyerData.getName(), buyerData.getAddress());

		//new Buyer(FenixFramework.getDomainRoot().getIrs(), buyerData.getNIF(), buyerData.getName(), buyerData.getAddress());
	}
	@Atomic(mode = TxMode.WRITE)
	public static void createSeller(SellerData sellerData) {
		new Seller(IRS.getIRSInstance(), sellerData.getNIF(), sellerData.getName(), sellerData.getAddress());

		//new Seller(FenixFramework.getDomainRoot().getIrs(), sellerData.getNIF(), sellerData.getName(), sellerData.getAddress());
	}
	
	@Atomic(mode = TxMode.WRITE)
	private static Invoice getInvoiceByReference(String code) {
		return IRS.getIRSInstance().getInvoiceSet().stream().filter(h -> h.getReference().equals(code)).findFirst()
				.orElse(null);
		//return FenixFramework.getDomainRoot().getIrs().getInvoiceSet().stream().filter(h -> h.getReference().equals(code)).findFirst()
			//	.orElse(null);
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static ItemType getItemTypeByName(String name) {
		//ItemType i = FenixFramework.getDomainRoot().getIrs().getItemTypeByName(name);
		ItemType i = IRS.getIRSInstance().getItemTypeByName(name);
		
		if (i == null) {
			return null;
		}
		return i;
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static TaxPayer getTaxPayerByName(String NIF) {
	//	TaxPayer tp = FenixFramework.getDomainRoot().getIrs().getTaxPayerByNIF(NIF);
		TaxPayer tp = IRS.getIRSInstance().getTaxPayerByNIF(NIF);
		
		if (tp == null) {
			return null;
		}
		return tp;
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static String submitInvoice(InvoiceData invoiceData) {
		return IRS.getIRSInstance().submitInvoice(invoiceData);		
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static String cancelInvoice(String invoiceData) {
		try {
			IRS.getIRSInstance().cancelInvoice(invoiceData);
			return "success";
		}
		catch(Exception e) {
			return "fail";
		}
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static List<TaxPayerData> getTaxPayers() {
		List<TaxPayerData> taxPayers = new ArrayList<TaxPayerData>();
		List<BuyerData> buyers = getBuyers();
		List<SellerData> sellers = getSellers();

		for(BuyerData b: buyers) {
			if(b != null) {
				taxPayers.add(b);
			}
		}		
		for(SellerData s: sellers) {
			if(s != null) {
				taxPayers.add(s);
			}
		}	
		return taxPayers;
	}
	
	@Atomic(mode = TxMode.WRITE)
	public  static List<BuyerData> getBuyers() {
		return IRS.getIRSInstance().getTaxPayerSet().stream().map(h -> new BuyerData(h))
				.collect(Collectors.toList());
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static List<SellerData> getSellers() {
		return IRS.getIRSInstance().getTaxPayerSet().stream().map(h -> new SellerData(h))
				.collect(Collectors.toList());
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static List<ItemTypeData> getItemTypes() {
		return IRS.getIRSInstance().getItemTypeSet().stream().map(h -> new ItemTypeData(h))
				.collect(Collectors.toList());
	}
}