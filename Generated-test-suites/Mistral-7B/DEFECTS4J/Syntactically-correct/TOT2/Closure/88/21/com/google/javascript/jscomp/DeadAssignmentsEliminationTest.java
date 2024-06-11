package com.google.javascript.jscomp.test;

import static com.google.javascript.rhino.NodeOp.*;
import static com.google.javascript.rhino.Token.*;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.GraphTestHelper;
import com.google.javascript.jscomp. JaneTestsHelper;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

import java.util.List;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    String sourceCode =
        "function foo() {" +
            "var a = 1, b = 2, c = 3;" +
            "if (true) {" +
                "var a = 4;" +
                "a = 5;" +
                "}" +
            "if (false) {" +
                "var b = 6;" +
                "}" +
            "c = a + b;" +
            "return c;" +
        "}";

    AbstractCompiler compiler = JaneTestsHelper.createCompiler();
    CompilerOptions options = new CompilerOptions();

    Node ast = NodeFactory.createProgram(sourceCode, null, null);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    List<CompilerPass> passes = ImmutableList.of(deadAssignmentsElimination);
    NodeTraversal traverser = new NodeTraversal(ast, passes, null);

    NodeTraversal t = new NodeTraversal(ast, null, null);
    Scope scope = t.getScope();

    GraphTestHelper.runTest(
        compiler, passes, ast, "DeadAssignmentsEliminationTest.js",
        "expected_output.js");

    assertEquals(
        1, scope.getVarCount() - livenessScopeVarCount(ast));

    assertEquals(1, scope.getVar("a").getAssignCount());
    assertEquals(1, scope.getVar("b").getAssignCount());
    assertEquals(1, scope.getVar("c").getAssignCount());

    Node actualOutput = GraphTestHelper.getCompiledCode(compiler);
    assertEquals(
        1,
        actualOutput.getChildCount() -
            livenessScopeNodeCount(actualOutput));

    assertEquals("function foo() {return 5 + 2;}", actualOutput.getString());
  }

  @Test
  public void testDeadAssignmentsEliminationWithScope() {
    String sourceCode =
        "function foo(x) {" +
            "var a = 1, b = 2;" +
            "if (x > 0) {" +
                "var a = 4;" +
                "a = 5;" +
            "}" +
            "if (x < 0) {" +
                "var b = 6;" +
                "}" +
            "var c = a + b;" +
            "return c;" +
        "}";

    AbstractCompiler compiler = JaneTestsHelper.createCompiler();
    CompilerOptions options = new CompilerOptions();

    Node ast = NodeFactory.createProgram(sourceCode, null, null);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    List<CompilerPass> passes = ImmutableList.of(deadAssignmentsElimination);
    NodeTraversal traverser = new NodeTraversal(ast, passes, null);

    NodeTraversal t = new NodeTraversal(ast, null, null);
    Scope scope = t.getScope();

    // The variable 'a' should be eliminated because it is assigned and dead after the assignment.
    assertEquals(
        1, scope.getVarCount() - livenessScopeVarCount(ast, "a"));

    // The variable 'b' should not be eliminated because it is not assigned and dead after the assignment.
    assertEquals(2, scope.getVarCount() - livenessScopeVarCount(ast, "b"));

    // The assignment 'a = 5' should be eliminated.
    assertEquals(
        1,
        ast.getChildCount() -
            livenessScopeNodeCount(ast) -
            nodeCount(ast, matchRemovableAssigns, Predicates.<Node>alwaysTrue()));

    // The assignment 'b = 6' should not be eliminated.
    assertEquals(
        1,
        ast.getChildCount() -
            livenessScopeNodeCount(ast) -
            nodeCount(ast, matchRemovableAssigns, Predicates.<Node>not(
                new Predicate<Node>() {
                  @Override
                  public boolean apply(Node n) {
                    return n.getFirstChild().getString().equals("a");
                  }
                })));

    GraphTestHelper.runTest(
        compiler, passes, ast, "DeadAssignmentsEliminationTest.js",
        "expected_output.js");

    assertEquals(
        1,
        GraphTestHelper.getCompiledCode(compiler).getChildCount() -
            livenessScopeNodeCount(
                GraphTestHelper.getCompiledCode(compiler)));

    assertEquals("function foo(x) {return 5 + 2;}",
        GraphTestHelper.getCompiledCode(compiler).getString());
  }

  @Test
  public void testDeadAssignmentsEliminationWithException() {
    String sourceCode =
        "function foo() {" +
            "try {" +
                "var a = 1;" +
                "throw new Error('Exception');" +
                "a = 2;" +
            "} catch (e) {" +
                "// do nothing" +
            "}" +
            "return a;" +
        "}";

    AbstractCompiler compiler = JaneTestsHelper.createCompiler();
    CompilerOptions options = new CompilerOptions();

    Node ast = NodeFactory.createProgram(sourceCode, null, null);
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);

    List<CompilerPass> passes = ImmutableList.of(deadAssignmentsElimination);
    NodeTraversal traverser = new NodeTraversal(ast, passes, null);

    NodeTraversal t = new NodeTraversal(ast, null, null);
    Scope scope = t.getScope();

    // The variable 'a' should be eliminated because it is assigned and dead after the assignment.
    assertEquals(
        1, scope.getVarCount() - livenessScopeVarCount(ast, "a"));

    // The assignment 'a = 2' should be eliminated.
    assertEquals(
        1,
        ast.getChildCount() -
            livenessScopeNodeCount(ast) -
            nodeCount(ast, matchRemovableAssigns, Predicates.<Node>alwaysTrue()));

    GraphTestHelper.runTest(
        compiler, passes, ast, "DeadAssignmentsEliminationTest.js",
        "expected_output.js");

    assertEquals("function foo() {return undefined;}",
        GraphTestHelper.getCompiledCode(compiler).getString());
  }

  private static int livenessScopeVarCount(Node ast) {
    return NodeTraversal.traverse(
        new AbstractPostOrderCallback() {
          int count = 0;

          @Override
          public void process(Node n, Node parent) {
            if (n.getType() == TOKEN || n.getType() == NAME) {
              count += NodeUtil.isName(n) ? 1 : 0;
            }
          }

          @Override
          public void visit(NodeTraversal t, Node n, Node parent) {
            process(n, parent);
          }
        }, ast, null).size();
  }

  private static int livenessScopeVarCount(Node ast, String variableName) {
    return NodeTraversal.traverse(
        new AbstractPostOrderCallback() {
          int count = 0;

          @Override
          public void process(Node n, Node parent) {
            if (n.getType() == TOKEN || n.getType() == NAME) {
              if (NodeUtil.isName(n) && n.getString().equals(variableName)) {
                count += 1;
              }
            }
          }

          @Override
          public void visit(NodeTraversal t, Node n, Node parent) {
            process(n, parent);
          }
        }, ast, null).size();
  }

  private static int livenessScopeNodeCount(Node ast) {
    return NodeTraversal.traverse(
        new AbstractPostOrderCallback() {
          int count = 0;

          @Override
          public void process(Node n, Node parent) {
            if (NodeUtil.isStatement(n)) {
              count += 1;
            }
          }

          @Override
          public void visit(NodeTraversal t, Node n, Node parent) {
            process(n, parent);
          }
        }, ast, null).size();
  }

  private static int livenessScopeNodeCount(Node ast, Predicate<Node> p) {
    return NodeTraversal.traverse(
        new AbstractPostOrderCallback() {
          int count = 0;

          @Override
          public void process(Node n, Node parent) {
            if (NodeUtil.isStatement(n) && p.apply(n)) {
              count += 1;
            }
          }

          @Override
          public void visit(NodeTraversal t, Node n, Node parent) {
            process(n, parent);
          }
        }, ast, null).size();
  }

  private static int nodeCount(Node ast, Predicate<Node> p, Predicate<Node> q) {
    return NodeTraversal.traverse(
        new AbstractPostOrderCallback() {
          int count = 0;

          @Override
          public void process(Node n, Node parent) {
            if (NodeUtil.isStatement(n) && p.apply(n) && q.apply(n)) {
              count += 1;
            }
          }

          @Override
          public void visit(NodeTraversal t, Node n, Node parent) {
            process(n, parent);
          }
        }, ast, null).size();
  }
}