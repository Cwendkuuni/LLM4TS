import static org.junit.Assert.*;
import com.google.javascript.jscomp.jsonml.NodeUtil;
import com.google.common.base.Preconditions;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[]{0, 1, 2}, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = Node.newNode(Token.IF, new int[]{0, 1}, null);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[]{0, 1}, null);
    Node parentScript = Node.newNode(Token.SCRIPT, new int[]{0}, null);
    parentScript.addChildToEnd(nodeStatement);

    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeNotStatement = Node.newNode(Token.IF, new int[]{0, 1}, null);
    assertFalse(NodeUtil.isStatement(nodeNotStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{0, 1}, null);
    nodeFunctionDeclaration.addChildToEnd(Node.newNode(Token.Identifier, new int[]{0}, "testFunction"));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[]{0, 1}, null);
    nodeFunctionExpression.addChildToEnd(Node.newNode(Token.Identifier, new int[]{0}, "testFunction"));
    nodeFunctionExpression.addChildToEnd(Node.newNode(Token.PUNCTUATION, new int[]{0}, '('));
    nodeFunctionExpression.addChildToEnd(Node.newNode(Token.PUNCTUATION, new int[]{0}, ')'));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{0, 1}, null);
    nodeHoistedFunctionDeclaration.addChildToEnd(Node.newNode(Token.Identifier, new int[]{0}, "testFunction"));
    Node parentScript = Node.newNode(Token.SCRIPT, new int[]{0}, null);
    parentScript.addChildToEnd(nodeHoistedFunctionDeclaration);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[]{0, 1}, null);
    nodeNotHoistedFunctionDeclaration.addChildToEnd(Node.newNode(Token.Identifier, new int[]{0}, "testFunction"));
    Node parentFunction = Node.newNode(Token.FUNCTION, new int[]{0, 1}, null);
    parentFunction.addChildToEnd(nodeNotHoistedFunctionDeclaration);
    parentScript.addChildToEnd(parentFunction);

    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));
  }

  @org.junit.Test(expected = IllegalStateException.class)
  public void testCheckState() {
    Node nodeNullParent = Node.newNode(Token.SCRIPT, new int[]{0}, null);
    Node nullNode = Node.newNode(Token.SCRIPT, new int[]{0}, null);
    nodeNullParent.addChildToEnd(nullNode);

    NodeUtil.isStatement(nullNode);
  }
}