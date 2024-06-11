import static org.junit.Assert.*;
import static com.google.javascript.jscomp.FunctionRewriter.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeTraversal;

import java.util.List;

public class FunctionRewriterTest {

  private Compiler compiler;

  @ org.junit.Before
  public void setUp() {
    compiler = new Compiler();
  }

  @ org.junit.Test
  public void testEmptyFunctionReducer() {
    String source = "function F() {}";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();
    assertEquals(HelperName.EMPTY_FN.toString(), helperNode.getString());
  }

  @ org.junit.Test
  public void testIdentityFunctionReducer() {
    String source = "function F(arg) { return arg; }";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();
    assertEquals(HelperName.IDENTITY_FN.toString(), helperNode.getString());

    Node argumentNode = functionNode.getLastChild();
    Node argument = argumentNode.getFirstChild();
    assertEquals("arg", argument.getString());
  }

  @ org.junit.Test
  public void testConstantReturnFunctionReducer() {
    String source = "function F() { return 42; }";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();
    assertEquals(HelperName.RETURN_ARG.toString(), helperNode.getString());
    Node constantNode = functionNode.getLastChild();
    assertEquals(42, constantNode.getDoubleValue());
  }

  @ org.junit.Test
  public void testReturnConstantFunctionReducerWithNumber() {
    String source = "function F() { return 3.14; }";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();
    assertEquals(HelperName.RETURN_ARG.toString(), helperNode.getString());
    Node constantNode = functionNode.getLastChild();
    assertEquals(3.14, constantNode.getDoubleValue(), 0.001);
  }

  @ org.junit.Test
  public void testReturnConstantFunctionReducerWithString() {
    String source = "function F() { return 'hello'; }";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();
    assertEquals(HelperName.RETURN_ARG.toString(), helperNode.getString());
    Node constantNode = functionNode.getLastChild();
    assertEquals("hello", constantNode.getString());
  }

  @ org.junit.Test
  public void testReturnConstantFunctionReducerWithBoolean() {
    String source = "function F() { return true; }";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();
    assertEquals(HelperName.RETURN_ARG.toString(), helperNode.getString());
    Node constantNode = functionNode.getLastChild();
    assertTrue(constantNode.getBooleanValue());
  }

  @ org.junit.Test
  public void testReturnConstantFunctionReducerWithNull() {
    String source = "function F() { return null; }";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();
    assertEquals(HelperName.RETURN_ARG.toString(), helperNode.getString());
    Node constantNode = functionNode.getLastChild();
    assertNull(constantNode);
  }

  @ org.junit.Test
  public void testReturnConstantFunctionReducerWithEmptyObject() {
    String source = "function F() { return {}; }";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();
    assertEquals(HelperName.RETURN_ARG.toString(), helperNode.getString());
    Node constantNode = functionNode.getLastChild();
    assertTrue(constantNode.isObject());
    assertEquals(0, constantNode.getNumChildren());
  }

  @ org.junit.Test
  public void testReturnConstantFunctionReducerWithArray() {
    String source = "function F() { return [1, 2, 3]; }";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();
    assertEquals(HelperName.RETURN_ARG.toString(), helperNode.getString());
    Node constantNode = functionNode.getLastChild();
    assertArrayEquals(new int[] {1, 2, 3}, constantNode.getChildrenAsIntsArray());
  }

  @ org.junit.Test
  public void testReturnConstantFunctionReducerWithFunction() {
    String source = "function F() { return function() {}; }";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();
    assertEquals(HelperName.RETURN_ARG.toString(), helperNode.getString());
    Node constantNode = functionNode.getLastChild();
    assertTrue(constantNode.isFunction());
    assertEquals(0, constantNode.getNumChildren());
  }

  @ org.junit.Test
  public void testReturnConstantFunctionReducerWithNamedFunction() {
    String source = "function F() { return myFunction; } function myFunction() {};";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();
    assertEquals(HelperName.RETURN_ARG.toString(), helperNode.getString());
    Node constantNode = functionNode.getLastChild();
    Node namedFunctionNode = findFunctionNode(root, "myFunction");
    assertTrue(constantNode.isFunction());
    assertEquals(namedFunctionNode, constantNode.getFirstChild());
    assertEquals(0, namedFunctionNode.getNumChildren());
  }

  @ org.junit.Test
  public void testGetterReducer() {
    String source = "function F() { return this.a; }";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();
    assertEquals(HelperName.GET.toString(), helperNode.getString());
    Node propertyNode = functionNode.getLastChild();
    assertEquals("a", propertyNode.getString());
  }

  @ org.junit.Test
  public void testSetterReducer() {
    String source = "function F(value) { this.a = value; }";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();
    assertEquals(HelperName.SET.toString(), helperNode.getString());
    Node propertyNode = functionNode.getLastChild();
    assertEquals("a", propertyNode.getString());
  }

