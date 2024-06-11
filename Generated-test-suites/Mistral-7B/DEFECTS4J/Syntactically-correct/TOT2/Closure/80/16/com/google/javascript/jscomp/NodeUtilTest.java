import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = Node.newForLoop("for (var i = 0; i < 5; i++) { }");
    assertTrue(NodeUtil.isForIn(forNode));

    Node notForNode = Node.newExpressionStatement("var i = 0;");
    assertFalse(NodeUtil.isForIn(notForNode));
  }
}