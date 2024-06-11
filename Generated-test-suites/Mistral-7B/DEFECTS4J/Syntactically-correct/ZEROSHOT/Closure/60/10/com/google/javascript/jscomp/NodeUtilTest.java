import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Test class for NodeUtil methods.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] {1, 2, 3}, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[] {1, 2}, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[] {1, 2}, null);
    Node parentNodeStatement = Node.newNode(Token.BLOCK, new int[] {}, null);
    parentNodeStatement.addChildToEnd(nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newNode(Token.IDENT, new int[] {1}, null);
    assertFalse(NodeUtil.isStatement(nodeNotStatement));

    Node nodeParentNotStatement = Node.newNode(Token.IF, new int[] {1, 2}, null);
    assertFalse(NodeUtil.isStatement(nodeParentNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 1}, null);
    nodeFunctionDeclaration.addChildToEnd(Node.newNode(Token.IDENT, new int[] {1}, null));
    nodeFunctionDeclaration.addChildToEnd(Node.newNode(Token.BLOCK, new int[] {}, null));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] {1, 1}, null);
    nodeFunctionExpression.addChildToEnd(Node.newNode(Token.IDENT, new int[] {1}, null));
    nodeFunctionExpression.addChildToEnd(Node.newNode(Token.BLOCK, new int[] {}, null));

    Node parentNodeFunctionExpression = Node.newNode(Token.ASSIGN, new int[] {1, 1}, null);
    parentNodeFunctionExpression.addChildToEnd(nodeFunctionExpression);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] {1, 1}, null);
    nodeFunctionDeclaration.addChildToEnd(Node.newNode(Token.IDENT, new int[] {1}, null));
    nodeFunctionDeclaration.addChildToEnd(Node.newNode(Token.BLOCK, new int[] {}, null));

    Node scriptNode = Node.newNode(Token.SCRIPT, new int[] {}, null);
    scriptNode.addChildToEnd(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[] {1, 1}, null);
    nodeFunctionDeclarationInFunction.addChildToEnd(Node.newNode(Token.IDENT, new int[] {1}, null));
    nodeFunctionDeclarationInFunction.addChildToEnd(Node.newNode(Token.BLOCK, new int[] {}, null));

    Node functionNode = Node.newNode(Token.FUNCTION, new int[] {1, 1}, null);
    functionNode.addChildToEnd(Node.newNode(Token.IDENT, new int[] {1}, null));
    functionNode.addChildToEnd(nodeFunctionDeclarationInFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }
}