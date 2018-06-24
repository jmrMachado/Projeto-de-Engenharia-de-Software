package pt.ulisboa.tecnico.softeng.car.services.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import pt.ulisboa.tecnico.softeng.car.services.remote.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.car.services.remote.exceptions.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.car.services.remote.exceptions.TaxException;


public class TaxInterface {
	private static Logger logger = LoggerFactory.getLogger(TaxInterface.class);
	private static String ENDPOINT = "http://localhost:8086";
	
	public static String submitInvoice(InvoiceData invoiceData) {
		logger.info("TaxInterface invoiceData:{}", invoiceData);
		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.postForObject(ENDPOINT + "/rest/taxes/submit?"
					+ "invoiceData=" + invoiceData , null, String.class);
			return result;
		} catch (HttpClientErrorException e) {
			throw new TaxException();
		} catch (Exception e) {
			throw new RemoteAccessException();
		}
	}

	public static void cancelInvoice(String invoiceReference) {
		logger.info("TaxInterface invoiceData:{}", invoiceReference);
		RestTemplate restTemplate = new RestTemplate();
		try {
			 restTemplate.postForObject(ENDPOINT + "/rest/taxes/cancel?"
					+ "invoiceReference=" + invoiceReference , null, String.class);
		
		} catch (HttpClientErrorException e) {
			throw new TaxException();
		} catch (Exception e) {
			throw new RemoteAccessException();
		}
	}
}
