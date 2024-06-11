package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node n = Node.newNode(Token.FUNCTION, Token.ANY, null);
    Node parent = Node.newNode(Token.SCRIPT, Token.ANY, null);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    assertTrue(checker.shouldTraverse(checker, n, parent));
  }

  @Test
  public void testShouldNotTraverseNonGlobalContext() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node n = Node.newNode(Token.FUNCTION, Token.ANY, null);
    Node parent = Node.newNode(Token.BLOCK, Token.ANY, null);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    assertFalse(checker.shouldTraverse(checker, n, parent));
  }

  @Test
  public void testShouldTraversePrototypeMethod() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node n = Node.newNode(Token.FUNCTION, Token.ANY, null);
    Node parent = Node.newNode(Token.NAME, Token.ANY, null);
    parent.setProperty("prototype", Node.newNode(Token.OBJECT_LITERAL, Token.ANY, null));
    parent.getLastChild().setProperty("foo", n);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    assertTrue(checker.shouldTraverse(checker, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithNoAnnotation() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node n = Node.newNode(Token.FUNCTION, Token.ANY, null);
    Node parent = Node.newNode(Token.NAME, Token.ANY, null);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    assertTrue(checker.shouldTraverse(checker, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node n = Node.newNode(Token.FUNCTION, Token.ANY, null);
    Node jsDoc = Node.newNode(Token.COMMENT, Token.ANY, "/** @this {Object} */");
    n.setJSDocInfo(jsDoc);
    Node parent = Node.newNode(Token.NAME, Token.ANY, null);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    assertTrue(checker.shouldTraverse(checker, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithInterfaceAnnotation() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node n = Node.newNode(Token.FUNCTION, Token.ANY, null);
    Node jsDoc = Node.newNode(Token.COMMENT, Token.ANY, "/** @interface */");
    n.setJSDocInfo(jsDoc);
    Node parent = Node.newNode(Token.NAME, Token.ANY, null);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    assertTrue(checker.shouldTraverse(checker, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithConstructorAnnotation() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node n = Node.newNode(Token.FUNCTION, Token.ANY, null);
    Node jsDoc = Node.newNode(Token.COMMENT, Token.ANY, "/** @constructor */");
    n.setJSDocInfo(jsDoc);
    Node parent = Node.newNode(Token.NAME, Token.ANY, null);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    assertTrue(checker.shouldTraverse(checker, n, parent));
  }

  @Test
  public void testShouldTraverseFunctionWithNoLogicalPlaceToPutThisAnnotation() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node n = Node.newNode(Token.FUNCTION, Token.ANY, null);
    Node parent = Node.newNode(Token.NAME, Token.ANY, null);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    assertTrue(checker.shouldTraverse(checker, n, parent));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node lhs = Node.newNode(Token.THIS, Token.ANY, null);
    Node rhs = Node.newNode(Token.NAME, Token.ANY, "x");
    Node assign = Node.newNode(Token.ASSIGN, Token.ANY, null);
    assign.setFirstChild(lhs);
    assign.setLastChild(rhs);
    Node parent = Node.newNode(Token.SCRIPT, Token.ANY, null);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    assertTrue(checker.shouldReportThis(lhs));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node lhs = Node.newNode(Token.NAME, Token.ANY, "x");
    Node rhs = Node.newNode(Token.THIS, Token.ANY, null);
    Node assign = Node.newNode(Token.ASSIGN, Token.ANY, null);
    assign.setFirstChild(lhs);
    assign.setLastChild(rhs);
    Node parent = Node.newNode(Token.SCRIPT, Token.ANY, null);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    assertFalse(checker.shouldReportThis(rhs));
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfGetProperty() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node lhs = Node.newNode(Token.THIS, Token.ANY, null);
    Node get = Node.newNode(Token.GETPROP, Token.ANY, null);
    get.setLastChild(Node.newNode(Token.NAME, Token.ANY, "prototype"));
    lhs.setFirstChild(get);
    Node parent = Node.newNode(Token.SCRIPT, Token.ANY, null);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    assertFalse(checker.shouldReportThis(lhs));
  }

  @Test
  public void testShouldNotReportThisOnGetPropertyChain() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node lhs = Node.newNode(Token.THIS, Token.ANY, null);
    Node get = Node.newNode(Token.GETPROP, Token.ANY, null);
    get.setLastChild(Node.newNode(Token.NAME, Token.ANY, "prototype"));
    Node llhs = Node.newNode(Token.GETPROP, Token.ANY, null);
    llhs.setLastChild(Node.newNode(Token.NAME, Token.ANY, "prototype"));
    get.setFirstChild(llhs);
    lhs.setFirstChild(get);
    Node parent = Node.newNode(Token.SCRIPT, Token.ANY, null);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(parent);

    assertFalse(checker.shouldReportThis(lhs));
  }

  @Test
  public void testShouldReportThisWithNoParent() {
    NodeTraversal traversal = mock(NodeTraversal.class);
    Node n = Node.newNode(Token.THIS, Token.ANY, null);

    when(compiler.getTraversal()).thenReturn(traversal);
    when(traversal.getCurrentNode()).thenReturn(null);

    assertTrue(checker.shouldReportThis(n));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    Node n = Node.newNode(Token.FUNCTION, Token.ANY, null);
    JSDocInfo jsDoc = Node.newNode(Token.COMMENT, Token.ANY, "/** */");
    n.setJSDocInfo(jsDoc);

    assertSame(jsDoc, checker.getFunctionJsDocInfo(n));
  }

  @Test
  public void testGetFunctionJsDocInfoWithNoJsDoc() {
    Node n = Node.newNode(Token.FUNCTION, Token.ANY, null);

    assertNull(checker.getFunctionJsDocInfo(n));
  }

  @Test
  public void testGetFunctionJsDocInfoWithName() {
    Node parent = Node.newNode(Token.NAME, Token.ANY, "MyFunction");
    Node n = Node.newNode(Token.FUNCTION, Token.ANY, null);
    parent.setLastChild(n);

    assertSame(parent.getJSDocInfo(), checker.getFunctionJsDocInfo(n));
  }

  @Test
  public void testGetFunctionJsDocInfoWithVar() {
    Node parent = Node.newNode(Token.VAR, Token.ANY, "MyFunction");
    Node n = Node.newNode(Token.FUNCTION, Token.ANY, null);
    parent.setLastChild(n);

    assertSame(parent.getJSDocInfo(), checker.getFunctionJsDocInfo(n));
  }

  @Test
  public void testGetFunctionJsDocInfoWithEmptyParent() {
    Node n = Node.newNode(Token.FUNCTION, Token.ANY, null);

    assertNull(checker.getFunctionJsDocInfo(n));
  }

  private void assertTrue(boolean condition) {
    assertThat(condition).isTrue();
  }

  private void assertFalse(boolean condition) {
    assertThat(condition).isFalse();
  }

  private Node newNode(int type, int line, Node... children) {
    return new Node(type, line, children);
  }
}