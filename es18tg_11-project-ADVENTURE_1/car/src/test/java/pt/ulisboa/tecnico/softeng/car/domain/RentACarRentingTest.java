package pt.ulisboa.tecnico.softeng.car.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.car.dataobjects.RentingData;
import pt.ulisboa.tecnico.softeng.car.exception.*;

import java.util.ArrayList;

public class RentACarRentingTest {

    private static  String COMPANY_NAME = "Opel";
    private static  RentACar empresa;
    private static  Vehicle carro;
    private static  Vehicle mota;
    private static  Vehicle charuto;
    private static  Vehicle triciclo;

    @Before
    public void setup(){
        empresa = new RentACar(COMPANY_NAME);
        carro = new Car("77-25-VE", 100, empresa);
        mota = new Motorcycle("25-77-VE", 200, empresa);
        charuto = new Car("55-33-PE", 300, empresa);
        triciclo = new Motorcycle("33-44-TY", 400, empresa);
    }

    @Test
    public void getAvailableCars(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate fim = LocalDate.parse("2018-09-15");

        Assert.assertNotNull(empresa.getAllAvailableCars(inicio, fim));
        ArrayList<Car> temp = empresa.getAllAvailableCars(inicio,fim);
        for (Vehicle i : temp){
            Assert.assertEquals(2, temp.size());
            Assert.assertNotNull(i);
            Assert.assertTrue(i instanceof  Car);
        }
    }

    @Test(expected = RentACarException.class)
    public void getRandomReferenceRentingData(){
        empresa.getRentingData("12345678");
    }

    @Test(expected = VehicleException.class)
    public void addVehicleProblem(){
        Vehicle carro = new Car("77-25-VE", 100, empresa);
        empresa.addVehicle(carro);
    }

    @Test(expected = RentACarException.class)
    public void addNullVehicleProblem(){
        empresa.addVehicle(null);
    }

    @Test(expected = RentACarException.class)
    public void getAvailableCarsNullDates1(){
        LocalDate inicio = null;
        LocalDate fim = LocalDate.parse("2018-09-15");
        empresa.getAllAvailableCars(inicio, fim);
    }


    @Test(expected = RentACarException.class)
    public void getAvailableCarsNullDates2(){
        LocalDate inicio = null;
        LocalDate fim = LocalDate.parse("2018-09-15");
        empresa.getAllAvailableCars(fim, inicio);
    }


    @Test(expected = RentACarException.class)
    public void getAvailableCarsNullDates3(){
        LocalDate inicio = null;
        LocalDate fim = null;
        empresa.getAllAvailableCars(inicio, fim);
    }

    @Test(expected = InvalidDateException.class)
    public void getAvailableCarsPastDates(){
        LocalDate inicio = LocalDate.parse("2017-09-15");
        LocalDate fim = LocalDate.parse("2018-09-15");
        empresa.getAllAvailableCars(inicio, fim);
    }

    @Test(expected = InvalidDateException.class)
    public void invalidDates(){
        LocalDate inicio = LocalDate.parse("2018-02-01");
        LocalDate fim = LocalDate.parse("2018-09-15");
        empresa.getAllAvailableCars(inicio, fim);
    }

    @Test(expected = InvalidDateException.class)
    public void invalidDates2(){
        LocalDate inicio = LocalDate.parse("2018-04-01");
        LocalDate fim = LocalDate.parse("2018-01-15");
        empresa.getAllAvailableCars(inicio, fim);
    }

    @Test(expected = VehicleException.class)
    public void coverageDates1(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate inicio2 = LocalDate.parse("2018-09-11");
        LocalDate fim = LocalDate.parse("2018-09-15");
        LocalDate fim2 = LocalDate.parse("2018-09-14");
        carro.reserve(inicio,fim,"1RE");
        carro.reserve(inicio2, fim2, "5TY");
    }

    @Test(expected = VehicleException.class)
    public void coverageDates2(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate inicio2 = LocalDate.parse("2018-09-9");
        LocalDate fim = LocalDate.parse("2018-09-15");
        LocalDate fim2 = LocalDate.parse("2018-09-14");
        carro.reserve(inicio,fim,"1RY");
        carro.reserve(inicio2, fim2, "5OY");
    }

    @Test(expected = VehicleException.class)
    public void coverageDates4(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate inicio2 = LocalDate.parse("2018-09-11");
        LocalDate fim = LocalDate.parse("2018-09-15");
        LocalDate fim2 = LocalDate.parse("2018-09-16");
        carro.reserve(inicio,fim,"1RE");
        carro.reserve(inicio2, fim2, "5TY");
    }

    @Test(expected = CarException.class)
    public void coverageDates5(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate inicio2 = LocalDate.parse("2018-09-11");
        LocalDate fim = LocalDate.parse("2018-09-15");
        LocalDate fim2 = LocalDate.parse("2018-09-9");
        carro.reserve(inicio,fim,"1RE");
        carro.reserve(inicio2, fim2, "5TY");
    }

