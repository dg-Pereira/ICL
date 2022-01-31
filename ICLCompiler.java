
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import ast.ASTNode;
import compiler.CodeBlock;
import compiler.Environment;
import compiler.Memory;
import exceptions.StaticTypingException;

public class ICLCompiler {

	private static final String FILENAME = "code.icl";

	/** Main entry point. */
	public static void main(String args[]) {

		InputStream in = null;

		String fileName;

		try {
			fileName = args[0];
			String extension = fileName.substring(fileName.length() - 4);
			if (!extension.equals(".icl")) {
				System.out.println("Warning: Code file does not have a .icl extension.");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			fileName = FILENAME;
			System.out.println(
					"Warning: No code file provided as argument. Proceeding with the default file name code.icl");
		}

		try {
			File file = new File(fileName);
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			handleException("File " + fileName + " not found.", e);
		} catch (Exception e) {
			handleException("File error encountered.", e);
		}

		Parser00 parser = new Parser00(in);

		CodeBlock code = new CodeBlock();
		try {
			ASTNode ast = parser.Start();
			ast.typecheck(new Environment<>(), new Memory());
			code.emit("aconst_null");
			code.emit("astore 4");
			code.emit("");
			ast.compile(code);
			code.dump("Main.j");
		} catch (StaticTypingException e) {
			handleException("Type error encountered.", e);
		} catch (Exception e) {
			handleException("Syntax error encountered.", e);
		}

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			Runtime.getRuntime().exec("java -jar jasmin.jar Main.j");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void handleException(String message, Exception e) {
		System.out.println(message + " Exiting...");

		System.out.println("Exception message:");
		System.out.println(e.getMessage());

		System.out.println("Stack trace on exit:");
		e.printStackTrace();

		System.exit(1);
	}
}
