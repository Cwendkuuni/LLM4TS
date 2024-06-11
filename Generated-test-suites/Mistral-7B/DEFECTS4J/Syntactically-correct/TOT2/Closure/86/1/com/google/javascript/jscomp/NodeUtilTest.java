import static org.junit.Assert.assertEquals;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    Node forNodeWithThreeChildren = new Node(Token.FOR, new Node[]{new Node(Token.IDENTIFIER), new Node(Token.ASSIGN), new Node(Token.IDENTIFIER)});
    assertEquals(true, NodeUtil.isForIn(forNodeWithThreeChildren));
    assertEquals(false, NodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = new Node(Token.PRINT, new Node[]{new Node(Token.STRING_LITERAL, "Hello World")});
    Node expressionNode = new Node(Token.ASSIGN, new Node[]{new Node(Token.IDENTIFIER), new Node(Token.NUMBER, 5)});

    assertEquals(true, NodeUtil.isStatement(statementNode));
    assertEquals(false, NodeUtil.isStatement(expressionNode));
  }

  // Add test cases for isFunctionDeclaration and isHoistedFunctionDeclaration here
}