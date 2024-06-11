import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChild(0, new Node("init"));
    forNode.setChild(1, new Node("condition"));
    forNode.setChild(2, new Node("next"));
    Node notForNode = new Node(Token.IF);

    // Test a FOR-IN loop
    assertTrue(NodeUtil.isForIn(forNode));

    // Test a non-FOR-IN node
    assertFalse(NodeUtil.isForIn(notForNode));
  }
}