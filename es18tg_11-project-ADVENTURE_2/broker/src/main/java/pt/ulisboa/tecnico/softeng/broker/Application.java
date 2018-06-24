package pt.ulisboa.tecnico.softeng.broker;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure;
import pt.ulisboa.tecnico.softeng.broker.domain.Broker;
import pt.ulisboa.tecnico.softeng.car.domain.Car;
import pt.ulisboa.tecnico.softeng.car.domain.RentACar;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.tax.domain.Buyer;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;
import pt.ulisboa.tecnico.softeng.tax.domain.Seller;

public class Application {

    private static final String CODE = "BR01";
    private static final String NAME_OF_BROKER = "WeExplore";
    private static final String NIF_AS_BUYER = "111111111";
    private static final String NIF_AS_SELLER = "222222222";
    private static final String ADDRESS_OF_BROKER = "AddressOfBroker";

    // Adventure and Client
    private static final String NIF_OF_CLIENT = "333333333";
    private static final String DRIVING_LICENSE = "IMT1234";
    private static final int AGE = 25;
    private static final String ADDRESS_OF_CLIENT = "AddressOfClient";
    private static final String NAME_OF_CLIENT = "NameOfClient";
    private static final double MARGIN = 0.3;
    private static final LocalDate begin = new LocalDate(2016, 12, 19);
    private static final LocalDate end = new LocalDate(2016, 12, 21);

    // Activity
    private static final String NAME_OF_PROVIDER = "ExtremeAdventure";
    private static final String NIF_OF_PROVIDER = "444444444";
    private static final int CAPACITY = 25;
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 75;
    private static final int ACTIVITY_COST = 30;

    // Hotel
    private static final String NAME_OF_HOTEL = "Hotel Lisboa";
    private static final String NIF_OF_HOTEL = "555555555";
    private static final double PRICE_SINGLE = 20.0;
    private static final double PRICE_DOUBLE = 30.0;

    // Car
    private static final String NAME_OF_RENT_A_CAR = "Drive Save";
    private static final String NIF_OF_RENT_A_CAR = "666666666";
    private static final String PLATE_OF_CAR = "22-33-HZ";
    private static final int PRICE_OF_CAR = 10;

    static Adventure adventure;

    static Seller brokerAsSeller;
    static Buyer brokerAsBuyer;
    static Buyer clientAsBuyer;
    static Seller providerAsSeller;
    static Seller hotelAsSeller;
    static Seller rentACarAsSeller;

    static Account brokerAccount;
    static Account clientAccount;
    static Account providerAccount;
    static Account hotelAccount;
    static Account rentACarAccount;

	public static void main(String[] args) {
		System.out.println("Adventures!");

        new ItemType(IRS.getIRS(), "SPORT", 10);
        new ItemType(IRS.getIRS(), "HOUSING", 10);
        new ItemType(IRS.getIRS(), "RENTAL", 10);
        new ItemType(IRS.getIRS(), "ADVENTURE", 10);
        brokerAsSeller = new Seller(IRS.getIRS(), NIF_AS_SELLER, NAME_OF_BROKER, ADDRESS_OF_BROKER);
        brokerAsBuyer = new Buyer(IRS.getIRS(), NIF_AS_BUYER, NAME_OF_BROKER, ADDRESS_OF_BROKER);
        clientAsBuyer = new Buyer(IRS.getIRS(), NIF_OF_CLIENT, NAME_OF_CLIENT, ADDRESS_OF_CLIENT);
        providerAsSeller = new Seller(IRS.getIRS(), NIF_OF_PROVIDER, NAME_OF_PROVIDER, "AddressOfProvider");
        hotelAsSeller = new Seller(IRS.getIRS(), NIF_OF_HOTEL, NAME_OF_HOTEL, "AddressOfHotel");
        rentACarAsSeller = new Seller(IRS.getIRS(), NIF_OF_RENT_A_CAR, NAME_OF_RENT_A_CAR, "AddressOfRentACar");

        // bank
        Bank bank = new Bank("Money", "BK01");
        pt.ulisboa.tecnico.softeng.bank.domain.Client brokerClient = new pt.ulisboa.tecnico.softeng.bank.domain.Client(
                bank, NAME_OF_BROKER);
        brokerAccount = new Account(bank, brokerClient);

        pt.ulisboa.tecnico.softeng.bank.domain.Client clientClient = new pt.ulisboa.tecnico.softeng.bank.domain.Client(
                bank, NAME_OF_CLIENT);
        clientAccount = new Account(bank, clientClient);
        clientAccount.deposit(1000);

        pt.ulisboa.tecnico.softeng.bank.domain.Client providerClient = new pt.ulisboa.tecnico.softeng.bank.domain.Client(
                bank, NAME_OF_PROVIDER);
        providerAccount = new Account(bank, providerClient);

        pt.ulisboa.tecnico.softeng.bank.domain.Client hotelClient = new pt.ulisboa.tecnico.softeng.bank.domain.Client(
                bank, NAME_OF_HOTEL);
        hotelAccount = new Account(bank, hotelClient);

        pt.ulisboa.tecnico.softeng.bank.domain.Client rentACarClient = new pt.ulisboa.tecnico.softeng.bank.domain.Client(
                bank, NAME_OF_RENT_A_CAR);
        rentACarAccount = new Account(bank, rentACarClient);

        // broker
        Broker broker = new Broker(CODE, NAME_OF_BROKER, brokerAsSeller.getNIF(), brokerAsBuyer.getNIF(),
                brokerAccount.getIBAN());
        adventure = new Adventure(new pt.ulisboa.tecnico.softeng.broker.domain.Client(broker, clientAccount.getIBAN(), clientAsBuyer.getNIF(), DRIVING_LICENSE, AGE), begin, end,
                MARGIN, true);

        // activity
        ActivityProvider provider = new ActivityProvider("XtremX", NAME_OF_PROVIDER, providerAsSeller.getNIF(),
                providerAccount.getIBAN());
        Activity activity = new Activity(provider, "Bush Walking", MIN_AGE, MAX_AGE, CAPACITY);
        new ActivityOffer(activity, begin, end, ACTIVITY_COST);

        // hotel
        Hotel hotel = new Hotel("XPTO123", NAME_OF_HOTEL, hotelAsSeller.getNIF(), hotelAccount.getIBAN(),
                PRICE_SINGLE, PRICE_DOUBLE);
        new Room(hotel, "01", Room.Type.SINGLE);

        // car
        RentACar rentACar = new RentACar(NAME_OF_RENT_A_CAR, rentACarAsSeller.getNIF(),
                rentACarAccount.getIBAN());
        new Car(PLATE_OF_CAR, 10, PRICE_OF_CAR, rentACar);


		adventure.process();
        adventure.process();
        adventure.process();
        adventure.process();

		System.out.println("Your payment reference is " + adventure.getPaymentConfirmation() + " and you have "
                + clientAccount.getBalance() + " euros left in your account");
	}

}
