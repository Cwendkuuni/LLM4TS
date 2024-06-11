package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checkGlobalThis;
  private NodeFactory nodeFactory;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(Compiler.class);
    nodeFactory = mock(NodeFactory.class);
    checkGlobalThis = new CheckGlobalThis(compiler);
    traversal = new NodeTraversal(nodeFactory, checkGlobalThis);
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    Node root = Node.newBlock("function foo() { this.bar = 42; }");
    when(nodeFactory.createFunctionNoArg(null, null)).thenReturn(Node.newName("foo"));
    when(nodeFactory.createBlock(ImmutableList.of(Node.newAssign(Node.newThis(), Node.newName("bar"))), null))
        .thenReturn(root);

    traversal.traverse(Node.newBlock(ImmutableList.of(root)), null);
    ArgumentCaptor<Node> errorNodeCaptor = ArgumentCaptor.forClass(Node.class);
    assertThat(compiler.reportedDiagnostics).hasSize(1);
    assertThat(compiler.reportedDiagnostics.get(0)).isInstanceOf(CheckGlobalThis.DiagnosticType.class);
    assertThat(compiler.reportedDiagnostics.get(0).getSourceNode()).isEqualTo(root);
  }

  @Test
  public void testShouldNotTraverseGlobalConstructorContext() {
    Node root = Node.newBlock("function Foo() {}");

    traversal.traverse(Node.newBlock(ImmutableList.of(root)), null);
    assertNull(compiler.reportedDiagnostics);
  }

  @Test
  public void testShouldNotTraverseFunctionWithAnnotation() {
    JSDocInfo jsDoc = Node.newJSDocInfo("@constructor");
    Node root = Node.newFunctionNoArg("function Foo() {}", jsDoc);

    traversal.traverse(Node.newBlock(ImmutableList.of(root)), null);
    assertNull(compiler.reportedDiagnostics);
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    JSDocInfo jsDoc = Node.newJSDocInfo("@this", ImmutableList.of("MyType"));
    Node root = Node.newFunctionNoArg("function Foo() {}", jsDoc);

    traversal.traverse(Node.newBlock(ImmutableList.of(root)), null);
    assertNull(compiler.reportedDiagnostics);
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutParent() {
    Node root = Node.newFunctionNoArg("function foo() {}", null);

    traversal.traverse(Node.newBlock(ImmutableList.of(root)), null);
    assertNull(compiler.reportedDiagnostics);
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    Node parent = Node.newAssign(Node.newName("Object.prototype.foo"), Node.newName("foo"));
    Node root = Node.newAssign(Node.newName("foo"), Node.newThis());

    traversal.traverse(Node.newBlock(ImmutableList.of(parent, root)), null);
    assertNull(compiler.reportedDiagnostics);
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeSubproperty() {
    Node parent = Node.newAssign(Node.newGetProp(Node.newName("Object.prototype"), "foo"), Node.newName("bar"));
    Node root = Node.newAssign(Node.newGetProp(Node.newName("foo"), "bar"), Node.newThis());

    traversal.traverse(Node.newBlock(ImmutableList.of(parent, root)), null);
    assertNull(compiler.reportedDiagnostics);
  }

  @Test
  public void testShouldNotTraverseFunctionLentToPrototype() {
    Node parent = Node.newAssign(Node.newName("Object.prototype.foo"), Node.newFunctionNoArg("function() {}", null));
    Node root = Node.newAssign(Node.newName("foo"), Node.newThis());

    traversal.traverse(Node.newBlock(ImmutableList.of(parent, root)), null);
    assertNull(compiler.reportedDiagnostics);
  }

  @Test
  public void testShouldReportFunctionAssignmentToGlobal() {
    Node root = Node.newAssign(Node.newName("foo"), Node.newThis());

    traversal.traverse(Node.newBlock(ImmutableList.of(root)), null);
    ArgumentCaptor<Node> errorNodeCaptor = ArgumentCaptor.forClass(Node.class);
    assertThat(compiler.reportedDiagnostics).hasSize(1);
    assertThat(compiler.reportedDiagnostics.get(0)).isInstanceOf(CheckGlobalThis.DiagnosticType.class);
    assertThat(compiler.reportedDiagnostics.get(0).getSourceNode()).isEqualTo(root);
  }

  @Test
  public void testShouldReportFunctionPropertyAccess() {
    Node parent = Node.newName("parent");
    Node root = Node.newGetProp(Node.newName("this"), "foo");

    traversal.traverse(Node.newBlock(ImmutableList.of(parent, Node.newAssign(parent, Node.newFunctionNoArg("function() {}", null)), root)), null);
    ArgumentCaptor<Node> errorNodeCaptor = ArgumentCaptor.forClass(Node.class);
    assertThat(compiler.reportedDiagnostics).hasSize(1);
    assertThat(compiler.reportedDiagnostics.get(0)).isInstanceOf(CheckGlobalThis.DiagnosticType.class);
    assertThat(compiler.reportedDiagnostics.get(0).getSourceNode()).isEqualTo(root);
  }
}