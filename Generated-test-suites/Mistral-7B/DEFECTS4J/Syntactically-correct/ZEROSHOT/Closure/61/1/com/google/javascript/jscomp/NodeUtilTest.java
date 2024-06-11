import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForInStatement(1, null, null, null);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newExpression(1));
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newExpression(1));
    assertTrue(isStatement(nodeStatement));

    Node nodeExpression = Node.newExpression(1);
    assertFalse(isStatement(nodeExpression));

    Node nodeFunction = Node.newFunction(1, null, null, null, null);
    assertTrue(isStatement(nodeFunction));

    Node nodeLabel = Node.newLabel(1, null);
    assertTrue(isStatement(nodeLabel));

    Node nodeScript = Node.newScript(1, null);
    assertTrue(isStatement(nodeScript));
  }

  @Test(expected = IllegalStateException.class)
  public void testIsStatementWithNullNode() {
    Node node = null;
    isStatement(node);
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(1, null, null, null, null);
    nodeFunctionDeclaration.setParent(Node.newBlockStatement(1, null));
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newExpression(Node.newFunction(1, null, null, null, null)));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression.getFirstChild()));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newFunction(1, null, null, null, null);
    nodeHoistedFunctionDeclaration.setParent(Node.newScript(1, null));
    assertTrue(isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newFunction(1, null, null, null, null);
    Node nodeFunction = Node.newFunction(1, null, null, null, null);
    nodeFunctionDeclarationInFunction.setParent(nodeFunction);
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
  }

  @Test(expected = IllegalStateException.class)
  public void testIsHoistedFunctionDeclarationWithNullNode() {
    Node node = null;
    isHoistedFunctionDeclaration(node);
  }
}