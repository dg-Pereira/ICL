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

public class ASTPrint extends ASTNode {

	private ASTNode body;

	private static final String OP = "print";

	public ASTPrint(ASTNode body) {
		this.body = body;
	}

	@Override
	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		IValue v1 = body.eval(e, m);
		System.out.print(v1.show());
		return v1;
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {
		type = body.typecheck(e, m);
		return type;
	}

	public void compile(CodeBlock c) {
		c.emit("getstatic java/lang/System/out Ljava/io/PrintStream;");
		body.compile(c);
		c.emit("invokestatic java/lang/String/valueOf(I)Ljava/lang/String;");
		c.emit("invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V");
	}
}