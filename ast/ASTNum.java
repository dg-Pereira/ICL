package ast;

import compiler.CodeBlock;
import compiler.Environment;
import compiler.Memory;
import exceptions.IdentifierAlreadyDeclaredException;
import exceptions.IdentifierNotDeclaredException;
import exceptions.TypeMismatchException;
import types.IType;
import types.TypeInt;
import values.IValue;
import values.VInt;

public class ASTNum extends ASTNode {

	int val;

	public ASTNum(int n) {
		val = n;
	}

	public int eval() {
		return val;
	}

	public IType typecheck(Environment<IType> e, Memory m) {
		type = new TypeInt();
		return type;
	}

	public void compile(CodeBlock c) {
		c.emit("sipush " + val);
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		return new VInt(val);
	}
}
