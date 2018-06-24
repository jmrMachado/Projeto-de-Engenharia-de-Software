package pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects;

import java.util.List;
import java.util.stream.Collectors;
import pt.ulisboa.tecnico.softeng.broker.domain.Client;

public class ClientData {
	private String iban;
	private String nif;
	private String drivingLicense;
	private int age;
	private String brokerCode;
	private String brokerName;
	private List<AdventureData> adventures;

	public ClientData() {
	}

	public ClientData(Client client) {
		this.brokerCode = client.getBroker().getCode();
		this.brokerName = client.getBroker().getName();
		this.iban = client.getIban();
		this.nif = client.getNif();
		this.drivingLicense = client.getDrivingLicense();
		this.age = client.getAge();
		
		this.adventures= client.getAdventureSet().stream().sorted((a1, a2) -> a1.getID().compareTo(a2.getID()))
				.map(a -> new AdventureData(a)).collect(Collectors.toList());
	}
	
	public String getBrokerCode() {
		return this.brokerCode;
	}
	
	public void setBrokerCode(String brokerCode) {
		this.brokerCode = brokerCode;
	}
	
	public String getBrokerName() {
		return this.brokerName;
	}
	
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	public String getIban() {
		return this.iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getNif() {
		return this.nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getDrivingLicense() {
		return this.drivingLicense;
	}

	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public List<AdventureData> getAdventures() {
		return this.adventures;
	}

	public void setAdventures(List<AdventureData> adventures) {
		this.adventures = adventures;
	}
}
