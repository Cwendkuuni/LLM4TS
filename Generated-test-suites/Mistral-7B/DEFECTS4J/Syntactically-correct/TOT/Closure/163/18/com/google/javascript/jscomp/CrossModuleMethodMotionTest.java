import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JSModuleGraph;

public class CrossModuleMethodMotionTest {

 private Compiler compiler;
 private CrossModuleMethodMotion pass;

 @Before
 public void setUp() {
 compiler = new Compiler();
 IdGenerator idGenerator = new IdGenerator();
 pass = new CrossModuleMethodMotion(compiler, idGenerator, true);
 }

 @Test
 public void testMoveMethodToAnotherModule() {
 // Create two modules with a shared prototype property
 String module1Source = "function Foo() {} \n" +
 "Foo.prototype.bar = function() { return 'module1'; };";
 String module2Source = "function Foo() {} \n" +
 "Foo.prototype.bar = function() { return 'module2'; };";

 // Compile the modules separately
 Node module1 = compiler.compile(module1Source, "Module1", null);
 Node module2 = compiler.compile(module2Source, "Module2", null);

 // Run the pass to move the method to the other module
 pass.process(module1, module2);
 compiler.reportCodeChange();

 // Check that the method was moved to the other module
 Node newModule1 = compiler.getNodeForCodeInsertion(null);
 Node newModule2 = compiler.getNodeForCodeInsertion(module2);
 assertEquals("module2", newModule1.getFirstChild().getChildren().get(0).getString());
 assertEquals("module1", newModule2.getFirstChild().getChildren().get(0).getString());
 }

 @Test(expected = IllegalStateException.class)
 public void testMoveMethodToSelf() {
 // Create a module with a prototype property
 String moduleSource = "function Foo() {} \n" +
 "Foo.prototype.bar = function() { return 'module'; };";

 // Compile the module
 Node module = compiler.compile(moduleSource, "Module", null);

 // Run the pass with an empty list of modules to ensure that an error is thrown
 pass.process(module, null);
 }

 @Test
 public void testMoveMethodToExterns() {
 // Create a module with a prototype property that is declared in the externs file
 String moduleSource = "function Foo() {} \n" +
 "Foo.prototype.bar = function() { return 'module'; };";
 String externs = "extern function Foo() {}\n" +
 "extern function Foo.prototype.bar():any";

 // Compile the module and the externs file
 Node module = compiler.compile(moduleSource, "Module", null);
 Node externsNode = compiler.compile(externs, "Externs", null);

 // Run the pass to move the method to the externs file
 pass.process(module, externsNode);
 compiler.reportCodeChange();

 // Check that the method was moved to the externs file
 Node newModule = compiler.getNodeForCodeInsertion(null);
 Node newExterns = compiler.getNodeForCodeInsertion(externsNode);
 assertEquals("function Foo.prototype.bar():any", newExterns.getFirstChild().getString());
 }
}