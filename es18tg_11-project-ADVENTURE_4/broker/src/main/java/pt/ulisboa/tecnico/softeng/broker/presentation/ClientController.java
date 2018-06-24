package pt.ulisboa.tecnico.softeng.broker.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.services.local.BrokerInterface;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BrokerData;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BrokerData.CopyDepth;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.ClientData;

@Controller
@RequestMapping(value = "/brokers/{code}/clients")
public class ClientController {
	
	private static Logger logger = LoggerFactory.getLogger(ClientController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String clientForm(Model model, @PathVariable String code) {
		logger.info("clientForm brokerCode:{}", code);

		BrokerData brokerData = BrokerInterface.getBrokerDataByCode(code, CopyDepth.CLIENTS);

		if (brokerData == null) {
			model.addAttribute("error", "Error: it does not exist a broker with the code " + code);
			model.addAttribute("broker", new BrokerData());
			model.addAttribute("brokers",BrokerInterface.getBrokers());
			return "brokers";
		}

		model.addAttribute("client", new ClientData());
		model.addAttribute("broker", brokerData);
		return "clients";
	}

	@RequestMapping(method = RequestMethod.POST)
	public String clientSubmit(Model model, @PathVariable String code, @ModelAttribute ClientData client) {
		logger.info("clientSubmit bankCode:{}, clientIban:{}, clientNif:{}, clientDrivingLicense:{}, clientAge:{}  ", code, client.getIban(), 
				client.getNif(), client.getDrivingLicense(), client.getAge());

		try {
			BrokerInterface.createClient(code, client);
		} catch (BrokerException be) {
			model.addAttribute("error", "Error: it was not possible to create the client");
			model.addAttribute("client", client);
			model.addAttribute("broker", BrokerInterface.getBrokerDataByCode(code, CopyDepth.CLIENTS));
			return "clients";
		}

		return "redirect:/brokers/" + code + "/clients";
	}
}