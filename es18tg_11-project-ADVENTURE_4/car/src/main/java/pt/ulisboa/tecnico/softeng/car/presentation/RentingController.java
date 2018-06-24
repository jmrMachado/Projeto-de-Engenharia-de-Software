package pt.ulisboa.tecnico.softeng.car.presentation;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.services.local.RentACarInterface;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.CheckoutData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentData;

@Controller
@RequestMapping(value = "/rentACars/{code}/{plate}")
public class RentingController {
    private static Logger logger = LoggerFactory.getLogger(RentingController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String rentingForm(Model model, @PathVariable String code, @PathVariable String plate) {
        logger.info("rentingForm rentACarCode:{}, vehiclePlate:{}", code, plate);

        RentACarData rentACarData = RentACarInterface.getRentACarDataByCode(code);

        if (rentACarData == null) {
            model.addAttribute("error", "Error: it does not exist a RentACar with the code " + code);
            model.addAttribute("rentACar", new RentACarData());
            model.addAttribute("rentACars", RentACarInterface.getRentACars());
            return "redirect:/rentACars/"+code+"/"+plate;
        }

        model.addAttribute("checkout", new CheckoutData());
        model.addAttribute("rentings", RentACarInterface.getRentings(plate, code));
        model.addAttribute("vehicle", plate);
        model.addAttribute("renting", new RentData());
        model.addAttribute("rentACar", rentACarData);
        return "vehiclesView";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String rentingSubmit(Model model, @ModelAttribute RentData carDto, @PathVariable String code, @PathVariable String plate) {
        logger.info("rentingSubmit drivingLicense:{}, beginDate:{}, endDate:{}, plate:{}", carDto.getDrivingLicense(), carDto.getBegin(), carDto.getEnd(), carDto.getDrivingLicense());

        try {
            RentACarInterface.createRenting(carDto.getDrivingLicense(), new LocalDate(carDto.getBegin()), new LocalDate(carDto.getEnd()), plate, carDto.getBuyerNif(), carDto.getBuyerIban(), code);
        } catch (CarException be) {
            model.addAttribute("error", "Error: it was not possible to create the requested vehicle");
            model.addAttribute("renting", carDto);
            model.addAttribute("rentings", RentACarInterface.getRentings(plate, code));
            model.addAttribute("rentACar", RentACarInterface.getRentACarDataByCode(code));
            return "redirect:/rentACars/"+code;
        }

        return "redirect:/rentACars/"+code+"/"+plate;
    }

    @RequestMapping(method = RequestMethod.POST, value ="/{reference}")
    public String rentingCheckout(Model model, @ModelAttribute CheckoutData carDto, @PathVariable String code, @PathVariable String plate, @PathVariable String reference) {
        logger.info("rentingCheckout kilometers:{}", carDto.getKilometers());

        try {
            RentACarInterface.checkout(plate, code, reference, carDto.getKilometers());
            logger.info("Checkout of {} kilometers Done Successfully ", carDto.getKilometers());
        }catch (CarException e){
            model.addAttribute("error", "Error: it was not possible to checkout the requested renting");
            model.addAttribute("renting", carDto);
            model.addAttribute("rentings", RentACarInterface.getRentings(plate, code));
            model.addAttribute("rentACar", RentACarInterface.getRentACarDataByCode(code));
        }

        return "redirect:/rentACars/"+code+"/"+plate;

    }
}
