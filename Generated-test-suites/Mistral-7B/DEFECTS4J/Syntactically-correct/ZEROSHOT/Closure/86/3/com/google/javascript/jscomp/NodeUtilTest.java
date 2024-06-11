import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{}, new Node[3]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{}, new Node[1]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.ASSIGN, new int[]{}, new Node[2]);
    Node statementParent = Node.newNode(Token.SCRIPT, new int[]{}, new Node[1]);
    statementParent.appendChild(nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newNode(Token.ASSIGN, new int[]{}, new Node[2]);
    Node expressionParent = Node.newNode(Token.IF, new int[]{}, new Node[1]);
    expressionParent.appendChild(nodeExpression);

    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{"id"}, new Node[1]);
    nodeFunctionDeclaration.setId("testFunction");
    Node functionDeclarationParent = Node.newNode(Token.BLOCK, new int[]{}, new Node[1]);
    functionDeclarationParent.appendChild(nodeFunctionDeclaration);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{}, new Node[1]);
    Node functionExpressionParent = Node.newNode(Token.ASSIGN, new int[]{}, new Node[2]);
    functionExpressionParent.appendChild(nodeFunctionExpression);

    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{"id"}, new Node[1]);
    nodeHoistedFunctionDeclaration.setId("testFunction");
    Node hoistedFunctionDeclarationParent = Node.newNode(Token.SCRIPT, new int[]{}, new Node[1]);
    hoistedFunctionDeclarationParent.appendChild(nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{"id"}, new Node[1]);
    nodeFunctionDeclaration.setId("testFunction");
    Node functionDeclarationParent = Node.newNode(Token.BLOCK, new int[]{}, new Node[1]);
    functionDeclarationParent.appendChild(nodeFunctionDeclaration);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeHoistedFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[]{"id"}, new Node[1]);
    nodeHoistedFunctionDeclarationInFunction.setId("testFunction");
    Node hoistedFunctionDeclarationParentInFunction = Node.newNode(Token.FUNCTION, new int[]{}, new Node[1]);
    hoistedFunctionDeclarationParentInFunction.appendChild(nodeHoistedFunctionDeclarationInFunction);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclarationInFunction));
  }
}