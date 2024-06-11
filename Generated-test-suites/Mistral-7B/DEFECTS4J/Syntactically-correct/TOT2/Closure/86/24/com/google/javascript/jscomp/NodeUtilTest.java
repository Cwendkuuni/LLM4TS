
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static com.google.javascript.jscomp.jsonml.NodeUtil.*;

public class NodeUtilTest {

  @org.junit.Test
  public void testIsForIn() {
    Node node = mock(Node.class);
    node.getType = Token.FOR;
    node.getChildCount = () -> 3;
    assertEquals(true, isForIn(node));
  }

  @org.junit.Test
  public void testIsForInNull() {
    Node node = null;
    assertEquals(false, isForIn(node));
  }

  @org.junit.Test
  public void testIsForInInvalidType() {
    Node node = mock(Node.class);
    node.getType = Token.IF;
    assertEquals(false, isForIn(node));
  }

  @org.junit.Test
  public void testIsForInEmptyChildList() {
    Node node = mock(Node.class);
    node.getType = Token.FOR;
    node.getChildCount = () -> 0;
    assertEquals(false, isForIn(node));
  }

  // Test cases for isStatement, isFunctionDeclaration, and isHoistedFunctionDeclaration methods

}
