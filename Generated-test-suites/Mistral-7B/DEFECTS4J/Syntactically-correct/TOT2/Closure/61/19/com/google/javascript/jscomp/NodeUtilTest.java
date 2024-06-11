import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new NodeToken(Token.FOR);
    Node nodeWithThreeChildren = new NodeListInternal(3);
    nodeForIn.setParent(nodeWithThreeChildren);
    for (int i = 0; i < 3; i++) {
      nodeWithThreeChildren.setChild(i, new NodeToken(Token.IDENTIFIER));
    }
    assertTrue(NodeUtil.isForIn(nodeForIn));

    Node nodeNotForIn = new NodeToken(Token.IF);
    assertFalse(NodeUtil.isForIn(nodeNotForIn));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new NodeToken(Token.PRINT);
    assertTrue(NodeUtil.isStatement(statementNode));

    Node expressionNode = new NodeToken(Token.PLUS);
    assertFalse(NodeUtil.isStatement(expressionNode));
  }

  // Add the tests for isFunctionDeclaration and isHoistedFunctionDeclaration here
}