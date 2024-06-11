package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParserTestUtils;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

/**
 * Tests the RemoveConstantExpressions class.
 */
public class RemoveConstantExpressionsTest {

  private Compiler compiler;

  @org.junit.Before
  public void setUp() throws Exception {
    compiler = ParserTestUtils.createCompiler();
  }

  /**
   * Tests that the RemoveConstantExpressions pass correctly removes
   * constant expressions from simple statements.
   */
  public void testSimpleStatements() {
    String input = "1 + foo() + bar();";
    Node node = ParserTestUtils.parse(compiler, input);
    CompilerPass removeConstantExpressions = new RemoveConstantExpressions(compiler);
    removeConstantExpressions.process(null, node);
    Node expected = new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
        NodeFactory.nodeForToken(Token.IDENTIFIER, "foo"),
        NodeFactory.nodeForToken(Token.IDENTIFIER, "bar")
    ));
    assertTrue(NodeUtil.isEquivalent(node, expected));
  }

  /**
   * Tests that the RemoveConstantExpressions pass correctly leaves
   * subexpressions in place if they have side-effects.
   */
  public void testSubexpressionsWithSideEffects() {
    String input = "console.log(1 + foo() + bar());";
    Node node = ParserTestUtils.parse(compiler, input);
    CompilerPass removeConstantExpressions = new RemoveConstantExpressions(compiler);
    removeConstantExpressions.process(null, node);
    Node expected = node;
    assertTrue(NodeUtil.isEquivalent(node, expected));
  }

  /**
   * Tests that the RemoveConstantExpressions pass correctly handles
   * complex statements with several constant subexpressions.
   */
  public void testComplexStatements() {
    String input = "1 + foo() * (bar() + baz()) + qux();";
    Node node = ParserTestUtils.parse(compiler, input);
    CompilerPass removeConstantExpressions = new RemoveConstantExpressions(compiler);
    removeConstantExpressions.process(null, node);
    Node expected = new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
        NodeFactory.nodeForToken(Token.IDENTIFIER, "foo"),
        NodeFactory.nodeForToken(Token.IDENTIFIER, "bar"),
        NodeFactory.nodeForToken(Token.IDENTIFIER, "baz"),
        NodeFactory.nodeForToken(Token.IDENTIFIER, "qux")
    ));
    assertTrue(NodeUtil.isEquivalent(node, expected));
  }

  /**
   * Tests that the RemoveConstantExpressions pass correctly does not
   * remove subexpressions that may have side-effects if they are used
   * as parts of expression with side-effects.
   */
  public void testSubexpressionsUsedAsPartsOfSideEffectExpressions() {
    String input = "if (1 + foo() + bar()) { console.log('Hello World'); }";
    Node node = ParserTestUtils.parse(compiler, input);
    CompilerPass removeConstantExpressions = new RemoveConstantExpressions(compiler);
    removeConstantExpressions.process(null, node);
    Node expected = node;
    assertTrue(NodeUtil.isEquivalent(node, expected));
  }

  /**
   * Tests that the RemoveConstantExpressions pass correctly does not
   * remove expressions from within conditional statements.
   */
  public void testConditionalExpressions() {
    String input = "if (1 + foo() > 0) { console.log('Positive'); }";
    Node node = ParserTestUtils.parse(compiler, input);
    CompilerPass removeConstantExpressions = new RemoveConstantExpressions(compiler);
    removeConstantExpressions.process(null, node);
    Node expected = node;
    assertTrue(NodeUtil.isEquivalent(node, expected));
  }

  /**
   * Tests that the RemoveConstantExpressions pass correctly handles
   * multiple passes.
   */
  public void testMultiplePasses() {
    String input = "1 + foo() + (1 + bar());";
    Node node = ParserTestUtils.parse(compiler, input);
    CompilerPass removeConstantExpressions = new RemoveConstantExpressions(compiler);
    removeConstantExpressions.process(null, node);
    assertFalse(NodeUtil.isEquivalent(node, new Node(Token.EXPR_STATEMENT,
        Lists.newArrayList(NodeFactory.nodeForToken(Token.IDENTIFIER, "foo"),
            NodeFactory.nodeForToken(Token.IDENTIFIER, "bar")))));
    removeConstantExpressions.process(null, node);
    Node expected = new Node(Token.EXPR_STATEMENT, Lists.newArrayList(
        NodeFactory.nodeForToken(Token.IDENTIFIER, "foo"),
        NodeFactory.nodeForToken(Token.IDENTIFIER, "bar")
    ));
    assertTrue(NodeUtil.isEquivalent(node, expected));
  }

  /**
   * Tests that the RemoveConstantExpressions pass correctly does not
   * simplify expressions that have the same value but different types.
   */
  public void testDifferentExpressionTypes() {
    String input = "1 + Math.floor(Math.random() * 2) + 2;";
    Node node = ParserTestUtils.parse(compiler, input);
    CompilerPass removeConstantExpressions = new RemoveConstantExpressions(compiler);
    removeConstantExpressions.process(null, node);
    Node expected = node;
    assertTrue(NodeUtil.isEquivalent(node, expected));
  }
}