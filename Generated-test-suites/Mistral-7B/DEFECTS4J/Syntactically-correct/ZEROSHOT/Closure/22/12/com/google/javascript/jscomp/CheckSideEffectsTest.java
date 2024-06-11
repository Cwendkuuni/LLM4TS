package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.JSDocInfo;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.SimpleCompilerPass;
import com.google.javascript.jscomp.test.TestSourceLoader;

import java.util.List;

/**
 * Test class for CheckSideEffects
 */
public class CheckSideEffectsTest {

  private static final String TEST_FILE = "CheckSideEffectsTest.js";
  private static final String SOURCE_FILE = "CheckSideEffects.js";

  @org.junit.Before
  public void setUp() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(DiagnosticType.WARNING, CheckSideEffects.USELESS_CODE_ERROR);
    Compiler compiler = new Compiler(options, new CheckerFactory());
    compiler.addPass(new CheckSideEffects(compiler, CheckLevel.WARNING, true));
    compiler.addPass(new SimpleCompilerPass.ErrorCollectorPass());

    TestSourceLoader sourceLoader = new TestSourceLoader();
    sourceLoader.addSourceFile(TEST_FILE, "test");
    sourceLoader.addSourceFile(SOURCE_FILE, "CheckSideEffects");
    compiler.processString(sourceLoader.getTestInputForFile(TEST_FILE), TEST_FILE);
    compiler.getDiagnostics().printDiagnostics();
  }

  @org.junit.Test
  public void testCheckSideEffects() {
    List<Node> problemNodes = Lists.newArrayList();
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, true);

    Node root = createTestNode();
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    problemNodes.addAll(checkSideEffects.problemNodes);

    verifyProblemNodes(problemNodes);
  }

  @org.junit.Test
  public void testProtectSideEffects() {
    List<Node> problemNodes = Lists.newArrayList();
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, CheckLevel.WARNING, false);

    Node root = createTestNode();
    NodeTraversal.traverse(compiler, root, checkSideEffects);
    checkSideEffects.protectSideEffects();

    List<Node> problemNodesAfterProtection = Lists.newArrayList();
    CheckSideEffects checkSideEffectsAfterProtection = new CheckSideEffects(compiler, CheckLevel.WARNING, true);
    NodeTraversal.traverse(compiler, root, checkSideEffectsAfterProtection);
    problemNodesAfterProtection.addAll(checkSideEffectsAfterProtection.problemNodes);

    verifyProblemNodesAfterProtection(problemNodes, problemNodesAfterProtection);
  }

  private Node createTestNode() {
    Node root = new Node(Token.BLOCK);
    root.setDebugInfo(TEST_FILE, 1, 1);
    root.addChildToBack(new Node(Token.VAR, "x", null, null, null, null));
    root.addChildToBack(new Node(Token.ASSIGN, null, null, new Node(Token.NAME, "foo"), null, null, null));
    root.addChildToBack(new Node(Token.NAME, "foo"));
    root.addChildToBack(new Node(Token.SEMICOLON, null, null));
    root.addChildToBack(new Node(Token.NAME, "x"));
    root.addChildToBack(new Node(Token.EQUALS, null, null, new Node(Token.NAME, "foo"), null, null, null));
    root.addChildToBack(new Node(Token.SEMICOLON, null, null));
    return root;
  }

  private void verifyProblemNodes(List<Node> problemNodes) {
    assertEquals(3, problemNodes.size());
    assertEquals(NodeUtil.getLineNumber(problemNodes.get(0)), 7);
    assertEquals(NodeUtil.getLineNumber(problemNodes.get(1)), 8);
    assertEquals(NodeUtil.getLineNumber(problemNodes.get(2)), 10);
  }

  private void verifyProblemNodesAfterProtection(List<Node> problemNodes, List<Node> problemNodesAfterProtection) {
    assertEquals(0, problemNodesAfterProtection.size());
  }
}