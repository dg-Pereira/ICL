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

public class ASTNew extends ASTNode {

	private ASTNode body;

	public ASTNew(ASTNode body) {
		this.body = body;
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		return m.newC(body.eval(e, m));
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {
		IType t1 = new TypeRef(body.typecheck(e, m));
		type = t1;
		return t1;
	}

	public void compile(CodeBlock c) {
		Reference ref = c.getReference((TypeRef) getType());

		c.emit("new " + ref.className());
		c.emit("dup");
		c.emit("invokespecial " + ref.className() + "/<init>()V");
		c.emit("dup");
		body.compile(c);
		c.emit("putfield " + ref.className() + "/v " + ref.getRefType());
	}
}