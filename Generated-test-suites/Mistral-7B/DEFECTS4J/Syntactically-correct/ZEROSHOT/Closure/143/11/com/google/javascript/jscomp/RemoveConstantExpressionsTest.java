import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.NodeUtil;
import org.junit.Before;
import org.junit.Test;

public class RemoveConstantExpressionsTest {

  private AbstractCompiler compiler;
  private Result result;

  @Before
  public void setup() {
    compiler = new AbstractCompiler() {
      @Override
      public CompilerPass getPass(String passName) {
        if (RemoveConstantExpressions.class.getName().equals(passName)) {
          return new RemoveConstantExpressions(this);
        }
        return null;
      }
    };
    result = new Result();
  }

  @Test
  public void testRemoveConstantExpressions() {
    Node node = new Node(Token.PROGRAM, null);
    node.addChild(new Node(Token.EXPR_STATEMENT, null));
    Node statement = node.getLastChild();
    statement.addChild(new Node(Token.ADD, null));
    statement.getFirstChild().addChild(new Node(Token.NUMBER, 1));
    statement.getFirstChild().addSibling(
        new Node(Token.IDENT, "foo"));
    statement.getLastChild().addChild(new Node(Token.IDENT, "bar"));

    NodeTraversal.traverse(null, node, new NodePrinter());
    compiler.process(null, node);
    NodeTraversal.traverse(null, node, new NodePrinter());

    Node expectedNode = new Node(Token.PROGRAM, null);
    Node expectedStatement = expectedNode.addChild(new Node(Token.EXPR_STATEMENT, null));
    expectedStatement.addChild(new Node(Token.IDENT, "foo"));
    expectedStatement.addSibling(new Node(Token.EXPR_STATEMENT, null));
    expectedStatement.getLastChild().addChild(new Node(Token.IDENT, "bar"));

    assertTrue(NodeUtil.isTreeEquivalent(node, expectedNode));
    assertFalse(result.changed);
  }

  private static class NodePrinter implements AbstractPostOrderCallback {
    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      if (node.getType() == Token.PROGRAM) {
        System.out.println("Program:\n");
        NodeUtil.printTree(node);
      }
    }
  }
}