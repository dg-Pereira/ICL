package types;

import java.util.Iterator;
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

	public Iterator<IType> getParams() {
		return params.iterator();
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

	// se other não for função é false
	// se o tipo de retorno de other não for o mesmo que o de this é false
	// se o tipo de algum parametro for diferente é false
	// se o numero de parametros for diferente é false
	// se passar estes checks todos é true
	public boolean equals(Object other) {
		if (other instanceof TypeFun) {

			TypeFun otherFun = (TypeFun) other;

			if (!type.equals(otherFun.getType())) {
				return false;
			}

			Iterator<IType> it = otherFun.getParams();
			int i = 0;
			IType curr = null;
			while (it.hasNext()) {
				curr = it.next();
				if (!params.get(i++).equals(curr)) {
					return false;
				}
			}
			if (i != params.size()) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
}
