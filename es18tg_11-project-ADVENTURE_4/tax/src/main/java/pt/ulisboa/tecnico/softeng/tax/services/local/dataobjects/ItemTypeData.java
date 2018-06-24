package pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects;

import java.util.List;
import java.util.stream.Collectors;

import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;

public class ItemTypeData {
	
		private String name;
		private int tax;
		private IRS irs;
		private List<InvoiceData> invoices;
		
		public ItemTypeData() {
			
		}
		
		public ItemTypeData(ItemType itemType) {
			this.irs = itemType.getIrs();
			this.tax = itemType.getTax();
			this.name = itemType.getName();
			this.invoices = itemType.getInvoiceSet().stream()
					.map(b -> new InvoiceData(b)).collect(Collectors.toList());
		}
		
		public String getName() {
			return name;
		}

		public int getTax() {
			return tax;
		}
		
		public List<InvoiceData> getList() {
			return invoices;
		}
		
		public IRS getIRS() {
			return irs;
		}
		
		public void setName(String n) {
			name = n;
		}

		public void setTax(int t) {
			tax = t;
		}
		
		public void setList(List<InvoiceData> idL) {
			invoices = idL;
		}
}
