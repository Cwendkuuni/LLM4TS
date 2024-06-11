import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeChangeHandler;
import com.google.javascript.jscomp.ScopeStack;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class PeepholeOptimizationsPassTest {

  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private Node rootNode;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    compiler = mock(AbstractCompiler.class);
    passManager = new CompilerPassManager(compiler);
    rootNode = new Node();
  }

  @Test
  public void testEmptyPass() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, (AbstractPeepholeOptimization[]) new Object[0]);
    pass.process(null, rootNode);

    verify(compiler, never()).addChangeHandler(any(PeepholeChangeHandler.class));
    verify(compiler, never()).removeChangeHandler(any(PeepholeChangeHandler.class));
    verify(compiler, never()).visit(any(Node.class));
  }

  @Test
  public void testOnePass() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
    pass.process(null, rootNode);

    verify(compiler).addChangeHandler(any(PeepholeChangeHandler.class));
    verify(compiler).removeChangeHandler(any(PeepholeChangeHandler.class));
    verify(compiler, times(1)).visit(eq(rootNode));
    verify(optimization).beginTraversal(eq(compiler));
    verify(optimization).endTraversal(eq(compiler));
  }

  @Test
  public void testMultiplePasses() {
    AbstractPeepholeOptimization optimization1 = mock(AbstractPeepholeOptimization.class);
    AbstractPeepholeOptimization optimization2 = mock(AbstractPeepholeOptimization.class);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization1, optimization2);
    pass.process(null, rootNode);

    verify(compiler).addChangeHandler(any(PeepholeChangeHandler.class));
    verify(compiler, times(2)).removeChangeHandler(any(PeepholeChangeHandler.class));
    verify(compiler, times(2)).visit(eq(rootNode));
    verify(optimization1).beginTraversal(eq(compiler));
    verify(optimization1).endTraversal(eq(compiler));
    verify(optimization2).beginTraversal(eq(compiler));
    verify(optimization2).endTraversal(eq(compiler));
  }

  @Test
  public void testChangeHandler() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    PeepholeChangeHandler changeHandler = new PeepholeChangeHandler();

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
    pass.process(null, rootNode);

    pass.getCompiler().addChangeHandler(changeHandler);
    pass.getCompiler().removeChangeHandler(changeHandler);

    verify(optimization).beginTraversal(eq(compiler));
    verify(optimization).endTraversal(eq(compiler));
    verify(changeHandler).reportChange();
  }

  @Test
  public void testTraversalState() {
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, (AbstractPeepholeOptimization[]) new Object[0]);

    pass.traverse(rootNode);

    assertNull(pass.traversalState.peek().changed);
    assertNull(pass.traversalState.peek().traverseChildScopes);

    pass.traversalState.push();
    pass.traversalState.peek().changed = true;
    pass.traversalState.pop();

    assertNull(pass.traversalState.peek().changed);
    assertNull(pass.traversalState.peek().traverseChildScopes);
  }

  @Test
  public void testVisitor() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    when(optimization.optimizeSubtree(any(Node.class))).thenReturn(null);

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
    pass.process(null, rootNode);

    pass.visit(rootNode);

    verify(optimization, never()).optimizeSubtree(any(Node.class));
  }

  @Test
  public void testVisitorWithChange() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    when(optimization.optimizeSubtree(any(Node.class))).thenReturn(new Node());

    PeepholeChangeHandler changeHandler = new PeepholeChangeHandler();
    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
    pass.process(null, rootNode);
    pass.getCompiler().addChangeHandler(changeHandler);

    pass.visit(rootNode);

    verify(optimization).optimizeSubtree(eq(rootNode));
    verify(changeHandler).reportChange();
  }

  @Test
  public void testBeginTraversal() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    when(optimization.beginTraversal(any(AbstractCompiler.class))).thenThrow(new RuntimeException());

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
    pass.process(null, rootNode);

    pass.beginTraversal();

    verify(optimization).beginTraversal(eq(compiler));
  }

  @Test(expected = RuntimeException.class)
  public void testBeginTraversalWithException() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    when(optimization.beginTraversal(any(AbstractCompiler.class))).thenThrow(new RuntimeException());

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
    pass.process(null, rootNode);

    pass.beginTraversal();
  }

  @Test
  public void testEndTraversal() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    when(optimization.endTraversal(any(AbstractCompiler.class))).thenThrow(new RuntimeException());

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
    pass.process(null, rootNode);

    pass.endTraversal();

    verify(optimization).endTraversal(eq(compiler));
  }

  @Test(expected = RuntimeException.class)
  public void testEndTraversalWithException() {
    AbstractPeepholeOptimization optimization = mock(AbstractPeepholeOptimization.class);

    when(optimization.endTraversal(any(AbstractCompiler.class))).thenThrow(new RuntimeException());

    PeepholeOptimizationsPass pass = new PeepholeOptimizationsPass(compiler, optimization);
    pass.process(null, rootNode);

    pass.endTraversal();
  }
}