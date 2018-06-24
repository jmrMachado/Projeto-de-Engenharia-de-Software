package pt.ulisboa.tecnico.softeng.tax.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;
import pt.ulisboa.tecnico.softeng.tax.services.local.IRSInterface;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.BuyerData;

@Controller
@RequestMapping(value = "/irs")
public class IRSController {
	private static Logger logger = LoggerFactory.getLogger(IRSController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String IRSForm(Model model) {
		logger.info("IRSForm");
		model.addAttribute("irs", IRS.getIRSInstance());
		return "irs";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String IRSSubmit(Model model) {
		/*logger.info("taxPayerSubmit sellerNIF:{}, buyerNIF:{}, itemType:{}, value{}, date:{}",
				invoiceData.getSellerNIF(), invoiceData.getBuyerNIF(), invoiceData.getItemType(), invoiceData.getValue(), 
				invoiceData.getDate().toString());*/

		return "redirect:/taxPayers";
	}
}
