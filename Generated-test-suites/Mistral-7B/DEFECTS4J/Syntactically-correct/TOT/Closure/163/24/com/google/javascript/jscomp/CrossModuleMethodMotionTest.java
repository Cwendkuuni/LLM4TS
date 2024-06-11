import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompileTest.*;

import com.google.javascript.jscomp.AnalyzePrototypeProperties.NameInfo;
import com.google.javascript.rhino.Node;

public class CrossModuleMethodMotionTest {

  public void testMoveMethodAcrossModules() {
    String sourceCode =
        "// Test class in module A.\n"
        + "class A {\n"
        + "  function foo() {\n"
        + "    return 'A.foo';\n"
        + "  }\n"
        + "}\n"
        + "\n"
        + "// Test class in module B.\n"
        + "class B {\n"
        + "  function bar() {\n"
        + "    return 'B.bar';\n"
        + "  }\n"
        + "\n"
        + "  function baz() {\n"
        + "    return A.foo();\n"
        + "  }\n"
        + "}\n";

    Node root = parseCompilationUnit(sourceCode);

    CompilerOptions options = new CompilerOptions(true, null, null);
    Compiler compiler = new AbstractCompiler() {
      @Override
      public Node visitTopLevelDeclaration(Node node, Node parent) {
        // We don't want to process top-level declarations in our tests,
        // as they are not part of any module.
        return super.visitTopLevelDeclaration(null, parent);
      }
    };
    compiler.setOptions(options);
    compiler.compile(root, null, null);

    // Check that the method 'baz' is still in module B and doesn't have a
    // stub.
    Node bazFunction = getFunctionByName(compiler.getRootNode(), "B.baz");
    assertNotNull(bazFunction);
    assertFalse(bazFunction.isFunctionExpression());
    assertFalse(bazFunction.getParent().isCallStatement());

    // Compile the class with method motion enabled.
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new IdGenerator(), true);
    Node newRoot = new Node(Node.ROOT);
    pass.process(null, newRoot);
    compiler.compile(newRoot, null, null);

    // Check that 'baz' has been moved to module A and has a stub function in module B.
    Node newBazFunction = getFunctionByName(compiler.getRootNode(), "A.foo");
    assertNotNull(newBazFunction);
    assertTrue(newBazFunction.isFunctionExpression());
    Node bazStub = getFunctionByName(compiler.getRootNode(), "JSCompiler_stubMethod");
    assertNotNull(bazStub);
    Node bazStubCall = getFunctionCall(bazStub, new Node(Node.NUMBER, 0));
    assertEquals(newBazFunction, bazStubCall.getChild(0));

