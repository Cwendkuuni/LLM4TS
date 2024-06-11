import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.rhino.Node;

public class PeepholeOptimizationsPassTest extends CompilerPassTestBase {

 private static class TestPeepholeOptimization implements AbstractPeepholeOptimization {
 @Override
 public Node optimizeSubtree(Node root) {
 return root;
 }

 @Override
 public void beginTraversal(AbstractCompiler compiler) {
 // empty implementation
 }

 @Override
 public void endTraversal(AbstractCompiler compiler) {
 // empty implementation
 }
 }

 @Test
 public void testPeepholeOptimizationsPass() {
 // Set up the test
 AbstractCompiler compiler = newCompiler();
 CompilerPass pass = new PeepholeOptimizationsPass(compiler, new TestPeepholeOptimization());

 // Test empty case
 Node root = toNode("function foo() { }");
 pass.process(compiler.getExterns(), root);
 assertSame(root, compiler.getRootNode());

 // Test with a constant expression
 root = toNode("function foo() { return 42; }");
 pass.process(compiler.getExterns(), root);
 assertSame(toNode("function foo() { return 42; }"), compiler.getRootNode());

 // Test with a simple statement
 root = toNode("function foo() { x = 42; }");
 pass.process(compiler.getExterns(), root);
 assertSame(toNode("function foo() { }"), compiler.getRootNode());

 // Test with a complex statement
 root = toNode("function foo() { x = y + 3; y = 5; }");
 pass.process(compiler.getExterns(), root);
 assertSame(toNode("function foo() { x = 8; }"), compiler.getRootNode());

 // Test with a function call
 root = toNode("function foo() { return bar(); } function bar() { return 42; }");
 pass.process(compiler.getExterns(), root);
 assertSame(toNode("function foo() { return 42; }"), compiler.getRootNode());

 // Test with multiple optimizations
 AbstractPeepholeOptimization optimization1 = new TestPeepholeOptimization();
 AbstractPeepholeOptimization optimization2 = new TestPeepholeOptimization();
 pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
 root = toNode("function foo() { x = y + 3; y = 5; } x = x + 1;");
 pass.process(compiler.getExterns(), root);
 assertSame(toNode("function foo() { x = 8; x = x; }"), compiler.getRootNode());

 // Test with an error thrown by an optimization
 optimization1 = new TestErrorThrowingPeepholeOptimization();
 pass = new PeepholeOptimizationsPass(compiler, optimization1);
 root = toNode("function foo() { x = y + 3; y = 5; }");
 try {
 pass.process(compiler.getExterns(), root);
 fail("expected an error to be thrown");
 } catch (AssertionError e) {
 // expected
 }
 assertSame(root, compiler.getRootNode());
 }

 private static class TestErrorThrowingPeepholeOptimization
 implements AbstractPeepholeOptimization {
 @Override
 public Node optimizeSubtree(Node root) {
 throw new AssertionError("test error");
 }

 @Override
 public void beginTraversal(AbstractCompiler compiler) {
 // empty implementation
 }

 @Override
 public void endTraversal(AbstractCompiler compiler) {
 // empty implementation
 }
 }
}