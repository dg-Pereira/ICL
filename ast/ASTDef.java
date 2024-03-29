package ast;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import compiler.CodeBlock;
import compiler.DeclList;
import compiler.Environment;
import compiler.Frame;
import compiler.Memory;
import compiler.Reference;
import exceptions.IdentifierAlreadyDeclaredException;
import exceptions.IdentifierNotDeclaredException;
import exceptions.StaticTypingException;
import exceptions.TypeMismatchException;
import types.IType;
import values.IValue;

public class ASTDef extends ASTNode {

	private ASTNode body;
	private Map<String, ASTNode> defs;

	private static final String OP = "def";

	public ASTDef(DeclList l, ASTNode body) throws IdentifierAlreadyDeclaredException {
		this.body = body;
		Iterator<Entry<String, ASTNode>> it = l.iterator();
		defs = new HashMap<>();

		while (it.hasNext()) {
			Entry<String, ASTNode> next = it.next();
			if (!defs.containsKey(next.getKey())) {
				defs.put(next.getKey(), next.getValue());
			} else {
				throw new IdentifierAlreadyDeclaredException(next.getKey());
			}
		}
	}

	public IValue eval(Environment<IValue> env, Memory m)
			throws TypeMismatchException, IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException {
		Environment<IValue> e = env.beginScope();
		for (Map.Entry<String, ASTNode> defs : defs.entrySet()) {
			IValue result = defs.getValue().eval(env, m);
			e.assoc(defs.getKey(), result);
			if (e.hasDupDef(defs.getKey(), false)) {
				throw new IdentifierAlreadyDeclaredException(defs.getKey());
			}
		}
		IValue ret = body.eval(e, m);
		e.endScope();
		return ret;
	}

	public IType typecheck(Environment<IType> env, Memory m)
			throws StaticTypingException, IdentifierNotDeclaredException, IdentifierAlreadyDeclaredException {

		Environment<IType> envLocal = env.beginScope();
		for (Map.Entry<String, ASTNode> entry : defs.entrySet()) {
			String id = entry.getKey();
			ASTNode val = entry.getValue();
			IType t1 = val.typecheck(env, m);
			envLocal.assoc(id, t1);
		}
		IType t2 = body.typecheck(envLocal, m);
		type = t2;
		envLocal.endScope();
		return t2;
	}

	public void bind(String id, ASTNode node) {
		defs.put(id, node);
	}

	public void addBody(ASTNode body) {
		this.body = body;
	}

	public void compile(CodeBlock c) {

		int newFrameN = c.getNewFrameN();
		Frame oldFrame = c.getCurrentFrame();

		Map<String, IType> types = new HashMap<>();
		for (Map.Entry<String, ASTNode> n : defs.entrySet()) {
			types.put(n.getKey(), n.getValue().getType());
		}

		Frame newFrame = new Frame(newFrameN, oldFrame, types);
		c.changeFrame(newFrame);
		newFrame.emitFrame(c);

		c.emit("new Frame_" + newFrameN);
		c.emit("dup");
		c.emit("invokespecial Frame_" + newFrameN + "/<init>()V");

		if (oldFrame != null) {
			c.emit("dup");
			c.emit("aload 4");
			c.emit("putfield Frame_" + newFrameN + "/sl LFrame_" + oldFrame.getFrameN() + ";");
		}

		int defCount = 0;
		for (Entry<String, ASTNode> entry : defs.entrySet()) {
			c.emit("dup");
			c.framePush();
			entry.getValue().compile(c);
			// compile faz o sipush
			c.framePop();

			String type = Reference.getType(c, entry.getValue().getType());
			c.emit("putfield Frame_" + newFrameN + "/v" + defCount + " " + type);
			try {
				c.addDef(entry.getKey(), defCount++, type);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

		c.emit("astore 4");

		body.compile(c);

		c.endFrame();

		c.emit("aload 4");
		if (oldFrame == null) {
			c.emit("getfield Frame_" + newFrameN + "/sl Ljava/lang/Object;");
		} else {
			c.emit("getfield Frame_" + newFrameN + "/sl LFrame_" + oldFrame.getFrameN() + ";");
		}
		c.emit("astore 4");
	}
}