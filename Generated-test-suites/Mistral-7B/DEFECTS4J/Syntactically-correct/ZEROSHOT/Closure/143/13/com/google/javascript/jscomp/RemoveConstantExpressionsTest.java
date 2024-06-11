package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass.Result;
import com.google.javascript.rhino.Token;

import java.util.List;

/**
 * Test class for RemoveConstantExpressions.
 */
public class RemoveConstantExpressionsTest {

  @org.junit.BeforeClass
  public static void setup() {
    Compiler compiler = new Compiler();
    compiler.addPass(new RemoveConstantExpressions(compiler));
  }

  /**
   * Test method for RemoveConstantExpressions.process().
   */
  @org.junit.Test
  public void testRemoveConstantExpressions() {
    String input = "1 + foo() + bar()";
    Node root = new Node(Token.PROGRAM, null);
    NodeTraversal.traverse(null, root, new NodeFactory(input).create());

    Result result = new RemoveConstantExpressions(new Compiler()).process(null, root).getResult();
    assertTrue(result.changed);

    Node expected = new Node(Token.EXPR_LIST, null);
    expected.setFirstChild(new Node(Token.IDENTIFIER, "foo"));
    expected.setNext(new Node(Token.EXPR_LIST, null));
    expected.getNext().setFirstChild(new Node(Token.IDENTIFIER, "bar"));

    Node actual = result.getAst();
    assertTrue(NodeUtil.isTreeEqual(expected, actual));
  }

  /**
   * Test method for RemoveConstantExpressions when there are no constant
   * expressions to remove.
   */
  @org.junit.Test
  public void testNoConstantExpressions() {
    String input = "foo() + bar() * baz()";
    Node root = new Node(Token.PROGRAM, null);
    NodeTraversal.traverse(null, root, new NodeFactory(input).create());

    Result result = new RemoveConstantExpressions(new Compiler()).process(null, root).getResult();
    assertFalse(result.changed);

    Node actual = result.getAst();
    Node expected = new Node(Token.EXPR_LIST, null);
    expected.setFirstChild(new Node(Token.IDENTIFIER, "foo"));
    expected.setNext(new Node(Token.OP_PLUS, null));
    expected.getNext().setFirstChild(new Node(Token.IDENTIFIER, "bar"));
    expected.getNext().setNext(new Node(Token.OP_TIMES, null));
    expected.getNext().getNext().setFirstChild(new Node(Token.IDENTIFIER, "baz"));

    assertTrue(NodeUtil.isTreeEqual(expected, actual));
  }
}

class NodeFactory {
  private final String input;

  NodeFactory(String input) {
    this.input = input;
  }

  Node create() {
    Node root = new Node(Token.PROGRAM, null);
    new Parser(new Compiler().getLanguage()).parseProgram(
        root, new TextSource(input, null, null, 0, input.length()));
    return root;
  }
}