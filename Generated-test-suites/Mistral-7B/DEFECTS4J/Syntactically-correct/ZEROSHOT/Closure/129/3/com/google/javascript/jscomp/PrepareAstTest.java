package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.PrepareAst;
import com.google.javascript.jscomp.PrepareAnnotations;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class PrepareAstTest {
  private Compiler compiler;
  private PrepareAst prepareAst;

  @Before
  public void setup() {
    compiler = mock(Compiler.class);
    prepareAst = new PrepareAst(compiler, false);
  }

  @Test
  public void testNormalizeNodeTypesEmptyAst() {
    Node root = NodeFactory.createRoot();
    prepareAst.process(null, root);
    assertNull(root.getFirstChild());
  }

  @Test
  public void testNormalizeNodeTypesSimpleAst() {
    Node root = NodeFactory.createIntLit(1);
    prepareAst.process(null, root);
    assertNull(root.getFirstChild());
  }

  @Test
  public void testNormalizeNodeTypesSimpleAstWithChild() {
    Node root = NodeFactory.createIntLit(1);
    Node child = NodeFactory.createIntLit(2);
    root.addChild(child);
    prepareAst.process(null, root);
    assertSame(child, root.getFirstChild());
  }

  @Test
  public void testNormalizeNodeTypesControlStructureWithBlock() {
    Node root = NodeFactory.createIf(NodeFactory.createBoolLit(true));
    Node block = IR.block().srcref(root);
    root.replaceChild(root.getFirstChild(), block);
    prepareAst.process(null, root);
    assertSame(block, root.getFirstChild());
  }

  @Test(expected = AssertionError.class)
  public void testNormalizeNodeTypesControlStructureNoBlock() {
    Node root = NodeFactory.createIf(NodeFactory.createBoolLit(true));
    Node child = NodeFactory.createIntLit(1);
    root.addChild(child);
    prepareAst.process(null, root);
  }

  @Test
  public void testNormalizeNodeTypesFunctionWithBlock() {
    Node root = NodeFactory.createFunction(
        NodeFactory.createIdentifier("foo"),
        new Token(Token.NONE, ""),
        null,
        NodeFactory.createBlock(null));
    prepareAst.process(null, root);
    assertSame(root.getLastChild(), root.getFirstChild());
  }

  @Test
  public void testNormalizeNodeTypesFunctionNoBlock() {
    Node root = NodeFactory.createFunction(
        NodeFactory.createIdentifier("foo"),
        new Token(Token.NONE, ""),
        null,
        null);
    prepareAst.process(null, root);
    assertNull(root.getFirstChild());
  }

  @Test
  public void testPrepareAnnotationsObjectLit() {
    Node objlit = NodeFactory.createObjectLit();
    when(compiler.getSourceFile()).thenReturn(mock(SourceFile.class));
    PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
    NodeTraversal.traverse(compiler, objlit, prepareAnnotations);
    assertNull(objlit.getJSDocInfo());
  }

  @Test
  public void testPrepareAnnotationsObjectLitWithAnnotation() {
    Node objlit = NodeFactory.createObjectLit();
    Node jsdoc = NodeFactory.createJSDocInfo("Java interface");
    when(compiler.getSourceFile()).thenReturn(mock(SourceFile.class));
    objlit.setJSDocInfo(jsdoc);
    PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
    NodeTraversal.traverse(compiler, objlit, prepareAnnotations);
    assertSame(jsdoc, objlit.getJSDocInfo());
  }

  @Test
  public void testPrepareAnnotationsCallWithoutThis() {
    Node call = NodeFactory.createCall(
        NodeFactory.createIdentifier("foo"),
        new Node[0]);
    when(compiler.getSourceFile()).thenReturn(mock(SourceFile.class));
    PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
    NodeTraversal.traverse(compiler, call, prepareAnnotations);
    assertFalse(call.getBooleanProp(Node.FREE_CALL));
  }

  @Test
  public void testPrepareAnnotationsCallWithThis() {
    Node call = NodeFactory.createCall(
        NodeFactory.createGet(
            NodeFactory.createThis(),
            NodeFactory.createIdentifier("foo")),
        new Node[0]);
    when(compiler.getSourceFile()).thenReturn(mock(SourceFile.class));
    PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
    NodeTraversal.traverse(compiler, call, prepareAnnotations);
    assertTrue(call.getBooleanProp(Node.FREE_CALL));
  }

  @Test
  public void testPrepareAnnotationsDirectEval() {
    Node call = NodeFactory.createCall(
        NodeFactory.createIdentifier("eval"),
        new Node[0]);
    when(compiler.getSourceFile()).thenReturn(mock(SourceFile.class));
    PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
    NodeTraversal.traverse(compiler, call, prepareAnnotations);
    assertFalse(call.getBooleanProp(Node.DIRECT_EVAL));
  }

  @Test
  public void testPrepareAnnotationsDirectEvalWithDirectEval() {
    Node call = NodeFactory.createCall(
        NodeFactory.createIdentifier("eval"),
        new Node[0]);
    call.putBooleanProp(Node.DIRECT_EVAL, true);
    when(compiler.getSourceFile()).thenReturn(mock(SourceFile.class));
    PrepareAnnotations prepareAnnotations = new PrepareAnnotations();
    NodeTraversal.traverse(compiler, call, prepareAnnotations);
    assertSame(call, prepareAnnotations.lastNode);
    assertSame(call, prepareAnnotations.lastNodeWithDirectEval);
    assertSame(call, prepareAnnotations.lastNodeWithNoFreeCall);
  }

  @Test
  public void testPrepareAnnotationsFunctionWithDispatcher() {
    Node dispatcher = NodeFactory.createFunction(
        NodeFactory.createIdentifier("dispatch"),
        new Token(Token.NONE, ""),
        null,
        null);
    Node function = NodeFactory.createFunction(
        NodeFactory.createIdentifier("foo"),
        new Token(Token.NONE, ""),
        null,
        null);
    NodeTraversal.traverse(compiler, function, prepareAnnotations);
    assertFalse(function.getBooleanProp(Node.IS_DISPATCHER));

    Node parent = NodeFactory.createAssign(
        NodeFactory.createIdentifier("x"),
        function);
    NodeTraversal.traverse(compiler, parent, prepareAnnotations);
    assertSame(function, parent.getLastChild());
    assertSame(function, prepareAnnotations.lastNodeWithDispatcher);
  }

  @Test
  public void testPrepareAnnotationsObjectLitKeyWithAnnotation() {
    Node objlit = NodeFactory.createObjectLit();
    Node key = NodeFactory.createIdentifier("a");
    Node value = NodeFactory.createFunction(
        NodeFactory.createIdentifier("foo"),
        new Token(Token.NONE, ""),
        null,
        null);
    objlit.addChild(key);
    objlit.addChild(value);
    NodeTraversal.traverse(compiler, objlit, prepareAnnotations);
    assertNull(key.getJSDocInfo());
    assertNull(value.getJSDocInfo());

    Node expectedJsdoc = NodeFactory.createJSDocInfo("A property");
    key.setJSDocInfo(expectedJsdoc);
    NodeTraversal.traverse(compiler, objlit, prepareAnnotations);
    assertSame(expectedJsdoc, key.getJSDocInfo());
  }

  private static boolean assertSame(Node expected, Node actual) {
    if (expected != actual) {
      throw new AssertionError("Expected: " + expected + "\nActual: " + actual);
    }
    return true;
  }
}

class SourceFile {
  // Empty implementation
}