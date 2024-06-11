package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverseFunctionNotAnnotated() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("evil");
    Node functionParent = Node.newBlock();
    functionParent.addChildToEnd(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(functionNode);
    when(traversal.getParentNode()).thenReturn(functionParent);

    assertThat(checker.shouldTraverse(traversal, Node.newThis(), functionNode))
        .isTrue();
  }

  @Test
  public void testShouldTraverseFunctionPrototype() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("Foo");
    functionNode.addJSDocInfo(new JSDocInfo(null, null, null, true, ImmutableList.of("prototype")));
    Node functionParent = Node.newBlock();
    functionParent.addChildToEnd(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(Node.newGetProp(Node.newIdentifier("Foo"), "prototype"));
    when(traversal.getParentNode()).thenReturn(functionParent);

    assertThat(checker.shouldTraverse(traversal, Node.newThis(), functionParent)).isTrue();
  }

  @Test
  public void testShouldTraverseFunctionConstructor() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("Foo", Node.newList(Node.newIdentifier("arg")), null);
    functionNode.addJSDocInfo(new JSDocInfo(null, null, null, true, ImmutableList.of()));
    Node functionParent = Node.newBlock();
    functionParent.addChildToEnd(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(Node.newThis());
    when(traversal.getParentNode()).thenReturn(functionParent);

    assertThat(checker.shouldTraverse(traversal, Node.newThis(), functionParent)).isTrue();
  }

  @Test
  public void testShouldTraverseFunctionAnnotatedWithThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("Foo", Node.newList(Node.newIdentifier("arg")), null);
    functionNode.addJSDocInfo(new JSDocInfo(null, null, "@this", null, null));
    Node functionParent = Node.newBlock();
    functionParent.addChildToEnd(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(Node.newThis());
    when(traversal.getParentNode()).thenReturn(functionParent);

    assertThat(checker.shouldTraverse(traversal, Node.newThis(), functionParent)).isTrue();
  }

  @Test
  public void testShouldTraverseFunctionNoLogicalPlaceForThisAnnotation() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("Foo", Node.newList(Node.newIdentifier("arg")), null);
    Node functionParent = Node.newBlock();
    functionParent.addChildToEnd(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(Node.newThis());
    when(traversal.getParentNode()).thenReturn(functionParent);

    assertThat(checker.shouldTraverse(traversal, Node.newThis(), functionParent)).isTrue();
  }

  @Test
  public void testShouldTraverseFunctionGettingLentToPrototype() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("Foo");
    functionNode.addJSDocInfo(new JSDocInfo(null, null, null, true, ImmutableList.of()));
    Node functionParent = Node.newBlock();
    functionParent.addChildToEnd(functionNode);

    Node getPropNode = Node.newGetProp(Node.newIdentifier("Foo"), "prototype");
    Node getPropParent = Node.newAssign(Node.newIdentifier("a"), Node.newThis());
    getPropParent.addChildToEnd(getPropNode);
    getPropParent.addChildToEnd(Node.newIdentifier("prop"));

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(getPropNode);
    when(traversal.getParentNode()).thenReturn(getPropParent);

    assertThat(checker.shouldTraverse(traversal, Node.newThis(), functionParent)).isTrue();
  }

  @Test
  public void testShouldNotTraverseNonGlobalContext() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("Foo", Node.newList(Node.newIdentifier("arg")), null);
    Node functionParent = Node.newAssign(Node.newIdentifier("a"), Node.newNumber(1));
    functionParent.addChildToEnd(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(Node.newThis());
    when(traversal.getParentNode()).thenReturn(functionParent);

    assertThat(checker.shouldTraverse(traversal, Node.newThis(), functionParent)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithParentAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("Foo", Node.newList(Node.newIdentifier("arg")), null);
    Node functionParent = Node.newAssign(Node.newIdentifier("a"), Node.newThis());
    functionParent.addChildToEnd(functionNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(Node.newThis());
    when(traversal.getParentNode()).thenReturn(functionParent);

    assertThat(checker.shouldTraverse(traversal, Node.newThis(), functionParent)).isFalse();
  }

  @Test
  public void testShouldNotTraverseNonFunctionParent() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("Foo", Node.newList(Node.newIdentifier("arg")), null);
    Node functionParent = Node.newIdentifier("a");

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(Node.newThis());
    when(traversal.getParentNode()).thenReturn(functionParent);

    assertThat(checker.shouldTraverse(traversal, Node.newThis(), functionParent)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypePropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("Foo", Node.newList(Node.newIdentifier("arg")), null);
    Node functionParent = Node.newBlock();
    functionParent.addChildToEnd(functionNode);

    Node getPropNode = Node.newGetProp(functionNode, "prototype");
    Node getPropParent = Node.newAssign(Node.newIdentifier("a"), Node.newThis());
    getPropParent.addChildToEnd(getPropNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(getPropNode);
    when(traversal.getParentNode()).thenReturn(getPropParent);

    assertThat(checker.shouldTraverse(traversal, Node.newThis(), functionParent)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithPrototypeSubPropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node functionNode = Node.newFunction("Foo", Node.newList(Node.newIdentifier("arg")), null);
    Node functionParent = Node.newBlock();
    functionParent.addChildToEnd(functionNode);

    Node getPropNode1 = Node.newGetProp(functionNode, "prototype");
    Node getPropNode2 = Node.newGetProp(getPropNode1, "subProp");
    Node getPropParent = Node.newAssign(Node.newIdentifier("a"), Node.newThis());
    getPropParent.addChildToEnd(getPropNode1);
    getPropParent.addChildToEnd(getPropNode2);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(getPropNode2);
    when(traversal.getParentNode()).thenReturn(getPropParent);

    assertThat(checker.shouldTraverse(traversal, Node.newThis(), functionParent)).isFalse();
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node assignNode = Node.newAssign(Node.newIdentifier("a"), Node.newThis());

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(assignNode);
    when(traversal.getParentNode()).thenReturn(null);

    Node parent = Node.newBlock();
    checker.visit(traversal, null, parent);

    compiler.assertDiagnosticsPresent(ImmutableList.of(
        new DiagnosticInfo(GLOBAL_THIS, assignNode, "dangerous use of the global 'this' object")
    ));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignmentWithParent() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node assignParent = Node.newBlock();
    Node assignNode = Node.newAssign(Node.newIdentifier("a"), Node.newThis());
    assignParent.addChildToEnd(assignNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(assignNode);
    when(traversal.getParentNode()).thenReturn(assignParent);

    Node parent = Node.newBlock();
    checker.visit(traversal, null, parent);

    compiler.assertDiagnosticsPresent(ImmutableList.of(
        new DiagnosticInfo(GLOBAL_THIS, assignNode, "dangerous use of the global 'this' object")
    ));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccess() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node getPropNode = Node.newGetProp(Node.newIdentifier("a"), "prop");
    Node getPropParent = Node.newBlock();
    getPropParent.addChildToEnd(getPropNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(getPropNode);
    when(traversal.getParentNode()).thenReturn(getPropParent);

    Node parent = Node.newBlock();
    checker.visit(traversal, null, parent);

    compiler.assertDiagnosticsPresent(ImmutableList.of(
        new DiagnosticInfo(GLOBAL_THIS, getPropNode, "dangerous use of the global 'this' object")
    ));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccessWithParent() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler);

    Node getPropParent = Node.newBlock();
    Node getPropNode = Node.newGetProp(Node.newIdentifier("a"), "prop");
    getPropParent.addChildToEnd(getPropNode);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(traversal.getCurrentNode()).thenReturn(getPropNode);
    when(traversal.getParentNode()).thenReturn(getPropParent);

    Node parent = Node.newBlock();
    checker.visit(traversal, null, parent);

    compiler.assertDiagnosticsPresent(ImmutableList.of(
        new DiagnosticInfo(GLOBAL_THIS, getPropNode, "dangerous use of the global 'this' object")
    ));
  }

  private static final DiagnosticType GLOBAL_THIS = DiagnosticType.warning(
      "JSC_USED_GLOBAL_THIS",
      "dangerous use of the global 'this' object");
}