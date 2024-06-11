import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.isName;
import static com.google.javascript.jscomp.NodeUtil.isVar;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ControlFlowGraph;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.BitSet;
import java.util.List;
import java.util.Set;

public class LiveVariablesAnalysisTest {

  @org.junit.Before
  public void setUp() {
    // Initialize your test data here, for example:
    // Scope scope = new Scope();
    // ControlFlowGraph cfg = new ControlFlowGraph();
    // AbstractCompiler compiler = new AbstractCompiler(cfg, scope);
  }

  @org.junit.Test
  public void testLiveVariablesAnalysis() {
    Scope scope = new Scope();
    ControlFlowGraph cfg = new ControlFlowGraph();
    AbstractCompiler compiler = new AbstractCompiler(cfg, scope);
    LiveVariablesAnalysis analysis = new LiveVariablesAnalysis(cfg, scope, compiler);

    // Test your methods here, for example:
    Node rootNode = Node.CODE_BLOCK;
    analysis.computeEscapedLocals();
    Set<Var> escapedLocals = analysis.getEscapedLocals();
    assertEquals(0, escapedLocals.size());

    // Test a simple variable assignment
    Node assignmentNode = Node.newNode(Token.ASSIGN, new Node[]{}, new Node[]{
        Node.newName("x"), Node.newInt(1)
    });
    analysis.computeGenKill(assignmentNode, BitSet.EMPTY, BitSet.EMPTY, false);
    BitSet liveVariables = analysis.getAnalysis(assignmentNode);
    Var x = scope.getVar("x");
    assertTrue(liveVariables.get(x.index));

    // Test a variable assignment within a loop
    Node loopNode = Node.newNode(Token.WHILE, new Node[]{
        Node.newName("condition")
    }, new Node[]{});
    Node assignmentNodeInLoop = Node.newNode(Token.ASSIGN, new Node[]{
        Node.newName("x")
    }, new Node[]{Node.newInt(2)});
    analysis.computeGenKill(assignmentNodeInLoop, BitSet.EMPTY, BitSet.EMPTY, false);
    analysis.computeGenKill(loopNode, BitSet.EMPTY, BitSet.EMPTY, false);
    liveVariables = analysis.getAnalysis(loopNode);
    assertTrue(liveVariables.get(x.index));

    // Test a conditional assignment
    Node ifNode = Node.newNode(Token.IF, new Node[]{Node.newName("condition")}, new Node[]{});
    Node assignmentNodeInIf = Node.newNode(Token.ASSIGN, new Node[]{Node.newName("x")}, new Node[]{Node.newInt(3)});
    Node elseNode = Node.newNode(Token.IF, new Node[]{Node.newName("condition")}, new Node[]{Node.newBlock()});
    Node assignelseNode = Node.newNode(Token.ASSIGN, new Node[]{Node.newName("x")}, new Node[]{Node.newInt(4)});
    Node blockNode = Node.newBlock();
    analysis.computeGenKill(ifNode, BitSet.EMPTY, BitSet.EMPTY, false);
    analysis.computeGenKill(assignelseNode, BitSet.EMPTY, BitSet.EMPTY, false);
    analysis.computeGenKill(elseNode, BitSet.EMPTY, BitSet.EMPTY, false);
    analysis.computeGenKill(assignmentNodeInIf, BitSet.EMPTY, BitSet.EMPTY, true);
    analysis.computeGenKill(blockNode, BitSet.EMPTY, BitSet.EMPTY, false);
    liveVariables = analysis.getAnalysis(ifNode);
    assertTrue(liveVariables.get(x.index));

    // Test a function call with arguments
    Node functionCallNode = Node.newName("myFunction");
    Node argumentsNode = Node.newArray(Node.ANY, new Node[]{Node.newInt(1), Node.newInt(2)});
    analysis.computeGenKill(functionCallNode, BitSet.EMPTY, BitSet.EMPTY, false);
    analysis.computeGenKill(argumentsNode, BitSet.EMPTY, BitSet.EMPTY, false);
    liveVariables = analysis.getAnalysis(functionCallNode);
    // Assume myFunction has a local variable with the same name as the first argument
    Var arg1 = scope.getVar("arg1");
    assertTrue(liveVariables.get(arg1.index));

    // Test a function definition
    Node functionDefinitionNode = Node.newNode(Token.FUNCTION, new Node[]{
        Node.newName("myFunction"), Node.newList(new Node[]{Node.newBlock()})
    }, new Node[]{});
    analysis.computeGenKill(functionDefinitionNode, BitSet.EMPTY, BitSet.EMPTY, false);
    liveVariables = analysis.getAnalysis(functionDefinitionNode);
    // Assume myFunction has a local variable with the name "result"
    Var result = scope.getVar("result");
    assertTrue(liveVariables.get(result.index));
  }

  @org.junit.After
  public void tearDown() {
    // Clean up your test data here, if necessary
  }
}