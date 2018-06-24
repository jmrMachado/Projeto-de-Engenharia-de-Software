package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

public class ActivityProviderReserveActivityMethodTest {
	private static final int MIN_AGE = 25;
	private static final int MAX_AGE = 80;
	private static final int CAPACITY = 25;
	private static final int AGE = 40;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	private ActivityProvider provider;
	private Activity activity;
	private ActivityOffer offer;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(this.provider, "Bush Walking", MIN_AGE, MAX_AGE, CAPACITY);
		this.offer = new ActivityOffer(this.activity, this.begin, this.end);
	}

	@Test
	public void success() {
		//String book = this.provider.reserveActivity(this.begin, this.end, AGE);
	
	}

	@Test(expected = ActivityException.class)
	public void nullBeginDate() {
		this.provider.reserveActivity(null, this.end, AGE);
		assertEquals(this.begin,null);
	}

	
	
	
	@Test(expected = ActivityException.class)
	public void nullEndDate() {
		this.provider.reserveActivity(this.begin, null, AGE);
		assertEquals(this.end,null);
	}
	
	
	@Test
	public void hasNewAct() {
		Activity newAct = new Activity(this.provider,"Swim",MIN_AGE,MAX_AGE,CAPACITY);
		new ActivityOffer(newAct, this.begin,this.end);
		this.provider.reserveActivity(this.begin, this.end, AGE);
		
		assertEquals(newAct.getName(),"Swim");
		assertEquals(newAct.getMinAge(),MIN_AGE);
		assertEquals(newAct.getMaxAge(),MAX_AGE);
		assertEquals(newAct.getCapacity(),CAPACITY);
		
	}
	
	@Test(expected = ActivityException.class)
	public void offerEmpty() {
		LocalDate nbegin = new LocalDate(2017, 11, 15);
		LocalDate nend = new LocalDate(2017, 11, 23);
		this.provider.reserveActivity(nbegin, nend, AGE);
		
		
	}
	

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
