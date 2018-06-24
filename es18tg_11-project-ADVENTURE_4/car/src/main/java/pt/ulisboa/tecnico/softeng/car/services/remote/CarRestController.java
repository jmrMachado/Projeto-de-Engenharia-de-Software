package pt.ulisboa.tecnico.softeng.car.services.remote;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.services.local.RentACarInterface;

@RestController
@RequestMapping(value = "/rest/rentACars")
public class CarRestController {
	private static Logger logger = LoggerFactory.getLogger(CarRestController.class);
	public static enum Type {
		CAR, MOTORCYCLE
	}

	@RequestMapping(value = "/reserve", method = RequestMethod.POST)
	public ResponseEntity<String> rent(@RequestParam Type type, 
			@RequestParam String drivingLicense, @RequestParam String nif, 
			@RequestParam String iban,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
			@DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam LocalDate end) {
		logger.info("rent type:{}, drivingLicensel:{}, nif:{}, iban:{}, begin:{}, end:{}", type,drivingLicense, nif, iban, begin, end);
		try {
			return new ResponseEntity<>(//RentACarInterface.(type, drivingLicense, nif, iban, begin, end),
					HttpStatus.OK);
		} catch (CarException be) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	

}
