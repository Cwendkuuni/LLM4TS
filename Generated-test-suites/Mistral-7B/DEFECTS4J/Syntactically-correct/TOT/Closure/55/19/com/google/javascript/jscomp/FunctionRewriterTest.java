package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class FunctionRewriterTest {

  @Test
  public void testEmptyFunctionReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new NoOpAbstractCompiler());
    Node root = NodeFactory.createProgram();
    Node emptyFunction = NodeFactory.createFunction(
        "emptyFunction",
        ImmutableList.of(),
        NodeFactory.createBlock(NodeFactory.createNull()));
    root.addChildToBack(emptyFunction);

    functionRewriter.process(null, root);

    Node reducedFunction = findNodeByVariableName(root, "emptyFunction");
    assertNotNull(reducedFunction);
    Node expectedHelperCode = NodeFactory.createCall(
        NodeFactory.createIdent("emptyFn"), ImmutableList.of());
    assertEquals(expectedHelperCode, reducedFunction.getFirstChild());
  }

  @Test
  public void testIdentityFunctionReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new NoOpAbstractCompiler());
    Node root = NodeFactory.createProgram();
    Node identityFunction = NodeFactory.createFunction(
        "identityFunction",
        ImmutableList.of(),
        NodeFactory.createBlock(NodeFactory.createReturnStatement(
            NodeFactory.createName("arg"))));
    root.addChildToBack(identityFunction);

    functionRewriter.process(null, root);

    Node reducedFunction = findNodeByVariableName(root, "identityFunction");
    assertNotNull(reducedFunction);
    Node expectedHelperCode = NodeFactory.createCall(
        NodeFactory.createIdent("identityFn"),
        ImmutableList.of(NodeFactory.createIdent("arg")));
    assertEquals(expectedHelperCode, reducedFunction.getFirstChild());
  }

  @Test
  public void testReturnConstantReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new NoOpAbstractCompiler());
    Node root = NodeFactory.createProgram();
    Node constantFunction = NodeFactory.createFunction(
        "constantFunction",
        ImmutableList.of(),
        NodeFactory.createBlock(NodeFactory.createReturnStatement(
            NodeFactory.createNumberLiteral(123))));
    root.addChildToBack(constantFunction);

    functionRewriter.process(null, root);

    Node reducedFunction = findNodeByVariableName(root, "constantFunction");
    assertNotNull(reducedFunction);
    Node expectedHelperCode = NodeFactory.createCall(
        NodeFactory.createIdent("returnArg"),
        ImmutableList.of(NodeFactory.createNumberLiteral(123)));
    assertEquals(expectedHelperCode, reducedFunction.getFirstChild());
  }

  @Test
  public void testGetterReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new NoOpAbstractCompiler());
    Node root = NodeFactory.createProgram();
    Node getterFunction = NodeFactory.createFunction(
        "getterFunction",
        ImmutableList.of(),
        NodeFactory.createBlock(NodeFactory.createReturnStatement(
            NodeFactory.createGetProp(NodeFactory.createThis(), "prop"))));
    root.addChildToBack(getterFunction);

    functionRewriter.process(null, root);

    Node reducedFunction = findNodeByVariableName(root, "getterFunction");
    assertNotNull(reducedFunction);
    Node expectedHelperCode = NodeFactory.createCall(
        NodeFactory.createIdent("getter"),
        ImmutableList.of(NodeFactory.createIdent("prop")));
    assertEquals(expectedHelperCode, reducedFunction.getFirstChild());
  }

  @Test
  public void testSetterReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new NoOpAbstractCompiler());
    Node root = NodeFactory.createProgram();
    Node setterFunction = NodeFactory.createFunction(
        "setterFunction",
        ImmutableList.of("value"),
        NodeFactory.createBlock(NodeFactory.createAssignStatement(
            NodeFactory.createGetProp(NodeFactory.createThis(), "prop"),
            NodeFactory.createName("value"))));
    root.addChildToBack(setterFunction);

    functionRewriter.process(null, root);

    Node reducedFunction = findNodeByVariableName(root, "setterFunction");
    assertNotNull(reducedFunction);
    Node expectedHelperCode = NodeFactory.createCall(
        NodeFactory.createIdent("setter"),
        ImmutableList.of("prop"),
        ImmutableList.of(NodeFactory.createIdent("value")));
    assertEquals(expectedHelperCode, reducedFunction.getFirstChild());
  }

  @Test
  public void testFunctionWithArgumentsReduction() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new NoOpAbstractCompiler());
    Node root = NodeFactory.createProgram();
    Node functionWithArguments = NodeFactory.createFunction(
        "functionWithArguments",
        ImmutableList.of("arg1", "arg2"),
        NodeFactory.createBlock(NodeFactory.createReturnStatement(
            NodeFactory.createBinaryOperator(
                NodeFactory.createName("arg1"),
                NodeFactory.createToken(Token.PLUS),
                NodeFactory.createName("arg2")))));
    root.addChildToBack(functionWithArguments);

    functionRewriter.process(null, root);

    Node reducedFunction = findNodeByVariableName(root, "functionWithArguments");
    assertNotNull(reducedFunction);
    Node expectedHelperCode = NodeFactory.createCall(
        NodeFactory.createIdent("functionWithArgumentsHelper"),
        ImmutableList.of(NodeFactory.createIdent("arg1"),
                          NodeFactory.createIdent("arg2")));
    assertEquals(expectedHelperCode, reducedFunction.getFirstChild());
  }

  private Node findNodeByVariableName(Node root, String variableName) {
    NodeTraversal nodeTraversal = new NodeTraversal(new AbstractCompiler()) {
      @Override
      public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        if (NodeUtil.isFunction(node)) {
          Node name = NodeUtil.getName(node.getFirstChild());
          if (name != null && name.getString().equals(variableName)) {
            return false;
          }
        }
        return true;
      }

      @Override
      public void visit(NodeTraversal traversal, Node node, Node parent) {
        if (NodeUtil.isFunction(node)) {
          Node name = NodeUtil.getName(node.getFirstChild());
          if (name != null && name.getString().equals(variableName)) {
            setFindNode(node);
          }
        }
      }
    };
    nodeTraversal.traverse(root);
    return getFindNode();
  }

  private static Node getFindNode() {
    return FunctionRewriterTest.findNode;
  }

  private static void setFindNode(Node node) {
    FunctionRewriterTest.findNode = node;
  }

  private static Node findNode;

  private static class NoOpAbstractCompiler implements AbstractCompiler {
    @Override
    public Node parseSyntheticCode(String code, int lineno, int charno) {
      return null;
    }

    @Override
    public Node parseSyntheticCode(String code, int lineno, int charno,
                                  String name) {
      return null;
    }

    @Override
    public Node parseSyntheticCode(String code, int lineno, int charno,
                                  String name, List<Node> args) {
      return null;
    }

    @Override
    public Node parseSyntheticCode(String code, int lineno, int charno,
                                  String name, List<Node> args, String name2) {
      return null;
    }

    @Override
    public Node parseSyntheticCode(String code, int lineno, int charno,
                                  String name, List<Node> args, Node node) {
      return null;
    }

    @Override
    public Node parseSyntheticCode(String code, int lineno, int charno,
                                  String name, List<Node> args, Node node, Node node2) {
      return null;
    }

    @Override
    public void reportCodeChange() {
    }

    @Override
    public Node getNodeForCodeInsertion(Node parent) {
      return null;
    }
  }
}