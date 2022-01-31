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
import types.TypeInt;
import values.IValue;
import values.VBool;
import values.VInt;

public class ASTLessEqual extends ASTNode {

	private static final String OP = "<=";

	private final ASTNode lhs;
	private final ASTNode rhs;

	public ASTLessEqual(ASTNode l, ASTNode r) {
		this.lhs = l;
		this.rhs = r;
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {

		IValue v1 = lhs.eval(e, m);
		IValue v2 = rhs.eval(e, m);

		if (v1 instanceof VInt && v2 instanceof VInt) {
			return new VBool(((VInt) v1).getVal() <= ((VInt) v2).getVal());
		} else {
			throw new TypeMismatchException(v1.getName(), v2.getName(), OP);
		}
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {

		IType t1 = lhs.typecheck(e, m);
		IType t2 = rhs.typecheck(e, m);

		if (t1 instanceof TypeInt && t2 instanceof TypeInt) {
			type = new TypeBool();
			return type;
		} else {
			throw new StaticTypingException();
		}
	}

	public void compile(CodeBlock c) {
		String l1 = "L" + c.getLabel();
		String l2 = "L" + c.getLabel();

		lhs.compile(c);
		rhs.compile(c);

		c.emit("isub");

		c.emit("ifle " + l1);
		c.emit("sipush 0");
		c.emit("goto " + l2);
		c.emit(l1 + ":");
		c.emit("sipush 1");
		c.emit(l2 + ":");
	}
}
