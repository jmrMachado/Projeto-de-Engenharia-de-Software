package pt.ulisboa.tecnico.softeng.broker.interfaces;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

import java.util.Set;

public class HotelInterface {
	public static String reserveRoom(String IBAN, String NIF, Room.Type type, LocalDate arrival, LocalDate departure) {
		return Hotel.reserveRoom(IBAN, NIF, type, arrival, departure);
	}

	public static String cancelBooking(String roomConfirmation) {
		return Hotel.cancelBooking(roomConfirmation);
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		return Hotel.getRoomBookingData(reference);
	}

	public static Set<String> bulkBooking(String IBAN, String NIF,int number, LocalDate arrival, LocalDate departure) {
		return Hotel.bulkBooking(IBAN, NIF, number, arrival, departure);
	}
}
