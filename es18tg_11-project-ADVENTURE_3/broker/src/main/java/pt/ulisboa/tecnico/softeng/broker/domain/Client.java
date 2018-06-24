package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class Client extends Client_Base { //Mete o extends em todas as classes!!!

	public Client(Broker broker, String IBAN, String NIF, String drivingLicense, int age) {
		checkArguments(broker, IBAN, NIF, drivingLicense, age);
		
		setIBAN(IBAN);
		setNIF(NIF);
		setDrivingLicense(drivingLicense);
		setAge(age);

		broker.addClient(this); //Uma vez que a Fenix Framework tem um metodo com este nome
		//podes deixa-lo exactamente como esta
	}

	private void checkArguments(Broker broker, String IBAN, String NIF, String drivingLicense, int age) {
		if (broker == null || IBAN == null || NIF == null ||
				IBAN.trim().isEmpty() || NIF.trim().isEmpty()) {
			throw new BrokerException();
		}

		if (drivingLicense != null && drivingLicense.trim().isEmpty()) {
			throw new BrokerException();
		}

		if (age < 0) {
			throw new BrokerException();
		}

		if (broker.getClientByNIF(NIF) != null) {
			throw new BrokerException();
		}

		if (broker.drivingLicenseIsRegistered(drivingLicense)) {
			throw new BrokerException();
		}

	}
	
	public void delete() {
		setBroker(null); //Elimina a ligacao com o broker
		for(Adventure adv : getAdventureSet()) {
			adv.delete();
		}
		deleteDomainObject(); //Apaga-se da base de dados
	}
}