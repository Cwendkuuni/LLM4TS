package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckSideEffects;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfoBuilder;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Tests for the {@code CheckSideEffects} class.
 */
public class CheckSideEffectsTest {

  private static final AbstractCompiler COMPILER = mock(AbstractCompiler.class);
  private static final NodeFactory NODE_FACTORY = mock(NodeFactory.class);
  private static final NodeTraversal NODE_TRAVERSAL = mock(NodeTraversal.class);
  private static final CheckSideEffects CHECK_SIDE_EFFECTS;
  private static final CheckLevel CHECK_LEVEL = CheckLevel.WARNING;
  private static final boolean PROTECT_SIDE_EFFECT_FREE_CODE = true;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    CHECK_SIDE_EFFECTS = new CheckSideEffects(COMPILER, CHECK_LEVEL, PROTECT_SIDE_EFFECT_FREE_CODE);
    when(COMPILER.getFactory()).thenReturn(NODE_FACTORY);
    when(COMPILER.getTraversal()).thenReturn(NODE_TRAVERSAL);
    when(NODE_FACTORY.call1(NODE_FACTORY, "JSCOMPILER_PRESERVE")).thenReturn(
        IR.name("JSCOMPILER_PRESERVE"));
  }

  @Test
  public void testProcessNoSideEffect() {
    Node root = IR.block(
        IR.exprResult(
            IR.add(
                IR.string("this string is "),
                IR.string("continued on the next line but you forgot the +"))));
    CHECK_SIDE_EFFECTS.process(null, root);
    assertEquals(0, CHECK_SIDE_EFFECTS.problemNodes.size());
  }

  @Test
  public void testProcessSideEffect() {
    Node root = IR.block(
        IR.exprResult(IR.call(IR.name("alert"))),
        IR.exprResult(IR.add(
            IR.string("this string is "),
            IR.string("continued on the next line but you forgot the +"))));
    CHECK_SIDE_EFFECTS.process(null, root);
    assertEquals(1, CHECK_SIDE_EFFECTS.problemNodes.size());
    Node problemNode = CHECK_SIDE_EFFECTS.problemNodes.get(0);
    assertEquals(Token.CALL, problemNode.getType());
  }

  @Test
  public void testVisitVoidNode() {
    Node parent = IR.block(IR.exprResult(IR.semicolon()));
    Node n = IR.semicolon();
    CHECK_SIDE_EFFECTS.visit(NODE_TRAVERSAL, n, parent);
  }

  @Test
  public void testVisitSimpleOp() {
    Node parent = IR.block(IR.exprResult(IR.add(IR.number(1), IR.number(2))));
    Node n = IR.add(IR.number(1), IR.number(2));
    CHECK_SIDE_EFFECTS.visit(NODE_TRAVERSAL, n, parent);
  }

  @Test
  public void testVisitStatementWithSideEffect() {
    Node parent = IR.block(IR.exprResult(IR.add(IR.number(1), IR.number(2))));
    Node n = IR.add(IR.number(1), IR.number(2));
    n.setJSDocInfo(new JSDocInfoBuilder(false).build(n));
    CHECK_SIDE_EFFECTS.visit(NODE_TRAVERSAL, n, parent);
    assertEquals(1, CHECK_SIDE_EFFECTS.problemNodes.size());
    Node problemNode = CHECK_SIDE_EFFECTS.problemNodes.get(0);
    assertEquals(Token.NAME, problemNode.getType());
  }

  @Test
  public void testVisitCallToProtectorFunction() {
    Node parent = IR.block(IR.exprResult(IR.call(IR.name("JSCOMPILER_PRESERVE"), IR.number(1))));
    Node n = IR.call(IR.name("JSCOMPILER_PRESERVE"), IR.number(1));
    CHECK_SIDE_EFFECTS.visit(NODE_TRAVERSAL, n, parent);
  }

  @Test
  public void testProtectSideEffects() {
    Node root = IR.block(IR.exprResult(IR.call(IR.name("JSCOMPILER_PRESERVE"), IR.number(1))));
    CHECK_SIDE_EFFECTS.protectSideEffects();
    assertEquals(1, CHECK_SIDE_EFFECTS.problemNodes.size());
    Node problemNode = CHECK_SIDE_EFFECTS.problemNodes.get(0);
    assertEquals(Token.CALL, problemNode.getType());
  }

  @Test
  public void testRemoveSideEffectProtection() {
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(COMPILER);
    Node root = IR.block(
        IR.call(IR.name("JSCOMPILER_PRESERVE"), IR.number(1)),
        IR.exprResult(IR.call(IR.name("JSCOMPILER_PRESERVE"), IR.number(1))));
    stripProtection.process(null, root);
    assertEquals(0, root.getChildCount());
  }
}