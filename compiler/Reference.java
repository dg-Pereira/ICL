package compiler;

import java.io.*;

import types.IType;
import types.TypeBool;
import types.TypeInt;

public class Reference {

	private String name;
	private String type;

	Reference(CodeBlock c, IType type) {
		name = type.getName(c);
		this.type = getType(c, type);
	}

	public String getRefType() {
		return type;
	}

	public String className() {
		return "ref_of_" + name;
	}

	// mudar isto para o CodeBlock
	public static String getType(CodeBlock c, IType type) {
		if (type instanceof TypeInt || type instanceof TypeBool) {
			return type.getName(c);
		} else {
			return "L" + type.getName(c) + "";
		}
	}

	public void emitRef() {

		String fileName = className() + ".j";
		File file = new File(fileName);
		FileWriter writer = null;

		try {
			file.createNewFile();
			writer = new FileWriter(file);
		} catch (Exception e) {
			System.out.println("Reference file creation failed.");
		}
		try {

			writer.write(".class public " + className() + "\n");
			writer.write(".super java/lang/Object\n");
			writer.write(".field public v " + type + ";\n");
			writer.write(".method public <init>()V\n");
			writer.write("aload_0\n");
			writer.write("invokenonvirtual java/lang/Object/<init>()V\n");
			writer.write("return\n");
			writer.write(".end method\n");
			writer.flush();
			writer.close();
		} catch (Exception e) {
			System.out.println("Could not write reference to file.");
		}

		try {
			Runtime.getRuntime().exec("java -jar jasmin.jar " + fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
