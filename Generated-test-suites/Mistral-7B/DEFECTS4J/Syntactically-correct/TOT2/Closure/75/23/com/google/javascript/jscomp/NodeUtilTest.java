import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    Node forNodeWithThreeChildren = new Node(Token.FOR).setChild(0, new Node(Token.IDENT, "for"))
        .setChild(1, new Node(Token.ASSIGN, null, new Node(Token.NAME, "i"), new Node(Token.NUM, 0)))
        .setChild(2, new Node(Token.SEMICOLON, null));
    Node notForNode = new Node(Token.IF, null);

    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(forNode));
    assertFalse(NodeUtil.isForIn(notForNode));
  }
}