    @Test
    public void coverageDates6(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate inicio2 = LocalDate.parse("2018-09-16");
        LocalDate fim = LocalDate.parse("2018-09-15");
        LocalDate fim2 = LocalDate.parse("2018-09-19");
        carro.reserve(inicio,fim,"1RE");
        carro.reserve(inicio2, fim2, "5TY");
    }

    @Test(expected = VehicleException.class)
    public void coverageDates7(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate inicio2 = LocalDate.parse("2018-09-9");
        LocalDate fim = LocalDate.parse("2018-09-15");
        LocalDate fim2 = LocalDate.parse("2018-09-19");
        carro.reserve(inicio,fim,"1RE");
        carro.reserve(inicio2, fim2, "5TY");
    }

    @Test
    public void coverageDates8(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate inicio2 = LocalDate.parse("2018-09-1");
        LocalDate fim = LocalDate.parse("2018-09-15");
        LocalDate fim2 = LocalDate.parse("2018-09-9");
        carro.reserve(inicio,fim,"1RE");
        carro.reserve(inicio2, fim2, "5TY");
    }

    @Test(expected = CarException.class)
    public void coverageDates9(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate inicio2 = LocalDate.parse("2018-09-16");
        LocalDate fim = LocalDate.parse("2018-09-8");
        LocalDate fim2 = LocalDate.parse("2018-09-9");
        carro.reserve(inicio,fim,"1RE");
        carro.reserve(inicio2, fim2, "5TY");
    }

    @Test(expected = VehicleException.class)
    public void coverageDates10(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate inicio2 = LocalDate.parse("2018-09-10");
        LocalDate fim = LocalDate.parse("2018-09-15");
        LocalDate fim2 = LocalDate.parse("2018-09-19");
        carro.reserve(inicio,fim,"1RE");
        carro.reserve(inicio2, fim2, "5TY");
    }

    @Test(expected = VehicleException.class)
    public void coverageDates11(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate inicio2 = LocalDate.parse("2018-09-15");
        LocalDate fim = LocalDate.parse("2018-09-15");
        LocalDate fim2 = LocalDate.parse("2018-09-19");
        carro.reserve(inicio,fim,"1RE");
        carro.reserve(inicio2, fim2, "5TY");
    }

    @Test(expected = VehicleException.class)
    public void coverageDates12(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate inicio2 = LocalDate.parse("2018-09-9");
        LocalDate fim = LocalDate.parse("2018-09-15");
        LocalDate fim2 = LocalDate.parse("2018-09-10");
        carro.reserve(inicio,fim,"1RE");
        carro.reserve(inicio2, fim2, "5TY");
    }

    @Test(expected = VehicleException.class)
    public void coverageDates13(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate inicio2 = LocalDate.parse("2018-09-13");
        LocalDate fim = LocalDate.parse("2018-09-15");
        LocalDate fim2 = LocalDate.parse("2018-09-15");
        carro.reserve(inicio,fim,"1RE");
        carro.reserve(inicio2, fim2, "5TY");
    }

    @Test
    public void successCancellation(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate fim = LocalDate.parse("2018-09-13");
        Renting temp = carro.reserve(inicio, fim, "1PT");
        carro.cancelRenting(temp.getRef());
        Assert.assertNull(carro.getRent(temp.getRef()));
    }

    @Test(expected = VehicleException.class)
    public void nullRef(){
        carro.cancelRenting(null);
    }

    @Test(expected = RentingException.class)
    public void noSuchRenting(){
        carro.cancelRenting("1234567890");
    }

    @Test
    public void getAvailableMotorcycles(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate fim = LocalDate.parse("2018-09-15");

        Assert.assertNotNull(empresa.getAllAvailableMotorcycles(inicio, fim));
        ArrayList<Car> temp = empresa.getAllAvailableMotorcycles(inicio,fim);
        for (Vehicle i : temp){
            Assert.assertNotNull(i);
            Assert.assertTrue(i instanceof  Motorcycle);
        }
    }

    @Test
    public void getAvailableMotorcyclesAndCarsCoverage(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate fim = LocalDate.parse("2018-09-15");
        carro.reserve(inicio,fim,"1RD");
        mota.reserve(inicio,fim,"4TG");
        ArrayList<Car> temp1 = empresa.getAllAvailableCars(inicio,fim);
        ArrayList<Motorcycle> temp2 = empresa.getAllAvailableMotorcycles(inicio,fim);
        Assert.assertNotNull(temp1);
        Assert.assertNotNull(temp2);
        Assert.assertEquals(1, temp1.size());
        Assert.assertEquals(1, temp2.size());
    }

    @Test(expected = RentACarException.class)
    public void getAvailableMotorcyclesNullDates1(){
        LocalDate inicio = null;
        LocalDate fim = LocalDate.parse("2018-09-15");
        empresa.getAllAvailableMotorcycles(inicio, fim);
    }


