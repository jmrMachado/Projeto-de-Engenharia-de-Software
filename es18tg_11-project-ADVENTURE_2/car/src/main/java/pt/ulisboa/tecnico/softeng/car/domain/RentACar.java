package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//empresa de aluguer de veiculos
public class RentACar {
	public static final Set<RentACar> rentACars = new HashSet<>();

	private static int counter;

	private final String name;
	private final String code;
	private final String nif;
	private final String iban;
	private final Map<String, Vehicle> vehicles = new HashMap<>();
	private final ProcessorCar processor = new ProcessorCar();
	
	// nif e iban sao do seller
	public RentACar(String name, String nif, String iban/*, Vehicle vehicle, LocalDate beginOfRent, LocalDate endOfRent*/) {
		checkArguments(name,nif,iban);
		this.name = name;
		this.nif = nif;
		this.iban = iban;
		this.code = Integer.toString(++RentACar.counter);
		
		rentACars.add(this);
	}

	private void checkArguments(String name, String nif, String iban) {
		if (name == null || name.isEmpty() || nif == null
				|| nif.trim().length() == 0 || iban == null || iban.trim().length() == 0) {
			throw new CarException();
		}
	}
	
	public ProcessorCar getProcessor() {
		return this.processor;
	}
	
	/**
	 * @return the nif of the Seller
	 */	
	public String getNif() {
		return nif;
	}

	/**
	 * @return the Iban acc of who rented the car
	 */
	public String getIban() {
		return iban;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	void addVehicle(Vehicle vehicle) {
		this.vehicles.put(vehicle.getPlate(), vehicle);
	}

	public boolean hasVehicle(String plate) {
		return vehicles.containsKey(plate);
	}

	public Set<Vehicle> getAvailableVehicles(Class<?> cls, LocalDate begin, LocalDate end) {
		Set<Vehicle> availableVehicles = new HashSet<>();
		for (Vehicle vehicle : this.vehicles.values()) {
			if (cls == vehicle.getClass() && vehicle.isFree(begin, end)) {
				availableVehicles.add(vehicle);
			}
		}
		return availableVehicles;
	}

	private static Set<Vehicle> getAllAvailableVehicles(Class<?> cls, LocalDate begin, LocalDate end) {
		Set<Vehicle> vehicles = new HashSet<>();
		for (RentACar rentACar : rentACars) {
			vehicles.addAll(rentACar.getAvailableVehicles(cls, begin, end));
		}
		return vehicles;
	}

	public static Set<Vehicle> getAllAvailableMotorcycles(LocalDate begin, LocalDate end) {
		return getAllAvailableVehicles(Motorcycle.class, begin, end);
	}

	public static Set<Vehicle> getAllAvailableCars(LocalDate begin, LocalDate end) {
		return getAllAvailableVehicles(Car.class, begin, end);
	}

	/**
	 * Lookup for a renting using its reference.
	 * 
	 * @param reference
	 * @return the renting with the given reference.
	 */
	public static Renting getRenting(String reference) {
        for (RentACar rentACar : rentACars) {
            for (Vehicle vehicle : rentACar.vehicles.values()) {
                Renting renting = vehicle.getRenting(reference);
                if (renting != null ) {
                    if (renting.getReference().equals(reference) || (renting.isCancelled() && renting.getCancellation().equals(reference))) {
                        return renting;
                    }
                }
            }
        }
        return null;
    }

	public static RentingData getRentingData(String reference) {
		Renting renting = getRenting(reference);
		if (renting == null) {
			throw new CarException();
		}
		return new RentingData(
			renting.getReference(),
			renting.getVehicle().getPlate(),
			renting.getDrivingLicense(),
			renting.getVehicle().getRentACar().getCode(),
			renting.getBegin(),
            renting.getEnd(),
            renting.getVehicle().getPreco(),
            renting.getIBAN(),
            renting.getNIF(),           
            renting.getCancellation(),
            renting.getCancellationDate()            
		);
	}
}
