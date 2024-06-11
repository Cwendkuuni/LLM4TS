import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class NodeUtilTest {

  @Test
  public void testIsForIn() {
    Node forNode = new Node(Token.FOR);
    forNode.setChildCount(3);
    assertTrue(NodeUtil.isForIn(forNode));
  }

  @Test
  public void testIsForInNull() {
    assertFalse(NodeUtil.isForIn(null));
  }

  @Test
  public void testIsForInInvalidNode() {
    Node ifNode = new Node(Token.IF);
    assertFalse(NodeUtil.isForIn(ifNode));
  }

  @Test
  public void testIsStatement() {
    // Add test cases for isStatement method here
  }

  @Test
  public void testIsFunctionDeclaration() {
    // Add test cases for isFunctionDeclaration method here
  }

  @Test
  public void testIsHoistedFunctionDeclaration() {
    // Add test cases for isHoistedFunctionDeclaration method here
  }
}