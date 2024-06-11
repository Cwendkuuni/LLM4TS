import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class PeepholeOptimizationsPassTest {

 private AbstractCompiler compiler;
 private PeepholeOptimizationsPass pass;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 pass = new PeepholeOptimizationsPass(compiler, new SimplePeepholeOptimization(), new ComplexPeepholeOptimization());
 }

 @Test
 public void testSimplePeepholeOptimization() {
 Node root = Node.newObject("root");
 Node input = Node.newNumber(42);
 root.setFirstChild(input);

 pass.process(null, root);

 Node expectedOutput = Node.newNumber(42);
 verify(compiler).process(null, expectedOutput);
 }

 @Test
 public void testComplexPeepholeOptimization() {
 Node root = Node.newObject("root");
 Node functionNode = Node.newFunction(null, null, null);
 root.setFirstChild(functionNode);
 Node functionInput = Node.newObject("input");
 functionNode.setFirstChild(functionInput);
 Node functionOutput = Node.newObject("output");
 functionNode.setLastChild(functionOutput);
 functionInput.setFirstChild(Node.newName("x"));
 functionInput.setLastChild(Node.newNumber(10));
 functionNode.setProperty("return", functionOutput);

 pass.process(null, root);

 Node expectedOutput = Node.newObject("output");
 expectedOutput.setProperty("value", Node.newNumber(9));
 verify(compiler).process(null, expectedOutput);
 }

 @Test(expected = AssertionError.class)
 public void testErrorScenario() {
 Node root = Node.newObject("root");
 Node functionNode = Node.newFunction(null, null, null);
 root.setFirstChild(functionNode);
 Node input = Node.newName("x");
 Node assignment = Node.newAssignment(input, Node.newNumber(10));
 functionNode.setFirstChild(assignment);
 functionNode.setLastChild(Node.newReturn(Node.newName("y")));

 pass.process(null, root);
 }
}

class SimplePeepholeOptimization implements AbstractPeepholeOptimization {
 @Override
 public Node optimizeSubtree(Node node) {
 return node;
 }
}

class ComplexPeepholeOptimization implements AbstractPeepholeOptimization {
 @Override
 public Node optimizeSubtree(Node node) {
 Node output = Node.newObject("output");
 node.setLastChild(output);
 return output;
 }

 @Override
 public void beginTraversal(AbstractCompiler compiler) {
 // Empty implementation
 }

 @Override
 public void endTraversal(AbstractCompiler compiler) {
 // Empty implementation
 }
}