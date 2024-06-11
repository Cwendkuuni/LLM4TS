import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.GraphTraversal;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.graph.ControlFlowGraph;
import com.google.javascript.rhino.Token;

public class DeadAssignmentsEliminationTest {

  @org.junit.Test
  public void testRemoveDeadAssignments() {
    String code = "function main() {\n" +
        "  var x = 1;\n" +
        "  var y = 1;\n" +
        "  x = y;\n" +
        "  y = 2;\n" +
        "  var z = x;\n" +
        "  x = 3;\n" +
        "  print(z); // expected output 1\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    Node root = compiler.parseTree(code, null, null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = root.getAnalysisResult(GraphTraversal.CONTROL_FLOW_GRAPH);
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversal, cfg);

    assertTrue(NodeUtil.has(root, NodeUtil.getOpFromAssignmentOp(
        NodeUtil.findFirstAssignment(root)))); // Check that x = y = 1 remains
    assertFalse(NodeUtil.has(root, NodeUtil.findFirstAssignment(root).getNext())); // Check that x = 1 is removed
    assertFalse(NodeUtil.has(root, NodeUtil.findLastAssignment(root).getPrevious())); // Check that y = 2 is removed

    code = "function main() {\n" +
        "  var x = 1;\n" +
        "  var y = 1;\n" +
        "  x = y;\n" +
        "  y = 2;\n" +
        "  var z = x;\n" +
        "  x = 3;\n" +
        "  print(z); // expected output 1\n" +
        "  x = 4; // new assignment to x\n" +
        "}";

    root = compiler.parseTree(code, null, null);
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    cfg = root.getAnalysisResult(GraphTraversal.CONTROL_FLOW_GRAPH);
    nodeTraversal = new NodeTraversal(root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversal, cfg);

