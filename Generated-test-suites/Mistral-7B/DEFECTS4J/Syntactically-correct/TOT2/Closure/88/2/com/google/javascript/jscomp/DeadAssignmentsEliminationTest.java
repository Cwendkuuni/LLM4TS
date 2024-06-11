import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.base.Predicate;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  private AbstractCompiler compiler;
  private DeadAssignmentsElimination deadAssignmentsElimination;
  private NodeFactory nodeFactory;

  @Before
  public void setUp() {
    compiler = new Compiler();
    nodeFactory = compiler.nodeFactory();
    deadAssignmentsElimination = new DeadAssignmentsElimination(compiler);
  }

  @Test
  public void testDeadAssignment() {
    Node root = createAssignmentNode("x", createNumberNode(1));
    Node assignment = createAssignmentNode("x", createNumberNode(2));
    root.addChild(assignment);

    NodeTraversal nodeTraversal = new NodeTraversal(nodeFactory, root);
    deadAssignmentsElimination.process(null, root);

    assertEquals(1, root.getChildCount());
    assertNull(assignment);
  }

  @Test
  public void testDeadAssignmentWithCondition() {
    Node root = createIfNode(
        createAssignmentNode("x", createNumberNode(1)),
        createAssignmentNode("x", createNumberNode(2)));

    NodeTraversal nodeTraversal = new NodeTraversal(nodeFactory, root);
    deadAssignmentsElimination.process(null, root);

    assertEquals(1, root.getChildCount());
    assertNull(getChildAtIndex(root, 0).getLastChild());
  }

  @Test
  public void testDeadAssignmentInsideLoop() {
    Node root = createWhileNode(
        createAssignmentNode("x", createNumberNode(1)),
        createAssignmentNode("x", createNumberNode(2)),
        createNode(TOKEN_INT_KEYWORD, "i", createNumberNode(0)));

    NodeTraversal nodeTraversal = new NodeTraversal(nodeFactory, root);
    deadAssignmentsElimination.process(null, root);

    assertEquals(1, root.getChildCount());
    assertNull(getChildAtIndex(root, 0).getLastChild());
  }

  @Test
  public void testDeadAssignmentWithFunction() {
    Node root = createFunctionNode(
        "test",
        createBlockNode(),
        createAssignmentNode("x", createNumberNode(1)),
        createReturnNode(createNameNode("x")));

    NodeTraversal nodeTraversal = new NodeTraversal(nodeFactory, root);
    deadAssignmentsElimination.process(null, root);

    Node function = getChildAtIndex(root, 1);
    assertEquals(2, function.getChildCount());
    assertNull(getChildAtIndex(function, 1).getLastChild());
  }

  @Test
  public void testDeadAssignmentWithFunctionCall() {
    Node root = createAssignmentNode(
        "x",
        createCallNode(
            createQualifiedNameNode("Math", "sin"),
            createNumberNode(Math.PI)));

    NodeTraversal nodeTraversal = new NodeTraversal(nodeFactory, root);
    deadAssignmentsElimination.process(null, root);

    assertEquals(1, root.getChildCount());
    assertNull(root.getLastChild());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullExterns() {
    deadAssignmentsElimination.process(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullRoot() {
    deadAssignmentsElimination.process(null, null);
  }

  @Test
  public void testVariableNotAssigned() {
    Node root = createFunctionNode(
        "test",
        createBlockNode(),
        createNameNode("x"));

    NodeTraversal nodeTraversal = new NodeTraversal(nodeFactory, root);
    deadAssignmentsElimination.process(null, root);

    assertEquals(1, root.getChildCount());
    assertNotNull(root.getLastChild());
  }

  @Test
  public void testVariableIsReadBeforeKilled() {
    Node root = createFunctionNode(
        "test",
        createBlockNode(),
        createAssignmentNode("x", createNumberNode(1)),
        createNameNode("x"),
        createAssignmentNode("x", createNumberNode(2)));

    NodeTraversal nodeTraversal = new NodeTraversal(nodeFactory, root);
    deadAssignmentsElimination.process(null, root);

    assertEquals(2, root.getChildCount());
    assertNotNull(getChildAtIndex(root, 1).getLastChild());
  }

  @Test
  public void testVariableIsReadAfterKilled() {
    Node root = createFunctionNode(
        "test",
        createBlockNode(),
        createAssignmentNode("x", createNumberNode(1)),
        createAssignmentNode("x", createNumberNode(2)),
        createNameNode("x"));

    NodeTraversal nodeTraversal = new NodeTraversal(nodeFactory, root);
    deadAssignmentsElimination.process(null, root);

    assertEquals(1, root.getChildCount());
    assertNull(root.getLastChild());
  }

  private Node createAssignmentNode(String variableName, Node value) {
    Node assignment = nodeFactory.createAssignment(variableName, value);
    return assignment;
  }

  private Node createIfNode(Node condition, Node thenBranch) {
    return nodeFactory.createIf(condition, thenBranch);
  }

  private Node createWhileNode(Node condition, Node body, Node label) {
    Node whileNode = nodeFactory.createWhile(condition, body);
    whileNode.setLabel(label);
    return whileNode;
  }

  private Node createFunctionNode(
      String name, Node body, Node... parameters) {
    Node function = nodeFactory.createFunction(name, body, parameters);
    return function;
  }

  private Node createBlockNode() {
    return nodeFactory.createBlock();
  }

  private Node createCallNode(Node callee, Node... arguments) {
    Node call = nodeFactory.createCall(callee, arguments);
    return call;
  }

  private Node createNumberNode(double value) {
    return nodeFactory.createNumber(value);
  }

  private Node createNameNode(String name) {
    return nodeFactory.createName(name);
  }

  private Node createQualifiedNameNode(String className, String name) {
    return nodeFactory.createQualifiedName(className, name);
  }

  private Node createReturnNode(Node value) {
    Node returnNode = nodeFactory.createReturnStatement(value);
    return returnNode;
  }

  private Node getChildAtIndex(Node parent, int index) {
    return parent.getChildAt(index);
  }
}