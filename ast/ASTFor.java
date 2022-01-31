package ast;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import compiler.CodeBlock;
import compiler.Environment;
import compiler.Frame;
import compiler.Memory;
import compiler.Reference;
import exceptions.IdentifierAlreadyDeclaredException;
import exceptions.IdentifierNotDeclaredException;
import exceptions.StaticTypingException;
import exceptions.TypeMismatchException;
import types.IType;
import types.TypeBool;
import values.IValue;
import values.VBool;

public class ASTFor extends ASTNode {

	private ASTNode cond, inc, body;
	private Map<String, ASTNode> defs;

	private static final String OP = "for";

	public ASTFor(Map<String, ASTNode> defs, ASTNode cond, ASTNode inc, ASTNode body) {
		this.defs = defs;
		this.cond = cond;
		this.inc = inc;
		this.body = body;
	}

	public IValue eval(Environment<IValue> env, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		Environment<IValue> e = env.beginScope();

		for (Map.Entry<String, ASTNode> defs : defs.entrySet()) {
			IValue result = defs.getValue().eval(env, m);
			e.assoc(defs.getKey(), result);
			if (e.hasDupDef(defs.getKey(), false)) {
				throw new IdentifierAlreadyDeclaredException(defs.getKey());
			}
		}

		// é preciso fazer num método separado para a definição dos nomes não ser
		// repetida em cada iteração
		IValue ret = recEval(e, m);
		env.endScope();
		return ret;
	}

	private IValue recEval(Environment<IValue> e, Memory m)
			throws IdentifierAlreadyDeclaredException, IdentifierNotDeclaredException, TypeMismatchException {
		IValue condEval = cond.eval(e, m);
		if (!(condEval instanceof VBool)) {
			throw new TypeMismatchException(condEval.getName(), OP);
		}

		if (((VBool) condEval).getVal()) {
			body.eval(e, m);
			inc.eval(e, m);
			return recEval(e, m);
		} else {
			return condEval;
		}
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

		IType t1 = cond.typecheck(envLocal, m);
		if (!(t1 instanceof TypeBool)) {
			throw new StaticTypingException();
		}

		body.typecheck(envLocal, m);
		inc.typecheck(envLocal, m);

		envLocal.endScope();
		type = new TypeBool();
		return type;
	}

	public void compile(CodeBlock c) {
		int l1 = c.getLabel();
		int l2 = c.getLabel();

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

		c.emit("L" + l1 + ":");
		cond.compile(c);
		c.emit("ifeq L" + l2);
		body.compile(c);
		inc.compile(c);
		c.emit("goto L" + l1);
		c.emit("L" + l2 + ":");

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
