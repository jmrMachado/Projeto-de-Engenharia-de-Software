package pt.ulisboa.tecnico.softeng.car.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import java.util.ArrayList;

public class CarPersistenceTest {

    private final String NAME = "eartz";
    private final String NIF = "NIF";
    private static final String IBAN = "IBAN";
    private static final String PLATE_CAR = "22-33-HZ";
    private static final String DRIVING_LICENSE = "lx1423";
    private static final LocalDate date1 = LocalDate.parse("2018-01-06");
    private static final LocalDate date2 = LocalDate.parse("2018-01-09");
    private static final String IBAN_BUYER = "IBAN";

    @Test
    public void success() {
    	atomicProcess();
        atomicAssert();
    }

    @Atomic(mode = TxMode.WRITE)
    public void atomicProcess() {
        RentACar empresa = new RentACar(NAME, NIF, IBAN);

        Vehicle veiculo = new Car(PLATE_CAR, 10, 10, empresa);

        Renting aluguer = veiculo.rent(DRIVING_LICENSE, date1, date2, NIF, IBAN_BUYER);
    }

    @Atomic(mode = TxMode.READ)
    public void atomicAssert(){
        RentACar temp = new ArrayList<RentACar>(FenixFramework.getDomainRoot().getRentACarSet()).get(0);

        assertEquals(NAME, temp.getName());
        assertEquals(NIF+"1", temp.getCode());
        assertEquals(1, temp.getVehiclesSet().size());
        assertEquals(1, FenixFramework.getDomainRoot().getRentACarCount());

        Vehicle carro = new ArrayList<Vehicle>(temp.getVehiclesSet()).get(0);

        assertEquals(PLATE_CAR, carro.getPlate());
        assertEquals(10, carro.getKilometers());
        assertEquals(10, carro.getPrice(), 2);
        assertNotNull(carro.getRentACar());
        assertEquals(1, carro.getRentingsSet().size());

        Renting aluguer = new ArrayList<Renting>(carro.getRentingsSet()).get(0);

        assertEquals(DRIVING_LICENSE, aluguer.getDrivingLicense());
        assertEquals(date1, aluguer.getBegin());
        assertEquals(date2, aluguer.getEnd());
        assertEquals(NIF, aluguer.getClientNIF());
        assertEquals(IBAN_BUYER, aluguer.getClientIBAN());
    }

    @After
    @Atomic(mode = TxMode.WRITE)
    public void tearDown() {
        for (RentACar empresa : FenixFramework.getDomainRoot().getRentACarSet()) {
            empresa.delete();
        }
    }

}
