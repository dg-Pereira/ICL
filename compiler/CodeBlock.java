package compiler;

import java.io.*;
import java.util.Stack;

import exceptions.IdentifierAlreadyDeclaredException;
import exceptions.IdentifierNotDeclaredException;
import types.IType;
import types.TypeBool;
import types.TypeInt;
import types.TypeRef;

import java.util.Map;
import java.util.HashMap;

public class CodeBlock {
	private static final String FIRST_PART = ".class public Main\n.super java/lang/Object\n;\n; standard initializer\n.method public <init>()V\naload_0\ninvokenonvirtual java/lang/Object/<init>()V\nreturn\n.end method\n.method public static main([Ljava/lang/String;)V\n; set limits used by this method\n.limit locals 10 \n.limit stack 256\n;    1 - the PrintStream object held in java.lang.System.out\n;\n; place your bytecodes here between START and END\n; START\n\n";
	private static final String SECOND_PART = "\n; END\nreturn\n.end method\n";
	private String[] code;
	private int pc;
	private Frame currFrame;
	private Stack<Frame> frames;
	private int frameCount;
	private int refCount;
	private Map<IType, Reference> typeClasses;

	private Environment<FrameLocation> defs;

	private int currLabel;

	public CodeBlock() {
		pc = 0;
		code = new String[1000];
		frameCount = 0;
		currFrame = new Frame(0, null, null);
		this.frames = new Stack<>();
		currLabel = 1;
		refCount = 0;
		typeClasses = new HashMap<>();
		Reference intRef = new Reference(this, getNewRefN(), new TypeInt());
		Reference boolRef = new Reference(this, getNewRefN(), new TypeBool());
		intRef.emitRef();
		boolRef.emitRef();
		typeClasses.put(new TypeInt(), intRef);
		typeClasses.put(new TypeBool(), boolRef);
		defs = null;
	}

	public int getLabel() {
		return currLabel++;
	}

	public void changeFrame(Frame newFrame) {
		if (defs == null) {
			defs = new Environment<>();
		} else {
			defs = defs.beginScope();
		}
		currFrame = newFrame;
	}

	public void framePush() {
		frames.push(currFrame);
		currFrame = currFrame.getParent();
	}

	public void framePop() {
		currFrame = frames.pop();
	}

	public void addDef(String id, int loc, String type) throws IdentifierAlreadyDeclaredException {
		FrameLocation frameLoc = new FrameLocation(currFrame.getFrameN(), loc, type);
		defs.assoc(id, frameLoc);
	}

	public FrameLocation findDef(String id) throws IdentifierNotDeclaredException {
		return (FrameLocation) defs.find(id);
	}

	public int getNewFrameN() {
		return frameCount++;
	}

	public int getNewRefN() {
		return refCount++;
	}

	public Reference getReference(TypeRef type) {
		Reference ref = typeClasses.get(type.getRefType());
		if (ref == null) {
			ref = new Reference(this, getNewRefN(), type.getRefType());
			ref.emitRef();
			typeClasses.put(type.getRefType(), ref);
		}
		return ref;
	}

	public void emit(String opcode) {
		code[pc++] = opcode;
	}

	private String stringArrayToString(String[] stringArray) {
		String ret = "";
		for (String s : stringArray) {
			if (s == null){
				break;
			}
			ret = ret + s + "\n";
		}
		return ret;
	}

	public void dump(String filename) {
		String codeString = stringArrayToString(code);
		File file = new File(filename);
		FileWriter writer = null;
		try {
			file.createNewFile();
			writer = new FileWriter(file);
			writer.write(FIRST_PART + codeString + SECOND_PART);
		} catch (Exception e) {
			System.out.println("oops");
		}
		try {
			writer.flush();
			writer.close();
		} catch (Exception e) {
			System.out.println("oops");
		}
	}

	public Frame getCurrentFrame() {
		return currFrame;
	}

	public void endFrame() {
		this.currFrame = this.currFrame.getParent();
	}
}
