import static org.junit.Assert.assertTrue;
import static com.google.javascript.rhino.NodeFactory.createAssign;
import static com.google.javascript.rhino.NodeFactory.createFunction;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createNodeTraversal;
import static com.google.javascript.rhino.NodeFactory.createThis;

import org.junit.Test;

public class CheckGlobalThisTest {
  @Test
  public void testShouldTraverseFunction() {
    NodeTraversal traversal = createNodeTraversal();
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    Node functionNode = createFunction(createIdentifier("testFunction"));
    Node parentNode = createAssign(createIdentifier("x"), createNodeTraversal());
    Node thisNode = createThis();

    traversal.setEnterCallback(checker);
    traversal.traverse(parentNode, null);

    assertTrue(checker.shouldTraverse(traversal, functionNode, parentNode));
  }
}