import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createString;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private static final String FILENAME_PREFIX = "test_";

  @org.junit.Test
  public void testProcessCommonJSModules() throws Exception {
    String inputCode =
        "var exports = module.exports = {\n" +
        "  foo: function() { return 1; },\n" +
        "  bar: function() { return 2; }\n" +
        "};\n" +
        "exports.baz = function() { return 3; }\n" +
        "require('common-module');\n" +
        "function commonModule() {}\n";

    Node externs = NodeFactory.createExterns();
    Node root = NodeFactory.createScript(inputCode);

    CompilerPassManager cpManager = new CompilerPassManager(null);
    CompilerPass pass = new ProcessCommonJSModules(null, FILENAME_PREFIX);
    cpManager.addPass(pass);
    cpManager.setInput(externs, root);
    cpManager.runPasses();

    Node expectedOutput = NodeFactory.createScript("" +
        "var module$exports = {\n" +
        "  foo: function() { return 1; },\n" +
        "  bar: function() { return 2; },\n" +
        "  baz: function() { return 3; }\n" +
        "};\n" +
        "goog.provide(\"test_common-module\");\n" +
        "goog.require(\"test_common-module\");\n" +
        "function test_common-module() {}\n");

    Node actualOutput = cpManager.getOutput();

    assertEquals(expectedOutput.toSource(), actualOutput.toSource());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithDependenciesReporting()
      throws Exception {
    String inputCode =
        "var exports = module.exports = {\n" +
        "  foo: function() { return 1; },\n" +
        "  bar: function() { return 2; }\n" +
        "};\n" +
        "exports.baz = function() { return 3; }\n" +
        "require('common-module');\n" +
        "require('another-module');\n" +
        "function commonModule() {}\n" +
        "function anotherModule() {}\n";

    Node externs = NodeFactory.createExterns();
    Node root = NodeFactory.createScript(inputCode);

    CompilerPassManager cpManager = new CompilerPassManager(null);
    CompilerPass pass = new ProcessCommonJSModules(null, FILENAME_PREFIX, true);
    cpManager.addPass(pass);
    cpManager.setInput(externs, root);
    cpManager.runPasses();

    Set<String> expectedDependencies = Sets.newHashSet("common-module",
        "another-module");
    Set<String> actualDependencies = cpManager.getInput().getRequireFiles();

    assertEquals(expectedDependencies, actualDependencies);
  }

  @org.junit.Test
  public void testProcessCommonJSModulesRelativeRequires()
      throws Exception {
    String inputCode =
        "var exports = module.exports = {\n" +
        "  foo: function() { return 1; },\n" +
        "  bar: function() { return 2; }\n" +
        "};\n" +
        "exports.baz = function() { return 3; }\n" +
        "require('./common-module');\n" +
        "require('../another-module');\n" +
        "function commonModule() {}\n" +
        "function anotherModule() {}\n";

    File commonModuleSource = new File("common-module.js");
    commonModuleSource.createNewFile();
    File anotherModuleSource = new File("../another-module.js");
    anotherModuleSource.createNewFile();

    Node externs = NodeFactory.createExterns();
    Node root = NodeFactory.createScript(inputCode);

    CompilerPassManager cpManager = new CompilerPassManager(null);
    CompilerPass pass = new ProcessCommonJSModules(null, FILENAME_PREFIX);
    cpManager.addPass(pass);
    cpManager.setInput(externs, root);
    cpManager.setSourceFile(new File("test.js"));
    cpManager.setSourceFiles(Sets.newHashSet(commonModuleSource,
        anotherModuleSource));
    cpManager.runPasses();

    Node expectedOutput = NodeFactory.createScript("" +
        "var module$exports = {\n" +
        "  foo: function() { return 1; },\n" +
        "  bar: function() { return 2; },\n" +
        "  baz: function() { return 3; }\n" +
        "};\n" +
        "goog.provide(\"test_.test\");\n" +
        "goog.require(\"test_.test.common-module\");\n" +
        "goog.require(\"test_.test.another-module\");\n" +
        "goog.require(\"test_.test.common-module\");\n" +
        "function test_.test() {}\n" +
        "function test_.test.common-module() {}\n" +
        "function test_.test.another-module() {}\n");

    Node actualOutput = cpManager.getOutput();

    assertEquals(expectedOutput.toSource(), actualOutput.toSource());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithModuleExports()
      throws Exception {
    String inputCode =
        "var module$exports = {\n" +
        "  foo: function() { return 1; },\n" +
        "  bar: function() { return 2; }\n" +
        "};\n" +
        "exports = module$exports;\n" +
        "exports.baz = function() { return 3; }\n" +
        "require('common-module');\n" +
        "function commonModule() {}\n";

    Node externs = NodeFactory.createExterns();
    Node root = NodeFactory.createScript(inputCode);

    CompilerPassManager cpManager = new CompilerPassManager(null);
    CompilerPass pass = new ProcessCommonJSModules(null, FILENAME_PREFIX);
    cpManager.addPass(pass);
    cpManager.setInput(externs, root);
    cpManager.runPasses();

    Node expectedOutput = NodeFactory.createScript("" +
        "var module$exports = {\n" +
        "  foo: function() { return 1; },\n" +
        "  bar: function() { return 2; },\n" +
        "  baz: function() { return 3; }\n" +
        "};\n" +
        "goog.provide(\"test_common-module\");\n" +
        "goog.require(\"test_common-module\");\n" +
        "goog.provide(\"test_common-module.module$exports\");\n" +
        "goog.require(\"test_common-module.module$exports\");\n" +
        "function test_common-module() {}\n");

    Node actualOutput = cpManager.getOutput();

    assertEquals(expectedOutput.toSource(), actualOutput.toSource());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithModuleExportsRenaming()
      throws Exception {
    String inputCode =
        "var module$exports = {\n" +
        "  foo: function() { return 1; },\n" +
        "  bar: function() { return 2; }\n" +
        "};\n" +
        "exports = module$exports;\n" +
        "exports.baz = function() { return 3; }\n" +
        "require('common-module');\n" +
        "function commonModule() {}\n";

    Node externs = NodeFactory.createExterns();
    Node root = NodeFactory.createScript(inputCode);

    CompilerPassManager cpManager = new CompilerPassManager(null);
    CompilerPass pass = new ProcessCommonJSModules(null, FILENAME_PREFIX);
    cpManager.addPass(pass);
    cpManager.setInput(externs, root);
    cpManager.runPasses();

    String expectedOutputSource =
        "var module$exports = {\n" +
        "  foo: function() { return 1; },\n" +
        "  bar: function() { return 2; },\n" +
        "  baz: function() { return 3; }\n" +
        "};\n" +
        "goog.provide(\"test_common-module\");\n" +
        "goog.require(\"test_common-module\");\n" +
        "goog.provide(\"test_common-module.module$exports\");\n" +
        "goog.provide(\"test_common-module.module$exports.baz\");\n" +
        "function test_common-module() {}\n";
    Node expectedOutput = NodeFactory.createScript(expectedOutputSource);

    Node actualOutput = cpManager.getOutput();

    assertEquals(expectedOutput.toSource(), actualOutput.toSource());
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithModuleExportsGlobalRenaming()
      throws Exception {
    String inputCode =
        "var exports = module.exports = {\n" +
        "  foo: function() { return 1; },\n" +
        "  bar: function() { return 2; }\n" +
        "};\n" +
        "exports.baz = function() { return 3; }\n" +
        "require('common-module');\n" +
        "function commonModule() {}\n";

    Node externs = NodeFactory.createExterns();
    Node root = NodeFactory.createScript(inputCode);

    CompilerPassManager cpManager = new CompilerPassManager(null);
    CompilerPass pass = new ProcessCommonJSModules(null, FILENAME_PREFIX);
    cpManager.addPass(pass);
    cpManager.setInput(externs, root);
    cpManager.runPasses();

    String expectedOutputSource =
        "var module$exports = {\n" +
        "  foo: function() { return 1; },\n" +
        "  bar: function() { return 2; },\n" +
        "  baz: function() { return 3; }\n" +
        "};\n" +
        "goog.provide(\"test_common-module\");\n" +
        "goog.require(\"test_common-module\");\n" +
        "goog.provide(\"test_common-module.module$exports\");\n" +
        "goog.provide(\"test_common-module.module$exports.baz\");\n" +
        "goog.provide(\"test_common-module.module$exports.foo\");\n" +
        "goog.provide(\"test_common-module.module$exports.bar\");\n" +
        "function test_common-module() {}\n";
    Node expectedOutput = NodeFactory.createScript(expectedOutputSource);

    Node actualOutput = cpManager.getOutput();

    assertEquals(expectedOutput.toSource(), actualOutput.toSource());
  }
}