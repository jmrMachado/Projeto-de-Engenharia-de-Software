package pt.ulisboa.tecnico.softeng.broker.services.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.TaxException;

public class TaxInterface {
	private static Logger logger = LoggerFactory.getLogger(HotelInterface.class);
	private static String ENDPOINT = "http://localhost:8086";

	public static String submitInvoice(InvoiceData invoiceData) {
		logger.info("submitInvoice invoiceData:{}", invoiceData);
		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.postForObject(ENDPOINT + "/rest/taxes/submit?invoiceData=" + invoiceData, 
					null, String.class);
			return result;
		} catch (HttpClientErrorException e) {
			logger.info("submitInvoice invoiceData:{}", invoiceData);
			throw new TaxException();
		} catch (Exception e) {
			logger.info("submitInvoice invoiceData:{}", invoiceData);
			throw new RemoteAccessException();
		}
	}

	public static void cancelInvoice(String invoiceReference) {
		logger.info("cancelInvoice invoiceReferene:{}", invoiceReference);
		RestTemplate restTemplate = new RestTemplate();
		try {
			restTemplate.postForObject(ENDPOINT + "/rest/taxes/submit?invoiceReference=" + invoiceReference, 
					null, String.class);
		} catch (HttpClientErrorException e) {
			logger.info("cancelInvoice invoiceReferene:{}", invoiceReference);
			throw new TaxException();
		} catch (Exception e) {
			logger.info("cancelInvoice invoiceReferene:{}", invoiceReference);
			throw new RemoteAccessException();
		}
	}
}
