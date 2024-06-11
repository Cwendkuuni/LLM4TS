import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.CheckGlobalThis.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler() {});
    Node node = NodeOperations.createNode("function testFunction() {}");
    assertTrue(checkGlobalThis.shouldTraverse(NodeTraversal.EMPTY, node, null));
  }
}