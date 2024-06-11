package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.compiler.AbstractCompiler;
import com.google.javascript.jscomp.compiler.CompilerPass;
import com.google.javascript.jscomp.compiler.CompilerPhase;
import com.google.javascript.jscomp.compiler.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.TernaryValue;

/**
 * Test cases for {@link MinimizeExitPoints}.
 */
public class MinimizeExitPointsTest {

  @Test
  public void testMinimizeExitPoints() {
    AbstractCompiler compiler = new Compiler();
    MinimizeExitPoints minimizeExitPoints = new MinimizeExitPoints(compiler);

    Node program = Node.newProgram();
    Node root = NodeUtil.newFunctionDeclaration(
        "test", NodeUtil.newBlock(program), NodeUtil.newBlock(program));
    program.addChildToBack(root);

    Node ifBlock = NodeUtil.newIfStatement(
        NodeUtil.newBooleanLiteral(true), NodeUtil.newBlock(program),
        NodeUtil.newBlock(program));
    Node ifStatement = NodeUtil.newStatement(NodeUtil.newExpressionStatement(
        NodeUtil.newAssignment(NodeUtil.newVariable("x"),
            NodeUtil.newNumberLiteral(1))), ifBlock);
    root.addChildToBack(ifStatement);

    Node ifElseBlock = NodeUtil.newBlock(program);
    Node ifElseStatement = NodeUtil.newStatement(NodeUtil.newExpressionStatement(
        NodeUtil.newAssignment(NodeUtil.newVariable("y"),
            NodeUtil.newNumberLiteral(2))), ifElseBlock);
    ifBlock.addChildToBack(ifElseStatement);

    Node elseBlock = NodeUtil.newBlock(program);
    Node elseStatement = NodeUtil.newStatement(NodeUtil.newExpressionStatement(
        NodeUtil.newAssignment(NodeUtil.newVariable("z"),
            NodeUtil.newNumberLiteral(3))), elseBlock);
    ifElseBlock.addChildToBack(elseStatement);

    Node returnStatement = NodeUtil.newStatement(NodeUtil.newExpressionStatement(
        NodeUtil.newReturnStatement(NodeUtil.newNull())), ifElseBlock);

    ifElseBlock.addChildToBack(returnStatement);
    ifBlock.addChildToBack(ifElseBlock);

    NodeTraversal.traverse(compiler, root, minimizeExitPoints);

    assertEquals( "", root.toString() );
    assertNull( findReturnStatement(root) );
    assertEquals( 3, root.getChildCount() );

    Node xAssignment = findXAssignment(root);
    Node yAssignment = findYAssignment(root);
    Node zAssignment = findZAssignment(root);

    assertEquals( 1, xAssignment.getLastChild().getChildCount() );
    assertEquals( 0, yAssignment.getLastChild().getChildCount() );
    assertEquals( 1, zAssignment.getLastChild().getChildCount() );

    Node xIfElse = findXIfElseStatement(root);
    Node xIf = xIfElse.getFirstChild();
    Node xIfElseIf = xIf.getLastChild();
    Node xElse = xIfElse.getNext();

    assertEquals( 1, xIf.getChildCount() );
    assertEquals( 1, xIfElseIf.getChildCount() );
    assertEquals( 1, xElse.getChildCount() );

    assertNull( findReturnStatement(xIf) );
    assertNull( findReturnStatement(xIfElseIf) );
    assertEquals( 1, xElse.getChildCount() );
    assertNull( findReturnStatement(xElse) );
  }

  private Node findXAssignment(Node node) {
    for (Node child : node.children()) {
      if (child.isStatement() && child.getFirstChild().isAssignment()) {
        if (child.getFirstChild().getFirstChild().isVariable()
          && child.getFirstChild().getFirstChild().getString().equals("x")) {
          return child;
        }
      }
    }
    return null;
  }

  private Node findYAssignment(Node node) {
    for (Node child : node.children()) {
      if (child.isStatement() && child.getFirstChild().isAssignment()) {
        if (child.getFirstChild().getFirstChild().isVariable()
          && child.getFirstChild().getFirstChild().getString().equals("y")) {
          return child;
        }
      }
    }
    return null;
  }

  private Node findZAssignment(Node node) {
    for (Node child : node.children()) {
      if (child.isStatement() && child.getFirstChild().isAssignment()) {
        if (child.getFirstChild().getFirstChild().isVariable()
          && child.getFirstChild().getFirstChild().getString().equals("z")) {
          return child;
        }
      }
    }
    return null;
  }

  private Node findReturnStatement(Node node) {
    for (Node child : node.children()) {
      if (child.isStatement() && child.getFirstChild().isReturnStatement()) {
        return child;
      }
    }
    return null;
  }

  private Node findXIfElseStatement(Node node) {
    for (Node child : node.children()) {
      if (child.isStatement() && child.getFirstChild().isIfStatement()) {
        return child;
      }
    }
    return null;
  }

  @CompilerPass
  @CompilerPhase(CompilerPhase.TOP_LEVEL)
  private static class Compiler
      extends AbstractCompiler {
  }
}