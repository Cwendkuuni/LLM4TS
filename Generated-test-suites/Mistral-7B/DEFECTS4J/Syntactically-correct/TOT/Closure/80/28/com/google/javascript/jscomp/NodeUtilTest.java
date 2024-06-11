import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[] {1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[] {1, 2});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[] {1, 2});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.ADD, new int[] {1, 2, 3});
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[] {1, 2},
        new Node[] {new Node(Token.IDENTIFIER, new int[] {1, 2}),
            new Node(Token.BLOCK, new int[] {3})});
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[] {1, 2},
        new Node[] {new Node(Token.IDENTIFIER, new int[] {1, 2}),
            new Node(Token.EQUALS, new int[] {3}),
            new Node(Token.BLOCK, new int[] {4})});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeRootFunction = new Node(Token.FUNCTION, new int[] {1, 2},
        new Node[] {new Node(Token.IDENTIFIER, new int[] {1, 2}),
            new Node(Token.BLOCK, new int[] {3})});
    Node root = new Node(Token.SCRIPT, new int[] {0}, new Node[] {nodeRootFunction});
    Node hoistedFunction = root.getChild(0);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunction));

    Node nodeFunction = new Node(Token.FUNCTION, new int[] {1, 2},
        new Node[] {new Node(Token.IDENTIFIER, new int[] {1, 2}),
            new Node(Token.BLOCK, new int[] {3})});
    Node block = new Node(Token.BLOCK, new int[] {2}, new Node[] {nodeFunction});
    Node parentScript = new Node(Token.SCRIPT, new int[] {0}, new Node[] {block});
    Node notHoistedFunction = parentScript.getChild(0).getChild(0);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunction));
  }

  @Test
  public void testIsStatementAndIsForInWithInvalidNodes() {
    Node invalidStatement = new Node(Token.INVALID, new int[] {1, 2});
    assertNull(NodeUtil.isStatement(invalidStatement));

    Node invalidForIn = new Node(Token.INVALID, new int[] {1, 2, 3});
    assertNull(NodeUtil.isForIn(invalidForIn));
  }

  @Test
  public void testIsStatementAndIsFunctionDeclarationWithInvalidNodes() {
    Node invalidStatement = new Node(Token.INVALID, new int[] {1, 2});
    assertNull(NodeUtil.isFunctionDeclaration(invalidStatement));
  }

  @Test
  public void testIsFunctionDeclarationWithNullNode() {
    assertNull(NodeUtil.isFunctionDeclaration(null));
  }
}