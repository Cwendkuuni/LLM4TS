import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChild(0, new Node(Token.IDENT, "var"));
    forNode.setChild(1, new Node(Token.IDENT, "i"));
    forNode.setChild(2, new Node(Token.ASSIGN, Node.OP_ASSIGN, new Node(Token.IDENT, "arr"), new Node(Token.ARRAY_LITERAL, "[]")));

    assertTrue(NodeUtil.isForIn(forNode));
  }
}