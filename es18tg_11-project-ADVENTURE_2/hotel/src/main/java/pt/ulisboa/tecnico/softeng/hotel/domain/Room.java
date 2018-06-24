package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

import java.util.HashSet;
import java.util.Set;

public class Room {
    private static int counter = 0;
    private final Hotel hotel;
    private final String number;
    private final Type type;
    private final Set<Booking> bookings = new HashSet<>();
    private final double _preco;
    private final String _roomID;
    private ItemType _it;
	
    public Room(Hotel hotel, String number, Type type) {
		checkArguments(hotel, number, type);

		this.hotel = hotel;
		this.number = number;
		this.type = type;
		this._preco = getPrecoInitializer(type, hotel);
		this._roomID = hotel.getCode() + Integer.toString(++Room.counter);
        this._it = new ItemType(IRS.getIRS(), this._roomID, 10);
		this.hotel.addRoom(this);
	}

	private void checkArguments(Hotel hotel, String number, Type type) {
		if (hotel == null || number == null || number.trim().length() == 0 || type == null) {
			throw new HotelException();
		}

		if (!number.matches("\\d*")) {
			throw new HotelException();
		}
	}

	public Hotel getHotel() {
		return this.hotel;
	}

	public String getNumber() {
		return this.number;
	}
	
	public String getRoomID() {
		return this._roomID;
	}


	public Type getType() {
		return this.type;
	}
	
	public double getPreco() {
		return this._preco;
	}
	
	/**
	 * Adiciona o preco correspondente ao quarto
	 * @param type
	 * @param hotel
	 * @return o preco do quarto
	 */
	private double getPrecoInitializer(Type type, Hotel hotel) {
		if(type == Type.SINGLE) {
			return hotel.getPrecoSingle();
		}
		else {
			return  hotel.getPrecoDouble();
		}
	}

	int getNumberOfBookings() {
		return this.bookings.size();
	}

	boolean isFree(Type type, LocalDate arrival, LocalDate departure) {
		if (!type.equals(this.type)) {
			return false;
		}

		for (Booking booking : this.bookings) {
			if (booking.conflict(arrival, departure)) {
				return false;
			}
		}

		return true;
	}
	
	public Booking reserve(String IBAN, String NIF, Type type, LocalDate arrival, LocalDate departure) {
		int numberOfDays;
    	if (type==null || arrival==null || departure==null || checkArgs2(NIF, IBAN, this.hotel)) {
            throw new HotelException();
        }
    	if (!isFree(type, arrival, departure)) {
            throw new HotelException();
        }    	 
    	numberOfDays = departure.getDayOfYear() - arrival.getDayOfYear();

        Booking booking = new Booking(this.hotel, arrival, departure, this.getPreco() * numberOfDays, IBAN, NIF, getRoomID());
        this.bookings.add(booking);
        this.getHotel().getProcessorHotel().submitRenting(booking);

        return booking;
    }
    
    public boolean checkArgs2(String nif, String iban, Hotel hotel) {
        return (nif == null || nif.trim().length() == 0 || iban == null || iban.trim().length() == 0);
    }

    public enum Type {
        SINGLE, DOUBLE
    }

	public Booking getBooking(String reference) {
		for (Booking booking : this.bookings) {
			if (booking.getReference().equals(reference)
					|| (booking.isCancelled() && booking.getCancellation().equals(reference))) {
				return booking;
			}
		}
		return null;
	}
	
	public static void clearCounter() {
		counter = 0;
	}

}
