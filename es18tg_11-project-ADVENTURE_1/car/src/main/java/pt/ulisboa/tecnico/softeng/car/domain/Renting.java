package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.exception.RentingException;
import java.util.Date;


public class Renting {

    private String ref;
    private String license;
    private LocalDate start;
    private LocalDate end;
    private int kilom;
    private Vehicle car;

    /**Class constructor that has to receive a unique reference, a driving license, two dates corresponding to the beginning
     * and end of the reservation period and the kilometers the vehicle did during the renting period
     * @param reference Unique reference that identifies a Renting
     * @param drivingLicense Drivers license of the person requesting the vehicle
     * @param begin Begin of reservation period
     * @param end End of reservation period
     * @param kms Kilometers that the vehicle did during the renting time
     */
    public Renting(Vehicle veiculo,String reference, String drivingLicense, LocalDate begin, LocalDate end, int kms) {
        if (veiculo != null){
            this.car = veiculo;
        }else{
            throw new RentingException("The vehicle provided cannot be null");
        }
        if(this.car.checkReference(reference) == null && reference.trim().length() != 0 && !reference.trim().equals("")){
            this.ref = reference;
        }else{
            throw new RentingException("The reference provided is already associated to another renting or is invalid");
        }
        if (checkLicense(drivingLicense)){
            this.license = drivingLicense;
        }else {
            throw new RentingException("The driving license must contain one digit at the beginning and has to be followed by letters only");
        }
        if (begin == null || end == null || begin.toDate().before(new Date()) || end.toDate().before(new Date())){
            throw new RentingException("Dates cannot be null");
        }else {
            this.start = begin;
            this.end = end;
        }
        if (kms < 0){
            throw new RentingException("Provided number of kilometers cannot be null");
        }else{
            this.kilom = kms;
        }
    }

    /**This method is responsible for checking if there are any reservations in the given time period
     * @param begin Beginning of reservation period
     * @param ending End of reservation period
     * @return Returns True if there are conflicts, false otherwise
     */
    public boolean conflict(LocalDate begin, LocalDate ending) {

        Date temp1 = begin.toDate();
        Date temp2 = ending.toDate();

        return temp1.after(start.toDate()) && temp1.before(end.toDate()) || temp2.after(start.toDate()) && temp2.before(end.toDate()) ||
                temp1.before(start.toDate()) && temp2.after(end.toDate()) || temp1.equals(start.toDate()) || temp1.equals(end.toDate()) ||
                temp2.equals(start.toDate()) || temp2.equals(end.toDate());

    }

    /**Method that adds kilometers to the total amount of kilometers made.
     * @param kilometers Kilometers that the vehicle did to be added to the total amount
     */
    public void checkout(int kilometers) {
        car.updateKilometers(kilometers);
    }

    //###############################################Metodos Auxiliares################################################

    public String getLicense() { return license; }

    public LocalDate getStart() { return start; }

    public LocalDate getEnd() { return end; }

    public String getRef() { return ref; }

    private boolean checkLicense(String carta){
        if (carta == null){
            throw new RentingException("Drivers License cannot be null");
        }
        if (carta.trim().equals("")){
            throw new RentingException("Empty license!");
        }
        if (!Character.isDigit(carta.charAt(0))){
            throw new RentingException("Invalid license syntax");
        }
        for (int i = 1; i<carta.length(); i++){
            if (!Character.isLetter(carta.charAt(i))){
                return false;
            }
        }
        return true;
    }

}