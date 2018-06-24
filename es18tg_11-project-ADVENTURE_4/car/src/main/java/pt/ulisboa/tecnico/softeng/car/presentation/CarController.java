package pt.ulisboa.tecnico.softeng.car.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.services.local.RentACarInterface;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;


@Controller
@RequestMapping(value = "/rentACars")
public class CarController {
    private static Logger logger = LoggerFactory.getLogger(CarController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String carForm(Model model) {
        logger.info("carForm");
        model.addAttribute("rentACar", new RentACarData());
        model.addAttribute("rentACars", RentACarInterface.getRentACars());
        return "rentACarsView";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String rentACarSubmit(Model model, @ModelAttribute RentACarData carDto) {
        logger.info("rentACarSubmit name:{}, code:{}, nif:{}, iban:{}", carDto.getName(), carDto.getCode(), carDto.getNif(), carDto.getIban());

        try {
            RentACarInterface.createRentACar(carDto);
        } catch (CarException be) {
            model.addAttribute("error", "Error: it was not possible to create the RentACar");
            model.addAttribute("rentACar", carDto);
            model.addAttribute("rentACars", RentACarInterface.getRentACars());
            return "rentACarsView";
        }

        return "redirect:/rentACars";
    }

}
