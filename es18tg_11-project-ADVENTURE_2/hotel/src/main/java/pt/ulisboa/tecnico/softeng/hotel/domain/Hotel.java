package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel {
	public static Set<Hotel> hotels = new HashSet<>();

	static final int CODE_SIZE = 7;

	private final String code;
	private final ProcessorHotel ph = new ProcessorHotel();
	private final String name;
	private final String _NIF;
	private final String _IBAN;
	private final double _precoSingle;
	private final double _precoDouble;
	private final Set<Room> rooms = new HashSet<>();
	
	//nif e iban do seller
	public Hotel(String code, String name, String NIF, String IBAN, double pSingle, double pDouble) {
		checkArguments(code, name, NIF, IBAN, pSingle, pDouble);

		this.code = code;
		this.name = name;
		this._NIF = NIF;
		this._IBAN = IBAN;
		this._precoSingle = pSingle;
		this._precoDouble = pDouble;
		Hotel.hotels.add(this);
	}

	private void checkArguments(String code, String name, String NIF, String IBAN, double pSingle, double pDouble) {
		if (code == null || name == null || code.trim().length() == 0 || name.trim().length() == 0) {
			throw new HotelException();
		}
		
		if (NIF == null	|| NIF.trim().length() == 0 || IBAN == null || IBAN.trim().length() == 0) {
			throw new HotelException("NIF ou IBAN invalido");		
		}

		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException();
		}
		
		if (pSingle < 0 || pDouble < 0) {
			throw new HotelException("Precos < 0");		
		}

		for (Hotel hotel : hotels) {
			if (hotel.getCode().equals(code)) {
				throw new HotelException();
			}
		}
	}

	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		for (Room room : this.rooms) {
			if (room.isFree(type, arrival, departure)) {
				return room;
			}
		}
		return null;
	}
	
	public ProcessorHotel getProcessorHotel() {
		return this.ph;
	}

	public String getCode() {
		return this.code;
	}

	public String getName() {
		return this.name;
	}
	
	public String getNIF() {
		return this._NIF;
	}
	
	public String getIBAN() {
		return this._IBAN;
	}
	
	public double getPrecoSingle() {
		return this._precoSingle;
	}
	
	public double getPrecoDouble() {
		return this._precoDouble;
	}
	
	void addRoom(Room room) {
		if (hasRoom(room.getNumber())) {
			throw new HotelException();
		}

		this.rooms.add(room);
	}

	int getNumberOfRooms() {
		return this.rooms.size();
	}

	public boolean hasRoom(String number) {
		for (Room room : this.rooms) {
			if (room.getNumber().equals(number)) {
				return true;
			}
		}
		return false;
	}

	private Booking getBooking(String reference) {
		for (Room room : this.rooms) {
			Booking booking = room.getBooking(reference);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}
	
	//IBAN e NIF do cliente
	public static String reserveRoom(String IBAN, String NIF, Room.Type type, LocalDate arrival, LocalDate departure) {
		for (Hotel hotel : Hotel.hotels) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(IBAN, NIF, type, arrival, departure).getReference();
			}
		}
		throw new HotelException();
	}

	public static String cancelBooking(String reference) {
		for (Hotel hotel : hotels) {
			Booking booking = hotel.getBooking(reference);
			if (booking != null) {
				return booking.cancel();
			}
		}
		throw new HotelException();
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		for (Hotel hotel : hotels) {
			for (Room room : hotel.rooms) {
				Booking booking = room.getBooking(reference);
				if (booking != null) {
					return new RoomBookingData(room, booking);
				}
			}
		}
		throw new HotelException();
	}

	public static Set<String> bulkBooking(String IBANc, String NIFc, int number, LocalDate arrival, LocalDate departure) {
		if (IBANc==null || IBANc.trim().length()==0 || NIFc==null || NIFc.trim().length()==0 || number < 1 || arrival == null || departure==null || departure.isBefore(arrival)) {
			throw new HotelException();
		}
		
		//verificar IBAN e NIF
		Set<Room> rooms = getAvailableRooms(number, arrival, departure);
		if (rooms.size() < number) {
			throw new HotelException();
		}

		Set<String> references = new HashSet<>();
		for (Room room : rooms) {
			references.add(room.reserve(IBANc, NIFc, room.getType(), arrival, departure).getReference());
		}

		return references;
	}

	static Set<Room> getAvailableRooms(int number, LocalDate arrival, LocalDate departure) {
		Set<Room> rooms = new HashSet<>();
		for (Hotel hotel : hotels) {
			for (Room room : hotel.rooms) {
				if (room.isFree(room.getType(), arrival, departure)) {
					rooms.add(room);
					if (rooms.size() == number) {
						return rooms;
					}
				}
			}
		}
		return rooms;
	}

    public void removeRooms() {
        rooms.clear();
    }
}
