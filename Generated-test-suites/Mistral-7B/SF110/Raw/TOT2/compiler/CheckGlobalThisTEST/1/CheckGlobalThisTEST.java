import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckGlobalThis.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    NodeTraversal traversal = new NodeTraversal();
    Node node = new Node("function() {}"); // or any other valid Node
    assertTrue(checkGlobalThis.shouldTraverse(traversal, node, null));
  }
}