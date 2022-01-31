package exceptions;

public class IdentifierNotDeclaredException extends Exception {

	private static final long serialVersionUID = 1L;

	public IdentifierNotDeclaredException(String id) {
		super("Identifier " + id + " has not been declared.");
	}

}
