package types;

import compiler.CodeBlock;

public class TypeBool implements IType {

	public TypeBool() {

	}

	public String getName(CodeBlock c) {
		return "Z";
	}

	public boolean equals(Object other) {
		return other instanceof TypeBool;
	}
}
