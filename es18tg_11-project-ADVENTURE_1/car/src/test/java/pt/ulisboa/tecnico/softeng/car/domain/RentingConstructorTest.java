package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.car.exception.RentACarException;
import pt.ulisboa.tecnico.softeng.car.exception.RentingException;

public class RentingConstructorTest {

    private static RentACar empresa;
    private static Vehicle carro;
    private static final String ref = "12345678";
    private static final String license = "1AE";
    private static final LocalDate inicio = LocalDate.parse("2018-09-10");
    private static final LocalDate fim = LocalDate.parse("2018-09-15");

    @Before
    public void setup(){
        empresa = new RentACar("Opel");
        carro = new Car("77-25-VE", 100000, empresa);
    }

    @Test
    public void success(){
        Renting aluguer1 = new Renting(carro, ref, license, inicio, fim, 100);

        Assert.assertEquals(license, aluguer1.getLicense());
        Assert.assertEquals(inicio, aluguer1.getStart());
        Assert.assertEquals(fim, aluguer1.getEnd());
    }

    @Test
    public void updateKilometers(){
        Renting aluguer = new Renting(carro, ref, license, inicio, fim, 100000);
        aluguer.checkout(100);
        Assert.assertEquals(100100, carro.getKilometers());
    }

    @Test(expected = RentingException.class)
    public void wrongDrivingLicense(){
        Renting teste = new Renting(carro, ref, "POLI", inicio, fim, 200);
    }

    @Test(expected = RentingException.class)
    public void nullCar(){
        Renting aluger = new Renting(null, ref, license, inicio, fim, 100);
    }

    @Test(expected = RentACarException.class)
    public void wrongRef(){
        Renting aluger = new Renting(carro, null, license, inicio, fim, 100);
    }

    @Test(expected = RentingException.class)
    public void emptyRef(){
        Renting aluguer = new Renting(carro, "", license, inicio, fim, 100);
    }

    @Test(expected = RentingException.class)
    public void untrimmedRef(){
        Renting aluguer = new Renting(carro, "        ", license, inicio, fim, 100);
    }

    @Test(expected = RentingException.class)
    public void nullLicense(){
        Renting aluguer = new Renting(carro, ref, null, inicio, fim, 100);
    }

    @Test(expected = RentingException.class)
    public void emptyLicense(){
        Renting aluguer = new Renting(carro, ref, "", inicio, fim, 100);
    }

    @Test(expected = RentingException.class)
    public void untrimmedLicense(){
        Renting aluguer = new Renting(carro, ref, "       ", inicio, fim, 100);
    }

    @Test(expected = RentingException.class)
    public void wrongLicenseSyntax() {
        Renting aluguer = new Renting(carro, ref, "1D6", inicio, fim, 100);
    }

    @Test(expected = RentingException.class)
    public void nullDates1(){
        Renting aluguer = new Renting(carro, ref, license, null, fim, 100);
    }

    @Test(expected = RentingException.class)
    public void nullDates2(){
        Renting aluguer = new Renting(carro, ref, license, null, null, 100);
    }

    @Test(expected = RentingException.class)
    public void nullDates3(){
        Renting aluguer = new Renting(carro, ref, license, inicio, null, 100);
    }

    @Test(expected = RentingException.class)
    public void invalidDate(){
        LocalDate teste = LocalDate.parse("2018-02-01");
        LocalDate teste2 = LocalDate.parse("2018-04-10");
        Renting aluguer = new Renting(carro, ref, license, teste2, teste, 100);
    }

    @Test(expected = RentingException.class)
    public void existingRef(){
        Renting temp = carro.reserve(inicio, fim, "1TU");
        Renting erro = new Renting(carro, temp.getRef(), license, inicio, fim, 100);
    }

    @Test(expected = RentingException.class)
    public void negativeKilometers(){
        Renting aluguer = new Renting(carro, ref, license, inicio, fim, -100);
    }

    @After
    public void tearDown(){
        empresa.getVehicleDatabase().clear();
        empresa.RentACarCompanies.clear();
    }
}
