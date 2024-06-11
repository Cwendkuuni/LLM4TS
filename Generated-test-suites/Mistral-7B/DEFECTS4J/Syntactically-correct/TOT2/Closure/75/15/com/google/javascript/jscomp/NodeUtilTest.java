import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node nodeForIn = new Node(Token.FOR, new int[]{}, new int[]{});
    Node nodeNotForIn = new Node(Token.IF, new int[]{}, new int[]{});
     assertEquals(true, NodeUtil.isForIn(nodeForIn));
     assertEquals(false, NodeUtil.isForIn(nodeNotForIn));
  }
}