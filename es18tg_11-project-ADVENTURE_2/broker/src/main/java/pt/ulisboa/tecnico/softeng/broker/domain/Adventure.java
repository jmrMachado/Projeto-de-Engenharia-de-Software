package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Adventure {
	private static Logger logger = LoggerFactory.getLogger(Adventure.class);

    public enum State {
		PROCESS_PAYMENT, RESERVE_ACTIVITY, BOOK_ROOM, RENT_VEHICLE, UNDO, CONFIRMED, CANCELLED
	}

	private static int counter = 0;

    private final Client pessoa;
    private final String license;
	private final String ID;
	private final Broker broker;
	private final LocalDate begin;
	private final LocalDate end;
	private final int age;
	private final String IBAN;
	private final double margin;
	private String paymentConfirmation;
	private String paymentCancellation;
	private String roomConfirmation;
	private String roomCancellation;
	private String activityConfirmation;
	private String activityCancellation;
	private String vehicleConfirmation;
	private String vehicleCancellation;
    private boolean carRent;
    private double finalprice;

	private AdventureState state;

    public Adventure(Client pessoa, LocalDate begin, LocalDate end, double margin, boolean aluguerCarro) {
		checkArguments(pessoa, begin, end, margin, aluguerCarro);

		this.ID = pessoa.getBROKER().getCode() + Integer.toString(++counter);
		this.broker = pessoa.getBROKER();
		this.begin = begin;
		this.end = end;
		this.age = pessoa.getAGE();
		this.IBAN = pessoa.getIBAN();
		this.margin = margin;
		this.pessoa = pessoa;
		this.license = pessoa.getLICENSE();
		this.carRent = aluguerCarro;
        this.finalprice = 0;

		broker.addAdventure(this);

        setState(State.RESERVE_ACTIVITY);
	}

	private void checkArguments(Client pessoa, LocalDate begin, LocalDate end, double margin, boolean aluguerCarro) {
		if (pessoa == null){
			throw new BrokerException();
		}
		if (pessoa.getBROKER() == null || begin == null || end == null || pessoa.getIBAN() == null || pessoa.getIBAN().trim().length() == 0) {
			throw new BrokerException();
		}

		if (end.isBefore(begin)) {
			throw new BrokerException();
		}

		if (pessoa.getAGE() < 18 || pessoa.getAGE() > 100) {
			throw new BrokerException();
		}

		if (margin <= 0) {
			throw new BrokerException();
		}
	}

    public Client getPessoa() {
        return pessoa;
    }

    public boolean isCarRent() {
        return carRent;
    }

    public String getLicense() {
        return license;
    }

	public String getID() {
		return this.ID;
	}

	public Broker getBroker() {
		return this.broker;
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}

	public int getAge() {
		return this.age;
	}

	public String getIBAN() {
		return this.IBAN;
	}

    public double getMargin() {
		return this.margin;
	}

	public String getPaymentConfirmation() {
		return this.paymentConfirmation;
	}

	public void setPaymentConfirmation(String paymentConfirmation) {
		this.paymentConfirmation = paymentConfirmation;
	}

	public String getPaymentCancellation() {
		return this.paymentCancellation;
	}

	public void setPaymentCancellation(String paymentCancellation) {
		this.paymentCancellation = paymentCancellation;
	}

	public String getActivityConfirmation() {
		return this.activityConfirmation;
	}

	public void setActivityConfirmation(String activityConfirmation) {
		this.activityConfirmation = activityConfirmation;
	}

	public String getActivityCancellation() {
		return this.activityCancellation;
	}

	public void setActivityCancellation(String activityCancellation) {
		this.activityCancellation = activityCancellation;
	}

	public String getRoomConfirmation() {
		return this.roomConfirmation;
	}

	public void setRoomConfirmation(String roomConfirmation) {
		this.roomConfirmation = roomConfirmation;
	}

	public String getRoomCancellation() {
		return this.roomCancellation;
	}

	public void setRoomCancellation(String roomCancellation) {
		this.roomCancellation = roomCancellation;
	}

	public void setVehicleConfirmation(String vehicleConfirmation){
	    this.vehicleConfirmation = vehicleConfirmation;
    }

    public String getVehicleConfirmation(){
	    return this.vehicleConfirmation;
    }

    public void setVehicleCancellation(String vehicleCancellation){
	    this.vehicleCancellation = vehicleCancellation;
    }

    public String getVehicleCancellation(){
	    return vehicleCancellation;
    }

	public State getState() {
		return this.state.getState();
	}

    public void addPrice(double amount) {
        finalprice += amount;
    }

    public double getFinalprice() {
        return finalprice * (1 + getMargin());
    }

	public void setState(State state) {
		switch (state) {
		case PROCESS_PAYMENT:
			this.state = new ProcessPaymentState();
			break;
		case RESERVE_ACTIVITY:
			this.state = new ReserveActivityState();
			break;
		case BOOK_ROOM:
			this.state = new BookRoomState();
			break;
		case RENT_VEHICLE:
		    this.state = new RentVehicleState();
		    break;
		case UNDO:
			this.state = new UndoState();
			break;
		case CONFIRMED:
			this.state = new ConfirmedState();
			break;
		case CANCELLED:
			this.state = new CancelledState();
			break;
		default:
			new BrokerException();
			break;
		}
	}

	public void process() {
		this.state.process(this);
	}

	public boolean cancelRoom() {
		return getRoomConfirmation() != null && getRoomCancellation() == null;
	}

	public boolean cancelActivity() {
		return getActivityConfirmation() != null && getActivityCancellation() == null;
	}

	public boolean cancelPayment() {
		return getPaymentConfirmation() != null && getPaymentCancellation() == null;
	}

	public boolean cancelRent(){
	    return getVehicleConfirmation() != null && getVehicleCancellation() == null;
    }

}
