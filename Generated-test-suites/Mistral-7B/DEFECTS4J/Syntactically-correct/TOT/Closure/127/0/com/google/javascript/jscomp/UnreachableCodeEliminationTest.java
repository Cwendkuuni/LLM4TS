import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private UnreachableCodeElimination eliminationPass;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    eliminationPass = new UnreachableCodeElimination(compiler, false);
  }

  @Test
  public void testEliminationOfSimpleDeadCode() {
    Node root = new Node(Token.BLOCK, 1, 1);
    root.setFirstChild(new Node(Token.RETURN, 1, 1));
    root.setLastChild(new Node(Token.ALERT, 1, 1));

    NodeTraversal.traverse(compiler, root, eliminationPass);

    assertTrue(root.isEmpty());
  }

  @Test
  public void testEliminationOfDeadCodeAfterConditionalStatement() {
    Node root = new Node(Token.BLOCK, 1, 1);
    root.setFirstChild(new Node(Token.IF, 1, 1));
    root.getFirstChild().setFirstChild(new Node(Token.RETURN, 1, 1));
    root.getFirstChild().setLastChild(new Node(Token.ALERT, 1, 1));
    root.setLastChild(new Node(Token.ALERT, 1, 1));

    NodeTraversal.traverse(compiler, root, eliminationPass);

    assertTrue(root.getLastChild().isPresent());
  }

  @Test
  public void testEliminationOfDeadCodeAfterReturnInFunction() {
    Node root = new Node(Token.FUNCTION, 1, 1);
    root.setFirstChild(new Node(Token.RETURN, 1, 1));
    root.setLastChild(new Node(Token.ALERT, 1, 1));

    NodeTraversal.traverseChangedFunctions(compiler, new NodeTraversal.FunctionCallback() {
      @Override
      public void visit(AbstractCompiler compiler, Node function) {
        function.setLastChild(root);
      }
    });

    NodeTraversal.traverse(compiler, root, eliminationPass);

    assertTrue(root.isFunction());
    assertTrue(root.getLastChild().isPresent());
  }

  @Test
  public void testEliminationOfDeadCodeInsideLoop() {
    Node root = new Node(Token.BLOCK, 1, 1);
    root.setFirstChild(new Node(Token.FOR, 1, 1));
    root.getFirstChild().setFirstChild(new Node(Token.RETURN, 1, 1));
    root.getFirstChild().setLastChild(new Node(Token.ALERT, 1, 1));
    root.setLastChild(new Node(Token.ALERT, 1, 1));

    NodeTraversal.traverse(compiler, root, eliminationPass);

    assertTrue(root.getLastChild().isPresent());
  }

  @Test
  public void testEliminationOfDeadCodeInsideSwitchStatement() {
    Node root = new Node(Token.BLOCK, 1, 1);
    root.setFirstChild(new Node(Token.SWITCH, 1, 1));
    root.getFirstChild().setFirstChild(
        new Node(Token.CASE, 1, 1, new Node(Token.RETURN, 1, 1)));
    root.getFirstChild().setLastChild(new Node(Token.ALERT, 1, 1));
    root.setLastChild(new Node(Token.ALERT, 1, 1));

    NodeTraversal.traverse(compiler, root, eliminationPass);

    assertTrue(root.getLastChild().isPresent());
  }

  @Test
  public void testEliminationOfNoOpStatement() {
    Node root = new Node(Token.BLOCK, 1, 1);
    root.setFirstChild(new Node(Token.VAR, 1, 1, new Node(Token.IDENT, 1, "x")));
    root.setLastChild(new Node(Token.VOID, 1, 1));

    NodeTraversal.traverse(compiler, root, eliminationPass);

    assertFalse(root.isEmpty());
  }

  @Test
  public void testEliminationOfNoOpStatementWithSideEffects() {
    Node root = new Node(Token.BLOCK, 1, 1);
    root.setFirstChild(new Node(Token.VAR, 1, 1, new Node(Token.IDENT, 1, "x")));
    root.getFirstChild().setLastChild(new Node(Token.ASSIGN, 1, 1, new Node(Token.IDENT, 1, "x"), new Node(Token.NUMBER, 1, 10)));
    root.setLastChild(new Node(Token.VOID, 1, 1));

    NodeTraversal.traverse(compiler, root, eliminationPass);

    assertFalse(root.isEmpty());
  }

  @Test
  public void testNoChangeWhenCodeIsReachable() {
    Node root = new Node(Token.BLOCK, 1, 1);
    root.setFirstChild(new Node(Token.ALERT, 1, 1));
    root.setLastChild(new Node(Token.ALERT, 1, 1));

    NodeTraversal.traverse(compiler, root, eliminationPass);

    assertFalse(root.isEmpty());
  }
}