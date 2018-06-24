package pt.ulisboa.tecnico.softeng.car.dataobjects;

import org.joda.time.LocalDate;

public class RentingData {
    private String reference;
    private String plate;
    private String drivingLicense;
    private String rentACarCode;
    private LocalDate begin;
    private LocalDate end;

    public RentingData(){}

    public RentingData(String referencia, String matricula, String carta, String empresa, LocalDate inicio, LocalDate fim) {
        this.reference = referencia;
        this.plate = matricula;
        this.drivingLicense = carta;
        this.rentACarCode = empresa;
        this.begin = inicio;
        this.end = fim;
    }

    public String getReference() { return reference; }

    public String getPlate() { return plate; }

    public String getDrivingLicense() { return drivingLicense; }

    public String getRentACarCode() { return rentACarCode; }

    public LocalDate getBegin() { return begin; }

    public LocalDate getEnd() { return end; }

    public void setReference(String reference) { this.reference = reference; }

    public void setPlate(String plate) { this.plate = plate; }

    public void setDrivingLicense(String drivingLicense) { this.drivingLicense = drivingLicense; }

    public void setRentACarCode(String rentACarCode) { this.rentACarCode = rentACarCode; }

    public void setBegin(LocalDate begin) { this.begin = begin; }

    public void setEnd(LocalDate end) { this.end = end; }

}
