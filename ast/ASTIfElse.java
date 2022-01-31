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

public class ASTIfElse extends ASTNode {

	private static final String OP = "if else";

	private final ASTNode cond;
	private final ASTNode thenBranch;
	private final ASTNode elseBranch;

	public ASTIfElse(ASTNode cond, ASTNode thenBranch, ASTNode elseBranch) {
		this.cond = cond;
		this.thenBranch = thenBranch;
		this.elseBranch = elseBranch;
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {

		IValue condEval = cond.eval(e, m);

		if (!(condEval instanceof VBool)) {
			throw new TypeMismatchException(condEval.getName(), OP);
		}

		if (((VBool) condEval).getVal()) {
			return thenBranch.eval(e, m);
		} else {
			return elseBranch.eval(e, m);
		}
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {

		IType t1 = cond.typecheck(e, m);
		if (t1 instanceof TypeBool) {
			IType t2 = thenBranch.typecheck(e, m);
			IType t3 = elseBranch.typecheck(e, m);
			if (!t2.getClass().getName().equals(t3.getClass().getName())) {
				throw new StaticTypingException();
			}
			type = t2;
			return t2;
		} else {
			throw new StaticTypingException();
		}

	}

	public void compile(CodeBlock c) {
		String falseLabel = "L" + c.getLabel();
		String trueLabel = "L" + c.getLabel();
		cond.compile(c);
		c.emit("ifeq " + falseLabel);
		thenBranch.compile(c);
		c.emit("goto " + trueLabel);
		c.emit(falseLabel + ":");
		elseBranch.compile(c);
		c.emit(trueLabel + ":");
	}
}
