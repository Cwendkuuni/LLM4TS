import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.CallbackFlag.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ScopedCallback;
import com.google.javascript.jscomp.graph.ControlFlowGraph;
import com.google.javascript.jscomp.graph.ControlFlowAnalysis;
import com.google.javascript.jscomp.graph.DiGraph;
import com.google.javascript.jscomp.graph.DiGraphEdge;
import com.google.javascript.jscomp.graph.DiGraphNode;
import com.google.javascript.rhino.Token;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

/**
 * Test class for UnreachableCodeElimination class.
 */
public class UnreachableCodeEliminationTest {

  @Test
  public void testUnreachableCodeElimination() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    UnreachableCodeElimination elimination = new UnreachableCodeElimination(compiler, true);

    Node root = Node.newBlock(null, null);
    Node a = Node.newLocal("a", INT, root);
    Node b = Node.newLocal("b", INT, root);
    Node c = Node.newLocal("c", INT, root);
    Node d = Node.newLocal("d", INT, root);

    Node initA = Node.newAssignment(a, Node.newNumber(1), root);
    Node initB = Node.newAssignment(b, Node.newNumber(2), root);

    Node assignment = Node.newAssignment(c, Node.newName("x"), root);
    Node ifStatement = Node.newIfStatement(Node.newBinaryOp(COMPARE, c, Token.LT, Node.newNumber(0), root),
        Node.newBlock(null, null), root);
    Node returnStatement = Node.newReturnStatement(root);

    Node ifBody = Node.newBlock(null, null);
    Node deadCode = Node.newAssignment(d, Node.newName("unreachable"), ifBody);

    Node elseBody = Node.newBlock(null, null);
    Node elseStatement = Node.newIfStatement(Node.newBooleanLiteral(false), elseBody, root);

    Node thenStatement = Node.newExpressionStatement(assignment, root);
    Node elseThenStatement = Node.newExpressionStatement(Node.newAssignment(d, Node.newNumber(3), elseBody), root);

    Node ifStatementAssign = Node.newBlock(null, null);
    Node ifStatementAssignBody = Node.newBlock(null, null);
    Node ifStatementAssignIf = Node.newIfStatement(Node.newBinaryOp(COMPARE, c, Token.LT, Node.newNumber(0), root),
        ifStatementAssignBody, ifStatementAssign);
    Node ifStatementAssignElse = Node.newBlock(null, null);
    Node ifStatementAssignElseThen = Node.newReturnStatement(ifStatementAssign);

    Node block = Node.newBlock(null, null);
    Node initialize = Node.newBlock(null, null);
    Node initialization = Node.newBlock(null, null);
    Node body = Node.newBlock(null, null);

    Node initializeBody = Node.newBlock(null, null);
    initializeBody.addChild(initA);
    initializeBody.addChild(initB);
    initialize.addChild(initializeBody);

    body.addChild(assignment);
    body.addChild(ifStatement);
    body.addChild(elseStatement);
    body.addChild(returnStatement);

    ifStatementAssignBody.addChild(thenStatement);
    ifStatementAssignBody.addChild(elseThenStatement);
    ifStatementAssignElse.addChild(elseBody);
    ifStatementAssignElse.addChild(elseThenStatement);
    ifStatementAssignBody.addChild(ifStatementAssignElse);

    initialization.addChild(initialize);
    initialization.addChild(body);
    initialization.addChild(ifStatementAssign);

    block.addChild(initialization);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler, root, new EliminateDeadCodeCallback());
    nodeTraversal.traverse(elimination);

    // Check that dead code is removed.
    assertFalse("Dead code 'd' should not exist after elimination.", elimination.curCfg.containsNode(d));
  }

  private static class EliminateDeadCodeCallback extends NodeTraversal.AbstractPostOrderCallback implements CompilerPass {

    @Override
    public void process(Node externs, Node root) {
      getCallback().process(externs, root);
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (parent == null || (!n.getType().isStatement() || NodeUtil.mayHaveSideEffects(n))) {
        return;
      }

      // Check if node is dead code.
      DiGraphNode<Node, Branch> gNode = elimination.curCfg.getDirectedGraphNode(n);
      if (gNode != null && !gNode.isReachable()) {
        // Remove dead code.
        Node parentStatement = n.getParent();
        NodeUtil.removeChild(parentStatement, n);
      }
    }
  }
}