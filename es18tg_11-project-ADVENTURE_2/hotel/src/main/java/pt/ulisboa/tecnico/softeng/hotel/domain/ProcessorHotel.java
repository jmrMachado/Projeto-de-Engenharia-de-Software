package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.activity.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.hotel.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ProcessorHotel {
	// important to use a set to avoid double submission of the same booking when it
		// is cancelled while trying to pay or send invoice
		private final Set<Booking> bookingToProcess = new HashSet<>();

		public void submitRenting(Booking booking) {
			this.bookingToProcess.add(booking);
			processInvoices();
		}

		private void processInvoices() {		
			Set<Booking> failedToProcess = new HashSet<>();
			for (Booking booking : this.bookingToProcess) {
				if(!booking.isCancelled()) {
			        if(booking.getPaymentReference() == null) {
						try {
				        	String processPay = BankInterface.processPayment(booking.getIBAN(), booking.getAmount());
				        	booking.setPaymentReference(processPay);
				        }
					    catch(BankException | RemoteAccessException ex) {	
							failedToProcess.add(booking);
							continue;
					    }
			        }
				InvoiceData invd = new InvoiceData(booking.getHotel().getNIF(),
												   booking.getNIF(),
												   booking.getRoomID(),
												   booking.getAmount(),
												   booking.getArrival());
				try {	        
					String submitIn = TaxInterface.submitInvoice(invd);
					booking.setInvoiceReference(submitIn);
				}catch(TaxException | RemoteAccessException ex) {	
				    failedToProcess.add(booking);
				}
				} else {
					try {
						if (booking.getCancelledRentingReference() == null) {
							booking.setCancelledRentingReference(
									BankInterface.cancelPayment(booking.getPaymentReference()));
						}
						TaxInterface.cancelInvoice(booking.getInvoiceReference());
						booking.setCancelledInvoice(true);
					} catch (BankException | TaxException | RemoteAccessException ex) {
						failedToProcess.add(booking);
					}
				}
				this.bookingToProcess.clear();
				this.bookingToProcess.addAll(failedToProcess);
			}
		}
		
		public void clean() {
			this.bookingToProcess.clear();
		}
}
