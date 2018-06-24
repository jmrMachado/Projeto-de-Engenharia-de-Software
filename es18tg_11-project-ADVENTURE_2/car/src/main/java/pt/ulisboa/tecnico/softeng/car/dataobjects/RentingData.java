package pt.ulisboa.tecnico.softeng.car.dataobjects;

import org.joda.time.LocalDate;

public class RentingData {
	private String reference;
	private String plate;
	private String drivingLicense;
	private String rentACarCode;
	private LocalDate begin;
	private LocalDate end;
    private double price;
    private String _NIFc;
    private String _IBANc;
    private String _cancel;
    private LocalDate _cancelDate;

	public RentingData() {
	}

	public RentingData(String reference, String plate, String drivingLicense, String rentACarCode, LocalDate begin,
                       LocalDate end, double price, String IBANc, String NIFc, String cancelation, LocalDate canceldate) {
		this.reference = reference;
		this.plate = plate;
		this.drivingLicense = drivingLicense;
		this.rentACarCode = rentACarCode;
		this.begin = begin;
		this.end = end;
        this.price = price * (end.getDayOfYear() - begin.getDayOfYear());
        this._IBANc = IBANc;
        this._NIFc = NIFc;
        this._cancel = cancelation;
        this._cancelDate = canceldate;
      
	}

	public String getCancelation () {
		return this._cancel;
	}
	
	public LocalDate getCancelationDate () {
		return this._cancelDate;
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
	 * @return the renting reference
	 */
	public String getReference() {
		return this.reference;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the vehicle plate
	 */
	public String getPlate() {
		return plate;
	}

	/**
	 * @param plate
	 *            the vehicle plate to set
	 */
	public void setPlate(String plate) {
		this.plate = plate;
	}

	/**
	 * @return the drivingLicense
	 */
	public String getDrivingLicense() {
		return drivingLicense;
	}

	/**
	 * @param drivingLicense
	 *            the drivingLicense to set
	 */
	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	/**
	 * @return the rentACarCode
	 */
	public String getRentACarCode() {
		return rentACarCode;
	}

	/**
	 * @param rentACarCode
	 *            the rentACarCode to set
	 */
	public void setRentACarCode(String rentACarCode) {
		this.rentACarCode = rentACarCode;
	}

	/**
	 * @return the begin
	 */
	public LocalDate getBegin() {
		return begin;
	}

	/**
	 * @param begin
	 *            the begin to set
	 */
	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}

	/**
	 * @return the end
	 */
	public LocalDate getEnd() {
		return end;
	}

	/**
	 * @param end
	 *            the end to set
	 */
	public void setEnd(LocalDate end) {
		this.end = end;
	}

    public double getPrice() {
        return price;
    }
}
