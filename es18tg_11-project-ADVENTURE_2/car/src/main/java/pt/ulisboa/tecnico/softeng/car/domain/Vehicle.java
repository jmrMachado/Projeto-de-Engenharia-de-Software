package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Vehicle {
	private static Logger logger = LoggerFactory.getLogger(Vehicle.class);
	
	private static String plateFormat = "..-..-..";
	static Set<String> plates = new HashSet<>();
	
	private final int _preco;
	private final String plate;
	private int kilometers;
	private final RentACar rentACar;
	private final ItemType _itemType;
	
	public final Map<String, Renting> rentings = new HashMap<>();
	
	
	public Vehicle(String plate, int kilometers, int preco, RentACar rentACar) {
		logger.debug("Vehicle plate: {}", plate);
		checkArguments(plate, kilometers, rentACar);

		this.plate = plate;
		this.kilometers = kilometers;
		this.rentACar = rentACar;
		this._preco = preco;
		this._itemType = new ItemType(IRS.getIRS(), plate, 20);
		
		
		
		plates.add(plate.toUpperCase());
		rentACar.addVehicle(this);
	}

	private void checkArguments(String plate, int kilometers, RentACar rentACar) {
		if (plate == null || !plate.matches(plateFormat) || plates.contains(plate.toUpperCase())) {
			throw new CarException();
		} else if (kilometers < 0) {
			throw new CarException();
		} else if (rentACar == null) {
			throw new CarException();
		}
	}

	/**
	 * @return ItemType associated with Vehicle
	 */
	public ItemType getItemType() {
		return this._itemType;
	}
	
	/**
	 * @return the plate
	 */
	public String getPlate() {
		return this.plate;
	}
	
	/**
	 * @return do preco
	 */
	public int getPreco() {
		return this._preco;
	}

	/**
	 * @return the kilometers
	 */
	public int getKilometers() {
		return this.kilometers;
	}

	/**
	 * @param kilometers
	 *            the kilometers to set
	 */
	public void addKilometers(int kilometers) {
		if (kilometers < 0) {
			throw new CarException();
		}
		this.kilometers += kilometers;
	}

	/**
	 * @return the rentACar
	 */
	public RentACar getRentACar() {
		return this.rentACar;
	}

	public boolean isFree(LocalDate begin, LocalDate end) {
		if (begin == null || end == null) {
			throw new CarException();
		}
		for (Renting renting : this.rentings.values()) {
			if (renting.conflict(begin, end)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Add a <code>Renting</code> object to the vehicle. Use with caution --- no
	 * validation is being made.
	 * 
	 * @param renting
	 */
	protected void addRenting(Renting renting) {
		this.rentings.put(renting.getReference(), renting);
	}

	/**
	 * Lookup for a <code>Renting</code> with the given reference.
	 * 
	 * @param reference
	 * @return Renting with the given reference
	 */
	public Renting getRenting(String reference) {
		return this.rentings.get(reference);
	}
	
	/**
	 * @param drivingLicense
	 * @param begin
	 * @param end
	 * @return
	 */
	//faltam as verificacoes
	public Renting rent(String drivingLicense, String IBAN, String NIF, LocalDate begin, LocalDate end) {
		if (!isFree(begin, end) || checkArgs2(NIF,IBAN)) {
			throw new CarException();
		}		
		Renting renting = new Renting(drivingLicense, begin, end, this, NIF, IBAN);		
		
		this.getRentACar().getProcessor().submitRenting(renting);
		
		return renting;
	}
	
	public String cancelRent(String reference) {		
		Renting renting = RentACar.getRenting(reference);
		if (renting != null) {
			return renting.cancel();
	    }
	    throw new CarException();
	}
	
	public boolean checkArgs2(String nif, String iban) {
		return (nif == null	|| nif.trim().length() == 0 || iban == null || iban.trim().length() == 0);
	}
}

