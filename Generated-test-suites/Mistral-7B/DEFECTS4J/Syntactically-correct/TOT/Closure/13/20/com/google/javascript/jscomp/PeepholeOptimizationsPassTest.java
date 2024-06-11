import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PeepholeOptimizationsPassTest {

 private Compiler compiler;
 private PeepholeOptimizationsPass optimizationsPass;
 private AbstractPeepholeOptimization optimizatioOne, optimizatioTwo;

 @Before
 public void setUp() {
 compiler = mock(Compiler.class);
 optimizatioOne = mock(AbstractPeepholeOptimization.class);
 optimizatioTwo = mock(AbstractPeepholeOptimization.class);

 when(compiler.addChangeHandler(any(CodeChangeHandler.class)))
 .thenReturn(compiler);
 when(compiler.removeChangeHandler(any(CodeChangeHandler.class)))
 .thenReturn(compiler);

 optimizationsPass = new PeepholeOptimizationsPass(compiler, optimizatioOne, optimizatioTwo);
 }

 @Test
 public void testEmpty() {
 Node root = Node.NULL;
 optimizationsPass.process(Node.NULL, root);
 assertNull(root);
 }

 @Test
 public void testOptimizationOne() {
 Node root = createNode("function foo() {}", "name", "foo");
 optimizationsPass.process(Node.NULL, root);
 assertNotNull(root);

 verifyOptimizationWasCalled(optimizatioOne);
 }

 @Test
 public void testOptimizationTwo() {
 Node root = createNode("function foo() {}", "name", "foo");
 optimizationsPass.process(Node.NULL, root);
 assertNotNull(root);

 verifyOptimizationWasCalled(optimizatioTwo);
 }

 @Test
 public void testOptimizationOneThenOptimizationTwo() {
 Node root = createNode("function foo() {}", "name", "foo");
 optimizationsPass.process(Node.NULL, root);
 assertNotNull(root);

 verifyOptimizationWasCalledInOrder(optimizatioOne, optimizatioTwo);
 }

 private Node createNode(String nodeType, String name, String nodeName) {
 Node n = new Node();
 n.setType(nodeType);
 n.setName(name);
 n.setDebugName(nodeName);
 return n;
 }

 private void verifyOptimizationWasCalled(AbstractPeepholeOptimization optimization) {
 when(optimization.beginTraversal(compiler)).thenReturn(optimization);
 when(optimization.endTraversal(compiler)).thenReturn(optimization);
 optimizationsPass.beginTraversal();
 optimizationsPass.endTraversal();

 // We don't care about the side effect of the optimization,
 // just that it was called.
 // You can check the result if needed.
 }

 private void verifyOptimizationWasCalledInOrder(AbstractPeepholeOptimization... optimizations) {
 List<AbstractPeepholeOptimization> optimizationsInvoked = new ArrayList<>();
 for (AbstractPeepholeOptimization optimization : optimizations) {
 when(optimization.beginTraversal(compiler)).thenAnswer(invocation -> {
 optimizationsInvoked.add(optimization);
 return optimization;
 });
 when(optimization.endTraversal(compiler)).thenAnswer(invocation -> optimization);
 }

 optimizationsPass.beginTraversal();
 optimizationsPass.endTraversal();

 assert(Arrays.equals(optimizations, optimizationsInvoked.toArray()));
 }
}