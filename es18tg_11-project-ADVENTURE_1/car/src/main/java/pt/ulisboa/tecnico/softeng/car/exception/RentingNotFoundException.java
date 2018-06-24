package pt.ulisboa.tecnico.softeng.car.exception;

public class RentingNotFoundException extends RuntimeException {
    public RentingNotFoundException() {
        super();
    }

    public RentingNotFoundException(String message) {
        super(message);
    }
}