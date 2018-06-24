package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelReserveRoomMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 24);
	private Hotel hotel;
	private Room room;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Lisboa");
		this.room = new Room(this.hotel, "01", Type.SINGLE);
	}

	@SuppressWarnings("static-access")
	@Test
	public void success() {
		assertNotNull(this.hotel.reserveRoom(Type.SINGLE, this.arrival, this.departure));
	}
	
	@SuppressWarnings("static-access")
	@Test(expected=HotelException.class)
	public void noRoomDifferentType() {
		this.hotel.reserveRoom(Type.DOUBLE, this.arrival, this.departure);
	}
	
	@SuppressWarnings("static-access")
	public void invalidDates() {
		assertNull(this.hotel.reserveRoom(Type.SINGLE, this.departure, this.departure));
	}
	
	
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
	
}