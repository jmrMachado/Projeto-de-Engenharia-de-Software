package pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.broker.domain.Adventure;
import pt.ulisboa.tecnico.softeng.broker.domain.Broker;
import pt.ulisboa.tecnico.softeng.broker.domain.BulkRoomBooking;
import pt.ulisboa.tecnico.softeng.broker.domain.Client;

public class BrokerData {
	public static enum CopyDepth {
		SHALLOW, BULKS, CLIENTS
	};

	private String name;
	private String code;
	private String nifAsSeller;
	private String nifAsBuyer;
	private String iban;
	private List<ClientData> clients = new ArrayList<>();
	private List<BulkData> bulks = new ArrayList<>();

	public BrokerData() {
	}

	public BrokerData(Broker broker, CopyDepth depth) {
		this.name = broker.getName();
		this.code = broker.getCode();
		this.nifAsSeller = broker.getNifAsSeller();
		this.nifAsBuyer = broker.getNifAsBuyer();
		this.iban = broker.getIban();

		switch (depth) {
		case CLIENTS:
			for (Client client : broker.getClientSet()) {
				this.clients.add(new ClientData(client));
			}
			break;
		case BULKS:
			for (BulkRoomBooking bulkRoomBooking : broker.getRoomBulkBookingSet()) {
				this.bulks.add(new BulkData(bulkRoomBooking));
			}
			break;
		case SHALLOW:
			break;
		default:
			break;
		}

	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNifAsSeller() {
		return this.nifAsSeller;
	}

	public void setNifAsSeller(String nifAsSeller) {
		this.nifAsSeller = nifAsSeller;
	}

	public String getNifAsBuyer() {
		return this.nifAsBuyer;
	}

	public void setNifAsBuyer(String nifAsBuyer) {
		this.nifAsBuyer = nifAsBuyer;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public List<ClientData> getClients() {
		return this.clients;
	}

	public void setAdventures(List<ClientData> clients) {
		this.clients = clients;
	}

	public List<BulkData> getBulks() {
		return this.bulks;
	}

	public void setBulks(List<BulkData> bulks) {
		this.bulks = bulks;
	}

}
