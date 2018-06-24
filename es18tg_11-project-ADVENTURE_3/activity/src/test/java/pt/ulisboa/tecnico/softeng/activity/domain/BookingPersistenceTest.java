package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import static org.junit.Assert.assertEquals;

public class BookingPersistenceTest {

	private static final String PROV_CODE = "codigo";
	private static final String PROV_NAME = "NomeProv";
	private static final String ACTIVITY_NAME = "ActName";
	private static final int MIN_AGE = 20;
	private static final int MAX_AGE = 70;
	private static final int CAPACITY = 30;
	private static final int AMOUNT = 80;
	private static final String NIF = "123456789";
	private static final String IBAN = "1234534343";
	private static final String BUYER_NIF ="987653421";
	private static final String BUYER_IBAN = "942184981";
	private final LocalDate BEGIN = new LocalDate(2018, 10, 12);
	private final LocalDate END = new LocalDate(2018, 10, 18);

	ActivityOffer activityOffer;
	Booking booking;

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		ActivityProvider activityProvider = new ActivityProvider(PROV_CODE, PROV_NAME,NIF,IBAN);
		Activity activity = new Activity(activityProvider, ACTIVITY_NAME, MIN_AGE, MAX_AGE, CAPACITY);
		activityOffer = new ActivityOffer(activity, BEGIN, END,AMOUNT);
		booking = new Booking(activityProvider, activityOffer,BUYER_NIF,BUYER_IBAN);
		new Booking(activityProvider, activityOffer,BUYER_NIF,BUYER_IBAN);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		Booking book = ActivityProvider.getBookingByReference(booking.getReference());
		assertEquals(booking.getReference(), book.getReference());
		Assert.assertNull(book.getCancel());
		Assert.assertNull(book.getCancellationDate());
		assertEquals(activityOffer, book.getActivityOffer());
		assertEquals(2, book.getActivityOffer().getNumberActiveOfBookings());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (ActivityProvider actP : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			actP.delete();
		}
	}
}