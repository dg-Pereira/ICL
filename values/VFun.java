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

	private final List<String> argNames;
	private final ASTNode body;
	private Environment<IValue> callEnv;

	public VFun(List<String> argNames, ASTNode body, Environment<IValue> callEnv) {
		this.argNames = argNames;
		this.body = body;
		this.callEnv = callEnv;
	}

	public IValue call(List<IValue> args, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		callEnv = callEnv.beginScope();
		for (int i = 0; i < argNames.size(); i++) {
			callEnv.assoc(argNames.get(i), args.get(i));
		}
		IValue result = body.eval(callEnv, m);
		callEnv = callEnv.endScope();
		return result;
	}

	@Override
	public String getName() {
		return TYPE_NAME;
	}

	public String show() {
		return TYPE_NAME;
	}
}
