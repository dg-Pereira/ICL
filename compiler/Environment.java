package compiler;

import java.util.HashMap;
import java.util.Map;

import exceptions.IdentifierAlreadyDeclaredException;
import exceptions.IdentifierNotDeclaredException;

public class Environment<Type> {

	private Environment<Type> father;
	private Map<String, Type> bindings;

	public Environment() {
		this.father = null;
		bindings = new HashMap<>();
	}

	public Environment(Environment<Type> father) {
		this.father = father;
		bindings = new HashMap<>();
	}

	public Environment<Type> beginScope() {
		return new Environment<Type>(this);
	}

	public Environment<Type> endScope() {
		return father;
	}

	public void assoc(String id, Type value) throws IdentifierAlreadyDeclaredException {
		if (bindings.containsKey(id)) {
			throw new IdentifierAlreadyDeclaredException(id);
		}

		bindings.put(id, value);
	}

	public Type find(String id) throws IdentifierNotDeclaredException {
		Type val = bindings.get(id);
		if (val != null) {
			return val;
		} else if (father != null) {
			return father.find(id);
		} else {
			throw new IdentifierNotDeclaredException(id);
		}
	}

	public boolean hasDupDef(String id, boolean foundOne) {
		for (Map.Entry<String, Type> entry : bindings.entrySet()) {
			if (entry.getKey().equals(id)) {
				if (foundOne == true) {
					return true;
				}
				foundOne = true;
			}
		}
		if (father != null) {
			return father.hasDupDef(id, foundOne);
		}
		return false;
	}

	public int depth() {
		return 0;
	}
}
