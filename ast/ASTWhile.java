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

public class ASTWhile extends ASTNode {

	private ASTNode cond, body;

	private static final String OP = "while";

	public ASTWhile(ASTNode cond, ASTNode body) {
		this.cond = cond;
		this.body = body;
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		IValue condEval = cond.eval(e, m);
		if (!(condEval instanceof VBool)) {
			throw new TypeMismatchException(condEval.getName(), OP);
		}

		if (((VBool) condEval).getVal()) {
			body.eval(e, m);
			return eval(e, m);
		} else {
			return condEval;
		}
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {
		IType t1 = cond.typecheck(e, m);
		if (!(t1 instanceof TypeBool)) {
			throw new StaticTypingException();
		}

		body.typecheck(e, m);
		type = new TypeBool();
		return new TypeBool();
	}

	public void compile(CodeBlock c) {
		int l1 = c.getLabel();
		int l2 = c.getLabel();
		c.emit("L" + l1 + ":");
		cond.compile(c);
		c.emit("ifeq L" + l2);
		body.compile(c);
		c.emit("goto L" + l1);
		c.emit("L" + l2 + ":");
	}
}
