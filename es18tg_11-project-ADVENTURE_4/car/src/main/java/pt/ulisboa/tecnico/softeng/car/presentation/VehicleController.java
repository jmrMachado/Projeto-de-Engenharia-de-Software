package pt.ulisboa.tecnico.softeng.car.presentation;

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
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.VehicleData;

@Controller
@RequestMapping(value = "/rentACars/{code}")
public class VehicleController {
    private static Logger logger = LoggerFactory.getLogger(VehicleController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String vehicleForm(Model model, @PathVariable String code) {
        logger.info("vehicleForm rentACarCode:{}", code);

        RentACarData rentACarData = RentACarInterface.getRentACarDataByCode(code);

        if (rentACarData == null) {
            model.addAttribute("error", "Error: it does not exist a RentACar with the code " + code);
            model.addAttribute("rentACar", new RentACarData());
            model.addAttribute("rentACars", RentACarInterface.getRentACars());
            return "redirect:/rentACars/"+code;
        }

        model.addAttribute("vehicle", new VehicleData());
        model.addAttribute("rentACar", rentACarData);
        model.addAttribute("vehicles", RentACarInterface.getVehicles(code));
        return "rentACarView";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String vehicleSubmit(Model model, @ModelAttribute VehicleData carDto, @PathVariable String code) {
        logger.info("vehicleSubmit plate:{}, kilometers:{}, price:{}, type:{}", carDto.getPlate(), carDto.getKilometers(), carDto.getPrice(), carDto.getType());

        try {
            if (carDto.getType().equals("MOTORCYCLE")){
                RentACarInterface.createMotorcycle(code, carDto);
            }else if (carDto.getType().equals("CAR")){
                RentACarInterface.createCar(code, carDto);
            }
        } catch (CarException be) {
            model.addAttribute("error", "Error: it was not possible to create the requested vehicle");
            model.addAttribute("vehicle", carDto);
            model.addAttribute("vehicles", RentACarInterface.getVehicles(code));
            model.addAttribute("rentACar", RentACarInterface.getRentACarDataByCode(code));
            return "redirect:/rentACars/"+code;
        }

        return "redirect:/rentACars/"+code;
    }
}
