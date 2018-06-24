package pt.ulisboa.tecnico.softeng.car.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.car.domain.RentACar;

import java.util.List;
import java.util.stream.Collectors;

public class RentACarData {
    private String name;
    private String code;
    private String nif;
    private String iban;
    private List<VehicleData> vehicles;

    public RentACarData(){

    }

    public RentACarData(RentACar rentACar){
        this.name = rentACar.getName();
        this.code = rentACar.getCode();
        this.nif = rentACar.getNif();
        this.iban = rentACar.getIban();

        this.vehicles = rentACar.getVehicleSet().stream().sorted((c1, c2) -> c1.getPlate().compareTo(c2.getPlate()))
                .map(c -> new VehicleData(c)).collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public List<VehicleData> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleData> vehicles) {
        this.vehicles = vehicles;
    }
}
