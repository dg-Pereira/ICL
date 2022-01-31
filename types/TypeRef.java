package types;

import compiler.CodeBlock;

public class TypeRef implements IType {

	private IType refType;

	public TypeRef(IType type) {
		refType = type;
	}

	public IType getRefType() {
		return refType;
	}

	public String getName(CodeBlock c) {
		return c.getReference(this).className();
	}

	public boolean equals(Object other) {
		if (other instanceof TypeRef) {
			TypeRef otherRef = (TypeRef) other;
			return this.refType.equals(otherRef.getRefType());
		} else {
			return false;
		}
	}
}