package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassTest;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerOptions.CompilationLevel;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.rhino.IR;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link ProcessCommonJSModules}.
 */
public class ProcessCommonJSModulesTest extends CompilerPassTest {

  private Compiler compiler;
  private CompilerPass processor;
  private CompilerOptions options;

  @Before
  public void setUp() {
    compiler = newCompiler();
    options = new CompilerOptions();
    options.setCompilationLevel(CompilationLevel.SIMPLE);
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);
    processor = new ProcessCommonJSModules(compiler, "test/", true);
  }

  /**
   * Test that a CommonJS module is rewritten correctly.
   */
  @Test
  public void testSimpleCommonJsModule() {
    String sourceCode =
        "var module = module || {exports: {}};\n" +
        "exports.hello = function() {\n" +
        "  return 'Hello World!';\n" +
        "};\n";

    Node node = parseNode(sourceCode);

    NodeTraversal traversal = new NodeTraversal(compiler, node, options);
    processor.process(null, node);
    AbstractPostOrderCallback callback = new TestCallback();
    NodeTraversal.traverse(compiler, node, callback);

    String expectedCode =
        "var module = module || {exports: {}};\n" +
        "module.exports.$$test = module.exports;\n" +
        "exports.hello = function() {\n" +
        "  return 'Hello World!';\n" +
        "};\n" +
        "goog.provide('test.$$test');\n";

    String actualCode = callback.getOutput();
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  /**
   * Test that multiple CommonJS modules are rewritten correctly.
   */
  @Test
  public void testMultipleCommonJsModules() {
    String sourceCode =
        "var module1 = module1 || {exports: {}};\n" +
        "exports1.hello = function() {\n" +
        "  return 'Hello from module1!';\n" +
        "};\n" +
        "\n" +
        "var module2 = module2 || {exports: {}};\n" +
        "exports2.hello = function() {\n" +
        "  return 'Hello from module2!';\n" +
        "};\n";

    Node node = parseNode(sourceCode);

    NodeTraversal traversal = new NodeTraversal(compiler, node, options);
    processor.process(null, node);
    AbstractPostOrderCallback callback = new TestCallback();
    NodeTraversal.traverse(compiler, node, callback);

    String expectedCode =
        "var module1 = module1 || {exports: {}};\n" +
        "module1.exports.$$module1 = module1.exports;\n" +
        "exports1.hello = function() {\n" +
        "  return 'Hello from module1!';\n" +
        "};\n" +
        "\n" +
        "var module2 = module2 || {exports: {}};\n" +
        "module2.exports.$$module2 = module2.exports;\n" +
        "exports2.hello = function() {\n" +
        "  return 'Hello from module2!';\n" +
        "};\n" +
        "goog.provide('module1.$$module1');\n" +
        "goog.provide('module2.$$module2');\n";

    String actualCode = callback.getOutput();
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  /**
   * Test that require calls are replaced by direct references.
   */
  @Test
  public void testRequireCalls() {
    String sourceCode =
        "var module = module || {exports: {}};\n" +
        "exports.hello = function() {\n" +
        "  return require('./other')();\n" +
        "};\n" +
        "\n" +
        "var other = require('./other');\n" +
        "function otherFunction() {\n" +
        "  return 'Hello from other!';\n" +
        "}\n";

    Node node = parseNode(sourceCode);

    NodeTraversal traversal = new NodeTraversal(compiler, node, options);
    processor.process(null, node);
    AbstractPostOrderCallback callback = new TestCallback();
    NodeTraversal.traverse(compiler, node, callback);

    String expectedCode =
        "var module = module || {exports: {}};\n" +
        "exports.hello = function() {\n" +
        "  return otherFunction;\n" +
        "};\n" +
        "\n" +
        "function otherFunction() {\n" +
        "  return 'Hello from other!';\n" +
        "}\n" +
        "goog.require('other');\n" +
        "goog.provide('other');\n";

    String actualCode = callback.getOutput();
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  /**
   * Test that module exports are correctly renamed.
   */
  @Test
  public void testModuleExports() {
    String sourceCode =
        "var module = module || {exports: {}};\n" +
        "exports.hello = function() {\n" +
        "  return module.exports.world;\n" +
        "};\n" +
        "\n" +
        "module.exports.world = 'Hello World!';\n";

    Node node = parseNode(sourceCode);

    NodeTraversal traversal = new NodeTraversal(compiler, node, options);
    processor.process(null, node);
    AbstractPostOrderCallback callback = new TestCallback();
    NodeTraversal.traverse(compiler, node, callback);

    String expectedCode =
        "var module = module || {exports: {}};\n" +
        "exports.hello = function() {\n" +
        "  return module.exports.$$test_world;\n" +
        "};\n" +
        "\n" +
        "module.exports.$$test = module.exports;\n" +
        "module.exports.$$test_world = 'Hello World!';\n" +
        "goog.provide('test.$$test');\n";

    String actualCode = callback.getOutput();
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  /**
   * Test that module dependencies are reported correctly.
   */
  @Test
  public void testReportDependencies() {
    String sourceCode =
        "var module = module || {exports: {}};\n" +
        "exports.hello = function() {\n" +
        "  return require('./other')();\n" +
        "};\n" +
        "\n" +
        "module.exports.world = 'Hello World!';\n" +
        "\n" +
        "var other = require('./other');\n" +
        "function otherFunction() {\n" +
        "  return 'Hello from other!';\n" +
        "}\n";

    Node node = parseNode(sourceCode);

    NodeTraversal traversal = new NodeTraversal(compiler, node, options);
    AbstractCompiler abstractCompiler = mock(Compiler.class);
    when(abstractCompiler.getInput()).thenReturn(new TestCompilerInput());
    processor = new ProcessCommonJSModules(abstractCompiler, "test/");
    processor.process(null, node);

    Set<String> dependencies = ((TestCompilerInput) abstractCompiler.getInput())
        .getRequireDependencies();

    String expectedDependency = "other";
    assertThat(dependencies).containsExactly(expectedDependency);
  }

  /**
   * Test that module exports are emitted correctly.
   */
  @Test
  public void testEmitModuleExports() {
    String sourceCode =
        "var module = module || {exports: {}};\n" +
        "exports.hello = function() {\n" +
        "  return 'Hello World!';\n" +
        "};\n";

    Node node = parseNode(sourceCode);

    NodeTraversal traversal = new NodeTraversal(compiler, node, options);
    AbstractCompiler abstractCompiler = mock(Compiler.class);
    when(abstractCompiler.getInput()).thenReturn(new TestCompilerInput());
    ProcessCommonJSModules processor = new ProcessCommonJSModules(
        abstractCompiler, "test/", true);
    processor.process(null, node);

    JSModule module = ((TestCompilerInput) abstractCompiler.getInput())
        .getModule();

    String expectedModuleName = "test.module$test";
    String actualModuleName = module.getName();
    assertThat(actualModuleName).isEqualTo(expectedModuleName);

    String expectedCode =
        "goog.provide('test.module$test');\n" +
        "exports = module.exports = {};\n" +
        "exports.hello = function() {\n" +
        "  return 'Hello World!';\n" +
        "};\n";

    String actualCode = module.getCode();
    assertThat(actualCode).isEqualTo(expectedCode);
  }

  /**
   * Callback class that collects the output of the test.
   */
  private class TestCallback extends AbstractPostOrderCallback {

    private StringBuilder output = new StringBuilder();

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isStatement()) {
        output.append(n.toSource());
        output.append("\n");
      }
    }

    public String getOutput() {
      return output.toString();
    }
  }

  /**
   * Input for the compiler that mocks the file name.
   */
  private static class TestCompilerInput extends AbstractCompilerInput {

    private final Set<String> requireDependencies = Sets.newHashSet();
    private JSModule module;

    TestCompilerInput() {
    }

    void addProvide(String name) {
      super.addProvide(name);
    }

    void addRequire(String name) {
      requireDependencies.add(name);
    }

    void addAndOverrideModule(JSModule module) {
      this.module = module;
    }

    Set<String> getRequireDependencies() {
      return requireDependencies;
    }

    JSModule getModule() {
      return module;
    }
  }
}