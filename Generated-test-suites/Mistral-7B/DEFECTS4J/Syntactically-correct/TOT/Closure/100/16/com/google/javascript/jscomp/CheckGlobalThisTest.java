package com.google.javascript.jscomp.CheckGlobalThis;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CheckGlobalThis;
import org.junit.Test;

/**
 * Test cases for CheckGlobalThis class.
 */
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(traversal);

    Node node = createNode("this.x = 1;");
    Node parent = createNode("var f = function() { this.x = 1; }");

    checker.shouldTraverse(traversal, node, parent);
    assertEquals(false, checker.shouldReportThis(node, parent));

    node = createNode("this.x = 1;");
    parent = createNode("function f() { this.x = 1; }");
    checker.shouldTraverse(traversal, node, parent);
    assertEquals(true, checker.shouldReportThis(node, parent));

    node = createNode("function f() { this.x = 1; }");
    parent = createNode("function g() { f.x = 1; }");
    checker.shouldTraverse(traversal, node, parent);
    assertEquals(false, checker.shouldReportThis(node, parent));

    node = createNode("function f() { this.x = 1; }");
    parent = createNode("function g() { this.f = function() { this.x = 1; } }");
    checker.shouldTraverse(traversal, node, parent);
    assertEquals(false, checker.shouldReportThis(node, parent));

    node = createNode("this.x = 1;");
    parent = createNode("function f() { var a = this; }");
    checker.shouldTraverse(traversal, node, parent);
    assertEquals(false, checker.shouldReportThis(node, parent));
  }

  @Test
  public void testCheckGlobalThis_Constructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(traversal);

    Node node = createNode("function Foo() { this.x = 1; }");
    Node parent = createNode("function Bar() { Foo.call(this); }");

    checker.shouldTraverse(traversal, node, parent);
    assertEquals(false, checker.shouldReportThis(node, parent));
  }

  @Test
  public void testCheckGlobalThis_Prototype() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(traversal);

    Node node = createNode("function Foo() { }");
    Node parent = createNode("Foo.prototype.x = 1;");

    checker.shouldTraverse(traversal, node, parent);
    assertEquals(false, checker.shouldReportThis(node, parent));
  }

  @Test
  public void testCheckGlobalThis_Error() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.ERROR);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.getTraverser()).thenReturn(traversal);

    Node node = createNode("this.x = 1;");
    Node parent = createNode("function f() { this.x = 1; }");

    checker.shouldTraverse(traversal, node, parent);
    assertEquals(true, checker.shouldReportThis(node, parent));

    when(compiler.report(traversal.makeError(node, CheckLevel.ERROR, DiagnosticType.warning(""))))
        .thenReturn(null);

    checker.visit(traversal, node, parent);
  }

  private Node createNode(String source) {
    return new Node(Token.NULL, Token.EOF, source);
  }
}