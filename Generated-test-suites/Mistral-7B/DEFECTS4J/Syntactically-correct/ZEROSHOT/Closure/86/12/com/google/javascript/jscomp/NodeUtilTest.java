import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{0, 0, 0});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{0, 0, 0});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = new Node(Token.ASSIGN, new int[]{}, new int[]{0, 0, 0});
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(
        Token.FUNCTION, new int[]{}, new int[]{0, 0, 0, 0});
    nodeFunctionDeclaration.setProperty("name", "testFunction");

    Node nodeNotFunctionDeclaration = new Node(
        Token.IF, new int[]{}, new int[]{0, 0, 0});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeNotFunctionDeclaration));

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = new Node(Token.SCRIPT, new int[]{}, new int[]{0, 0, 0});
    Node nodeFunctionDeclaration = new Node(
        Token.FUNCTION, new int[]{}, new int[]{0, 0, 0, 0});
    nodeFunctionDeclaration.setProperty("name", "testFunction");
    nodeScript.addChild(nodeFunctionDeclaration);

    Node nodeFunctionExpression = new Node(
        Token.FUNCTION, new int[]{}, new int[]{0, 0, 0, 0});
    nodeFunctionExpression.setProperty("name", "testFunction");
    Node nodeStatement = new Node(Token.ASSIGN, new int[]{}, new int[]{0, 0, 0});
    nodeFunctionExpression.addChild(nodeStatement);
    nodeScript.addChild(nodeFunctionExpression);

    Node nodeFunctionInExpression = new Node(
        Token.FUNCTION, new int[]{}, new int[]{0, 0, 0, 0});
    nodeFunctionInExpression.setProperty("name", "testFunction");
    Node nodeExpression = new Node(Token.ASSIGN, new int[]{}, new int[]{0, 0, 0});
    nodeFunctionInExpression.addChild(nodeExpression);
    Node nodeExpressionStatement = new Node(
        Token.EXPRESSION_STATEMENT, new int[]{}, new int[]{0, 0, 0});
    nodeExpressionStatement.addChild(nodeFunctionInExpression);
    nodeScript.addChild(nodeExpressionStatement);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInExpression));
  }
}