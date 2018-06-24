package pt.ulisboa.tecnico.softeng.car.exception;

public class RentACarException extends RuntimeException {
    public RentACarException() {
        super();
    }

    public RentACarException(String message) {
        super(message);
    }
}