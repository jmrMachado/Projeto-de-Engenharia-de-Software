package pt.ulisboa.tecnico.softeng.tax.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.tax.services.local.IRSInterface;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.InvoiceData;

@Controller
@RequestMapping(value = "/invoices")
public class InvoiceController {
	private static Logger logger = LoggerFactory.getLogger(InvoiceController.class);	
	@RequestMapping(method = RequestMethod.GET)
	public String invoiceForm(Model model) {
		logger.info("invoiceForm");
		model.addAttribute("invoice", new InvoiceData());
		model.addAttribute("invoices", IRSInterface.getInvoices());
		return "invoices";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String invoicesSubmit(Model model, @ModelAttribute(value ="invoiceData")
	InvoiceData invoiceData) {
		logger.info("itemTypeSubmit SellerNIF:{}, BuyerNIF:{}, itemType:{}, value:{}, date:{}",
				invoiceData.getSellerNIF(), invoiceData.getBuyerNIF(), invoiceData.getItemType(), 
				invoiceData.getValue(), invoiceData.getDate());

		try {
			IRSInterface.createInvoice(invoiceData);
		} catch (TaxException be) {
			model.addAttribute("error", "Error: it was not possible to create the invoice");
			model.addAttribute("invoice", invoiceData);
			model.addAttribute("invoices", IRSInterface.getInvoices());
			return "invoices";
		}

		return "redirect:/invoices";
	}
}
