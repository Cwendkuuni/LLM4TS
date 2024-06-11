import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractNode;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.compiler.CheckGlobalThis;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeTraversal.Callback;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
  }

  @Test
  public void testShouldTraverseFunctionWithoutAnnotation() {
    Node node = new Node(Token.FUNCTION, null, new Node[0]);
    Node parent = new Node(Token.SCRIPT, null, new Node[0]);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(NodeTraversal.Kind.ALL)).thenReturn(traversal);

    given(traversal.getCurrentNode()).willReturn(parent);
    given(parent.getParent()).willReturn(null);

    assertFalse(checker.shouldTraverse(checker, node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithConstructorAnnotation() {
    Node node = new Node(Token.FUNCTION, null, new Node[0]);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    node.setJSDocInfo(jsDoc);

    Node parent = new Node(Token.SCRIPT, null, new Node[0]);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(NodeTraversal.Kind.ALL)).thenReturn(traversal);
    when(traversal.getCurrentNode()).willReturn(parent);
    given(parent.getParent()).willReturn(null);

    assertEquals(false, checker.shouldTraverse(checker, node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    Node node = new Node(Token.FUNCTION, null, new Node[0]);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setThisType(new JSDocInfo.ThisType(JSDocInfo.ThisType.VALUE_TYPE, null));
    node.setJSDocInfo(jsDoc);

    Node parent = new Node(Token.SCRIPT, null, new Node[0]);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(NodeTraversal.Kind.ALL)).thenReturn(traversal);
    when(traversal.getCurrentNode()).willReturn(parent);
    given(parent.getParent()).willReturn(null);

    assertEquals(true, checker.shouldTraverse(checker, node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotation() {
    Node node = new Node(Token.FUNCTION, null, new Node[0]);
    Node parent = new Node(Token.SCRIPT, null, new Node[0]);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(NodeTraversal.Kind.ALL)).thenReturn(traversal);
    when(traversal.getCurrentNode()).willReturn(parent);
    given(parent.getParent()).willReturn(null);

    assertEquals(true, checker.shouldTraverse(checker, node, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeProperty() {
    Node node = new Node(Token.FUNCTION, null, new Node[0]);
    Node parent = new Node(Token.NAME, "Object", new Node[0]);
    Node prototype = new Node(Token.GETPROP, null, new Node[] { new Node(Token.STRING, "prototype"), node });
    parent.setParent(new Node(Token.SCRIPT, null, new Node[0]));

    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(NodeTraversal.Kind.ALL)).thenReturn(traversal);
    when(traversal.getCurrentNode()).willReturn(prototype);
    given(prototype.getParent()).willReturn(parent);

    assertEquals(false, checker.shouldTraverse(checker, node, prototype));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeSubProperty() {
    Node node = new Node(Token.FUNCTION, null, new Node[0]);
    Node parent = new Node(Token.NAME, "Object", new Node[0]);
    Node prototype = new Node(Token.GETPROP, null, new Node[] { new Node(Token.STRING, "prototype"), new Node(Token.STRING, "sub") });
    parent.setParent(new Node(Token.SCRIPT, null, new Node[0]));

    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(NodeTraversal.Kind.ALL)).thenReturn(traversal);
    when(traversal.getCurrentNode()).willReturn(prototype);
    given(prototype.getParent()).willReturn(parent);

    assertEquals(false, checker.shouldTraverse(checker, node, prototype));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node assign = new Node(Token.ASSIGN, null, new Node[0]);
    Node left = new Node(Token.THIS, null, new Node[0]);
    Node right = new Node(Token.NAME, "global", new Node[0]);
    assign.setFirstChild(left);
    assign.setLastChild(right);

    Node parent = new Node(Token.SCRIPT, null, new Node[0]);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(NodeTraversal.Kind.ALL)).thenReturn(traversal);
    when(traversal.getCurrentNode()).willReturn(parent);
    given(parent.getParent()).willReturn(null);

    checker.visit(checker, assign, parent);

    verify(compiler).report(argThat((InvocationOnMock invocation) -> {
      AbstractNode errorNode = invocation.getArgument(0);
      DiagnosticType diagnosticType = invocation.getArgument(1);

      assertEquals(assign, errorNode);
      assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnosticType);
    }));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node assign = new Node(Token.ASSIGN, null, new Node[0]);
    Node left = new Node(Token.NAME, "global", new Node[0]);
    Node right = new Node(Token.THIS, null, new Node[0]);
    assign.setFirstChild(left);
    assign.setLastChild(right);

    Node parent = new Node(Token.SCRIPT, null, new Node[0]);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(NodeTraversal.Kind.ALL)).thenReturn(traversal);
    when(traversal.getCurrentNode()).willReturn(parent);
    given(parent.getParent()).willReturn(null);

    checker.visit(checker, assign, parent);

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testShouldReportThisInPropertyAccess() {
    Node getProp = new Node(Token.GETPROP, null, new Node[] { new Node(Token.THIS, null, new Node[0]) });

    Node parent = new Node(Token.SCRIPT, null, new Node[0]);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(NodeTraversal.Kind.ALL)).thenReturn(traversal);
    when(traversal.getCurrentNode()).willReturn(parent);
    given(parent.getParent()).willReturn(null);

    checker.visit(checker, getProp, parent);

    verify(compiler).report(argThat((InvocationOnMock invocation) -> {
      AbstractNode errorNode = invocation.getArgument(0);
      DiagnosticType diagnosticType = invocation.getArgument(1);

      assertEquals(getProp, errorNode);
      assertEquals(CheckGlobalThis.GLOBAL_THIS, diagnosticType);
    }));
  }

  @Test
  public void testShouldNotReportThisInPropertyAccessWithPrototype() {
    Node getProp = new Node(Token.GETPROP, null, new Node[] { new Node(Token.THIS, null, new Node[0]), new Node(Token.STRING, "prototype") });

    Node parent = new Node(Token.SCRIPT, null, new Node[0]);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(NodeTraversal.Kind.ALL)).thenReturn(traversal);
    when(traversal.getCurrentNode()).willReturn(parent);
    given(parent.getParent()).willReturn(null);

    checker.visit(checker, getProp, parent);

    verifyNoMoreInteractions(compiler);
  }

  @Test
  public void testShouldNotReportThisInPropertyAccessWithPrototypeKey() {
    Node getProp = new Node(Token.GETPROP, null, new Node[] { new Node(Token.THIS, null, new Node[0]), new Node(Token.STRING_KEY, "prototype") });

    Node parent = new Node(Token.SCRIPT, null, new Node[0]);
    NodeTraversal traversal = mock(NodeTraversal.class);

    when(compiler.getTraversal(NodeTraversal.Kind.ALL)).thenReturn(traversal);
    when(traversal.getCurrentNode()).willReturn(parent);
    given(parent.getParent()).willReturn(null);

    checker.visit(checker, getProp, parent);

    verifyNoMoreInteractions(compiler);
  }

  private void given(Node node) {
    Node parent = new Node(Token.SCRIPT, null, new Node[0]);
    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.getTraversal(NodeTraversal.Kind.ALL)).thenReturn(traversal);
    when(traversal.getCurrentNode()).willReturn(parent);
    given(parent.getParent()).willReturn(null);
    node.setParent(parent);
  }

  private void verifyNoMoreInteractions(NodeTraversal traversal) {
    verifyNoMoreInteractions(compiler);
    verifyNoMoreInteractions(traversal);
  }

  private void verify(NodeTraversal traversal) {
    verify(traversal).getCurrentNode();
  }

  private void verify(AbstractCompiler compiler, int times) {
    verify(compiler, times(times)).report(anyObject());
  }

  private void verify(AbstractCompiler compiler) {
    verify(compiler).report(anyObject());
  }

  private Answer<JSDocInfo> getFunctionJsDocInfoAnswer() {
    return new Answer<JSDocInfo>() {
      @Override
      public JSDocInfo answer(InvocationOnMock invocation) throws Throwable {
        return new JSDocInfo();
      }
    };
  }

  private void when(NodeTraversal traversal, Node node, Node parent) {
    when(traversal.getCurrentNode()).thenReturn(node);
    given(node.getParent()).willReturn(parent);
  }

  private void when(Node node, Node parent) {
    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(parent);
    given(node.getParent()).willReturn(parent);
    given(compiler.getTraversal(NodeTraversal.Kind.ALL)).willReturn(traversal);
  }

  private void when(Node node) {
    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.getTraversal(NodeTraversal.Kind.ALL)).willReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(node);
    given(node.getParent()).willReturn(null);
  }
}