import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.ElaboratedNode;
import com.google.javascript.jscomp.GraphReachability;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.ArrayList;

/**
 * Test class for UnreachableCodeElimination class.
 */
public class UnreachableCodeEliminationTest {

  private AbstractCompiler compiler;
  private ControlFlowGraph<Node> controlFlowGraph;
  private Node rootNode;

  @org.junit.Before
  public void setUp() {
    compiler = new CompilerOptions();
    controlFlowGraph = new ControlFlowAnalysis(compiler, false, false);
    rootNode = NodeFactory.createProgram();
  }

  @org.junit.Test
  public void testUnreachableCodeElimination() {
    // Test case 1: Dead code after return statement
    Node functionNode = NodeFactory.createFunctionDeclaration(
        "test", NodeFactory.createList(Token.PUBLIC), NodeFactory.createList(Token.VOID),
        NodeFactory.createBlock(1, new Node[0]));
    Node conditionalNode = NodeFactory.createConditionalExpression(
        NodeFactory.createIdentifier("x"), NodeFactory.createBooleanLiteral(true),
        NodeFactory.createReturnStatement(NodeFactory.createNull()),
        NodeFactory.createBlock(1, new Node[0]));
    Node functionBodyNode = NodeFactory.createBlock(1,
        new Node[] { NodeFactory.createVariableDeclarationStatement(
            "x", NodeFactory.createIdentifier("x"), NodeFactory.createNull()),
            functionNode, conditionalNode, NodeFactory.createIdentifier("alert")});
    rootNode.addChild(functionBodyNode);

    controlFlowAnalysis();
    new UnreachableCodeElimination(compiler, true).process(null, rootNode);
    assertFalse("Dead code should have been removed",
        NodeUtil.hasDescendants(rootNode, conditionalNode));

    // Test case 2: Dead code without side effects
    Node deadCodeNode1 = NodeFactory.createExpressionStatement(
        NodeFactory.createPropertyAccess(NodeFactory.createIdentifier("a"),
            NodeFactory.createIdentifier("b"), NodeFactory.createIdentifier("MyClass"))
            .getFirstChild());
    Node deadCodeNode2 = NodeFactory.createExpressionStatement(
        NodeFactory.createBooleanLiteral(true));
    Node functionNode2 = NodeFactory.createFunctionDeclaration(
        "test2", NodeFactory.createList(Token.PUBLIC), NodeFactory.createList(Token.VOID),
        NodeFactory.createBlock(1, new Node[] { deadCodeNode1, deadCodeNode2 }));
    rootNode.addChild(functionNode2);

    controlFlowAnalysis();
    new UnreachableCodeElimination(compiler, true).process(null, rootNode);
    assertFalse("Dead code should have been removed",
        NodeUtil.hasDescendants(rootNode, functionNode2));

    // Test case 3: Dead code after a break statement
    Node breakStatementNode = NodeFactory.createBreakStatement();
    Node deadCodeNode3 = NodeFactory.createExpressionStatement(
        NodeFactory.createPropertyAccess(NodeFactory.createIdentifier("a"),
            NodeFactory.createIdentifier("b"), NodeFactory.createIdentifier("MyClass"))
            .getFirstChild());
    Node functionNode3 = NodeFactory.createFunctionDeclaration(
        "test3", NodeFactory.createList(Token.PUBLIC), NodeFactory.createList(Token.VOID),
        NodeFactory.createBlock(1, new Node[] { breakStatementNode, deadCodeNode3 }));
    rootNode.addChild(functionNode3);

    controlFlowAnalysis();
    new UnreachableCodeElimination(compiler, false).process(null, rootNode);
    assertFalse("Dead code should have been removed",
        NodeUtil.hasDescendants(rootNode, functionNode3));

    // Test case 4: Dead code after a continue statement
    Node continueStatementNode = NodeFactory.createContinueStatement();
    Node deadCodeNode4 = NodeFactory.createExpressionStatement(
        NodeFactory.createPropertyAccess(NodeFactory.createIdentifier("a"),
            NodeFactory.createIdentifier("b"), NodeFactory.createIdentifier("MyClass"))
            .getFirstChild());
    Node functionNode4 = NodeFactory.createFunctionDeclaration(
        "test4", NodeFactory.createList(Token.PUBLIC), NodeFactory.createList(Token.VOID),
        NodeFactory.createBlock(1, new Node[] { continueStatementNode, deadCodeNode4 }));
    rootNode.addChild(functionNode4);

    controlFlowAnalysis();
    new UnreachableCodeElimination(compiler, false).process(null, rootNode);
    assertFalse("Dead code should have been removed",
        NodeUtil.hasDescendants(rootNode, functionNode4));

    // Test case 5: Dead code after a return statement with a side-effect
    Node functionNode5 = NodeFactory.createFunctionDeclaration(
        "test5", NodeFactory.createList(Token.PUBLIC), NodeFactory.createList(Token.VOID),
        NodeFactory.createBlock(1, new Node[] {
            NodeFactory.createVariableDeclarationStatement(
                "x", NodeFactory.createIdentifier("x"), NodeFactory.createNull()),
            NodeFactory.createReturnStatement(NodeFactory.createIdentifier("x")),
            NodeFactory.createExpressionStatement(
                NodeFactory.createPropertyAccess(NodeFactory.createIdentifier("a"),
                    NodeFactory.createIdentifier("b"), NodeFactory.createIdentifier("MyClass"))
                    .getFirstChild()) }));
    rootNode.addChild(functionNode5);

    controlFlowAnalysis();
    new UnreachableCodeElimination(compiler, true).process(null, rootNode);
    assertTrue("Dead code should not have been removed",
        NodeUtil.hasDescendants(rootNode,
            NodeUtil.getFirstChild(NodeUtil.getLastChild(functionNode5))));
  }

  private void controlFlowAnalysis() {
    ElaboratedNode elaboratedNode = NodeTraversal.traverse(compiler, rootNode);
    controlFlowGraph.computeReachableNodes(elaboratedNode.getGraph());
  }
}