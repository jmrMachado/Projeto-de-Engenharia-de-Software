package pt.ulisboa.tecnico.softeng.tax.services.remote.exceptions;

public class RemoteAccessException extends RuntimeException {
	public RemoteAccessException() {
		super();
	}

	public RemoteAccessException(String message) {
		super(message);
	}
}
