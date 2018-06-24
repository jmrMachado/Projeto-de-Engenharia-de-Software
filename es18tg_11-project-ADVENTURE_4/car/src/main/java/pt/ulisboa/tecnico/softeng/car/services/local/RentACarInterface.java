package pt.ulisboa.tecnico.softeng.car.services.local;

import org.joda.time.LocalDate;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.car.domain.*;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentACarData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.services.local.dataobjects.VehicleData;

import java.util.List;
import java.util.stream.Collectors;

public class RentACarInterface {
    @Atomic(mode = Atomic.TxMode.READ)

    public static List<RentACarData> getRentACars() {
        return FenixFramework.getDomainRoot().getRentACarSet().stream()
                .sorted((b1, b2) -> b1.getName().compareTo(b2.getName())).map(b -> new RentACarData(b))
                .collect(Collectors.toList());
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void createRentACar(RentACarData rentACarData) {
        new RentACar(rentACarData.getName(), rentACarData.getNif(), rentACarData.getIban());
    }

    @Atomic(mode = Atomic.TxMode.READ)
    public static RentACarData getRentACarDataByCode(String code) {
        RentACar empresa = getRentACarByCode(code);
        if (empresa == null) {
            return null;
        }

        return new RentACarData(empresa);
    }


    private static RentACar getRentACarByCode(String code) {
        return FenixFramework.getDomainRoot().getRentACarSet().stream().filter(b -> b.getCode().equals(code)).findFirst()
                .orElse(null);
    }

    @Atomic(mode = Atomic.TxMode.READ)
    public static List<VehicleData> getVehicles(String code){
        RentACar temp = getRentACarByCode(code);
        return temp.getVehicleSet().stream()
                .sorted((b1, b2) -> b1.getPlate().compareTo(b2.getPlate())).map(b -> new VehicleData(b))
                .collect(Collectors.toList());
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void createCar(String code, VehicleData vehicle) {
        RentACar empresa = getRentACarByCode(code);
        if (empresa == null) {
            throw new CarException();
        }

        new Car(vehicle.getPlate(),vehicle.getKilometers(),vehicle.getPrice(),empresa);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void createMotorcycle(String code, VehicleData vehicle) {
        RentACar empresa = getRentACarByCode(code);
        if (empresa == null) {
            throw new CarException();
        }

        new Motorcycle(vehicle.getPlate(),vehicle.getKilometers(),vehicle.getPrice(),empresa);
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void createRenting(String drivingLicense, LocalDate begin, LocalDate end, String plate, String buyerNIF, String buyerIBAN, String code){
        RentACar temp = getRentACarByCode(code);
        Vehicle vehicle = null;
        if (temp == null){
            throw new CarException();
        }
        for (Vehicle i : temp.getVehicleSet()){
            if (i.getPlate().equals(plate)){
                vehicle = i;
            }
        }
        if (vehicle == null){
            throw new CarException();
        }
        new Renting(drivingLicense,begin,end,vehicle,buyerNIF, buyerIBAN);
    }

    @Atomic(mode = Atomic.TxMode.READ)
    public static List<RentingData> getRentings(String plate, String code){
        for (VehicleData i : getVehicles(code)){
            if (i.getPlate().equals(plate)){
                return i.getRentings();
            }
        }
        throw new CarException();
    }

    @Atomic(mode = Atomic.TxMode.WRITE)
    public static void checkout(String plate, String code, String reference, int kilometers){
        RentACar temp = getRentACarByCode(code);
        if (temp == null){
            throw new CarException();
        }
        if (!temp.hasVehicle(plate)){
            throw new CarException();
        }
        for (Vehicle i : temp.getVehicleSet()){
            if (i.getPlate().equals(plate)){
                i.getRenting(reference).checkout(kilometers);
            }
        }
    }
}
