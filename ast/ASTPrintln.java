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

public class ASTPrintln extends ASTNode {

	private ASTNode body;

	private static final String OP = "println";

	public ASTPrintln(ASTNode body) {
		this.body = body;
	}

	@Override
	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		IValue v1 = body.eval(e, m);
		System.out.println(v1.show());
		return v1;
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {
		IType t1 = body.typecheck(e, m);
		type = t1;
		return t1;
	}

	public void compile(CodeBlock c) {
		c.emit("getstatic java/lang/System/out Ljava/io/PrintStream;");
		body.compile(c);
		c.emit("invokestatic java/lang/String/valueOf(I)Ljava/lang/String;");
		c.emit("invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V");
	}
}