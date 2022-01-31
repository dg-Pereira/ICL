package ast;

import compiler.CodeBlock;
import compiler.Environment;
import compiler.Memory;
import compiler.Reference;
import exceptions.IdentifierAlreadyDeclaredException;
import exceptions.IdentifierNotDeclaredException;
import exceptions.StaticTypingException;
import exceptions.TypeMismatchException;
import types.IType;
import types.TypeRef;
import values.IValue;
import values.VCell;

public class ASTDeref extends ASTNode {

	private ASTNode rhs;

	private static final String OP = "*";

	public ASTDeref(ASTNode r) {
		rhs = r;
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		return m.get((VCell) rhs.eval(e, m));
	}

	public IType typecheck(Environment<IType> env, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {
		IType t1 = rhs.typecheck(env, m);
		if (t1 instanceof TypeRef) {
			type = ((TypeRef) t1).getRefType();
			return type;
		} else {
			throw new StaticTypingException();
		}
	}

	public void compile(CodeBlock c) {
		rhs.compile(c);
		Reference ref = c.getReference((TypeRef) rhs.getType());
		c.emit("checkcast " + ref.className());
		c.emit("getfield " + ref.className() + "/v " + ref.getRefType());
	}
}