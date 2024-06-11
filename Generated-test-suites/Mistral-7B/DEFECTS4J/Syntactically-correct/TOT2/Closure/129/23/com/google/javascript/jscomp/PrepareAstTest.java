import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PrepareAst;

public class PrepareAstTest {

  @Test
  public void testProcess() {
    // Given a valid Node and CompilerPass
    PrepareAst prepareAst = new PrepareAst(null);
    Node root = Node.objectLiteral();

    // When processing the Node
    prepareAst.process(null, root);

    // Then the Node should be normalized
    assertTrue(root.isObjectLit());
    assertFalse(root.isBlock());

    // Test edge case: Node with blocks
    Node nodeWithBlocks = Node.block("{}");
    prepareAst.process(null, nodeWithBlocks);
    assertTrue(nodeWithBlocks.isBlock());
    assertFalse(nodeWithBlocks.isObjectLit());

    // Test exception scenario: Null node
    try {
      prepareAst.process(null, null);
      fail("Expected NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }
}