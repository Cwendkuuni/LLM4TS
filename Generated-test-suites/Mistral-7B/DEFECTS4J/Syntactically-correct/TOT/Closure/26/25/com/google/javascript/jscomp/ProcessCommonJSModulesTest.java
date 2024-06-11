import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String input = "// Test input file\n" +
        "var exports = module.exports = {\n" +
        "  foo: function() { return 1; },\n" +
        "};\n" +
        "exports.bar = function() { return 2; };\n" +
        "\n" +
        "require('module');\n" +
        "\n" +
        "exports.baz = function() {\n" +
        "  var require = require;\n" +
        "  var foo = require('./foo');\n" +
        "  return foo() + require('./bar')();\n" +
        "};\n";

    Compiler compiler = new Compiler();
    CompilerPass pass = new ProcessCommonJSModules(compiler, "");
    Node externs = null;
    Node root = IR.parse(input, null);
    pass.process(externs, root);
    String output = IR.toString(root);

    String expected = "// Test output file\n" +
        "goog.provide('foo');\n" +
        "\n" +
        "goog.module('bar');\n" +
        "\n" +
        "foo.foo = function() {\n" +
        "  return 1;\n" +
        "};\n" +
        "\n" +
        "bar.exports = function() {\n" +
        "  return (foo.foo)();\n" +
        "};\n" +
        "\n" +
        "bar.exports.bar = function() {\n" +
        "  return 2;\n" +
        "};\n" +
        "\n" +
        "bar.exports.baz = function() {\n" +
        "  return (foo.foo)() + (bar.exports.bar)();\n" +
        "};\n" +
        "\n" +
        "goog.require('foo');\n" +
        "\n";

    assertEquals(expected, output);
  }
}