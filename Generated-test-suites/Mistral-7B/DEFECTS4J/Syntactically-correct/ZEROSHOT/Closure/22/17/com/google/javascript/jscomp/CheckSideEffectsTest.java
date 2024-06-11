package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Token;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Before;
import org.junit.Test;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckerFactory checkerFactory;
  private NodeTraversal nodeTraversal;

  @Before
  public void setUp() {
    checkerFactory = mock(CheckerFactory.class);
    compiler = mock(AbstractCompiler.class);
    when(compiler.getCheckFactory()).thenReturn(checkerFactory);
    nodeTraversal = new NodeTraversal(compiler, null, null);
  }

  @Test
  public void testVisitVoidStatement() {
    Node root = NodeOperations.createRootNode("void x;");
    nodeTraversal.traverse(root);
    assertTrue(root.isVoid());
  }

  @Test
  public void testVisitStringLiteralStatement() {
    Node root = NodeOperations.createRootNode("var s = \"this string is continued on the next line but you forgot the +\";");
    nodeTraversal.traverse(root);
    assertTrue(root.isBlock());
    Node messageNode = root.getFirstChild();
    assertTrue(messageNode.isString());
    Node plusNode = messageNode.getNext();
    assertTrue(plusNode == null || plusNode.isName() && "+" == plusNode.getType());
  }

  @Test
  public void testVisitAssignmentStatement() {
    Node root = NodeOperations.createRootNode("x = foo();");
    nodeTraversal.traverse(root);
    assertTrue(root.isBlock());
    Node assignmentNode = root.getFirstChild();
    assertTrue(assignmentNode.isName());
    assertTrue(assignmentNode.getName().getString().equals("x"));
    Node equalsNode = assignmentNode.getNext();
    assertTrue(equalsNode.isCall());
    Node functionCallNode = equalsNode.getFirstChild();
    assertTrue(functionCallNode.isName());
    assertTrue(functionCallNode.getName().getString().equals("foo"));
    assertTrue(assignmentNode.getLastChild().isSemicolon());
  }

  @Test
  public void testVisitEmptyStatement() {
    Node root = NodeOperations.createRootNode(";");
    nodeTraversal.traverse(root);
    assertTrue(root.isVoid());
  }

  @Test
  public void testVisitEmptyBlock() {
    Node root = NodeOperations.createRootNode("{}");
    nodeTraversal.traverse(root);
    assertTrue(root.isBlock());
    assertTrue(root.getChildCount() == 0);
  }

  @Test
  public void testVisitCallStatementWithEval() {
    Node root = NodeOperations.createRootNode("eval('x = 1');");
    nodeTraversal.traverse(root);
    assertTrue(root.isBlock());
    Node callNode = root.getFirstChild();
    assertTrue(callNode.isCall());
    Node functionCallNode = callNode.getFirstChild();
    assertTrue(functionCallNode.isName());
    assertTrue(functionCallNode.getName().getString().equals("eval"));
    Node argumentNode = functionCallNode.getNext();
    assertTrue(argumentNode.isString());
  }

  @Test
  public void testVisitBlockWithStatementAndExpressionResult() {
    Node root = NodeOperations.createRootNode("{x = 1;}");
    nodeTraversal.traverse(root);
    assertTrue(root.isBlock());
    Node assignmentNode = root.getFirstChild();
    assertTrue(assignmentNode.isName());
    assertTrue(assignmentNode.getName().getString().equals("x"));
    Node equalsNode = assignmentNode.getNext();
    assertTrue(equalsNode.isNumber());
    assertTrue(equalsNode.getDoubleValue() == 1.0);
  }

  @Test
  public void testVisitStatementWithSideEffect() {
    Node root = NodeOperations.createRootNode("document.write('Hello World');");
    nodeTraversal.traverse(root);
    // This test should not throw an error, since document.write is a side effect.
  }

  @Test
  public void testProtectSideEffects() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    CompilerInput input = new CompilerInput(new StringReader("function f() { x = 1; }"));
    checkSideEffects.process(input.getExterns(), input.getRootNode());
    Node protectedNode = checkSideEffects.problemNodes.get(0);
    Node protectedCallNode = protectedNode.getLastChild();
    Node protectedFunctionCallNode = protectedCallNode.getFirstChild();
    Node protectedNameNode = protectedFunctionCallNode.getFirstChild();
    assertTrue(protectedNameNode.isName());
    assertTrue(protectedNameNode.getName().getString().equals(CheckSideEffects.PROTECTOR_FN));
    assertTrue(protectedCallNode.isCall());
    assertTrue(protectedFunctionCallNode.isName());
    assertTrue(protectedFunctionCallNode.getName().getString().equals(CheckSideEffects.PROTECTOR_FN));
  }

  @Test
  public void testStripProtection() {
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    CompilerInput input = new CompilerInput(new StringReader("function f() { var jscomp_protector_fn = JSCOMPILER_PRESERVE; jscomp_protector_fn(x = 1); }"));
    checkSideEffects.process(input.getExterns(), input.getRootNode());
    checkSideEffects.protectSideEffects();
    Node strippedNode = checkSideEffects.problemNodes.get(0);
    assertTrue(strippedNode.isName());
    assertTrue(strippedNode.getName().getString().equals("x"));
    assertTrue(strippedNode.getParent().getChildCount() == 1);
    assertTrue(strippedNode.getParent().getChild(0).isNumber());
    assertTrue(strippedNode.getParent().getChild(0).getDoubleValue() == 1.0);

    StripProtection stripProtection = new StripProtection(compiler);
    stripProtection.process(input.getExterns(), input.getRootNode());

    assertTrue(input.getRootNode().getChildCount() == 1);
    assertTrue(input.getRootNode().getChild(0).isName());
    assertTrue(input.getRootNode().getChild(0).getName().getString().equals("f"));
  }
}