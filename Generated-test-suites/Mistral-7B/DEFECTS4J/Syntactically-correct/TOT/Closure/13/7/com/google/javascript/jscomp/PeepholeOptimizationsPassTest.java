import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(JUnit4.class)
public class PeepholeOptimizationsPassTest {

  private AbstractCompiler compiler;
  private CompilerPassFactory passFactory;
  private AbstractPeepholeOptimization optimization1;
  private AbstractPeepholeOptimization optimization2;

  @Before
  public void setUp() {
    initMocks(this);
    compiler = mock(AbstractCompiler.class);
    passFactory = mock(CompilerPassFactory.class);
    optimization1 = mock(AbstractPeepholeOptimization.class);
    optimization2 = mock(AbstractPeepholeOptimization.class);

    when(passFactory.createPeepholeOptimizationPass(compiler, optimization1, optimization2))
        .thenReturn(new PeepholeOptimizationsPass(compiler, optimization1, optimization2));
  }

  @Test
  public void testPeepholeOptimizationsPass() {
    // Test case for a simple Node without optimizations
    Node root = NodeOps.newIntLiteral(5);
    PeepholeOptimizationsPass pass =
        (PeepholeOptimizationsPass) passFactory.createPeepholeOptimizationPass(compiler, optimization1, optimization2);
    pass.process(null, root);
    verify(compiler, times(1)).addChangeHandler(any(CodeChangeHandler.class));
    verify(compiler, times(1)).removeChangeHandler(any(CodeChangeHandler.class));
    verify(optimization1, times(1)).beginTraversal(compiler);
    verify(optimization1, times(1)).endTraversal(compiler);
    verify(optimization2, times(1)).beginTraversal(compiler);
    verify(optimization2, times(1)).endTraversal(compiler);

    // Test case for Node with optimization by optimization1
    Node nodeWithOptimization1 = NodeOps.newBinaryOp("+", NodeOps.newIntLiteral(2), NodeOps.newIntLiteral(3));
    doReturn(nodeWithOptimization1).when(optimization1).optimizeSubtree(any(Node.class));
    pass = (PeepholeOptimizationsPass) passFactory.createPeepholeOptimizationPass(compiler, optimization1, optimization2);
    pass.process(null, root);
    Node expectedNode = NodeOps.newIntLiteral(5);
    Node actualNode = pass.getCompiler().getOutput();
    assertEquals(expectedNode, actualNode);

    // Test case for Node with optimization by optimization2
    doReturn(NodeOps.newIntLiteral(6)).when(optimization2).optimizeSubtree(any(Node.class));
    pass = (PeepholeOptimizationsPass) passFactory.createPeepholeOptimizationPass(compiler, optimization1, optimization2);
    pass.process(null, root);
    expectedNode = NodeOps.newIntLiteral(6);
    actualNode = pass.getCompiler().getOutput();
    assertEquals(expectedNode, actualNode);

    // Test case for Node with optimization by both optimization1 and optimization2
    Node nodeWithBothOptimizations = NodeOps.newBinaryOp("+", NodeOps.newIntLiteral(4), NodeOps.newBinaryOp("+", NodeOps.newIntLiteral(2), NodeOps.newIntLiteral(3)));
    doReturn(NodeOps.newIntLiteral(7)).when(optimization1).optimizeSubtree(any(Node.class));
    doReturn(NodeOps.newIntLiteral(6)).when(optimization2).optimizeSubtree(any(Node.class));
    pass = (PeepholeOptimizationsPass) passFactory.createPeepholeOptimizationPass(compiler, optimization1, optimization2);
    pass.process(null, root);
    expectedNode = NodeOps.newIntLiteral(7);
    actualNode = pass.getCompiler().getOutput();
    assertEquals(expectedNode, actualNode);

    // Test error scenario: optimization throws an exception
    doThrow(new RuntimeException("Test exception")).when(optimization1).optimizeSubtree(any(Node.class));
    pass = (PeepholeOptimizationsPass) passFactory.createPeepholeOptimizationPass(compiler, optimization1, optimization2);
    pass.process(null, root);
    verify(compiler, times(1)).reportError(anyString());
  }
}