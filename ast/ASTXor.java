package ast;

import compiler.CodeBlock;
import compiler.Environment;
import compiler.Memory;
import exceptions.IdentifierAlreadyDeclaredException;
import exceptions.IdentifierNotDeclaredException;
import exceptions.StaticTypingException;
import exceptions.TypeMismatchException;
import types.IType;
import types.TypeBool;
import values.IValue;
import values.VBool;

public class ASTXor extends ASTNode {

	private final ASTNode lhs, rhs;

	private static final String OP = "xor";

	public ASTXor(ASTNode l, ASTNode r) {
		lhs = l;
		rhs = r;
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		IValue v1 = lhs.eval(e, m);
		IValue v2 = rhs.eval(e, m);

		if (v1 instanceof VBool && v2 instanceof VBool) {
			VBool v1Bool = (VBool) v1;
			VBool v2Bool = (VBool) v2;

			return new VBool(v1Bool.getVal() ^ v2Bool.getVal());
		} else {
			throw new TypeMismatchException(v1.getName(), v2.getName(), OP);
		}
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {
		IType t1 = lhs.typecheck(e, m);
		IType t2 = rhs.typecheck(e, m);
		if (t1 instanceof TypeBool && t2 instanceof TypeBool) {
			type = new TypeBool();
			return type;
		} else {
			throw new StaticTypingException();
		}
	}

	public void compile(CodeBlock c) {
		lhs.compile(c);
		rhs.compile(c);
		c.emit("ixor");
	}
}
