
import static org.junit.Assert.*;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{1, 2, 3});
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{1, 2});
    assertFalse(NodeUtil.isForIn(nodeNotForIn));

    Node invalidNode = new Node(-1, new int[]{}, new int[]{});
    assertFalse(NodeUtil.isForIn(invalidNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.IF, new int[]{}, new int[]{1, 2});
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = new Node(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3});
    assertFalse(NodeUtil.isStatement(expressionNode));

    Node invalidNode = new Node(-1, new int[]{}, new int[]{});
    assertFalse(NodeUtil.isStatement(invalidNode));
  }

  @Test
  public void testIsFunctionDeclaration() {
    Node functionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3, 4});
    assertTrue(NodeUtil.isFunctionDeclaration(functionDeclaration));

    Node functionExpression = new Node(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3});
    assertFalse(NodeUtil.isFunctionDeclaration(functionExpression));

    Node invalidNode = new Node(-1, new int[]{}, new int[]{});
    assertFalse(NodeUtil.isFunctionDeclaration(invalidNode));
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    Node hoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3, 4, 5});
    Node parentOfHoistedFunctionDeclaration = new Node(Token.SCRIPT, new int[]{}, new int[]{});
    parentOfHoistedFunctionDeclaration.addChildToChildren(hoistedFunctionDeclaration);
    assertTrue(NodeUtil.isHoistedFunctionDeclaration(hoistedFunctionDeclaration));

    Node nonHoistedFunctionDeclaration = new Node(Token.FUNCTION, new int[]{}, new int[]{1, 2, 3, 4, 5});
    Node parentOfNonHoistedFunctionDeclaration = new Node(Token.BLOCK, new int[]{}, new int[]{1, 2, 3});
    parentOfNonHoistedFunctionDeclaration.addChildToChildren(nonHoistedFunctionDeclaration);
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(nonHoistedFunctionDeclaration));

    Node malformedASTNode = new Node(-1, new int[]{}, new int[]{});
    assertFalse(NodeUtil.isHoistedFunctionDeclaration(malformedASTNode));
  }
}