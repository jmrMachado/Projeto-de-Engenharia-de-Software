package pt.ulisboa.tecnico.softeng.car.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class Motorcycle extends Motorcycle_Base {

	private static String plateFormat = "..-..-..";

	public Motorcycle(String plate, int kilometers, double price, RentACar rentACar) {
		checkArguments(plate, kilometers, rentACar);
		setPlate(plate);
		setKilometers(kilometers);
		setPrice(price);
		setRentACar(rentACar);
		rentACar.addVehicles(this);
	}

	private void checkArguments(String plate, int kilometers, RentACar rentACar) {
		for (RentACar i : FenixFramework.getDomainRoot().getRentACarSet()){
			for (Vehicle x : i.getVehiclesSet()){
				if (x.getPlate().equals(plate))
					throw new CarException();
			}
		}
		if (plate == null || !plate.matches(plateFormat)) {
			throw new CarException();
		} else if (kilometers < 0) {
			throw new CarException();
		} else if (rentACar == null) {
			throw new CarException();
		}
	}
}
