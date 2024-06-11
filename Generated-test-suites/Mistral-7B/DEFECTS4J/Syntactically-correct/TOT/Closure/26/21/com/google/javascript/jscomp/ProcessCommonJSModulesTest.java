import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String input = "// Test module\n" +
        "var exports = {\n" +
        "  add: function(x, y) {\n" +
        "    return x + y;\n" +
        "  }\n" +
        "};\n" +
        "exports.module$exports = exports;\n" +
        "goog.provide('test');\n" +
        "goog.require('test');\n" +
        "goog.require('moduleA');\n" +
        "test.add(moduleA.sum);\n" +
        "function moduleA() {\n" +
        "  goog.provide('moduleA');\n" +
        "  var sum = 2;\n" +
        "  return {\n" +
        "    sum: sum\n" +
        "  };\n" +
        "}\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    compiler.compile(input, null, options);
    Node root = compiler.getMainModule().getBody();

    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, ".", false);
    pcm.process(options.externs, root);

    Node expectedOutput = IR.exprResult(IR.call(IR.getprop(IR.name("test"),
        IR.string("add")), IR.number(3), IR.number(2)));
    Node actualOutput = root.getLastChild();

    assertEquals("Expected add function to be called with correct arguments",
        expectedOutput, actualOutput);
  }

  @Test
  public void testProcessCommonJSModulesWithRelativePath() {
    String input = "// Test module\n" +
        "var exports = {\n" +
        "  add: function(x, y) {\n" +
        "    return x + y;\n" +
        "  }\n" +
        "};\n" +
        "exports.module$exports = exports;\n" +
        "goog.provide('test');\n" +
        "goog.require('./moduleA');\n" +
        "test.add(moduleA.sum);\n" +
        "function moduleA() {\n" +
        "  goog.provide('moduleA');\n" +
        "  var sum = 2;\n" +
        "  return {\n" +
        "    sum: sum\n" +
        "  };\n" +
        "}\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    compiler.compile(input, null, options);
    Node root = compiler.getMainModule().getBody();

    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, ".", false);
    pcm.process(options.externs, root);

    Node expectedOutput = IR.exprResult(IR.call(IR.getprop(IR.name("test"),
        IR.string("add")), IR.number(3), IR.number(2)));
    Node actualOutput = root.getLastChild();

    assertEquals("Expected add function to be called with correct arguments",
        expectedOutput, actualOutput);
  }

  @Test
  public void testProcessCommonJSModulesWithAbsolutPath() {
    String input = "// Test module\n" +
        "var exports = {\n" +
        "  add: function(x, y) {\n" +
        "    return x + y;\n" +
        "  }\n" +
        "};\n" +
        "exports.module$exports = exports;\n" +
        "goog.provide('test');\n" +
        "goog.require('../moduleA');\n" +
        "test.add(moduleA.sum);\n" +
        "function moduleA() {\n" +
        "  goog.provide('moduleA');\n" +
        "  var sum = 2;\n" +
        "  return {\n" +
        "    sum: sum\n" +
        "  };\n" +
        "}\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    compiler.compile(input, null, options);
    Node root = compiler.getMainModule().getBody();

    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, ".", false);
    pcm.process(options.externs, root);

    Node expectedOutput = IR.exprResult(IR.call(IR.getprop(IR.name("test"),
        IR.string("add")), IR.number(3), IR.number(2)));
    Node actualOutput = root.getLastChild();

    assertEquals("Expected add function to be called with correct arguments",
        expectedOutput, actualOutput);
  }

  @Test
  public void testProcessCommonJSModulesWithCyclicDependency() {
    String input = "// Test module A\n" +
        "goog.provide('moduleA');\n" +
        "goog.require('moduleB');\n" +
        "goog.require('moduleA');\n" +
        "function moduleA() {\n" +
        "  return {};\n" +
        "}\n" +
        "\n" +
        "// Test module B\n" +
        "goog.provide('moduleB');\n" +
        "goog.require('moduleA');\n" +
        "function moduleB() {\n" +
        "  return {};\n" +
        "}\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    compiler.compile(input, null, options);
    Node root = compiler.getMainModule().getBody();

    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, ".", false);
    try {
      pcm.process(options.externs, root);
      fail("Expected an error due to cyclic dependency");
    } catch (RuntimeException e) {
      // Expected exception
    }
  }

  @Test
  public void testProcessCommonJSModulesWithMissingDependency() {
    String input = "// Test module\n" +
        "goog.provide('test');\n" +
        "goog.require('missingModule');\n" +
        "test.add(missingModule.sum);\n" +
        "function test() {\n" +
        "  return {\n" +
        "    add: function(x, y) {\n" +
        "      return x + y;\n" +
        "    }\n" +
        "  };\n" +
        "}\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    Compiler compiler = new Compiler();
    compiler.compile(input, null, options);
    Node root = compiler.getMainModule().getBody();

    ProcessCommonJSModules pcm = new ProcessCommonJSModules(compiler, ".", false);
    try {
      pcm.process(options.externs, root);
      fail("Expected an error due to missing dependency");
    } catch (RuntimeException e) {
      // Expected exception
    }
  }
}