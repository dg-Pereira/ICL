package values;

import java.util.List;

import ast.ASTNode;
import compiler.Environment;
import compiler.Memory;
import exceptions.IdentifierAlreadyDeclaredException;
import exceptions.IdentifierNotDeclaredException;
import exceptions.TypeMismatchException;

public class VFun implements IValue {

	private static final String TYPE_NAME = "Function";

	private final List<String> paramNames;
	private final ASTNode body;
	private Environment<IValue> callEnv;

	public VFun(List<String> paramNames, ASTNode body, Environment<IValue> callEnv) {
		this.paramNames = paramNames;
		this.body = body;
		this.callEnv = callEnv;
	}

	public IValue call(List<IValue> args, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		callEnv = callEnv.beginScope();
		for (int i = 0; i < paramNames.size(); i++) {
			callEnv.assoc(paramNames.get(i), args.get(i));
		}
		IValue result = body.eval(callEnv, m);
		callEnv = callEnv.endScope();
		return result;
	}

	public String getName() {
		return TYPE_NAME;
	}

	public String show() {
		return TYPE_NAME;
	}
}
