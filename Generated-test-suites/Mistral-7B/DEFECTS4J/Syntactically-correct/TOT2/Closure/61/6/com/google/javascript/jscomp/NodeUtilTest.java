import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null);
    Node nodeNotForIn = new Node(Token.IF, null);

    assertTrue(NodeUtil.isForIn(nodeForIn));
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @org.junit.Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.PRINT, null);
    Node expressionNode = new Node(Token.MUL, null);

    assertTrue(NodeUtil.isStatement(statementNode));
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  @org.junit.Test
  public void testIsFunctionDeclaration() {
    Node functionDeclarationNode = new Node(Token.FUNCTION, new Node("funcName", null));
    Node functionExpressionNode = new Node(Token.FUNCTION, null);

    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclarationNode));
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpressionNode));
  }

  @org.junit.Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new Node("funcName", null));
    Node notHoistedFunctionDeclarationNode = new Node(Token.FUNCTION, new Node("funcName", null));
    Node scriptNode = new Node(Token.SCRIPT, null);
    Node functionNode = new Node(Token.FUNCTION, null);

    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(notHoistedFunctionDeclarationNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(scriptNode));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(functionNode));
  }
}