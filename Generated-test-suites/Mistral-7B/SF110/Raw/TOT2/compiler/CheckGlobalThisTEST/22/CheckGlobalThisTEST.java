import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node n = mock(Node.class);
    Node parent = mock(Node.class);

    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);

    when(nodeTraversal.makeError(n, eq(CheckGlobalThis.GLOBAL_THIS))).thenReturn(mock(Node.class));

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, n, parent));

    verify(compiler, times(1)).report(eq(nodeTraversal.makeError(n, eq(CheckGlobalThis.GLOBAL_THIS))));
  }
}