import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import java.util.List;

public class UnreachableCodeEliminationTest {

  @SuppressWarnings("unchecked")
  private static <T> T newInstance(Class<?> clazz, Object... initargs)
      throws InstantiationException, IllegalAccessException {
    return (T) clazz.getConstructor(AbstractCompiler.class, boolean.class).newInstance(
        new AbstractCompiler(null, null), false);
  }

  @org.junit.Test
  public void testUnreachableCodeElimination() {
    AbstractCompiler compiler = newInstance(AbstractCompiler.class);
    UnreachableCodeElimination elimination = newInstance(UnreachableCodeElimination.class, compiler, false);

    Node rootNode = Node.newBlock(Node.newText("function foo() {"));
    rootNode.addChild(Node.newReturn(Node.newText("return 42;")));
    rootNode.addChild(Node.newText("alert('unreachable');"));

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode);
    elimination.process(Node.nullNode(), rootNode);
    NodeTraversal.traverse(compiler, rootNode, elimination);

    List<Node> children = NodeUtil.getChildren(rootNode);
    assertFalse("There should be no alert statement.", children.stream().anyMatch(n -> n.getType() == Token.ALERT));
    assertTrue("Return statement should still be present.", children.stream().anyMatch(n -> n.getType() == Token.RETURN));

    elimination = newInstance(UnreachableCodeElimination.class, compiler, true);
    elimination.process(Node.nullNode(), rootNode);
    NodeTraversal.traverse(compiler, rootNode, elimination);

    children = NodeUtil.getChildren(rootNode);
    assertTrue("Return statement should be removed.", children.isEmpty());

    rootNode = Node.newBlock(Node.newText("function foo() {"));
    rootNode.addChild(Node.newIf(Node.newBooleanLiteral(true), Node.nullNode(), Node.nullNode()));
    rootNode.addChild(Node.newText("alert('unreachable');"));

    elimination = newInstance(UnreachableCodeElimination.class, compiler, false);
    elimination.process(Node.nullNode(), rootNode);
    NodeTraversal.traverse(compiler, rootNode, elimination);

    children = NodeUtil.getChildren(rootNode);
    assertFalse("There should be no alert statement.", children.stream().anyMatch(n -> n.getType() == Token.ALERT));

    elimination = newInstance(UnreachableCodeElimination.class, compiler, true);
    elimination.process(Node.nullNode(), rootNode);
    NodeTraversal.traverse(compiler, rootNode, elimination);

    children = NodeUtil.getChildren(rootNode);
    assertTrue("If statement should still be present.", children.size() == 1);

    rootNode = Node.newBlock(Node.newText("function foo() {"));
    rootNode.addChild(Node.newIf(Node.newBooleanLiteral(false), Node.nullNode(), Node.nullNode()));
    rootNode.addChild(Node.newText("alert('unreachable');"));

    elimination = newInstance(UnreachableCodeElimination.class, compiler, false);
    elimination.process(Node.nullNode(), rootNode);
    NodeTraversal.traverse(compiler, rootNode, elimination);

    children = NodeUtil.getChildren(rootNode);
    assertTrue("If statement should be removed.", children.isEmpty());

    rootNode = Node.newBlock(Node.newText("function foo() {"));
    rootNode.addChild(Node.newText("if (true) { alert('unreachable'); }"));

    elimination = newInstance(UnreachableCodeElimination.class, compiler, false);
    elimination.process(Node.nullNode(), rootNode);
    NodeTraversal.traverse(compiler, rootNode, elimination);

    children = NodeUtil.getChildren(rootNode);
    assertNull("There should be no alert statement.", children.get(0));
  }
}