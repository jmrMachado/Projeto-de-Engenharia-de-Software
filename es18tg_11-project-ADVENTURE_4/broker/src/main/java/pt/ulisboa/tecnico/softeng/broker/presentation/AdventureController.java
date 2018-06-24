package pt.ulisboa.tecnico.softeng.broker.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.ClientData;
import pt.ulisboa.tecnico.softeng.broker.domain.Broker;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.services.local.BrokerInterface;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.AdventureData;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BrokerData;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BrokerData.CopyDepth;

@Controller
@RequestMapping(value = "/brokers/{brokerCode}/clients/{nif}/adventures")
public class AdventureController {
	private static Logger logger = LoggerFactory.getLogger(AdventureController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String adventureForm(Model model, @PathVariable String brokerCode, @PathVariable String nif) {
		logger.info("adventureForm brokerCode:{}, clientNif:{}", brokerCode, nif);

		ClientData clientData = BrokerInterface.getClientDataByNif(brokerCode, nif);

		if (clientData == null) {
			model.addAttribute("error",
					"Error: it does not exist a client with nif " + nif + " in broker with code " + brokerCode);
			model.addAttribute("broker", new BrokerData());
			model.addAttribute("brokers", BrokerInterface.getBrokers());
			return "brokers";
		} else {
			model.addAttribute("adventure", new AdventureData());
			model.addAttribute("client", clientData);
			return "adventures";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submitAdventure(Model model, @PathVariable String brokerCode, @PathVariable String nif,
			@ModelAttribute AdventureData adventureData) {
		logger.info("adventureSubmit brokerCode:{}, clientNif:{}, begin:{}, end:{}, age:{}, iban:{}, amount:{}", brokerCode, nif,
				adventureData.getBegin(), adventureData.getEnd(), adventureData.getAge(), BrokerInterface.getClientDataByNif(brokerCode, nif).getIban(),
				adventureData.getAmount());
		
		try {
			BrokerInterface.createAdventure(brokerCode, nif, adventureData);
		} catch (BrokerException be) {
			model.addAttribute("error", "Error: it was not possible to create the adventure");
			model.addAttribute("adventure", adventureData);
			model.addAttribute("client", BrokerInterface.getClientDataByNif(brokerCode, nif));
			return "adventures";
		}

		return "redirect:/brokers/" + brokerCode + "/clients/" + nif + "/adventures";
	}

}
