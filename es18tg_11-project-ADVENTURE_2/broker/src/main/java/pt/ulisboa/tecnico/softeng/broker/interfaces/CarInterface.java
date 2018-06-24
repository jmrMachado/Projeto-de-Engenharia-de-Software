package pt.ulisboa.tecnico.softeng.broker.interfaces;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;
import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

import java.util.ArrayList;


public class CarInterface {

    public static String reserveVehicle(VehicleType tipo, String IBAN, String NIF, String drivingLicense, LocalDate begin, LocalDate end){
        if (tipo == VehicleType.CAR){
            return reserveCar(drivingLicense, begin, end, IBAN, NIF);
        }else  if (tipo == VehicleType.MOTORCYCLE){
            return reserveMotorcycle(drivingLicense, begin, end, IBAN, NIF);
        }
        throw new BrokerException("Problems in CarInterface.");
    }

    private static String reserveMotorcycle(String drivingLicense, LocalDate begin, LocalDate end, String IBAN, String NIF) {
        ArrayList<Vehicle> temp = new ArrayList<>(RentACar.getAllAvailableMotorcycles(begin, end));
        if (temp.isEmpty()) {
            throw new CarException("No Available Motorcycles");
        }
        return temp.get(0).rent(drivingLicense, IBAN, NIF, begin, end).getReference();
    }

    private static String reserveCar(String drivingLicense, LocalDate begin, LocalDate end, String IBAN, String NIF){
        ArrayList<Vehicle> temp = new ArrayList<>(RentACar.getAllAvailableCars(begin, end));
        if (temp.isEmpty()){
            throw new CarException("No Available Cars");
        }
        return temp.get(0).rent(drivingLicense, IBAN, NIF, begin, end).getReference();
    }

    public enum VehicleType {
        CAR, MOTORCYCLE
    }

    public static void CancelRent(String ref){
        if (RentACar.getRenting(ref).getVehicle().rentings.remove(ref) == null){
            throw new CarException("No Renting Associated to given reference");
        }
    }

    public static RentingData getRentingData(String ref){
        return RentACar.getRentingData(ref);
    }

}
