import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.Node;
import org.junit.Test;
import static org.junit.Assert.*;

public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck_ValidFunctionWithValidArguments() {
    // Create a valid Node tree for testing
    Node root = new Node(Token.SCRIPT, null, null);
    Node function = NodeUtil.newFunctionNode("testFunction", new Node[0], null);
    root.addChild(function);

    // Initialize the compiler and the runtime type checker
    AbstractCompiler compiler = new Compiler(null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    // Perform the compilation and the type checking passes
    runtimeTypeCheck.process(null, root);

    // Assert that the function has the expected markers and checks
    FunctionType functionType = (FunctionType) function.getJSType();
    assertTrue(functionType.getMarkers().contains("instance_of__testFunction"));
    assertNotNull(functionType.getCheckers());

    // Create some valid arguments and check the function call
    Node arg1 = Node.newNumber(1);
    Node arg2 = Node.newBoolean(true);
    Node call = new Node(Token.CALL, function, arg1, arg2);
    Node result = NodeUtil.executeFunction(compiler, function, null, call);

    // Assert that the function call returns the expected result
    assertEquals(Node.newNumber(1), result);
  }

  @Test
  public void testRuntimeTypeCheck_ValidFunctionWithNullArgument() {
    // Create a valid Node tree for testing
    Node root = new Node(Token.SCRIPT, null, null);
    Node function = NodeUtil.newFunctionNode("testFunction", new Node[0], null);
    root.addChild(function);

    // Initialize the compiler and the runtime type checker
    AbstractCompiler compiler = new Compiler(null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    // Perform the compilation and the type checking passes
    runtimeTypeCheck.process(null, root);

    // Assert that the function has the expected markers and checks
    FunctionType functionType = (FunctionType) function.getJSType();
    assertTrue(functionType.getMarkers().contains("instance_of__testFunction"));
    assertNotNull(functionType.getCheckers());

    // Create a null argument and check the function call
    Node arg = Node.newNull();
    Node call = new Node(Token.CALL, function, arg);
    try {
      Node result = NodeUtil.executeFunction(compiler, function, null, call);
      fail("Expected a TypeError");
    } catch (TypeError e) {
      // Expected type error
    }
  }

  @Test
  public void testRuntimeTypeCheck_InvalidFunctionWithMismatchedArguments() {
    // Create a valid Node tree for testing
    Node root = new Node(Token.SCRIPT, null, null);
    Node function = NodeUtil.newFunctionNode("testFunction", new Node[0], null);
    root.addChild(function);

    // Initialize the compiler and the runtime type checker
    AbstractCompiler compiler = new Compiler(null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    // Perform the compilation and the type checking passes
    runtimeTypeCheck.process(null, root);

    // Assert that the function has the expected markers and checks
    FunctionType functionType = (FunctionType) function.getJSType();
    assertTrue(functionType.getMarkers().contains("instance_of__testFunction"));
    assertNotNull(functionType.getCheckers());

    // Create an integer argument for a function that expects a boolean argument
    Node arg = Node.newNumber(1);
    Node call = new Node(Token.CALL, function, arg);
    try {
      Node result = NodeUtil.executeFunction(compiler, function, null, call);
      fail("Expected a TypeError");
    } catch (TypeError e) {
      // Expected type error
    }
  }

  @Test
  public void testRuntimeTypeCheck_ValidFunctionWithInterfaceImplementation() {
    // Create a valid Node tree for testing
    Node root = new Node(Token.SCRIPT, null, null);
    Node interfaceDeclaration = new Node(Token.VAR, null, null);
    interfaceDeclaration.setName(new Node(Token.IDENT, "MyInterface"));
    interfaceDeclaration.addChild(
        new Node(Token.BLOCK,
            new Node(Token.TYPES, null),
            new Node(Token.FUNCTION, null, null, null,
                new Node[0], null, null, null, null, null)));
    Node classDeclaration = new Node(Token.VAR, null, null);
    classDeclaration.setName(new Node(Token.IDENT, "MyClass"));
    classDeclaration.addChild(new Node(Token.BLOCK, null, null));
    classDeclaration.addChild(new Node(Token.EXTENDS, null, new Node(Token.IDENT, "Object")));
    classDeclaration.addChild(new Node(Token.IMPLEMENTS, null, interfaceDeclaration));
    classDeclaration.addChild(
        new Node(Token.FUNCTION, null, null, null,
            new Node[0], null, null, null, null, null));
    root.addChild(interfaceDeclaration);
    root.addChild(classDeclaration);

    // Initialize the compiler and the runtime type checker
    AbstractCompiler compiler = new Compiler(null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    // Perform the compilation and the type checking passes
    runtimeTypeCheck.process(null, root);

    // Create an instance of the class and check the function call
    Node instance = new Node(Token.NEW, classDeclaration, null);
    Node call = new Node(Token.CALL, function, instance);
    Node result = NodeUtil.executeFunction(compiler, function, null, call);

    // Assert that the function call returns the expected result
    assertEquals(instance, result);
  }

  @Test
  public void testRuntimeTypeCheck_ValidFunctionWithUnionTypeArguments() {
    // Create a valid Node tree for testing
    Node root = new Node(Token.SCRIPT, null, null);
    Node function = NodeUtil.newFunctionNode("testFunction", new Node[0], null);
    root.addChild(function);

    // Initialize the compiler
    AbstractCompiler compiler = new Compiler(null);

    // Perform the compilation pass
    compiler.process(null, root);

    // Initialize the runtime type checker with the compiled code
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    // Perform the type checking pass
    runtimeTypeCheck.process(null, root);

    // Assert that the function has the expected markers and checks
    FunctionType functionType = (FunctionType) function.getJSType();
    assertTrue(functionType.getMarkers().contains("instance_of__testFunction"));
    assertNotNull(functionType.getCheckers());

    // Create some union type arguments and check the function call
    Node arg1 = Node.newNumber(1);
    Node arg2 = Node.newString("hello");
    Node call = new Node(Token.CALL, function, arg1, arg2);

    try {
      Node result = NodeUtil.executeFunction(compiler, function, null, call);
      fail("Expected a TypeError");
    } catch (TypeError e) {
      // Expected type error

      // Check the error message
      String errorMessage = e.getMessage();
      assertTrue(errorMessage.contains("Expected string but found number"));

      // Check the alternative checks
      FunctionType checkers = functionType.getCheckers();
      assertEquals(JSType.STRING_VALUE_TYPE, checkers.get(0).getType());
      assertEquals(JSType.NUMBER_VALUE_TYPE, checkers.get(1).getType());
    }
  }

  @Test
  public void testRuntimeTypeCheck_ExternFunctionWithUnknownImplementation() {
    // Create a valid Node tree for testing
    Node root = new Node(Token.SCRIPT, null, null);
    Node functionDeclaration = new Node(Token.VAR, null, null);
    functionDeclaration.setName(new Node(Token.IDENT, "MyFunction"));
    functionDeclaration.addChild(
        new Node(Token.TYPES, null, new Node(Token.FUNCTION, null, null, null,
            new Node[0], null, null, null, null, null)));
    root.addChild(functionDeclaration);

    // Initialize the compiler and the runtime type checker
    AbstractCompiler compiler = new Compiler(null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    // Perform the compilation and the type checking passes
    runtimeTypeCheck.process(null, root);

    // Assert that no markers or checks are added for the function
    FunctionType functionType = (FunctionType) functionDeclaration.getFirstChild();
    assertFalse(functionType.getMarkers().isEmpty());
    assertNull(functionType.getCheckers());
  }

  @Test
  public void testRuntimeTypeCheck_ExternFunctionWithImplementation() {
    // Create a valid Node tree for testing
    Node root = new Node(Token.SCRIPT, null, null);
    Node functionDeclaration = new Node(Token.VAR, null, null);
    functionDeclaration.setName(new Node(Token.IDENT, "MyFunction"));
    functionDeclaration.addChild(
        new Node(Token.TYPES, null, new Node(Token.FUNCTION, null, null, null,
            new Node[0], null, null, null, null, null)));
    root.addChild(functionDeclaration);

    // Initialize the compiler and the runtime type checker
    AbstractCompiler compiler = new Compiler(null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    // Perform the compilation pass
    compiler.process(null, root);

    // Create an implementation for the function and add it to the compiler
    Node implementation = NodeUtil.newFunctionNode("MyFunction", new Node[0], null);
    Node functionNode = functionDeclaration.getFirstChild();
    Node functionType = (FunctionType) functionNode.getJSType();
    functionType.setImplementation(implementation);
    functionNode.setJSType(functionType);

    // Perform the type checking pass
    runtimeTypeCheck.process(null, root);

    // Assert that the function has the expected markers and checks
    FunctionType functionType = (FunctionType) functionDeclaration.getFirstChild();
    assertTrue(functionType.getMarkers().contains("instance_of__MyFunction"));
    assertNotNull(functionType.getCheckers());

    // Create some arguments and check the function call
    Node arg1 = Node.newNumber(1);
    Node arg2 = Node.newBoolean(true);
    Node call = new Node(Token.CALL, functionDeclaration, arg1, arg2);
    Node result = NodeUtil.executeFunction(compiler, functionDeclaration, null, call);

    // Assert that the function call returns the expected result
    assertEquals(Node.newNumber(1), result);
  }

  @Test
  public void testRuntimeTypeCheck_ExternClassWithUnknownImplementation() {
    // Create a valid Node tree for testing
    Node root = new Node(Token.SCRIPT, null, null);
    Node classDeclaration = new Node(Token.VAR, null, null);
    classDeclaration.setName(new Node(Token.IDENT, "MyClass"));
    classDeclaration.addChild(new Node(Token.TYPES, null, null));
    root.addChild(classDeclaration);

    // Initialize the compiler and the runtime type checker
    AbstractCompiler compiler = new Compiler(null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    // Perform the compilation and the type checking passes
    runtimeTypeCheck.process(null, root);

    // Assert that no markers or checks are added for the class
    ObjectType classType = (ObjectType) classDeclaration.getFirstChild();
    assertFalse(classType.getMarkers().isEmpty());
    assertNull(classType.getCheckers());
  }

  @Test
  public void testRuntimeTypeCheck_ExternClassWithImplementation() {
    // Create a valid Node tree for testing
    Node root = new Node(Token.SCRIPT, null, null);
    Node classDeclaration = new Node(Token.VAR, null, null);
    classDeclaration.setName(new Node(Token.IDENT, "MyClass"));
    classDeclaration.addChild(new Node(Token.TYPES, null, null));
    root.addChild(classDeclaration);

    // Initialize the compiler and the runtime type checker
    AbstractCompiler compiler = new Compiler(null);
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);

    // Perform the compilation pass
    compiler.process(null, root);

    // Create an implementation for the class and add it to the compiler
    Node implementation = new Node(Token.CLASS, null, null, null);
    implementation.addChild(new Node(Token.EXTENDS, null, new Node(Token.IDENT, "Object")));
    implementation.addChild(new Node(Token.BLOCK, null, null));
    Node classNode = classDeclaration.getFirstChild();
    ObjectType classType = (ObjectType) classNode;
    classType.setImplementation(implementation);
    classNode.setJSType(classType);

    // Perform the type checking pass
    runtimeTypeCheck.process(null, root);

    // Assert that the class has the expected markers and checks
    ObjectType classType = (ObjectType) classDeclaration.getFirstChild();
    assertTrue(classType.getMarkers().contains("instance_of__MyClass"));
    assertNotNull(classType.getCheckers());

    // Create an instance of the class and check its type
    Node instance = new Node(Token.NEW, classDeclaration, null);
    Node instanceType = ((ObjectType) instance.getJSType());
    assertEquals("MyClass", instanceType.getReferenceName());
  }
}