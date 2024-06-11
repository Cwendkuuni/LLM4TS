import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import org.junit.Before;
import org.junit.Test;

public class PeepholeOptimizationsPassTest {

 private AbstractCompiler compiler;
 private AbstractPeepholeOptimization optimization1;
 private AbstractPeepholeOptimization optimization2;
 private CompilerPass peepholeOptimizationsPass;

 @Before
 public void setUp() {
 compiler = new AbstractCompiler();
 optimization1 = new NoOpPeepholeOptimization();
 optimization2 = new ConstantFoldingPeepholeOptimization();
 peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
 }

 @Test
 public void testProcess() {
 Node root = createNode("function foo() { return 1 + 1; }");
 peepholeOptimizationsPass.process(null, root);
 Node optimizedNode = compiler.getOutput();
 assertEquals("function foo() { return 2; }", optimizedNode.toSource());
 }

 @Test
 public void testProcessWithNoOptimization() {
 Node root = createNode("function foo() { return 1 + 1; }");
 peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler);
 peepholeOptimizationsPass.process(null, root);
 Node optimizedNode = compiler.getOutput();
 assertEquals("function foo() { return 1 + 1; }", optimizedNode.toSource());
 }

 @Test(expected = AssertionError.class)
 public void testProcessWithError() {
 Node root = createNode("function foo() { return 1 / 0; }");
 peepholeOptimizationsPass.process(null, root);
 fail("Expected an error but none was thrown.");
 }

 @Test
 public void testVisit() {
 Node root = createNode("function foo() { return 1 + 1; }");
 peepholeOptimizationsPass.traverse(root);
 Node optimizedNode = compiler.getOutput();
 assertEquals("function foo() { return 2; }", optimizedNode.toSource());
 }

 @Test
 public void testShouldVisit() {
 Node root = createNode("function foo() { return 1 + 1; }");
 assertTrue(peepholeOptimizationsPass.shouldVisit(root));
 }

 @Test
 public void testShouldRetraverse() {
 Node root = createNode("function foo() { return 1 + 1; }");
 assertFalse(peepholeOptimizationsPass.shouldRetraverse(root));

 Node functionNode = createNode("function bar() { return foo(); }");
 peepholeOptimizationsPass.traverse(root);
 peepholeOptimizationsPass.push();
 assertTrue(peepholeOptimizationsPass.shouldRetraverse(functionNode));
 }

 @Test
 public void testExitNode() {
 Node root = createNode("function foo() { return 1 + 1; }");
 peepholeOptimizationsPass.traverse(root);
 peepholeOptimizationsPass.pop();
 assertEquals("function foo() { return 1 + 1; }", compiler.getOutput().toSource());
 }

 @Test
 public void testBeginTraversal() {
 peepholeOptimizationsPass.beginTraversal();
 }

 @Test
 public void testEndTraversal() {
 peepholeOptimizationsPass.endTraversal();
 }

 private Node createNode(String code) {
 Node node = new Node(Node.ROOT);
 compiler.parse(null, code, null, null, node);
 return node;
 }
}

class NoOpPeepholeOptimization implements AbstractPeepholeOptimization {
 @Override
 public Node optimizeSubtree(Node n) {
 return n;
 }
}

class ConstantFoldingPeepholeOptimization implements AbstractPeepholeOptimization {
 @Override
 public Node optimizeSubtree(Node n) {
 if (n.isNumberLiteral()) {
 return n;
 }
 return null;
 }

 @Override
 public void beginTraversal(AbstractCompiler compiler) {
 // Empty
 }

 @Override
 public void endTraversal(AbstractCompiler compiler) {
 // Empty
 }
}