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

public class ASTAssign extends ASTNode {

	private ASTNode lhs, rhs;

	private static final String OP = ":=";

	public ASTAssign(ASTNode l, ASTNode r) {
		lhs = l;
		rhs = r;
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		IValue v1 = lhs.eval(e, m);
		IValue v2 = rhs.eval(e, m);
		if (v1 instanceof VCell) {
			m.set((VCell) v1, v2);
			return v2;
		} else {
			throw new TypeMismatchException(v1.getName(), v2.getName(), OP);
		}
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {
		IType t1 = lhs.typecheck(e, m);
		IType t2 = rhs.typecheck(e, m);
		if (t1 instanceof TypeRef
				&& ((TypeRef) t1).getRefType().getClass().getSimpleName().equals(t2.getClass().getSimpleName())) {
			type = t2;
			return t2;
		} else {
			throw new StaticTypingException();
		}
	}

	public void compile(CodeBlock c) {
		Reference ref = c.getReference((TypeRef) lhs.getType());

		lhs.compile(c);
		rhs.compile(c);
		c.emit("putfield " + ref.className() + "/v " + ref.getRefType());
	}
}