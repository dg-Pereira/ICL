## ICL Language
This language consists of an interpreter and a compiler.
The interpreter implements all features of the level 2 language, and the compiler implements all features of the level 1 language, plus typechecking. The compiler does not support functions. Aditionally, both the interpreter and the compiler support a for loop (see [ASTFor.java](https://github.com/dg-Pereira/ICL/blob/main/ast/ASTFor.java)), and the modulo operator (see [ASTRem.java](https://github.com/dg-Pereira/ICL/blob/main/ast/ASTRem.java)).

To compile the interpreter and compiler, and run some examples (located in [/examples](https://github.com/dg-Pereira/ICL/tree/main/examples)), you need to do the following:

## How to run
 - Run javacc on the parser file ([Parser00.jj](https://github.com/dg-Pereira/ICL/blob/main/Parser00.jj)). 
 - Compile the Java file generated by javacc (should be called Parser00.java).
 - Compile the [ICLInterpreter.java](https://github.com/dg-Pereira/ICL/blob/main/ICLInterpreter.java) and [ILCompiler.java](https://github.com/dg-Pereira/ICL/blob/main/ICLCompiler.java) files.
 - Run the intended program.

The ICLInterpreter program starts a prompt where you can write your programs, and the results will be immediately printed after you hit enter.
The ICLCompiler program accepts a file name as an argument, and compiles the code to a file called Main.class using jasmin. To then run the code, run java on the Main.class file that was generated. File names are recommended to have a .icl extension. If no file name is given as argument, the compiler tries to find a file called code.icl. If that file is not found, the compiler closes.
