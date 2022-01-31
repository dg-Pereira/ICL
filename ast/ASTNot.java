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

public class ASTNot extends ASTNode {
	private final ASTNode rhs;

	private static final String OP = "!";

	public ASTNot(ASTNode r) {
		rhs = r;
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		IValue v1 = rhs.eval(e, m);
		if (v1 instanceof VBool) {
			VBool v1Bool = (VBool) v1;
			return new VBool(!v1Bool.getVal());
		} else {
			throw new TypeMismatchException(v1.getName(), OP);
		}
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {
		IType t1 = rhs.typecheck(e, m);
		if (t1 instanceof TypeBool) {
			type = new TypeBool();
			return type;
		} else {
			throw new StaticTypingException();
		}
	}

	public void compile(CodeBlock c) {
		rhs.compile(c);
		c.emit("ineg");
	}
}