import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.ArrayList;
import java.util.List;

public class FunctionRewriterTest {

  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    nodeFactory = mock(NodeFactory.class);
  }

  @org.junit.Test
  public void testEmptyFunctionReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = nodeFactory.createNode(Token.BLOCK, 1, 1);
    Node emptyFunction = nodeFactory.createNode(Token.FUNCTION, 1, 1);
    emptyFunction.setProperty("prototype", createPrototype(nodeFactory));
    emptyFunction.setProperty("prototype.test", emptyFunction);

    Node result = functionRewriter.process(null, root);
    Node expected = nodeFactory.createCall(
        "JSCompiler_emptyFn", null, 1, 1);
    Node test = result.getFirstChild();
    assertEquals(expected, test);
  }

  @org.junit.Test
  public void testIdentityFunctionReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = nodeFactory.createNode(Token.BLOCK, 1, 1);
    Node identityFunction = nodeFactory.createNode(
        Token.FUNCTION, 1, 1, "test", null, ImmutableList.of(), null);
    identityFunction.setProperty("prototype", createPrototype(nodeFactory));
    identityFunction.setProperty("prototype.test", identityFunction);
    Node argument = nodeFactory.createNode(Token.IDENT, 1, 1, "arg");
    Node body = nodeFactory.createNode(Token.RETURN, 1, 1);
    body.addChildToBack(argument);
    identityFunction.setLastChild(body);

    Node result = functionRewriter.process(null, root);
    Node expected = nodeFactory.createCall(
        "JSCompiler_identityFn", argument, 1, 1);
    Node test = result.getFirstChild();
    assertEquals(expected, test);
  }

  @org.junit.Test
  public void testReturnConstantReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = nodeFactory.createNode(Token.BLOCK, 1, 1);
    Node constantFunction = nodeFactory.createNode(
        Token.FUNCTION, 1, 1, "test", null, ImmutableList.of(), null);
    constantFunction.setProperty("prototype", createPrototype(nodeFactory));
    constantFunction.setProperty("prototype.test", constantFunction);
    Node constant = nodeFactory.createNode(Token.NUMBER, 1, 1, 10);
    Node body = nodeFactory.createNode(Token.RETURN, 1, 1);
    body.addChildToBack(constant);
    constantFunction.setLastChild(body);

    Node result = functionRewriter.process(null, root);
    Node expected = nodeFactory.createCall(
        "JSCompiler_returnArg", constant, 1, 1);
    Node test = result.getFirstChild();
    assertEquals(expected, test);
  }

  @org.junit.Test
  public void testGetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = nodeFactory.createNode(Token.BLOCK, 1, 1);
    Node getterFunction = nodeFactory.createNode(
        Token.FUNCTION, 1, 1, "test", null, ImmutableList.of(), null);
    getterFunction.setProperty("prototype", createPrototype(nodeFactory));
    getterFunction.setProperty("prototype.test", getterFunction);
    Node getterProperty = nodeFactory.createNode(
        Token.GETPROP, 1, 1, "test_", getterFunction);
    Node body = nodeFactory.createNode(Token.RETURN, 1, 1);
    body.addChildToBack(getterProperty);
    getterFunction.setLastChild(body);

    Node result = functionRewriter.process(null, root);
    Node expected = nodeFactory.createCall(
        "JSCompiler_get", getterProperty, 1, 1);
    Node test = result.getFirstChild();
    assertEquals(expected, test);
  }

  @org.junit.Test
  public void testSetterReducer() {
    FunctionRewriter functionRewriter = new FunctionRewriter(compiler);
    Node root = nodeFactory.createNode(Token.BLOCK, 1, 1);
    Node setterFunction = nodeFactory.createNode(
        Token.FUNCTION, 1, 1, "test", null, ImmutableList.of(), null);
    setterFunction.setProperty("prototype", createPrototype(nodeFactory));
    setterFunction.setProperty("prototype.test", setterFunction);
    Node setterProperty = nodeFactory.createNode(
        Token.GETPROP, 1, 1, "test_", setterFunction);
    Node argument = nodeFactory.createNode(Token.IDENT, 1, 1, "value");
    Node assign = nodeFactory.createNode(Token.ASSIGN, 1, 1);
    assign.addChildToBack(setterProperty);
    assign.addChildToBack(argument);
    Node body = nodeFactory.createNode(Token.BLOCK, 1, 1);
    body.addChildToBack(assign);
    setterFunction.setLastChild(body);

    Node result = functionRewriter.process(null, root);
    Node expected = nodeFactory.createCall(
        "JSCompiler_set", setterProperty, argument, 1, 1);
    Node test = result.getFirstChild();
    assertEquals(expected, test);
  }

  // Test helper methods

  private Node createPrototype(NodeFactory nodeFactory) {
    return nodeFactory.createNode(
        Token.OBJECT, 1, 1, null, ImmutableList.of(), null);
  }

  private boolean isReduceableFunctionExpression(Node node) {
    return NodeUtil.isFunctionExpression(node);
  }

  private List<CompilerPass> getReducers() {
    return ImmutableList.of(
        new FunctionRewriter.EmptyFunctionReducer(),
        new FunctionRewriter.IdentityReducer(),
        new FunctionRewriter.ReturnConstantReducer(),
        new FunctionRewriter.GetterReducer(),
        new FunctionRewriter.SetterReducer());
  }
}