package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
  }

  @org.junit.Test
  public void testShouldTraverseFunctions() {
    Node functionNode = Node.newFunction("functionName", "params", null);
    Node parentNode = Node.newName("parentName");
    Node parentParentNode = Node.newBlock();

    when(functionNode.getParent()).thenReturn(parentNode);
    when(parentNode.getParent()).thenReturn(parentParentNode);
    when(parentNode.getJSDocInfo()).thenReturn(null);
    when(parentParentNode.getType()).thenReturn(Token.SCRIPT);

    assertThat(checker.shouldTraverse(new NodeTraversal(checker), functionNode, parentParentNode)).isTrue();
  }

  @org.junit.Test
  public void testShouldNotTraverseConstructors() {
    Node constructorNode = Node.newFunction("functionName", "params", null);
    constructorNode.setIsConstructor(true);
    Node parentNode = Node.newName("parentName");
    Node parentParentNode = Node.newBlock();

    when(constructorNode.getParent()).thenReturn(parentNode);
    when(parentNode.getParent()).thenReturn(parentParentNode);
    when(parentNode.getJSDocInfo()).thenReturn(null);
    when(parentParentNode.getType()).thenReturn(Token.SCRIPT);

    assertThat(checker.shouldTraverse(new NodeTraversal(checker), constructorNode, parentParentNode)).isFalse();
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionsWithThisAnnotation() {
    Node functionNode = Node.newFunction("functionName", "params", null);
    functionNode.setJSDocInfo(createJSDocInfo("this"));
    Node parentNode = Node.newName("parentName");
    Node parentParentNode = Node.newBlock();

    when(functionNode.getParent()).thenReturn(parentNode);
    when(parentNode.getParent()).thenReturn(parentParentNode);
    when(parentNode.getJSDocInfo()).thenReturn(null);
    when(parentParentNode.getType()).thenReturn(Token.SCRIPT);

    assertThat(checker.shouldTraverse(new NodeTraversal(checker), functionNode, parentParentNode)).isFalse();
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionsAnnotatedWithInterface() {
    Node functionNode = Node.newFunction("functionName", "params", null);
    functionNode.setJSDocInfo(createJSDocInfo("interface"));
    Node parentNode = Node.newName("parentName");
    Node parentParentNode = Node.newBlock();

    when(functionNode.getParent()).thenReturn(parentNode);
    when(parentNode.getParent()).thenReturn(parentParentNode);
    when(parentNode.getJSDocInfo()).thenReturn(null);
    when(parentParentNode.getType()).thenReturn(Token.SCRIPT);

    assertThat(checker.shouldTraverse(new NodeTraversal(checker), functionNode, parentParentNode)).isFalse();
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionsWithNoContext() {
    Node functionNode = Node.newFunction("functionName", "params", null);
    Node parentNode = Node.newName("parentName");
    Node parentParentNode = Node.newName("grandParentName");

    when(functionNode.getParent()).thenReturn(parentNode);
    when(parentNode.getParent()).thenReturn(parentParentNode);
    when(parentNode.getJSDocInfo()).thenReturn(null);
    when(parentParentNode.getType()).thenReturn(Token.ASSIGN);

    assertThat(checker.shouldTraverse(new NodeTraversal(checker), functionNode, parentParentNode)).isFalse();
  }

  @org.junit.Test
  public void testShouldTraverseAssignments() {
    Node assignNode = Node.newAssign(null, Node.newName("x"), Node.newThis());
    Node parentNode = Node.newName("parentName");

    when(assignNode.getParent()).thenReturn(parentNode);
    when(parentNode.getType()).thenReturn(Token.ASSIGN);

    assertThat(checker.shouldTraverse(new NodeTraversal(checker), assignNode, null)).isTrue();
  }

  @org.junit.Test
  public void testShouldNotTraverseAssignmentsToPrototypeProperties() {
    Node assignNode = Node.newAssign(Node.newName("parentName").getLastChild(), Node.newName("x"), Node.newThis());
    Node parentNode = Node.newName("parentName");

    when(assignNode.getParent()).thenReturn(parentNode);
    when(parentNode.getLastChild().getString()).thenReturn("prototype");

    assertThat(checker.shouldTraverse(new NodeTraversal(checker), assignNode, null)).isFalse();
  }

  @org.junit.Test
  public void testShouldNotTraverseAssignmentsToPrototypeSubproperties() {
    Node assignNode = Node.newAssign(Node.newGetProp(Node.newName("parentName"), "child"), Node.newName("x"), Node.newThis());
    Node parentNode = Node.newName("parentName");

    when(assignNode.getParent()).thenReturn(parentNode);
    when(parentNode.getFirstChild().getString()).thenReturn("child");
    when(parentNode.getFirstChild().getLastChild().getString()).thenReturn("prototype");

    assertThat(checker.shouldTraverse(new NodeTraversal(checker), assignNode, null)).isFalse();
  }

  @org.junit.Test
  public void testShouldReportGlobalThis() {
    Node globalThisNode = Node.newThis();
    Node parentNode = Node.newBlock();

    when(globalThisNode.getParent()).thenReturn(parentNode);

    checker.visit(new NodeTraversal(checker), globalThisNode, parentNode);
    checker.compiler.reportedErrors.clear();

    assertThat(checker.compiler.reportedErrors).hasSize(1);
    assertThat(checker.compiler.reportedErrors.get(0).getMessage()).isEqualTo("dangerous use of the global 'this' object");
  }

  private JSDocInfo createJSDocInfo(String annotation) {
    return new JSDocInfo(new JSDocComment(), new JSDocParam[] {});
  }
}