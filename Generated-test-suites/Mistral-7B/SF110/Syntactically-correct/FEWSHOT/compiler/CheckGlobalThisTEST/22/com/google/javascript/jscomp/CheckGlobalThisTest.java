package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckGlobalThis;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeFactory nodeFactory;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
    checker = new CheckGlobalThis(compiler);
    traversal = new NodeTraversal(nodeFactory, checker);
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    Node root = createNode("function foo() { }");
    Node functionNode = root.getFirstChild();

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(traversal, functionNode, root))).isTrue();
  }

  @Test
  public void testShouldNotTraverseLocalContext() {
    Node root = createNode("function foo() { function bar() { } }");
    Node functionNode = root.getFirstChild();
    Node localFunctionNode = functionNode.getFirstChild();

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(traversal, localFunctionNode, root))).isFalse();
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node root = createNode("function Foo() { }");

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(traversal, root.getFirstChild(), root))).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node root = createNode("function Foo@this() { }");

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(traversal, root.getFirstChild(), root))).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node root = createNode("function Foo@override() { }");

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(traversal, root.getFirstChild(), root))).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithInterfaceAnnotation() {
    Node root = createNode("function Foo@interface() { }");

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(traversal, root.getFirstChild(), root))).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionLentToPrototype() {
    Node root = createNode("var a = { get x() { function foo() { } } }");

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(traversal, root.getFirstChild().getFirstChild(), root))).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionGettingLentToPrototype() {
    Node root = createNode("var a = { get prop() { function foo() { } } }");

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(traversal, root.getFirstChild().getLastChild(), root))).isFalse();
  }

  @Test
  public void testShouldNotTraversePropertyAccessToPrototype() {
    Node root = createNode("var a = { get x() { return prototype.foo } }");

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(traversal, root.getFirstChild(), root))).isFalse();
  }

  @Test
  public void testShouldNotTraversePropertyAccessToPrototypeSubProperty() {
    Node root = createNode("var a = { get x() { return prototype.foo.bar } }");

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(traversal, root.getFirstChild().getLastChild(), root))).isFalse();
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    Node root = createNode("var a = { get prop() { this.foo = 1 } }");

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(traversal, root.getFirstChild().getLastChild(), root))).isFalse();
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeSubProperty() {
    Node root = createNode("var a = { get prop() { this.foo.bar = 1 } }");

    assertThat(checker.shouldTraverse(ArgumentMatchers.eq(traversal, root.getFirstChild().getLastChild().getLastChild(), root))).isFalse();
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node root = createNode("function foo() { this.x = 1; }");

    Node thisNode = root.getFirstChild();

    InOrder inOrder = inOrder(compiler);
    traversal.traverse(root);

    checker.visit(ArgumentMatchers.eq(traversal, thisNode, root));

    inOrder.verify(compiler).report(ArgumentMatchers.eq(traversal.makeError(thisNode, CheckGlobalThis.GLOBAL_THIS)));
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfAssignment() {
    Node root = createNode("function foo() { this = 1; }");

    Node thisNode = root.getFirstChild();

    traversal.traverse(root);

    checker.visit(ArgumentMatchers.eq(traversal, thisNode, root));

    // No error should have been reported.
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node root = createNode("function foo() { this.x = 1; }");

    Node assignmentNode = root.getFirstChild().getLastChild();

    traversal.traverse(root);

    checker.visit(ArgumentMatchers.eq(traversal, assignmentNode, root));

    // No error should have been reported.
  }

  @Test
  public void testShouldReportPropertyAccessToGlobalThis() {
    Node root = createNode("function foo() { return this.x; }");

    Node propertyAccessNode = root.getFirstChild().getLastChild();

    InOrder inOrder = inOrder(compiler);
    traversal.traverse(root);

    checker.visit(ArgumentMatchers.eq(traversal, propertyAccessNode, root));

    inOrder.verify(compiler).report(ArgumentMatchers.eq(traversal.makeError(propertyAccessNode, CheckGlobalThis.GLOBAL_THIS)));
  }

  private Node createNode(String nodeType) {
    NodeFactory nodeFactory = mock(NodeFactory.class);
    Node root = nodeFactory.createNode(Token.SCRIPT, null, null);
    Node node = nodeFactory.createNode(Token.NAME, null, nodeType);
    node.setJSDocInfo(null);
    root.appendChild(node);
    return root;
  }
}