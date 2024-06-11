import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node forNode = Node.newNode(Token.FOR, new int[] {0, 0});
    Node notForNode = Node.newNode(Token.IF, new int[] {0, 0});
    Node invalidForNode = Node.newNode(Token.VAR, new int[] {0, 0});

    assertTrue(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(notForNode));
    assertFalse(NodeUtil.isForIn(invalidForNode));

    Node invalidForNodeWithLessChildren = Node.newNode(Token.FOR, new int[] {0, 0});
    Node invalidForNodeWithMoreChildren = Node.newNode(
        Token.FOR, new int[] {0, 0, 0, 1});

    assertFalse(NodeUtil.isForIn(invalidForNodeWithLessChildren));
    assertFalse(NodeUtil.isForIn(invalidForNodeWithMoreChildren));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.IF, new int[] {0, 0});
    Node expressionNode = Node.newNode(Token.MUL, new int[] {0, 0, 0, 0});
    Node parentOfStatementNode = Node.newNode(Token.SCRIPT, new int[] {0, 1});
    Node parentOfExpressionNode = Node.newNode(Token.IF, new int[] {0, 0});
    Node parentOfNonStatementNode = Node.newNode(Token.RETURN, new int[] {0, 0});

    assertTrue(NodeUtil.isStatement(statementNode));
    assertFalse(NodeUtil.isStatement(expressionNode));

    Node invalidParentOfStatementNode = Node.newNode(Token.FOR, new int[] {0, 0});
    Node invalidParentOfExpressionNode = Node.newNode(Token.IF, new int[] {0, 0});
    Node invalidParentOfNonStatementNode = Node.newNode(Token.LABEL, new int[] {0, 0});

    assertFalse(NodeUtil.isStatement(expressionNode));
    assertFalse(NodeUtil.isStatement(invalidParentOfStatementNode));
    assertFalse(NodeUtil.isStatement(invalidParentOfExpressionNode));
    assertFalse(NodeUtil.isStatement(invalidParentOfNonStatementNode));
  }
}