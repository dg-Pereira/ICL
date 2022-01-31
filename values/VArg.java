package values;

public class VArg implements IValue {

	private static final String TYPE_NAME = "Argument";

	private final String name;

	public VArg(String name) {
		this.name = name;
	}

	public String show() {
		return name;
	}

	@Override
	public String getName() {
		return TYPE_NAME;
	}

}
