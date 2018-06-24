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

@Controller
@RequestMapping(value = "/buyers")
public class BuyerController {
private static Logger logger = LoggerFactory.getLogger(BuyerController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String BuyerForm(Model model) {
		logger.info("buyerForm");
		model.addAttribute("buyer", new BuyerData());
		model.addAttribute("buyers", IRSInterface.getBuyers());
		return "buyers";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String BuyerSubmit(Model model, @ModelAttribute BuyerData buyerData) {
		logger.info("buyerSubmit NIF:{}, Name:{}, Address:{}",
				buyerData.getNIF(), buyerData.getName(), buyerData.getAddress());

		try {
			IRSInterface.createBuyer(buyerData);
		} catch (TaxException be) {
			model.addAttribute("error", "Error: it was not possible to create the buyer");
			model.addAttribute("buyer", buyerData);
			model.addAttribute("buyers", IRSInterface.getBuyers());
			return "buyers";
		}

		return "redirect:/buyers";
	}
}
