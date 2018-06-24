package pt.ulisboa.tecnico.softeng.broker.domain;

import mockit.Delegate;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.CarInterface;
import pt.ulisboa.tecnico.softeng.car.exception.CarException;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;

@RunWith(JMockit.class)
public class RentVehicleStateMethodTest {
    private static final String IBAN = "BK01987654321";
    private static final int AMOUNT = 300;
    private static final int AGE = 20;
    private static final String VEHICLE_CONFIRMATION = "VehicleConfirmation";
    private static final LocalDate arrival = new LocalDate(2016, 12, 19);
    private static final LocalDate departure = new LocalDate(2016, 12, 21);
    private Adventure adventure;
    private Buyer comprador = new Buyer(IRS.getIRS(), "123456789", "Pedro", "Rua");
    private Seller vendedor = new Seller(IRS.getIRS(), "987654321", "Pedro", "Rua");

    @Injectable
    private Broker broker = new Broker("BR01", "eXtremeADVENTURE", "123456789", "987654321", IBAN);
    private Client temp = new Client(broker, IBAN, "123456789", "6TG", AGE);

    @Before
    public void setUp() {
        this.adventure = new Adventure(temp, arrival, departure, AMOUNT, true);
        this.adventure.setState(Adventure.State.RENT_VEHICLE);
    }

    @Test
    public void successRentACar(@Mocked final CarInterface carInterface) {
        new Expectations() {
            {
                CarInterface.reserveVehicle(CarInterface.VehicleType.CAR, this.anyString, this.anyString, temp.getLICENSE(), arrival, departure);
                this.result = VEHICLE_CONFIRMATION;
            }
        };

        this.adventure.process();

        Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventure.getState());
    }

    @Test
    public void carException(@Mocked final CarInterface carInterface) {
        new Expectations() {
            {
                CarInterface.reserveVehicle(CarInterface.VehicleType.CAR, this.anyString, this.anyString, temp.getLICENSE(), arrival, departure);
                this.result = new CarException();
            }
        };

        this.adventure.process();

        Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
    }

    @Test
    public void singleRemoteAccessException(@Mocked final CarInterface carInterface) {
        new Expectations() {
            {
                CarInterface.reserveVehicle(CarInterface.VehicleType.CAR, this.anyString, this.anyString, temp.getLICENSE(), arrival, departure);
                this.result = new RemoteAccessException();
            }
        };

        this.adventure.process();

        Assert.assertEquals(Adventure.State.RENT_VEHICLE, this.adventure.getState());
    }

    @Test
    public void maxRemoteAccessException(@Mocked final CarInterface carInterface) {
        new Expectations() {
            {
                CarInterface.reserveVehicle(CarInterface.VehicleType.CAR, this.anyString, this.anyString, temp.getLICENSE(), arrival, departure);
                this.result = new RemoteAccessException();
                this.times = BookRoomState.MAX_REMOTE_ERRORS;
            }
        };

        for (int i = 0; i < RentVehicleState.MAX_REMOTE_ERRORS; i++) {
            this.adventure.process();
        }

        Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
    }

    @Test
    public void maxMinusOneRemoteAccessException(@Mocked final CarInterface carInterface) {
        new Expectations() {
            {
                CarInterface.reserveVehicle(CarInterface.VehicleType.CAR, this.anyString, this.anyString, temp.getLICENSE(), arrival, departure);
                this.result = new RemoteAccessException();
                this.times = BookRoomState.MAX_REMOTE_ERRORS - 1;
            }
        };

        for (int i = 0; i < RentVehicleState.MAX_REMOTE_ERRORS - 1; i++) {
            this.adventure.process();
        }

        Assert.assertEquals(Adventure.State.RENT_VEHICLE, this.adventure.getState());
    }

    @Test
    public void fiveRemoteAccessExceptionOneSuccess(@Mocked final CarInterface carInterface) {
        new Expectations() {
            {
                CarInterface.reserveVehicle(CarInterface.VehicleType.CAR, this.anyString, this.anyString, temp.getLICENSE(), arrival, departure);
                this.result = new Delegate() {
                    int i = 0;

                    public String delegate() {
                        if (this.i < 5) {
                            this.i++;
                            throw new RemoteAccessException();
                        } else {
                            return VEHICLE_CONFIRMATION;
                        }
                    }
                };
                this.times = 6;
            }
        };

        this.adventure.process();
        this.adventure.process();
        this.adventure.process();
        this.adventure.process();
        this.adventure.process();
        this.adventure.process();

        Assert.assertEquals(Adventure.State.PROCESS_PAYMENT, this.adventure.getState());
    }

    @Test
    public void oneRemoteAccessExceptionOneActivityException(@Mocked final CarInterface carInterface) {
        new Expectations() {
            {
                CarInterface.reserveVehicle(CarInterface.VehicleType.CAR, this.anyString, this.anyString, temp.getLICENSE(), arrival, departure);
                this.result = new Delegate() {
                    int i = 0;

                    public String delegate() {
                        if (this.i < 1) {
                            this.i++;
                            throw new RemoteAccessException();
                        } else {
                            throw new CarException();
                        }
                    }
                };
                this.times = 2;
            }
        };

        this.adventure.process();
        this.adventure.process();

        Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
    }

    @After
    public void tearDown(){
        Broker.brokers.clear();
        IRS.getIRS().clearAll();
    }
}
