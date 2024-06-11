import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import org.junit.Test;
import static org.junit.Assert.*;

public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck_FunctionWithValidTypeArguments() {
    Compiler compiler = new Compiler(null);
    String sourceCode = "function add(x, y) {\n" +
            "  return x + y;\n" +
            "}";

    Node root = Parser.parse(sourceCode, null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, root);
    Node result = compiler.getResult();

    Node addFunction = Iterables.get(result.getChildren(), 0);
    FunctionType addFunctionType = (FunctionType) addFunction.getJSType();

    Node callNode = new Node(Token.CALL, addFunction, Node.newNumber(1), Node.newNumber(2));
    Node checkTypeCallNode = new Node(Token.CALL, jsCode("checkType"), callNode, Node.newArray(jsCode("valueChecker"), jsCode("valueChecker")));
    Node checkedResult = compiler.compile(checkTypeCallNode, null, null).getResult();

    Node checkedValue = Iterables.get(checkedResult.getChildren(), 0);
    assertEquals(Node.newNumber(3), checkedValue);
  }

  @Test
  public void testRuntimeTypeCheck_FunctionWithMismatchedTypes() {
    Compiler compiler = new Compiler(null);
    String sourceCode = "function add(x, y) {\n" +
            "  return x + y;\n" +
            "}";

    Node root = Parser.parse(sourceCode, null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, root);
    Node result = compiler.getResult();

    Node addFunction = Iterables.get(result.getChildren(), 0);
    FunctionType addFunctionType = (FunctionType) addFunction.getJSType();

    Node callNode = new Node(Token.CALL, addFunction, Node.newString("1"), Node.newNumber(2));
    Node checkTypeCallNode = new Node(Token.CALL, jsCode("checkType"), callNode, Node.newArray(jsCode("valueChecker"), jsCode("stringChecker")));
    Node checkedResult = compiler.compile(checkTypeCallNode, null, null).getResult();

    assertThrows(AssertionError.class, () -> {
      Node checkedValue = Iterables.get(checkedResult.getChildren(), 0);
      assertEquals(Node.newString("TypeError: '1' is not a string"), checkedValue.getFirstChild().getString());
    });
  }

  @Test
  public void testRuntimeTypeCheck_ClassType() {
    Compiler compiler = new Compiler(null);
    String sourceCode = "class MyClass {\n" +
            "  constructor(x) {\n" +
            "  }\n" +
            "}\n" +
            "function test(obj) {\n" +
            "  MyClass.prototype['instance_of__MyClass'].call(obj);\n" +
            "}";

    Node root = Parser.parse(sourceCode, null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, root);
    Node result = compiler.getResult();

    Node testFunction = Iterables.get(result.getChildren(), 1);
    FunctionType testFunctionType = (FunctionType) testFunction.getJSType();

    Node myClassInstance = new Node(Token.NEW,
        NodeUtil.newQualifiedNameNode("MyClass", -1, -1), Node.newArray(Node.newNumber(1)));

    Node callNode = new Node(Token.CALL, testFunction, myClassInstance);
    Node checkTypeCallNode = new Node(Token.CALL, jsCode("checkType"), callNode, Node.newArray(jsCode("classChecker"), jsCode("classChecker")));
    Node checkedResult = compiler.compile(checkTypeCallNode, null, null).getResult();

    Node checkedValue = Iterables.get(checkedResult.getChildren(), 0);
    assertTrue(checkedValue instanceof Node);
  }

  @Test
  public void testRuntimeTypeCheck_InterfaceType() {
    Compiler compiler = new Compiler(null);
    String sourceCode = "interface MyInterface {\n" +
            "}\n" +
            "class MyClass implements MyInterface {\n" +
            "  constructor() {\n" +
            "  }\n" +
            "}";

    Node root = Parser.parse(sourceCode, null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, root);
    Node result = compiler.getResult();

    Node myClassInstance = new Node(Token.NEW,
        NodeUtil.newQualifiedNameNode("MyClass", -1, -1));

    Node callNode = new Node(Token.CALL,
        NodeUtil.getFnDeclaration("MyClass", result).getLastChild(),
        myClassInstance);

    Node checkTypeCallNode = new Node(Token.CALL, jsCode("checkType"), callNode, Node.newArray(jsCode("interfaceChecker"), jsCode("interfaceChecker")));
    Node checkedResult = compiler.compile(checkTypeCallNode, null, null).getResult();

    Node checkedValue = Iterables.get(checkedResult.getChildren(), 0);
    assertTrue(checkedValue instanceof Node);
  }

  @Test
  public void testRuntimeTypeCheck_NullChecker() {
    Compiler compiler = new Compiler(null);
    String sourceCode = "function foo(x) {\n" +
            "  if (x == null) {\n" +
            "    throw new Error(\"x is null\");\n" +
            "  }\n" +
            "  return x;\n" +
            "}";

    Node root = Parser.parse(sourceCode, null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, root);
    Node result = compiler.getResult();

    Node fooFunction = Iterables.get(result.getChildren(), 0);
    FunctionType fooFunctionType = (FunctionType) fooFunction.getJSType();

    Node callNode = new Node(Token.CALL, fooFunction, Node.newNull());
    Node checkTypeCallNode = new Node(Token.CALL, jsCode("checkType"), callNode, Node.newArray(jsCode("nullChecker"), jsCode("nullChecker")));
    Node checkedResult = compiler.compile(checkTypeCallNode, null, null).getResult();

    assertThrows(AssertionError.class, () -> {
      Node checkedValue = Iterables.get(checkedResult.getChildren(), 0);
      assertEquals(Node.newString("TypeError: 'null' is null"), checkedValue.getFirstChild().getString());
    });
  }

  @Test
  public void testRuntimeTypeCheck_ValueChecker() {
    Compiler compiler = new Compiler(null);
    String sourceCode = "function foo(x) {\n" +
            "  return x + 1;\n" +
            "}";

    Node root = Parser.parse(sourceCode, null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, root);
    Node result = compiler.getResult();

    Node fooFunction = Iterables.get(result.getChildren(), 0);
    FunctionType fooFunctionType = (FunctionType) fooFunction.getJSType();

    Node callNode = new Node(Token.CALL, fooFunction, Node.newNumber(1));
    Node checkTypeCallNode = new Node(Token.CALL, jsCode("checkType"), callNode, Node.newArray(jsCode("valueChecker"), jsCode("numberChecker")));
    Node checkedResult = compiler.compile(checkTypeCallNode, null, null).getResult();

    Node checkedValue = Iterables.get(checkedResult.getChildren(), 0);
    assertEquals(Node.newNumber(2), checkedValue);
  }

  @Test
  public void testRuntimeTypeCheck_ClassChecker() {
    Compiler compiler = new Compiler(null);
    String sourceCode = "class MyClass {\n" +
            "  constructor(x) {\n" +
            "    this.x = x;\n" +
            "  }\n" +
            "}\n" +
            "function foo(myInstance) {\n" +
            "  MyClass.prototype['instance_of__MyClass'].call(myInstance);\n" +
            "  return myInstance.x;\n" +
            "}";

    Node root = Parser.parse(sourceCode, null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, root);
    Node result = compiler.getResult();

    Node myClassInstance = new Node(Token.NEW,
        NodeUtil.newQualifiedNameNode("MyClass", -1, -1), Node.newArray(Node.newNumber(1)));

    Node callNode = new Node(Token.CALL, fooFunction, myClassInstance);
    Node checkTypeCallNode = new Node(Token.CALL, jsCode("checkType"), callNode, Node.newArray(jsCode("classChecker"), jsCode("classChecker")));
    Node checkedResult = compiler.compile(checkTypeCallNode, null, null).getResult();

    Node checkedValue = Iterables.get(checkedResult.getChildren(), 0);
    assertEquals(Node.newNumber(1), checkedValue);
  }

  @Test
  public void testRuntimeTypeCheck_InterfaceChecker() {
    Compiler compiler = new Compiler(null);
    String sourceCode = "interface MyInterface {\n" +
            "  method1(): number;\n" +
            "}\n" +
            "class MyClass implements MyInterface {\n" +
            "  constructor() {\n" +
            "  }\n" +
            "  method1() {\n" +
            "    return 1;\n" +
            "  }\n" +
            "}";

    Node root = Parser.parse(sourceCode, null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, root);
    Node result = compiler.getResult();

    Node myClassInstance = new Node(Token.NEW,
        NodeUtil.newQualifiedNameNode("MyClass", -1, -1));

    Node callNode = new Node(Token.CALL,
        NodeUtil.getFnDeclaration("method1", result).getLastChild(),
        myClassInstance);

    Node checkTypeCallNode = new Node(Token.CALL, jsCode("checkType"), callNode, Node.newArray(jsCode("interfaceChecker"), jsCode("interfaceChecker")));
    Node checkedResult = compiler.compile(checkTypeCallNode, null, null).getResult();

    Node checkedValue = Iterables.get(checkedResult.getChildren(), 0);
    assertEquals(Node.newNumber(1), checkedValue);
  }
}