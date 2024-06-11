import static org.junit.Assert.*;
import static com.google.javascript.jscomp.MaybeReachingVariableUse.*;

import org.junit.Before;
import org.junit.Test;

public class MaybeReachingVariableUseTest {

  private ControlFlowGraph<Node> cfg;
  private Scope jsScope;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    // Initialize the ControlFlowGraph, Scope and AbstractCompiler for testing.
    // Replace this with actual initialization code.
    cfg = new ControlFlowGraph<Node>() {};
    jsScope = new Scope();
    compiler = new AbstractCompiler();
  }

  @Test
  public void testMaybeReachingVariableUse() {
    MaybeReachingVariableUse analyzer = new MaybeReachingVariableUse(cfg, jsScope, compiler);

    // Test empty program.
    ReachingUses result = analyzer.analyze(Node.NULL);
    assertTrue(result.mayUseMap.isEmpty());

    // Test a simple variable assignment.
    Node assignmentNode = Node.newNode(Token.ASSIGN, null, Node.newName("x"), Node.newNumber(5));
    Node useNode = Node.newName("x");
    Node defNode = Node.newName("x");
    cfg.addEdge(new DiGraphEdge(defNode, useNode));

    result = analyzer.analyze(useNode);
    Set<Node> uses = analyzer.getUses("x", defNode);
    assertTrue(uses.contains(defNode));

    // Test a conditional statement.
    Node ifNode = Node.newToken(Token.IF, null);
    Node condition = Node.newBoolean(true);
    Node assignmentIfTrueNode = Node.newNode(Token.ASSIGN, null, Node.newName("x"), Node.newNumber(6));
    Node assignmentIfFalseNode = Node.newNode(Token.ASSIGN, null, Node.newName("x"), Node.newNumber(7));
    cfg.addEdge(new DiGraphEdge(ifNode, assignmentIfTrueNode));
    cfg.addEdge(new DiGraphEdge(ifNode, assignmentIfFalseNode));

    result = analyzer.analyze(useNode);
    uses = analyzer.getUsages(useNode);
    assertTrue(uses.contains(assignmentIfTrueNode));
    assertTrue(uses.contains(assignmentIfFalseNode));

    // Test a loop.
    Node whileNode = Node.newToken(Token.WHILE, null);
    Node conditionWhile = Node.newBoolean(true);
    Node assignmentWhileNode = Node.newNode(Token.ASSIGN, null, Node.newName("x"), Node.newNumber(8));
    cfg.addEdge(new DiGraphEdge(whileNode, assignmentWhileNode));

    result = analyzer.analyze(useNode);
    uses = analyzer.getUsages(useNode);
    assertTrue(uses.contains(assignmentWhileNode));

    // Test a function call.
    Node functionCallNode = Node.newName("myFunction");
    Node functionDefNode = Node.newName("myFunction");
    cfg.addEdge(new DiGraphEdge(useNode, functionCallNode));
    cfg.addEdge(new DiGraphEdge(functionDefNode, functionCallNode));

    result = analyzer.analyze(useNode);
    uses = analyzer.getUsages(useNode);
    assertTrue(uses.contains(functionDefNode));

    // Test a function with a local variable.
    Node functionNode = Node.newNode(Token.FUNCTION, null, Node.newName("myFunction"), null, null, null);
    Node localVariableNode = Node.newName("myLocalVariable");
    Node localVariableAssignmentNode = Node.newNode(Token.ASSIGN, null, localVariableNode, Node.newNumber(9));
    cfg.addChild(functionNode);
    cfg.addChild(localVariableAssignmentNode);
    cfg.addEdge(new DiGraphEdge(functionNode, localVariableAssignmentNode));

    result = analyzer.analyze(useNode);
    uses = analyzer.getUsages(useNode);
    assertTrue(uses.isEmpty());

    // Test a function with an escaped variable.
    jsScope.declareVar("myEscapedVariable");
    Node escapedVariableNode = Node.newName("myEscapedVariable");
    Node escapedVariableAssignmentNode = Node.newNode(Token.ASSIGN, null, escapedVariableNode, Node.newNumber(10));
    cfg.addChild(functionNode);
    cfg.addChild(escapedVariableAssignmentNode);
    cfg.addEdge(new DiGraphEdge(functionNode, escapedVariableAssignmentNode));

    result = analyzer.analyze(useNode);
    uses = analyzer.getUsages(useNode);
    assertTrue(uses.isEmpty());

    // Test a function with a local variable and an escaped variable.
    Node localVariableNode2 = Node.newName("myLocalVariable2");
    Node localVariableAssignmentNode2 = Node.newNode(Token.ASSIGN, null, localVariableNode2, Node.newNumber(11));
    cfg.addChild(functionNode);
    cfg.addChild(localVariableAssignmentNode2);
    cfg.addEdge(new DiGraphEdge(functionNode, localVariableAssignmentNode2));
    cfg.addEdge(new DiGraphEdge(functionNode, escapedVariableNode));

    result = analyzer.analyze(useNode);
    uses = analyzer.getUsages(useNode);
    assertTrue(uses.isEmpty());

  }
}