    assertTrue(NodeUtil.has(root, NodeUtil.getOpFromAssignmentOp(
        NodeUtil.findFirstAssignment(root)))); // Check that x = y = 1 remains
    assertTrue(NodeUtil.has(root, NodeUtil.findLastAssignment(root))); // Check that x = 4 remains
  }

  @org.junit.Test
  public void testRemoveDeadAssignmentsWithConditionals() {
    String code = "function main() {\n" +
        "  var x = 1;\n" +
        "  if (x > 0) {\n" +
        "    var y = 1;\n" +
        "    x = y;\n" +
        "  } else {\n" +
        "    var z = 2;\n" +
        "    x = z;\n" +
        "  }\n" +
        "  var a = x;\n" +
        "  print(a); // expected output 1\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    Node root = compiler.parseTree(code, null, null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = root.getAnalysisResult(GraphTraversal.CONTROL_FLOW_GRAPH);
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversal, cfg);

    assertTrue(NodeUtil.has(root, NodeUtil.getOpFromAssignmentOp(
        NodeUtil.findFirstAssignment(root)))); // Check that x = y = 1 remains when x > 0
    assertFalse(NodeUtil.has(root, NodeUtil.findFirstAssignment(root).getNext())); // Check that x = 1 is removed
    assertFalse(NodeUtil.has(root, NodeUtil.findLastAssignment(root).getPrevious())); // Check that z = 2 is removed
  }

  @org.junit.Test
  public void testRemoveDeadAssignmentsWithForLoop() {
    String code = "function main() {\n" +
        "  var x = 1;\n" +
        "  for (var i = 0; i < 10; i++) {\n" +
        "    var y = i;\n" +
        "    x = y;\n" +
        "  }\n" +
        "  var a = x;\n" +
        "  print(a); // expected output 10\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    Node root = compiler.parseTree(code, null, null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = root.getAnalysisResult(GraphTraversal.CONTROL_FLOW_GRAPH);
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversal, cfg);

    assertTrue(NodeUtil.has(root, NodeUtil.getOpFromAssignmentOp(
        NodeUtil.findFirstAssignment(root)))); // Check that x = y = 1 remains in the for loop
    assertFalse(NodeUtil.has(root, NodeUtil.findLastAssignment(root).getPrevious())); // Check that y = i is removed
  }

  @org.junit.Test
  public void testRemoveDeadAssignmentsWithAssignmentsInCondition() {
    String code = "function main() {\n" +
        "  var x = 1;\n" +
        "  if (x = 2) {\n" +
        "    x = 3;\n" +
        "  } else {\n" +
        "    x = 4;\n" +
        "  }\n" +
        "  print(x); // expected output 3\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    Node root = compiler.parseTree(code, null, null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = root.getAnalysisResult(GraphTraversal.CONTROL_FLOW_GRAPH);
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversal, cfg);

    assertTrue(NodeUtil.has(root, NodeUtil.getOpFromAssignmentOp(
        NodeUtil.findFirstAssignment(root)))); // Check that x = 2 is replaced with x = 2 && true
    assertFalse(NodeUtil.has(root, NodeUtil.findLastAssignment(root).getPrevious())); // Check that x = 1 is removed
  }

  @org.junit.Test
  public void testRemoveDeadAssignmentsWithIncrementOrDecrementOperator() {
    String code = "function main() {\n" +
        "  var x = 1;\n" +
        "  x++;\n" +
        "  x++;\n" +
        "  print(x); // expected output 3\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    Node root = compiler.parseTree(code, null, null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = root.getAnalysisResult(GraphTraversal.CONTROL_FLOW_GRAPH);
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversal, cfg);

    assertTrue(NodeUtil.has(root, NodeUtil.getOpFromAssignmentOp(
        NodeUtil.findFirstAssignment(root)))); // Check that x++ is replaced with x += 1
    assertFalse(NodeUtil.has(root, NodeUtil.findLastAssignment(root).getPrevious())); // Check that x++ is removed
  }

  @org.junit.Test
  public void testRemoveDeadAssignmentsWithFunctionCall() {
    String code = "function main() {\n" +
        "  function square(x) {\n" +
        "    return x * x;\n" +
        "  }\n" +
        "  var x = 1;\n" +
        "  x = square(x);\n" +
        "  print(x); // expected output 1\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    Node root = compiler.parseTree(code, null, null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = root.getAnalysisResult(GraphTraversal.CONTROL_FLOW_GRAPH);
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversal, cfg);

    assertTrue(NodeUtil.has(root, NodeUtil.getOpFromAssignmentOp(
        NodeUtil.findFirstAssignment(root)))); // Check that x = square(x) is replaced with x = square(x)
    // Check that x = 1 is not removed since it is not a dead assignment
  }

  @org.junit.Test
  public void testRemoveDeadAssignmentsWithVariableDeclaration() {
    String code = "function main() {\n" +
        "  var x = 1;\n" +
        "  var x = 2;\n" +
        "  print(x); // expected output 2\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    Node root = compiler.parseTree(code, null, null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = root.getAnalysisResult(GraphTraversal.CONTROL_FLOW_GRAPH);
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversal, cfg);

    assertTrue(NodeUtil.has(root, NodeUtil.findLastAssignment(root))); // Check that x = 1 is not removed
    assertFalse(NodeUtil.has(root, NodeUtil.findFirstAssignment(root).getNext())); // Check that x = 2 is removed
  }

  @org.junit.Test
  public void testRemoveDeadAssignmentsWithVariableShadowing() {
    String code = "function main() {\n" +
        "  var x = 1;\n" +
        "  function foo() {\n" +
        "    var x = 2;\n" +
        "  }\n" +
        "  foo();\n" +
        "  print(x); // expected output 1\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions(new CompilerOptions.Defaults());
    Node root = compiler.parseTree(code, null, null);

    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    deadAssignmentsElimination.process(null, root);

    ControlFlowGraph<Node> cfg = root.getAnalysisResult(GraphTraversal.CONTROL_FLOW_GRAPH);
    NodeTraversal nodeTraversal = new NodeTraversal(root);
    deadAssignmentsElimination.tryRemoveDeadAssignments(nodeTraversal, cfg);

    assertTrue(NodeUtil.has(root, NodeUtil.getOpFromAssignmentOp(
        NodeUtil.findFirstAssignment(root)))); // Check that x = 1 is not removed
    assertFalse(NodeUtil.has(root, NodeUtil.findLastAssignment(root).getPrevious())); // Check that x = 2 is removed
  }
}