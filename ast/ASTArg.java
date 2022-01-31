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
import values.VArg;

//type é o tipo que foi fornecido no código
public class ASTArg extends ASTNode {
	private final String name;
	private final IType type;

	public ASTArg(String name, IType type) {
		this.name = name;
		this.type = type;
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		return new VArg(name);
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {
		e.assoc(name, type);
		super.type = type;
		return type;
	}

	@Override
	public void compile(CodeBlock c) {

	}

	public String getName() {
		return name;
	}

	public IType getType() {
		return type;
	}
}
