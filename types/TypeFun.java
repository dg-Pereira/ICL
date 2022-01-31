package types;

import java.util.List;

import ast.ASTNode;
import compiler.CodeBlock;
import compiler.Environment;
import compiler.Memory;
import exceptions.IdentifierAlreadyDeclaredException;
import exceptions.IdentifierNotDeclaredException;
import exceptions.StaticTypingException;

public class TypeFun implements IType {
	private final List<IType> params;
	private final IType type;

	public TypeFun(List<IType> params, IType type) {
		this.params = params;
		this.type = type;
	}

	public IType getType() {
		return type;
	}

	public List<IType> getParams() {
		return params;
	}

	// unused
	public String getName(CodeBlock c) {
		return null;
	}

	public void checkArgs(List<ASTNode> args, Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {
		if (args.size() != params.size()) {
			throw new StaticTypingException();
		}

		for (int i = 0; i < params.size(); i++) {
			if (!args.get(i).typecheck(e, m).equals(params.get(i))) {
				throw new StaticTypingException();
			}
		}
	}
}
