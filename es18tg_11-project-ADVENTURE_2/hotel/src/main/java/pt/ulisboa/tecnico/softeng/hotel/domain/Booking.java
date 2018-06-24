package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class Booking {
	private static int counter = 0;
	
	private final Set<Booking> bookingToCancelPay = new HashSet<>();

	private final String reference;
	private String cancellation;
	private LocalDate cancellationDate;
	private final LocalDate arrival;
	private final LocalDate departure;
	private final double _amount;
	private final String IBAN;
	private final String NIF;
	private final String roomID;
	private String paymentReference;
	private String invoiceReference;
	private Hotel _hotel;
	private String cancel;
	private String cancelledBookingReference;
	private boolean cancelledInvoice = false;


	Booking(Hotel hotel, LocalDate arrival, LocalDate departure, double amount, String IBAN, String NIF, String roomID) {
		checkArguments(hotel, arrival, departure, amount, IBAN, NIF, roomID);

		this.reference = hotel.getCode() + Integer.toString(++Booking.counter);
		this.arrival = arrival;
		this.departure = departure;
		this._amount = amount;
		this.IBAN = IBAN;
		this.NIF = NIF;
		this.roomID = roomID;
		this._hotel = hotel;
	}

	private void checkArguments(Hotel hotel, LocalDate arrival, LocalDate departure, double amount, String IBAN, String NIF, String roomID) {
		if (hotel == null || arrival == null || departure == null || amount < 0 || IBAN==null || NIF==null || roomID==null || IBAN.trim().length()==0 || NIF.trim().length()==0 || roomID.trim().length()==0) {
			throw new HotelException();
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}
	}
	
	public Hotel getHotel() {
		return this._hotel;
	}
	
	public String getRoomID() {
		return this.roomID;
	}
	
	public String getInvoiceReference() {
		return this.invoiceReference;
	}
	
	public String getPaymentReference() {
		return this.paymentReference;
	}
	
	public void setInvoiceReference(String ref) {
		this.invoiceReference = ref;
	}
	
	public void setPaymentReference(String ref) {
		this.paymentReference = ref;
	}
	
	public String getNIF() {
		return this.NIF;
	}
	
	public String getIBAN() {
		return this.IBAN;
	}
	
	public double getAmount() {
		return this._amount;
	}

	public String getReference() {
		return this.reference;
	}

	public String getCancellation() {
		return this.cancellation;
	}

	public LocalDate getArrival() {
		return this.arrival;
	}

	public LocalDate getDeparture() {
		return this.departure;
	}

	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}
	
	public String getCancelledRentingReference() {
		return this.cancelledBookingReference;
	}
	
	public void setCancelledRentingReference(String ref) {
		this.cancelledBookingReference = ref;
	}
	
	public void setCancelledInvoice(boolean b) {
		this.cancelledInvoice = b;
	}
	
	public boolean getCancelledInvoice() {
		return this.cancelledInvoice;
	}

	boolean conflict(LocalDate arrival, LocalDate departure) {
		if (isCancelled()) {
			return false;
		}

		if (arrival.equals(departure)) {
			return true;
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}

		if ((arrival.equals(this.arrival) || arrival.isAfter(this.arrival)) && arrival.isBefore(this.departure)) {
			return true;
		}

		if ((departure.equals(this.departure) || departure.isBefore(this.departure))
				&& departure.isAfter(this.arrival)) {
			return true;
		}

		if ((arrival.isBefore(this.arrival) && departure.isAfter(this.departure))) {
			return true;
		}

		return false;
	}

	public String cancel() {
        this.cancel = "CANCEL" + this.reference;
        this.cancellationDate = new LocalDate();
		this._hotel.getProcessorHotel().submitRenting(this);
		
		return this.cancel;
	}
	
	public void cancelPaymentBooking() {    	
		Set<Booking> failedToCancelPay = new HashSet<>();       
        
		for (Booking booking : this.bookingToCancelPay) {
	        try {
	        	BankInterface.cancelPayment(this.getPaymentReference());
				TaxInterface.cancelInvoice(this.getInvoiceReference());
		    }
		    catch(BankException | TaxException ex) {	
				failedToCancelPay.add(booking);
				continue;
		    }	        
		}	
		
		this.bookingToCancelPay.clear();
		this.bookingToCancelPay.addAll(failedToCancelPay);		
	}

	public boolean isCancelled() {
		return this.cancel != null;
	}

}
