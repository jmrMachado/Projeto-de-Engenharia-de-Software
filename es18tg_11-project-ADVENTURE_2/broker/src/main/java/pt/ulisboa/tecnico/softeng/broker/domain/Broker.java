package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;

import java.util.HashSet;
import java.util.Set;

public class Broker {
	private static Logger logger = LoggerFactory.getLogger(Broker.class);

	public static Set<Broker> brokers = new HashSet<>();

	private final String code;
	private final String name;
	private final String COMPRADOR;
	private final String VENDEDOR;
	private final String IBAN;
	private final Set<Adventure> adventures = new HashSet<>();
	private final Set<BulkRoomBooking> bulkBookings = new HashSet<>();

	public Broker(String code, String name, String seller, String buyer, String IBAN) {
		checkCode(code);
		checkName(name);
		checkBuyerAndSeller(buyer, seller);
		
		this.code = code;
		this.name = name;
		this.COMPRADOR = buyer;
		this.VENDEDOR = seller;
		this.IBAN = IBAN;
		Broker.brokers.add(this);
	}

	private void checkBuyerAndSeller(String buyer, String seller) {
	    if (buyer == null || seller == null || buyer.trim().equals("") || seller.trim().equals("")){
	        throw new BrokerException("Either Buyer or Seller NIFs are Null!");
        }
		if (IRS.getIRS().getTaxPayerByNIF(buyer) == null){
			throw new BrokerException("This buyer does not exist");
		}
		if (IRS.getIRS().getTaxPayerByNIF(seller) == null){
		    throw new BrokerException("This seller does not exist!");
        }

        for (Broker it : brokers){
	        if (it.getBuyerNIF().equals(buyer) || it.getSellerNIF().equals(buyer)){
	            throw new BrokerException("Buyer Not Unique");
            }
            if (it.getSellerNIF().equals(seller) || it.getBuyerNIF().equals(seller)){
	            throw new BrokerException("Seller not Unique");
            }
        }
	}

	private void checkCode(String code) {
		if (code == null || code.trim().length() == 0) {
			throw new BrokerException();
		}

		for (Broker broker : Broker.brokers) {
			if (broker.getCode().equals(code)) {
				throw new BrokerException();
			}
		}
	}

	private void checkName(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new BrokerException();
		}
	}

	String getCode() {
		return this.code;
	}

	String getName() {
		return this.name;
	}

	String getBuyerNIF(){
	    return COMPRADOR;
    }

    String getSellerNIF(){
	    return VENDEDOR;
    }

    String getIBAN(){
		return IBAN;
	}
	
	public int getNumberOfAdventures() {
		return this.adventures.size();
	}

	public void addAdventure(Adventure adventure) {
		this.adventures.add(adventure);
	}

	public boolean hasAdventure(Adventure adventure) {
		return this.adventures.contains(adventure);
	}

	public void bulkBooking(String IBAN, String NIF, int number, LocalDate arrival, LocalDate departure) {
		BulkRoomBooking bulkBooking = new BulkRoomBooking(IBAN, NIF, number, arrival, departure);
		this.bulkBookings.add(bulkBooking);
		bulkBooking.processBooking();
	}

}
