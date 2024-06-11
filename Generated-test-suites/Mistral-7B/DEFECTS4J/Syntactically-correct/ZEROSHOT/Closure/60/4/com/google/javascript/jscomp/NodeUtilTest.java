import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import com.google.javascript.jscomp.jsonml.Node;
import com.google.javascript.rhino.Token;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = Node.newNode(Token.FOR, null, null, null, null, null, null, null);
    Node nodeNotForIn = Node.newNode(Token.IF, null, null, null, null, null, null, null);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = Node.newNode(Token.IF, null, null, null, null, null, null, null);
    Node nodeExpression = Node.newNode(Token.FUNCTION, null, null, null, null, null, null, null);

    assertTrue(NodeUtil.isStatement(nodeStatement));
    assertFalse(NodeUtil.isStatement(nodeExpression));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, "myFunction", null, null, null, null, null, null);
    Node nodeFunctionExpression = Node.newNode(Token.FUNCTION, null, null, null, null, null, null, null);

    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeScript = Node.newNode(Token.SCRIPT, null, null, null, null, null, null, null);
    Node nodeFunctionDeclaration = Node.newNode(Token.FUNCTION, "myFunction", null, null, null, null, null, null);
    Node nodeFunctionDeclarationInFunction = Node.newNode(Token.FUNCTION, "outerFunction", null, null, nodeFunctionDeclaration, null, null, null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclaration));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeScript));
  }
}