package compiler;

import java.io.*;
import java.util.Map;
import java.util.Map.Entry;

public class Frame {
	private final int frameN;
	private final Frame parent;
	private int defCount;

	public Frame(int frameN, Frame parent, int defCount) {
		this.frameN = frameN;
		this.parent = parent;
		this.defCount = defCount;
	}

	public Frame getParent() {
		return parent;
	}

	public int getFrameN() {
		return frameN;
	}

	public void emitFrame(CodeBlock c) {
		System.out.println("chegou");
		String fileName = "Frame_" + frameN + ".j";
		File file = new File(fileName);
		FileWriter writer = null;
		try {
			file.createNewFile();
			writer = new FileWriter(file);
		} catch (Exception e) {
			System.out.println("oops");
		}

		try {
			writer.write(".class public Frame_" + frameN + "\n");
			writer.write(".super java/lang/Object\n");
			if (parent != null) {
				writer.write(".field public sl LFrame_" + parent.getFrameN() + ";\n");
			}

			for (int i = 0; i < defCount; i++) {
				writer.write(".field public v" + i + " I\n");
			}

			writer.write(".method public <init>()V\n");
			writer.write("aload 0\n");
			writer.write("invokenonvirtual java/lang/Object/<init>()V\n");
			writer.write("return\n");

			writer.write(".end method\n");
			writer.flush();
			writer.close();
		} catch (Exception e) {
			System.out.println("oops");
		}

		try {
			Runtime.getRuntime().exec("java -jar jasmin.jar " + fileName);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
