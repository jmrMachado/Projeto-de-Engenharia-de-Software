package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;

public class ReserveActivityState extends AdventureState {
	public static final int MAX_REMOTE_ERRORS = 5;

	@Override
	public State getState() {
		return State.RESERVE_ACTIVITY;
	}

	@Override
	public void process(Adventure adventure) {
		try {
			adventure.setActivityConfirmation(
                    ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(), adventure.getAge(), adventure.getBroker().getBuyerNIF(), adventure.getBroker().getIBAN()));
            adventure.addPrice(ActivityInterface.getActivityReservationData(adventure.getActivityConfirmation()).getAmount());
		} catch (ActivityException ae) {
			adventure.setState(State.UNDO);
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				adventure.setState(State.UNDO);
			}
			return;
		}

        if (adventure.getBegin().equals(adventure.getEnd()) && !adventure.isCarRent()) {
            adventure.setState(State.PROCESS_PAYMENT);
        } else if (adventure.getBegin().equals(adventure.getEnd()) && adventure.isCarRent()) {
            adventure.setState(State.RENT_VEHICLE);
		} else {
			adventure.setState(State.BOOK_ROOM);
		}
	}

}
