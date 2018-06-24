package pt.ulisboa.tecnico.softeng.broker.domain;


import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class BrokerConstructorMethodTest {
	private static final String HOTEL_NAME = "Londres";
	
	@Test
	public void success() {
		Broker broker = new Broker("BR01", "WeExplore");

		Assert.assertEquals("BR01", broker.getCode());
		Assert.assertEquals("WeExplore", broker.getName());
		Assert.assertEquals(0, broker.getNumberOfAdventures());
		Assert.assertTrue(Broker.brokers.contains(broker));
	}
	
	@Test
	public void differentCodes() {
		
		 new Broker("BR01", "WeExplore");
		 new Broker("BR02", "WeExploreX");
		 new Broker("BR03", "WeExploreY");
		 Assert.assertEquals(3,Broker.brokers.size());	 
		 
	}

	
	@Test
	public void nullCode() {
		try {
			new Broker(null, "WeExplore");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyCode() {
		try {
			new Broker("", "WeExplore");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankCode() {
		try {
			new Broker("  ", "WeExplore");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void uniqueCode() {
		Broker broker = new Broker("BR01", "WeExplore");

		try {
			new Broker("BR01", "WeExploreX");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(1, Broker.brokers.size());
			Assert.assertTrue(Broker.brokers.contains(broker));
		}
	}

	@Test
	public void nullName() {
		try {
			new Broker("BR01", null);
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void emptyName() {
		try {
			new Broker("BR01", "");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}

	@Test
	public void blankName() {
		try {
			new Broker("BR01", "    ");
			Assert.fail();
		} catch (BrokerException be) {
			Assert.assertEquals(0, Broker.brokers.size());
		}
	}
	
	@Test
	public void newBulkBooking() {
		LocalDate arr = new LocalDate(2018,1,1);
		LocalDate dep = new LocalDate(2018,1,2);
		Broker broker = new Broker("BR01", "WeExplore");
		broker.bulkBooking(0, arr, dep);
		Assert.assertFalse(Broker.brokers.isEmpty());
	}
	
	@Test
	public void nullArriveBulkBooking() {
		LocalDate dep = new LocalDate(2018,1,2);
		Broker broker = new Broker("BR01", "WeExplore");
		broker.bulkBooking(1, null, dep);
		Assert.assertFalse(Broker.brokers.isEmpty());
	}
	
	@Test
	public void nullDepartureBulkBooking() {
		LocalDate arr = new LocalDate(2018,1,1);
		Broker broker = new Broker("BR01", "WeExplore");
		broker.bulkBooking(1, arr, null);
		Assert.assertFalse(Broker.brokers.isEmpty());
	}
	
	@Test
	public void bukkRoomBookingGets() {
		LocalDate arr = new LocalDate(2018,1,1);
		LocalDate dep = new LocalDate(2018,1,2);
		BulkRoomBooking brb = new BulkRoomBooking(1, arr, dep);
		Assert.assertTrue(brb.getReferences().isEmpty());
		Assert.assertEquals(brb.getNumber(), 1);
		Assert.assertEquals(brb.getArrival(), arr);
		Assert.assertEquals(brb.getDeparture(), dep);	
	}
	
	@Test
	public void processBookingNoHotelTest() {
		LocalDate arr = new LocalDate(2018,1,1);
		LocalDate dep = new LocalDate(2018,1,2);
		BulkRoomBooking brb = new BulkRoomBooking(1, arr, dep);
		brb.processBooking();
		brb.processBooking();
		brb.processBooking();
		
		brb.processBooking();
	}
	
	@Test
	public void processBookingWithHotelTest() {
		Hotel h = new Hotel("1234567", "nome");
		LocalDate arr = new LocalDate(2018,1,1);
		LocalDate dep = new LocalDate(2018,1,2);
		Booking b = new Booking(h, arr, dep);
		Room r = new Room(h, "12", Type.SINGLE);
		BulkRoomBooking brb = new BulkRoomBooking(1, arr, dep);
		brb.processBooking();
	}
	
	@Test
	public void getReferenceNotCancelledTest() {
		LocalDate arr = new LocalDate(2018,1,1);
		LocalDate dep = new LocalDate(2018,1,2);
		BulkRoomBooking brb = new BulkRoomBooking(1, arr, dep);
		Assert.assertNull(brb.getReference("tipo"));
	} //esta vai para o null
	
	@Test
	public void getReferenceCancelledTest() {
		LocalDate arr = new LocalDate(2018,1,1);
		LocalDate dep = new LocalDate(2018,1,2);
		BulkRoomBooking brb = new BulkRoomBooking(1, arr, dep);
		brb.processBooking();
		brb.processBooking();
		brb.processBooking();
		Assert.assertNull(brb.getReference("tipo"));
	} //canceled primeiro if
	
	@Test
	public void getReferenceNeitherReferenceTest() {
		LocalDate arr = new LocalDate(2018,1,1);
		LocalDate dep = new LocalDate(2018,1,2);
		BulkRoomBooking brb = new BulkRoomBooking(1, arr, dep);
		brb.processBooking();
		brb.getReference("NADA"); // data = null e getRoomType !=
	} //ESTE TESTE NAO ESTA A FAZER NADA. Ele da logo erro no processbooking

	@Test
	public void getReferenceWithReferenceTest() {
		Hotel h = new Hotel("7654321", HOTEL_NAME);
		LocalDate arr = new LocalDate(2018,1,1);
		LocalDate dep = new LocalDate(2018,1,2);
		BulkRoomBooking brb = new BulkRoomBooking(1, arr, dep);
		Booking b = new Booking(h, arr, dep);		
		Room r = new Room(h, "12", Type.SINGLE);
		brb.processBooking();
		Assert.assertTrue(brb.getReference("SINGLE") != null); // data != null e getRoomType = Single
	} // 2 OF 4 BRANCHES MISSING 
	
	@Test
	public void getReferenceWithReferenceTest2() {
		Hotel h = new Hotel("7654321", HOTEL_NAME);
		LocalDate arr = new LocalDate(2018,1,1);
		LocalDate dep = new LocalDate(2018,1,2);
		BulkRoomBooking brb = new BulkRoomBooking(1, arr, dep);
		Booking b = new Booking(h, arr, dep);		
		Room r = new Room(h, "12", Type.SINGLE);
		brb.processBooking();
		Assert.assertTrue(brb.getReference("DOUBLE") == null); // data != null e getRoomType = Double
	} // 3 OF 4 BRANCHES MISSING 
	
	
	@Test
	public void getReferenceNoReferenceTest() {
		Hotel h = new Hotel("7654321", HOTEL_NAME);
		LocalDate arr = new LocalDate(2018,1,1);
		LocalDate dep = new LocalDate(2018,1,2);
		BulkRoomBooking brb = new BulkRoomBooking(1, arr, dep);
		Booking b = new Booking(h, arr, dep);		
		Room r = new Room(h, "12", Type.SINGLE);
		brb.processBooking();
		Hotel.hotels.clear(); //data = null e getRoomType = null
		Assert.assertTrue(brb.getReference("DOUBLE") == null);
	} // 4 OF 4
	
	public void bothDif() {
		Hotel h = new Hotel("7654321", HOTEL_NAME);
		LocalDate arr = new LocalDate(2018,1,1);
		LocalDate dep = new LocalDate(2018,1,2);
		BulkRoomBooking brb = new BulkRoomBooking(1, arr, dep);
		Booking b = new Booking(h, arr, dep);		
		Room r = new Room(h, "12", Type.SINGLE);
		brb.processBooking();
		Assert.assertTrue(brb.getReference("DOUBLE") == null); 
	} // Teste adicional
	
	
	@After
	public void tearDown() {
		Broker.brokers.clear();
		Hotel.hotels.clear();
	}

}