    Node newBazFunctionInB = getFunctionByName(getModuleByName(compiler.getRootNode(), "B"), "foo");
    assertNotNull(newBazFunctionInB);
    assertTrue(newBazFunctionInB.isFunctionExpression());
    Node unstubCall = getFunctionCall(newBazFunctionInB, new Node(Node.IDENT, "JSCompiler_unstubMethod"));
    Node unstubBody = unstubCall.getChild(1);
    assertEquals(newBazFunction, unstubBody);
  }

  public void testMoveMethodToExternalModule() {
    String sourceCode =
        "// Test class in module A.\n"
        + "class A {\n"
        + "  function foo() {\n"
        + "    return 'A.foo';\n"
        + "  }\n"
        + "}\n"
        + "\n"
        + "// Test class in module B.\n"
        + "class B {\n"
        + "  function bar() {\n"
        + "    return A.foo();\n"
        + "  }\n"
        + "}\n"
        + "\n"
        + "// Externs file for module C.\n"
        + "/** @exports C */\n"
        + "class C {\n"
        + "  function baz() {\n"
        + "    return A.foo();\n"
        + "  }\n"
        + "}"
        + "\n";

    Node root = parseCompilationUnit(sourceCode);

    CompilerOptions options = new CompilerOptions(true, null, null);
    Compiler compiler = new AbstractCompiler() {
      @Override
      public Node visitTopLevelDeclaration(Node node, Node parent) {
        // We don't want to process top-level declarations in our tests,
        // as they are not part of any module.
        return super.visitTopLevelDeclaration(null, parent);
      }
    };
    compiler.setOptions(options);
    compiler.compile(root, null, null);

    // Check that the method 'baz' is still in module C and doesn't have a
    // stub.
    Node bazFunction = getFunctionByName(compiler.getRootNode(), "C.baz");
    assertNotNull(bazFunction);
    assertFalse(bazFunction.isFunctionExpression());
    assertFalse(bazFunction.getParent().isCallStatement());

    // Compile the class with method motion enabled.
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new IdGenerator(), true);
    Node newRoot = new Node(Node.ROOT);
    pass.process(null, newRoot);
    compiler.compile(newRoot, null, null);

    // Check that 'baz' has been moved to module A and has a stub function in module C.
    Node newBazFunction = getFunctionByName(compiler.getRootNode(), "A.foo");
    assertNotNull(newBazFunction);
    assertTrue(newBazFunction.isFunctionExpression());
    Node bazStub = getFunctionByName(compiler.getRootNode(), "JSCompiler_stubMethod");
    assertNotNull(bazStub);
    Node bazStubCall = getFunctionCall(bazStub, new Node(Node.NUMBER, 0));
    assertEquals(newBazFunction, bazStubCall.getChild(0));

    Node newBazFunctionInC = getFunctionByName(getModuleByName(compiler.getRootNode(), "C"), "foo");
    assertNotNull(newBazFunctionInC);
    assertTrue(newBazFunctionInC.isFunctionExpression());
    Node unstubCall = getFunctionCall(newBazFunctionInC, new Node(Node.IDENT, "JSCompiler_unstubMethod"));
    Node unstubBody = unstubCall.getChild(1);
    assertEquals(newBazFunction, unstubBody);
  }

  public void testMoveMethodToExternalModuleWithCanModifyExternsFalse() {
    String sourceCode =
        "// Test class in module A.\n"
        + "class A {\n"
        + "  function foo() {\n"
        + "    return 'A.foo';\n"
        + "  }\n"
        + "}\n"
        + "\n"
        + "// Test class in module B.\n"
        + "class B {\n"
        + "  function bar() {\n"
        + "    return A.foo();\n"
        + "  }\n"
        + "}\n"
        + "\n"
        + "// Externs file for module C.\n"
        + "/** @exports C */\n"
        + "class C {\n"
        + "  function baz() {\n"
        + "    return A.foo();\n"
        + "  }\n"
        + "}"
        + "\n";

    Node root = parseCompilationUnit(sourceCode);

    CompilerOptions options = new CompilerOptions(true, null, null);
    Compiler compiler = new AbstractCompiler() {
      @Override
      public Node visitTopLevelDeclaration(Node node, Node parent) {
        // We don't want to process top-level declarations in our tests,
        // as they are not part of any module.
        return super.visitTopLevelDeclaration(null, parent);
      }
    };
    compiler.setOptions(options);
    compiler.compile(root, null, null);

    // Check that the method 'baz' is still in module C and doesn't have a
    // stub.
    Node bazFunction = getFunctionByName(compiler.getRootNode(), "C.baz");
    assertNotNull(bazFunction);
    assertFalse(bazFunction.isFunctionExpression());
    assertFalse(bazFunction.getParent().isCallStatement());

    // Compile the class with method motion enabled but with canModifyExterns set to false.
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new IdGenerator(), false);
    Node newRoot = new Node(Node.ROOT);
    pass.process(null, newRoot);
    compiler.compile(newRoot, null, null);

    // Check that 'baz' is still in module C and hasn't been moved or stubbed.
    Node newBazFunction = getFunctionByName(compiler.getRootNode(), "C.baz");
    assertNotNull(newBazFunction);
    assertFalse(newBazFunction.isFunctionExpression());
    assertFalse(newBazFunction.getParent().isCallStatement());
  }

  public void testMoveMethodToCyclicModule() {
    String sourceCodeA =
        "// Test class in module A.\n"
        + "class A {\n"
        + "  function foo() {\n"
        + "    return 'A.foo';\n"
        + "  }\n"
        + "}\n";

    String sourceCodeB =
        "// Test class in module B.\n"
        + "class B {\n"
        + "  function bar() {\n"
        + "    return A.foo();\n"
        + "  }\n"
        + "}\n"
        + "\n"
        + "// Test class in module C which depends on both A and B.\n"
        + "class C {\n"
        + "  function baz() {\n"
        + "    return A.foo() + B.foo();\n"
        + "  }\n"
        + "}\n";

    Node rootA = parseCompilationUnit(sourceCodeA);
    Node rootB = parseCompilationUnit(sourceCodeB);
    Node combinedRoot = new Node(Node.ROOT);
    combinedRoot.addChildrenToFront(rootA.getChildren().clone());
    combinedRoot.addChildrenToFront(rootB.getChildren().clone());

    CompilerOptions options = new CompilerOptions(true, null, null);
    Compiler compiler = new AbstractCompiler() {
      @Override
      public Node visitTopLevelDeclaration(Node node, Node parent) {
        // We don't want to process top-level declarations in our tests,
        // as they are not part of any module.
        return super.visitTopLevelDeclaration(null, parent);
      }
    };
    compiler.setOptions(options);
    compiler.compile(combinedRoot, null, null);

    // Check that 'baz' is still in module C and doesn't have a stub.
    Node bazFunction = getFunctionByName(compiler.getRootNode(), "C.baz");
    assertNotNull(bazFunction);
    assertFalse(bazFunction.isFunctionExpression());
    assertFalse(bazFunction.getParent().isCallStatement());

    // Compile the class with method motion enabled.
    CrossModuleMethodMotion pass = new CrossModuleMethodMotion(compiler, new IdGenerator(), true);
    Node newRoot = new Node(Node.ROOT);
    pass.process(null, newRoot);
    compiler.compile(newRoot, null, null);

    // Check that 'baz' is still in module C and hasn't been moved or stubbed.
    Node newBazFunction = getFunctionByName(compiler.getRootNode(), "C.baz");
    assertNotNull(newBazFunction);
    assertFalse(newBazFunction.isFunctionExpression());
    assertFalse(newBazFunction.getParent().isCallStatement());
  }
}