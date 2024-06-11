import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import org.junit.Before;
import org.junit.Test;

public class PeepholeOptimizationsPassTest {

  private AbstractCompiler compiler;
  private PeepholeOptimizationsPass peepholeOptimizationsPass;
  private CompilerPassManager compilerPassManager;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    AbstractPeepholeOptimization optimization1 = mock(AbstractPeepholeOptimization.class);
    AbstractPeepholeOptimization optimization2 = mock(AbstractPeepholeOptimization.class);

    peepholeOptimizationsPass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
    compilerPassManager = new CompilerPassManager();
  }

  @Test
  public void testProcess() {
    Node root = Node.newIntLiteral(1);
    peepholeOptimizationsPass.process(null, root);
    assertNotNull(peepholeOptimizationsPass.getCompiler());
  }

  @Test
  public void testTraverse() {
    // test case for empty node
    Node emptyNode = Node.EMPTY();
    peepholeOptimizationsPass.traverse(emptyNode);

    // test case for non-empty node
    Node nonEmptyNode = Node.newIntLiteral(1);
    peepholeOptimizationsPass.traverse(nonEmptyNode);

    Node childNode = Node.newIntLiteral(2);
    Node parentNode = Node.newExpressionStatement(Node.newName("x"), Node.newAssignment(Node.newName("x"), childNode));
    Node grandParentNode = Node.newExpressionStatement(Node.newName("y"), Node.newAssignment(Node.newName("y"), parentNode));

    peepholeOptimizationsPass.traverse(grandParentNode);
  }

  @Test(expected = AssertionError.class)
  public void testInvalidNode() {
    Node invalidNode = Node.newIntLiteral(-1);
    peepholeOptimizationsPass.traverse(invalidNode);
  }

  @Test
  public void testBeginAndEndTraversal() {
    peepholeOptimizationsPass.beginTraversal();
    peepholeOptimizationsPass.endTraversal();

    for (AbstractPeepholeOptimization optimization : peepholeOptimizationsPass.peepholeOptimizations) {
      when(optimization.beginTraversal(compiler)).thenReturn(true);
      when(optimization.endTraversal(compiler)).thenReturn(true);
    }
  }
}