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
import values.VArg;
import values.VFun;

public class ASTFun extends ASTNode {

	private ASTNode body;
	private List<ASTArg> params;

	private static final String OP = "fun";

	public ASTFun(ASTNode body, List<ASTArg> params) {
		this.body = body;
		this.params = params;
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		List<String> paramNames = new ArrayList<>();
		Environment<IValue> env = e.beginScope();
		for (ASTArg n : params) {
			paramNames.add(((VArg) n.eval(env, m)).show());
		}
		IValue ret = new VFun(paramNames, body, env);
		env.endScope();
		return ret;
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {

		List<IType> argTypes = new ArrayList<>();
		Environment<IType> env = e.beginScope();
		for (ASTNode n : params) {
			argTypes.add(n.typecheck(env, m));
		}
		type = new TypeFun(argTypes, body.typecheck(env, m));
		env.endScope();
		return type;
	}

	public void compile(CodeBlock c) {

	}
}