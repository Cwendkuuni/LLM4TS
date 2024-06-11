package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.NodeUtil.isAssign;
import static com.google.javascript.jscomp.NodeUtil.isName;
import static org.junit.Assert.assertEquals;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPostOrderCallback;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.ControlFlowGraph.Branch;
import com.google.javascript.jscomp.DataFlowAnalysis.FlowState;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.ScopedCallback;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.graph.DiGraph.DiGraphNode;
import com.google.javascript.rhino.Token;
import org.junit.Test;

public class DeadAssignmentsEliminationTest {

  @Test
  public void testDeadAssignmentsElimination() {
    String sourceCode =
        "function f(x) {" +
            "var y = 1, z = 1;" +
            "x = y;" +
            "y = 2;" +
            "z = x;" +
            "return x;" +
            "}";

    AbstractCompiler compiler = new AbstractCompiler();
    CompilerPass pass = new DeadAssignmentsElimination(compiler);
    Node root = AbstractCompiler.parseAndTraverse(sourceCode, compiler);
    pass.process(compiler.getExterns(), root);

    // Check the original AST
    Node f = Node.getFunction("f", new Node[0], root);
    Node body = f.getBody();
    assertEquals(4, body.getChildCount());
    Node xAssign = body.getChild(0);
    Node yAssign1 = body.getChild(1);
    Node yAssign2 = body.getChild(2);
    Node zAssign = body.getChild(3);

    assertEquals(Token.ASSIGN, xAssign.getType());
    assertEquals(Token.NAME, xAssign.getFirstChild().getType());
    assertEquals("y", xAssign.getFirstChild().getString());

    assertEquals(Token.ASSIGN, yAssign1.getType());
    assertEquals(Token.NUMBER, yAssign1.getFirstChild().getType());
    assertEquals(1.0, yAssign1.getFirstChild().getNumber().doubleValue(), 0.0);

    assertEquals(Token.ASSIGN, yAssign2.getType());
    assertEquals(Token.NUMBER, yAssign2.getFirstChild().getType());
    assertEquals(2.0, yAssign2.getFirstChild().getNumber().doubleValue(), 0.0);

    assertEquals(Token.ASSIGN, zAssign.getType());
    assertEquals(Token.NAME, zAssign.getFirstChild().getType());
    assertEquals("x", zAssign.getFirstChild().getString());

    // Check the transformed AST
    assertEquals(3, body.getChildCount());
    Node xAssignTransformed = body.getChild(0);
    Node yAssign1Transformed = body.getChild(1);
    Node zAssignTransformed = body.getChild(2);

    assertEquals(Token.ASSIGN, xAssignTransformed.getType());
    assertEquals(Token.NAME, xAssignTransformed.getFirstChild().getType());
    assertEquals("y", xAssignTransformed.getFirstChild().getString());

    assertEquals(Token.VOID, yAssign1Transformed.getType());

    assertEquals(Token.ASSIGN, zAssignTransformed.getType());
    assertEquals(Token.NAME, zAssignTransformed.getFirstChild().getType());
    assertEquals("x", zAssignTransformed.getFirstChild().getString());

    // Check the scope
    Scope scope = compiler.getScope();
    Var xVar = scope.getVar("x");
    Var yVar = scope.getVar("y");
    Var zVar = scope.getVar("z");

    assertEquals("x", xVar.name);
    assertEquals("y", yVar.name);
    assertEquals("z", zVar.name);

    assertEquals(true, xVar.isFunction());
    assertEquals(false, yVar.isFunction());
    assertEquals(false, zVar.isFunction());

    assertEquals(FlowState.LIVE, xVar.getLiveness().getIn());
    assertEquals(FlowState.LIVE, yVar.getLiveness().getOut());
    assertEquals(FlowState.KILLED, zVar.getLiveness().getOut());
  }
}

class NodeUtil {
  public static boolean containsFunction(Node node) {
    return node.getType() == Token.FUNCTION;
  }

  public static boolean isAssign(Node node) {
    return node.getType() == Token.ASSIGN;
  }

  public static boolean isName(Node node) {
    return node.getType() == Token.NAME;
  }

  public static Node getFunction(String name, Node[] args, Node root) {
    for (Node declaration = root; declaration != null; declaration = declaration.getParent()) {
      if (declaration.getType() == Token.FUNCTION) {
        Node functionNode = declaration;
        if (functionNode.getString().equals(name)) {
          Preconditions.checkArgument(functionNode.getChildCount() >= 1);
          for (int i = 0; i < args.length; i++) {
            Node argumentNode = functionNode.getChild(i + 1);
            assertEquals(Token.NAME, argumentNode.getType());
            assertEquals(args[i].toString(), argumentNode.getString());
          }
          return functionNode;
        }
      }
    }
    throw new AssertionError();
  }

  public static Node getConditionExpression(Node node) {
    switch (node.getType()) {
      case Token.IF:
      case Token.WHILE:
      case Token.DO:
      case Token.FOR:
      case Token.SWITCH:
      case Token.CASE:
      case Token.RETURN:
        return node.getFirstChild();
      default:
        return null;
    }
  }
}