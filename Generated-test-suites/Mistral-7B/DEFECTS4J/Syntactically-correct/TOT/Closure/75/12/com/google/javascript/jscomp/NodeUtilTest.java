import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newForStatement(1, null, null, null, null, null, null);
    Node nodeNotForIn = Node.newExpressionStatement(Node.newName("x"), null);

    assertTrue(isForIn(nodeForIn));
    assertFalse(isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newExpressionStatement(Node.newName("x"), null);
    Node nodeNotStatement = Node.newExpression(Token.IDENT, "x", null);

    assertTrue(isStatement(nodeStatement));
    assertFalse(isStatement(nodeNotStatement));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newFunction(
        1, null, "myFunction", null, Node.newBlock(null, null), null);
    Node nodeFunctionExpression = Node.newExpressionStatement(nodeFunctionDeclaration, null);
    Node nodeNotFunctionDeclaration = Node.newExpressionStatement(Node.newName("x"), null);

    assertTrue(isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isFunctionDeclaration(nodeFunctionExpression));
    assertFalse(isFunctionDeclaration(nodeNotFunctionDeclaration));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newBlock(null, null);
    Node nodeFunctionDeclaration = Node.newFunction(
        1, null, "myFunction", null, Node.newBlock(null, null), null);
    Node nodeFunctionInsideFunction = Node.newFunction(
        1, Node.newName("outerFunction"), "outerFunction", null,
        Node.newBlock(null, Node.newReturnStatement(nodeFunctionDeclaration, null)), null);
    Node nodeFunctionInsideScript = Node.newBlock(
        Node.newFunction(
            1, null, "myFunction", null, Node.newBlock(null, null), null), null);

    assertTrue(isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(isHoistedFunctionDeclaration(nodeFunctionInsideFunction));
    assertTrue(isHoistedFunctionDeclaration(nodeFunctionInsideScript));
  }
}