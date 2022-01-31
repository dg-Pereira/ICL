package values;

public class VCell implements IValue {

	private static final String TYPE_NAME = "Memory Cell";

	private IValue val;

	public VCell(IValue val) {
		this.val = val;
	}

	public IValue getVal() {
		return val;
	}

	public IValue setVal(IValue val) {
		return this.val = val;
	}

	public String getName() {
		return TYPE_NAME;
	}

	public String show() {
		return TYPE_NAME;
	}
}