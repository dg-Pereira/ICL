package compiler;

import values.IValue;
import values.VCell;

public class Memory {

	public Memory() {

	}

	public VCell newC(IValue val) {
		return new VCell(val);
	}

	public IValue get(VCell cell) {
		return cell.getVal();
	}

	public IValue set(VCell cell, IValue val) {
		return cell.setVal(val);
	}
}