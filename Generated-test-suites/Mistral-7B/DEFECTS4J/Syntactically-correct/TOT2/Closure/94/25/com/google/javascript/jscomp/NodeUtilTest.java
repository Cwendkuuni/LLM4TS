import static org.junit.Assert.*;
import com.google.javascript.rhino.Node;

public class NodeUtilTest {
  @Test
  public void testIsForIn() {
    Node forNodeWithThreeChildren = new Node(Token.FOR, new Node[]{new Node(Token.IDENTIFIER, "i"), new Node(Token.KEYWORD, "in"), new Node(Token.IDENTIFIER, "array")});

    assertTrue(NodeUtil.isForIn(forNodeWithThreeChildren));
    assertFalse(NodeUtil.isForIn(new Node(Token.FUNCTION)));
  }

  @Test
  public void testIsForIn_InvalidNode() {
    Node invalidNode = null;
    assertFalse(NodeUtil.isForIn(invalidNode));
  }

  @Test
  public void testIsForIn_InvalidNumberOfChildren() {
    Node forNodeWithTwoChildren = new Node(Token.FOR);
    assertFalse(NodeUtil.isForIn(forNodeWithTwoChildren));
  }

  @Test
  public void testIsForIn_InvalidNumberOfChildren_WithExtraChild() {
    Node forNodeWithFourChildren = new Node(Token.FOR, new Node[]{new Node(Token.IDENTIFIER, "i"), new Node(Token.KEYWORD, "in"), new Node(Token.IDENTIFIER, "array"), new Node(Token.IDENTIFIER, "x")});
    assertFalse(NodeUtil.isForIn(forNodeWithFourChildren));
  }

  // Test cases for other methods in NodeUtil class would go here.

  @After
  public void tearDown() {
    // You can add any clean-up code here.
  }
}