import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.traverse;
import static com.google.javascript.rhino.Token.*;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.DataFlowAnalysis;
import com.google.javascript.jscomp.DeadAssignmentsElimination;
import com.google.javascript.jscomp.LiveVariablesAnalysis;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.TokenFactory;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DeadAssignmentsEliminationTest {

  private static Node createSimpleAssignmentsNode(String variable) {
    Node root = NodeFactory.createBlock(null, null);
    Node assignNode = NodeFactory.createAssignment(
        TokenFactory.createIdentifierToken(variable),
        NodeFactory.createNumber(1));
    Node parent = NodeFactory.createStatement(assignNode);
    root.addChild(parent);
    return root;
  }

  private static Node createIfAssignmentsNode(String variable) {
    Node root = NodeFactory.createBlock(null, null);
    Node ifNode = NodeFactory.createIfStatement(
        NodeFactory.createBooleanLiteral(true), NodeFactory.createBlock(null, null),
        NodeFactory.createBlock(null, null));
    Node assignNode = NodeFactory.createAssignment(
        TokenFactory.createIdentifierToken(variable),
        NodeFactory.createNumber(1));
    Node parent = NodeFactory.createStatement(assignNode);
    Node ifBlock = NodeFactory.createBlock(null, null);
    ifBlock.addChild(parent);
    ifNode.setThenStatement(ifBlock);
    Node elseBlock = NodeFactory.createBlock(null, null);
    ifNode.setElseStatement(elseBlock);
    root.addChild(ifNode);
    return root;
  }

  private static Node createForAssignmentsNode(String variable) {
    Node root = NodeFactory.createBlock(null, null);
    Node forNode = NodeFactory.createForStatement(
        TokenFactory.createIdentifierToken(variable),
        NodeFactory.createNumber(0), NodeFactory.createNumber(1),
        NodeFactory.createEmptyStatement(), NodeFactory.createBlock(null, null));
    root.addChild(forNode);
    return root;
  }

  private static Node createWhileAssignmentsNode(String variable) {
    Node root = NodeFactory.createBlock(null, null);
    Node whileNode = NodeFactory.createWhileLoop(
        NodeFactory.createBooleanLiteral(true), NodeFactory.createBlock(null, null),
        NodeFactory.createBlock(null, null), NodeFactory.createEmptyStatement());
    Node assignNode = NodeFactory.createAssignment(
        TokenFactory.createIdentifierToken(variable),
        NodeFactory.createNumber(1));
    Node parent = NodeFactory.createStatement(assignNode);
    Node whileBlock = NodeFactory.createBlock(null, null);
    whileBlock.addChild(parent);
    whileNode.setBody(whileBlock);
    root.addChild(whileNode);
    return root;
  }

  private static Node createReturnAssignmentsNode(String variable) {
    Node root = NodeFactory.createBlock(null, null);
    Node assignNode = NodeFactory.createAssignment(
        TokenFactory.createIdentifierToken(variable),
        NodeFactory.createNumber(1));
    Node returnNode = NodeFactory.createReturnStatement(null, null, NodeFactory.createEmptyStatement());
    Node parent = NodeFactory.createStatement(assignNode);
    Node function = NodeFactory.createFunction(
        null, null, null, NodeFactory.createBlock(null, null));
    function.addChild(parent);
    function.addChild(returnNode);
    root.addChild(function);
    return root;
  }

  private static Node createAssignmentWithDeadVariableNode() {
    Node root = NodeFactory.createBlock(null, null);
    Node assignNode = NodeFactory.createAssignment(
        TokenFactory.createIdentifierToken("deadVariable"),
        NodeFactory.createNumber(1));
    Node parent = NodeFactory.createStatement(assignNode);
    Node ifNode = NodeFactory.createIfStatement(
        NodeFactory.createBooleanLiteral(false), NodeFactory.createEmptyStatement(),
        NodeFactory.createEmptyStatement());
    Node deadAssignNode = NodeFactory.createAssignment(
        TokenFactory.createIdentifierToken("deadVariable"),
        NodeFactory.createNumber(2));
    Node ifBlock = NodeFactory.createBlock(null, null);
    ifBlock.addChild(deadAssignNode);
    ifNode.setThenStatement(ifBlock);
    root.addChild(parent);
    root.addChild(ifNode);
    return root;
  }

  private static Node createAssignmentWithDeadExpressionNode() {
    Node root = NodeFactory.createBlock(null, null);
    Node assignNode = NodeFactory.createAssignment(
        TokenFactory.createIdentifierToken("deadVariable"),
        NodeFactory.createNegatedExpression(
            NodeFactory.createBinaryOperator(
                OVERFLOW_OR,
                NodeFactory.createNumber(1),
                NodeFactory.createNumber(1))));
    Node parent = NodeFactory.createStatement(assignNode);
    Node ifNode = NodeFactory.createIfStatement(
        NodeFactory.createBooleanLiteral(false), NodeFactory.createEmptyStatement(),
        NodeFactory.createEmptyStatement());
    Node deadAssignNode = NodeFactory.createAssignment(
        TokenFactory.createIdentifierToken("deadVariable"),
        NodeFactory.createNumber(2));
    Node ifBlock = NodeFactory.createBlock(null, null);
    ifBlock.addChild(deadAssignNode);
    ifNode.setThenStatement(ifBlock);
    root.addChild(parent);
    root.addChild(ifNode);
    return root;
  }

  private static Node createAssignmentWithDeadParentNode() {
    Node root = NodeFactory.createBlock(null, null);
    Node assignNode = NodeFactory.createAssignment(
        TokenFactory.createIdentifierToken("parentVariable"),
        NodeFactory.createNumber(1));
    Node parent = new Node(ASSIGN, new Node[]{assignNode, NodeFactory.createNumber(2)}, null);
    Node parentAssignNode = NodeFactory.createAssignment(
        TokenFactory.createIdentifierToken("deadVariable"),
        parent);
    Node parentBlock = NodeFactory.createBlock(null, null);
    parentBlock.addChild(parentAssignNode);
    Node ifNode = NodeFactory.createIfStatement(
        NodeFactory.createBooleanLiteral(false), NodeFactory.createEmptyStatement(),
        NodeFactory.createEmptyStatement());
    Node deadAssignNode = NodeFactory.createAssignment(
        TokenFactory.createIdentifierToken("deadVariable"),
        NodeFactory.createNumber(2));
    Node ifBlock = NodeFactory.createBlock(null, null);
    ifBlock.addChild(deadAssignNode);
    ifNode.setThenStatement(ifBlock);
    root.addChild(parentBlock);
    root.addChild(ifNode);
    return root;
  }

  private static Node createAssignmentWithDeadChildNode() {
    Node root = NodeFactory.createBlock(null, null);
    Node assignNode = NodeFactory.createAssignment(
        TokenFactory.createIdentifierToken("parentVariable"),
        NodeFactory.createNumber(1));
    Node parent = NodeFactory.createStatement(assignNode);
    Node parentVariable = NodeFactory.createVariableDeclarationStatement(
        NodeFactory.createIdentifierToken("parentVariable"),
        Token.ASSIGN, NodeFactory.createNumber(1), NodeFactory.createEmptyStatement());
    Node parentBlock = NodeFactory.createBlock(null, null);
    parentBlock.addChild(parentVariable);
    parentBlock.addChild(parent);
    Node ifNode = NodeFactory.createIfStatement(
        NodeFactory.createBooleanLiteral(false), NodeFactory.createEmptyStatement(),
        NodeFactory.createEmptyStatement());
    Node deadAssignNode = NodeFactory.createAssignment(
        NodeFactory.createIdentifierToken("deadChildVariable"),
        NodeFactory.createNumber(2));
    Node deadChildBlock = NodeFactory.createBlock(null, null);
    deadChildBlock.addChild(deadAssignNode);
    Node deadChildVariable = NodeFactory.createVariableDeclarationStatement(
        NodeFactory.createIdentifierToken("deadChildVariable"),
        Token.ASSIGN, NodeFactory.createNumber(2), NodeFactory.createEmptyStatement());
    Node deadChildBlockWithVariable = NodeFactory.createBlock(null, null);
    deadChildBlockWithVariable.addChild(deadChildVariable);
    deadChildBlockWithVariable.addChild(deadChildBlock);
    Node deadAssignParent = NodeFactory.createAssignment(
        TokenFactory.createIdentifierToken("parentVariable"),
        NodeFactory.createBlock(null, null));
    Node deadAssignParentWithVariable = NodeFactory.createBlock(null, null);
    deadAssignParentWithVariable.addChild(deadAssignParent);
    deadAssignParentWithVariable.addChild(deadChildBlockWithVariable);
    ifNode.setThenStatement(deadAssignParentWithVariable);
    root.addChild(parentBlock);
    root.addChild(ifNode);
    return root;
  }

  private static void testDeadAssignmentsElimination(Node input) {
    AbstractCompiler compiler = new AbstractCompiler();
    DeadAssignmentsElimination deadAssignmentsElimination =
        new DeadAssignmentsElimination(compiler);
    ControlFlowGraph controlFlowGraph = new ControlFlowGraph();
    LiveVariablesAnalysis liveVariablesAnalysis = new LiveVariablesAnalysis(
        controlFlowGraph, null, compiler);
    liveVariablesAnalysis.analyze();
    deadAssignmentsElimination.tryRemoveDeadAssignments(
        NodeTraversal.create(compiler, input, liveVariablesAnalysis), controlFlowGraph);
    Node expectedOutput = NodeFactory.createBlock(null, null);
    Function<Node, Node> eliminator = new Function<Node, Node>() {
      @Override
      public Node apply(Node input) {
        return DeadAssignmentsElimination.tryRemoveAssignment(
            NodeTraversal.create(compiler, input, liveVariablesAnalysis), input,
            liveVariablesAnalysis.getLastAnalysisResult());
      }
    };
    Node output = NodeTraversal.traverse(eliminator, input, null);
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testSimpleAssignments() {
    Node input = createSimpleAssignmentsNode("simpleVariable");
    testDeadAssignmentsElimination(input);
  }

  @Test
  public void testIfAssignments() {
    Node input = createIfAssignmentsNode("ifVariable");
    testDeadAssignmentsElimination(input);
  }

  @Test
  public void testForAssignments() {
    Node input = createForAssignmentsNode("forVariable");
    testDeadAssignmentsElimination(input);
  }

  @Test
  public void testWhileAssignments() {
    Node input = createWhileAssignmentsNode("whileVariable");
    testDeadAssignmentsElimination(input);
  }

  @Test
  public void testReturnAssignments() {
    Node input = createReturnAssignmentsNode("returnVariable");
    testDeadAssignmentsElimination(input);
  }

  @Test
  public void testAssignmentsWithDeadVariable() {
    Node input = createAssignmentWithDeadVariableNode();
    testDeadAssignmentsElimination(input);
  }

  @Test
  public void testAssignmentsWithDeadExpression() {
    Node input = createAssignmentWithDeadExpressionNode();
    testDeadAssignmentsElimination(input);
  }

  @Test
  public void testAssignmentsWithDeadParent() {
    Node input = createAssignmentWithDeadParentNode();
    testDeadAssignmentsElimination(input);
  }

  @Test
  public void testAssignmentsWithDeadChild() {
    Node input = createAssignmentWithDeadChildNode();
    testDeadAssignmentsElimination(input);
  }

  @Test
  public void testNoAssignments() {
    Node input = NodeFactory.createBlock(null, null);
    testDeadAssignmentsElimination(input);
  }
}