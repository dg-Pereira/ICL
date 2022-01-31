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
import exceptions.IdentifierAlreadyDeclaredException;
import exceptions.IdentifierNotDeclaredException;
import exceptions.StaticTypingException;
import exceptions.TypeMismatchException;
import types.IType;
import values.IValue;

public class ASTDef extends ASTNode {

	private ASTNode body;
	private Map<String, ASTNode> init;

	private static final String OP = "def";

	public ASTDef(DeclList l, ASTNode body) throws IdentifierAlreadyDeclaredException {
		this.body = body;
		Iterator<Entry<String, ASTNode>> it = l.iterator();
		init = new HashMap<>();

		while (it.hasNext()) {
			Entry<String, ASTNode> next = it.next();
			if (!init.containsKey(next.getKey())) {
				init.put(next.getKey(), next.getValue());
			} else {
				throw new IdentifierAlreadyDeclaredException(next.getKey());
			}
		}
	}

	public IValue eval(Environment<IValue> env, Memory m)
			throws TypeMismatchException, IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException {
		Environment<IValue> e = env.beginScope();
		for (Map.Entry<String, ASTNode> defs : init.entrySet()) {
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
		for (Map.Entry<String, ASTNode> entry : init.entrySet()) {
			String id = entry.getKey();
			ASTNode val = entry.getValue();
			IType t1 = val.typecheck(env, m);
			envLocal.assoc(id, t1);
		}
		type = body.typecheck(envLocal, m);
		envLocal.endScope();
		return type;
	}

	public void bind(String id, ASTNode node) {
		init.put(id, node);
	}

	public void addBody(ASTNode body) {
		this.body = body;
	}

	public void compile(CodeBlock c) {
		// create and push new stack frame

		int newFrameN = c.getNewFrameN();
		Frame oldFrame = c.getCurrentFrame();
		Frame newFrame = new Frame(newFrameN, oldFrame, init.size());
		c.changeFrame(newFrame);
		newFrame.emitFrame(c);
		c.emit("new Frame_" + newFrameN);
		c.emit("dup");
		c.emit("invokespecial Frame_" + newFrameN + "/<init>()V");
		c.emit("dup");
		c.emit("aload 4");
		if (oldFrame != null) {
			c.emit("putfield Frame_" + newFrameN + "/sl LFrame_" + oldFrame.getFrameN() + ";");
		}
		c.emit("astore 4");

		int defCount = 0;
		for (Entry<String, ASTNode> entry : init.entrySet()) {
			c.emit("aload 4");
			c.framePush();
			entry.getValue().compile(c);
			// compile faz o sipush
			c.framePop();

			c.emit("putfield Frame_" + newFrameN + "/v" + defCount + " I");
			try {
				c.addDef(entry.getKey(), defCount);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
			defCount++;
		}

		body.compile(c);

		c.endFrame();

		c.emit("aload 4");
		if (oldFrame == null) {
			c.emit("getfield Frame_" + newFrameN + "/sl Ljava/lang/Object;");
		} else {
			c.emit("getfield Frame_" + newFrameN + "/sl LFrame_" + oldFrame.getFrameN() + ";");
		}
		c.emit("astore 4");

		// initialize the slots

		// pop off the frame
	}
}