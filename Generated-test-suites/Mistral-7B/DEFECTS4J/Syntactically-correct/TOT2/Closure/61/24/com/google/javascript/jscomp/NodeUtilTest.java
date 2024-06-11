import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeForInWithInit = new Node(Token.FOR, new int[]{}, new int[]{Token.VAR, Token.ASSIGN, new Node(Token.IDENT, new int[]{}, new int[]{})}, new int[]{});
    assertTrue(NodeUtil.isForIn(nodeForInWithInit));

    Node nodeForInWithUpdate = new Node(Token.FOR, new int[]{}, new int[]{}, new Node(Token.ASSIGN, new int[]{}, new Node(Token.IDENT, new int[]{}, new int[]{}), new Node(Token.IDENT, new int[]{}, new int[]{}), new int[]{}));
    assertTrue(NodeUtil.isForIn(nodeForInWithUpdate));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));

    Node nodeNotForInWithStatement = new Node(Token.IF, new int[]{}, new int[]{Token.STATEMENT});
    assertFalse(NodeUtil.isForIn(nodeNotForInWithStatement));

    Node nodeNull = null;
    try {
      NodeUtil.isForIn(nodeNull);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.IF, new int[]{}, new int[]{});
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeStatementWithExpression = new Node(Token.IF, new int[]{}, new int[]{Token.EXPRESSION});
    assertTrue(NodeUtil.isStatement(nodeStatementWithExpression));

    Node nodeStatementWithBlock = new Node(Token.IF, new int[]{}, new int[]{Token.BLOCK});
    assertTrue(NodeUtil.isStatement(nodeStatementWithBlock));

    Node nodeExpression = new Node(Token.IDENT, new int[]{}, new int[]{});
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeBlock = new Node(Token.BLOCK, new int[]{}, new int[]{});
    assertFalse(NodeUtil.isStatement(nodeBlock));

    Node nodeNull = null;
    try {
      NodeUtil.isStatement(nodeNull);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{});
    nodeFunctionDeclaration.setProperty("id", new Node(Token.IDENT, new int[]{}, new int[]{}));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionDeclarationWithParameters = new Node(Token.FUNCTION, new int[]{}, new int[]{Token.IDENT, Token.IDENT}, new int[]{});
    nodeFunctionDeclarationWithParameters.setProperty("id", new Node(Token.IDENT, new int[]{}, new int[]{}));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationWithParameters));

    Node nodeFunctionDeclarationWithBody = new Node(Token.FUNCTION, new int[]{}, new int[]{Token.BLOCK}, new int[]{});
    nodeFunctionDeclarationWithBody.setProperty("id", new Node(Token.IDENT, new int[]{}, new int[]{}));
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclarationWithBody));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{});
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeNull = null;
    try {
      NodeUtil.isFunctionDeclaration(nodeNull);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new Node(Token.IDENT, new int[]{}, new int[]{}));
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{});
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));

    Node nodeFunctionDeclarationInScript = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new Node(Token.IDENT, new int[]{}, new int[]{}));
    Node scriptNode = new Node(Token.SCRIPT, new int[]{}, new int[]{}, nodeFunctionDeclarationInScript);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInScript));

    Node nodeFunctionDeclarationInFunction = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new Node(Token.IDENT, new int[]{}, new int[]{}));
    Node functionNode = new Node(Token.FUNCTION, new int[]{}, new int[]{}, new Node(Token.BLOCK, new int[]{}, new int[]{}, nodeFunctionDeclarationInFunction));
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInFunction));

    Node nodeNull = null;
    try {
      NodeUtil.isHoistedFunctionDeclaration(nodeNull);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }
}