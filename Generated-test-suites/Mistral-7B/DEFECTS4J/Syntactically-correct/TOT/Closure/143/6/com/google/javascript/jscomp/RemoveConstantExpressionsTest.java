package com.google.javascript.jscomp.tests;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class RemoveConstantExpressionsTest {
  private Compiler compiler;
  private CompilerPass removeConstantExpressions;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() throws Exception {
    compiler = new Compiler();
    removeConstantExpressions = new RemoveConstantExpressions(compiler);
    nodeFactory = compiler.getFactory();
  }

  @Test
  public void testSimpleExpressionReplacement() {
    String source = "1 + foo() + bar();";
    Node root = nodeFactory.parseTree(source);

    ParallelCompilerPass.runPasses(compiler, root, removeConstantExpressions);

    Node expected = nodeFactory.parseTree("foo();bar()");
    Node actual = NodeUtil.getFirstChild(removeConstantExpressions.getResult().getOutput());

    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testNoChangeForNonExpressionStatement() {
    String source = "var x = 1 + foo() + bar();";
    Node root = nodeFactory.parseTree(source);

    ParallelCompilerPass.runPasses(compiler, root, removeConstantExpressions);

    Node actual = NodeUtil.getFirstChild(removeConstantExpressions.getResult().getOutput());
    assertNotNull(actual);
    assertEquals("1 + foo() + bar()", actual.toString());
  }

  @Test
  public void testNoChangeForSingleStatementWithoutSideEffect() {
    String source = "if (foo()) { bar(); }";
    Node root = nodeFactory.parseTree(source);

    ParallelCompilerPass.runPasses(compiler, root, removeConstantExpressions);

    Node actual = NodeUtil.getFirstChild(removeConstantExpressions.getResult().getOutput());
    assertNotNull(actual);
    assertEquals("if (foo()) { bar(); }", actual.toString());
  }

  @Test
  public void testNoChangeForComplexStatement() {
    String source = "if (foo()) { bar(); baz(); } else { qux(); }";
    Node root = nodeFactory.parseTree(source);

    ParallelCompilerPass.runPasses(compiler, root, removeConstantExpressions);

    Node actual = NodeUtil.getFirstChild(removeConstantExpressions.getResult().getOutput());
    assertNotNull(actual);
    assertEquals("if (foo()) { bar(); baz(); } else { qux(); }", actual.toString());
  }

  @Test
  public void testChangeWithSideEffectBefore() {
    String source = "foo(); 1 + bar();";
    Node root = nodeFactory.parseTree(source);

    Node expected = nodeFactory.parseTree("foo(); bar()");
    ParallelCompilerPass.runPasses(compiler, root, removeConstantExpressions);

    Node actual = NodeUtil.getFirstChild(removeConstantExpressions.getResult().getOutput());
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testChangeWithSideEffectAfter() {
    String source = "1 + foo(); bar();";
    Node root = nodeFactory.parseTree(source);

    Node expected = nodeFactory.parseTree("foo(); bar()");
    ParallelCompilerPass.runPasses(compiler, root, removeConstantExpressions);

    Node actual = NodeUtil.getFirstChild(removeConstantExpressions.getResult().getOutput());
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testChangeWithSideEffectInside() {
    String source = "1 + foo(); bar(); baz();";
    Node root = nodeFactory.parseTree(source);

    Node expected = nodeFactory.parseTree("foo(); bar(); baz()");
    ParallelCompilerPass.runPasses(compiler, root, removeConstantExpressions);

    Node actual = NodeUtil.getFirstChild(removeConstantExpressions.getResult().getOutput());
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testChangeWithSideEffectInConditional() {
    String source = "if (1 + foo()) { bar(); }";
    Node root = nodeFactory.parseTree(source);

    Node expected = nodeFactory.parseTree("if (foo()) { bar(); }");
    ParallelCompilerPass.runPasses(compiler, root, removeConstantExpressions);

    Node actual = NodeUtil.getFirstChild(removeConstantExpressions.getResult().getOutput());
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testChangeWithMultipleSideEffects() {
    String source = "foo(); 1 + bar(); qux();";
    Node root = nodeFactory.parseTree(source);

    Node expected = nodeFactory.parseTree("foo(); bar(); qux()");
    ParallelCompilerPass.runPasses(compiler, root, removeConstantExpressions);

    Node actual = NodeUtil.getFirstChild(removeConstantExpressions.getResult().getOutput());
    assertEquals(expected.toString(), actual.toString());
  }

  @Test
  public void testNoChangeForComments() {
    String source = "//comment\n1 + foo() + bar();";
    Node root = nodeFactory.parseTree(source);

    ParallelCompilerPass.runPasses(compiler, root, removeConstantExpressions);

    Node actual = NodeUtil.getFirstChild(removeConstantExpressions.getResult().getOutput());
    assertNotNull(actual);
    assertEquals("//comment\n1 + foo() + bar()", actual.toString());
  }

  @Test
  public void testNoChangeForStrings() {
    String source = "\"foo\" + foo() + bar();";
    Node root = nodeFactory.parseTree(source);

    ParallelCompilerPass.runPasses(compiler, root, removeConstantExpressions);

    Node actual = NodeUtil.getFirstChild(removeConstantExpressions.getResult().getOutput());
    assertNotNull(actual);
    assertEquals("\"foo\" + foo() + bar()", actual.toString());
  }

  @Test
  public void testNoChangeForObjects() {
    String source = "{foo: 1} + foo() + bar();";
    Node root = nodeFactory.parseTree(source);

    ParallelCompilerPass.runPasses(compiler, root, removeConstantExpressions);

    Node actual = NodeUtil.getFirstChild(removeConstantExpressions.getResult().getOutput());
    assertNotNull(actual);
    assertEquals("{foo: 1} + foo() + bar()", actual.toString());
  }

  @Test
  public void testNoChangeForComplexExpressions() {
    String source = "(1 + foo()) * bar();";
    Node root = nodeFactory.parseTree(source);

    ParallelCompilerPass.runPasses(compiler, root, removeConstantExpressions);

    Node actual = NodeUtil.getFirstChild(removeConstantExpressions.getResult().getOutput());
    assertNotNull(actual);
    assertEquals("(1 + foo()) * bar()", actual.toString());
  }

  @Test
  public void testNoChangeForCycle() {
    String source = "var x = 0;\n" +
        "x = 1 + foo();\n" +
        "foo() = x;";
    Node root = nodeFactory.parseTree(source);

    try {
      ParallelCompilerPass.runPasses(compiler, root, removeConstantExpressions);
      fail("Should throw an exception due to cyclic reference");
    } catch (RuntimeException e) {
      assertFalse(removeConstantExpressions.getResult().changed);
    }
  }
}