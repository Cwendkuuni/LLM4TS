import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.google.javascript.jscomp.NodeTraversal.traverse;

import com.google.common.base.Predicates;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

/**
 * Tests for {@link DeadAssignmentsElimination} class
 */
public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    AbstractCompiler compiler = new AbstractCompiler();
    AbstractPostOrderCallback callback = new DeadAssignmentsElimination(compiler);
    Node rootNode = NodeFactory.createScript("function foo() {}");

    traverse(compiler, rootNode, callback);

    ControlFlowGraph<Node> cfg = compiler.getControlFlowGraph();
    LiveVariablesAnalysis liveness = callback.liveness;

    // Basic functionality test
    Node functionNode = NodeUtil.getFunctionDeclaration(rootNode);
    DiGraphNode<Node, Branch> functionNodeInGraph = cfg.getDirectedGraphNode(functionNode);
    FlowState<LiveVariableLattice> state = functionNodeInGraph.getAnnotation();
    assertFalse(state.getOut().isLive(liveness.getVariableByName("x")));

    // Test functionality with dead variable before assignment
    Node deadVariableBeforeAssignmentNode = NodeFactory.createStatement(
        "var x; x = 1;");
    Node deadVariableBeforeAssignmentFunctionNode = NodeUtil.getFunctionDeclaration(
        NodeFactory.createScript("function bar() {" +
            "  " + deadVariableBeforeAssignmentNode.toSource() +
            "}"));
    Node deadVariableBeforeAssignmentFunctionNodeInGraph = cfg.getDirectedGraphNode(
        NodeUtil.getFunctionDeclaration(NodeFactory.createScript("function bar() {" +
            "  " + deadVariableBeforeAssignmentNode.toSource() +
            "}")).getLastChild());
    state = deadVariableBeforeAssignmentFunctionNodeInGraph.getAnnotation();
    assertFalse(state.getOut().isLive(liveness.getVariableByName("x")));

    // Test functionality with dead variable after assignment
    Node deadVariableAfterAssignmentNode = NodeFactory.createStatement(
        "var x = 1; x = 2;");
    Node deadVariableAfterAssignmentFunctionNode = NodeUtil.getFunctionDeclaration(
        NodeFactory.createScript("function baz() {" +
            "  " + deadVariableAfterAssignmentNode.toSource() +
            "}")).getLastChild();
    state = deadVariableAfterAssignmentFunctionNode.getAnnotation();
    assertFalse(state.getOut().isLive(liveness.getVariableByName("x")));

    // Test functionality with dead variable assignment in if statement (condition)
    Node deadVariableAssignmentInIfStatementConditionNode = NodeFactory.createBinaryOp(
        Token.IF, NodeFactory.createBooleanLiteral(false),
        NodeFactory.createStatement(NodeFactory.createAssignment(
            NodeFactory.createName("x"),
            NodeFactory.createNumberLiteral(1))));
    Node deadVariableAssignmentInIfStatementFunctionNode = NodeUtil.getFunctionDeclaration(
        NodeFactory.createScript("function qux() {" +
            "  if (" + deadVariableAssignmentInIfStatementConditionNode.toSource() +
            ")" +
            "  {}" +
            "}")).getLastChild();
    state = deadVariableAssignmentInIfStatementFunctionNode.getAnnotation();
    assertFalse(state.getOut().isLive(liveness.getVariableByName("x")));

    // Test functionality with dead variable assignment in for statement (condition)
    Node deadVariableAssignmentInForStatementConditionNode = NodeFactory.createBinaryOp(
        Token.FOR, NodeFactory.createName("i"),
        NodeFactory.createBinaryOp(
            Token.LESS, NodeFactory.createName("i"),
            NodeFactory.createNumberLiteral(5),
            NodeFactory.createBinaryOp(Token.PLUS, NodeFactory.createName("i"),
                NodeFactory.createNumberLiteral(1))));
    Node deadVariableAssignmentInForStatementFunctionNode = NodeUtil.getFunctionDeclaration(
        NodeFactory.createScript("function quux() {" +
            "  for (" + deadVariableAssignmentInForStatementConditionNode.toSource() +
            ")" +
            "  {" +
            "    var x = 1;" +
            "    x = 2;" +
            "  }" +
            "}")).getLastChild();
    state = deadVariableAssignmentInForStatementFunctionNode.getAnnotation();
    assertFalse(state.getOut().isLive(liveness.getVariableByName("x")));

    // Test edge case with dead variable assignment in while statement
    Node deadVariableAssignmentInWhileStatementConditionNode = NodeFactory.createBinaryOp(
        Token.WHILE, NodeFactory.createBooleanLiteral(false),
        NodeFactory.createStatement(NodeFactory.createAssignment(
            NodeFactory.createName("x"),
            NodeFactory.createNumberLiteral(1))));
    Node deadVariableAssignmentInWhileStatementFunctionNode = NodeUtil.getFunctionDeclaration(
        NodeFactory.createScript("function quuz() {" +
            "  while (" + deadVariableAssignmentInWhileStatementConditionNode.toSource() +
            ")" +
            "  {" +
            "    var x = 1;" +
            "    x = 2;" +
            "  }" +
            "}")).getLastChild();
    state = deadVariableAssignmentInWhileStatementFunctionNode.getAnnotation();
    assertFalse(state.getOut().isLive(liveness.getVariableByName("x")));

    // Test edge case with dead variable assignment in switch statement
    Node deadVariableAssignmentInSwitchStatementNode = NodeFactory.createStatement(
        "switch (1) {" +
        "  case 1:" +
        "    var x = 1;" +
        "    x = 2;" +
        "}");
    Node deadVariableAssignmentInSwitchStatementFunctionNode = NodeUtil.getFunctionDeclaration(
        NodeFactory.createScript("function quuz() {" +
            "  switch (1)" +
            "  {" +
            "    case 1:" +
            "      " + deadVariableAssignmentInSwitchStatementNode.toSource() +
            "    default:" +
            "      {}" +
            "  }" +
            "}")).getLastChild();
    state = deadVariableAssignmentInSwitchStatementFunctionNode.getAnnotation();
    assertFalse(state.getOut().isLive(liveness.getVariableByName("x")));

    // Test edge case with dead variable assignment in return statement
    Node deadVariableAssignmentInReturnStatementNode = NodeFactory.createStatement(
        "function foo() {" +
        "  var x = 1;" +
        "  return x = 2;" +
        "}");
    Node deadVariableAssignmentInReturnStatementFunctionNode = NodeUtil.getFunctionDeclaration(
        NodeFactory.createScript("function foo() {" +
            "  " + deadVariableAssignmentInReturnStatementNode.toSource() +
            "}")).getLastChild();
    state = deadVariableAssignmentInReturnStatementFunctionNode.getAnnotation();
    assertFalse(state.getOut().isLive(liveness.getVariableByName("x")));
  }

  @Test
  public void testDeadAssignmentsEliminationWithExceptionScenarios() {
    AbstractCompiler compiler = new AbstractCompiler();
    AbstractPostOrderCallback callback = new DeadAssignmentsElimination(compiler);
    Node rootNode = NodeFactory.createStatement(
        "try {" +
        "  function foo() {" +
        "    x = 1;" + // Dead assignment
        "    throw new Error('test error');" +
        "  }" +
        "} catch (e) {}");

    traverse(compiler, rootNode, callback);

    ControlFlowGraph<Node> cfg = compiler.getControlFlowGraph();
    LiveVariablesAnalysis liveness = callback.liveness;

    // Test functionality with try-catch statement
    Node functionNode = NodeUtil.getFunctionDeclaration(rootNode).getLastChild();
    DiGraphNode<Node, Branch> functionNodeInGraph = cfg.getDirectedGraphNode(functionNode);
    FlowState<LiveVariableLattice> state = functionNodeInGraph.getAnnotation();
    assertFalse(state.getOut().isLive(liveness.getVariableByName("x")));
  }

  /**
   * Utilities
   */
  private static NodeUtil NodeUtil = new NodeUtil();
  private static final Predicate<Node> MATCH_REMOVABLE_ASSIGNS =
      Predicates.compose(Predicates.isInstanceOf(Node.class), DeadAssignmentsElimination.matchRemovableAssigns);

}