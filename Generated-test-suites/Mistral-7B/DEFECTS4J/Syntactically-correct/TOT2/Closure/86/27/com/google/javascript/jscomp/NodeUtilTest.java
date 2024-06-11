import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = new Node("for (var i in array) { }");
    assertTrue(NodeUtil.isForIn(node));

    node = new Node("for (var i = 0; i < 10; i++) { }");
    assertFalse(NodeUtil.isForIn(node));

    node = new Node("if (true) { for (var i in array) { } }");
    assertFalse(NodeUtil.isForIn(node));

    node = new Node("while (true) { for (var i in array) { } }");
    assertFalse(NodeUtil.isForIn(node));
  }
}