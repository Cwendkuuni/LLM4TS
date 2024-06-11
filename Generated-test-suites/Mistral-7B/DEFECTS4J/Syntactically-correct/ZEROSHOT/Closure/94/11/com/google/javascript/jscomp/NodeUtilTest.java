import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.jscomp.jsonml.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, null, null, null, null);
    Node nodeNotForIn = Node.newNode(Token.IF, null, null, null, null);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null);
    Node nodeBlock = Node.newNode(Token.BLOCK, null, null, null, null);
    Node nodeLabel = Node.newNode(Token.LABEL, "label", null, null, null);
    Node nodeFunction = Node.newNode(Token.FUNCTION, "functionName", null, null, null);
    Node nodeExpression = Node.newNode(Token.BINARY_MINUS, null, null, null, null);

    assertTrue(NodeUtil.isStatement(nodeScript));
    assertTrue(NodeUtil.isStatement(nodeBlock));
    assertTrue(NodeUtil.isStatement(nodeLabel));
    assertFalse(NodeUtil.isStatement(nodeFunction));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, "functionName", null, null, null);
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, null, null, null, null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null);
    Node nodeFunctionScript = Node.newNode(Token.FUNCTION, "functionName", null, null, null);
    Node nodeFunction = Node.newNode(Token.FUNCTION, "functionName", null, null, null);
    Node nodeBlockFunction = Node.newNode(Token.BLOCK, null, nodeFunction, null, null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionScript));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeBlockFunction));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsHoistedFunctionDeclarationNullNode() {
    NodeUtil.isHoistedFunctionDeclaration(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsStatementNullNode() {
    NodeUtil.isStatement(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsFunctionDeclarationNullNode() {
    NodeUtil.isFunctionDeclaration(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIsForInNullNode() {
    NodeUtil.isForIn(null);
  }
}