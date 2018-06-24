package pt.ulisboa.tecnico.softeng.car.exception;

public class VehicleException extends RuntimeException {
    public VehicleException() {
        super();
    }

    public VehicleException(String message) {
        super(message);
    }
}