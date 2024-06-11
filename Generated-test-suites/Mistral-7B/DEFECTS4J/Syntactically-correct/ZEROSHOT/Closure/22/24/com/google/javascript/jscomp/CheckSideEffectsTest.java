package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SimpleSourceFile;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

public class CheckSideEffectsTest {

  private AbstractCompiler compiler;
  private CheckSideEffects checker;
  private CheckerFactory checkerFactory;
  private CompilerOptions options;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    compiler = mock(AbstractCompiler.class);
    checkerFactory = mock(CheckerFactory.class);
    options = new CompilerOptions();
    nodeFactory = new NodeFactory();
    when(checkerFactory.getCheck(AbstractPostOrderCallback.class, CheckSideEffects.class))
        .thenReturn(checker = new CheckSideEffects(compiler, CheckLevel.WARNING, false));
  }

  @Test
  public void testVisitExpressionResult() {
    Node root = nodeFactory.createExpressionStatement(Token.ASSIGN, nodeFactory.createName("x"),
        nodeFactory.createNumberLiteral(1));
    NodeTraversal traversal = new NodeTraversal(compiler, options, checkerFactory, null);
    Node result = nodeFactory.createExpressionStatement(Token.ASSIGN, nodeFactory.createName("y"),
        nodeFactory.createCall(nodeFactory.createName("foo"), nodeFactory.createList(nodeFactory.createName("x"))));
    root.addChildToBack(result);
    traversal.traverse(root, null);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testVisitVoidStatement() {
    Node root = nodeFactory.createExpressionStatement(Token.VOID, null);
    NodeTraversal traversal = new NodeTraversal(compiler, options, checkerFactory, null);
    traversal.traverse(root, null);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testVisitSimpleOperator() {
    Node root = nodeFactory.createExpressionStatement(Token.ASSIGN, nodeFactory.createName("x"),
        nodeFactory.createBinaryOperator(Token.ADD,
            nodeFactory.createName("s"),
            nodeFactory.createStringLiteral("this string is ")));
    NodeTraversal traversal = new NodeTraversal(compiler, options, checkerFactory, null);
    traversal.traverse(root, null);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testVisitStatementWithSideEffect() {
    Node root = nodeFactory.createExpressionStatement(Token.ASSIGN, nodeFactory.createName("x"),
        nodeFactory.createCall(nodeFactory.createName("foo"), nodeFactory.createList()));
    NodeTraversal traversal = new NodeTraversal(compiler, options, checkerFactory, null);
    traversal.traverse(root, null);
    assertTrue(checker.problemNodes.size() == 1);
    Node node = checker.problemNodes.get(0);
    DiagnosticType diagnosticType = checker.level.getDiagnosticType();
    List<String> expectedArgs = Lists.newArrayList("Suspicious code.", "This code lacks side-effects. Is there a bug?");
    assertTrue(NodeUtil.getMessage(node)
        .equals(String.format(diagnosticType.getFormat(), expectedArgs.toArray(new String[0]))));
  }

  @Test
  public void testVisitCallStatementWithSideEffect() {
    Node root = nodeFactory.createExpressionStatement(Token.ASSIGN, nodeFactory.createName("x"),
        nodeFactory.createCall(nodeFactory.createName("foo"), nodeFactory.createList()));
    NodeTraversal traversal = new NodeTraversal(compiler, options, checkerFactory, null);
    traversal.traverse(root, null);
    assertTrue(checker.problemNodes.size() == 1);
    Node node = checker.problemNodes.get(0);
    DiagnosticType diagnosticType = checker.level.getDiagnosticType();
    List<String> expectedArgs = Lists.newArrayList("Suspicious code.", "The result of the 'foo' call is not being used.");
    assertTrue(NodeUtil.getMessage(node)
        .equals(String.format(diagnosticType.getFormat(), expectedArgs.toArray(new String[0]))));
  }

  @Test
  public void testVisitStatementWithSideEffectAndJSDocInfo() {
    Node root = nodeFactory.createStatement(Token.BLOCK, null,
        nodeFactory.createStatement(Token.ASSIGN, nodeFactory.createName("x"),
            nodeFactory.createCall(nodeFactory.createName("foo"), nodeFactory.createList())),
        nodeFactory.createStatement(Token.ASSIGN, nodeFactory.createName("y"),
            nodeFactory.createCall(nodeFactory.createName("bar"), nodeFactory.createList())));
    NodeTraversal traversal = new NodeTraversal(compiler, options, checkerFactory, null);
    traversal.traverse(root, null);
    assertTrue(checker.problemNodes.size() == 1);
    Node node = checker.problemNodes.get(0);
    DiagnosticType diagnosticType = checker.level.getDiagnosticType();
    List<String> expectedArgs = Lists.newArrayList("Suspicious code.", "The result of the 'foo' call is not being used.");
    assertTrue(NodeUtil.getMessage(node)
        .equals(String.format(diagnosticType.getFormat(), expectedArgs.toArray(new String[0]))));
  }

  @Test
  public void testVisitStatementWithSideEffectAndJSDocInfoWithProtectedCode() {
    SimpleSourceFile input = new SimpleSourceFile("Test.js", "test() {");
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(CheckerFactory.TypeChecker, CheckLevel.WARNING);
    compiler.compile(input, options);
    AbstractCompiler compilerMock = mock(AbstractCompiler.class);
    when(compilerMock.getInput()).thenReturn(input);
    checker = new CheckSideEffects(compilerMock, CheckLevel.WARNING, true);
    Node root = nodeFactory.createStatement(Token.BLOCK, null,
        nodeFactory.createStatement(Token.ASSIGN, nodeFactory.createName("x"),
            nodeFactory.createCall(nodeFactory.createName("foo"), nodeFactory.createList())),
        nodeFactory.createStatement(Token.ASSIGN, nodeFactory.createName("y"),
            nodeFactory.createCall(nodeFactory.createName("bar"), nodeFactory.createList())));
    NodeTraversal traversal = new NodeTraversal(compilerMock, options, checkerFactory, null);
    traversal.traverse(root, null);
    assertTrue(checker.problemNodes.isEmpty());
  }

  @Test
  public void testProtectSideEffects() {
    Node root = nodeFactory.createStatement(Token.BLOCK, null,
        nodeFactory.createStatement(Token.ASSIGN, nodeFactory.createName("x"),
            nodeFactory.createCall(nodeFactory.createName("foo"), nodeFactory.createList())),
        nodeFactory.createStatement(Token.ASSIGN, nodeFactory.createName("y"),
            nodeFactory.createCall(nodeFactory.createName("bar"), nodeFactory.createList())));
    checker.protectSideEffects();
    Node expected = nodeFactory.createCall(nodeFactory.createName("JSCOMPILER_PRESERVE"),
        nodeFactory.createList(nodeFactory.createStringLiteral("foo")));
    Node actual = checker.problemNodes.get(0);
    assertTrue(actual.getParent().equals(root));
    assertTrue(actual.getLastChild().equals(expected));
  }

  @Test
  public void testStripProtection() {
    SimpleSourceFile input = new SimpleSourceFile("Test.js", "test() {JSCOMPILER_PRESERVE foo(); JSCOMPILER_PRESERVE bar();}");
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    compiler.compile(input, options);
    StripProtection stripProtection = new StripProtection(compiler);
    Node root = nodeFactory.createStatement(Token.BLOCK, null,
        nodeFactory.createStatement(Token.ASSIGN, nodeFactory.createName("x"),
            nodeFactory.createCall(nodeFactory.createName("JSCOMPILER_PRESERVE"),
                nodeFactory.createList(nodeFactory.createName("foo")))));
    NodeTraversal traversal = new NodeTraversal(compiler, options, checkerFactory, null);
    traversal.traverse(root, stripProtection);
    assertTrue(stripProtection.problemNodes.isEmpty());
    assertTrue(root.getLastChild().isUndefined());
  }
}