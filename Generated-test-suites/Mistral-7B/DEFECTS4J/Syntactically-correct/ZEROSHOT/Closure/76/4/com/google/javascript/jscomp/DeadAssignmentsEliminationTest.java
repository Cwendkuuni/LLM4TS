package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Graph.DiGraphNode;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    String sourceCode = "function foo() {"
        + "var x = 1, y = 1, z = 1;"
        + "x = y = z = 2;" // Dead assignment.
        + "print(x);"
        + "}";

    Compiler compiler = new Compiler(CompilationLevel.ADVANCED);
    AbstractCompiler abstractCompiler = compiler.getOptions().get(
        AbstractCompiler.class);
    abstractCompiler.addPass(DeadAssignmentsElimination.class);

    Node root = abstractCompiler.parseTree(sourceCode);
    abstractCompiler.process(root, null);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    nodeTraversal.traverse();

    DiGraphNode<Node, Branch> cfgNode = nodeTraversal.getControlFlowGraph()
        .getEntryNode();
    FlowState<DataFlowAnalysis.LiveVariableLattice> state = cfgNode
        .getAnnotation();

    Node xAssignment = null;
    for (Node n = cfgNode.getValue(); n != null; n = n.getNext()) {
      if (n.getType() == Token.ASSIGN && n.getFirstChild().getString()
          .equals("x")) {
        xAssignment = n;
        break;
      }
    }

    // The dead assignment x = y = z = 2; should be removed.
    // The assignment x = 1; should remain.
    assertTrue(xAssignment == null || xAssignment.getParent() == null);
  }

  @Test
  public void testDeadAssignmentsEliminationWithReturnStatement() {
    String sourceCode = "function foo() {"
        + "var x = 1, y = 1, z = 1;"
        + "x = y = z = 2;" // Dead assignment.
        + "return x;"
        + "}";

    Compiler compiler = new Compiler(CompilationLevel.ADVANCED);
    AbstractCompiler abstractCompiler = compiler.getOptions().get(
        AbstractCompiler.class);
    abstractCompiler.addPass(DeadAssignmentsElimination.class);

    Node root = abstractCompiler.parseTree(sourceCode);
    abstractCompiler.process(root, null);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    nodeTraversal.traverse();

    DiGraphNode<Node, Branch> cfgNode = nodeTraversal.getControlFlowGraph()
        .getEntryNode();
    FlowState<DataFlowAnalysis.LiveVariableLattice> state = cfgNode
        .getAnnotation();

    Node xAssignment = null;
    for (Node n = cfgNode.getValue(); n != null; n = n.getNext()) {
      if (n.getType() == Token.ASSIGN && n.getFirstChild().getString()
          .equals("x")) {
        xAssignment = n;
        break;
      }
    }

    // The dead assignment x = y = z = 2; should be removed.
    // The assignment x = 1; should remain.
    assertTrue(xAssignment == null || xAssignment.getParent() == null);
  }

  @Test
  public void testDeadAssignmentsEliminationWithFunctionDeclaration() {
    String sourceCode = "function foo() {"
        + "function bar() {"
        + "var x = 1, y = 1, z = 1;"
        + "x = y = z = 2;" // Dead assignment.
        + "}"
        + "return foo();"
        + "}";

    Compiler compiler = new Compiler(CompilationLevel.ADVANCED);
    AbstractCompiler abstractCompiler = compiler.getOptions().get(
        AbstractCompiler.class);
    abstractCompiler.addPass(DeadAssignmentsElimination.class);

    Node root = abstractCompiler.parseTree(sourceCode);
    abstractCompiler.process(root, null);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    nodeTraversal.traverse();

    DiGraphNode<Node, Branch> cfgNode = nodeTraversal.getControlFlowGraph()
        .getEntryNode();
    FlowState<DataFlowAnalysis.LiveVariableLattice> state = cfgNode
        .getAnnotation();

    Node barFunction = null;
    for (Node n = cfgNode.getValue(); n != null; n = n.getNext()) {
      if (n.getType() == Token.FUNCTION) {
        if ("bar".equals(n.getString())) {
          barFunction = n;
          break;
        }
      }
    }

    // The dead assignment x = y = z = 2; should be removed.
    // The function declaration "function bar() {...}" should remain.
    assertTrue(barFunction != null && barFunction.getChildrenCount() > 1);
  }

  @Test
  public void testDeadAssignmentsEliminationWithMultipleFunctionDeclarations() {
    String sourceCode = "function foo() {"
        + "function bar() {"
        + "var x = 1, y = 1, z = 1;"
        + "x = y = z = 2;" // Dead assignment.
        + "}"
        + "function baz() {"
        + "var x = 1, y = 1, z = 1;"
        + "x = y = z = 2;" // Dead assignment.
        + "}"
        + "return foo();"
        + "}";

    Compiler compiler = new Compiler(CompilationLevel.ADVANCED);
    AbstractCompiler abstractCompiler = compiler.getOptions().get(
        AbstractCompiler.class);
    abstractCompiler.addPass(DeadAssignmentsElimination.class);

    Node root = abstractCompiler.parseTree(sourceCode);
    abstractCompiler.process(root, null);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    nodeTraversal.traverse();

    DiGraphNode<Node, Branch> cfgNode = nodeTraversal.getControlFlowGraph()
        .getEntryNode();
    FlowState<DataFlowAnalysis.LiveVariableLattice> state = cfgNode
        .getAnnotation();

    Node barFunction = null;
    for (Node n = cfgNode.getValue(); n != null; n = n.getNext()) {
      if (n.getType() == Token.FUNCTION) {
        if ("bar".equals(n.getString())) {
          barFunction = n;
          break;
        }
      }
    }

    Node bazFunction = null;
    for (Node n = cfgNode.getValue(); n != null; n = n.getNext()) {
      if (n.getType() == Token.FUNCTION) {
        if ("baz".equals(n.getString())) {
          bazFunction = n;
          break;
        }
      }
    }

    // Each function declaration should remain.
    assertTrue(barFunction != null && barFunction.getChildrenCount() > 1);
    assertTrue(bazFunction != null && bazFunction.getChildrenCount() > 1);
  }

  @Test
  public void testDeadAssignmentsEliminationWithConditionalStatement() {
    String sourceCode = "function foo() {"
        + "var x = 1, y = 1, z = 1;"
        + "if (x > 1) {"
        + "  x = y = z = 2;" // Dead assignment.
        + "}"
        + "return x;"
        + "}";

    Compiler compiler = new Compiler(CompilationLevel.ADVANCED);
    AbstractCompiler abstractCompiler = compiler.getOptions().get(
        AbstractCompiler.class);
    abstractCompiler.addPass(DeadAssignmentsElimination.class);

    Node root = abstractCompiler.parseTree(sourceCode);
    abstractCompiler.process(root, null);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    nodeTraversal.traverse();

    DiGraphNode<Node, Branch> cfgNode = nodeTraversal.getControlFlowGraph()
        .getEntryNode();
    FlowState<DataFlowAnalysis.LiveVariableLattice> state = cfgNode
        .getAnnotation();

    Node xAssignment = null;
    for (Node n = cfgNode.getValue(); n != null; n = n.getNext()) {
      if (n.getType() == Token.ASSIGN && n.getFirstChild().getString()
          .equals("x")) {
        xAssignment = n;
        break;
      }
    }

    // The dead assignment x = y = z = 2; should be removed.
    // The assignment x = 1; should remain.
    assertTrue(xAssignment == null || xAssignment.getParent() == null);
  }

  @Test
  public void testDeadAssignmentsEliminationWithLoopStatement() {
    String sourceCode = "function foo() {"
        + "var x = 1, y = 1, z = 1;"
        + "for (var i = 0; i < 10; ++i) {"
        + "  x = y = z = 2;" // Dead assignment.
        + "}"
        + "return x;"
        + "}";

    Compiler compiler = new Compiler(CompilationLevel.ADVANCED);
    AbstractCompiler abstractCompiler = compiler.getOptions().get(
        AbstractCompiler.class);
    abstractCompiler.addPass(DeadAssignmentsElimination.class);

    Node root = abstractCompiler.parseTree(sourceCode);
    abstractCompiler.process(root, null);

    NodeTraversal nodeTraversal = new NodeTraversal(root);
    nodeTraversal.traverse();

    DiGraphNode<Node, Branch> cfgNode = nodeTraversal.getControlFlowGraph()
        .getEntryNode();
    FlowState<DataFlowAnalysis.LiveVariableLattice> state = cfgNode
        .getAnnotation();

    Node xAssignment = null;
    for (Node n = cfgNode.getValue(); n != null; n = n.getNext()) {
      if (n.getType() == Token.ASSIGN && n.getFirstChild().getString()
          .equals("x")) {
        xAssignment = n;
        break;
      }
    }

    // The dead assignment x = y = z = 2; should be removed.
    // The assignment x = 1; should remain.
    assertTrue(xAssignment == null || xAssignment.getParent() == null);
  }
}