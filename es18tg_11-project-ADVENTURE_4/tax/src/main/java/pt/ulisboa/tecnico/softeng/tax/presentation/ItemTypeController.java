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
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.ItemTypeData;

@Controller
@RequestMapping(value = "/itemTypes")
public class ItemTypeController {
	private static Logger logger = LoggerFactory.getLogger(ItemTypeController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String itemTypeForm(Model model) {
		logger.info("itemTypeForm");
		model.addAttribute("itemType", new ItemTypeData());
		model.addAttribute("itemTypes", IRSInterface.getItemTypes());
		return "itemTypes";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String itemTypeSubmit(Model model, @ModelAttribute ItemTypeData itemTypeData) {
		logger.info("itemTypeSubmit Name:{}, Tax:{}, invoices:{}",
				itemTypeData.getName(), itemTypeData.getTax(), itemTypeData.getList());

		try {
			IRSInterface.createItemType(itemTypeData);
		} catch (TaxException be) {
			model.addAttribute("error", "Error: it was not possible to create the buyer");
			model.addAttribute("itemType", itemTypeData);
			model.addAttribute("itemTypes", IRSInterface.getItemTypes());
			return "itemTypes";
		}

		return "redirect:/itemTypes";
	}
}
