import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.rhino.TokenType.*;

import org.junit.Test;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckGlobalThis;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThisConstructor() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    assertNotNull(checkGlobalThis);
  }

  @Test
  public void testShouldTraverseFalse() {
    NodeTraversal nodeTraversal = new NodeTraversal(null, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node node = Node.newFunctionNode("function foo() {}");
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, node, null));
  }

  @Test
  public void testShouldTraverseTrue() {
    NodeTraversal nodeTraversal = new NodeTraversal(null, null);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node node = Node.newFunctionNode("function foo() { return this; }");
    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, null));
  }

  // ...
}