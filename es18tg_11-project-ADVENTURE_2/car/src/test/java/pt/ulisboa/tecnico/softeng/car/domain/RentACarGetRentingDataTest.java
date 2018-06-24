package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.*;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.car.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;

@RunWith(JMockit.class)
public class RentACarGetRentingDataTest {

	private static final String NAME1 = "eartz";
	private static final String PLATE_CAR1 = "aa-00-11";
	private static final String DRIVING_LICENSE = "br123";
	private static final LocalDate date1 = LocalDate.parse("2018-01-06");
	private static final LocalDate date2 = LocalDate.parse("2018-01-07");
	private Car car;
	private int preco = 10;

	private static final String NIF_OF_RENT_A_CAR = "666666666";
	private static final String IBAN_OF_RENT_A_CAR = "IBAN";
	
	RentACar rentACar;
	
	Seller rentACarAsSeller;
	Account rentACarAccount;

	@Before
	public void setUp() {
		this.rentACar = new RentACar(NAME1, NIF_OF_RENT_A_CAR, IBAN_OF_RENT_A_CAR);
		this.car = new Car(PLATE_CAR1, 10, preco, this.rentACar);
	}

	@Test
	public void success(@Mocked final BankInterface bint, @Mocked final TaxInterface tint) {
		new Expectations() {
			{
				BankInterface.processPayment(this.anyString, this.anyDouble);
				
				TaxInterface.submitInvoice((InvoiceData) this.any);
			}
		};
		Renting renting = car.rent(DRIVING_LICENSE, IBAN_OF_RENT_A_CAR,NIF_OF_RENT_A_CAR, date1, date2);
		RentingData rd = RentACar.getRentingData(renting.getReference());
		
		assertEquals(renting.getReference(),rd.getReference());
		assertEquals(renting.getBegin(), rd.getBegin());
		assertEquals(renting.getCancellation(), rd.getCancelation());
		assertEquals(renting.getCancellationDate(), rd.getCancelationDate());
		assertEquals(renting.getDrivingLicense(), rd.getDrivingLicense());
		assertEquals(renting.getEnd(), rd.getEnd());
		assertEquals(renting.getIBAN(), rd.getIBAN());
		assertEquals(renting.getVehicle().getPlate(), rd.getPlate());
		assertEquals((int)renting.getVehicle().getPreco() * (renting.getEnd().getDayOfYear() - renting.getBegin().getDayOfYear()), (int)rd.getPrice());
		assertEquals(renting.getVehicle().getRentACar().getCode(), rd.getRentACarCode());
		assertEquals(renting.getVehicle().getRentACar().getCode(),rd.getRentACarCode());
		
	}
	
	
	@Test(expected = CarException.class)
	public void getRentingDataFail() {
		RentACar.getRentingData("1");
	}
	
	
	@Test(expected = CarException.class)
	public void nullReference() {
		RentACar.getRentingData(null);
	}

	@Test(expected = CarException.class)
	public void emptyReference() {
		RentACar.getRentingData("");
	}
	
	@After
	public void tearDown() {
		RentACar.rentACars.clear();
		Vehicle.plates.clear();
		Account.cleanCount();
		IRS.getIRS().clearAll();
	}
}
