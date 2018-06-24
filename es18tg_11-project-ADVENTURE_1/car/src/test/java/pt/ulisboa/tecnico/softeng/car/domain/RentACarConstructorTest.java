package pt.ulisboa.tecnico.softeng.car.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.car.exception.RentACarException;

public class RentACarConstructorTest {

    private static final String COMPANY_NAME = "Opel";
    private static final String COMPANY_CODE = "Renting Company 1";

    @Test
    public void success() {
        RentACar empresa = new RentACar(COMPANY_NAME);

        Assert.assertEquals(COMPANY_NAME, empresa.getNome());
        Assert.assertEquals(COMPANY_CODE, empresa.getCode());
        Assert.assertEquals(0, empresa.getNumberOfCars());
        Assert.assertEquals(1, RentACar.RentACarCompanies.size());
    }

    @Test(expected = RentACarException.class)
    public void nullName(){ new RentACar(null); }

    @Test(expected = RentACarException.class)
    public void emptyName(){ new RentACar(""); }

    @Test(expected = RentACarException.class)
    public void blankName(){ new RentACar("          "); }

    @After
    public void tearDown(){ RentACar.RentACarCompanies.clear(); }

}
