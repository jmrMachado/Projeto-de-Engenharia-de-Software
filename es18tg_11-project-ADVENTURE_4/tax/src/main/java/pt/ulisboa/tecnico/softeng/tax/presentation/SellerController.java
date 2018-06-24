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
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.BuyerData;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.SellerData;

@Controller
@RequestMapping(value = "/sellers")
public class SellerController {
private static Logger logger = LoggerFactory.getLogger(SellerController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String SellerForm(Model model) {
		logger.info("sellerForm");
		model.addAttribute("seller", new BuyerData());
		model.addAttribute("sellers", IRSInterface.getSellers());
		return "sellers";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String SellerSubmit(Model model, @ModelAttribute SellerData sellerData) {
		logger.info("sellerSubmit NIF:{}, Name:{}, Address:{}",
				sellerData.getNIF(), sellerData.getName(), sellerData.getAddress());

		try {
			IRSInterface.createSeller(sellerData);
		} catch (TaxException be) {
			model.addAttribute("error", "Error: it was not possible to create the seller");
			model.addAttribute("seller", sellerData);
			model.addAttribute("sellers", IRSInterface.getSellers());
			return "sellers";
		}

		return "redirect:/sellers";
	}
}
