import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String inputFileName = "input.js";
    String expectedOutputFileName = "output.js";
    String testFileName = "test.js";

    CompilerInput input = new CompilerInput(inputFileName);
    CompilerOptions options = new CompilerOptions();
    options.setOutputFile(expectedOutputFileName);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(input.getCompiler(),
        input.getFilenamePrefix());

    Node root = IR.parse(String.format(
        "// %s\n" +
        "// %s: input.js\n" +
        "// %s: test.js\n" +
        "\n" +
        "// Input file\n" +
        "require('test');\n" +
        "\n" +
        "// Test file\n" +
        "module.exports = {\n" +
        "  'main': function() {\n" +
        "    require('input');\n" +
        "  }\n" +
        "};",
        inputFileName, testFileName, inputFileName), null, null);

    pass.process(input, root);

    String output = input.getCompiler().getSourceAsString(new File(expectedOutputFileName));

    String expectedOutput = String.format(
        "// %s\n" +
        "// %s: input.js\n" +
        "// %s: test.js\n" +
        "// %s: output.js\n" +
        "\n" +
        "// Input file\n" +
        "goog.provide('input');\n" +
        "goog.require('test');\n" +
        "var module$exports = module.exports;\n" +
        "\n" +
        "// Test file\n" +
        "goog.provide('test');\n" +
        "goog.require('input');\n" +
        "goog.require('module$input');\n" +
        "\n" +
        "module$test.main = function() {\n" +
        "  input.main();\n" +
        "};\n",
        inputFileName, testFileName, expectedOutputFileName, inputFileName);

    assertEquals(expectedOutput, output);

    // Test error scenario: require call with non-existent module
    inputFileName = "errorInput.js";
    input = new CompilerInput(inputFileName);
    root = IR.parse(String.format(
        "// %s\n" +
        "// %s: errorInput.js\n" +
        "\n" +
        "// Input file\n" +
        "require('nonExistentModule');\n" +
        "\n"), null, null);

    try {
      pass.process(input, root);
      fail("Expected CompilerError.");
    } catch (CompilerError e) {
      // Expected
    }

    // Test edge case: require call with relative path
    inputFileName = "relativeInput.js";
    testFileName = "relativeTest.js";
    expectedOutputFileName = "relativeOutput.js";
    input = new CompilerInput(inputFileName);

    root = IR.parse(String.format(
        "// %s\n" +
        "// %s: input.js\n" +
        "// %s: test.js\n" +
        "// %s: output.js\n" +
        "\n" +
        "// Input file\n" +
        "require('./test');\n" +
        "\n" +
        "// Test file\n" +
        "module.exports = {\n" +
        "  'main': function() {\n" +
        "  }\n" +
        "};\n" +
        "\n" +
        "// Test file (relative)\n" +
        "goog.require('input');\n" +
        "goog.provide('test');\n" +
        "\n" +
        "module$test.main = function() {\n" +
        "  goog.require('module$input');\n" +
        "};\n"), null, null);

    pass.process(input, root);

    output = input.getCompiler().getSourceAsString(new File(expectedOutputFileName));

    expectedOutput = String.format(
        "// %s\n" +
        "// %s: input.js\n" +
        "// %s: test.js\n" +
        "// %s: output.js\n" +
        "\n" +
        "// Input file\n" +
        "goog.provide('input');\n" +
        "goog.require('test');\n" +
        "goog.require('module$test');\n" +
        "var module$exports = module.exports;\n" +
        "\n" +
        "// Test file\n" +
        "goog.provide('test');\n" +
        "goog.require('input');\n" +
        "\n" +
        "// Test file (relative)\n" +
        "goog.provide('module$test');\n" +
        "\n" +
        "module$test.main = function() {\n" +
        "};\n" +
        "\n" +
        "module$test.main = function() {\n" +
        "  goog.require('module$input');\n" +
        "};\n",
        inputFileName, testFileName, expectedOutputFileName, inputFileName);

    assertEquals(expectedOutput, output);
  }
}