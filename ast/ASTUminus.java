package ast;

import compiler.CodeBlock;
import compiler.Environment;
import compiler.Memory;
import exceptions.IdentifierAlreadyDeclaredException;
import exceptions.IdentifierNotDeclaredException;
import exceptions.StaticTypingException;
import exceptions.TypeMismatchException;
import types.IType;
import types.TypeInt;
import values.IValue;
import values.VInt;

public class ASTUminus extends ASTNode {

	private static final String OP = "-";

	ASTNode rhs;

	public ASTUminus(ASTNode r) {
		rhs = r;
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		IValue v1 = rhs.eval(e, m);
		if (v1 instanceof VInt) {
			VInt v1Int = (VInt) v1;
			return new VInt(-v1Int.getVal());
		} else {
			throw new TypeMismatchException(v1.getName(), OP);
		}
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {

		IType t1 = rhs.typecheck(e, m);
		if (t1 instanceof TypeInt) {
			type = new TypeInt();
			return type;
		} else {
			throw new StaticTypingException();
		}
	}

	public void compile(CodeBlock c) {
		ASTNode zero = new ASTNum(0);
		zero.compile(c);
		rhs.compile(c);
		c.emit("isub");
	}
}
