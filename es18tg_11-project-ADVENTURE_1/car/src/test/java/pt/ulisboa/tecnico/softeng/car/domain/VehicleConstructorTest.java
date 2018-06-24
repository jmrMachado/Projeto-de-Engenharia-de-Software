package pt.ulisboa.tecnico.softeng.car.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.car.exception.VehicleException;

public class VehicleConstructorTest {

    private static  RentACar empresa;
    private static final String PLATE = "12-34-56";
    private static final int KILOMETERS = 100000;

    @Before
    public void setup(){
        empresa = new RentACar("Opel");
    }

    @Test
    public void success(){
        Vehicle Astra = new Car(PLATE, KILOMETERS, empresa);

        Assert.assertEquals(PLATE, Astra.getPlate());
        Assert.assertEquals(KILOMETERS, Astra.getKilometers());
        Assert.assertNotNull(Astra.getRentingCompany());

    }

    @Test(expected = VehicleException.class)
    public void negativeKilometers(){
        Vehicle Mercedes = new Car("77-25-VE", -1, empresa);
    }

    @Test(expected = VehicleException.class)
    public void wrongPlateSyntax(){
        Vehicle Mazda = new Car("55b47pXP", 10, empresa);
    }

    @Test(expected = VehicleException.class)
    public void wrongPlateSize(){
        Vehicle Bmw = new Car("45-78-98-23", 50, empresa);
    }

    @Test(expected = VehicleException.class)
    public void emptyPlate(){ Vehicle ok = new Car("", 100, empresa); }

    @Test(expected = VehicleException.class)
    public void emptyPlate2(){ Vehicle ok = new Car("        ", 100, empresa); }

    @Test(expected = VehicleException.class)
    public void doublePlate(){
        Vehicle a = new Car(PLATE, 100, empresa);
        Vehicle b = new Car(PLATE, 100, empresa);
    }

    @Test(expected = VehicleException.class)
    public void specialCharactersInPlate(){
        Vehicle Kia = new Car("*´-DP-VE", 50, empresa);
    }

    @Test(expected = VehicleException.class)
    public void motorcycleTest(){
        Vehicle mota = new Motorcycle("12-34-56", 100, null);
    }

    @Test(expected = VehicleException.class)
    public void nullCompany(){
        Vehicle Porsche = new Car("55-79-FP", 100, null);
    }

    @Test(expected = VehicleException.class)
    public void nullPlate(){
        Vehicle charuto = new Car(null, 0, empresa);
    }

    @Test(expected = VehicleException.class)
    public void allNull(){
        Vehicle teste = new Car(null, 0, null);
    }

    @After
    public void tearDown(){
        empresa.RentACarCompanies.clear();
        empresa.getVehicleDatabase().clear();
    }
}
