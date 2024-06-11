import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    Callback checker = new CheckGlobalThis(compiler);
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node node = mock(Node.class);
    Node parent = mock(Node.class);

    when(compiler.report(Mockito.any())).thenReturn(true);

    assertTrue(checker.shouldTraverse(traversal, node, parent));
  }
}