package pt.ulisboa.tecnico.softeng.broker.services.remote;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

public class ActivityInterface {
	private static Logger logger = LoggerFactory.getLogger(ActivityInterface.class);

	private static String ENDPOINT = "http://localhost:8081";
	
	public static String reserveActivity(LocalDate begin, LocalDate end, int age, String nif, String iban) {
		logger.info("reserveActivity begin:{}, end:{}, age:{}, nif:{},iban:{}", begin, end, age, nif,iban);
		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.postForObject(ENDPOINT + "/rest/providers/reserve?begin=" + begin + "&end="
					+ end + "&age=" + age + "&nif=" + nif+ "&iban=" + iban, null, String.class);
			return result;
		} catch (HttpClientErrorException e) {
			throw new ActivityException();
		} catch (Exception e) {
			throw new RemoteAccessException();
		}
	}

	public static String cancelReservation(String activityConfirmation) {
		logger.info("cancelReservation activityConfirmation:{}", activityConfirmation);
		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.postForObject(
					ENDPOINT + "/rest/providers/cancel?reference=" + activityConfirmation, null, String.class);
			return result;
		} catch (HttpClientErrorException e) {
			throw new ActivityException();
		} catch (Exception e) {
			throw new RemoteAccessException();
		}
	}

	public static ActivityReservationData getActivityReservationData(String reference) {
		logger.info("getActivityReservationData reference:{}", reference);
		RestTemplate restTemplate = new RestTemplate();
		try {
			ActivityReservationData result = restTemplate.getForObject(
					ENDPOINT + "/rest/providers/reservation?reference=" + reference, ActivityReservationData.class);
			return result;
		} catch (HttpClientErrorException e) {
			throw new ActivityException();
		} catch (Exception e) {
			throw new RemoteAccessException();
		}
	}
}


