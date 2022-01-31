
import ast.ASTNode;
import compiler.Environment;
import compiler.Memory;
import values.IValue;

public class ICLInterpreter {

    /** Main entry point. */
    public static void main(String args[]) {
      Parser00 parser = new Parser00(System.in);
  
      while (true) {
      try {
        System.out.print("> ");
        Environment<IValue> e = new Environment<>();
        Memory m = new Memory();
        ASTNode ast = parser.Start();
        ast.eval(e, m);
      } catch (Exception e) {
        System.out.println ("Syntax Error!");
        e.printStackTrace();
        parser.ReInit(System.in);
      }
      }
    }
  
  }