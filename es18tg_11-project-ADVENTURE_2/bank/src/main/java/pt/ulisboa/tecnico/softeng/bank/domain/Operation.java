package pt.ulisboa.tecnico.softeng.bank.domain;

import org.joda.time.LocalDateTime;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.tax.domain.IRS;
import pt.ulisboa.tecnico.softeng.tax.domain.ItemType;

public class Operation {
    private static int counter = 0;
    private final ItemType tipo;

	private final String reference;
	private final Type type;
	private final Account account;
	private final double value;
	private final LocalDateTime time;
	public Operation(Type type, Account account, double value) {
		checkArguments(type, account, value);

		this.reference = account.getBank().getCode() + Integer.toString(++Operation.counter);
		this.type = type;
		this.account = account;
		this.value = value;
		this.time = LocalDateTime.now();
        this.tipo = new ItemType(IRS.getIRS(), reference, 10);

		account.getBank().addLog(this);
	}

    public enum Type {
        DEPOSIT, WITHDRAW
    }

	private void checkArguments(Type type, Account account, double value) {
		if (type == null || account == null || value <= 0) {
			throw new BankException();
		}
	}

	public String getReference() {
		return this.reference;
	}

	public Type getType() {
		return this.type;
	}

	public Account getAccount() {
		return this.account;
	}

	public double getValue() {
		return this.value;
	}

	public LocalDateTime getTime() {
		return this.time;
	}

	public String revert() {
		switch (this.type) {
		case DEPOSIT:
			return this.account.withdraw(this.value);
		case WITHDRAW:
			return this.account.deposit(this.value);
		default:
			throw new BankException();

		}

	}

}
