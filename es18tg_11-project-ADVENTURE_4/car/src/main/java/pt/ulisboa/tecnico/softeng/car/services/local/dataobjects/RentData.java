package pt.ulisboa.tecnico.softeng.car.services.local.dataobjects;


public class RentData {
    private String drivingLicense;
    private String begin;
    private String end;
    private String licensePlate;
    private String buyerNif;
    private String buyerIban;

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public void setDrivingLicense(String drivingLicense) {
        this.drivingLicense = drivingLicense;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getBuyerNif() {
        return buyerNif;
    }

    public void setBuyerNif(String buyerNif) {
        this.buyerNif = buyerNif;
    }

    public String getBuyerIban() {
        return buyerIban;
    }

    public void setBuyerIban(String buyerIban) {
        this.buyerIban = buyerIban;
    }
}
