package pt.ulisboa.tecnico.softeng.broker.services.remote;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import pt.ulisboa.tecnico.softeng.broker.services.remote.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.CarException;
import pt.ulisboa.tecnico.softeng.broker.services.remote.exception.RemoteAccessException;

public class CarInterface {
	private static Logger logger = LoggerFactory.getLogger(HotelInterface.class);
	private static String ENDPOINT = "http://localhost:8084";
	public static enum Type {
		CAR, MOTORCYCLE
	}

	public static String rentCar(Type vehicleType, String drivingLicense, String nif, String iban, LocalDate begin,
			LocalDate end) {
		logger.info("rentCar drivingLicensel:{}, nif:{}, iban:{}, begin:{}, end:{}", drivingLicense, nif, iban, begin, end);
		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.postForObject(ENDPOINT + "/rest/rentACars/reserve?type=" + vehicleType + "&drivingLicense="
					+ drivingLicense + "&nif=" + nif + "&iban=" + iban + "&begin=" + begin + "&end=" + end,  null, String.class);
			return result;
		} catch (HttpClientErrorException e) {
			logger.info("rentCar drivingLicensel:{}, nif:{}, iban:{}, begin:{}, end:{}", drivingLicense, nif, iban, begin, end);
			throw new CarException();
		} catch (Exception e) {
			logger.info("rentCar drivingLicensel:{}, nif:{}, iban:{}, begin:{}, end:{}", drivingLicense, nif, iban, begin, end);
			throw new RemoteAccessException();
		}
	}

	public static String cancelRenting(String rentingReference) {
		logger.info("cancelRenting rentingReference:{}", rentingReference);
		RestTemplate restTemplate = new RestTemplate();
		try {
			String result = restTemplate.postForObject(ENDPOINT + "/rest/rentACars/cancel?reference=" + rentingReference,
					null, String.class);
			return result;
		} catch (HttpClientErrorException e) {
			throw new CarException();
		} catch (Exception e) {
			throw new RemoteAccessException();
		}
	}

	public static RentingData getRentingData(String reference) {
		logger.info("getRentingData reference:{}", reference);
		RestTemplate restTemplate = new RestTemplate();
		try {
			RentingData result = restTemplate.getForObject(ENDPOINT + "/rest/rentACars/renting?reference=" + reference,
					RentingData.class);
			return result;
		} catch (HttpClientErrorException e) {
			throw new CarException();
		} catch (Exception e) {
			throw new RemoteAccessException();
		}
	}

}
