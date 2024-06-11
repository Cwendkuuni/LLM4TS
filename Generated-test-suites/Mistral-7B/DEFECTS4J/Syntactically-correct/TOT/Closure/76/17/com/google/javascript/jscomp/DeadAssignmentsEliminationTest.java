package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private DeadAssignmentsElimination eliminator;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    eliminator = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testDeadAssignmentsElimination() {
    String sourceCode = "function foo() {" +
        "var x = 1, y = 1;" +
        "x = y;" +
        "y = 2;" +
        "print(x);" +
        "}";

    Node root = AbstractCompiler.compile(compiler, sourceCode, "foo");

    eliminator.process(null, root);

    List<Node> expectedNodes = ImmutableList.of(
        new Node(Token.FUNCTION, null, null, null),
        new Node(Token.VAR, null, null, null),
        new Node(Token.VAR, Token.IDENTIFIER, null, null, null),
        new Node(Token.ASSIGN, Token.ASSIGN, null, null, null, null,
            new Node(Token.IDENTIFIER, Token.IDENTIFIER, null, null),
            new Node(Token.IDENTIFIER, Token.IDENTIFIER, null, null)),
        new Node(Token.VAR, Token.IDENTIFIER, null, null, null),
        new Node(Token.ASSIGN, Token.ASSIGN, null, null, null, null,
            new Node(Token.IDENTIFIER, Token.IDENTIFIER, null, null),
            new Node(Token.NUMBER, null, null, null, 2, null)),
        new Node(Token.PRINT, null, null, null),
        new Node(Token.RETURN, null, null, null)
    );

    NodeTraversal nodeTraversal = new NodeTraversal(root, null);
    Node currentNode = root;
    int index = 0;

    while (currentNode != null) {
      if (NodeOperations.deepEquals(currentNode, expectedNodes.get(index))) {
        index++;
      }
      currentNode = currentNode.getNext();
    }

    Preconditions.checkState(index == expectedNodes.size(),
        "Unexpected nodes found in the CFG");
  }

  @Test
  public void testDeadAssignmentEliminationWithFunctionCalls() {
    String sourceCode = "function foo() {" +
        "var x = 1, y = 1;" +
        "x = y;" +
        "y = bar(1);" +
        "print(x);" +
        "}" +
        "function bar(a) {" +
        "var b = 2;" +
        "return 3;" +
        "}";

    Node root = AbstractCompiler.compile(compiler, sourceCode, "foo");

    eliminator.process(null, root);

    List<Node> expectedNodes = ImmutableList.of(
        new Node(Token.FUNCTION, null, null, null),
        new Node(Token.VAR, null, null, null),
        new Node(Token.VAR, Token.IDENTIFIER, null, null, null),
        new Node(Token.ASSIGN, Token.ASSIGN, null, null, null, null,
            new Node(Token.IDENTIFIER, Token.IDENTIFIER, null, null),
            new Node(Token.IDENTIFIER, Token.IDENTIFIER, null, null)),
        new Node(Token.VAR, Token.IDENTIFIER, null, null, null),
        new Node(Token.FUNCTION, null, null, null),
        new Node(Token.VAR, null, null, null),
        new Node(Token.VAR, Token.IDENTIFIER, null, null, null),
        new Node(Token.ASSIGN, Token.ASSIGN, null, null, null, null,
            new Node(Token.IDENTIFIER, Token.IDENTIFIER, null, null),
            new Node(Token.NUMBER, null, null, null, 3, null)),
        new Node(Token.RETURN, null, null, null),
        new Node(Token.FUNCTION, null, null, null),
        new Node(Token.VAR, null, null, null),
        new Node(Token.VAR, Token.IDENTIFIER, null, null, null),
        new Node(Token.ASSIGN, Token.ASSIGN, null, null, null, null,
            new Node(Token.IDENTIFIER, Token.IDENTIFIER, null, null),
            new Node(Token.NUMBER, null, null, null, 2, null)),
        new Node(Token.RETURN, Token.NUMBER, null, null, 3, null),
        new Node(Token.VAR, Token.IDENTIFIER, null, null, null),
        new Node(Token.ASSIGN, Token.ASSIGN, null, null, null, null,
            new Node(Token.IDENTIFIER, Token.IDENTIFIER, null, null),
            new Node(Token.NUMBER, null, null, null, 1, null)),
        new Node(Token.PRINT, null, null, null),
        new Node(Token.RETURN, null, null, null)
    );

    NodeTraversal nodeTraversal = new NodeTraversal(root, null);
    Node currentNode = root;
    int index = 0;

    while (currentNode != null) {
      if (NodeOperations.deepEquals(currentNode, expectedNodes.get(index))) {
        index++;
      }
      currentNode = currentNode.getNext();
    }

    Preconditions.checkState(index == expectedNodes.size(),
        "Unexpected nodes found in the CFG");
  }

  @Test
  public void testDeadAssignmentEliminationWithIncrementOperator() {
    String sourceCode = "function foo() {" +
        "var x = 1;" +
        "x++;" +
        "x++;" +
        "print(x);" +
        "}";

    Node root = AbstractCompiler.compile(compiler, sourceCode, "foo");

    eliminator.process(null, root);

    List<Node> expectedNodes = ImmutableList.of(
        new Node(Token.FUNCTION, null, null, null),
        new Node(Token.VAR, null, null, null),
        new Node(Token.VAR, Token.IDENTIFIER, null, null, null),
        new Node(Token.ASSIGN, Token.ASSIGN, null, null, null, null,
            new Node(Token.IDENTIFIER, Token.IDENTIFIER, null, null),
            new Node(Token.VOID, null, null, null)),
        new Node(Token.ASSIGN, Token.ASSIGN, null, null, null, null,
            new Node(Token.IDENTIFIER, Token.IDENTIFIER, null, null),
            new Node(Token.VOID, null, null, null)),
        new Node(Token.PRINT, null, null, null),
        new Node(Token.RETURN, null, null, null)
    );

    NodeTraversal nodeTraversal = new NodeTraversal(root, null);
    Node currentNode = root;
    int index = 0;

    while (currentNode != null) {
      if (NodeOperations.deepEquals(currentNode, expectedNodes.get(index))) {
        index++;
      }
      currentNode = currentNode.getNext();
    }

    Preconditions.checkState(index == expectedNodes.size(),
        "Unexpected nodes found in the CFG");
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentEliminationWithVariableAssignedInsideLoop() {
    String sourceCode = "function foo() {" +
        "var x = 1;" +
        "for (var i = 0; i < 10; i++) {" +
        "  x = i;" +
        "}" +
        "print(x);" +
        "}";

    Node root = AbstractCompiler.compile(compiler, sourceCode, "foo");

    eliminator.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentEliminationWithVariableAssignedInsideConditional() {
    String sourceCode = "function foo() {" +
        "var x = 1;" +
        "if (Math.random() < 0.5) {" +
        "  x = 2;" +
        "}" +
        "print(x);" +
        "}";

    Node root = AbstractCompiler.compile(compiler, sourceCode, "foo");

    eliminator.process(null, root);
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentEliminationWithVariableAssignedInsideSwitch() {
    String sourceCode = "function foo() {" +
        "var x = 1;" +
        "switch (Math.random()) {" +
        "  case 0.1: x = 2;" +
        "  case 0.2: x = 3;" +
        "}" +
        "print(x);" +
        "}";

    Node root = AbstractCompiler.compile(compiler, sourceCode, "foo");

    eliminator.process(null, root);
  }
}