import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {
  @org.junit.Test
  public void testIsForIn() {
    Node forInNode = createNodeForTest("for (var i in array) { ... }"); // FOR-IN loop
    assertTrue(NodeUtil.isForIn(forInNode));

    Node notForInNode = createNodeForTest("for (var i = 0; i < 5; i++) { ... }"); // FOR loop
    assertFalse(NodeUtil.isForIn(notForInNode));
  }

  private Node createNodeForTest(String nodeCode) {
    // Implementation of creating a Node object from the given node code
  }
}