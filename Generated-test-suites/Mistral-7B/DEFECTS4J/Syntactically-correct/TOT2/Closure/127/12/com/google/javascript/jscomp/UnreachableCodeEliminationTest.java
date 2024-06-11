import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.UnreachableCodeElimination;

public class UnreachableCodeEliminationTest {

 @Test
 public void testUnreachableCodeRemoval() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions(null);
 CompilerPass pass = new UnreachableCodeElimination(compiler, true);
 options.setPasses(pass);

 String sourceCode = "function foo() {" +
 "if (true) {" +
 "return; " +
 "alert('unreachable');" +
 "}" +
 "}";

 Node externs = null;
 Node toplevel = NodePrinter.print(new AstNodeFactory().parse(sourceCode), 0);

 compiler.compile(externs, toplevel, options);

 assertTrue("Code should have changed", pass.codeChanged);
 assertEquals("Expected no code after return statement", "", toplevel.toString());
 }

 @Test
 public void testDeadCodeWithNoSideEffects() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions(null);
 CompilerPass pass = new UnreachableCodeElimination(compiler, true);
 options.setPasses(pass);

 String sourceCode = "function foo() {" +
 "return a.b.MyClass.prototype.propertyName;" +
 "}";

 Node externs = null;
 Node toplevel = NodePrinter.print(new AstNodeFactory().parse(sourceCode), 0);

 compiler.compile(externs, toplevel, options);

 assertTrue("Code should have changed", pass.codeChanged);
 assertEquals("Expected no code left after eliminating dead code", "", toplevel.toString());
 }

 @Test(expected = AssertionError.class)
 public void testExceptionThrownOnUnreachableCode() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions(null);
 CompilerPass pass = new UnreachableCodeElimination(compiler, true);
 options.setPasses(pass);

 String sourceCode = "function foo() {" +
 "throw new Error('Exception thrown');" +
 "return; " +
 "alert('unreachable');" +
 "}";

 Node externs = null;
 Node toplevel = NodePrinter.print(new AstNodeFactory().parse(sourceCode), 0);

 compiler.compile(externs, toplevel, options);

 fail("Exception should have been thrown but was not");
 }

 @Test
 public void testDeadCodeInsideFunction() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions(null);
 CompilerPass pass = new UnreachableCodeElimination(compiler, true);
 options.setPasses(pass);

 String sourceCode = "function foo() {" +
 "function bar() {" +
 "if (true) {" +
 "return; " +
 "alert('unreachable');" +
 "}" +
 "}" +
 "bar();" +
 "}";

 Node externs = null;
 Node toplevel = NodePrinter.print(new AstNodeFactory().parse(sourceCode), 0);

 compiler.compile(externs, toplevel, options);

 assertTrue("Code should have changed", pass.codeChanged);
 assertEquals("Expected no call to bar() function", "", toplevel.toString());
 }

 @Test
 public void testDeadCodeInsideFunctionWithSideEffects() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions(null);
 CompilerPass pass = new UnreachableCodeElimination(compiler, true);
 options.setPasses(pass);

 String sourceCode = "function foo() {" +
 "function bar() {" +
 "if (true) {" +
 "return; " +
 "alert('unreachable');" +
 "}" +
 "console.log('Log message');" +
 "}" +
 "bar();" +
 "}";

 Node externs = null;
 Node toplevel = NodePrinter.print(new AstNodeFactory().parse(sourceCode), 0);

 compiler.compile(externs, toplevel, options);

 assertTrue("Code should have changed", pass.codeChanged);
 assertEquals("Expected log message to be printed", "Log message\n", toplevel.toString());
 }

 @Test
 public void testDeadCodeInsideFunctionWithReturnStatement() {
 Compiler compiler = new Compiler();
 CompilerOptions options = new CompilerOptions(null);
 CompilerPass pass = new UnreachableCodeElimination(compiler, true);
 options.setPasses(pass);

 String sourceCode = "function foo() {" +
 "function bar() {" +
 "return; " +
 "if (true) {" +
 "alert('unreachable');" +
 "}" +
 "}" +
 "bar();" +
 "return 1;" +
 "}";

 Node externs = null;
 Node toplevel = NodePrinter.print(new AstNodeFactory().parse(sourceCode), 0);

 compiler.compile(externs, toplevel, options);

 assertTrue("Code should have changed", pass.codeChanged);
 assertEquals("Expected return statement to still be present", "return 1;", toplevel.toString());
 }
}