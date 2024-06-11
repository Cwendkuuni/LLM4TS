import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private UnreachableCodeElimination elimination;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    elimination = new UnreachableCodeElimination(compiler, false);
  }

  @Test
  public void testRemoveDeadCodeFollowingReturn() {
    Node root = new AbstractNode(Token.SCRIPT, null);
    root.addChild(new AbstractNode(Token.RETURN, null));
    root.addChild(new AbstractNode(Token.PRINT, null));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.setPostOrderCallback(elimination);
    traversal.traverse();

    assertTrue(elimination.curCfg.getDirectedGraphNode(root.getFirstChild())
        .getAnnotation() == GraphReachability.UNREACHABLE);
    assertFalse(NodeUtil.hasChild(root, elimination.curCfg.getDirectedGraphNode(root.getFirstChild())
        .getDestination().getValue()));
  }

  @Test
  public void testRemoveDeadCodeFollowingBreak() {
    Node root = new AbstractNode(Token.SCRIPT, null);
    Node loop = new AbstractNode(Token.FOR, null);
    root.addChild(loop);
    loop.addChild(new AbstractNode(Token.BREAK, null));
    loop.addChild(new AbstractNode(Token.PRINT, null));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.setPostOrderCallback(elimination);
    traversal.traverse();

    assertTrue(elimination.curCfg.getDirectedGraphNode(loop.getChild(0))
        .getAnnotation() == GraphReachability.UNREACHABLE);
    assertFalse(NodeUtil.hasChild(root, elimination.curCfg.getDirectedGraphNode(loop.getChild(0))
        .getDestination().getValue()));
  }

  @Test
  public void testRemoveDeadCodeFollowingContinue() {
    Node root = new AbstractNode(Token.SCRIPT, null);
    Node loop = new AbstractNode(Token.FOR, null);
    root.addChild(loop);
    loop.addChild(new AbstractNode(Token.CONTINUE, null));
    loop.addChild(new AbstractNode(Token.PRINT, null));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.setPostOrderCallback(elimination);
    traversal.traverse();

    assertTrue(elimination.curCfg.getDirectedGraphNode(loop.getChild(0))
        .getAnnotation() == GraphReachability.UNREACHABLE);
    assertFalse(NodeUtil.hasChild(root, elimination.curCfg.getDirectedGraphNode(loop.getChild(0))
        .getDestination().getValue()));
  }

  @Test
  public void testRemoveDeadCodeNoOpStatement() {
    Node root = new AbstractNode(Token.SCRIPT, null);
    root.addChild(new AbstractNode(Token.PRINT, null));
    root.addChild(new AbstractNode(Token.TRUE, null));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.setPostOrderCallback(elimination);
    traversal.traverse();

    assertFalse(elimination.curCfg.getDirectedGraphNode(root.getLastChild())
        .getAnnotation() == GraphReachability.UNREACHABLE);
    assertTrue(NodeUtil.hasChild(root, elimination.curCfg.getDirectedGraphNode(root.getLastChild())
        .getDestination().getValue()));
  }

  @Test
  public void testRemoveDeadCodeNoOpStatementWithSideEffect() {
    Node root = new AbstractNode(Token.SCRIPT, null);
    root.addChild(new AbstractNode(Token.PRINT, null));
    root.addChild(new AbstractNode(Token.TRUE, null));
    root.addChild(new AbstractNode(Token.ASSIGN, null, root.getLastChild(), new AbstractNode(Token.IDENT, "a")));

    NodeTraversal traversal = new NodeTraversal(compiler, root);
    traversal.setPostOrderCallback(elimination);
    traversal.traverse();

    assertFalse(elimination.curCfg.getDirectedGraphNode(root.getLastChild())
        .getAnnotation() == GraphReachability.UNREACHABLE);
    assertTrue(NodeUtil.hasChild(root, elimination.curCfg.getDirectedGraphNode(root.getLastChild())
        .getDestination().getValue()));
  }
}