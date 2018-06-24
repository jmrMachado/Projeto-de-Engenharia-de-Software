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
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.SellerData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.TaxPayerData;

@Controller
@RequestMapping(value = "/taxPayers")
public class TaxPayerController {
	private static Logger logger = LoggerFactory.getLogger(TaxPayerController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String TaxPayerForm(Model model) {
		logger.info("taxPayerForm");
		model.addAttribute("taxPayers", IRSInterface.getTaxPayers());
		return "taxPayers";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String TaxPayerSubmit(Model model, @ModelAttribute TaxPayerData taxPayerData) {
		return "redirect:/taxPayers";
	}
}
