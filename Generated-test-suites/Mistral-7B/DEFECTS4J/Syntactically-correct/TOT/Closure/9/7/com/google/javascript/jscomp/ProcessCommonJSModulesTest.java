import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.ProcessCommonJSModules.JSModule;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String sourceCode = "// src/moduleA.js\n" +
        "var a = 1;\n" +
        "exports.exportA = a;\n";

    String expectedOutput = "// src/moduleA.js\n" +
        "goog.provide('moduleA');\n" +
        "goog.exportProperty(moduleA, 'exportA', a);\n";

    CompilerInput input = new CompilerInput("src/moduleA.js", sourceCode, null);
    CompilerOptions options = new CompilerOptions(null);
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(null, ".");

    pcjm.process(input, null);

    Node output = input.getCompiledJs();
    assertEquals(expectedOutput, output.toSource());

    JSModule module = pcjm.getModule();
    assertNotNull(module);
    assertEquals("moduleA", module.getName());
  }

  @Test
  public void testProcessCommonJSModulesWithDependencies() {
    String sourceCodeA = "// src/moduleA.js\n" +
        "var a = 1;\n" +
        "exports.exportA = a;\n";

    String sourceCodeB = "// src/moduleB.js\n" +
        "var b = require('./moduleA');\n" +
        "exports.exportB = b + 1;\n";

    String expectedOutputA = "// src/moduleA.js\n" +
        "goog.provide('moduleA');\n" +
        "goog.exportProperty(moduleA, 'exportA', a);\n";

    String expectedOutputB = "// src/moduleB.js\n" +
        "goog.provide('moduleB');\n" +
        "goog.require('moduleA');\n" +
        "goog.exportProperty(moduleB, 'exportB', (moduleA.exportA + 1));\n";

    CompilerInput inputA = new CompilerInput("src/moduleA.js", sourceCodeA, null);
    CompilerInput inputB = new CompilerInput("src/moduleB.js", sourceCodeB, null);
    CompilerOptions options = new CompilerOptions(null);
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(null, ".");

    pcjm.process(inputA, null);
    pcjm.process(inputB, inputA);

    Node outputA = inputA.getCompiledJs();
    assertEquals(expectedOutputA, outputA.toSource());

    Node outputB = inputB.getCompiledJs();
    assertEquals(expectedOutputB, outputB.toSource());

    JSModule moduleA = pcjm.getModule("moduleA");
    JSModule moduleB = pcjm.getModule("moduleB");
    assertNotNull(moduleA);
    assertNotNull(moduleB);
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeImports() {
    String sourceCodeA = "// src/moduleA.js\n" +
        "var a = 1;\n" +
        "exports.exportA = a;\n";

    String sourceCodeB = "// src/moduleB.js\n" +
        "var b = require('./moduleA');\n" +
        "exports.exportB = b + 1;\n";

    String expectedOutputA = "// src/moduleA.js\n" +
        "goog.provide('moduleA');\n" +
        "goog.exportProperty(moduleA, 'exportA', a);\n";

    String expectedOutputB = "// src/moduleB.js\n" +
        "goog.provide('moduleB');\n" +
        "goog.require('moduleA');\n" +
        "goog.exportProperty(moduleB, 'exportB', (moduleA.exportA + 1));\n";

    CompilerInput inputA = new CompilerInput("src/moduleA.js", sourceCodeA, null);
    CompilerInput inputB = new CompilerInput("src/moduleB.js", sourceCodeB, null);
    CompilerOptions options = new CompilerOptions(null);
    ProcessCommonJSModules pcjm = new ProcessCommonJSModules(null, ".");

    pcjm.process(inputA, null);
    pcjm.process(inputB, inputA);

    Node outputA = inputA.getCompiledJs();
    assertEquals(expectedOutputA, outputA.toSource());

    Node outputB = inputB.getCompiledJs();
    assertEquals(expectedOutputB, outputB.toSource());

    JSModule moduleA = pcjm.getModule("moduleA");
    JSModule moduleB = pcjm.getModule("moduleB");
    assertNotNull(moduleA);
    assertNotNull(moduleB);

    // Test that the require call is resolved correctly based on the current file name
    String currentFileName = inputB.getSourceName();
    String requiredFileName = "moduleA.js";
    String expectedModuleName = toModuleName(requiredFileName, currentFileName);
    JSModule requiredModule = pcjm.getModule(expectedModuleName);
    assertNotNull(requiredModule);
  }

  private String toModuleName(String requiredFilename, String currentFilename) {
    return ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
  }
}