import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForIn();
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newName("x"));
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = Node.newExpression(Node.newName("x"), Node.NO_OP);
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(Node.newName("foo"), null, null, 0, null);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newExpressionStatement(Node.newExpression(Node.newName("foo"), Node.NO_OP));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression.getFirstChild()));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newBlock(Node.newFunction(Node.newName("myFunction"), null, null, 0, null));
    Node nodeFunctionDeclaration = nodeScript.getFirstChild();

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationInFunction = Node.newBlock(
        Node.newExpressionStatement(Node.newExpression(Node.newName("myFunction"), Node.NO_OP)),
        Node.newStatementList(Node.newExpressionStatement(Node.newExpression(Node.newName("x"), Node.NO_OP))));
    Node nodeFunctionDeclarationInFunctionChild = nodeFunctionDeclarationInFunction.getFirstChild().getFirstChild();

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunctionChild));
  }
}