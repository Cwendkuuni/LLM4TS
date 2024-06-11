package com.google.javascript.jscomp.FunctionRewriterTest;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FunctionRewriterTest {

  private static Node createFunctionNode(String functionText) {
    AbstractCompiler compiler = new AbstractCompiler();
    Node externs = NodeFactory.createRoot();
    Node root = NodeFactory.createRoot();
    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(externs, root);
    Node code = NodeFactory.newText(functionText).getFirstChild();
    Node function = NodeTraversal.traverse(compiler, root, new FunctionFinder(), code);
    return function;
  }

  private static class FunctionFinder implements NodeTraversal.Callback {
    @Override
    public boolean shouldTraverse(NodeTraversal traverser, Node node, Node parent) {
      return node.getType() == Token.FUNCTION;
    }

    @Override
    public void visit(NodeTraversal traversal, Node node, Node parent) {
      assertEquals("Test function", node.getString(), parent.getFirstChild().getString());
    }
  }

  @Test
  public void testEmptyFunction() {
    Node function = createFunctionNode("function() {}");
    Node expected = Node.newString(Token.NAME, "emptyFn");
    assertEquals("Empty function reduced to helper method call: " + expected,
                 expected, function);
  }

  @Test
  public void testIdentityFunction() {
    Node function = createFunctionNode("function(a) {return a}");
    Node expected = Node.newString(Token.NAME, "identityFn");
    assertEquals("Identity function reduced to helper method call: " + expected,
                 expected, function);
  }

  @Test
  public void testReturnConstant() {
    Node function = createFunctionNode("function() {return 10}");
    Node expected = Node.newString(Token.NAME, "returnconst");
    assertEquals("Constant function reduced to helper method call: " + expected,
                 expected, function);
  }

  @Test
  public void testGetter() {
    Node function = createFunctionNode("function() {return this.a}");
    Node expected = Node.newString(Token.NAME, "getter");
    assertEquals("Getter function reduced to helper method call: " + expected,
                 expected, function);
  }

  @Test
  public void testSetter() {
    Node function = createFunctionNode("function(value) {this.a = value}");
    Node expected = Node.newString(Token.NAME, "setter");
    assertEquals("Setter function reduced to helper method call: " + expected,
                 expected, function);
  }

  @Test
  public void testEmptyFunctionWithArguments() {
    Node function = createFunctionNode("function(a, b, c) {}");
    assertNull("Empty function with arguments should not be reduced", function);
  }

  @Test
  public void testFunctionWithReturnAndAssign() {
    Node function = createFunctionNode("function(a) {var b = a; return b; }");
    Node expected = Node.newString(Token.NAME, "returnconst");
    assertEquals("Function with return statement and assign should be reduced: " + expected,
                 expected, function);
  }

  @Test
  public void testFunctionWithReturnAndReturn() {
    Node function = createFunctionNode("function(a) {return a; return 10; }");
    Node expected = Node.newString(Token.NAME, "returnconst");
    assertEquals("Function with multiple return statements should be reduced: " + expected,
                 expected, function);
  }

  @Test
  public void testFunctionWithComplexBody() {
    Node function = createFunctionNode("function(a) {" +
        "  var b = a * 2;" +
        "  var c = b + 5;" +
        "  return c;" +
        "}");
    assertNull("Function with complex body should not be reduced", function);
  }
}