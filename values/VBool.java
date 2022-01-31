package values;

public class VBool implements IValue {

	private static final String TYPE_NAME = "Boolean";

	private boolean v;

	public VBool(boolean v0) {
		this.v = v0;
	}

	public String getName() {
		return TYPE_NAME;
	}

	public boolean getVal() {
		return v;
	}

	public String show() {
		return String.valueOf(v);
	}
}