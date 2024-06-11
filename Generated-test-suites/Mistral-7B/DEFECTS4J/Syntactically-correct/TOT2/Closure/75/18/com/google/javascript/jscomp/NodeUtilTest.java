import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[3]);
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[0], new int[0]);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));

    Node nodeWithInvalidType = new Node(Token.INVALID, new int[0], new int[0]);
    assertFalse(NodeUtil.isForIn(nodeWithInvalidType));
  }

  @Test
  public void testIsStatement() {
    Node nodeStatement = new Node(Token.PRINT, new int[]{}, new int[0]);
    assertTrue(NodeUtil.isStatement(nodeStatement));

    Node nodeExpression = new Node(Token.MUL, new int[]{Token.NUMBER, Token.NUMBER}, new int[0]);
    assertFalse(NodeUtil.isStatement(nodeExpression));

    Node nodeWithNoChildren = new Node(Token.IDENT, new int[0], new int[0]);
    assertFalse(NodeUtil.isStatement(nodeWithNoChildren));

    Node nodeTryStatement = new Node(Token.TRY, new int[0], new int[0]);
    assertTrue(NodeUtil.isStatement(nodeTryStatement));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node nodeFunctionDeclaration = new Node(Token.FUNCTION, new int[]{Token.IDENT, Token.LPAREN, Token.RPAREN}, new int[0]);
    assertTrue(NodeUtil.isFunctionDeclaration(nodeFunctionDeclaration));

    Node nodeFunctionExpression = new Node(Token.FUNCTION, new int[]{Token.LPAREN}, new int[0]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeFunctionExpression));

    Node nodeWithInvalidType = new Node(Token.INVALID, new int[0], new int[0]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeWithInvalidType));

    Node nodeWithMoreThanThreeChildren = new Node(Token.FUNCTION, new int[]{Token.IDENT, Token.LPAREN, Token.RPAREN, Token.LPAREN}, new int[0]);
    assertFalse(NodeUtil.isFunctionDeclaration(nodeWithMoreThanThreeChildren));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node nodeHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{Token.IDENT, Token.LPAREN, Token.RPAREN}, new int[0]);
    Node parentOfHoistedFunctionDeclaration = new Node(Token.SCRIPT, new int[0], new int[0]);
    parentOfHoistedFunctionDeclaration.addChild(nodeHoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(nodeHoistedFunctionDeclaration));

    Node nodeNotHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{Token.IDENT, Token.LPAREN, Token.RPAREN}, new int[0]);
    Node parentOfNotHoistedFunctionDeclaration = new Node(Token.BLOCK, new int[0], new int[0]);
    parentOfNotHoistedFunctionDeclaration.addChild(nodeNotHoistedFunctionDeclaration);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeNotHoistedFunctionDeclaration));

    Node nodeEmptyParent = new Node(Token.SCRIPT, new int[0], new int[0]);
    Node nodeFunctionDeclarationInEmptyParent = new Node(Token.FUNCTION, new int[]{Token.IDENT, Token.LPAREN, Token.RPAREN}, new int[0]);
    nodeFunctionDeclarationInEmptyParent.addChild(nodeEmptyParent);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nodeFunctionDeclarationInEmptyParent));
  }
}