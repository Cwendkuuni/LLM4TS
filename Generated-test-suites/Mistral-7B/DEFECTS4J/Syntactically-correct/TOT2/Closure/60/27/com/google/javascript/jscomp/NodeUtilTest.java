import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node node = new Node("for (var i in array) { }");
    assertTrue(NodeUtil.isForIn(node));
  }

  @Test
  public void testIsForInFalse() {
    Node node = new Node("if (true) { }");
    assertFalse(NodeUtil.isForIn(node));
  }

  // Add more test cases here
}