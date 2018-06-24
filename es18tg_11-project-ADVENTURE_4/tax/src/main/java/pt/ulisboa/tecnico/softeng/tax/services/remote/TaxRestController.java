package pt.ulisboa.tecnico.softeng.tax.services.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.ulisboa.tecnico.softeng.tax.services.local.IRSInterface;
import pt.ulisboa.tecnico.softeng.tax.services.local.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.services.remote.exceptions.TaxException;

@RestController
@RequestMapping(value = "/rest/taxes")
public class TaxRestController {
	private static Logger logger = LoggerFactory.getLogger(TaxRestController.class);

	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	public ResponseEntity<String> submitInvoice(@RequestParam InvoiceData invoiceData) {
		try {
			return new ResponseEntity<>(IRSInterface.submitInvoice(invoiceData),
					HttpStatus.OK);
		} catch (TaxException be) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/cancel", method = RequestMethod.POST)
	public ResponseEntity<String> cancelInvoice(String invoiceReference) {
		logger.info("reserve invoiceReference:{}", invoiceReference);
		try {
			return new ResponseEntity<>(IRSInterface.cancelInvoice(invoiceReference),
					HttpStatus.OK);
		} catch (TaxException be) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
