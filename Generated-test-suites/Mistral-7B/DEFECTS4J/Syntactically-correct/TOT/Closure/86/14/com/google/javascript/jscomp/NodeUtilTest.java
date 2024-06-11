import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.common.truth.Truth.assertThat;

import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement(1, new Node[]{}, new Node[]{});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newFunction(1, new Node[0], null, null));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newNumberLiteral(1));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newExpression(Node.newNumberLiteral(1));
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(1, new Node[]{}, null, null);
    nodeFunctionDeclaration.setName(Node.newIdentifier("testFunction"));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction(1, new Node[0], null, null));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(1, new Node[0], null, null);
    nodeHoistedFunctionDeclaration.setName(Node.newIdentifier("testFunction"));
    Node parentScript = Node.newScript(new Node[0]);
    parentScript.appendChild(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newFunction(1, new Node[0], null, null);
    nodeFunctionDeclarationInFunction.setName(Node.newIdentifier("testFunction"));
    Node parentFunction = Node.newFunction(1, new Node[0], null, null);
    parentFunction.appendChild(nodeFunctionDeclarationInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test
  public void testIsFunctionExpression() { // Missing method in NodeUtil, but should be false if method name is changed to isFunctionExpression
    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newFunction(1, new Node[0], null, null));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(NodeUtil.isFunctionExpression(nodeFunctionExpression)); // Should be added

    Node nodeStatementFunctionExpression = Node.newExpressionStatement(Node.newFunction(1, new Node[0], null, Node.newExpressionStatement(Node.newExpressionStatement(Node.newExpressionStatement(Node.newFunction(1, new Node[0], null, null))), nodeFunctionExpression)));
    assertFalse(NodeUtil.isStatement(nodeStatementFunctionExpression));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeStatementFunctionExpression));
    assertFalse(NodeUtil.isFunctionExpression(nodeStatementFunctionExpression)); // Should be added
  }

  @Test
  public void testIsStatementAndIsFunctionDeclarationForSameNode() {
    Node nodeStatementFunction = Node.newExpressionStatement(Node.newFunction(1, new Node[0], null, null));
    assertTrue(NodeUtil.isStatement(nodeStatementFunction));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeStatementFunction));
    assertTrue(NodeUtil.isStatement(nodeStatementFunction) && NodeUtil.isFunctionDeclaration(nodeStatementFunction)
        == NodeUtil.isStatement(nodeStatementFunction));
  }

  @Test
  public void testIsStatementAndIsFunctionDeclarationForDifferentNodes() {
    Node nodeStatement = Node.newExpressionStatement(Node.newExpressionStatement(Node.newNumberLiteral(1)));
    Node nodeFunctionDeclaration = Node.newFunction(1, new Node[0], null, null);
    nodeFunctionDeclaration.setName(Node.newIdentifier("testFunction"));
    assertFalse(NodeUtil.isStatement(nodeFunctionDeclaration) && NodeUtil.isFunctionDeclaration(nodeStatement)
        == NodeUtil.isStatement(nodeFunctionDeclaration) && NodeUtil.isFunctionDeclaration(nodeStatement));
  }
}