package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class RentVehicleState extends AdventureState{
    public static final int MAX_REMOTE_ERRORS = 10;

    @Override
    public State getState() {
        return State.RENT_VEHICLE;
    }

    @Override
    public void process(Adventure adventure){
        try {
            adventure.setVehicleConfirmation(
                    CarInterface.reserveVehicle(CarInterface.VehicleType.CAR, adventure.getBroker().getIBAN(), adventure.getBroker().getBuyerNIF(), adventure.getLicense(), adventure.getBegin(), adventure.getEnd()));
            adventure.addPrice(CarInterface.getRentingData(adventure.getVehicleConfirmation()).getPrice());
        } catch (CarException e){
            adventure.setState(State.UNDO);
            return;
        } catch (RemoteAccessException rae){
            incNumOfRemoteErrors();
            if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
                adventure.setState(State.UNDO);
            }
            return;
        }

        adventure.setState(State.PROCESS_PAYMENT);
    }

}
