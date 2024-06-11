import static com.google.javascript.jscomp.MinimizeExitPoints.Token.*;
import static com.google.javascript.rhino.Token.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Before;
import org.junit.Test;

public class MinimizeExitPointsTest {

  private Compiler compiler;
  private NodeFactory nodeFactory;
  private MinimizeExitPoints minimizeExitPoints;

  @Before
  public void setUp() {
    compiler = mock(Compiler.class);
    nodeFactory = NodeFactory.getInitial();
    minimizeExitPoints = new MinimizeExitPoints(compiler);
  }

  @Test
  public void testMinimizeLabelExit() {
    Node root = NodeUtil.createLabelNode("loop", new IR.StatementList());
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    minimizeExitPoints.visit(traversal, root, null);

    Node exitNode = NodeUtil.createBreakStatement("loop");
    Node exitParent = NodeUtil.createStatementList();
    exitParent.addChild(exitNode);
    root.getLastChild().replaceChild(
        root.getLastChild().getLastChild(), exitParent);

    minimizeExitPoints.visit(traversal, root, null);

    assertNull(root.getLastChild().getLastChild());
    verify(compiler, times(1)).reportCodeChange();
  }

  @Test
  public void testMinimizeIfExit() {
    Node root = NodeUtil.createBlock("ifStatement", new IR.StatementList());
    Node ifNode = NodeUtil.createIfNode();
    Node trueBlock = NodeUtil.createBlock("trueBlock", new IR.StatementList());
    Node falseBlock = NodeUtil.createBlock("falseBlock", new IR.StatementList());
    Node exitNode = NodeUtil.createReturnNode();

    root.addChild(ifNode);
    ifNode.addChild(trueBlock);
    trueBlock.addChild(exitNode);
    ifNode.addChild(falseBlock);

    minimizeExitPoints.visit(mock(NodeTraversal.class), root, null);

    verify(compiler, times(1)).reportCodeChange();
    assertEquals(falseBlock, root.getLastChild());
  }

  @Test
  public void testMinimizeIfWithElseExit() {
    Node root = NodeUtil.createBlock("ifStatement", new IR.StatementList());
    Node ifNode = NodeUtil.createIfNode();
    Node trueBlock = NodeUtil.createBlock("trueBlock", new IR.StatementList());
    Node falseBlock = NodeUtil.createBlock("falseBlock", new IR.StatementList());
    Node exitNodeTrue = NodeUtil.createReturnNode();
    Node exitNodeFalse = NodeUtil.createLabelNode("elseLabel", new IR.StatementList());

    root.addChild(ifNode);
    ifNode.addChild(trueBlock);
    trueBlock.addChild(exitNodeTrue);
    ifNode.addChild(falseBlock);
    falseBlock.addChild(exitNodeFalse);

    minimizeExitPoints.visit(mock(NodeTraversal.class), root, null);

    verify(compiler, times(1)).reportCodeChange();
    assertEquals(exitNodeFalse, root.getLastChild());
  }

  @Test
  public void testMinimizeLoopExit() {
    Node root = NodeUtil.createLabelNode("loop", new IR.StatementList());
    Node loopCodeBlock = NodeUtil.createBlock("loopBlock", new IR.StatementList());
    Node exitNode = NodeUtil.createBreakStatement();

    root.addChild(loopCodeBlock);
    loopCodeBlock.addChild(exitNode);

    minimizeExitPoints.visit(mock(NodeTraversal.class), root, null);

    verify(compiler, times(1)).reportCodeChange();
    assertNull(root.getLastChild().getLastChild());
  }

  @Test
  public void testMinimizeDoWhileLoopExit() {
    Node root = NodeUtil.createLabelNode("loop", new IR.StatementList());
    Node doWhileCodeBlock = NodeUtil.createBlock("doWhileBlock", new IR.StatementList());
    Node condition = NodeUtil.createBooleanLiteral(false);
    Node exitNode = NodeUtil.createBreakStatement();

    root.addChild(doWhileCodeBlock);
    doWhileCodeBlock.addChild(condition);
    doWhileCodeBlock.addChild(exitNode);

    minimizeExitPoints.visit(mock(NodeTraversal.class), root, null);

    verify(compiler, times(1)).reportCodeChange();
    assertNull(root.getLastChild().getLastChild());
  }

  @Test
  public void testMinimizeFunctionExit() {
    Node root = NodeUtil.createFunctionNode("function", new IR.StatementList());
    Node exitNode = NodeUtil.createReturnNode();

    root.getLastChild().addChild(exitNode);

    minimizeExitPoints.visit(mock(NodeTraversal.class), root, null);

    verify(compiler, times(1)).reportCodeChange();
    assertNull(root.getLastChild().getLastChild());
  }
}