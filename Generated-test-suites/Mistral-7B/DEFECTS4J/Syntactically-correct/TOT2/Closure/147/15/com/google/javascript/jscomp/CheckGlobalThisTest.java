import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class CheckGlobalThisTest {

  @Mock
  private AbstractCompiler compiler;
  @Mock
  private NodeTraversal nodeTraversal;
  @Mock
  private Node parentNode;
  @Mock
  private Node node;

  private CheckGlobalThis checkGlobalThis;

  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverse_FunctionWithNoAnnotationOrPrototype_ShouldBeTraversed() {
    when(parentNode.getType()).thenReturn(Token.FUNCTION);
    when(node.getType()).thenReturn(Token.THIS);

    assertTrue(checkGlobalThis.shouldTraverse(nodeTraversal, node, parentNode));
  }
}