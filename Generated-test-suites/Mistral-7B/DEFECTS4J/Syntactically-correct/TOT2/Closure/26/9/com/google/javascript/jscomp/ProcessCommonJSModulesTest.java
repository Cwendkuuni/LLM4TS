import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.TestSourceFile;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ProcessCommonJSModulesTest {

  private static final String TEST_DATA = "testData/";
  private static final String INPUT_FILE = TEST_DATA + "input.js";
  private static final String OUTPUT_FILE = TEST_DATA + "output.js";

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    TestSourceFile input = createInputSourceFile();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    compiler.process(input, null);
    Node output = input.getCompiled();
    Node expectedOutput = NodeFactory.createScriptNodeFromString(
        readFileToString(OUTPUT_FILE), null, null, null);
    assertEquals(expectedOutput, output);
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithRequires() throws IOException {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    TestSourceFile input = createInputSourceFileWithRequires();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    compiler.process(input, null);
    Node output = input.getCompiled();
    Node expectedOutput = NodeFactory.createScriptNodeFromString(
        readFileToString(OUTPUT_FILE + "_requires.js"), null, null, null);
    assertEquals(expectedOutput, output);
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithModuleExports() throws IOException {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    TestSourceFile input = createInputSourceFileWithModuleExports();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    compiler.process(input, null);
    Node output = input.getCompiled();
    Node expectedOutput = NodeFactory.createScriptNodeFromString(
        readFileToString(OUTPUT_FILE + "_moduleExports.js"), null, null, null);
    assertEquals(expectedOutput, output);
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithRelativeRequires() throws IOException {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    TestSourceFile input = createInputSourceFileWithRelativeRequires();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    compiler.process(input, null);
    Node output = input.getCompiled();
    Node expectedOutput = NodeFactory.createScriptNodeFromString(
        readFileToString(OUTPUT_FILE + "_relativeRequires.js"), null, null, null);
    assertEquals(expectedOutput, output);
  }

  @org.junit.Test
  public void testProcessCommonJSModulesWithErrors() throws IOException {
    CompilerOptions options = new CompilerOptions();
    Compiler compiler = new Compiler(options);
    TestSourceFile input = createInputSourceFileWithErrors();
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");

    try {
      compiler.process(input, null);
      fail("Expected compile error.");
    } catch (Exception e) {
      // Expected
    }
  }

  private TestSourceFile createInputSourceFile() {
    String inputCode = "var a = 1;";
    return new TestSourceFile(new File(INPUT_FILE), inputCode);
  }

  private TestSourceFile createInputSourceFileWithRequires() {
    String inputCode =
        "var a = require('module1');" +
        "var b = require('module2');" +
        "var c = require('module3');";
    return new TestSourceFile(new File(INPUT_FILE), inputCode);
  }

  private TestSourceFile createInputSourceFileWithModuleExports() {
    String inputCode =
        "/**\n" +
        " * module1\n" +
        " */\n" +
        "exports.m1 = function() {\n" +
        "  return 'module1';\n" +
        "};\n";
    return new TestSourceFile(new File(INPUT_FILE), inputCode);
  }

  private TestSourceFile createInputSourceFileWithRelativeRequires() {
    String inputCode =
        "var a = require('./module1');" +
        "var b = require('../module2');";
    return new TestSourceFile(new File(INPUT_FILE), inputCode);
  }

  private TestSourceFile createInputSourceFileWithErrors() {
    String inputCode = "var a = require('nonexistentmodule');";
    return new TestSourceFile(new File(INPUT_FILE), inputCode);
  }

  private String readFileToString(String filePath) throws IOException {
    byte[] bytes = Files.readAllBytes(new File(filePath).toPath());
    return new String(bytes, "UTF-8");
  }
}