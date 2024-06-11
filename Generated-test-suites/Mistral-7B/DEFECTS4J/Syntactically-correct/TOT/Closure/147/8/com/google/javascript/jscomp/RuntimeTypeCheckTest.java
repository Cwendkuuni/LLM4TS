import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import static org.junit.Assert.*;

public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() throws Exception {
    String sourceCode = "function Foo() {}";
    CompilerInput input = new CompilerInput(
        "test",
        "test.js",
        SourceFile.Kind.SOURCE_INPUT,
        sourceCode);

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    Node root = compiler.parseTree(input);
    RuntimeTypeCheck pass = new RuntimeTypeCheck(compiler, null);
    pass.process(null, root);

    Node checkFunctionCall = findCheckFunctionCall(root, "Foo");
    assertNotNull(checkFunctionCall);

    Node fooInstance = NodeUtil.newObjectLiteral(
        NodeUtil.newProperty(NodeUtil.newIdentifier("x"), Node.newNumber(1)));
    Node checkResult = NodeUtil.callMethod(checkFunctionCall, "checkType", fooInstance);
    assertTrue(checkResult.isTrue());

    Node invalidInstance = NodeUtil.newObjectLiteral(
        NodeUtil.newProperty(NodeUtil.newIdentifier("x"), Node.newString("invalid")));
    Node checkError = assertThrows(RuntimeException.class, () -> {
      Node checkResult = NodeUtil.callMethod(checkFunctionCall, "checkType", invalidInstance);
    });
    String expectedErrorMessage = "expected Foo, but got String";
    assertTrue(checkError.getMessage().contains(expectedErrorMessage));
  }

  private Node findCheckFunctionCall(Node root, String className) {
    return findChildByFunctionName(root, "checkType", className);
  }

  private Node findChildByFunctionName(Node node, String name, String className) {
    if (node.getFirstChild() != null) {
      for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
        if (NodeUtil.isFunction(child) &&
            NodeUtil.getFunctionName(child.getJSType()).equals(name) &&
            NodeUtil.getFunctionName(child.getJSType().getSource().getClassName()).equals(className)) {
          return child;
        }
        Node result = findChildByFunctionName(child, name, className);
        if (result != null) {
          return result;
        }
      }
    }
    return null;
  }

  @Test
  public void testRuntimeTypeCheckWithExternedClass() throws Exception {
    String sourceCode = "var foo = {};";
    CompilerInput input = new CompilerInput(
        "test",
        "test.js",
        SourceFile.Kind.SOURCE_INPUT,
        sourceCode);

    CompilerOptions options = new CompilerOptions();
    options.setExterns("foo: {}");
    AbstractCompiler compiler = new AbstractCompiler(options);

    Node root = compiler.parseTree(input);
    RuntimeTypeCheck pass = new RuntimeTypeCheck(compiler, null);
    pass.process(null, root);

    Node checkFunctionCall = findCheckFunctionCall(root, "foo");
    assertNotNull(checkFunctionCall);

    Node fooInstance = NodeUtil.newObjectLiteral(
        NodeUtil.newProperty(NodeUtil.newIdentifier("x"), Node.newNumber(1)));
    Node checkResult = NodeUtil.callMethod(checkFunctionCall, "checkType", fooInstance);
    assertTrue(checkResult.isTrue());
  }

  @Test
  public void testRuntimeTypeCheckWithInterface() throws Exception {
    String sourceCode = "function Bar(x) {} Bar.prototype.foo = 42;";
    CompilerInput input = new CompilerInput(
        "test",
        "test.js",
        SourceFile.Kind.SOURCE_INPUT,
        sourceCode);

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    Node root = compiler.parseTree(input);
    RuntimeTypeCheck pass = new RuntimeTypeCheck(compiler, null);
    pass.process(null, root);

    Node checkFunctionCall = findCheckFunctionCall(root, "Bar");
    assertNotNull(checkFunctionCall);

    Node barInstance = NodeUtil.newObjectLiteral(
        NodeUtil.newProperty(NodeUtil.newIdentifier("x"), Node.newNumber(1)));
    Node checkResult = NodeUtil.callMethod(checkFunctionCall, "checkType", barInstance);
    assertTrue(checkResult.isTrue());

    Node invalidInstance = NodeUtil.newObjectLiteral(
        NodeUtil.newProperty(NodeUtil.newIdentifier("x"), Node.newNumber(2)));
    Node checkError = assertThrows(RuntimeException.class, () -> {
      Node checkResult = NodeUtil.callMethod(checkFunctionCall, "checkType", invalidInstance);
    });
    String expectedErrorMessage = "expected Bar, but got Object";
    assertTrue(checkError.getMessage().contains(expectedErrorMessage));
  }

  @Test
  public void testRuntimeTypeCheckWithInterfaceImplementation() throws Exception {
    String sourceCode = "function Foo() {} Foo.prototype.foo = 42; function Bar extends Foo {}";
    CompilerInput input = new CompilerInput(
        "test",
        "test.js",
        SourceFile.Kind.SOURCE_INPUT,
        sourceCode);

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    Node root = compiler.parseTree(input);
    RuntimeTypeCheck pass = new RuntimeTypeCheck(compiler, null);
    pass.process(null, root);

    Node checkFunctionCall = findCheckFunctionCall(root, "Bar");
    assertNotNull(checkFunctionCall);

    Node fooInstance = NodeUtil.newObjectLiteral(
        NodeUtil.newProperty(NodeUtil.newIdentifier("x"), Node.newNumber(1)));
    Node barInstance = new Node(Token.NEW, NodeUtil.newQualifiedNameNode("Foo"), null);
    Node checkResult = NodeUtil.callMethod(checkFunctionCall, "checkType", barInstance);
    assertTrue(checkResult.isTrue());
  }

  @Test
  public void testRuntimeTypeCheckWithUnionType() throws Exception {
    String sourceCode = "function Foo() {} function Bar() {}";
    CompilerInput input = new CompilerInput(
        "test",
        "test.js",
        SourceFile.Kind.SOURCE_INPUT,
        sourceCode);

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    Node root = compiler.parseTree(input);
    RuntimeTypeCheck pass = new RuntimeTypeCheck(compiler, null);
    pass.process(null, root);

    Node checkFunctionCall = findCheckFunctionCall(root, "Foo");
    assertNotNull(checkFunctionCall);

    Node fooInstance = NodeUtil.newObjectLiteral(
        NodeUtil.newProperty(NodeUtil.newIdentifier("x"), Node.newNumber(1)));
    Node checkResult = NodeUtil.callMethod(checkFunctionCall, "checkType", fooInstance);
    assertTrue(checkResult.isTrue());

    Node barInstance = NodeUtil.newObjectLiteral(
        NodeUtil.newProperty(NodeUtil.newIdentifier("x"), Node.newNumber(2)));
    Node checkError = assertThrows(RuntimeException.class, () -> {
      Node checkResult = NodeUtil.callMethod(checkFunctionCall, "checkType", barInstance);
    });
    String expectedErrorMessage = "expected Foo|Bar, but got Object";
    assertTrue(checkError.getMessage().contains(expectedErrorMessage));

    Node checkFunctionCallForBar = findCheckFunctionCall(root, "Bar");
    assertNotNull(checkFunctionCallForBar);

    Node fooInstanceForBar = NodeUtil.newObjectLiteral(
        NodeUtil.newProperty(NodeUtil.newIdentifier("x"), Node.newNumber(1)));
    Node checkResultForBar = NodeUtil.callMethod(checkFunctionCallForBar, "checkType", fooInstanceForBar);
    assertTrue(checkResultForBar.isTrue());

    Node barInstanceForBar = NodeUtil.newObjectLiteral(
        NodeUtil.newProperty(NodeUtil.newIdentifier("x"), Node.newNumber(2)));
    Node checkResultForBar = NodeUtil.callMethod(checkFunctionCallForBar, "checkType", barInstanceForBar);
    assertTrue(checkResultForBar.isTrue());
  }

  @Test
  public void testRuntimeTypeCheckWithNullType() throws Exception {
    String sourceCode = "function Foo() {} Foo.prototype.foo = 42; function Bar() { return null; }";
    CompilerInput input = new CompilerInput(
        "test",
        "test.js",
        SourceFile.Kind.SOURCE_INPUT,
        sourceCode);

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    Node root = compiler.parseTree(input);
    RuntimeTypeCheck pass = new RuntimeTypeCheck(compiler, null);
    pass.process(null, root);

    Node checkFunctionCall = findCheckFunctionCall(root, "Bar");
    assertNotNull(checkFunctionCall);

    Node checkResult = NodeUtil.callMethod(checkFunctionCall, "checkType", Node.newNull());
    assertTrue(checkResult.isTrue());
  }

  @Test
  public void testRuntimeTypeCheckWithVoidType() throws Exception {
    String sourceCode = "function Foo() { return; }";
    CompilerInput input = new CompilerInput(
        "test",
        "test.js",
        SourceFile.Kind.SOURCE_INPUT,
        sourceCode);

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    Node root = compiler.parseTree(input);
    RuntimeTypeCheck pass = new RuntimeTypeCheck(compiler, null);
    pass.process(null, root);

    Node checkFunctionCall = findCheckFunctionCall(root, "Foo");
    assertNotNull(checkFunctionCall);

    Node checkResult = NodeUtil.callMethod(checkFunctionCall, "checkType", Node.newNull());
    assertTrue(checkResult.isTrue());
  }

  @Test
  public void testRuntimeTypeCheckWithStringValueType() throws Exception {
    String sourceCode = "function Foo() {} Foo.prototype.foo = 'hello';";
    CompilerInput input = new CompilerInput(
        "test",
        "test.js",
        SourceFile.Kind.SOURCE_INPUT,
        sourceCode);

    CompilerOptions options = new CompilerOptions();
    AbstractCompiler compiler = new AbstractCompiler(options);

    Node root = compiler.parseTree(input);
    RuntimeTypeCheck pass = new RuntimeTypeCheck(compiler, null);
    pass.process(null, root);

    Node checkFunctionCall = findCheckFunctionCall(root, "Foo");
    assertNotNull(checkFunctionCall);

    Node fooInstance = NodeUtil.newObjectLiteral(
        NodeUtil.newProperty(NodeUtil.newIdentifier("x"), Node.newString("world")));
    Node checkError = assertThrows(RuntimeException.class, () -> {
      Node checkResult = NodeUtil.callMethod(checkFunctionCall, "checkType", fooInstance);
    });
    String expectedErrorMessage = "expected 'hello', but got world";
    assertTrue(checkError.getMessage().contains(expectedErrorMessage));

    Node checkFunctionCallForString = findCheckFunctionCall(root, "String");
    assertNotNull(checkFunctionCallForString);

    Node stringInstance = NodeUtil.newString("hello");
    Node checkResultForString = NodeUtil.callMethod(checkFunctionCallForString, "checkType", stringInstance);
    assertTrue(checkResultForString.isTrue());
  }
}