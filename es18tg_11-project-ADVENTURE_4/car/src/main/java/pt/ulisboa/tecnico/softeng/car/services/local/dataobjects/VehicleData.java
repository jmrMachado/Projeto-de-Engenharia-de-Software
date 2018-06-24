package pt.ulisboa.tecnico.softeng.car.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.car.domain.Vehicle;

import java.util.List;
import java.util.stream.Collectors;

public class VehicleData {
    private String plate;
    private int kilometers;
    private double price;
    private String type;


    private List<RentingData> rentings;

    public VehicleData(){

    }

    public VehicleData(Vehicle vehicle){
        this.plate = vehicle.getPlate();
        this.kilometers = vehicle.getKilometers();

        this.rentings = vehicle.getRentingSet().stream().sorted((c1, c2) -> c1.getReference().compareTo(c2.getReference()))
                .map(c -> new RentingData(c)).collect(Collectors.toList());
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<RentingData> getRentings() {
        return rentings;
    }

    public void setRentings(List<RentingData> rentings) {
        this.rentings = rentings;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
