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

public class ASTIf extends ASTNode {

	private static final String OP = "if";

	private final ASTNode cond;
	private final ASTNode thenBranch;

	public ASTIf(ASTNode cond, ASTNode thenBranch) {
		this.cond = cond;
		this.thenBranch = thenBranch;
	}

	//se não cumprir a condição devolve um VBool com false.
	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {

		IValue condEval = cond.eval(e, m);

		if (!(condEval instanceof VBool)) {
			throw new TypeMismatchException(condEval.getName(), OP);
		}

		if (((VBool) condEval).getVal()) {
			return thenBranch.eval(e, m);
		} else {
			return condEval;
		}
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {

		IType t1 = cond.typecheck(e, m);
		if (t1 instanceof TypeBool) {
			type = thenBranch.typecheck(e, m);
			return type;
		} else {
			throw new StaticTypingException();
		}
	}

	public void compile(CodeBlock c) {
		String falseLabel = "L" + c.getLabel();
		cond.compile(c);
		c.emit("ifeq " + falseLabel);
		thenBranch.compile(c);
		c.emit(falseLabel + ":");
	}
}
