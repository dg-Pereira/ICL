package ast;

import java.util.ArrayList;
import java.util.List;

import compiler.CodeBlock;
import compiler.Environment;
import compiler.Memory;
import exceptions.IdentifierAlreadyDeclaredException;
import exceptions.IdentifierNotDeclaredException;
import exceptions.StaticTypingException;
import exceptions.TypeMismatchException;
import types.IType;
import types.TypeFun;
import values.IValue;
import values.VFun;

public class ASTCall extends ASTNode {

	private ASTNode fun;
	private final List<ASTNode> args;

	private static final String OP = "call";

	public ASTCall(List<ASTNode> args, ASTNode fun) {
		this.fun = fun;
		this.args = args;
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		VFun f = (VFun) fun.eval(e, m);
		List<IValue> argEvals = new ArrayList<>();
		for (ASTNode arg : args) {
			argEvals.add(arg.eval(e, m));
		}
		return f.call(argEvals, m);
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {

		IType idType = fun.typecheck(e, m);
		if (idType instanceof TypeFun) {
			TypeFun fType = (TypeFun) idType;
			fType.checkArgs(args, e, m);
			type = fType.getType();
			return type;
		} else {
			throw new StaticTypingException();
		}
	}

	public void compile(CodeBlock c) {

	}
}