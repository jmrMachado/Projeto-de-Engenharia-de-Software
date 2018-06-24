package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelGetRoomBookingDataMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 24);
	private Hotel hotel;
	private Room room;
	private Booking booking;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Lisboa");
		this.room = new Room(this.hotel, "01", Type.SINGLE);
		this.booking = this.room.reserve(Type.SINGLE, this.arrival, this.departure);
	}

	@Test
	public void success() {
		RoomBookingData data = Hotel.getRoomBookingData(this.booking.getReference());

		assertEquals(this.booking.getReference(), data.getReference());
		assertNull(data.getCancellation());
		assertNull(data.getCancellationDate());
		assertEquals(this.hotel.getName(), data.getHotelName());
		assertEquals(this.hotel.getCode(), data.getHotelCode());
		assertEquals(this.room.getNumber(), data.getRoomNumber());
		assertEquals(this.room.getType().name(), data.getRoomType());
		assertEquals(this.booking.getArrival(), data.getArrival());
		assertEquals(this.booking.getDeparture(), data.getDeparture());
	}
	
	@Test
	public void testSets() {
		this.booking.cancel();
		RoomBookingData data = new RoomBookingData();
		
		String novoNome = "TOT";
		String novaRef ="asd123";
		String canc = "cancel";
		String cod ="cod";
		String roomnumber ="02";
		Type tipo = Type.DOUBLE;
		LocalDate _begin = new LocalDate(2018,1,1);
		LocalDate _end = new LocalDate(2018,2,2);
		LocalDate _cancDate = new LocalDate(2018,3,3);
		
		data.setReference(novaRef);
		assertEquals(data.getReference(), novaRef);
		data.setCancellation(canc);
		assertEquals(data.getCancellation(), canc);
		data.setCancellationDate(_cancDate);
		assertEquals(data.getCancellationDate(), _cancDate);
		data.setHotelName(novoNome);
		assertEquals(data.getHotelName(), novoNome);
		data.setHotelCode(cod);
		assertEquals(data.getHotelCode(), cod);
		data.setRoomNumber(roomnumber);
		assertEquals(data.getRoomNumber(),roomnumber);
		data.setRoomType(tipo.toString());
		assertEquals(data.getRoomType(),tipo.toString());
		data.setArrival(_begin);
		assertEquals(data.getArrival(), _begin);
		data.setDeparture(_end);
		assertEquals(data.getDeparture(), _end);
		

		
		
	}

	@Test
	public void successCancellation() {
		this.booking.cancel();
		RoomBookingData data = Hotel.getRoomBookingData(this.booking.getCancellation());

		assertEquals(this.booking.getReference(), data.getReference());
		assertEquals(this.booking.getCancellation(), data.getCancellation());
		assertEquals(this.booking.getCancellationDate(), data.getCancellationDate());
		assertEquals(this.hotel.getName(), data.getHotelName());
		assertEquals(this.hotel.getCode(), data.getHotelCode());
		assertEquals(this.room.getNumber(), data.getRoomNumber());
		assertEquals(this.room.getType().name(), data.getRoomType());
		assertEquals(this.booking.getArrival(), data.getArrival());
		assertEquals(this.booking.getDeparture(), data.getDeparture());
	}

	@Test(expected = HotelException.class)
	public void nullReference() {
		Hotel.getRoomBookingData(null);
	}

	@Test(expected = HotelException.class)
	public void emptyReference() {
		Hotel.getRoomBookingData("");
	}

	@Test(expected = HotelException.class)
	public void referenceDoesNotExist() {
		Hotel.getRoomBookingData("XPTO");
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
