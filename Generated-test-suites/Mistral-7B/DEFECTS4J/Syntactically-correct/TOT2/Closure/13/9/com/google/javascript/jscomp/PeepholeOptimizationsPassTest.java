import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPasses;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PeepholeOptimizationsPass;
import com.google.javascript.jscomp.ScopeState;
import com.google.javascript.jscomp.StateStack;

public class PeepholeOptimizationsPassTest {
  private AbstractCompiler compiler;
  private AbstractPeepholeOptimization optimization;
  private PeepholeOptimizationsPass pass;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    optimization = mock(AbstractPeepholeOptimization.class);
    pass = new PeepholeOptimizationsPass(compiler, optimization);
  }

  @Test
  public void testConstructor() {
    assertNotNull(pass);
  }

  @Test
  public void testProcess_EmptyAST() {
    Node root = null;
    pass.process(null, root);
    assertFalse("No change expected", pass.getCompiler().hasChanges());
  }

  @Test
  public void testProcess_WithNode() {
    Node root = new Node();
    pass.process(null, root);
    assertFalse("No change expected", pass.getCompiler().hasChanges());
  }

  @Test(expected = NullPointerException.class)
  public void testProcess_NullCompiler() {
    pass = new PeepholeOptimizationsPass(null, optimization);
    Node root = new Node();
    pass.process(null, root);
  }

  @Test(expected = NullPointerException.class)
  public void testProcess_NullOptimization() {
    pass = new PeepholeOptimizationsPass(compiler, null);
    Node root = new Node();
    pass.process(null, root);
  }

  @Test
  public void testVisit_NullNode() {
    pass.visit(null);
    assertNull("Null node should not be visited", pass.getCompiler());
  }

  @Test
  public void testVisit_EmptyNode() {
    Node node = new Node();
    pass.visit(node);
    assertFalse("No change expected", pass.getCompiler().hasChanges());
  }

  @Test
  public void testVisit_WithNode() {
    Node node = new Node();
    pass.visit(node);
    assertFalse("No change expected", pass.getCompiler().hasChanges());
  }

  @Test
  public void testShouldVisit_FunctionNode() {
    Node node = new Node();
    node.setType(Node.FUNCTION);
    ScopeState state = pass.traversalState.peek();
    assertTrue(pass.shouldVisit(node));
  }

  @Test
  public void testShouldVisit_ScriptNode() {
    Node node = new Node();
    node.setType(Node.SCRIPT);
    ScopeState state = pass.traversalState.peek();
    assertTrue(pass.shouldVisit(node));
  }

  @Test
  public void testShouldVisit_NonFunctionNonScriptNode() {
    Node node = new Node();
    assertFalse(pass.shouldVisit(node));
  }

  @Test
  public void testShouldRetraverse_FunctionNodeWithChangedParent() {
    Node parent = new Node();
    Node node = new Node();
    parent.setType(Node.FUNCTION);
    node.setType(Node.SCRIPT);
    node.setParent(parent);
    ScopeState state = pass.traversalState.peek();
    assertTrue(pass.shouldRetraverse(node));
  }

  @Test
  public void testShouldRetraverse_FunctionNodeWithUnchangedParent() {
    Node parent = new Node();
    Node node = new Node();
    parent.setType(Node.FUNCTION);
    node.setType(Node.SCRIPT);
    node.setParent(parent);
    ScopeState state = new ScopeState();
    pass.traversalState.push();
    state = pass.traversalState.peek();
    state.changed = true;
    assertFalse(pass.shouldRetraverse(node));
  }

  @Test
  public void testShouldRetraverse_NonFunctionNode() {
    Node node = new Node();
    assertFalse(pass.shouldRetraverse(node));
  }

  @Test
  public void testExitNode_FunctionNode() {
    Node node = new Node();
    node.setType(Node.FUNCTION);
    pass.exitNode(node);
    assertEquals(0, pass.traversalState.getCurrentDepth());
  }

  @Test
  public void testExitNode_ScriptNode() {
    Node node = new Node();
    node.setType(Node.SCRIPT);
    pass.exitNode(node);
    assertEquals(0, pass.traversalState.getCurrentDepth());
  }

  @Test
  public void testExitNode_NullNode() {
    pass.exitNode(null);
    assertEquals(0, pass.traversalState.getCurrentDepth());
  }
}