  @ org.junit.Test
  public void testFunctionWithManyStatements() {
    String source = "function F() {" +
        "this.a = 42;" +
        "this.b = this.a * 2;" +
        "return this.b;" +
        "}";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();
    assertEquals(HelperName.SET.toString(), helperNode.getLastChild().getString());
    Node returnNode = helperNode.getPrevious();
    assertEquals(HelperName.GET.toString(), returnNode.getString());
    Node propertyNode = returnNode.getPrevious();
    assertEquals("b", propertyNode.getString());
  }

  @ org.junit.Test
  public void testFunctionWithEmptyBody() {
    String source = "function F() {}";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertTrue(functionNode.isFunction());
    assertEquals(0, functionNode.getNumChildren());
  }

  @ org.junit.Test
  public void testFunctionWithOneStatement() {
    String source = "function F() { return 42; }";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();
    assertEquals(HelperName.RETURN_ARG.toString(), helperNode.getString());
    Node constantNode = functionNode.getLastChild();
    assertEquals(42, constantNode.getDoubleValue());
  }

  @ org.junit.Test
  public void testFunctionWithManyStatementsAndReturn() {
    String source = "function F() {" +
        "this.a = 42;" +
        "this.b = this.a * 2;" +
        "return this.b * 3;" +
        "}";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();
    Node returnHelperNode = helperNode.getLastChild();
    assertEquals(HelperName.MUL.toString(), returnHelperNode.getLastChild().getString());
    Node propertyNode = returnHelperNode.getPrevious();
    assertEquals(HelperName.GET.toString(), propertyNode.getString());
    Node returnPropertyNode = propertyNode.getPrevious();
    assertEquals("b", returnPropertyNode.getString());
  }

  @ org.junit.Test
  public void testFunctionWithManyStatementsAndMultipleReturn() {
    String source = "function F() {" +
        "this.a = 42;" +
        "this.b = this.a * 2;" +
        "return this.b * 3;" +
        "return this.a * 2 + 5;" +
        "}";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();

    Node lastHelperNode = helperNode.getLastChild();
    assertEquals(HelperName.ADD.toString(), lastHelperNode.getLastChild().getString());
    Node lastPropertyNode = lastHelperNode.getPrevious();
    assertEquals("a", lastPropertyNode.getString());

    Node secondLastHelperNode = helperNode.getLastChild().getPrevious();
    assertEquals(HelperName.MUL.toString(), secondLastHelperNode.getLastChild().getString());
    Node secondLastPropertyNode = secondLastHelperNode.getPrevious();
    assertEquals("b", secondLastPropertyNode.getString());
  }

  @ org.junit.Test
  public void testFunctionWithManyStatementsAndNestedReturn() {
    String source = "function F() {" +
        "this.a = 42;" +
        "this.b = this.a * 2;" +
        "if (true) {" +
        "  return this.b;" +
        "}" +
        "return this.a * 2 + 5;" +
        "}";
    Node root = compiler.parse(source, null, null);

    FunctionRewriter pass = new FunctionRewriter(compiler);
    pass.process(null, root);

    Node functionNode = findFunctionNode(root, "F");
    assertEquals(Token.CALL, functionNode.getType());
    Node helperNode = functionNode.getFirstChild();

    Node ifHelperNode = helperNode.getLastChild().getPrevious();
    assertEquals(HelperName.IF.toString(), ifHelperNode.getString());
    Node ifConditionNode = ifHelperNode.getPrevious();
    assertTrue(ifConditionNode.isBoolean());
    Node ifFalseHelperNode = ifHelperNode.getLastChild();
    assertEquals(HelperName.ADD.toString(), ifFalseHelperNode.getLastChild().getString());
    Node ifFalsePropertyNode = ifFalseHelperNode.getPrevious();
    assertEquals("a", ifFalsePropertyNode.getString());

    Node ifTrueHelperNode = helperNode.getLastChild();
    Node ifTruePropertyNode = ifTrueHelperNode.getPrevious();
    assertEquals("b", ifTruePropertyNode.getString());
  }

  private Node findFunctionNode(Node root, String name) {
    NodeTraversal traversal = new NodeTraversal(root) {
      @Override
      public boolean shouldTraverse(NodeTraversal nt, Node parent, Node node) {
        if (node.isFunction() && node.getString().equals(name)) {
          return false;
        }
        return true;
      }
    };
    Node functionNode = traversal.depthFirst(root);
    assertNotNull(functionNode);
    return functionNode;
  }
}

enum HelperName {
  EMPTY_FN,
  IDENTITY_FN,
  GET,
  SET,
  RETURN_ARG,
  ADD,
  MUL,
  IF
}