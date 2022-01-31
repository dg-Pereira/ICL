package ast;

import compiler.CodeBlock;
import compiler.Environment;
import compiler.Memory;
import exceptions.IdentifierAlreadyDeclaredException;
import exceptions.IdentifierNotDeclaredException;
import exceptions.StaticTypingException;
import exceptions.TypeMismatchException;
import types.IType;
import values.IValue;

public class ASTSeq extends ASTNode {

	private final ASTNode lhs;
	private final ASTNode rhs;

	public ASTSeq(ASTNode l, ASTNode r) {
		this.lhs = l;
		this.rhs = r;
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		lhs.eval(e, m);
		return rhs.eval(e, m);
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {
		lhs.typecheck(e, m);
		type = rhs.typecheck(e, m);
		return type;
	}

	public void compile(CodeBlock c) {
		lhs.compile(c);
		rhs.compile(c);
	}

}
