import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.ProcessCommonJSModules.JSModule;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String inputCode =
        "// Test file\n" +
            "require('./module1');\n" +
            "goog.provide('testModule');\n" +
            "goog.require('module1');\n" +
            "testModule.module$exports = {'test': function() {\n" +
            "  return 'Hello World';\n" +
            "}};\n";

    CompilerInput input = new CompilerInput("test.js", inputCode, null);
    CompilerPass processor = new ProcessCommonJSModules(null, "");
    processor.process(input, null);

    String expectedOutput =
        "// Test file\n" +
            "goog.provide('testModule');\n" +
            "goog.require('module1');\n" +
            "var module1Module$exports = (function() {\n" +
            "  return this.module$exports;\n" +
            "}).call(this);\n" +
            "testModule.module$exports = {\n" +
            "  test: function() {\n" +
            "    return 'Hello World';\n" +
            "  }\n" +
            "};\n";

    String outputCode = input.getCode();
    assertEquals(expectedOutput, outputCode);
  }

  @Test
  public void testProcessCommonJSModules_reportDependencies() {
    String inputCode =
        "// Test file\n" +
            "require('./module1');\n" +
            "goog.provide('testModule');\n" +
            "goog.require('module1');\n" +
            "testModule.module$exports = {'test': function() {\n" +
            "  return 'Hello World';\n" +
            "}};\n";

    CompilerInput input = new CompilerInput("test.js", inputCode, null);
    CompilerPass processor = new ProcessCommonJSModules(null, "", true);
    processor.process(input, null);

    Set<String> dependencies = input.getRequireList();
    assertEquals(Sets.newHashSet("module1"), dependencies);
  }

  @Test
  public void testProcessCommonJSModules_emptyInput() {
    String inputCode = "";

    CompilerInput input = new CompilerInput("test.js", inputCode, null);
    CompilerPass processor = new ProcessCommonJSModules(null, "");
    processor.process(input, null);

    String outputCode = input.getCode();
    assertNull(outputCode);
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModules_invalidUriSyntax() {
    String inputCode =
        "// Test file\n" +
            "require('../test.js/../../../module1');\n";

    CompilerInput input = new CompilerInput("test.js", inputCode, null);
    CompilerPass processor = new ProcessCommonJSModules(null, "");
    processor.process(input, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcessCommonJSModules_multipleRequireCalls() {
    String inputCode =
        "// Test file\n" +
            "require('./module1');\n" +
            "require('./module2');\n" +
            "goog.provide('testModule');\n" +
            "goog.require('module1');\n" +
            "goog.require('module2');\n" +
            "testModule.module$exports = {'test': function() {\n" +
            "  return 'Hello World';\n" +
            "}};\n";

    CompilerInput input = new CompilerInput("test.js", inputCode, null);
    CompilerPass processor = new ProcessCommonJSModules(null, "");
    processor.process(input, null);
  }

  @Test
  public void testProcessCommonJSModules_moduleNameConflict() {
    String inputCode1 =
        "// Test file 1\n" +
            "goog.provide('testModule1');\n" +
            "goog.provide('module1');\n" +
            "testModule1.module$exports = {'test': function() {\n" +
            "  return 'Hello World 1';\n" +
            "}};\n";

    String inputCode2 =
        "// Test file 2\n" +
            "goog.provide('testModule2');\n" +
            "goog.provide('module1');\n" +
            "testModule2.module$exports = {'test': function() {\n" +
            "  return 'Hello World 2';\n" +
            "}};\n";

    CompilerInput input1 = new CompilerInput("testFile1.js", inputCode1, null);
    CompilerInput input2 = new CompilerInput("testFile2.js", inputCode2, null);
    CompilerPass processor = new ProcessCommonJSModules(null, "");

    processor.process(input1, null);
    processor.process(input2, input1);

    String expectedOutput1 =
        "// Test file 1\n" +
            "goog.provide('testModule1');\n" +
            "goog.provide('module1$1');\n" +
            "testModule1.module$exports = {\n" +
            "  test: function() {\n" +
            "    return 'Hello World 1';\n" +
            "  }\n" +
            "};\n";

    String expectedOutput2 =
        "// Test file 2\n" +
            "goog.provide('testModule2');\n" +
            "goog.provide('module1$2');\n" +
            "testModule2.module$exports = {\n" +
            "  test: function() {\n" +
            "    return 'Hello World 2';\n" +
            "  }\n" +
            "};\n";

    String outputCode1 = input1.getCode();
    assertEquals(expectedOutput1, outputCode1);

    String outputCode2 = input2.getCode();
    assertEquals(expectedOutput2, outputCode2);
  }

  @Test
  public void testProcessCommonJSModules_moduleNamePrefix() {
    String inputCode =
        "// Test file\n" +
            "goog.provide('testModule');\n" +
            "goog.require('module1');\n" +
            "testModule.module$exports = {'test': function() {\n" +
            "  return 'Hello World';\n" +
            "}};\n";

    CompilerInput input = new CompilerInput("test.js", inputCode, null);
    CompilerPass processor = new ProcessCommonJSModules(null, "test_");
    processor.process(input, null);

    String expectedOutput =
        "// Test file\n" +
            "goog.provide('test_testModule');\n" +
            "goog.require('module1');\n" +
            "test_testModule.module$exports = {\n" +
            "  test: function() {\n" +
            "    return 'Hello World';\n" +
            "  }\n" +
            "};\n";

    String outputCode = input.getCode();
    assertEquals(expectedOutput, outputCode);
  }
}