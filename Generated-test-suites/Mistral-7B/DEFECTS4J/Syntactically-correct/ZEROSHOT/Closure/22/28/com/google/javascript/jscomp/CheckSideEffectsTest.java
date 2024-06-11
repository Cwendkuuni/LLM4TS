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
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.IR;
import com.google.javascript.jscomp.JSDocInfoBuilder;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.CompilationUnit;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

/**
 * Test for CheckSideEffects class.
 *
 */
public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checkSideEffects;
  private NodeFactory nodeFactory;
  private CompilerInput input;
  private CompilationUnit root;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = new NodeFactory();
    input = new CompilerInput("test.js", "");
    root = new CompilationUnit(input);
    checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
  }

  @Test
  public void testEmptyBlock() {
    Node block = nodeFactory.createBlock();
    checkSideEffects.visit(null, block, null);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testComma() {
    Node comma = nodeFactory.createComma();
    checkSideEffects.visit(null, comma, null);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testVoidStatements() {
    Node voidStatement1 = nodeFactory.createStatement();
    Node voidStatement2 = nodeFactory.createStatement();
    Node block = nodeFactory.createBlock().setFirstChild(voidStatement1)
        .setLastChild(voidStatement2);
    checkSideEffects.visit(null, block, null);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testStatementWithSideEffect() {
    Node name = nodeFactory.createName("console");
    Node call = nodeFactory.createCall(name, "log");
    Node statement = nodeFactory.createStatement(call);
    Node block = nodeFactory.createBlock().setFirstChild(statement);
    checkSideEffects.visit(null, block, null);
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(statement, checkSideEffects.problemNodes.get(0));
  }

  @Test
  public void testSimpleOperator() {
    Node add = nodeFactory.createBinaryOp(Token.ADD, nodeFactory.createNumber(1),
        nodeFactory.createNumber(2));
    Node statement = nodeFactory.createStatement(add);
    Node block = nodeFactory.createBlock().setFirstChild(statement);
    checkSideEffects.visit(null, block, null);
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(add, checkSideEffects.problemNodes.get(0));
  }

  @Test
  public void testSimpleOperatorUsed() {
    Node add = nodeFactory.createBinaryOp(Token.ADD, nodeFactory.createName("a"),
        nodeFactory.createName("b"));
    Node assign = nodeFactory.createAssignment(Token.ASSIGN,
        nodeFactory.createName("c"), add);
    Node statement = nodeFactory.createStatement(assign);
    Node block = nodeFactory.createBlock().setFirstChild(statement);
    checkSideEffects.visit(null, block, null);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testAssignment() {
    Node name = nodeFactory.createName("a");
    Node assign = nodeFactory.createAssignment(Token.ASSIGN, name,
        nodeFactory.createNumber(1));
    Node statement = nodeFactory.createStatement(assign);
    Node block = nodeFactory.createBlock().setFirstChild(statement);
    checkSideEffects.visit(null, block, null);
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(assign, checkSideEffects.problemNodes.get(0));
  }

  @Test
  public void testCallStatement() {
    Node name = nodeFactory.createName("console");
    Node call = nodeFactory.createCall(name, "log");
    Node statement = nodeFactory.createStatement(call);
    Node block = nodeFactory.createBlock().setFirstChild(statement);
    checkSideEffects.visit(null, block, null);
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(call, checkSideEffects.problemNodes.get(0));
  }

  @Test
  public void testCallExpression() {
    Node name = nodeFactory.createName("console");
    Node call = nodeFactory.createCall(name, "log");
    Node expression = nodeFactory.createExpression(call);
    Node block = nodeFactory.createBlock().setFirstChild(expression);
    checkSideEffects.visit(null, block, null);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testSimpleExpression() {
    Node add = nodeFactory.createBinaryOp(Token.ADD, nodeFactory.createNumber(1),
        nodeFactory.createNumber(2));
    Node expression = nodeFactory.createExpression(add);
    Node block = nodeFactory.createBlock().setFirstChild(expression);
    checkSideEffects.visit(null, block, null);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testCommaExpression() {
    Node add = nodeFactory.createBinaryOp(Token.ADD, nodeFactory.createNumber(1),
        nodeFactory.createNumber(2));
    Node expression = nodeFactory.createExpression(nodeFactory.createComma());
    expression.setFirstChild(add);
    Node block = nodeFactory.createBlock().setFirstChild(expression);
    checkSideEffects.visit(null, block, null);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testCallWithSideEffect() {
    Node name = nodeFactory.createName("console");
    Node call = nodeFactory.createCall(name, "log");
    Node statement = nodeFactory.createStatement(call);
    Node block = nodeFactory.createBlock().setFirstChild(statement);
    checkSideEffects.visit(null, block, null);
    assertEquals(1, checkSideEffects.problemNodes.size());
    assertEquals(statement, checkSideEffects.problemNodes.get(0));
  }

  @Test
  public void testProtectSideEffectFreeCode() {
    Node name = nodeFactory.createName("console");
    Node call = nodeFactory.createCall(name, "log");
    Node statement = nodeFactory.createStatement(call);
    Node block = nodeFactory.createBlock().setFirstChild(statement);
    checkSideEffects.process(input, root);
    checkSideEffects.protectSideEffects();
    Node protectorFn = root.getFirstChild();
    Node callToProtectorFn = nodeFactory.createCall(protectorFn, "call");
    Node replacement = nodeFactory.createExpression(callToProtectorFn);
    Node nodeToProtect = checkSideEffects.problemNodes.get(0);
    Node parent = nodeToProtect.getParent();
    parent.replaceChild(nodeToProtect, replacement);
    replacement.addChildToBack(nodeToProtect);
    checkSideEffects.compiler.reportCodeChange();
    List<Node> newProblemNodes = Lists.newArrayList();
    checkSideEffects.visit(null, block, null);
    assertEquals(0, checkSideEffects.problemNodes.size());
    assertEquals(1, newProblemNodes.size());
    assertEquals(replacement, newProblemNodes.get(0));
  }

  @Test
  public void testRemoveProtection() {
    CheckSideEffects.StripProtection stripProtection = new CheckSideEffects.StripProtection(compiler);
    Node protectorFn = nodeFactory.createName(CheckSideEffects.PROTECTOR_FN);
    Node callToProtectorFn = nodeFactory.createCall(protectorFn, "call");
    Node replacement = nodeFactory.createExpression(callToProtectorFn);
    Node nodeToProtect = root.getFirstChild();
    Node parent = nodeToProtect.getParent();
    parent.replaceChild(nodeToProtect, replacement);
    replacement.addChildToBack(nodeToProtect);
    stripProtection.process(input, root);
    assertEquals(0, root.getChildCount());
  }

  @Test(expected = AssertionError.class)
  public void testInvalidCheck() {
    Node name = nodeFactory.createName("console");
    Node call = nodeFactory.createCall(name, "invalidCheck");
    Node statement = nodeFactory.createStatement(call);
    Node block = nodeFactory.createBlock().setFirstChild(statement);
    checkSideEffects.visit(null, block, null);
  }

  @Test
  public void testJsDocInfo() {
    Node jsDocInfoNode = nodeFactory.createJSDocInfo("@function foo() {}");
    Node statement = nodeFactory.createStatement(jsDocInfoNode);
    Node block = nodeFactory.createBlock().setFirstChild(statement);
    checkSideEffects.visit(null, block, null);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testExpressionResult() {
    Node exprResult = nodeFactory.createExpressionResult();
    Node statement = nodeFactory.createStatement(exprResult);
    Node block = nodeFactory.createBlock().setFirstChild(statement);
    checkSideEffects.visit(null, block, null);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  @Test
  public void testForLoop() {
    Node name = nodeFactory.createName("i");
    Node initializer = nodeFactory.createAssignment(Token.ASSIGN, name,
        nodeFactory.createNumber(0));
    Node condition = nodeFactory.createExpression(nodeFactory.createBinaryOp(
        Token.LESS_EQUAL, name, nodeFactory.createNumber(10)));
    Node increment = nodeFactory.createExpression(nodeFactory.createBinaryOp(
        Token.ADD, name, nodeFactory.createNumber(1)));
    Node forStmt = nodeFactory.createForStatement(initializer, condition, increment,
        nodeFactory.createBlock());
    Node block = nodeFactory.createBlock().setFirstChild(forStmt);
    checkSideEffects.visit(null, block, null);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }
}