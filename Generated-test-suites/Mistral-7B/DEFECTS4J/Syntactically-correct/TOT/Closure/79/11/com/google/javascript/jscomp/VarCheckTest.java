import static org.junit.Assert.*;
import static com.google.javascript.jscomp.JSEmitter.Type.VAR;
import static com.google.javascript.jscomp.JSEmitter.Type.FUNCTION;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.Var.DECLARED;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestUtils;
import com.google.javascript.jscomp.check.AbstractChecker;
import com.google.javascript.jscomp.check.CheckerFactory;
import com.google.javascript.jscomp.check.Error;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class VarCheckTest {

  private final Compiler compiler;
  private final SourceFile testSource;
  private final String testCode;

  public VarCheckTest() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setPrintStackTraces(false);
    options.setThrowExceptionOnError(true);
    this.compiler = new Compiler(options);

    this.testSource = new SourceFile("Test.java", "package test;\n");
    this.testCode = "class Test {\n" +
        "  static void main() {\n" +
        "    var x;\n" +
        "    var y;\n" +
        "    function foo() {\n" +
        "      var z;\n" +
        "      x = y;\n" +
        "      y = x;\n" +
        "    }\n" +
        "    foo();\n" +
        "  }\n" +
        "}\n";
    this.testSource.appendText(this.testCode);
  }

  @org.junit.Test
  public void testUndefinedVariableInNormalCode() throws IOException {
    CompilerPass pass = new VarCheck(compiler);
    Node root = NodeUtil.compile(this.compiler, this.testSource);

    NodeTraversal.traverseRoots(compiler, Arrays.asList(root), pass);
    List<Error> errors = pass.getErrors();
    assertEquals(1, errors.size());
    Error error = errors.get(0);
    assertEquals("variable x is undefined", error.getMessage());
    assertEquals("JSC_UNDEFINED_VARIABLE", error.getType().getName());
  }

  @org.junit.Test
  public void testUndefinedVariableInExterns() throws IOException {
    CompilerPass pass = new VarCheck(compiler, true);
    Node root = NodeUtil.compile(this.compiler, this.testSource);

    NodeTraversal.traverseRoots(compiler, Arrays.asList(root), pass);
    List<Error> errors = pass.getErrors();
    assertEquals(1, errors.size());
    Error error = errors.get(0);
    assertEquals("accessing name x in externs has no effect", error.getMessage());
    assertEquals("JSC_NAME_REFERENCE_IN_EXTERNS", error.getType().getName());
  }

  @org.junit.Test
  public void testDuplicateVariableDeclaration() throws IOException {
    CompilerPass pass = new VarCheck(compiler);
    Node root = NodeUtil.compile(this.compiler, this.testSource);

    Node newRoot = NodeUtil.cloneNode(root, Node.cloneType.DEEP);
    Node n = newRoot.getFirstChild();
    Node duplicate = Node.newName("x");
    Node duplicateParent = Node.newExpressionStatement(
        Node.newAssignment(n, duplicate));
    newRoot.addChildToBack(duplicateParent);

    NodeTraversal.traverseRoots(compiler, Arrays.asList(newRoot), pass);
    List<Error> errors = pass.getErrors();
    assertEquals(1, errors.size());
    Error error = errors.get(0);
    assertEquals("duplicate variable declaration for x", error.getMessage());
    // This error type is not defined in the codebase, so we can't use
    // assertEquals directly.
    assertTrue(error.getMessage().contains("duplicate"));
  }

  @org.junit.Test
  public void testUndefinedVariableInFunctionExpression() throws IOException {
    CompilerPass pass = new VarCheck(compiler);
    Node root = NodeUtil.compile(this.compiler, this.testSource);

    Node newRoot = NodeUtil.cloneNode(root, Node.cloneType.DEEP);
    Node function = Node.newFunction(
        "foo",
        ImmutableMap.of(),
        Arrays.asList(Node.newVar("x")),
        Arrays.asList(Node.newExpressionStatement(
            Node.newAssignment(
                Node.newName("y"),
                Node.newName("x")))));
    newRoot.addChildToBack(function);

    NodeTraversal.traverseRoots(compiler, Arrays.asList(newRoot), pass);
    List<Error> errors = pass.getErrors();
    assertEquals(1, errors.size());
    Error error = errors.get(0);
    assertEquals("variable x is undefined", error.getMessage());
    assertEquals("JSC_UNDEFINED_VARIABLE", error.getType().getName());
  }

  @org.junit.Test
  public void testUndefinedVariableInFunctionDeclaration() throws IOException {
    CompilerPass pass = new VarCheck(compiler);
    Node root = NodeUtil.compile(this.compiler, this.testSource);

    Node newRoot = NodeUtil.cloneNode(root, Node.cloneType.DEEP);
    Node function = Node.newFunctionDeclaration(
        "foo",
        ImmutableMap.of(),
        Collections.emptyList(),
        Arrays.asList(Node.newVar("x")),
        Arrays.asList());
    newRoot.addChildToBack(function);

    NodeTraversal.traverseRoots(compiler, Arrays.asList(newRoot), pass);
    List<Error> errors = pass.getErrors();
    assertEquals(1, errors.size());
    Error error = errors.get(0);
    assertEquals("variable x is undefined", error.getMessage());
    assertEquals("JSC_UNDEFINED_VARIABLE", error.getType().getName());
  }

  @org.junit.Test
  public void testUndefinedVariableInGetProp() throws IOException {
    CompilerPass pass = new VarCheck(compiler);
    Node root = NodeUtil.compile(this.compiler, this.testSource);

    Node newRoot = NodeUtil.cloneNode(root, Node.cloneType.DEEP);
    Node getProp = Node.newGetProp(
        Node.newExpressionStatement(
            Node.newAssignment(
                Node.newName("y"),
                Node.newName("x"))),
        "x");
    newRoot.addChildToBack(getProp);

    NodeTraversal.traverseRoots(compiler, Arrays.asList(newRoot), pass);
    List<Error> errors = pass.getErrors();
    assertEquals(1, errors.size());
    Error error = errors.get(0);
    assertEquals("variable x is undefined", error.getMessage());
    assertEquals("JSC_UNDEFINED_VARIABLE", error.getType().getName());
  }

  @org.junit.Test
  public void testVariableAccessOutsideOfModule() throws IOException {
    CompilerPass pass = new VarCheck(compiler);
    Node root = NodeUtil.compile(this.compiler, this.testSource);

    String otherModuleName = "OtherModule";
    SourceFile otherSource = new SourceFile(otherModuleName, "package other;" +
        "class Other {\n" +
        "  static void main() {\n" +
        "    var test = new Test().new Test();\n" +
        "    test.foo();\n" +
        "  }\n" +
        "}\n");

    Compiler compilerWithOtherModule = new Compiler(new CompilerOptions());
    compilerWithOtherModule.addModule(otherModuleName, otherSource);
    compilerWithOtherModule.compileModule(otherModuleName, null);

    Node otherRoot = compilerWithOtherModule.getAstForCurrentSource();

    NodeTestingUtil.injectTestFileIntoCompiler(
        compilerWithOtherModule, this.testSource);

    NodeTraversal.traverseRoots(compilerWithOtherModule, Arrays.asList(root, otherRoot), pass);
    List<Error> errors = pass.getErrors();
    assertEquals(2, errors.size());

    Error errorForTest = findErrorForModule("Test", errors);
    assertEquals("variable x is undefined", errorForTest.getMessage());
    assertEquals("JSC_UNDEFINED_VARIABLE", errorForTest.getType().getName());

    Error errorForOtherModule = findErrorForModule("OtherModule", errors);
    assertEquals("module OtherModule cannot reference Test, defined in Test, " +
        "since Test loads after OtherModule", errorForOtherModule.getMessage());
    assertEquals("JSC_VIOLATED_MODULE_DEPENDENCY", errorForOtherModule.getType().getName());
  }

  @org.junit.Test
  public void testVariableAccessIncorrectModuleDependency() throws IOException {
    CompilerPass pass = new VarCheck(compiler);
    Node root = NodeUtil.compile(this.compiler, this.testSource);

    String otherModuleName = "OtherModule";
    SourceFile otherSource = new SourceFile(otherModuleName, "package other;" +
        "class Other {\n" +
        "  static void main() {\n" +
        "    var test = new Test().new Test();\n" +
        "    test.foo();\n" +
        "  }\n" +
        "}\n");

    Compiler compilerWithOtherModule = new Compiler(new CompilerOptions());
    compilerWithOtherModule.addModule(otherModuleName, otherSource);
    compilerWithOtherModule.compileModule(otherModuleName, null);

    Node otherRoot = compilerWithOtherModule.getAstForCurrentSource();

    NodeTestingUtil.injectTestFileIntoCompiler(
        compilerWithOtherModule, this.testSource);

    // Reorder the modules to make it seem like OtherModule is loaded before Test
    TestUtils.swapFiles(compiler, "Test.java", "OtherModule.java");

    NodeTraversal.traverseRoots(compiler, Arrays.asList(root, otherRoot), pass);
    List<Error> errors = pass.getErrors();
    assertEquals(2, errors.size());

    Error errorForTest = findErrorForModule("Test", errors);
    assertEquals("module Test cannot reference OtherModule, defined in OtherModule",
        errorForTest.getMessage());
    assertEquals("JSC_STRICT_MODULE_DEPENDENCY", errorForTest.getType().getName());

    Error errorForOtherModule = findErrorForModule("OtherModule", errors);
    assertEquals("module OtherModule cannot reference Test, defined in Test",
        errorForOtherModule.getMessage());
    assertEquals("JSC_STRICT_MODULE_DEPENDENCY", errorForOtherModule.getType().getName());
  }

  @org.junit.Test
  public void testVariableAccessCorrectModuleDependency() throws IOException {
    CompilerPass pass = new VarCheck(compiler);
    Node root = NodeUtil.compile(this.compiler, this.testSource);

    String otherModuleName = "OtherModule";
    SourceFile otherSource = new SourceFile(otherModuleName, "package other;" +
        "class Other {\n" +
        "  static void main() {\n" +
        "    var test = new Test().new Test();\n" +
        "    test.foo();\n" +
        "  }\n" +
        "}\n");

    Compiler compilerWithOtherModule = new Compiler(new CompilerOptions());
    compilerWithOtherModule.addModule(otherModuleName, otherSource);
    compilerWithOtherModule.compileModule(otherModuleName, null);

    Node otherRoot = compilerWithOtherModule.getAstForCurrentSource();

    NodeTestingUtil.injectTestFileIntoCompiler(
        compilerWithOtherModule, this.testSource);

    // Make sure OtherModule is loaded before Test
    compilerWithOtherModule.loadModule("Test");

    NodeTraversal.traverseRoots(compilerWithOtherModule, Arrays.asList(root, otherRoot), pass);
    List<Error> errors = pass.getErrors();
    assertEquals(0, errors.size());
  }

  private Error findErrorForModule(String moduleName, List<Error> errors) {
    for (Error error : errors) {
      Node location = error.getLocation();
      Scope scope = NodeTraversal.getScopeAtNode(location);
      if (scope != null && scope.isModuleScope(moduleName)) {
        return error;
      }
    }
    throw new IllegalStateException("Could not find error for module: " + moduleName);
  }
}