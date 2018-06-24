package pt.ulisboa.tecnico.softeng.broker;


import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure;
import pt.ulisboa.tecnico.softeng.broker.domain.Broker;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;



public class Application {


	public static void main(String[] args) {
		System.out.println("Adventures!");

		Bank bank = new Bank("MoneyPlus", "BK01");
		Account account = new Account(bank, new Client(bank, "José dos Anzóis"));
		account.deposit(1000);

		Broker broker = new Broker("BR01", "Fun");
		Adventure adventure = new Adventure(broker, new LocalDate(), new LocalDate(), 33, account.getIBAN(), 50);

		adventure.process();

		System.out.println("Your payment reference is " + adventure.getPaymentConfirmation() + " and you have "
				+ account.getBalance() + " euros left in your account");
		
		//1 Hotel
		Hotel hotel = new Hotel("1234567","Pestana"); //criado corretamente
		
		//Room
		Type tipo = Type.SINGLE;
		Room room = new Room (hotel,"30",tipo); //criado corretamente
		
		//Booking
		LocalDate hoje = new LocalDate();//LocalDate.now();
		LocalDate amanha = new LocalDate();//hoje.plusDays(1);
		
		Booking book = new Booking(hotel,hoje,amanha);
		
		//RoomBookingData bookdata = new RoomBookingData (room,book);
		
		System.out.println("Manda vir hotel " + hotel.getCode() + " + " + hotel.getName());
		System.out.println("Manda vir room " + room.getNumber() + " + " + room.getHotel()
		+ " + " +  room.getType());
		System.out.println("Data " + hoje + " + " + amanha + " + " + book.getReference());
		//System.out.println("Manda vir bookdata " + bookdata.getHotelCode() + " + " + bookdata.getReference());
		
		
		
	}

}
