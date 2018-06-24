package pt.ulisboa.tecnico.softeng.car.exception;

public class InvalidDateException extends RuntimeException {
    public InvalidDateException() {
        super();
    }

    public InvalidDateException(String message) {
        super(message);
    }
}