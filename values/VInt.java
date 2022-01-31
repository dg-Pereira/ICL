package values;

public class VInt implements IValue {

	private static final String TYPE_NAME = "int";

	private int v;

	public VInt(int v0) {
		v = v0;
	}

	public Integer getVal() {
		return Integer.valueOf(v);
	}

	public String getName() {
		return TYPE_NAME;
	}

	public String show() {
		return String.valueOf(v);
	}
}