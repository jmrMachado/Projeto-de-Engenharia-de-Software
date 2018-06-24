package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.car.exception.CarException;

public class Renting extends Renting_Base{
	private static String drivingLicenseFormat = "^[a-zA-Z]+\\d+$";

	public Renting(String drivingLicense, LocalDate begin, LocalDate end, Vehicle vehicle, String buyerNIF,
			String buyerIBAN) {
		checkArguments(drivingLicense, begin, end, vehicle);

		setReference(Integer.toString(vehicle.getRentACar().getCounter()));
		setDrivingLicense(drivingLicense);
		setBegin(begin);
		setEnd(end);
		setVehicle(vehicle);
		setClientNIF(buyerNIF);
		setClientIBAN(buyerIBAN);
		setPrice(vehicle.getPrice() * (end.getDayOfYear() - begin.getDayOfYear()));
		setType("RENTAL");

		vehicle.addRentings(this);
	}

    public void delete() {
        setVehicle(null);
        setProcessor(null);
        deleteDomainObject();
    }

	private void checkArguments(String drivingLicense, LocalDate begin, LocalDate end, Vehicle vehicle) {
		if (drivingLicense == null || !drivingLicense.matches(drivingLicenseFormat) || begin == null || end == null
				|| vehicle == null || end.isBefore(begin)) {
			throw new CarException();
		}
	}

	public boolean isCancelled() {
		return (getCancellationReference() != null && getCancellationDate() != null);
	}

	/**
	 * @param begin
	 * @param end
	 * @return <code>true</code> if this Renting conflicts with the given date
	 *         range.
	 */
	public boolean conflict(LocalDate begin, LocalDate end) {
		if (end.isBefore(begin)) {
			throw new CarException("Error: end date is before begin date.");
		} else if ((begin.equals(getBegin()) || begin.isAfter(getBegin()))
				&& (begin.isBefore(getEnd()) || begin.equals(getEnd()))) {
			return true;
		} else if ((end.equals(getEnd()) || end.isBefore(getEnd()))
				&& (end.isAfter(getBegin()) || end.isEqual(getBegin()))) {
			return true;
		} else if (begin.isBefore(getBegin()) && end.isAfter(getEnd())) {
			return true;
		}

		return false;
	}

	/**
	 * Settle this renting and update the kilometers in the vehicle.
	 * 
	 * @param kilometers
	 */
	public void checkout(int kilometers) {
		setKilometers(kilometers);
		getVehicle().addKilometers(getKilometers());
	}

	public String cancel() {
		setCancellationReference(getReference() + "CANCEL");
		setCancellationDate(LocalDate.now());

		getVehicle().getRentACar().getProcessor().submitRenting(this);

		return getCancellationReference();
	}

}
