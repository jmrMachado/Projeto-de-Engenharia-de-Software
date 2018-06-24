package pt.ulisboa.tecnico.softeng.car.domain;

import pt.ulisboa.tecnico.softeng.activity.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.car.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.car.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

import java.util.HashSet;
import java.util.Set;

public class ProcessorCar {
	// important to use a set to avoid double submission of the same booking when it
	// is cancelled while trying to pay or send invoice
	private final Set<Renting> rentingToProcess = new HashSet<>();

	public void submitRenting(Renting renting) {
		this.rentingToProcess.add(renting);
		processInvoices();
	}

	private void processInvoices() {		
		Set<Renting> failedToProcess = new HashSet<>();
		for (Renting renting : this.rentingToProcess) {
			if(!renting.isCancelled()) {
		        if(renting.getPayRef() == null) {
					try {
			        	String processPay = BankInterface.processPayment(renting.getIBAN(), 
			        			renting.getVehicle().getPreco() * (renting.getEnd().getDayOfYear() - renting.getBegin().getDayOfYear()));
			        	renting.setPayRef(processPay);
			        }
				    catch(BankException | RemoteAccessException ex) {	
						failedToProcess.add(renting);
						continue;
				    }
		        }
			    InvoiceData invd = new InvoiceData(renting.getVehicle().getRentACar().getNif(),
			        							   renting.getNIF(),
			        							   renting.getVehicle().getItemType().getName(),
			        							   renting.getVehicle().getPreco(),
			        							   renting.getBegin());
			   try {	        
				   String submitIn = TaxInterface.submitInvoice(invd);
				   renting.setInvRef(submitIn);
			   }catch(TaxException | RemoteAccessException ex) {	
				   failedToProcess.add(renting);
			   }
			} else {
				try {
					if (renting.getCancelledRentingReference() == null) {
						renting.setCancelledRentingReference(
								BankInterface.cancelPayment(renting.getPayRef()));
					}
					TaxInterface.cancelInvoice(renting.getInvRef());
					renting.setCancelledInvoice(true);
				} catch (BankException | TaxException | RemoteAccessException ex) {
					failedToProcess.add(renting);
				}
			}
			this.rentingToProcess.clear();
			this.rentingToProcess.addAll(failedToProcess);
		}
	}
	
	public void clean() {
		this.rentingToProcess.clear();
	}

}
