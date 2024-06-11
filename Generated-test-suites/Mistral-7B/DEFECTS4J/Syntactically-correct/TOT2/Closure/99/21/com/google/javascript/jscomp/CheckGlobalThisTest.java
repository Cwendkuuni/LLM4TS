import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testConstructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    assertEquals(compiler, checkGlobalThis.compiler);
    assertEquals(level, checkGlobalThis.level);
  }

  @Test
  public void testShouldTraverse_Normal() {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    Callback callback = mock(Callback.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    when(node.getType()).thenReturn(Token.FUNCTION);
    when(getFunctionJsDocInfo(node)).thenReturn(null);
    when(parent.getType()).thenReturn(Token.SCRIPT);

    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
    assertEquals(true, result);
  }

  @Test
  public void testShouldTraverse_Constructor() {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    Callback callback = mock(Callback.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    when(node.getType()).thenReturn(Token.FUNCTION);
    when(getFunctionJsDocInfo(node)).thenReturn(new JSDocInfo(true, null, null, null, null, null, null, null));

    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
    assertEquals(false, result);
  }

  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation() {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    Callback callback = mock(Callback.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    when(node.getType()).thenReturn(Token.FUNCTION);
    when(getFunctionJsDocInfo(node)).thenReturn(new JSDocInfo(null, null, "@this", null, null, null, null, null));

    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
    assertEquals(false, result);
  }

  @Test
  public void testShouldTraverse_FunctionWithoutLogicalPlaceForThisAnnotation() {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    Callback callback = mock(Callback.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    when(node.getType()).thenReturn(Token.FUNCTION);
    when(getFunctionJsDocInfo(node)).thenReturn(null);
    when(parent.getType()).thenReturn(Token.SCRIPT);

    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
    assertEquals(true, result);
  }

  @Test(expected = AssertionError.class)
  public void testShouldReportThis_Normal() {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    when(node.getType()).thenReturn(Token.THIS);
    when(shouldReportThis(node, parent)).thenReturn(false);

    checkGlobalThis.visit(nodeTraversal, node, parent);
  }

  @Test
  public void testVisit_Assignment() {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node assignNode = mock(Node.class);
    Node lhs = mock(Node.class);
    Node rhs = mock(Node.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    when(assignNode.getType()).thenReturn(Token.ASSIGN);
    when(assignNode.getFirstChild()).thenReturn(lhs);
    when(lhs.getType()).thenReturn(Token.THIS);

    checkGlobalThis.visit(nodeTraversal, assignNode, null);
  }

  @Test
  public void testShouldReportThis_PropertyAccess() {
    NodeTraversal nodeTraversal = mock(NodeTraversal.class);
    Node node = mock(Node.class);
    Node parent = mock(Node.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    when(node.getType()).thenReturn(Token.THIS);
    when(parent.getType()).thenReturn(Token.GETPROP);
    when(shouldReportThis(node, parent)).thenReturn(true);

    checkGlobalThis.visit(nodeTraversal, node, parent);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithJsDoc() {
    Node node = mock(Node.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);
    assertEquals(new JSDocInfo(true, null, null, null, null, null, null, null), result);
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJsDoc() {
    Node node = mock(Node.class);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(node);
    assertEquals(null, result);
  }
}