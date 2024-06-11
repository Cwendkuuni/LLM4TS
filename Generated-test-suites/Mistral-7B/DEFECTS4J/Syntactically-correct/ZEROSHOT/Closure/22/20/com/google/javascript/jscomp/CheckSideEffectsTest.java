package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

/**
 * Test class for {@link CheckSideEffects} class.
 */
public class CheckSideEffectsTest {

  private static AbstractCompiler compiler;
  private static NodeFactory nodeFactory;

  @org.junit.BeforeClass
  public static void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = NodeFactory.getFactory();
  }

  private Node createCheckSideEffectsNode() {
    CompilerOptions options = new CompilerOptions();
    CheckLevel checkLevel = new CheckLevel(CheckLevel.Type.WARNING, "JSC_USELESS_CODE");
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, checkLevel, true);

    Node root = nodeFactory.createProgram();
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    return checkSideEffects;
  }

  @org.junit.Test
  public void testVisitVoidStatement() {
    CheckSideEffects checkSideEffects = createCheckSideEffectsNode();
    Node voidNode = nodeFactory.createToken(Token.VOID);

    Node parent = nodeFactory.createBlock();
    parent.addChildToBack(voidNode);

    NodeTraversal traverser = NodeTraversal.create(compiler, parent, null);
    checkSideEffects.visit(traverser, null, parent);

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testVisitExpressionResult() {
    CheckSideEffects checkSideEffects = createCheckSideEffectsNode();
    Node expressionResult = nodeFactory.createExprResult();

    Node parent = nodeFactory.createBlock();
    parent.addChildToBack(expressionResult);

    NodeTraversal traverser = NodeTraversal.create(compiler, parent, null);
    checkSideEffects.visit(traverser, null, parent);

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testVisitSimpleOperator() {
    CheckSideEffects checkSideEffects = createCheckSideEffectsNode();
    Node simpleOperator = nodeFactory.createBinaryOperator(Token.PLUS,
                                                         nodeFactory.createNumberLiteral(1),
                                                         nodeFactory.createNumberLiteral(2));

    Node parent = nodeFactory.createBlock();
    parent.addChildToBack(simpleOperator);

    NodeTraversal traverser = NodeTraversal.create(compiler, parent, null);
    checkSideEffects.visit(traverser, null, parent);

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testVisitNonSideEffectingStatementNonEmpty() {
    CheckSideEffects checkSideEffects = createCheckSideEffectsNode();
    Node statement = nodeFactory.createBlock();
    statement.addChildToBack(nodeFactory.createToken(Token.ASSIGN, "x",
                                                    nodeFactory.createName("foo")));

    Node parent = nodeFactory.createBlock();
    parent.addChildToBack(statement);

    NodeTraversal traverser = NodeTraversal.create(compiler, parent, null);
    checkSideEffects.visit(traverser, null, parent);

    assertEquals(1, checkSideEffects.problemNodes.size());

    Node problemNode = checkSideEffects.problemNodes.get(0);
    assertEquals(problemNode.getParent(), parent);
    assertEquals(problemNode.getChildCount(), 1);
    assertEquals(problemNode.getChild(0).getType(), Token.ASSIGN);
  }

  @org.junit.Test
  public void testVisitNonSideEffectingStatementEmpty() {
    CheckSideEffects checkSideEffects = createCheckSideEffectsNode();
    Node statement = nodeFactory.createBlock();

    Node parent = nodeFactory.createBlock();
    parent.addChildToBack(statement);

    NodeTraversal traverser = NodeTraversal.create(compiler, parent, null);
    checkSideEffects.visit(traverser, null, parent);

    assertEquals(1, checkSideEffects.problemNodes.size());

    Node problemNode = checkSideEffects.problemNodes.get(0);
    assertEquals(problemNode.getParent(), parent);
    assertEquals(problemNode.getChildCount(), 0);
  }

  @org.junit.Test
  public void testVisitCallExpressionWithProtectorFn() {
    CheckSideEffects checkSideEffects = createCheckSideEffectsNode();
    Node protectorFnCall = nodeFactory.createCall(nodeFactory.createName("JSCOMPILER_PRESERVE"), Lists.newArrayList());

    Node parent = nodeFactory.createBlock();
    parent.addChildToBack(protectorFnCall);

    NodeTraversal traverser = NodeTraversal.create(compiler, parent, null);
    checkSideEffects.visit(traverser, null, parent);

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testVisitCallExpressionWithoutProtectorFn() {
    CheckSideEffects checkSideEffects = createCheckSideEffectsNode();
    Node callExpression = nodeFactory.createCall(nodeFactory.createName("eval"), Lists.newArrayList());

    Node parent = nodeFactory.createBlock();
    parent.addChildToBack(callExpression);

    NodeTraversal traverser = NodeTraversal.create(compiler, parent, null);
    checkSideEffects.visit(traverser, null, parent);

    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @org.junit.Test
  public void testVisitCallExpressionWithProtectorFnAndSideEffect() {
    CheckSideEffects checkSideEffects = createCheckSideEffectsNode();
    Node protectorFnCall = nodeFactory.createCall(nodeFactory.createName("JSCOMPILER_PRESERVE"), Lists.newArrayList());
    Node sideEffect = nodeFactory.createAssign(nodeFactory.createName("x"), nodeFactory.createNumberLiteral(1));

    Node parent = nodeFactory.createBlock();
    parent.addChildToBack(protectorFnCall);
    parent.addChildToBack(sideEffect);

    NodeTraversal traverser = NodeTraversal.create(compiler, parent, null);
    checkSideEffects.visit(traverser, null, parent);

    assertEquals(1, checkSideEffects.problemNodes.size());

    Node problemNode = checkSideEffects.problemNodes.get(0);
    assertEquals(problemNode.getParent(), parent);
    assertEquals(problemNode.getChildCount(), 1);
    assertEquals(problemNode.getChild(0).getType(), Token.ASSIGN);
  }

  @org.junit.Test
  public void testVisitCallExpressionWithProtectorFnAndDeadCode() {
    CheckSideEffects checkSideEffects = createCheckSideEffectsNode();
    Node protectorFnCall = nodeFactory.createCall(nodeFactory.createName("JSCOMPILER_PRESERVE"), Lists.newArrayList());
    Node deadCode = nodeFactory.createAssign(nodeFactory.createName("x"), nodeFactory.createNumberLiteral(1));
    Node sideEffect = nodeFactory.createAssign(nodeFactory.createName("y"), nodeFactory.createNumberLiteral(2));

    Node parent = nodeFactory.createBlock();
    parent.addChildToBack(protectorFnCall);
    parent.addChildToBack(deadCode);
    parent.addChildToBack(sideEffect);

    NodeTraversal traverser = NodeTraversal.create(compiler, parent, null);
    checkSideEffects.visit(traverser, null, parent);

    assertEquals(1, checkSideEffects.problemNodes.size());

    Node problemNode = checkSideEffects.problemNodes.get(0);
    assertEquals(problemNode.getParent(), parent);
    assertEquals(problemNode.getChildCount(), 1);
    assertEquals(problemNode.getChild(0).getType(), Token.ASSIGN);
  }

  @org.junit.Test
  public void testProtectSideEffects() {
    CheckSideEffects checkSideEffects = createCheckSideEffectsNode();
    checkSideEffects.protectSideEffects();

    Node externs = checkSideEffects.compiler.getSynthesizedExternsInput().getAstRoot(checkSideEffects.compiler);
    assertEquals(1, externs.getChildCount());

    Node extern = externs.getChild(0);
    assertEquals(extern.getChildCount(), 1);

    Node name = extern.getChild(0);
    assertEquals(name.getString(), "JSCOMPILER_PRESERVE");

    Node var = name.getNext();
    assertEquals(var.getChildCount(), 0);
  }

  @org.junit.Test
  public void testStripProtection() {
    CheckSideEffects checkSideEffects = createCheckSideEffectsNode();
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(checkSideEffects.compiler);

    Node root = nodeFactory.createProgram();
    Node protectorFnCall = nodeFactory.createCall(nodeFactory.createName("JSCOMPILER_PRESERVE"), Lists.newArrayList());
    Node sideEffect = nodeFactory.createAssign(nodeFactory.createName("x"), nodeFactory.createNumberLiteral(1));

    root.addChildToBack(protectorFnCall);
    root.addChildToBack(sideEffect);

    NodeTraversal.traverse(checkSideEffects.compiler, root, stripProtection);

    assertEquals(1, sideEffect.getChildCount());
    assertEquals(sideEffect.getChild(0).getType(), Token.ASSIGN);
  }
}