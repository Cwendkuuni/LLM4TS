package com.google.javascript.jscomp.test;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class RemoveConstantExpressionsTest {

  @Test
  public void testRemoveConstantExpressionsConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(compiler);
    assertNotNull(removeConstantExpressions);
  }

  @Test
  public void testProcessMethod() {
    Node externs = new Node(Token.PROGRAM, null);
    Node root = new Node(
        Token.BLOCK,
        List.of(
            new Node(Token.EXPR_STATEMENT, new Node(Token.ASSIGN, Token.NAME, new Node(Token.NUMBER, 5))),
            new Node(Token.EXPR_STATEMENT, new Node(Token.ASSIGN, Token.NAME, new Node(Token.NUMBER, 10))),
            new Node(
                Token.EXPR_STATEMENT,
                new Node(
                    Token.ASSIGN,
                    Token.NAME,
                    new Node(Token.ADD, new Node(Token.NAME, "foo"), new Node(Token.NUMBER, 5))
                )
            ),
            new Node(Token.EXPR_STATEMENT, new Node(Token.ASSIGN, Token.NAME, new Node(Token.NUMBER, 15)))
        )
    );

    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(null);
    removeConstantExpressions.process(externs, root);

    Node expectedRoot = new Node(
        Token.BLOCK,
        List.of(
            new Node(Token.EXPR_STATEMENT, new Node(Token.ASSIGN, Token.NAME, new Node(Token.NUMBER, 5))),
            new Node(Token.EXPR_STATEMENT, new Node(Token.ASSIGN, Token.NAME, new Node(Token.NUMBER, 10))),
            new Node(
                Token.EXPR_STATEMENT,
                new Node(
                    Token.ASSIGN,
                    Token.NAME,
                    new Node(Token.NAME, "foo")
                )
            ),
            new Node(Token.EXPR_STATEMENT, new Node(Token.ASSIGN, Token.NAME, new Node(Token.NUMBER, 15)))
        )
    );

    assertEquals(expectedRoot, root);
  }

  @Test
  public void testProcessMethodWithInvalidInputs() {
    // Test with null inputs, empty program, etc.
    Node externs = null; // or empty Node
    Node root = null; // or empty Node

    RemoveConstantExpressions removeConstantExpressions = new RemoveConstantExpressions(null);
    removeConstantExpressions.process(externs, root);

    // Check that the program is unchanged or an error is thrown
    // based on the edge case or error handling scenarios
    // For example: checking for null inputs
    assertNotNull(externs);
    assertNotNull(root);
  }

  @Test
  public void testCallbackMethod() {
    Node inputNode = new Node(Token.ADD, new Node(Token.NUMBER, 5), new Node(Token.NUMBER, 10));

    RemoveConstantRValuesCallback callback = new RemoveConstantRValuesCallback();

    // Test if the callback replaces the constant expression with side effect nodes
    Node expectedResult = new Node(
        Token.EXPR_STATEMENT,
        new Node(Token.ASSIGN, Token.NAME, new Node(Token.NUMBER, 5))
    );

    Node result = callback.getSideEffectNodes(inputNode).get(0);
    assertEquals(expectedResult, result);
  }
}