package pt.ulisboa.tecnico.softeng.car.domain;

import java.util.HashSet;
import java.util.Set;
import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;


public class Renting {
	private static String drivingLicenseFormat = "^[a-zA-Z]+\\d+$";
	private static int counter;
	
	private final Set<RentACar> RentToPay = new HashSet<>();

	private final String reference;
	private final String drivingLicense;
	private final LocalDate begin;
	private final LocalDate end;
	private int kilometers = -1;
	private final Vehicle vehicle;
	private String _NIFc;
	private String _IBANc;
	private String _payRef;
	private String _invRef;
	private String cancel = null;
	private LocalDate cancellationDate;
	private String cancelledRentingReference;
	private boolean cancelledInvoice = false;
	
	public Renting(String drivingLicense, LocalDate begin, LocalDate end, Vehicle vehicle, String NIFc, String IBANc) {
		checkArguments(drivingLicense, begin, end, vehicle,NIFc,IBANc);
		this.reference = vehicle.getRentACar().getCode() + Integer.toString(++Renting.counter);
		this.drivingLicense = drivingLicense;
		this.begin = begin;
		this.end = end;
		this.vehicle = vehicle;
		
		this._IBANc = IBANc;
		this._NIFc = NIFc;
		
		this.vehicle.addRenting(this);
	}

	private void checkArguments(String drivingLicense, LocalDate begin, LocalDate end, Vehicle vehicle,String nif, String iban) {
		if (drivingLicense == null || 
				!drivingLicense.matches(drivingLicenseFormat) || 
				begin == null || 
				end == null || 
				vehicle == null
				|| end.isBefore(begin) ||
				(nif == null	|| 
				nif.trim().length() == 0 || 
				iban == null || 
				iban.trim().length() == 0))
			throw new CarException();
	}
	
	public void setPayRef(String payRef) {
		this._payRef = payRef;
	}
	
	public String getCancellation() {
		return this.cancel;
	}
	
	public String getPayRef() {
		return this._payRef;
	}
	
	public void setInvRef(String invRef) {
		this._invRef = invRef;
	}
	
	public String getInvRef() {
		return this._invRef;
	}
	
	/**
	 * @return clients IBAN
	 */
	public String getIBAN() {
		return this._IBANc;
	}

	/**
	 * @return clients NIF
	 */
	public String getNIF() {
		return this._NIFc;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}
	
	/**
	 * @return o numero de kilometros feitos durante a reserva
	 */
	public int getKm() {
		return this.kilometers;
	}

	/**
	 * @return the drivingLicense
	 */
	public String getDrivingLicense() {
		return drivingLicense;
	}

	/**
	 * @return the begin
	 */
	public LocalDate getBegin() {
		return begin;
	}

	/**
	 * @return the end
	 */
	public LocalDate getEnd() {
		return end;
	}

	/**
	 * @return the vehicle
	 */
	public Vehicle getVehicle() {
		return vehicle;
	}
	
	public String getCancelledRentingReference() {
        return this.cancelledRentingReference;
    }

    public void setCancelledRentingReference(String cancelledPaymentReference) {
        this.cancelledRentingReference = cancelledPaymentReference;
    }
    
    public void setCancelledInvoice(boolean cancelledInvoice) {
        this.cancelledInvoice = cancelledInvoice;
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
		} else if ((begin.equals(this.getBegin()) || begin.isAfter(this.getBegin()))
				&& (begin.isBefore(this.getEnd()) || begin.equals(this.getEnd()))) {
			return true;
		} else if ((end.equals(this.getEnd()) || end.isBefore(this.getEnd()))
				&& (end.isAfter(this.getBegin()) || end.isEqual(this.getBegin()))) {
			return true;
        } else return (begin.isBefore(this.getBegin()) && end.isAfter(this.getEnd()));

    }

	/**
	 * Settle this renting and update the kilometers in the vehicle.
	 * 
	 * @param kilometers
	 */
	public void checkout(int kilometers) {
		this.kilometers = kilometers;
		this.vehicle.addKilometers(this.kilometers);
	}
	
	
	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}
	
	/*
	 * Cancels Renting
	 */
	public String cancel() {
        this.cancel = "CANCEL" + this.reference;
        this.cancellationDate = new LocalDate();

        this.getVehicle().getRentACar().getProcessor().submitRenting(this);
        return this.cancel;
    }

    public boolean isCancelled() {
        return this.cancel != null;
    }
	
}
