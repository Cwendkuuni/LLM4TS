package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SimpleCompiler;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

public class CheckSideEffectsTest {

  private static final String SOURCE = "function foo() { return 42; } "
      + "var a = 1; "
      + "var b = 2; "
      + "var c = a + b; "
      + "var s = \"this string is continued on the next line but you forgot the +\"; "
      + "var x = foo(); ";

  private AbstractCompiler compiler;
  private CompilerInput input;
  private CompilerResult result;

  @Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions(null);
    compiler = new SimpleCompiler(options);
    input = new CompilerInput(SOURCE, "Test", null);
  }

  @Test
  public void testCheckSideEffects() {
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = input.getAstRoot(compiler);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();

    checkSideEffectsWarnings(result.getDiagnostics());

    assertEquals(0, checker.problemNodes.size());
  }

  @Test
  public void testCheckSideEffectsWithNoProtection() {
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false);
    Node root = input.getAstRoot(compiler);
    NodeTraversal.traverse(compiler, root, checker);

    checkSideEffectsWarnings(result.getDiagnostics());

    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals(Token.STRING, problemNode.getType());
    assertEquals("this string is continued on the next line but you forgot the +", problemNode.getString());
  }

  @Test
  public void testCheckSideEffectsWithCheckLevelError() {
    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.ERROR, true);
    Node root = input.getAstRoot(compiler);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();

    checkSideEffectsErrors(result.getDiagnostics());

    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals(Token.STRING, problemNode.getType());
    assertEquals("this string is continued on the next line but you forgot the +", problemNode.getString());
  }

  @Test
  public void testCheckSideEffectsWithSimpleOperator() {
    String source = "function foo() { return 42; } "
        + "var a = 1; "
        + "var b = 2; "
        + "var c = a + b; "
        + "var x = 0; "
        + "x = a + b; ";

    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = new NodeFactory().makeTree(source);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();

    checkSideEffectsWarnings(result.getDiagnostics());

    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals(Token.PLUS, problemNode.getType());
    Node left = problemNode.getFirstChild();
    Node right = problemNode.getLastChild();
    assertEquals(Token.VAR, left.getType());
    assertEquals("x", left.getString());
    assertEquals(Token.VAR, right.getType());
    assertEquals("b", right.getString());
  }

  @Test
  public void testCheckSideEffectsWithStatementResult() {
    String source = "function foo() { return 42; } "
        + "var a = 1; "
        + "var b = 2; "
        + "var c = a + b; "
        + "var x = foo(); ";

    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = new NodeFactory().makeTree(source);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();

    checkSideEffectsWarnings(result.getDiagnostics());

    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals(Token.NAME, problemNode.getType());
    assertEquals("x", problemNode.getString());
  }

  @Test
  public void testCheckSideEffectsWithVoidStatement() {
    String source = "function foo() { return 42; } "
        + "var a = 1; "
        + "var b = 2; "
        + "var c = a + b; "
        + "var x; ";

    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = new NodeFactory().makeTree(source);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();

    checkSideEffectsWarnings(result.getDiagnostics());

    assertEquals(0, checker.problemNodes.size());
  }

  @Test
  public void testCheckSideEffectsWithCallExpression() {
    String source = "function foo() { return 42; } "
        + "function bar() { var x = foo(); }";

    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = new NodeFactory().makeTree(source);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();

    checkSideEffectsWarnings(result.getDiagnostics());

    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals(Token.CALL, problemNode.getType());
    Node callee = problemNode.getFirstChild();
    assertEquals(Token.NAME, callee.getType());
    assertEquals("foo", callee.getString());
  }

  @Test
  public void testCheckSideEffectsWithForLoop() {
    String source = "function foo() { return 42; } "
        + "var a = 1; "
        + "for (var i = 0; i < 10; i++) {"
        + "  var x = foo(); "
        + "}"
        + "var b = 2; "
        + "var c = a + b; ";

    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = new NodeFactory().makeTree(source);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();

    checkSideEffectsWarnings(result.getDiagnostics());

    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals(Token.FOR, problemNode.getType());

    Node init = problemNode.getFirstChild();
    assertEquals(Token.VAR, init.getType());
    assertEquals("i", init.getLastChild().getString());

    Node condition = problemNode.getLastChild().getLastChild();
    assertEquals(Token.LESS_THAN, condition.getType());

    Node increment = problemNode.getLastChild().getLastChild().getLastChild();
    assertEquals(Token.PLUS, increment.getType());
  }

  @Test
  public void testCheckSideEffectsWithEvalStatement() {
    String source = "function foo() { return 42; } "
        + "var x = eval(\"foo()\"); ";

    CheckSideEffects checker = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    Node root = new NodeFactory().makeTree(source);
    NodeTraversal.traverse(compiler, root, checker);
    checker.protectSideEffects();

    checkSideEffectsWarnings(result.getDiagnostics());

    assertEquals(1, checker.problemNodes.size());
    Node problemNode = checker.problemNodes.get(0);
    assertEquals(Token.CALL, problemNode.getType());
    Node callee = problemNode.getFirstChild();
    assertEquals(Token.NAME, callee.getType());
    assertEquals("eval", callee.getString());
    Node args = problemNode.getLastChild();
    assertEquals(Token.STRING, args.getType());
    assertEquals("foo()", args.getString());
  }

  private void checkSideEffectsErrors(List<? extends DiagnosticPosition> diagnostics) {
    List<DiagnosticPosition> errors = Lists.newArrayList();
    for (DiagnosticPosition diagnostic : diagnostics) {
      if (diagnostic.getSeverity() == Diagnostic.Error) {
        errors.add(diagnostic);
      }
    }
    assertEquals(1, errors.size());
  }

  private void checkSideEffectsWarnings(List<? extends DiagnosticPosition> diagnostics) {
    List<DiagnosticPosition> warnings = Lists.newArrayList();
    for (DiagnosticPosition diagnostic : diagnostics) {
      if (diagnostic.getSeverity() == Diagnostic.Warning) {
        warnings.add(diagnostic);
      }
    }
    assertEquals(1, warnings.size());
  }
}