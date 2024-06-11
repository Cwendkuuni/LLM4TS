import static org.junit.Assert.assertEquals;
import static com.google.javascript.rhino.NodeFactory.createToken;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = NodeFactory.createNodeFromString("for (var i in array) { }");
    assertTrue(NodeUtil.isForIn(forNode));

    Node ifNode = NodeFactory.createNodeFromString("if (true) { }");
    assertFalse(NodeUtil.isForIn(ifNode));

    Node forNodeWithInvalidChildCount = NodeFactory.createNodeFromString("for (var i in array) { var j = 1; }");
    assertFalse(NodeUtil.isForIn(forNodeWithInvalidChildCount));
  }
}