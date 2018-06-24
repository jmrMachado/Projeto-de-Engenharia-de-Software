package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.*;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.InvalidDateException;
import pt.ulisboa.tecnico.softeng.car.exception.RentACarException;
import pt.ulisboa.tecnico.softeng.car.exception.RentingNotFoundException;
import pt.ulisboa.tecnico.softeng.car.dataobjects.*;
import pt.ulisboa.tecnico.softeng.car.exception.VehicleException;


public class RentACar {
    private final String nome;
    private final String code;

    public static Set<RentACar> RentACarCompanies = new HashSet<>();

    private TreeMap<String, Vehicle> veiculos = new TreeMap<>();

    /**Class constructor that receives a name that corresponds to the renting company
     * @param name Name to be given to the renting company
     */
    public RentACar(String name){
        checkConstructorArguments(name);

        this.nome = name.trim();
        this.code = "Renting Company " + String.valueOf(RentACarCompanies.size() + 1);
        RentACarCompanies.add(this);
    }

    /**Method that searches for a Renting object that has a given reference
     * @param reference Reference belonging to a certain renting
     * @return Objeto Renting object corresponding to the given reference
     */
    public Renting getRenting(String reference) {
        if (reference == null){
            throw new RentACarException("Argument reference cannot be null");
        }

        ArrayList<Vehicle> temp = new ArrayList<>(veiculos.values());
        for(Vehicle i : temp){
            if(i.getRent(reference) != null){
                return i.getRent(reference);
            }
        }
        throw new RentingNotFoundException("The specified reference is not associated to any Renting");
    }

    /**Method responsible for presenting all the available cars at the given time period
     * @param begin Beginning date of the reservation period
     * @param end Ending date of the reservation period
     * @return An array containing all the available cars in the given time period
     */
    public ArrayList getAllAvailableCars(LocalDate begin, LocalDate end){
        LocalDate atual = new LocalDate();
        if (begin == null || end == null) {
            throw new RentACarException("The specified dates cannot be null");
        }else if ((begin.toDate()).before(atual.toDate()) || end.toDate().before(atual.toDate())){
            throw new InvalidDateException("The specified begining date cannot be a past date");
        }

        ArrayList<Vehicle> fim = new ArrayList<>();
        ArrayList<Vehicle> temp = new ArrayList<>(veiculos.values());

        for(Vehicle i : temp){
            if(i.isFree(begin, end) && i instanceof Car){
                fim.add(i);
            }
        }
        return fim;
    }

    /**Method responsible for presenting all the available motorcycles at the given time period
     * @param begin Beginning date of the reservation period
     * @param end Ending date of the reservation period
     * @return An array containing all the available motorcycles in the given time period
     */
    public ArrayList getAllAvailableMotorcycles(LocalDate begin, LocalDate end){
        LocalDate atual = new LocalDate();

        if (begin == null || end == null) {
            throw new RentACarException("The specified dates cannot be null");
        }else if ((begin.toDate()).before(atual.toDate())){
            throw new InvalidDateException("The specified begining date cannot be a past date");
        }

        ArrayList<Vehicle> fim = new ArrayList<>();

        ArrayList<Vehicle> temp = new ArrayList<>(veiculos.values());
        for(Vehicle i : temp){
            if(i.isFree(begin, end) && i instanceof Motorcycle){
                fim.add(i);
            }
        }

        return fim;

    }

    /**Method Responsible for returning a RentingData object corresponding to a given reference
     * @param reference Reference to which we want to obtain the information to create the RentingData
     * @return Returns a RentingData object corresponding to the info related with the given reference
     */
    public RentingData getRentingData(String reference) {
        ArrayList<Vehicle> temp = new ArrayList<>(veiculos.values());
        for (Vehicle i : temp) {
            if (i.getRent(reference) != null){
                Renting temp2 = i.getRent((reference));
                return new RentingData(reference, i.getPlate(), temp2.getLicense(), code, temp2.getStart(), temp2.getEnd());
            }
        }
        throw new RentACarException("An error occurred while trying to obtain renting data");
    }

    /**This method is the one responsible for adding a vehicle to the treemap that contains all of the RentACar company
     * vehicles
     * @param veiculo The vehicle to be added
     */
    protected void addVehicle(Vehicle veiculo) {
        if (veiculo == null){
            throw new RentACarException("Null vehicle detected, please check this problem");
        }
        veiculos.put(veiculo.getPlate(), veiculo);
    }

    //###################################Auxiliary Methods##############################################################

    public String getNome() { return nome; }

    public String getCode() { return code; }

    public int getNumberOfCars() { return veiculos.size(); }

    public TreeMap getVehicleDatabase(){
        return veiculos;
    }

    /**Method that checks if the license plate is unique among every car
     * @param plate The plate to be checked
     * @return Returns true if the plate is unique, false otherwise
     */
    protected boolean checkLicensePlate(String plate) {
        for (RentACar i : RentACarCompanies) {
            if (i.getVehicleDatabase().containsKey(plate)) {
                return false;
            }
        }
        return true;
    }

    private void checkConstructorArguments(String nome){
        if (nome == null){
            throw new RentACarException("Argument name of constructor cannot be of type null");
        }
        if (nome.trim().equals("")) {
            throw new RentACarException("Argument name of constructor cannot be empty");
        }
    }

}