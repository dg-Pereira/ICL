package ast;

import compiler.CodeBlock;
import compiler.Environment;
import compiler.FrameLocation;
import compiler.Memory;
import exceptions.IdentifierAlreadyDeclaredException;
import exceptions.IdentifierNotDeclaredException;
import exceptions.StaticTypingException;
import exceptions.TypeMismatchException;
import types.IType;
import values.IValue;

public class ASTId extends ASTNode {

	private String id;

	public ASTId(String id) {
		this.id = id;
	}

	public IValue eval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		return e.find(id);
	}

	public IType typecheck(Environment<IType> e, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException {
		type = e.find(id);
		return type;
	}

	public void compile(CodeBlock c) {
		c.emit("aload 4");
		FrameLocation addr;
		try {
			addr = c.findDef(id);
		} catch (IdentifierNotDeclaredException e) {
			addr = null;
			e.printStackTrace();
			System.exit(1);
		}

		compiler.Frame currFrame;

		for (currFrame = c.getCurrentFrame(); currFrame.getParent() != null
				&& currFrame.getFrameN() != addr.destinyFrame; currFrame = currFrame.getParent()) {
			c.emit("getfield Frame_" + currFrame.getFrameN() + "/sl LFrame_" + currFrame.getParent().getFrameN() + ';');
		}

		c.emit("getfield Frame_" + currFrame.getFrameN() + "/v" + addr.loc + " I");
	}
}
