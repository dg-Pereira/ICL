package exceptions;

public class IdentifierAlreadyDeclaredException extends Exception {

	private static final long serialVersionUID = 1L;

	public IdentifierAlreadyDeclaredException(String id) {
		super("Identifier " + id + " has already been declared.");
	}

}
