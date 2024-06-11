import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

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

    Node nodeNotStatement = Node.newExpression("x + y");
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"), Node.newList(), Node.newList());
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(
        Node.newFunction(Node.newIdentifier("myFunction"), Node.newList(), Node.newList(), Node.newBlock(Node.newExpressionStatement(Node.newExpression("1")))));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"), Node.newList(), Node.newList(), Node.newBlock(Node.newExpressionStatement(Node.newExpression("1"))));
    Node parentScript = Node.newScript();
    parentScript.addChildToEnd(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newFunction(
        Node.newIdentifier("myFunction"), Node.newList(), Node.newList(), Node.newBlock(Node.newExpressionStatement(Node.newExpression("1"))));
    Node parentFunction = Node.newFunction(
        Node.newIdentifier("parentFunction"), Node.newList(), Node.newList(), Node.newBlock(Node.newExpressionStatement(Node.newExpression("2"))));
    parentFunction.addChildToEnd(nodeNotHoistedFunctionDeclaration);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }
}