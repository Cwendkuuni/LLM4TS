import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeUtil;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RuntimeTypeCheckTest {

  private AbstractCompiler compiler;
  private Node rootNode;

  @Before
  public void setUp() {
    compiler = new Compiler();
    rootNode = NodeUtil.parseJsFile("test.js");
  }

  @Test
  public void testBasicUsage() {
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(null, rootNode);

    // Add your assertions here to check the generated code
  }

  @Test
  public void testNullTypeChecker() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null);
    JSType nullType = JSType.NULL;
    Node testFunction = NodeUtil.newFunction(
        "testNullType", Node.newName("test"), null, null);
    Node argument = NodeUtil.newName("arg");
    Node nullLiteral = Node.newNull();
    testFunction.getBody().addChild(new Node(Token.RETURN, nullLiteral));
    rootNode.addChild(testFunction);

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, rootNode);

    Node testFunctionAfterProcessing = NodeUtil.getFunctionByName(
        rootNode, "testNullType");
    Node checkNode = findCheckNode(testFunctionAfterProcessing, nullType, 0);
    assertNotNull(checkNode);

    Node testCall = NodeUtil.newExprCall(testFunctionAfterProcessing,
        NodeUtil.newArray(NodeUtil.newFunctionCall(
            NodeUtil.newQualifiedNameNode("checkType"), NodeUtil.newNull(),
            NodeUtil.newArray(checkNode))));
    Node result = (Node) compiler.compileSingleCompilationUnit(input, testCall);

    // Add your assertions here to check the result
  }

  @Test
  public void testStringTypeChecker() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null);
    JSType stringType = JSType.STRING;
    Node testFunction = NodeUtil.newFunction(
        "testStringType", Node.newName("test"), null, null);
    Node argument = NodeUtil.newName("arg");
    Node stringLiteral = Node.newString("test");
    testFunction.getBody().addChild(new Node(Token.RETURN, stringLiteral));
    rootNode.addChild(testFunction);

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, rootNode);

    Node testFunctionAfterProcessing = NodeUtil.getFunctionByName(
        rootNode, "testStringType");
    Node checkNode = findCheckNode(testFunctionAfterProcessing, stringType, 0);
    assertNotNull(checkNode);

    Node testCall = NodeUtil.newExprCall(testFunctionAfterProcessing,
        NodeUtil.newArray(NodeUtil.newFunctionCall(
            NodeUtil.newQualifiedNameNode("checkType"), argument,
            NodeUtil.newArray(checkNode))));
    Node result = (Node) compiler.compileSingleCompilationUnit(input, testCall);

    // Add your assertions here to check the result
  }

  @Test
  public void testNumberTypeChecker() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null);
    JSType numberType = JSType.NUMBER;
    Node testFunction = NodeUtil.newFunction(
        "testNumberType", Node.newName("test"), null, null);
    Node argument = NodeUtil.newName("arg");
    Node numberLiteral = Node.newNumber(42);
    testFunction.getBody().addChild(new Node(Token.RETURN, numberLiteral));
    rootNode.addChild(testFunction);

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, rootNode);

    Node testFunctionAfterProcessing = NodeUtil.getFunctionByName(
        rootNode, "testNumberType");
    Node checkNode = findCheckNode(testFunctionAfterProcessing, numberType, 0);
    assertNotNull(checkNode);

    Node testCall = NodeUtil.newExprCall(testFunctionAfterProcessing,
        NodeUtil.newArray(NodeUtil.newFunctionCall(
            NodeUtil.newQualifiedNameNode("checkType"), argument,
            NodeUtil.newArray(checkNode))));
    Node result = (Node) compiler.compileSingleCompilationUnit(input, testCall);

    // Add your assertions here to check the result
  }

  @Test
  public void testObjectTypeChecker() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null);
    JSType objectType = new ObjectType(NodeUtil.newName("MyType"));
    Node testFunction = NodeUtil.newFunction(
        "testObjectType", Node.newName("test"), null, null);
    Node argument = NodeUtil.newObjectLiteral();
    argument.addChild(new Node(Token.PROPERTIES, 1, null));
    argument.getLastChild().addChild(
        new Node(Token.KEY, Node.newString("name"), NodeUtil.newString("test")));
    argument.getLastChild().addChild(
        new Node(Token.VALUE, NodeUtil.newString("test")));
    testFunction.getBody().addChild(new Node(Token.RETURN, argument));
    rootNode.addChild(testFunction);

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, rootNode);

    Node testFunctionAfterProcessing = NodeUtil.getFunctionByName(
        rootNode, "testObjectType");
    Node checkNode = findCheckNode(testFunctionAfterProcessing, objectType, 0);
    assertNotNull(checkNode);

    Node testCall = NodeUtil.newExprCall(testFunctionAfterProcessing,
        NodeUtil.newArray(NodeUtil.newFunctionCall(
            NodeUtil.newQualifiedNameNode("checkType"), argument,
            NodeUtil.newArray(checkNode))));
    Node result = (Node) compiler.compileSingleCompilationUnit(input, testCall);

    // Add your assertions here to check the result
  }

  @Test
  public void testArrayTypeChecker() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null);
    JSType arrayType = new ObjectType(NodeUtil.newName("MyArray"))
        .setTypeParameters(NodeUtil.newArray(NodeUtil.newName("T")))
        .setElementType(JSType.ANY);
    Node testFunction = NodeUtil.newFunction(
        "testArrayType", Node.newName("test"), null, null);
    Node argument = NodeUtil.newArrayLiteral();
    argument.addChild(NodeUtil.newNumber(1));
    argument.addChild(NodeUtil.newIntLiteral(42));
    testFunction.getBody().addChild(new Node(Token.RETURN, argument));
    rootNode.addChild(testFunction);

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, rootNode);

    Node testFunctionAfterProcessing = NodeUtil.getFunctionByName(
        rootNode, "testArrayType");
    Node checkNode = findCheckNode(testFunctionAfterProcessing, arrayType, 0);
    assertNotNull(checkNode);

    Node testCall = NodeUtil.newExprCall(testFunctionAfterProcessing,
        NodeUtil.newArray(NodeUtil.newFunctionCall(
            NodeUtil.newQualifiedNameNode("checkType"), argument,
            NodeUtil.newArray(checkNode))));
    Node result = (Node) compiler.compileSingleCompilationUnit(input, testCall);

    // Add your assertions here to check the result
  }

  @Test
  public void testFunctionTypeChecker() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null);
    JSType functionType = new FunctionType(NodeUtil.newName("MyFunction"),
        new ObjectType(NodeUtil.newName("MyArguments")), null, null);
    Node testFunction = NodeUtil.newFunction(
        "testFunctionTypeChecker", Node.newName("test"), null, null);
    Node functionArgument = NodeUtil.newFunction(
        "myFunction", Node.newName("myFunction"), null, null);
    Node functionArgumentType = new FunctionType(NodeUtil.newName("MyArguments"),
        new ObjectType(NodeUtil.newName("MyArgumentsElement")), null, null);
    functionArgument.setJSType(functionArgumentType);
    Node argument = NodeUtil.newName("arg");
    Node functionCall = NodeUtil.newExprCall(functionArgument, new Node[]{argument});
    Node functionCallReturn = NodeUtil.newName("arg");
    testFunction.getBody().addChild(new Node(Token.RETURN, functionCallReturn));
    rootNode.addChild(testFunction);

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, rootNode);

    Node testFunctionAfterProcessing = NodeUtil.getFunctionByName(
        rootNode, "testFunctionTypeChecker");
    Node checkNode = findCheckNode(testFunctionAfterProcessing, functionType, 0);
    assertNotNull(checkNode);

    Node testCall = NodeUtil.newExprCall(testFunctionAfterProcessing,
        NodeUtil.newArray(NodeUtil.newFunctionCall(
            NodeUtil.newQualifiedNameNode("checkType"), argument,
            NodeUtil.newArray(checkNode))));
    Node result = (Node) compiler.compileSingleCompilationUnit(input, testCall);

    // Add your assertions here to check the result
  }

  @Test
  public void testInterfaceTypeChecker() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null);
    JSType interfaceType = new ObjectType(NodeUtil.newName("MyInterface"))
        .setInterface(true);
    Node testFunction = NodeUtil.newFunction(
        "testInterfaceTypeChecker", Node.newName("test"), null, null);
    Node testObject = NodeUtil.newObjectLiteral();
    testObject.addChild(new Node(Token.PROPERTIES, 1, null));
    testObject.getLastChild().addChild(
        new Node(Token.KEY, NodeUtil.newString("method"), NodeUtil.newName("method")));
    Node methodCall = NodeUtil.newExprCall(testObject, NodeUtil.newArray(NodeUtil.newName("method")));
    Node methodCallReturn = NodeUtil.newName("result");
    testFunction.getBody().addChild(new Node(Token.RETURN, methodCallReturn));
    rootNode.addChild(testFunction);

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, rootNode);

    Node testFunctionAfterProcessing = NodeUtil.getFunctionByName(
        rootNode, "testInterfaceTypeChecker");
    Node checkNode = findCheckNode(testFunctionAfterProcessing, interfaceType, 0);
    assertNotNull(checkNode);

    Node testCall = NodeUtil.newExprCall(testFunctionAfterProcessing,
        NodeUtil.newArray(NodeUtil.newFunctionCall(
            NodeUtil.newQualifiedNameNode("checkType"), testObject,
            NodeUtil.newArray(checkNode))));
    Node result = (Node) compiler.compileSingleCompilationUnit(input, testCall);

    // Add your assertions here to check the result
  }

  @Test
  public void testExceptionHandling() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null);
    JSType stringType = JSType.STRING;
    Node testFunction = NodeUtil.newFunction(
        "testExceptionHandling", Node.newName("test"), null, null);
    Node argument = NodeUtil.newName("arg");
    Node stringLiteral = Node.newString("test");
    Node throwStatement = new Node(Token.THROW, new Node(Token.NEW,
        NodeUtil.newQualifiedNameNode("Error"), NodeUtil.newString("test")));
    testFunction.getBody().addChild(new Node(Token.TRY, null, null));
    testFunction.getBody().addChild(new Node(Token.ASSIGN, argument, stringLiteral));
    testFunction.getBody().addChild(throwStatement);
    testFunction.getBody().addChild(NodeUtil.newReturn(NodeUtil.newName("result")));
    rootNode.addChild(testFunction);

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, rootNode);

    Node testFunctionAfterProcessing = NodeUtil.getFunctionByName(
        rootNode, "testExceptionHandling");
    Node checkNode = findCheckNode(testFunctionAfterProcessing, stringType, 0);
    assertNotNull(checkNode);

    Node testCall = NodeUtil.newExprCall(testFunctionAfterProcessing,
        NodeUtil.newArray(NodeUtil.newFunctionCall(
            NodeUtil.newQualifiedNameNode("checkType"), argument,
            NodeUtil.newArray(checkNode))));
    Node result = (Node) compiler.compileSingleCompilationUnit(input, testCall);

    // Add your assertions here to check the result and exception handling
  }

  @Test
  public void testClassTypeCheckWithExternClass() {
    CompilerInput input = new CompilerInput("test.js", "test.js", null);
    JSType classType = new ObjectType(NodeUtil.newName("MyExternClass"));
    Node testFunction = NodeUtil.newFunction(
        "testClassTypeCheckWithExternClass", Node.newName("test"), null, null);
    Node argument = NodeUtil.newName("arg");
    testFunction.getBody().addChild(new Node(Token.RETURN, argument));
    rootNode.addChild(testFunction);

    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(input, rootNode);

    Node testFunctionAfterProcessing = NodeUtil.getFunctionByName(
        rootNode, "testClassTypeCheckWithExternClass");
    Node checkNode = findCheckNode(testFunctionAfterProcessing, classType, 0);
    assertNotNull(checkNode);

    Node testCall = NodeUtil.newExprCall(testFunctionAfterProcessing,
        NodeUtil.newArray(NodeUtil.newFunctionCall(
            NodeUtil.newQualifiedNameNode("checkType"), argument,
            NodeUtil.newArray(checkNode))));
    Node result = (Node) compiler.compileSingleCompilationUnit(input, testCall);

    // Add your assertions here to check the result
  }

  private Node findCheckNode(Node node, JSType type, int index) {
    if (node.getType() == Token.CALL && NodeUtil.isFunctionCall(node)) {
      Node checkTypeCall = node.getFirstChild();
      Node checkTypeFunction = checkTypeCall.getFirstChild();
      if (checkTypeFunction.getType() == Token.IDENT &&
          checkTypeFunction.getStringValue().equals("checkType")) {
        Node checkNodes = checkTypeCall.getLastChild();
        if (checkNodes.getType() == Token.ARRAYLIT) {
          for (int i = 0; i < checkNodes.getChildCount(); i++) {
            Node checkNode = checkNodes.getChild(i);
            if (checkNode != null && checkNode.getType() == Token.CALL &&
                NodeUtil.isFunctionCall(checkNode)) {
              Node checkFunction = checkNode.getFirstChild();
              if (checkFunction != null && checkFunction.getType() == Token.IDENT &&
                  (checkFunction.getStringValue().equals("valueChecker") ||
                  checkFunction.getStringValue().equals("classChecker") ||
                  checkFunction.getStringValue().equals("interfaceChecker"))) {
                Node checkerType = checkNode.getLastChild();
                if (checkerType != null && checkerType.getType() == Token.IDENT &&
                    checkerType.getStringValue().equals(type.toString())) {
                  return checkNode;
                }
              }
            }
          }
        }
      }
    }
    return null;
  }
}