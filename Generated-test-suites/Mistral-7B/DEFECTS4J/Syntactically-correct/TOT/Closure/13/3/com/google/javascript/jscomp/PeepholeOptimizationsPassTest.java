import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPasses;
import com.google.javascript.jscomp.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PeepholeOptimizationsPassTest {

 private AbstractCompiler compiler;
 private PeepholeOptimizationsPass optimizationsPass;
 private AbstractPeepholeOptimization optimization1;
 private AbstractPeepholeOptimization optimization2;

 @Before
 public void setUp() {
 compiler = mock(AbstractCompiler.class);
 optimization1 = mock(AbstractPeepholeOptimization.class);
 optimization2 = mock(AbstractPeepholeOptimization.class);

 when(compiler.getOptions()).thenReturn(new CompilerOptions());
 when(optimization1.getPriority()).thenReturn(1);
 when(optimization2.getPriority()).thenReturn(2);

 optimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
 }

 @Test
 public void testProcess() {
 Node root = new Node("root");
 optimizationsPass.process(null, root);
 }

 @Test
 public void testOptimizationsAreCalled() {
 Node root = new Node("root");
 optimizationsPass.process(null, root);

 // Verify that both optimizations were called
 verify(optimization1).optimizeSubtree(root);
 verify(optimization2).optimizeSubtree(root);
 }

 @Test
 public void testShouldVisitFunctionNode() {
 Node functionNode = new Node("functionNode");
 optimizationsPass.traverse(functionNode);

 // verify that function node was visited
 verify(optimization1).optimizeSubtree(functionNode);
 verify(optimization2).optimizeSubtree(functionNode);
 }

 @Test
 public void testShouldNotVisitNonFunctionNode() {
 Node nonFunctionNode = new Node("nonFunctionNode");
 optimizationsPass.traverse(nonFunctionNode);

 // verify that non function node was not visited
 verify(optimization1, never()).optimizeSubtree(nonFunctionNode);
 verify(optimization2, never()).optimizeSubtree(nonFunctionNode);
 }

 @Test
 public void testTraverseChildScopes() {
 Node functionNode = new Node("functionNode");
 Node childFunctionNode = new Node("childFunctionNode");
 functionNode.setFirstChild(childFunctionNode);
 optimizationsPass.traverse(functionNode);

 // verify that child function node was visited
 verify(optimization1).optimizeSubtree(childFunctionNode);
 verify(optimization2).optimizeSubtree(childFunctionNode);
 }

 @Test
 public void testBeginAndEndTraversal() {
 optimizationsPass.beginTraversal();
 optimizationsPass.endTraversal();

 // verify that begin and end traversal were called for all optimizations
 List<AbstractPeepholeOptimization> optimizations = new ArrayList<>(
 Arrays.asList(optimization1, optimization2));
 for (AbstractPeepholeOptimization optimization : optimizations) {
 verify(optimization).beginTraversal(compiler);
 verify(optimization).endTraversal(compiler);
 }
 }

 @Test(expected = AssertionError.class)
 public void testProcessThrowsError() {
 Node root = new Node("root");
 when(optimization1.optimizeSubtree(root)).thenThrow(new AssertionError("test error"));
 optimizationsPass.process(null, root);
 }

 @Test(expected = AssertionError.class)
 public void testOptimizationThrowsError() {
 Node root = new Node("root");
 optimizationsPass.process(null, root);
 verify(optimization1).optimizeSubtree(root);
 verify(optimization2).optimizeSubtree(root);

 when(optimization1.optimizeSubtree(root)).thenThrow(new AssertionError("test error"));
 optimizationsPass.process(null, root);
 }
}