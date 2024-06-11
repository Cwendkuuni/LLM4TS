package com.google.javascript.jscomp;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    String jsCode = "function foo() {" +
        "var x = 1, y = 1, z = 1;" +
        "if (x > 1) {" +
        "  x = 2;" +
        "  y = 3;" +
        "} else {" +
        "  x = 4;" +
        "  z = 5;" +
        "}" +
        "if (y > 1) {" +
        "  y = 6;" +
        "} else {" +
        "  z = 7;" +
        "}" +
        "return x + y + z;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler() {} ;
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    Node root = AbstractCompiler.parseAbstractSyntaxTree(jsCode);
    eliminator.process(null, root);

    ControlFlowGraph cfg = eliminator.getCompiler().getControlFlowGraph();
    LiveVariablesAnalysis liveness = eliminator.liveness;

    assertEquals(15, cfg.getDirectedGraphNodes().size());

    // Check that x = 2 and y = 3 are removed since x and y are dead after the assignments
    assertEquals(null, cfg.getNodeByValue(NodeUtil.parseExpression("x = 2")).getValue());
    assertEquals(null, cfg.getNodeByValue(NodeUtil.parseExpression("y = 3")).getValue());

    // Check that x = 4 and z = 5 are not removed
    Node xAssign = cfg.getNodeByValue(NodeUtil.parseExpression("x = 1"));
    Node xAssignAfterIf = cfg.getNodeByValue(NodeUtil.parseExpression("x = 4"));
    assertEquals(xAssignAfterIf, xAssign.getNext());

    Node zAssign = cfg.getNodeByValue(NodeUtil.parseExpression("z = 1"));
    Node zAssignInElse = cfg.getNodeByValue(NodeUtil.parseExpression("z = 5"));
    assertEquals(zAssignInElse, zAssign.getNext());

    // Check that y = 6 is not removed since y is live after the assignment
    Node yAssign = cfg.getNodeByValue(NodeUtil.parseExpression("if (y > 1)"));
    Node yAssignAfterIf = cfg.getNodeByValue(NodeUtil.parseExpression("y = 6"));
    assertEquals(yAssignAfterIf, yAssign.getNext());

    // Check that z = 7 is not removed since z is live after the assignment
    Node zAssignAfterIf = cfg.getNodeByValue(NodeUtil.parseExpression("z = 7"));
    Node zAssignInElse = cfg.getNodeByValue(NodeUtil.parseExpression("else"));
    assertEquals(zAssignInElse, zAssignAfterIf.getPrevious());
  }

  @Test
  public void testDeadAssignmentsEliminationWithFunction() {
    String jsCode = "function foo() {" +
        "function bar() {" +
        "  var x = 1, y = 1;" +
        "  if (x > 1) {" +
        "    x = 2;" +
        "  } else {" +
        "    y = 3;" +
        "  }" +
        "  return x + y;" +
        "}" +
        "return bar();" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler() {} ;
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    Node root = AbstractCompiler.parseAbstractSyntaxTree(jsCode);
    eliminator.process(null, root);

    ControlFlowGraph cfg = eliminator.getCompiler().getControlFlowGraph();
    LiveVariablesAnalysis liveness = eliminator.liveness;

    assertEquals(17, cfg.getDirectedGraphNodes().size());

    // Check that x = 2 is removed since x is dead after the assignment
    Node barFunction = cfg.getNodeByValue(NodeUtil.parseExpression("function bar"));
    Node xAssign = barFunction.getChild(0).getChild(1);
    assertEquals(null, xAssign.getValue());

    // Check that y = 3 is not removed since y is live after the assignment
    Node yAssign = barFunction.getChild(0).getChild(2);
    assertEquals(NodeUtil.parseExpression("y = 3"), yAssign.getValue());

    // Check that x = 2 is not removed since it is assigned again later
    Node xAssignInMainFunction = cfg.getNodeByValue(NodeUtil.parseExpression("return foo"));
    Node xAssignAfterIf = xAssignInMainFunction.getChild(1);
    assertEquals(xAssignAfterIf, xAssignInMainFunction.getChild(0).getNext());

    // Check that y = 3 is not removed since it is assigned again later
    Node yAssignInMainFunction = cfg.getNodeByValue(NodeUtil.parseExpression("return foo"));
    Node yAssign = yAssignInMainFunction.getChild(1);
    assertEquals(yAssign, yAssignInMainFunction.getChild(0).getNext());
  }

  @Test(expected = AssertionError.class)
  public void testDeadAssignmentsEliminationWithInfiniteLoop() {
    String jsCode = "function foo() {" +
        "for (var i = 0; i < 1000000; i++) {" +
        "  i = i + 1;" +
        "}" +
        "return i;" +
        "}";

    AbstractCompiler compiler = new AbstractCompiler() {} ;
    DeadAssignmentsElimination eliminator = new DeadAssignmentsElimination(compiler);

    Node root = AbstractCompiler.parseAbstractSyntaxTree(jsCode);
    eliminator.process(null, root);

    ControlFlowGraph cfg = eliminator.getCompiler().getControlFlowGraph();
    LiveVariablesAnalysis liveness = eliminator.liveness;

    // This test should fail since the loop never ends and the variable i is
    // not dead after the assignment.
  }
}