package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Before
  public void setUp() {
    compiler = new Compiler();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testDeadAssignmentElimination() {
    String source = "function foo() {\n" +
        "  var x = 1;\n" +
        "  x = 2;\n" +
        "  return x;\n" +
        "}";

    Node root = Parser.parse(source, null, null);
    deadAssignmentsElimination.process(null, root);

    Node result = NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void process(NodeTraversal t, Node n, Node parent) {
        if (n != null && NodeUtil.isReturnStatement(n)) {
          assertEquals(1, n.getFirstChild().getNumber().getIntValueExact());
        }
      }
    }).getValue();

    assertEquals("1", result.toString());
  }

  @Test
  public void testDeadAssignmentEliminationWithVariableDeclaredButNotUsed() {
    String source = "function foo() {\n" +
        "  var x;\n" +
        "  x = 1;\n" +
        "  x = 2;\n" +
        "  return x;\n" +
        "}";

    Node root = Parser.parse(source, null, null);
    deadAssignmentsElimination.process(null, root);

    Node result = NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void process(NodeTraversal t, Node n, Node parent) {
        if (n != null && NodeUtil.isReturnStatement(n)) {
          fail("Variable x should have been eliminated.");
        }
      }
    }).getValue();

    assertTrue("Variable x should have been eliminated.", result == null);
  }

  @Test
  public void testDeadAssignmentEliminationWithVariableDeclaredAndUsed() {
    String source = "function foo() {\n" +
        "  var x;\n" +
        "  x = 1;\n" +
        "  return x + 1;\n" +
        "}";

    Node root = Parser.parse(source, null, null);
    deadAssignmentsElimination.process(null, root);

    Node result = NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void process(NodeTraversal t, Node n, Node parent) {
        if (n != null && NodeUtil.isReturnStatement(n)) {
          assertEquals(2, n.getFirstChild().getNumber().getIntValueExact());
        }
      }
    }).getValue();

    assertEquals("2", result.toString());
  }

  @Test
  public void testDeadAssignmentEliminationWithDeadVariableInCondition() {
    String source = "function foo() {\n" +
        "  var x = 1;\n" +
        "  if (x = 2) {\n" +
        "    return true;\n" +
        "  }\n" +
        "  return false;\n" +
        "}";

    Node root = Parser.parse(source, null, null);
    deadAssignmentsElimination.process(null, root);

    Node result = NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void process(NodeTraversal t, Node n, Node parent) {
        if (n != null && NodeUtil.isReturnStatement(n)) {
          assertTrue("Condition should be evaluated to false.", n.getBoolean());
        }
      }
    }).getValue();

    assertTrue("Condition should be evaluated to false.", result.getBoolean());
  }

  @Test
  public void testDeadAssignmentEliminationWithDeadVariableInAssignment() {
    String source = "function foo() {\n" +
        "  var x = 1;\n" +
        "  var y = x;\n" +
        "  x = 2;\n" +
        "  y = 3;\n" +
        "  return y;\n" +
        "}";

    Node root = Parser.parse(source, null, null);
    deadAssignmentsElimination.process(null, root);

    Node result = NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void process(NodeTraversal t, Node n, Node parent) {
        if (n != null && NodeUtil.isReturnStatement(n)) {
          assertEquals(3, n.getFirstChild().getNumber().getIntValueExact());
        }
      }
    }).getValue();

    assertEquals("3", result.toString());
  }

  @Test
  public void testDeadAssignmentEliminationWithDeadVariableInLoop() {
    String source = "function foo() {\n" +
        "  var i = 0;\n" +
        "  while (i = 5) {\n" +
        "    // Some code here\n" +
        "    i++;\n" +
        "  }\n" +
        "  return i;\n" +
        "}";

    Node root = Parser.parse(source, null, null);
    deadAssignmentsElimination.process(null, root);

    Node result = NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void process(NodeTraversal t, Node n, Node parent) {
        if (n != null && NodeUtil.isReturnStatement(n)) {
          assertEquals(1, n.getFirstChild().getNumber().getIntValueExact());
        }
      }
    }).getValue();

    assertEquals("1", result.toString());
  }

  @Test
  public void testDeadAssignmentEliminationWithDeadVariableInFunctionCall() {
    String source = "function foo() {\n" +
        "  var x = 1;\n" +
        "  return bar(x);\n" +
        "}";

    String barSource = "function bar(x) {\n" +
        "  x = 2;\n" +
        "  // Some code here\n" +
        "  return x;\n" +
        "}";

    Node root = Parser.parse(source, null, null);
    Node barNode = Parser.parse(barSource, null, root).getValue();

    deadAssignmentsElimination.process(null, root);

    Node result = NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void process(NodeTraversal t, Node n, Node parent) {
        if (n != null && NodeUtil.isReturnStatement(n)) {
          assertEquals(1, n.getFirstChild().getNumber().getIntValueExact());
        }
      }
    }).getValue();

    assertEquals("1", result.toString());
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentEliminationWithDeadVariableInNestedFunctionCall() {
    String source = "function foo() {\n" +
        "  function bar() {\n" +
        "    var x = 1;\n" +
        "    x = 2;\n" +
        "    return x;\n" +
        "  }\n" +
        "  return bar();\n" +
        "}";

    Node root = Parser.parse(source, null, null);

    deadAssignmentsElimination.process(null, root);

    Node result = NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void process(NodeTraversal t, Node n, Node parent) {
        if (n != null && NodeUtil.isReturnStatement(n)) {
          fail("Variable x should not be accessible from the outer function.");
        }
      }
    }).getValue();

    assertTrue("Variable x should not be accessible from the outer function.", result == null);
  }

  @Test
  public void testDeadAssignmentEliminationWithDeadVariableInSwitchCase() {
    String source = "function foo() {\n" +
        "  var x = 1;\n" +
        "  switch (x) {\n" +
        "    case 1:\n" +
        "      x = 2;\n" +
        "      break;\n" +
        "    case 2:\n" +
        "      return true;\n" +
        "    default:\n" +
        "      return false;\n" +
        "  }\n" +
        "}";

    Node root = Parser.parse(source, null, null);
    deadAssignmentsElimination.process(null, root);

    Node result = NodeTraversal.traverse(compiler, root, new AbstractPostOrderCallback() {
      @Override
      public void process(NodeTraversal t, Node n, Node parent) {
        if (n != null && NodeUtil.isReturnStatement(n)) {
          assertTrue("Condition should be evaluated to 2.", n.getNumber().getIntValueExact() == 2);
        }
      }
    }).getValue();

    assertEquals("true", result.toString());
  }
}