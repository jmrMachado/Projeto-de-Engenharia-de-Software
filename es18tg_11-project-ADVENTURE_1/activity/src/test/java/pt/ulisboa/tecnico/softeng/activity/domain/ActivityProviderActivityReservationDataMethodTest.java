package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityProviderActivityReservationDataMethodTest {
	private static final String NAME = "ExtremeAdventure";
	private static final String CODE = "XtremX";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private ActivityProvider provider;
	private ActivityOffer offer;
	private Booking booking;
	private Activity activity;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider(CODE, NAME);
		this.activity = new Activity(this.provider, "Bush Walking", 18, 80, 3);
		this.offer = new ActivityOffer(activity, this.begin, this.end);
		this.booking = new Booking(this.provider, this.offer);
	}

	@Test
	public void success() {
		ActivityReservationData data = ActivityProvider.getActivityReservationData(this.booking.getReference());

		assertEquals(this.booking.getReference(), data.getReference());
		assertNull(data.getCancellation());
		assertEquals(NAME, data.getName());
		assertEquals(CODE, data.getCode());
		assertEquals(this.begin, data.getBegin());
		assertEquals(this.end, data.getEnd());
		assertNull(data.getCancellationDate());
	}

	@Test
	public void successCancelled() {
		this.booking.cancel();
		ActivityReservationData data = ActivityProvider.getActivityReservationData(this.booking.getCancellation());

		assertEquals(this.booking.getReference(), data.getReference());
		assertEquals(this.booking.getCancellation(), data.getCancellation());
		assertEquals(NAME, data.getName());
		assertEquals(CODE, data.getCode());
		assertEquals(this.begin, data.getBegin());
		assertEquals(this.end, data.getEnd());
		assertNotNull(data.getCancellationDate());
	}
	
	@Test
	public void testSets() {
		ActivityReservationData data = new ActivityReservationData();//ActivityProvider.getActivityReservationData(this.booking.getReference());
		String novoNome = "TOT";
		String novaRef ="asd123";
		String canc = "cancel";
		String cod ="cod";
		LocalDate _begin = new LocalDate(2018,1,1);
		LocalDate _end = new LocalDate(2018,2,2);
		LocalDate _cancDate = new LocalDate(2018,3,3);
		
		data.setName(novoNome);
		assertEquals(data.getName(), novoNome);
		data.setReference(novaRef);
		assertEquals(data.getReference(), novaRef);
		data.setCancellation(canc);
		assertEquals(data.getCancellation(), canc);
		data.setCode(cod);
		assertEquals(data.getCode(), cod);
		data.setBegin(_begin);
		assertEquals(data.getBegin(), _begin);
		data.setEnd(_end);
		assertEquals(data.getEnd(), _end);
		data.setCancellationDate(_cancDate);
		assertEquals(data.getCancellationDate(), _cancDate);

		assertEquals(novoNome,data.getName());
		assertEquals(novaRef,data.getReference());
		assertEquals(canc,data.getCancellation());
		assertEquals(cod,data.getCode());
		
		
	}

	
	@Test(expected = ActivityException.class)
	public void nullReference() {
		ActivityProvider.getActivityReservationData(null);
		assertEquals(this.booking.getReference(),null);
	}

	@Test(expected = ActivityException.class)
	public void emptyReference() {
		ActivityProvider.getActivityReservationData("");
		assertEquals(this.booking.getReference(),"");
	}

	@Test(expected = ActivityException.class)
	public void notExistsReference() {
		ActivityProvider.getActivityReservationData("XPTO");
		assertEquals(this.booking.getReference(),"XPTO");
	}
	
	@Test(expected = ActivityException.class)
	public void nonExistsProviders() {
		ActivityProvider.providers.clear();
		ActivityProvider.getActivityReservationData(this.booking.getReference());
		assertEquals(ActivityProvider.providers.size(),0);
	}
	
	@Test(expected = ActivityException.class)
	public void nonExistsOffers() {
		activity.getOffers().clear();
		ActivityProvider.getActivityReservationData(this.booking.getReference());
		assertEquals(activity.getOffers().size(),0);
	}
	
	@Test(expected = ActivityException.class)
	public void nonExistsProvidersAndOffers() {
		ActivityProvider.providers.clear();
		activity.getOffers().clear();
		ActivityProvider.getActivityReservationData(this.booking.getReference());
		assertEquals(activity.getOffers().size(),0);
		assertEquals(ActivityProvider.providers.size(),0);
		
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
