package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilationTestHelper;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeOperations;
import com.google.javascript.rhino.SourceName;

@RunWith(JUnit4.class)
public class ProcessCommonJSModulesTest {

  @Test
  public void testSimpleProcessCommonJSModules() throws IOException {

    String input = "// Simple CommonJS module.\n" +
        "var a = 1;\n" +
        "exports.b = 2;\n" +
        "require(\"module2\");\n" +
        "exports.c = 3;\n";

    Compiler compiler = new Compiler();
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler,
        ".", false);

    Node externs = NodeOperations.createRootNode(input);
    Node root = NodeOperations.createRootNode("");

    processCommonJSModules.process(externs, root);

    // Check output.
    String expectedOutput = "// Rewritten CommonJS module.\n" +
        "var a = 1;\n" +
        "var module$exports = {}\n" +
        "goog.provide(\"module1\");\n" +
        "goog.require(\"module2\");\n" +
        "module$exports.b = 2;\n" +
        "module$exports.c = 3;\n";

    String actualOutput = getSourceCode(root);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testProcessCommonJSModulesWithDependentModule()
      throws IOException, URISyntaxException {

    String module1Input = "// module1.js\n" +
        "exports.a = 1";
    String module2Input = "// module2.js\n" +
        "exports.b = 2";

    Compiler compiler = new Compiler();
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler,
        ".", false);

    Node module1Externs = NodeOperations.createRootNode(module1Input);
    Node module1Root = NodeOperations.createRootNode("");

    processCommonJSModules.process(module1Externs, module1Root);

    String module1ModuleName = toModuleName("module1.js");

    Node module2Externs = NodeOperations.createRootNode(module2Input);
    Node module2Root = NodeOperations.createRootNode("");

    Node module1Require = createRequireCall(module1Root, "module2");

    processCommonJSModules.process(module2Externs, module2Root);

    String expectedOutput = "// Rewritten module1.js\n" +
        "var module$module1 = {\n" +
        "  a: 1,\n" +
        "  __googTrans: {\n" +
        "    'module1': 'module1'\n" +
        "  }\n" +
        "};\n" +
        "goog.provide(\"module1\");\n" +
        "goog.require(\"module2\");\n" +
        "module$module1.module$module2 = {\n" +
        "  b: 2\n" +
        "};\n" +
        "module$module1.a = 1;\n" +
        "goog.export(module$module1, 'module$module1', module$module1);\n" +
        "\n" +
        "// Rewritten module2.js\n" +
        "goog.provide(\"module2\");\n" +
        "exports.b = 2;\n";

    String actualOutput = getSourceCode(module1Root);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testProcessCommonJSModulesWithModuleExports()
      throws IOException {

    String input = "// Simple CommonJS module with module.exports.\n" +
        "exports.a = 1;\n" +
        "exports.module = {\n" +
        "  exports: exports\n" +
        "};\n";

    Compiler compiler = new Compiler();
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler,
        ".", false);

    Node externs = NodeOperations.createRootNode(input);
    Node root = NodeOperations.createRootNode("");

    processCommonJSModules.process(externs, root);

    // Check output.
    String expectedOutput = "// Rewritten CommonJS module.\n" +
        "var a = 1;\n" +
        "var module$exports = {}\n" +
        "goog.provide(\"module1\");\n" +
        "goog.require(\"\");\n" +
        "module$exports.a = 1;\n" +
        "module$exports.module = {\n" +
        "  exports: module$exports\n" +
        "};\n" +
        "goog.export(module$exports, 'module$module', module$exports.module);\n";

    String actualOutput = getSourceCode(root);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testProcessCommonJSModulesWithTwoModuleExports()
      throws IOException {

    String input = "// Simple CommonJS module with two module.exports.\n" +
        "exports.a = 1;\n" +
        "exports.module = {\n" +
        "  exports: exports\n" +
        "};\n" +
        "exports.module2 = {\n" +
        "  exports: exports\n" +
        "};\n";

    Compiler compiler = new Compiler();
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler,
        ".", false);

    Node externs = NodeOperations.createRootNode(input);
    Node root = NodeOperations.createRootNode("");

    processCommonJSModules.process(externs, root);

    // Check output.
    String expectedOutput = "// Rewritten CommonJS module.\n" +
        "var module$exports = {}\n" +
        "goog.provide(\"module1\");\n" +
        "goog.require(\"\");\n" +
        "module$exports.a = 1;\n" +
        "module$exports.module = {\n" +
        "  exports: module$exports\n" +
        "};\n" +
        "module$exports.module2 = {\n" +
        "  exports: module$exports\n" +
        "};\n" +
        "goog.export(module$exports, 'module$module', module$exports.module);\n" +
        "goog.export(module$exports, 'module$module2', module$exports.module2);\n";

    String actualOutput = getSourceCode(root);
    assertEquals(expectedOutput, actualOutput);
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequire()
      throws IOException, URISyntaxException {

    String module1Input = "// module1.js\n" +
        "exports.a = require(\"./module2\");\n";
    String module2Input = "// module2.js\n" +
        "exports.b = 2;\n";

    Compiler compiler = new Compiler();
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler,
        ".", false);

    Node module1Externs = NodeOperations.createRootNode(module1Input);
    Node module1Root = NodeOperations.createRootNode("");

    processCommonJSModules.process(module1Externs, module1Root);

    String module1ModuleName = toModuleName("module1.js");

    Node module2Externs = NodeOperations.createRootNode(module2Input);
    Node module2Root = NodeOperations.createRootNode("");

    Node module1Require = createRequireCall(module1Root, "module2");

    processCommonJSModules.process(module2Externs, module2Root);

    String expectedOutput = "// Rewritten module1.js\n" +
        "var module$module1 = {\n" +
        "  a: null,\n" +
        "  __googTrans: {\n" +
        "    'module1': 'module1'\n" +
        "  }\n" +
        "};\n" +
        "goog.provide(\"module1\");\n" +
        "goog.require(require(\"../module2\"));\n" +
        "module$module1.module$module2 = {\n" +
        "  b: 2\n" +
        "};\n" +
        "module$module1.a = module$module1.module$module2;\n" +
        "goog.export(module$module1, 'module$module1', module$module1);\n" +
        "\n" +
        "// Rewritten module2.js\n" +
        "goog.provide(\"module2\");\n" +
        "exports.b = 2;\n";

    String actualOutput = getSourceCode(module1Root);
    assertEquals(expectedOutput, actualOutput);
  }

  private void assertNullResult(Node node) {
    assertNull(node);
  }

  private Node createRequireCall(Node root, String moduleName) {
    Node requireCall = NodeOperations.createCall(
        "require", NodeOperations.createString(moduleName), root);
    return requireCall;
  }

  private String getSourceCode(Node node) {
    CompilationTestHelper.maybeEnableTreeShaking(compiler);
    return CompilationTestHelper.parseAndCompileToText(compiler, node);
  }

  private String toModuleName(String filename) {
    return ProcessCommonJSModules.toModuleName(filename);
  }
}