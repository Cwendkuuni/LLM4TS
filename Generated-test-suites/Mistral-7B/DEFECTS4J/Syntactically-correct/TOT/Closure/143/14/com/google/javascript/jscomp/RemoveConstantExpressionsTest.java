package com.google.javascript.jscomp;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ParallelCompilerPass;
import com.google.javascript.jscomp.test.TestCompiler;
import com.google.javascript.rhino.Token;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.google.javascript.jscomp.NodeUtil.nodeTypeMayHaveSideEffects;
import static org.junit.Assert.assertEquals;

public class RemoveConstantExpressionsTest {
  private Compiler compiler;
  private ParallelCompilerPass<RemoveConstantExpressions> pass;

  @Before
  public void setUp() {
    compiler = new TestCompiler();
    pass = new ParallelCompilerPass<RemoveConstantExpressions>(
        new RemoveConstantExpressions(compiler));
  }

  @Test
  public void testSimpleExpression() {
    String input = "1 + foo() + bar()";
    List<Node> ast = compiler.parse(input, null);

    Node root = ast.get(0);
    pass.process(null, root);
    Node expected = new Node(Token.EXPR_LIST, Lists.newArrayList(
        new Node(Token.EXPR_RESULT, nodeFromName("foo()")),
        new Node(Token.EXPR_RESULT, nodeFromName("bar()")))
    );

    assertEquals(expected, root);
  }

  @Test
  public void testComplexExpression() {
    String input = "1 + foo() * bar() + baz()";
    List<Node> ast = compiler.parse(input, null);

    Node root = ast.get(0);
    pass.process(null, root);
    Node expected = new Node(Token.EXPR_LIST, Lists.newArrayList(
        new Node(Token.EXPR_RESULT, nodeFromName("foo()")),
        new Node(Token.TOKEN, Token.MULTIPLY),
        new Node(Token.EXPR_RESULT, nodeFromName("bar()")),
        new Node(Token.EXPR_LIST, Lists.newArrayList(
            new Node(Token.EXPR_RESULT, nodeFromName("baz()"))
        ))
    ));

    assertEquals(expected, root);
  }

  @Test
  public void testNoSideEffects() {
    String input = "1 + 2 + 3";
    List<Node> ast = compiler.parse(input, null);

    Node root = ast.get(0);
    pass.process(null, root);
    Node expected = new Node(Token.EXPR_LIST, Lists.newArrayList(
        new Node(Token.EXPR_RESULT, new Node(Token.NUM, 1)),
        new Node(Token.PLUS),
        new Node(Token.EXPR_RESULT, new Node(Token.NUM, 2)),
        new Node(Token.PLUS),
        new Node(Token.EXPR_RESULT, new Node(Token.NUM, 3))
    ));

    assertEquals(expected, root);
  }

  @Test(expected = AssertionError.class)
  public void testSideEffectInInput() {
    String input = "1 + foo()++";
    List<Node> ast = compiler.parse(input, null);

    Node root = ast.get(0);
    pass.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testSideEffectInSubexpression() {
    String input = "1 + foo() * ++i";
    List<Node> ast = compiler.parse(input, null);

    Node root = ast.get(0);
    pass.process(null, root);
  }

  private Node nodeFromName(String name) {
    return new Node(Token.IDENT, name);
  }
}