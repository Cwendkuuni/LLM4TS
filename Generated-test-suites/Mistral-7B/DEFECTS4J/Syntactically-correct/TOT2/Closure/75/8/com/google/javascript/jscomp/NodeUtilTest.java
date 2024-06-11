import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, null, new Node[]{}, null);
    assertTrue(NodeUtil.isForIn(nodeForIn));
  }
}