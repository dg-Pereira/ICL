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

public class ASTBool extends ASTNode {

	private final boolean b;

	public ASTBool(boolean b) {
		this.b = b;
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		return new VBool(b);
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {
		type = new TypeBool();
		return type;
	}

	public void compile(CodeBlock c) {
		if (b)
			c.emit("iconst_1");
		else
			c.emit("iconst_0");
	}

}
