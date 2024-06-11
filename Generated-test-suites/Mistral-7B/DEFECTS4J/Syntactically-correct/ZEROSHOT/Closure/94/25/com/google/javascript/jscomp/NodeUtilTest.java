import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isForIn;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isFunctionDeclaration;
import static com.google.javascript.jscomp.jsonml.NodeUtil.isHoistedFunctionDeclaration;
import static com.google.javascript.rhino.Token.BLOCK;
import static com.google.javascript.rhino.Token.FUNCTION;
import static com.google.javascript.rhino.Token.FOR;
import static com.google.javascript.rhino.Token.LABEL;
import static com.google.javascript.rhino.Token.SCRIPT;

import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn(1, null, null, null, null, null, null);
    assertTrue(isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpression(1, null);
    assertFalse(isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpression(1, null);
    Node parentScript = Node.newScript(1, null);
    parentScript.addChildToEnd(nodeStatement);

    assertTrue(isStatement(nodeStatement));

    Node nodeFunctionStatement = Node.newFunction(
        1, "foo", null, null, null, null, null, null, null);
    parentScript.addChildToEnd(nodeFunctionStatement);

    assertTrue(isStatement(nodeFunctionStatement));

    Node nodeFunctionExpression = Node.newExpression(1,
        Node.newFunction(1, null, null, null, null, null, null, null, null));
    assertFalse(isStatement(nodeFunctionExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        1, "foo", null, null, null, null, null, null, null);
    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpression(1,
        Node.newFunction(1, null, null, null, null, null, null, null, null));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        1, "foo", null, null, null, null, null, null, null);
    Node parentScript = Node.newScript(1, null);
    parentScript.addChildToEnd(nodeFunctionDeclaration);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node parentFunction = Node.newExpression(1,
        Node.newFunction(1, null, null, null, null, null, null, null, null));
    parentFunction.addChildToEnd(nodeFunctionDeclaration);

    assertFalse(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
  }

  @Test(expected = IllegalStateException.class)
  public void testCheckStateFailed() {
    Node node = Node.newExpression(1, null);
    Preconditions.checkState(node.getParent() == null);
  }

}