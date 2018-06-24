package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityReservationData;

@Controller
@RequestMapping(value = "/providers/{codeProvider}/activities/{codeActivity}/offers/{codeOffer}")
public class ActivityReservationController {
    private static Logger logger = LoggerFactory.getLogger(ActivityReservationController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String reservationForm(Model model, @PathVariable String codeProvider, @PathVariable String codeActivity, @PathVariable String codeOffer) {
        logger.info("offerForm codeProvider:{}, codeActivity:{}, codeOffer:{}", codeProvider, codeActivity, codeOffer);

        ActivityOfferData activityOfferData = ActivityInterface.getActivityOfferDataByCode(codeProvider, codeActivity, codeOffer);

        if (activityOfferData == null) {
            model.addAttribute("error", "Error: it does not exist an activity offer with code" + codeOffer + " in activity with code " + codeActivity
                    + " in provider with code " + codeProvider);
            model.addAttribute("provider", new ActivityProviderData());
            model.addAttribute("providers", ActivityInterface.getProviders());
            return "providers";
        } else {
            model.addAttribute("reservation", new ActivityReservationData());
            model.addAttribute("offer", activityOfferData);
            return "reservations";
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public String reservationSubmit(Model model, @PathVariable String codeProvider, @PathVariable String codeActivity,
                                    @PathVariable String codeOffer, @ModelAttribute ActivityOfferData offer, @ModelAttribute ActivityReservationData reservation) {

        logger.info("reservationSubmit codeProvider:{}, codeActivity:{}, codeOffer:{}, begin:{}, end:{}, price:{}", codeProvider, codeActivity, codeOffer,
                reservation.getBegin(), reservation.getEnd(), reservation.getPrice());

        try {
        	 ActivityInterface.reserveActivity(reservation.getBegin(), reservation.getEnd(),
        			 ActivityInterface.getActivityDataByCode(codeProvider, codeActivity).getMinAge(), 
        			 ActivityInterface.getProviderByCode(codeProvider).getNif(),ActivityInterface.getProviderByCode(codeProvider).getIban());
        	
           
        } catch (ActivityException e) {
            model.addAttribute("error", "Error: it was not possible to create the reservation");
            model.addAttribute("offer" );
            model.addAttribute("activity", ActivityInterface.getActivityDataByCode(codeProvider, codeActivity));
            return "offers";
        }

        return "redirect:/providers/" + codeProvider + "/activities/" + codeActivity + "/offers/" + codeOffer + "reservation/";
    }
}
