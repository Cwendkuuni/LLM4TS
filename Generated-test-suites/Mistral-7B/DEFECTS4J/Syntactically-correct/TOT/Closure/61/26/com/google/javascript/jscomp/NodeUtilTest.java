import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newName("x"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newExpression(Node.newName("x"), Node.newNumber(1));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"), null, Node.newBlock(Node.newExpressionStatement(Node.newExpression(Node.newName("x"), Node.newNumber(1)))));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction(Node.newIdentifier("myFunction"), null, Node.newBlock(Node.newExpressionStatement(Node.newExpression(Node.newName("x"), Node.newNumber(1))))));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"), null, Node.newBlock(Node.newExpressionStatement(Node.newExpression(Node.newName("x"), Node.newNumber(1)))));
    Node parentScript = Node.newScript();
    parentScript.addChild(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newFunction(
        Node.newIdentifier("outerFunction"), null, Node.newBlock(Node.newFunction(
            Node.newIdentifier("myFunction"), null, Node.newBlock(Node.newExpressionStatement(Node.newExpression(Node.newName("x"), Node.newNumber(1)))))));
    Node parentFunction = Node.newFunction(Node.newIdentifier("outerFunction"), null, Node.newBlock());
    parentFunction.addChild(nodeFunctionDeclarationInFunction);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeStatementInFunction = Node.newExpressionStatement(Node.newExpression(Node.newName("x"), Node.newNumber(1)));
    Node parentStatement = Node.newBlock();
    parentStatement.addChild(nodeStatementInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeStatementInFunction));
  }
}