    @Test(expected = RentACarException.class)
    public void getAvailableMotorcyclesNullDates2(){
        LocalDate inicio = null;
        LocalDate fim = LocalDate.parse("2018-09-15");
        empresa.getAllAvailableMotorcycles(fim, inicio);
    }


    @Test(expected = RentACarException.class)
    public void getAvailableMotorcyclesNullDates3(){
        LocalDate inicio = null;
        LocalDate fim = null;
        empresa.getAllAvailableMotorcycles(inicio, fim);
    }

    @Test(expected = InvalidDateException.class)
    public void getAvailableMotorcyclesPastDates(){
        LocalDate inicio = LocalDate.parse("2017-09-15");
        LocalDate fim = LocalDate.parse("2018-09-15");
        empresa.getAllAvailableMotorcycles(inicio, fim);
    }

    @Test(expected = VehicleException.class)
    public void doublePlates(){
        Vehicle repetido = new Car("77-25-VE", 1000, empresa);
        Vehicle repetido2 = new Car("77-25-VE", 1000, empresa);
    }

    @Test(expected = VehicleException.class)
    public void wrongUpdate(){
        Vehicle repetido = new Car("64-91-GP", 1000, empresa);
        repetido.updateKilometers(-56);
    }

    @Test(expected = VehicleException.class)
    public void notFreeToRent(){
        Vehicle repetido = new Car("64-91-GP", 1000, empresa);
        repetido.reserve(LocalDate.parse("2018-3-30"), LocalDate.parse("2018-4-25"), "1TG");
        repetido.reserve(LocalDate.parse("2018-3-30"), LocalDate.parse("2018-4-25"), "6TG");
    }

    @Test
    public void tryToRent(){
        Vehicle porra = new Car("01-58-FK", 100, empresa);
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate fim = LocalDate.parse("2018-09-15");
        Renting temp = porra.reserve(inicio, fim, "1AR");
        Assert.assertNotNull(temp);
        Assert.assertNotNull(temp.getRef());
        Assert.assertNotEquals("", temp.getRef());
        Assert.assertEquals("1AR", temp.getLicense());
        Assert.assertEquals(inicio, temp.getStart());
        Assert.assertEquals(fim, temp.getEnd());
        String ref = temp.getRef();
        Assert.assertNotNull(empresa.getRenting(ref));
        Assert.assertEquals(temp, empresa.getRenting(ref));
    }

    @Test(expected = CarException.class)
    public void FAQUpdate(){
        LocalDate inicio = LocalDate.parse("2018-09-19");
        LocalDate fim = LocalDate.parse("2018-09-15");
        Vehicle teste = new Car("01-58-FK", 100, empresa);
        teste.reserve(inicio, fim, "8JR");
    }

    @Test(expected = RentACarException.class)
    public void nullRent(){
        empresa.getRenting(null);
    }

    @Test
    public void getRentingData(){
        Vehicle porra = new Car("01-58-FK", 100, empresa);
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate fim = LocalDate.parse("2018-09-15");
        Renting temp = porra.reserve(inicio, fim, "1AR");
        RentingData result = empresa.getRentingData(temp.getRef());
        Assert.assertNotNull(result);
        Assert.assertEquals(temp.getRef(), result.getReference());
        Assert.assertEquals(temp.getLicense(), result.getDrivingLicense());
        Assert.assertEquals(temp.getStart(), result.getBegin());
        Assert.assertEquals(temp.getEnd(), result.getEnd());
        Assert.assertEquals(porra.getPlate(), result.getPlate());
    }

    @Test(expected = VehicleException.class)
    public void datesConflict(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate fim = LocalDate.parse("2018-09-15");
        charuto.reserve(inicio, fim, "1AR");
        charuto.reserve(inicio, fim, "2PE");
    }

    @Test(expected = RentingException.class)
    public void pastDate(){
        LocalDate inicio = LocalDate.parse("2017-09-10");
        LocalDate fim = LocalDate.parse("2018-09-15");
        mota.reserve(inicio, fim, "6TY");
    }

    @Test
    public void severalDatesCase(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate fim = LocalDate.parse("2018-09-15");
        LocalDate inicio2 = LocalDate.parse("2018-09-19");
        LocalDate fim2 = LocalDate.parse("2018-09-25");
        carro.reserve(inicio, fim, "1TP");
        carro.reserve(inicio2, fim2, "4TL");
    }

    @Test
    public void checkForRenting(){
        LocalDate inicio = LocalDate.parse("2018-09-10");
        LocalDate fim = LocalDate.parse("2018-09-15");
        Renting temp = carro.reserve(inicio, fim, "1TP");
        Assert.assertNotNull(carro.checkReference(temp.getRef()));
    }

    @After
    public void tearDown(){
        empresa.getVehicleDatabase().clear();
        empresa.RentACarCompanies.clear();
    }
}
