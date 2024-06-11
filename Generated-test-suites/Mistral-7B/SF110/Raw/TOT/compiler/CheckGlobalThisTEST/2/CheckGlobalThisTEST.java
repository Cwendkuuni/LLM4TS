import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {

 private Compiler compiler;

 @Before
 public void setUp() {
 compiler = new Compiler();
 compiler.addProcessor(new CheckGlobalThis(compiler));
 }

 @Test
 public void testSafeUsage() {
 Node node = Node.newExpression("function SafeUsage() { this.x = 5; }");
 NodeTraversal traversal = new NodeTraversal(compiler.getCurrentSource(), node);
 compiler.process(node, null);
 assertTrue(compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testUnsafeAssignmentToGlobalProperty() {
 Node node = Node.newExpression("this.x = 5;");
 NodeTraversal traversal = new NodeTraversal(compiler.getCurrentSource(), node);
 compiler.process(Node.SCOPE, node);
 Node warning = traversal.traverse(node);
 assertEquals(Node.WARNING, warning.getType());
 assertEquals("JSC_USED_GLOBAL_THIS", warning.getDiagnosticCode());
 }

 @Test
 public void testUnsafeAssignmentToPrototypeProperty() {
 Node node = Node.newExpression("Object.prototype.x = function() { this.y = 5; }");
 NodeTraversal traversal = new NodeTraversal(compiler.getCurrentSource(), node);
 compiler.process(Node.SCOPE, node);
 Node warning = traversal.traverse(node);
 assertEquals(Node.WARNING, warning.getType());
 assertEquals("JSC_USED_GLOBAL_THIS", warning.getDiagnosticCode());
 }

 @Test
 public void testUnsafeFunctionCall() {
 Node node = Node.newExpression("function Foo() { this.x = 5; } Foo();");
 NodeTraversal traversal = new NodeTraversal(compiler.getCurrentSource(), node);
 compiler.process(Node.SCOPE, node);
 Node warning = traversal.traverse(Node.NAME, 0); // Foo()
 assertEquals(Node.WARNING, warning.getType());
 assertEquals("JSC_USED_GLOBAL_THIS", warning.getDiagnosticCode());
 }

 @Test
 public void testFunctionWithThisAnnotation() {
 Node node = Node.newExpression("function @this Foo() { this.x = 5; }");
 NodeTraversal traversal = new NodeTraversal(compiler.getCurrentSource(), node);
 compiler.process(Node.SCOPE, node);
 assertTrue(compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testFunctionWithConstructorAnnotation() {
 Node node = Node.newExpression("function Foo() { this.x = 5; }", null, Node.NEWEXP);
 NodeTraversal traversal = new NodeTraversal(compiler.getCurrentSource(), node);
 compiler.process(Node.SCOPE, node);
 assertTrue(compiler.getDiagnostics().isEmpty());
 }

 @Test
 public void testFunctionWithPrototypeMethod() {
 Node node = Node.newExpression("var Obj = function() {}; Obj.prototype.m = function() { this.x = 5; }; Obj.m();");
 NodeTraversal traversal = new NodeTraversal(compiler.getCurrentSource(), node);
 compiler.process(Node.SCOPE, node);
 assertTrue(compiler.getDiagnostics().isEmpty());
 }
}