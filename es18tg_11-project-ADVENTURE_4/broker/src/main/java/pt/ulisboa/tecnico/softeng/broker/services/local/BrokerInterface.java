package pt.ulisboa.tecnico.softeng.broker.services.local;

import java.util.ArrayList;
import java.util.List;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.ClientData;
import pt.ulisboa.tecnico.softeng.broker.domain.Client;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure;
import pt.ulisboa.tecnico.softeng.broker.domain.Broker;
import pt.ulisboa.tecnico.softeng.broker.domain.BulkRoomBooking;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.AdventureData;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BrokerData;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BrokerData.CopyDepth;
import pt.ulisboa.tecnico.softeng.broker.services.local.dataobjects.BulkData;

public class BrokerInterface {

	@Atomic(mode = TxMode.READ)
	public static List<BrokerData> getBrokers() {
		List<BrokerData> brokers = new ArrayList<>();
		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			brokers.add(new BrokerData(broker, CopyDepth.SHALLOW));
		}
		return brokers;
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createBroker(BrokerData brokerData) {
		new Broker(brokerData.getCode(), brokerData.getName(), brokerData.getNifAsSeller(), brokerData.getNifAsBuyer(),
				brokerData.getIban());
	}

	@Atomic(mode = TxMode.READ)
	public static BrokerData getBrokerDataByCode(String brokerCode, CopyDepth depth) {
		Broker broker = getBrokerByCode(brokerCode);

		if (broker != null) {
			return new BrokerData(broker, depth);
		} else {
			return null;
		}
	}
	
	@Atomic(mode = TxMode.WRITE)
	public static void createClient(String code, ClientData client) {
		Broker broker = getBrokerByCode(code);
		if (broker== null) {
			throw new BrokerException();
		}

		new Client(broker, client.getIban(), client.getNif(), client.getDrivingLicense(), client.getAge());
	}
	
	@Atomic(mode = TxMode.READ)
	public static ClientData getClientDataByNif(String code, String nif) {
		Broker broker = getBrokerByCode(code);
		if (broker== null) {
			return null;
		}

		Client client = broker.getClientByNIF(nif);
		if (client == null) {
			return null;
		}

		return new ClientData(client);
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createAdventure(String brokerCode, String nif, AdventureData adventureData) {
		
		if(adventureData.getBegin()==null) {
			throw new BrokerException();
		}
		
		if(adventureData.getEnd()==null) {
			throw new BrokerException();
		}
		
		if(adventureData.getAge()==null) {
			throw new BrokerException();
		}
		
		if(adventureData.getAmount() == null) {
			throw new BrokerException();
		}
		
		new Adventure(adventureData.getBegin(), adventureData.getEnd(), getBrokerByCode(brokerCode).getClientByNIF(nif), adventureData.getAmount(), 0.1);
	}

	@Atomic(mode = TxMode.WRITE)
	public static void createBulkRoomBooking(String brokerCode, BulkData bulkData) {
		
		if(bulkData.getArrival()==null) {
			throw new BrokerException();
		}
		
		if(bulkData.getDeparture()==null) {
			throw new BrokerException();
		}
		
		if(bulkData.getBuyerIban()==null) {
			throw new BrokerException();
		}
		
		if(bulkData.getBuyerIban() == null) {
			throw new BrokerException();
		}
		
		new BulkRoomBooking(getBrokerByCode(brokerCode), bulkData.getNumber() != null ? bulkData.getNumber() : 0,
				bulkData.getArrival(), bulkData.getDeparture(),bulkData.getBuyerNif(), bulkData.getBuyerNif());

	}

	private static Broker getBrokerByCode(String code) {
		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			if (broker.getCode().equals(code)) {
				return broker;
			}
		}
		throw new BrokerException();
	}

}
