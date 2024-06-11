import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RuntimeTypeCheckTest {

  private AbstractCompiler compiler;
  private Node rootNode;

  @Before
  public void setup() {
    compiler = new Compiler(); // Assuming the Compiler class is defined
    rootNode = Node.newText("function foo() {}");
  }

  @Test
  public void testRuntimeTypeCheckForStringType() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    Node newRootNode = Node.newText("function foo(str) { return str.toUpperCase(); }");
    Node functionNode = Node.newFromSource("foo", newRootNode.getText());
    Node functionType = (FunctionType) functionNode.getJSType();

    Node inputArgument = Node.newText("'hello'");
    Node argumentNode = new Node(Token.EXPR_RESULT, inputArgument);
    Node callNode = new Node(Token.CALL, functionNode, argumentNode);

    runtimeTypeCheck.process(null, newRootNode);

    Node checkedFunctionNode = NodeTraversal.findNode(
        callNode, NodeUtil::isFunction, "foo");

    Node checkNode = NodeTraversal.findNode(checkedFunctionNode, Node::getLastChild,
        Node::getType, Token.CALL);

    assertEquals("checkType", checkNode.getFirstChild().getValue());
    assertArrayEquals(new String[]{"valueChecker('string')"},
        NodeUtil.getArguments(checkNode));

    assertTrue(NodeUtil.hasChild(checkedFunctionNode, NodeUtil.newQualifiedNameNode(
        "jscomp.typecheck.checkType", -1, -1)));
  }

  @Test
  public void testRuntimeTypeCheckForNumberType() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    Node newRootNode = Node.newText("function foo(num) { return num * 2; }");
    Node functionNode = Node.newFromSource("foo", newRootNode.getText());
    Node functionType = (FunctionType) functionNode.getJSType();

    Node inputArgument = Node.newText("42");
    Node argumentNode = new Node(Token.EXPR_RESULT, inputArgument);
    Node callNode = new Node(Token.CALL, functionNode, argumentNode);

    runtimeTypeCheck.process(null, newRootNode);

    Node checkedFunctionNode = NodeTraversal.findNode(
        callNode, NodeUtil::isFunction, "foo");

    Node checkNode = NodeTraversal.findNode(checkedFunctionNode, Node::getLastChild,
        Node::getType, Token.CALL);

    assertEquals("checkType", checkNode.getFirstChild().getValue());
    assertArrayEquals(new String[]{"valueChecker('number')"},
        NodeUtil.getArguments(checkNode));

    assertTrue(NodeUtil.hasChild(checkedFunctionNode, NodeUtil.newQualifiedNameNode(
        "jscomp.typecheck.checkType", -1, -1)));
  }

  @Test
  public void testRuntimeTypeCheckForCustomClassType() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    Node newRootNode = Node.newText("function MyClass() {}");
    Node functionNode = Node.newFromSource("MyClass", newRootNode.getText());
    Node functionType = (FunctionType) functionNode.getJSType();

    Node classInstance = Node.newProperty(NodeUtil.newQualifiedNameNode(
        "MyClass", -1, -1), "prototype", Node.newObjectLiteral());

    Node constructorCall = new Node(Token.CALL, functionNode, classInstance);

    runtimeTypeCheck.process(null, newRootNode);

    Node checkedFunctionNode = NodeTraversal.findNode(
        constructorCall, NodeUtil::isFunction, "MyClass");

    Node checkNode = NodeTraversal.findNode(checkedFunctionNode, Node::getLastChild,
        Node::getType, Token.CALL);

    assertEquals("checkType", checkNode.getFirstChild().getValue());
    assertArrayEquals(new String[]{"classChecker('MyClass')"},
        NodeUtil.getArguments(checkNode));

    assertTrue(NodeUtil.hasChild(checkedFunctionNode, NodeUtil.newQualifiedNameNode(
        "jscomp.typecheck.checkType", -1, -1)));
  }

  @Test
  public void testRuntimeTypeCheckForInterfaceType() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    Node newRootNode = Node.newText("interface MyInterface { foo(); }");
    Node interfaceNode = Node.newFromSource("MyInterface", newRootNode.getText());

    Node classNode = Node.newText("class MyClass implements MyInterface { foo() {} }");
    Node classFunctionNode = Node.newFromSource("MyClass", classNode.getText());
    Node functionType = (FunctionType) classFunctionNode.getJSType();

    Node constructorCall = NodeTraversal.findNode(
        classFunctionNode, NodeUtil::isCallExpression, "new");

    runtimeTypeCheck.process(null, newRootNode);

    Node checkedFunctionNode = NodeTraversal.findNode(
        constructorCall, NodeUtil::isFunction, "MyClass");

    Node checkNode = NodeTraversal.findNode(checkedFunctionNode, Node::getLastChild,
        Node::getType, Token.CALL);

    assertEquals("checkType", checkNode.getFirstChild().getValue());
    assertArrayEquals(new String[]{"interfaceChecker('MyInterface')"},
        NodeUtil.getArguments(checkNode));

    assertTrue(NodeUtil.hasChild(checkedFunctionNode, NodeUtil.newQualifiedNameNode(
        "jscomp.typecheck.checkType", -1, -1)));
  }

  @Test
  public void testRuntimeTypeCheckForNullType() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    Node newRootNode = Node.newText("function foo() { return null; }");
    Node functionNode = Node.newFromSource("foo", newRootNode.getText());
    Node functionType = (FunctionType) functionNode.getJSType();

    Node callNode = new Node(Token.CALL, functionNode, Node.newNull());

    runtimeTypeCheck.process(null, newRootNode);

    Node checkedFunctionNode = NodeTraversal.findNode(
        callNode, NodeUtil::isFunction, "foo");

    Node checkNode = NodeTraversal.findNode(checkedFunctionNode, Node::getLastChild,
        Node::getType, Token.CALL);

    assertEquals("checkType", checkNode.getFirstChild().getValue());
    assertArrayEquals(new String[]{"nullChecker()"}, NodeUtil.getArguments(checkNode));

    assertTrue(NodeUtil.hasChild(checkedFunctionNode, NodeUtil.newQualifiedNameNode(
        "jscomp.typecheck.checkType", -1, -1)));
  }

  @Test(expected = AssertionError.class)
  public void testRuntimeTypeCheckForUnknownType() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    Node newRootNode = Node.newText("function foo() { return 10 + 'hello'; }");
    Node functionNode = Node.newFromSource("foo", newRootNode.getText());
    Node functionType = (FunctionType) functionNode.getJSType();

    Node callNode = new Node(Token.CALL, functionNode, Node.newText(""));

    runtimeTypeCheck.process(null, newRootNode);

    Node checkedFunctionNode = NodeTraversal.findNode(
        callNode, NodeUtil::isFunction, "foo");

    Node checkNode = NodeTraversal.findNode(checkedFunctionNode, Node::getLastChild,
        Node::getType, Token.CALL);

    // The assertion error is expected here
    NodeUtil.getArguments(checkNode);
  }
}