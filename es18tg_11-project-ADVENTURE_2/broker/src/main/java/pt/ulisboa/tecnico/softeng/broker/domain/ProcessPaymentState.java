package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.TaxInterface;
import pt.ulisboa.tecnico.softeng.tax.dataobjects.InvoiceData;
import pt.ulisboa.tecnico.softeng.tax.exception.TaxException;

public class ProcessPaymentState extends AdventureState {
	public static final int MAX_REMOTE_ERRORS = 3;

	@Override
	public State getState() {
		return State.PROCESS_PAYMENT;
	}

	@Override
	public void process(Adventure adventure) {
		try {
            adventure.setPaymentConfirmation(BankInterface.processPayment(adventure.getPessoa().getIBAN(), (int) adventure.getFinalprice()));
            TaxInterface.submitInvoice(
                    new InvoiceData(
                            adventure.getBroker().getSellerNIF(), adventure.getPessoa().getNIF(), adventure.getPaymentConfirmation(), adventure.getFinalprice(),
                            new LocalDate(adventure.getEnd().getYear(), adventure.getEnd().getMonthOfYear(), adventure.getEnd().getDayOfMonth())));
		} catch (BankException | NullPointerException | TaxException e) {
			adventure.setState(State.CANCELLED);
			return;
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == MAX_REMOTE_ERRORS) {
				adventure.setState(State.CANCELLED);
			}
			return;
		}

        adventure.setState(State.CONFIRMED);
	}

}
