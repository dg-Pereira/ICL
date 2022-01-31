package exceptions;

public class StaticTypingException extends Exception {
	private static final long serialVersionUID = 1L;

	public StaticTypingException() {
		super("The types supplied do not match with the operation");
	}
}
