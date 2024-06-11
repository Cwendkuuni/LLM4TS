import static com.google.javascript.jscomp.JSError.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class VarCheckTest {

  @Test
  public void testUndefinedVariable() {
    String jsCode = "var x; x = 1;";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(JSC_UNDEFINED_VARIABLE, CheckLevel.ERROR);

    Node root = new Parser(options.getLanguageMode()).parse(
        jsCode, "Test", null).getTree();

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new VarCheck(compiler).process(null, root);

    try {
      compiler.compile(root, options, null);
      fail("Compilation should have failed due to undefined variable 'x'.");
    } catch (JSError e) {
      assertEquals("JSC_UNDEFINED_VARIABLE", e.getType().name());
      assertEquals("variable x is undefined", e.getMessage());
    }
  }

  @Test
  public void testFilePrivateVariableAccess() {
    String jsCodeFile1 = "function foo() {\n" +
        "  var x = 1;\n" +
        "}\n";

    String jsCodeFile2 = "function bar() {\n" +
        "  foo.x = 2;\n" +
        "}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    Node rootFile1 = new Parser(options.getLanguageMode()).parse(
        jsCodeFile1, "File1.js", null).getTree();
    Node rootFile2 = new Parser(options.getLanguageMode()).parse(
        jsCodeFile2, "File2.js", rootFile1).getTree();

    NodeTraversal traversal = new NodeTraversal(rootFile1, compiler);
    new VarCheck(compiler).process(rootFile1, rootFile1);

    // Add file2 to the compiler input list.
    CompilerInput input = new CompilerInput("File2.js", null, null, jsCodeFile2);
    compiler.addInput(input);

    try {
      compiler.compile(rootFile1, options, null);
      fail("Compilation should have failed due to file-private variable access.");
    } catch (JSError e) {
      assertEquals("JSC_VIOLATED_FILE_PRIVATE_VAR_ACCESS", e.getType().name());
      assertEquals("file-private variable x of File1.js cannot be accessed from File2.js", e.getMessage());
    }
  }

  @Test
  public void testDeclaredModuleDependency() {
    String depModuleJsCode = "var x = 1;";

    String mainModuleJsCode = "require('DepModule').x;";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.addDependency("DepModule.js", depModuleJsCode);

    Node rootMain = new Parser(options.getLanguageMode()).parse(
        mainModuleJsCode, "MainModule.js", null).getTree();

    NodeTraversal traversal = new NodeTraversal(rootMain, compiler);
    new VarCheck(compiler).process(null, rootMain);

    try {
      compiler.compile(rootMain, options, null);
      fail("Compilation should have succeeded with a warning for missing module dependency.");
    } catch (JSError ignored) {
      // Expected warning
    }

    options.setWarningLevel(JSC_MISSING_MODULE_DEPENDENCY, CheckLevel.ERROR);

    try {
      compiler.compile(rootMain, options, null);
      fail("Compilation should have failed due to missing module dependency.");
    } catch (JSError e) {
      assertEquals("JSC_MISSING_MODULE_DEPENDENCY", e.getType().name());
      assertEquals("missing module dependency; MainModule.js should depend on DepModule.js because it references x", e.getMessage());
    }
  }

  @Test
  public void testStrictModuleDependency() {
    String depModuleJsCode = "var x = 1;";

    String mainModuleJsCode = "require('DepModule').x;";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.addDependency("DepModule.js", depModuleJsCode);
    options.setErrorLevel(JSC_VIOLATED_MODULE_DEPENDENCY, ErrorLevel.WARNING);

    Node rootMain = new Parser(options.getLanguageMode()).parse(
        mainModuleJsCode, "MainModule.js", null).getTree();

    NodeTraversal traversal = new NodeTraversal(rootMain, compiler);
    new VarCheck(compiler, true).process(null, rootMain);

    try {
      compiler.compile(rootMain, options, null);
      // Expected warning
    } catch (JSError ignored) {
    }

    options.setErrorLevel(JSC_VIOLATED_MODULE_DEPENDENCY, ErrorLevel.ERROR);

    try {
      compiler.compile(rootMain, options, null);
      fail("Compilation should have failed due to strict module dependency violation.");
    } catch (JSError e) {
      assertEquals("JSC_VIOLATED_MODULE_DEPENDENCY", e.getType().name());
      assertEquals("module MainModule cannot reference x, defined in DepModule", e.getMessage());
    }
  }

  @Test
  public void testNameReferenceInExterns() {
    String jsCode = "//@externs\n" +
        "var x = 1;\n";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    Node root = new Parser(options.getLanguageMode()).parse(
        jsCode, "Test.js", null).getTree();

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new VarCheck(compiler).process(null, root);

    try {
      compiler.compile(root, options, null);
      fail("Compilation should have failed due to name reference in externs.");
    } catch (JSError e) {
      assertEquals("JSC_NAME_REFERENCE_IN_EXTERNS", e.getType().name());
      assertEquals("accessing name x in externs has no effect", e.getMessage());
    }
  }

  @Test
  public void testUndefinedExternVariable() {
    String jsCode = "//@externs\n" +
        "var x;\n";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    Node root = new Parser(options.getLanguageMode()).parse(
        jsCode, "Test.js", null).getTree();

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new VarCheck(compiler).process(null, root);

    try {
      compiler.compile(root, options, null);
      fail("Compilation should have failed due to undefined extern variable 'x'.");
    } catch (JSError e) {
      assertEquals("JSC_UNDEFINED_EXTERN_VAR_ERROR", e.getType().name());
      assertEquals("name x is not undefined in the externs.", e.getMessage());
    }
  }

  @Test
  public void testValidFunctionDeclaration() {
    String jsCode = "function foo() {}";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    Node root = new Parser(options.getLanguageMode()).parse(
        jsCode, "Test.js", null).getTree();

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new VarCheck(compiler).process(null, root);

    try {
      compiler.compile(root, options, null);
      // Expected success
    } catch (JSError ignored) {
    }
  }

  @Test
  public void testInvalidFunctionDeclaration() {
    String jsCode = "function";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    Node root = new Parser(options.getLanguageMode()).parse(
        jsCode, "Test.js", null).getTree();

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new VarCheck(compiler).process(null, root);

    try {
      compiler.compile(root, options, null);
      fail("Compilation should have failed due to invalid function declaration.");
    } catch (JSError e) {
      assertEquals("JSC_INVALID_FUNCTION_DECL", e.getType().name());
      assertEquals("Syntax error: function declaration must have a name", e.getMessage());
    }
  }

  @Test
  public void testDuplicateVariableDeclaration() {
    String jsCode = "var x;\n" +
        "var x;";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    Node root = new Parser(options.getLanguageMode()).parse(
        jsCode, "Test.js", null).getTree();

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new VarCheck(compiler).process(null, root);

    try {
      compiler.compile(root, options, null);
      fail("Compilation should have failed due to duplicate variable declaration.");
    } catch (JSError e) {
      assertEquals("JSC_DUPLICATE_VARIABLE_DECLARATION", e.getType().name());
      assertEquals("Duplicate variable declaration 'x'", e.getMessage());
    }
  }

  @Test
  public void testVariableDeclarationInExterns() {
    String jsCode = "//@externs\n" +
        "var x = 1;\n";

    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();

    Node root = new Parser(options.getLanguageMode()).parse(
        jsCode, "Test.js", null).getTree();

    NodeTraversal traversal = new NodeTraversal(root, compiler);
    new VarCheck(compiler).process(null, root);

    try {
      compiler.compile(root, options, null);
      // Expected success
    } catch (JSError ignored) {
    }
  }
}