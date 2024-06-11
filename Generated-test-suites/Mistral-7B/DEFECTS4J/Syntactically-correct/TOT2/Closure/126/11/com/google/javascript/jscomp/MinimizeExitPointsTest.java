import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeUtil.*;
import static com.google.javascript.rhino.Token.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.MinimizeExitPoints;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Test;

public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExits() {
    Compiler compiler = new Compiler();
    Node root = NodeFactory.createProgram();

    // Test an empty block.
    Node emptyBlock = NodeFactory.createBlock();
    MinimizeExitPoints minimizer = new MinimizeExitPoints(compiler);
    minimizer.process(null, root);
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals(0, emptyBlock.getChildCount());

    // Test a block with a single return statement.
    Node singleReturnBlock = NodeFactory.createBlock();
    singleReturnBlock.addChild(NodeFactory.createReturnStatement(null));
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals(0, singleReturnBlock.getChildCount());

    // Test a block with a single break statement.
    Node singleBreakBlock = NodeFactory.createBlock();
    singleBreakBlock.addChild(NodeFactory.createBreakStatement(null));
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals(0, singleBreakBlock.getChildCount());

    // Test a block with a single continue statement.
    Node singleContinueBlock = NodeFactory.createBlock();
    singleContinueBlock.addChild(NodeFactory.createContinueStatement(null));
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals(0, singleContinueBlock.getChildCount());

    // Test a block with multiple statements and a return statement.
    Node multipleStatementsReturnBlock = NodeFactory.createBlock();
    multipleStatementsReturnBlock.addChild(NodeFactory.createStatement(null));
    multipleStatementsReturnBlock.addChild(NodeFactory.createStatement(null));
    multipleStatementsReturnBlock.addChild(NodeFactory.createReturnStatement(null));
    NodeTraversal.traverse(compiler, root, minimizer);
    Node lastChild = multipleStatementsReturnBlock.getLastChild();
    assertEquals(lastChild.getType(), RETURN);

    // Test a block with multiple statements and a break statement.
    Node multipleStatementsBreakBlock = NodeFactory.createBlock();
    multipleStatementsBreakBlock.addChild(NodeFactory.createStatement(null));
    multipleStatementsBreakBlock.addChild(NodeFactory.createStatement(null));
    multipleStatementsBreakBlock.addChild(NodeFactory.createLabelStatement("label"));
    multipleStatementsBreakBlock.addChild(NodeFactory.createBlock());
    multipleStatementsBreakBlock.addChild(NodeFactory.createBreakStatement("label"));
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals(0, multipleStatementsBreakBlock.getLastChild().getChildCount());

    // Test a block with multiple statements and a continue statement.
    Node multipleStatementsContinueBlock = NodeFactory.createBlock();
    multipleStatementsContinueBlock.addChild(NodeFactory.createStatement(null));
    multipleStatementsContinueBlock.addChild(NodeFactory.createStatement(null));
    multipleStatementsContinueBlock.addChild(NodeFactory.createLabelStatement("label"));
    multipleStatementsContinueBlock.addChild(NodeFactory.createBlock());
    multipleStatementsContinueBlock.addChild(NodeFactory.createContinueStatement("label"));
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals(0, multipleStatementsContinueBlock.getLastChild().getChildCount());

    // Test a block with a label and a return statement.
    Node labelReturnBlock = NodeFactory.createBlock();
    labelReturnBlock.addChild(NodeFactory.createLabelStatement("label"));
    labelReturnBlock.addChild(NodeFactory.createBlock());
    labelReturnBlock.addChild(NodeFactory.createLabelStatement("label"));
    labelReturnBlock.addChild(NodeFactory.createReturnStatement(null));
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals(0, labelReturnBlock.getLastChild().getChildCount());

    // Test a block with a label and a break statement.
    Node labelBreakBlock = NodeFactory.createBlock();
    labelBreakBlock.addChild(NodeFactory.createLabelStatement("label"));
    labelBreakBlock.addChild(NodeFactory.createBlock());
    labelBreakBlock.addChild(NodeFactory.createLabelStatement("label"));
    labelBreakBlock.addChild(NodeFactory.createBreakStatement("label"));
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals(0, labelBreakBlock.getLastChild().getChildCount());

    // Test a block with a label and a continue statement.
    Node labelContinueBlock = NodeFactory.createBlock();
    labelContinueBlock.addChild(NodeFactory.createLabelStatement("label"));
    labelContinueBlock.addChild(NodeFactory.createBlock());
    labelContinueBlock.addChild(NodeFactory.createLabelStatement("label"));
    labelContinueBlock.addChild(NodeFactory.createContinueStatement("label"));
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals(0, labelContinueBlock.getLastChild().getChildCount());

    // Test a block with an if statement that has a break statement.
    Node ifBreakBlock = NodeFactory.createBlock();
    ifBreakBlock.addChild(NodeFactory.createIfStatement(null, NodeFactory.createBlock()));
    ifBreakBlock.addChild(NodeFactory.createLabelStatement("label"));
    ifBreakBlock.addChild(NodeFactory.createBlock());
    ifBreakBlock.addChild(NodeFactory.createBreakStatement("label"));
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals(0, ifBreakBlock.getLastChild().getChildCount());

    // Test a block with an if statement that has a return statement.
    Node ifReturnBlock = NodeFactory.createBlock();
    ifReturnBlock.addChild(NodeFactory.createIfStatement(null, NodeFactory.createReturnStatement(null)));
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals(0, ifReturnBlock.getLastChild().getChildCount());

    // Test a block with multiple statements and a try statement.
    Node tryBlock = NodeFactory.createBlock();
    tryBlock.addChild(NodeFactory.createStatement(null));
    tryBlock.addChild(NodeFactory.createStatement(null));
    tryBlock.addChild(NodeFactory.createTryStatement(null));
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals(0, tryBlock.getLastChild().getChildCount());

    // Test a block with multiple statements and a try statement with a catch
    // block.
    Node tryCatchBlock = NodeFactory.createBlock();
    tryCatchBlock.addChild(NodeFactory.createStatement(null));
    tryCatchBlock.addChild(NodeFactory.createStatement(null));
    tryCatchBlock.addChild(NodeFactory.createTryStatement(null));
    tryCatchBlock.addChild(NodeFactory.createCatchStatement(null));
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals(0, tryCatchBlock.getLastChild().getChildCount());

    // Test a block with multiple statements and a try statement with a finally
    // block.
    Node tryFinallyBlock = NodeFactory.createBlock();
    tryFinallyBlock.addChild(NodeFactory.createStatement(null));
    tryFinallyBlock.addChild(NodeFactory.createStatement(null));
    tryFinallyBlock.addChild(NodeFactory.createTryStatement(null));
    tryFinallyBlock.addChild(NodeFactory.createFinallyStatement(null));
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals(0, tryFinallyBlock.getLastChild().getChildCount());

    // Test a block with multiple statements and a switch statement.
    Node switchBlock = NodeFactory.createBlock();
    switchBlock.addChild(NodeFactory.createStatement(null));
    switchBlock.addChild(NodeFactory.createStatement(null));
    switchBlock.addChild(NodeFactory.createSwitchStatement(null));
    NodeTraversal.traverse(compiler, root, minimizer);
    assertEquals(0, switchBlock.getLastChild().getChildCount());
  }
}