package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import org.joda.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;

public class RoomBookingData {
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate arrival;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate departure;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate cancellationDate;
	private String reference;
	private String cancellation;
	private String hotelName;
	private String hotelCode;
	private String roomNumber;
	private String roomType;
	private double price;
	private String paymentReference;
	private String invoiceReference;
	private String buyerNif;
	private String buyerIban;

	public RoomBookingData() {
	}

	public RoomBookingData(Booking booking) {
		this.reference = booking.getReference();
		this.cancellation = booking.getCancellation();
		this.hotelName = booking.getRoom().getHotel().getName();
		this.hotelCode = booking.getRoom().getHotel().getCode();
		this.roomNumber = booking.getRoom().getNumber();
		this.roomType = booking.getRoom().getType().name();
		this.arrival = booking.getArrival();
		this.departure = booking.getDeparture();
		this.cancellationDate = booking.getCancellationDate();
		this.price = booking.getPrice();
		this.paymentReference = booking.getPaymentReference();
		this.invoiceReference = booking.getInvoiceReference();
		
		this.buyerIban = booking.getBuyerIban();
		this.buyerNif= booking.getBuyerNif();
	}

	public String getReference() {
		return this.reference;
	}

	public String getCancellation() {
		return this.cancellation;
	}

	public String getHotelName() {
		return this.hotelName;
	}

	public String getHotelCode() {
		return this.hotelCode;
	}

	public String getRoomNumber() {
		return this.roomNumber;
	}

	public String getRoomType() {
		return this.roomType;
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

	public double getPrice() {
		return this.price;
	}

	public String getPaymentReference() {
		return this.paymentReference;
	}

	public String getInvoiceReference() {
		return this.invoiceReference;
	}

	public String getBuyerNif() {
		return this.buyerNif;
	}

	public void setBuyerNif(String buyerNif) {
		this.buyerNif = buyerNif;
	}

	public String getBuyerIban() {
		return this.buyerIban;
	}

	public void setBuyerIban(String buyerIban) {
		this.buyerIban = buyerIban;
	}
	
	public void setArrival(LocalDate a) {
		this.arrival = a;
	}

	public void setDeparture(LocalDate d) {
		this.departure = d;
	}

}
