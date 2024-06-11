package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import org.junit.Test;

public class CheckGlobalThisTest {
  private AbstractCompiler compiler;
  private Checker checker;
  private NodeTraversal traversal;

  @Test
  public void testShouldTraverse() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
    traversal = mock(NodeTraversal.class);

    Node node = Node.newNode(Token.FUNCTION, "evil");
    Node parent = Node.newBlock("evilBlock");

    when(compiler.getChecker()).thenReturn(checker);
    when(checker.getTraversal()).thenReturn(traversal);
    when(node.getParent()).thenReturn(parent);

    assertTrue(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldNotTraverseConstructors() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
    traversal = mock(NodeTraversal.class);

    Node node = Node.newNode(Token.FUNCTION, "Constructor", null, null, null, null, null, null, null, null, null, null, null, null, null, null, Token.FUNCTION_DECLARATION);
    Node parent = Node.newBlock("constructorBlock");

    when(compiler.getChecker()).thenReturn(checker);
    when(checker.getTraversal()).thenReturn(traversal);
    when(node.getParent()).thenReturn(parent);
    when(node.getJSDocInfo()).thenReturn(createConstructorJsDoc());

    assertFalse(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldNotTraverseOverride() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
    traversal = mock(NodeTraversal.class);

    Node node = Node.newNode(Token.FUNCTION, "override", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, Token.FUNCTION_DECLARATION);
    Node parent = Node.newBlock("overridenBlock");

    when(compiler.getChecker()).thenReturn(checker);
    when(checker.getTraversal()).thenReturn(traversal);
    when(node.getParent()).thenReturn(parent);
    when(node.getJSDocInfo()).thenReturn(createOverrideJsDoc());

    assertFalse(checker.shouldTravere(traversal, node, parent));
  }

  @Test
  public void testShouldNotTraverseInterface() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
    traversal = mock(NodeTraversal.class);

    Node node = Node.newNode(Token.FUNCTION, "InterfaceFunction", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, Token.FUNCTION_DECLARATION);
    Node parent = Node.newBlock("interfaceBlock");

    when(compiler.getChecker()).thenReturn(checker);
    when(checker.getTraversal()).thenReturn(traversal);
    when(node.getParent()).thenReturn(parent);
    when(node.getJSDocInfo()).thenReturn(createInterfaceJsDoc());

    assertFalse(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldNotTraversePrototype() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
    traversal = mock(NodeTraversal.class);

    Node node = Node.newNode(Token.ASSIGN, null, Node.newNode(Token.GET_PROP, null, Node.newNode(Token.NAME, "prototype")), null);
    Node parent = Node.newBlock("prototypeBlock");

    when(compiler.getChecker()).thenReturn(checker);
    when(checker.getTraversal()).thenReturn(traversal);
    when(node.getParent()).thenReturn(parent);

    assertFalse(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldNotTraversePrototypeKey() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
    traversal = mock(NodeTraversal.class);

    Node node = Node.newNode(Token.GET_PROP, null, Node.newNode(Token.STRING_KEY, "prototype"));
    Node parent = Node.newBlock("prototypeKeyBlock");

    when(compiler.getChecker()).thenReturn(checker);
    when(checker.getTraversal()).thenReturn(traversal);
    when(node.getParent()).thenReturn(parent);

    assertFalse(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldTraverseAssignment() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);
    traversal = mock(NodeTraversal.class);

    Node node = Node.newNode(Token.ASSIGN, null, Node.newNode(Token.THIS), Node.newNode(Token.NAME, "property"));
    Node parent = Node.newBlock("assignmentBlock");

    when(compiler.getChecker()).thenReturn(checker);
    when(checker.getTraversal()).thenReturn(traversal);
    when(node.getParent()).thenReturn(parent);

    assertTrue(checker.shouldTraverse(traversal, node, parent));
  }

  @Test
  public void testShouldReportThis() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);

    Node node = Node.newNode(Token.THIS);

    assertTrue(checker.shouldReportThis(node));
  }

  @Test
  public void testShouldNotReportThisWhenLhsOfAssignment() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);

    Node node = Node.newNode(Token.THIS);
    Node parent = Node.newNode(Token.ASSIGN, null, node, Node.newNode(Token.NAME, "property"));

    assertFalse(checker.shouldReportThis(node));
  }

  @Test
  public void testShouldReportThisWhenPropertyAccess() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler);

    Node node = Node.newNode(Token.THIS);
    Node parent = Node.newNode(Token.GET_PROP, null, node);

    assertTrue(checker.shouldReportThis(node));
  }

  private JSDocInfo createConstructorJsDoc() {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    return jsDoc;
  }

  private JSDocInfo createOverrideJsDoc() {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    return jsDoc;
  }

  private JSDocInfo createInterfaceJsDoc() {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setInterface(true);
    return jsDoc;
  }
}