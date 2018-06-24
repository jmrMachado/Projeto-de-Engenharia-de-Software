package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;
import java.lang.*;

import pt.ulisboa.tecnico.softeng.car.exception.*;

/**Class that represents a vehicle that is associated to a certain renting company*/

public abstract class Vehicle {

    private final String plate;
    private int kilometers;
    private RentACar company;

    private TreeMap<String, Renting> rentings;  //String is reference of renting

    /**Class constructor that should receive 3 important arguments which are the license plate, the number of kilometers
     * and the company to wich the vehicle is associated
     * @param matricula License plate of the vehicle that should be unique
     * @param kilometros Number of kilometers that the vehicle has made
     * @param empresa Renting company to wich the vehicle is associated
     */
    public Vehicle(String matricula, int kilometros, RentACar empresa){
        checkConstructorArguments(matricula, kilometros, empresa);
        this.company = empresa;
        this.plate = matricula;
        this.kilometers = kilometros;
        this.rentings = new TreeMap<>();
        this.company.addVehicle(this);

    }

    /**Method responsible for checking if the vehicle is free on a given time period
     * @param begin Start of period
     * @param end End of period
     * @return Returns True if the vehicle is available on the requested time, false otherwise
     */
    public boolean isFree(LocalDate begin, LocalDate end) {

        if (end.toDate().before(begin.toDate())){
            throw new CarException();
        }
        ArrayList<Renting> temp = new ArrayList<Renting>(rentings.values());
        int[] res = new int[temp.size()];
        int x = 0;
        for (Renting i : temp){
            if (i.conflict(begin, end)){
                res[x] = 1;
                x++;
            }else{
                res[x] = 0;
                x++;
            }
        }

        for (int i=0; i<temp.size(); i++){
            if (res[i] == 1){
                return false;
            }
        }

        return true;
    }

    /**Given a certain reference this method is responsible for checking if that reference is associated to a certain
     * renting and if there is, returns that renting
     * @param ref Reference of the Renting that the user wants
     * @return If there is a renting associated to the given reference, returns the renting
     */
    public Renting getRent(String ref) {
        if (rentings.containsKey(ref)) {
            return rentings.get(ref);
        }else {
            return null;
        }
    }

    public void updateKilometers(int update){
        if (update < 0){
            throw new VehicleException("Negative kilometers are not valid");
        }
        kilometers += update;
    }

    /**Method that makes the reservation of the vehicle to the given time period
     * @param beginning Begin of the reservation period
     * @param end End of the reservation period
     * @param license The driving license of the person making the reservation
     * @return Returns a Renting object
     */
    public Renting reserve(LocalDate beginning, LocalDate end, String license){
        if (isFree(beginning, end)){
            String generatedReference = generateReference();
            Renting result = new Renting(this, generatedReference, license, beginning, end, this.kilometers);
            rentings.put(generatedReference, result);
            return result;
        }
        throw new VehicleException("The vehicle is not available at the given time period");
    }

    /**Method responsible for canceling a certain Renting
     * @param reference The reference that the renting is associated to
     */
    public void cancelRenting(String reference){
        if (reference == null){
            throw new VehicleException("Reference cannot be null");
        }
        if (rentings.containsKey(reference)){
            rentings.remove(reference);
        }else {
            throw new RentingException("The specified reference is not associated to any Renting");
        }
    }

    //######################################### Auxiliary Methods ######################################################

    /**Auxiliary method to check if the license plate is according to rules
     * @param matricula License plate to be checked
     * @return True if the license plate is according to rules, false otherwise
     */
    private boolean checkPlate(String matricula) {
        if (matricula.length() != 8 || matricula == null || matricula.trim().equals("") || !company.checkLicensePlate(matricula)){
            return false;
        }
        return matricula.matches("^[A-Z0-9]{2}-[A-Z0-9]{2}-[A-Z0-9]{2}");
    }

    /**Auxiliary method to check if given kilometers for the vehicle are valid
     * @param kilometros Number of kilometers to be checked
     * @return True if valid, false otherwise
     */
    private boolean checkKilometers (int kilometros) {
        return kilometros > 0;
    }

    public String getPlate(){ return this.plate; }

    public int getKilometers() {
        return kilometers;
    }

    public RentACar getRentingCompany() {
        return company;
    }

    /**Method that checks if the generated reference is unique amongst all references of the renting company
     * @param reference Reference to be checked
     * @return Returns a Renting object associated to the reference or null if there is not a renting associated
     */
    public Renting checkReference(String reference) {
        try{
            return this.company.getRenting(reference);
        }catch (RentingNotFoundException e){
            return null;
        }
    }

    /**Method that generates a random reference of length 8 and checks if the generated reference is actually unique
     * among all the references of the renting car company
     * @return A string corresponding to the new unique generated reference
     */
    private String generateReference() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 8) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();

        if (checkReference(saltStr) != null){
            generateReference();
        }

        return saltStr;
    }

    private void checkConstructorArguments(String matricula, int kilometros, RentACar empresa){
        if (empresa == null){
            throw new VehicleException("Provided Rent-A-Car company is of type null, please provide a Rent-A-Car company");
        }
        this.company = empresa;
        if (!checkKilometers(kilometros)) {
            throw new VehicleException("A problem occured with the kilometers provided for the vehicle");
        }
        if(!checkPlate(matricula)) {
            throw new VehicleException("A problem occured with the License Plate provided for the vehicle");
        }
    }
}