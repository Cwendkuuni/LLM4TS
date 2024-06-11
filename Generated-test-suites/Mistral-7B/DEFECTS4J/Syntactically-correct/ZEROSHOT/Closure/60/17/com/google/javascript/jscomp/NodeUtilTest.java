import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Test cases for the NodeUtil class.
 */
public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn(null, null, null, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeScript = Node.newScript(null);
    assertTrue(NodeUtil.isStatement(nodeScript));

    Node nodeExpression = Node.newExpressionStatement(Node.newIdentifier("x"));
    assertTrue(NodeUtil.isStatement(nodeExpression));

    Node nodeFunction = Node.newFunction(null, null, null, null);
    assertTrue(NodeUtil.isStatement(nodeFunction));

    Node nodeLabel = Node.newLabel("label");
    assertTrue(NodeUtil.isStatement(nodeLabel));

    Node nodeVariable = Node.newVariable("x");
    assertFalse(NodeUtil.isStatement(nodeVariable));

    Node nodeIf = Node.newIfStatement(null, null, null);
    assertTrue(NodeUtil.isStatement(nodeIf));

    Node nodeArray = Node.newArrayLiteral(Node.newNumberLiteral(1));
    assertFalse(NodeUtil.isStatement(nodeArray));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunction = Node.newFunction(null, null, null, null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunction));

    Node nodeFunctionExpression = Node.newExpressionStatement(nodeFunction);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeIdentifier = Node.newIdentifier("foo");
    assertFalse(NodeUtil.isFunctionDeclaration(nodeIdentifier));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionInScript = Node.newFunction(null, null, null, null);
    Node nodeScript = Node.newScript(nodeFunctionInScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInScript));

    Node nodeFunctionInFunction = Node.newFunction(null, null, null, null);
    Node nodeFunction = Node.newFunction(null, null, null, nodeFunctionInFunction);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionInFunction));
  }
}