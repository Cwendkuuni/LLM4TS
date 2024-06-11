import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.has;
import static com.google.javascript.jscomp.NodeUtil.isAssign;
import static com.google.javascript.jscomp.NodeUtil.isFunction;
import static com.google.javascript.jscomp.NodeUtil.isName;
import static com.google.javascript.jscomp.NodeUtil.isOp;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.LiveVariablesAnalysis.LiveVariableLattice;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

@RunWith(JUnit4.class)
public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private DeadAssignmentsElimination deadAssignmentsElimination;

  @Test
  public void testDeadAssignmentsElimination() {
    CompilerOptions options = new CompilerOptions();
    compiler = new AbstractCompiler() {};
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node root = NodeFactory.createProgram();
    DeadAssignmentsElimination.process(root, compiler);

    // No dead assignments to test.
    assertSameNodes(root, getTestProgram1());

    // Dead assignment test 1: dead_x = dead_y = 1;
    root = NodeFactory.createProgram();
    Node n1 = NodeFactory.createVariable("x");
    Node n2 = NodeFactory.createVariable("y");
    Node assign1 = NodeFactory.createAssign(ASSIGN, n1, NodeFactory.createNumber(1));
    Node assign2 = NodeFactory.createAssign(ASSIGN, n2, NodeFactory.createNumber(1));
    Node assign3 = NodeFactory.createAssign(ASSIGN, n1, n2);
    root.setBody(NodeFactory.createBlock(Arrays.asList(assign1, assign2, assign3)));
    DeadAssignmentsElimination.process(root, compiler);
    assertSameNodes(root, getTestProgram2());

    // Dead assignment test 2: dead_x = dead_y = 1; and print(x)
    root = NodeFactory.createProgram();
    Node printStmt = NodeFactory.createCall(PRINT, NodeFactory.createName("print"),
        NodeFactory.createName("x"));
    root.setBody(NodeFactory.createBlock(
        Arrays.asList(assign1, assign2, assign3, printStmt)));
    DeadAssignmentsElimination.process(root, compiler);
    assertSameNodes(root, getTestProgram3());

    // Dead assignment test 3: dead_x = dead_y = 1; and x = 2
    root = NodeFactory.createProgram();
    Node assign4 = NodeFactory.createAssign(ASSIGN, n1, NodeFactory.createNumber(2));
    root.setBody(NodeFactory.createBlock(
        Arrays.asList(assign1, assign2, assign3, assign4)));
    DeadAssignmentsElimination.process(root, compiler);
    assertSameNodes(root, getTestProgram4());

    // Dead assignment test 4: if (X = a && a = C) {..}
    root = NodeFactory.createProgram();
    Node ifStmt = NodeFactory.createIfStatement(
        NodeFactory.createBinaryOp(AND, NodeFactory.createName("X"), assign1),
        NodeFactory.createBlock(NodeFactory.createEmpty()),
        null);
    root.setBody(NodeFactory.createBlock(ifStmt));
    DeadAssignmentsElimination.process(root, compiler);
    assertSameNodes(root, getTestProgram5());

    // Dead assignment test 5: if (a = C && X = a) {..}
    root = NodeFactory.createProgram();
    Node ifStmt2 = NodeFactory.createIfStatement(
        NodeFactory.createBinaryOp(AND, assign1, NodeFactory.createAssign(ASSIGN, n1, n2)),
        NodeFactory.createBlock(NodeFactory.createEmpty()),
        null);
    root.setBody(NodeFactory.createBlock(ifStmt2));
    DeadAssignmentsElimination.process(root, compiler);
    assertSameNodes(root, getTestProgram6());

    // Dead assignment test 6: y = 1, x = 1, x = 2, print(x)
    root = NodeFactory.createProgram();
    Node varX = NodeFactory.createVariable("x");
    Node varY = NodeFactory.createVariable("y");
    root.setBody(NodeFactory.createBlock(
        Arrays.asList(
            NodeFactory.createAssign(ASSIGN, varY, NodeFactory.createNumber(1)),
            NodeFactory.createAssign(ASSIGN, varX, NodeFactory.createNumber(1)),
            NodeFactory.createAssign(ASSIGN, varX, NodeFactory.createNumber(2)),
            printStmt
        )));
    DeadAssignmentsElimination.process(root, compiler);
    assertSameNodes(root, getTestProgram7());

    // Dead assignment test 7: y = 1, x = 1, x = 2, x = 3, print(x)
    root = NodeFactory.createProgram();
    Node assign5 = NodeFactory.createAssign(ASSIGN, varX, NodeFactory.createNumber(3));
    root.setBody(NodeFactory.createBlock(
        Arrays.asList(
            NodeFactory.createAssign(ASSIGN, varY, NodeFactory.createNumber(1)),
            NodeFactory.createAssign(ASSIGN, varX, NodeFactory.createNumber(1)),
            NodeFactory.createAssign(ASSIGN, varX, NodeFactory.createNumber(2)),
            assign5, printStmt
        )));
    DeadAssignmentsElimination.process(root, compiler);
    assertSameNodes(root, getTestProgram8());

    // Dead assignment test 8: dead_x = dead_y = 1, print(x)
    root = NodeFactory.createProgram();
    Node printStmt2 = NodeFactory.createCall(PRINT, NodeFactory.createName("print"),
        NodeFactory.createName("x"));
    root.setBody(NodeFactory.createBlock(
        Arrays.asList(assign1, assign2, assign3, printStmt2)));
    DeadAssignmentsElimination.process(root, compiler);
    assertSameNodes(root, getTestProgram9());

    // Dead assignment test 9: dead_x = dead_y = 1, x = 2, print(x)
    root = NodeFactory.createProgram();
    root.setBody(NodeFactory.createBlock(
        Arrays.asList(assign1, assign2, assign3, assign4, printStmt2)));
    DeadAssignmentsElimination.process(root, compiler);
    assertSameNodes(root, getTestProgram10());

    // Dead assignment test 10: dead_x = dead_y = 1, x = 2, x = 3, print(x)
    root = NodeFactory.createProgram();
    root.setBody(NodeFactory.createBlock(
        Arrays.asList(assign1, assign2, assign3, assign4, assign5, printStmt2)));
    DeadAssignmentsElimination.process(root, compiler);
    assertSameNodes(root, getTestProgram11());

    // Dead assignment test 11: if (X = a && a = C) {..}, print(x)
    root = NodeFactory.createProgram();
    root.setBody(NodeFactory.createBlock(
        Arrays.asList(ifStmt2, printStmt2)));
    DeadAssignmentsElimination.process(root, compiler);
    assertSameNodes(root, getTestProgram12());

    // Dead assignment test 12: if (a = C && X = a) {..}, print(x)
    root = NodeFactory.createProgram();
    root.setBody(NodeFactory.createBlock(
        Arrays.asList(ifStmt, printStmt2)));
    DeadAssignmentsElimination.process(root, compiler);
    assertSameNodes(root, getTestProgram13());
  }

  @Test
  public void testDeadAssignmentsEliminationWithFunction() {
    CompilerOptions options = new CompilerOptions();
    compiler = new AbstractCompiler() {};
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);

    Node root = NodeFactory.createProgram();
    root.setBody(getFunctionTestProgram());
    DeadAssignmentsElimination.process(root, compiler);
    assertSameNodes(root, getFunctionTestProgramResult());
  }

  private Node getTestProgram1() {
    return NodeFactory.createProgram();
  }

  private Node getTestProgram2() {
    return NodeFactory.createProgram();
  }

  private Node getTestProgram3() {
    return NodeFactory.createProgram();
  }

  private Node getTestProgram4() {
    return NodeFactory.createProgram();
  }

  private Node getTestProgram5() {
    return NodeFactory.createProgram();
  }

  private Node getTestProgram6() {
    return NodeFactory.createProgram();
  }

  private Node getTestProgram7() {
    return NodeFactory.createProgram();
  }

  private Node getTestProgram8() {
    return NodeFactory.createProgram();
  }

  private Node getTestProgram9() {
    return NodeFactory.createProgram();
  }

  private Node getTestProgram10() {
    return NodeFactory.createProgram();
  }

  private Node getTestProgram11() {
    return NodeFactory.createProgram();
  }

  private Node getTestProgram12() {
    return NodeFactory.createProgram();
  }

  private Node getTestProgram13() {
    return NodeFactory.createProgram();
  }

  private Node getFunctionTestProgram() {
    Scope scope = NodeFactory.createTopLevelScope();
    Node root = NodeFactory.createFunctionDeclaration(
        NodeFactory.createName("test"), ImmutableList.of(),
        NodeFactory.createBlock(
            Arrays.asList(
              NodeFactory.createVariable("x"),
              NodeFactory.createVariable("y"),
              NodeFactory.createAssign(ASSIGN, NodeFactory.createName("x"),
                  NodeFactory.createName("y")),
              NodeFactory.createVariable("z"),
              NodeFactory.createAssign(ASSIGN, NodeFactory.createName("y"),
                  NodeFactory.createName("z")),
              NodeFactory.createReturnStatement(NodeFactory.createName("x"))
            )
        )
    );
    scope.setParent(root.getBody().asBlock());
    return root;
  }

  private Node getFunctionTestProgramResult() {
    Scope scope = NodeFactory.createTopLevelScope();
    Node root = NodeFactory.createFunctionDeclaration(
        NodeFactory.createName("test"), ImmutableList.of(),
        NodeFactory.createBlock(
            Arrays.asList(
              NodeFactory.createVariable("x"),
              NodeFactory.createVariable("y"),
              NodeFactory.createAssign(ASSIGN, NodeFactory.createName("x"), NodeFactory.createName("y")),
              NodeFactory.createVariable("z"),
              NodeFactory.createReturnStatement(NodeFactory.createName("x"))
            )
        )
    );
    scope.setParent(root.getBody().asBlock());
    if (isFunction(root.getLastChild())) {
      root.getLastChild().removeChild(root.getLastChild().getLastChild());
    }
    return root;
  }

  private void assertSameNodes(Node expected, Node actual) {
    assertEquals(expected.toString(), actual.toString());
  }
}