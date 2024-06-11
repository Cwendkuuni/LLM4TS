import static org.junit.Assert.*;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @ org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, new int[] { 0, 1, 2 });
    Node nodeNotForIn = Node.newNode(Token.IF, new int[] { 0, 1 });

    assertTrue("Node represents FOR-IN loop", isForIn(nodeForIn));
    assertFalse("Node does not represent FOR-IN loop", isForIn(nodeNotForIn));
  }

  @ org.junit.Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, new int[] { 0, 1 });
    Node nodeExpression = Node.newNode(Token.FUNCTION, new int[] { 0, 1, 2 });

    assertTrue("Node is used as a statement", isStatement(nodeStatement));
    assertFalse("Node is not used as a statement", isStatement(nodeExpression));
  }

  @ org.junit.Test
  public void testIsFunctionDeclarationAndIsHoistedFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, new int[] { 0, 1, 2, 3 }, "myFunction");
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, new int[] { 0, 1, 2 }, null);

    Node nodeFunctionDeclarationInScript = Node.newNode(Token.SCRIPT, new int[] { 0 }, Node.newNode(nodeFunctionDeclaration, new int[]{}));
    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, new int[] { 0, 1 },
        Node.newNode(Node.newNode(Token.BLOCK, new int[]{}), new int[]{}), Node.newNode(nodeFunctionDeclaration, new int[]{}));

    Node nodeFunctionExpressionInFunction = Node.newNode(Token.FUNCTION, new int[] { 0, 1 }, null, Node.newNode(nodeFunctionExpression, new int[]{}));

    assertTrue("Node is a function declaration", isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse("Node is not a function declaration", isFunctionDeclaration(nodeFunctionExpression));

    assertTrue("Node is a hoisted function declaration", isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));
    assertFalse("Node is not a hoisted function declaration", isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse("Node is not a hoisted function declaration", isHoistedFunctionDeclaration(nodeFunctionExpressionInFunction));
  }
}