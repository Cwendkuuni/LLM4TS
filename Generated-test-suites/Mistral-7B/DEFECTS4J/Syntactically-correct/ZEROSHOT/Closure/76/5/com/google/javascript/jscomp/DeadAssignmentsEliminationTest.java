package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractCompiler.CompilerPass;
import com.google.javascript.jscomp.AbstractNodeTraversal;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Graph.DiGraphNode;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    String source = "function test() {" +
        "var x = 1, y = 2, z = 3;" +
        "x = y = z = 4;" +
        "}";

    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = compiler.getOptions().getPassManager()
        .getPass(CompilerPass.DEAD_ASSIGNMENTS_ELIMINATION);
    Preconditions.checkNotNull(abstractCompiler, "Compiler pass not found");

    Node root = NodeUtil.parse(source);
    AbstractNodeTraversal traversal = new NodeTraversal(root, compiler);

    ((DeadAssignmentsElimination) abstractCompiler).process(null, root);

    assertTrue(NodeUtil.isNull(root.getFirstChild()));
    Var x = traversal.getScope().getVar("x");
    Var y = traversal.getScope().getVar("y");
    Var z = traversal.getScope().getVar("z");
    assertTrue(x.value.equals(y.value));
    assertTrue(x.value.equals(z.value));
    assertTrue(x.value.equals(NodeUtil.newNumber(4).getValue()));
  }

  @Test
  public void testDeadAssignmentsEliminationWithFunction() {
    String source = "function test(x) {" +
        "var y = 1, z = 2, x = 3;" +
        "y = x = z = 4;" +
        "return x;" +
        "}";

    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = compiler.getOptions().getPassManager()
        .getPass(CompilerPass.DEAD_ASSIGNMENTS_ELIMINATION);
    Preconditions.checkNotNull(abstractCompiler, "Compiler pass not found");

    Node root = NodeUtil.parse(source);
    AbstractNodeTraversal traversal = new NodeTraversal(root, compiler);

    ((DeadAssignmentsElimination) abstractCompiler).process(null, root);

    Var x = traversal.getScope().getVar("x");
    Var y = traversal.getScope().getVar("y");
    Var z = traversal.getScope().getVar("z");
    assertTrue(x.value.equals(NodeUtil.newNumber(3).getValue()));
    assertTrue(y.value.equals(NodeUtil.newNumber(4).getValue()));
    assertTrue(z.value.equals(NodeUtil.newNumber(4).getValue()));
  }

  @Test
  public void testDeadAssignmentsEliminationWithIf() {
    String source = "function test() {" +
        "var x = 1, y = 2;" +
        "if (x = 3) {" +
        "  x = 4;" +
        "}" +
        "}" +
        "function main() {" +
        "test();" +
        "}" +
        "main();";

    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = compiler.getOptions().getPassManager()
        .getPass(CompilerPass.DEAD_ASSIGNMENTS_ELIMINATION);
    Preconditions.checkNotNull(abstractCompiler, "Compiler pass not found");

    Node root = NodeUtil.parse(source);
    AbstractNodeTraversal traversal = new NodeTraversal(root, compiler);

    ((DeadAssignmentsElimination) abstractCompiler).process(null, root);

    Var x = traversal.getScope().getVar("x");
    assertTrue(x.value.equals(NodeUtil.newNumber(3).getValue()));
  }

  @Test
  public void testDeadAssignmentsEliminationWithConditionalExpression() {
    String source = "function test() {" +
        "var x = 1, y = 2;" +
        "x = (y = 3)?" +
        "      x = 4;" +
        "  : x = 5;" +
        "}" +
        "function main() {" +
        "test();" +
        "}" +
        "main();";

    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = compiler.getOptions().getPassManager()
        .getPass(CompilerPass.DEAD_ASSIGNMENTS_ELIMINATION);
    Preconditions.checkNotNull(abstractCompiler, "Compiler pass not found");

    Node root = NodeUtil.parse(source);
    AbstractNodeTraversal traversal = new NodeTraversal(root, compiler);

    ((DeadAssignmentsElimination) abstractCompiler).process(null, root);

    Var x = traversal.getScope().getVar("x");
    assertTrue(x.value.equals(NodeUtil.newNumber(3).getValue()));
  }

  @Test
  public void testDeadAssignmentsEliminationWithWhile() {
    String source = "function test() {" +
        "var x = 1, y = 2;" +
        "while (x = 3) {" +
        "  x = 4;" +
        "}" +
        "}" +
        "function main() {" +
        "test();" +
        "}" +
        "main();";

    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = compiler.getOptions().getPassManager()
        .getPass(CompilerPass.DEAD_ASSIGNMENTS_ELIMINATION);
    Preconditions.checkNotNull(abstractCompiler, "Compiler pass not found");

    Node root = NodeUtil.parse(source);
    AbstractNodeTraversal traversal = new NodeTraversal(root, compiler);

    ((DeadAssignmentsElimination) abstractCompiler).process(null, root);

    Var x = traversal.getScope().getVar("x");
    assertTrue(x.value.equals(NodeUtil.newNumber(-1).getValue())); // Loop never ends
  }

  @Test
  public void testDeadAssignmentsEliminationWithFor() {
    String source = "function test() {" +
        "for (var x = 1, y = 2; x = 3; x++) {" +
        "  x = 4;" +
        "}" +
        "}" +
        "function main() {" +
        "test();" +
        "}" +
        "main();";

    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = compiler.getOptions().getPassManager()
        .getPass(CompilerPass.DEAD_ASSIGNMENTS_ELIMINATION);
    Preconditions.checkNotNull(abstractCompiler, "Compiler pass not found");

    Node root = NodeUtil.parse(source);
    AbstractNodeTraversal traversal = new NodeTraversal(root, compiler);

    ((DeadAssignmentsElimination) abstractCompiler).process(null, root);

    Var x = traversal.getScope().getVar("x");
    assertTrue(x.value.equals(NodeUtil.newNumber(-1).getValue())); // Loop never ends
  }

  @Test
  public void testDeadAssignmentsEliminationWithFunctionAndConditional() {
    String source = "function test(x) {" +
        "if (x = 3) {" +
        "  x = 4;" +
        "}" +
        "}" +
        "function main() {" +
        "test(1);" +
        "}" +
        "main();";

    Compiler compiler = new Compiler();
    AbstractCompiler abstractCompiler = compiler.getOptions().getPassManager()
        .getPass(CompilerPass.DEAD_ASSIGNMENTS_ELIMINATION);
    Preconditions.checkNotNull(abstractCompiler, "Compiler pass not found");

    Node root = NodeUtil.parse(source);
    AbstractNodeTraversal traversal = new NodeTraversal(root, compiler);

    ((DeadAssignmentsElimination) abstractCompiler).process(null, root);

    Var x = traversal.getScope().getVar("x");
    assertTrue(x.value.equals(NodeUtil.newNumber(3).getValue()));
  }
}