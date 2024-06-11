import static org.junit.Assert.assertEquals;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forInNode = Node.newNode(Token.FOR, new long[]{0, 0, 1});
    assertEquals(true, NodeUtil.isForIn(forInNode));

    Node notForInNode = Node.newNode(Token.IF, new long[]{0, 0, 1});
    assertEquals(false, NodeUtil.isForIn(notForInNode));
  }

  @Test
  public void testIsStatement() {
    Node statementNode = Node.newNode(Token.IF, new long[]{0, 0, 1});
    assertEquals(true, NodeUtil.isStatement(statementNode));

    Node expressionNode = Node.newNode(Token.ADD, new long[]{0, 1, 1});
    assertEquals(false, NodeUtil.isStatement(expressionNode));
  }

  // Add test cases for isFunctionDeclaration and isHoistedFunctionDeclaration
}