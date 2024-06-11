import org.junit.Before;
import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import static org.junit.Assert.*;

public class PeepholeOptimizationsPassTest {

 private AbstractCompiler compiler;
 private PeepholeOptimizationsPass optimizationsPass;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 AbstractPeepholeOptimization optimization1 = new StubPeepholeOptimization1();
 AbstractPeepholeOptimization optimization2 = new StubPeepholeOptimization2();
 optimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
 }

 @Test
 public void testPeepholeOptimizationsPass_EmptyCode() {
 Node root = Node.root();
 optimizationsPass.process(root, root);
 assertEquals(root, optimizationsPass.getCompiler().getOutput());
 }

 @Test
 public void testPeepholeOptimizationsPass_SimpleAssignment() {
 Node root = createNode("x = 5;");
 optimizationsPass.process(root, root);
 assertEquals(Node.root(), optimizationsPass.getCompiler().getOutput());
 }

 @Test
 public void testPeepholeOptimizationsPass_SimpleExpression() {
 Node root = createNode("1 + 2;");
 optimizationsPass.process(root, root);
 assertEquals(Node.intLiteral(3), optimizationsPass.getCompiler().getOutput());
 }

 @Test
 public void testPeepholeOptimizationsPass_SimpleFunction() {
 Node root = createNode("function foo() { return 1 + 2; }");
 optimizationsPass.process(root, root);
 assertEquals(Node.root(), optimizationsPass.getCompiler().getOutput());
 assertEquals(Node.intLiteral(3), optimizationsPass.getCompiler().getOutput().getFirstChild().getFirstChild().getFirstChild());
 }

 @Test
 public void testPeepholeOptimizationsPass_NestedFunctions() {
 Node root = createNode("function foo() { function bar() { return 1 + 2; } return bar(); }");
 optimizationsPass.process(root, root);
 assertEquals(Node.intLiteral(3), optimizationsPass.getCompiler().getOutput().getFirstChild().getFirstChild().getFirstChild());
 }

 @Test(expected = RuntimeException.class)
 public void testPeepholeOptimizationsPass_ErrorScenario() {
 Node root = createNode("function foo() { throw new Error('Test error'); }");
 optimizationsPass.process(root, root);
 fail("Expected an error but none was thrown");
 }

 @Test
 public void testPeepholeOptimizationsPass_ErrorHandling() {
 Node root = createNode("try { function foo() { throw new Error('Test error'); } } catch (e) { return 42; }");
 optimizationsPass.process(root, root);
 assertEquals(Node.intLiteral(42), optimizationsPass.getCompiler().getOutput());
 }

 @Test
 public void testPeepholeOptimizationsPass_DeepNestedExpressions() {
 Node root = createNode("(1 + 2) * (3 + 4);");
 optimizationsPass.process(root, root);
 assertEquals(Node.intLiteral(13), optimizationsPass.getCompiler().getOutput());
 }

 private Node createNode(String code) {
 return NodePrinter.parseCompilationUnit(code);
 }

 private AbstractCompiler getCompiler() {
 return optimizationsPass.getCompiler();
 }
}

class StubPeepholeOptimization1 implements AbstractPeepholeOptimization {
 @Override
 public Node optimizeSubtree(Node node) {
 return node;
 }
}

class StubPeepholeOptimization2 implements AbstractPeepholeOptimization {
 @Override
 public Node optimizeSubtree(Node node) {
 return node;
 }

 @Override
 public void beginTraversal(CompilerPass pass) {
 throw new UnsupportedOperationException();
 }

 @Override
 public void endTraversal(CompilerPass pass) {
 throw new UnsupportedOperationException();
 }
}