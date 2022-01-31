package exceptions;

public class TypeMismatchException extends Exception {

	private static final long serialVersionUID = 1L;

	public TypeMismatchException() {
		super("The types supplied do not match with the operation");
	}

	public TypeMismatchException(String type1, String op) {
		super("The type " + type1 + "does not match with the operation " + op + ".");
	}

	public TypeMismatchException(String type1, String type2, String op) {
		super("The types " + type1 + " and " + type2 + "do not match with the operation " + op + ".");
	}

	public TypeMismatchException(String type1, String type2, String type3, String op) {
		super("The types " + type1 + ", " + type2 + " and " + type3 + "do not match with the operation " + op + ".");
	}
}
