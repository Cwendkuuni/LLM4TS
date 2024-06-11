package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class FunctionRewriterTest {

  private static Node createConstantFunctionNode(int value) {
    NodeFactory nodeFactory = new NodeFactory();
    Node functionNode = nodeFactory.createFunction(null, null, null, false);
    Node body = nodeFactory.createBlock(null, null);
    body.addChildToBack(nodeFactory.createReturnStatement(
        nodeFactory.createNumberLiteral(value)));
    functionNode.setBody(body);
    return functionNode;
  }

  private static Node createIdentityFunctionNode(String varName) {
    NodeFactory nodeFactory = new NodeFactory();
    Node functionNode = nodeFactory.createFunction(
        null, null, ImmutableList.of(nodeFactory.createIdentifier(varName)),
        false);
    Node body = functionNode.getBody();
    Node returnStatement = NodeUtil.createReturnStatement(
        NodeUtil.createName(varName), null, body);
    body.replaceChild(0, returnStatement);
    return functionNode;
  }

  private static Node createGetterNode(String propName) {
    NodeFactory nodeFactory = new NodeFactory();
    Node functionNode = nodeFactory.createFunction(
        null, null, null, false);
    Node body = functionNode.getBody();
    Node getPropNode = NodeUtil.createGetProperty(
        NodeUtil.createThis(), NodeUtil.createIdentifier(propName));
    Node returnStatement = NodeUtil.createReturnStatement(getPropNode, null,
        body);
    body.replaceChild(0, returnStatement);
    return functionNode;
  }

  private static Node createSetterNode(String propName) {
    NodeFactory nodeFactory = new NodeFactory();
    Node functionNode = nodeFactory.createFunction(
        null, null, ImmutableList.of(nodeFactory.createIdentifier("value")),
        false);
    Node body = functionNode.getBody();
    Node setPropNode = NodeUtil.createSetProperty(
        NodeUtil.createThis(), NodeUtil.createIdentifier(propName),
        NodeUtil.createIdentifier("value"));
    Node returnStatement = NodeUtil.createReturnStatement(null, null, body);
    body.replaceChild(0, returnStatement);
    return functionNode;
  }

  private static Node createEmptyFunctionNode() {
    NodeFactory nodeFactory = new NodeFactory();
    Node functionNode = nodeFactory.createFunction(
        null, null, null, false);
    return functionNode;
  }

  @Test
  public void testConstantFunctionReduction() {
    FunctionRewriter rewriter = new FunctionRewriter(null);

    Node functionNode = createConstantFunctionNode(10);
    Node rootNode = new NodeFactory().createRootNode();
    rootNode.addChildToBack(functionNode);

    rewriter.process(null, rootNode);

    Node expectedNode = rewriter.parseHelperCode(EmptyFunctionReducer.class)
        .getNext();
    assertEquals(expectedNode.getString(), functionNode.getString());
  }

  @Test
  public void testIdentityFunctionReduction() {
    FunctionRewriter rewriter = new FunctionRewriter(null);

    Node functionNode = createIdentityFunctionNode("x");
    Node rootNode = new NodeFactory().createRootNode();
    rootNode.addChildToBack(functionNode);

    rewriter.process(null, rootNode);

    Node expectedNode = rewriter.parseHelperCode(IdentityReducer.class)
        .getNext();
    assertEquals(expectedNode.getString(), functionNode.getString());
  }

  @Test
  public void testGetterFunctionReduction() {
    FunctionRewriter rewriter = new FunctionRewriter(null);

    Node functionNode = createGetterNode("prop");
    Node rootNode = new NodeFactory().createRootNode();
    rootNode.addChildToBack(functionNode);

    rewriter.process(null, rootNode);

    Node expectedNode = rewriter.parseHelperCode(GetterReducer.class)
        .getNext();
    assertEquals(expectedNode.getString(), functionNode.getString());
  }

  @Test
  public void testSetterFunctionReduction() {
    FunctionRewriter rewriter = new FunctionRewriter(null);

    Node functionNode = createSetterNode("prop");
    Node rootNode = new NodeFactory().createRootNode();
    rootNode.addChildToBack(functionNode);

    rewriter.process(null, rootNode);

    Node expectedNode = rewriter.parseHelperCode(SetterReducer.class)
        .getNext();
    assertEquals(expectedNode.getString(), functionNode.getString());
  }

  @Test
  public void testEmptyFunctionReduction() {
    FunctionRewriter rewriter = new FunctionRewriter(null);

    Node functionNode = createEmptyFunctionNode();
    Node rootNode = new NodeFactory().createRootNode();
    rootNode.addChildToBack(functionNode);

    rewriter.process(null, rootNode);

    assertNotNull(rewriter.parseHelperCode(EmptyFunctionReducer.class));
  }

  @Test
  public void testFunctionReductionWithSavings() {
    FunctionRewriter rewriter = new FunctionRewriter(null);

    // Create a big function that has savings.
    String bigFunctionSource = "function b(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z) { }";
    Node bigFunctionNode = new NodeFactory().parseCompilationUnit(bigFunctionSource);

    Node rootNode = new NodeFactory().createRootNode();
    rootNode.addChildToBack(bigFunctionNode);

    rewriter.process(null, rootNode);

    int savings = rewriter.getSavings();
    assert savings > 0;
  }

  @Test
  public void testFunctionReductionWithNoSavings() {
    FunctionRewriter rewriter = new FunctionRewriter(null);

    // Create a small function that has no savings.
    Node smallFunctionNode = createConstantFunctionNode(1);
    Node rootNode = new NodeFactory().createRootNode();
    rootNode.addChildToBack(smallFunctionNode);

    rewriter.process(null, rootNode);

    assertNotNull(rewriter.parseHelperCode(EmptyFunctionReducer.class));

    int savings = rewriter.getSavings();
    assertEquals(savings, 0);
  }
}