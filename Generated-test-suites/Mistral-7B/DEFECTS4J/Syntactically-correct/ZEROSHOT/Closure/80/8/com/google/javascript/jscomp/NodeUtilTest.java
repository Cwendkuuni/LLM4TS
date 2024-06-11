import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Test class for NodeUtil.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new Object[]{}, null);
    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new Object[]{}, null);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new Object[]{}, null);
    Node nodeFunction = new Node(Token.FUNCTION, new int[]{}, new Object[]{}, null);
    Node nodeIf = new Node(Token.IF, new int[]{}, new Object[]{}, null);
    Node nodeIdentifier = new Node(Token.IDENT, new int[]{}, new Object[]{""}, null);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeFunction));
    assertTrue(NodeUtil.isStatement(nodeIf));
    assertFalse(NodeUtil.isStatement(nodeIdentifier));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new Object[]{"name"}, null);
    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new Object[]{null}, null);
    Node nodeVariable = new Node(Token.VAR, new int[]{}, new Object[]{}, null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeVariable));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclarationInRoot = new Node(Token.FUNCTION, new int[]{}, new Object[]{"name"}, null);
    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{}, new Object[]{"name"}, null);
    Node nodeFunctionDeclarationInBlock = new Node(Token.FUNCTION, new int[]{}, new Object[]{"name"}, null);
    Node nodeFunctionExpressionInRoot = new Node(Token.FUNCTION, new int[]{}, new Object[]{null}, null);
    Node nodeFunctionExpressionInFunction = new Node(Token.FUNCTION, new int[]{}, new Object[]{null}, null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInRoot));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInBlock));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionExpressionInRoot));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionExpressionInFunction));
  }
}