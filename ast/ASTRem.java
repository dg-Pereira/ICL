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

public class ASTRem extends ASTNode {

	private static final String OP = "%";

	private ASTNode lhs, rhs;

	public ASTRem(ASTNode l, ASTNode r) {
		lhs = l;
		rhs = r;
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		IValue v1 = lhs.eval(e, m);
		IValue v2 = rhs.eval(e, m);

		if (v1 instanceof VInt && v2 instanceof VInt) {
			VInt v1Int = (VInt) v1;
			VInt v2Int = (VInt) v2;
			return new VInt(v1Int.getVal() % v2Int.getVal());
		} else {
			throw new TypeMismatchException(v1.getName(), v2.getName(), OP);
		}
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {
		IType t1 = lhs.typecheck(e, m);
		IType t2 = rhs.typecheck(e, m);
		if (t1 instanceof TypeInt && t2 instanceof TypeInt) {
			type = new TypeInt();
			return type;
		} else {
			throw new StaticTypingException();
		}
	}

	public void compile(CodeBlock c) {
		lhs.compile(c);
		rhs.compile(c);
		c.emit("irem");
	}
}
