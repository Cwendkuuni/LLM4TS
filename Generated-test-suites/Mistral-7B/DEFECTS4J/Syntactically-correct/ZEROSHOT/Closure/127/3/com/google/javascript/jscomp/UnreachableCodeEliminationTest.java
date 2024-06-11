import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.ControlFlowAnalysis;
import com.google.javascript.rhino.Token;
import com.google.Common.base.Preconditions;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private ControlFlowAnalysis cfa;
  private ControlFlowGraph<Node> cfg;

  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    cfa = new ControlFlowAnalysis(compiler, false, false);
  }

  @Test
  public void testUnreachableCodeElimination() {
    Node root = new Node(Token.NULL);
    Node functionNode = new Node(Token.FUNCTION, "test", new Node[0]);
    Node ifStatementNode = new Node(Token.IF, null, null);
    Node returnStatementNode = new Node(Token.RETURN, null, null);
    Node alertStatementNode = new Node(Token.CALL, "alert", new Node[0]);

    Node ifConditionNode = new Node(Token.IDENT, "x");
    Node unreachableCodeNode = new Node(Token.CALL, "alert", new Node[0]);

    ifStatementNode.addChild(ifConditionNode);
    ifStatementNode.addChild(returnStatementNode);
    ifStatementNode.addChild(alertStatementNode);
    functionNode.addChild(ifStatementNode);
    functionNode.addChild(unreachableCodeNode);
    root.addChild(functionNode);

    cfa.process(null, root);
    cfg = cfa.getCfg();

    new GraphReachability<Node, ControlFlowGraph.Branch>(cfg)
        .compute(cfg.getEntry().getValue());

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);

    Node functionNodeAfterProcessing = NodeTraversal.findFunction(root, "test");
    Node unreachableCodeNodeAfterProcessing =
        NodeTraversal.findNode(functionNodeAfterProcessing, unreachableCodeNode);

    assertFalse("UnreachableCode node should be removed",
        functionNodeAfterProcessing.getChildIndex(unreachableCodeNodeAfterProcessing) != -1);
  }

  @Test
  public void testRemoveNoOpStatements() {
    Node root = new Node(Token.NULL);
    Node functionNode = new Node(Token.FUNCTION, "test", new Node[0]);
    Node expressionStatementNode1 = new Node(Token.ASSIGN, null, null);
    Node expressionStatementNode2 = new Node(Token.ASSIGN, null, null);
    Node assignmentExpressionNode = new Node(Token.IDENT, "x");
    Node constantExpressionNode = new Node(Token.NUMBER, 1);

    expressionStatementNode1.addChild(assignmentExpressionNode);
    expressionStatementNode1.addChild(constantExpressionNode);
    functionNode.addChild(expressionStatementNode1);
    functionNode.addChild(expressionStatementNode2);
    root.addChild(functionNode);

    cfa.process(null, root);
    cfg = cfa.getCfg();

    new GraphReachability<Node, ControlFlowGraph.Branch>(cfg)
        .compute(cfg.getEntry().getValue());

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, true);
    pass.process(null, root);

    Node functionNodeAfterProcessing = NodeTraversal.findFunction(root, "test");
    Node expressionStatementNode1AfterProcessing =
        NodeTraversal.findNode(functionNodeAfterProcessing, expressionStatementNode1);

    assertTrue("Expression statement node 1 should be removed",
        expressionStatementNode1AfterProcessing == null);
  }

  @Test(expected = AssertionError.class)
  public void testFunctionNodeAfterProcessingIsNull() {
    Node root = new Node(Token.NULL);
    Node functionNode = new Node(Token.FUNCTION, "test", new Node[0]);
    Node returnStatementNode = new Node(Token.RETURN, null, null);
    Node unreachableCodeNode = new Node(Token.CALL, "alert", new Node[0]);

    functionNode.addChild(returnStatementNode);
    functionNode.addChild(unreachableCodeNode);
    root.addChild(functionNode);

    cfa.process(null, root);
    cfg = cfa.getCfg();

    new GraphReachability<Node, ControlFlowGraph.Branch>(cfg)
        .compute(cfg.getEntry().getValue());

    UnreachableCodeElimination pass = new UnreachableCodeElimination(compiler, false);
    pass.process(null, root);

    Node functionNodeAfterProcessing = NodeTraversal.findFunction(root, "test");
    Node unreachableCodeNodeAfterProcessing =
        NodeTraversal.findNode(functionNodeAfterProcessing, unreachableCodeNode);

    assertFalse("UnreachableCode node should be removed",
        functionNodeAfterProcessing.getChildIndex(unreachableCodeNodeAfterProcessing) != -1);

    // Assertion error should be thrown because functionNodeAfterProcessing is null.
    Node functionNodeAfterProcessingNull = NodeTraversal.findNode(root, functionNodeAfterProcessing);
    assertTrue("Function node after processing should not be null", functionNodeAfterProcessingNull != null);
  }
}