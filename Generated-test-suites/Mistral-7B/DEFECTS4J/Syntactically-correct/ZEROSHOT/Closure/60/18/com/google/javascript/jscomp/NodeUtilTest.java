import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, null, null, null, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, null, null, null, null, null, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, null, null, null, null, null, null);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.EXPRESSION_STATEMENT, null, null, null, null, null, null);
    assertTrue(NodeUtil.isStatement(nodeExpression));

    Node nodeNotStatement = Node.newNode(Token.IDENTIFIER, null, null, null, null, null, null);
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(
        Token.FUNCTION, null, null, null, null, null, null);
    nodeFunctionDeclaration.setProperty("name", "testFunction");
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(
        Token.FUNCTION, null, null, null, null, null, null);
    nodeFunctionExpression.setProperty("name", "testFunction");
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(
        Token.FUNCTION, Node.newNode(Token.IDENTIFIER, null, null, null, null, null, null), null, null, null, null, null);
    nodeHoistedFunctionDeclaration.setProperty("name", "testFunction");
    Node parent = Node.newNode(Token.SCRIPT, null, null, null, null, null, null);
    nodeHoistedFunctionDeclaration.setParent(parent);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newNode(
        Token.FUNCTION, Node.newNode(Token.IDENTIFIER, null, null, null, null, null, null), null, null, null, null, null);
    nodeFunctionDeclarationInFunction.setProperty("name", "testFunction");
    Node parentFunction = Node.newNode(Token.FUNCTION, null, null, null, null, null, null);
    nodeFunctionDeclarationInFunction.setParent(parentFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeNotHoistedFunctionDeclaration = Node.newNode(
        Token.FUNCTION, Node.newNode(Token.IDENTIFIER, null, null, null, null, null, null), null, null, null, null, null);
    nodeNotHoistedFunctionDeclaration.setProperty("name", "testFunction");
    Node parentScript = Node.newNode(Token.SCRIPT, null, null, null, null, null, null);
    nodeNotHoistedFunctionDeclaration.setParent(parentScript);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }
}