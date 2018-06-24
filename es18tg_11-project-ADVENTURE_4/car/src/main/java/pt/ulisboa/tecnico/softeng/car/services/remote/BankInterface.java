package pt.ulisboa.tecnico.softeng.car.services.remote;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import pt.ulisboa.tecnico.softeng.car.services.remote.exceptions.BankException;
import pt.ulisboa.tecnico.softeng.car.services.remote.exceptions.RemoteAccessException;



public class BankInterface {
	private static Logger logger = LoggerFactory.getLogger(BankInterface.class);

	private static String ENDPOINT = "http://localhost:8082";

	public static String processPayment(String iban, double d) {
		logger.info("processPayment iban:{}, amount:{}", iban, d);

		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.postForObject(ENDPOINT + "/rest/banks/accounts/" + iban
					+ "/processPayment?amount=" + d, null, String.class);
			return result;
		} catch (HttpClientErrorException e) {
			throw new BankException();
		} catch (Exception e) {
			throw new RemoteAccessException();
		}
	}

	public static String cancelPayment(String reference) {
		logger.info("cancelPayment reference:{}", reference);

		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.postForObject(ENDPOINT + "/rest/banks/cancel?reference=" + reference, null,
					String.class);
			return result;
		} catch (HttpClientErrorException e) {
			throw new BankException();
		} catch (Exception e) {
			throw new RemoteAccessException();
		}
	}

}
