package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
    traversal = NodeTraversal.create(new Node(), null, new Callback[0]);
  }

  @Test
  public void testShouldTraverseGlobalFunction() {
    Node globalFunction = Node.newFunction("globalFunc", new Node[0], new Node[0]);
    Node parent = Node.newName("window");
    Node grandParent = Node.newName("window");

    when(globalFunction.getParent()).thenReturn(parent);
    when(parent.getParent()).thenReturn(grandParent);

    assertThat(checker.shouldTraverse(traversal, Node.newThis(), grandParent)).isTrue();
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node constructorFunction = Node.newFunction("Constructor", new Node[0], new Node[0]);
    Node jsDocInfo = Node.newJsDocInfo("Constructor", null, null, true, false, null, null);
    constructorFunction.setJSDocInfo(jsDocInfo);
    Node parent = Node.newName("constructor");

    assertThat(checker.shouldTraverse(traversal, Node.newThis(), parent)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnoation() {
    Node functionWithThisAnnotation = Node.newFunction("functionWithThis", new Node[0], new Node[0]);
    Node jsDocInfo = Node.newJsDocInfo("functionWithThis", null, "@this", null, null, null, null);
    functionWithThisAnnotation.setJSDocInfo(jsDocInfo);
    Node parent = Node.newName("functionWithThis");

    assertThat(checker.shouldTraverse(traversal, Node.newThis(), parent)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionInObjectLiteral() {
    Node objectLiteral = Node.newObjectLiteral();
    Node functionInObjectLiteral = Node.newFunction("functionInObject", new Node[0], new Node[0]);
    Node property = Node.newName("property");
    Node propertyValue = Node.newObjectLiteral();
    Node parent = Node.newName("object");

    objectLiteral.addChildrenToSequence(property, propertyValue);
    property.addChildrenToSequence(functionInObjectLiteral);

    assertThat(checker.shouldTraverse(traversal, Node.newThis(), parent)).isFalse();
  }

  @Test
  public void testShouldNotTraverseFunctionLentToPrototype() {
    Node functionLentToPrototype = Node.newFunction("functionLentToPrototype", new Node[0], new Node[0]);
    Node objectLiteral = Node.newObjectLiteral();
    Node prototype = Node.newName("prototype");
    Node parent = Node.newName("object");

    objectLiteral.addChildrenToSequence(prototype);
    prototype.addChildrenToSequence(Node.newName("prototypeProperty"), functionLentToPrototype);

    assertThat(checker.shouldTraverse(traversal, Node.newThis(), parent)).isFalse();
  }

  @Test
  public void testShouldTraverseAssignment() {
    Node leftSide = Node.newName("a");
    Node rightSide = Node.newThis();
    Node parent = Node.newAssign(Token.ASSIGN, leftSide, rightSide);

    ArgumentCaptor<DiagnosticType> diagnosticTypeArgumentCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    checker.visit(traversal, Node.newThis(), parent);
    traversal.traverse(checker);
    compiler.reportError(diagnosticTypeArgumentCaptor.capture());
    assertThat(diagnosticTypeArgumentCaptor.getValue()).isEqualTo(CheckGlobalThis.GLOBAL_THIS);
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    Node leftSide = Node.newGetProp(Node.newName("prototype"), Node.newName("property"));
    Node rightSide = Node.newThis();
    Node parent = Node.newAssign(Token.ASSIGN, leftSide, rightSide);

    checker.visit(traversal, Node.newThis(), parent);
    traversal.traverse(checker);
    assertThat(compiler.reportedErrors()).isEmpty();
  }
}