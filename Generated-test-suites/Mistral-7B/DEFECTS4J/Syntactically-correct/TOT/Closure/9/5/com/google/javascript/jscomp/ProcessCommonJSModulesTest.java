import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.rhino.Node;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  @Test
  public void testCommonJSModuleRewriting() {
    String input = "// input.js\n" +
        "var a = 1;\n" +
        "exports.b = 2;\n" +
        "require('./output')";

    String expectedOutput = "// input.js\n" +
        "var a = 1;\n" +
        "goog.provide('myapp.input');\n" +
        "goog.require('myapp.output');\n" +
        "myapp.input.a = 1;\n" +
        "myapp.output.goog.exports.c = 2;\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    CompilerInput inputNode = new CompilerInput(input, "input.js", null);
    Compiler compiler = new Compiler();

    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, ".");
    p.process(inputNode, null);
    Node output = compiler.getProgram();

    assertTrue(output.getText().equals(expectedOutput));
  }

  @Test(expected = RuntimeException.class)
  public void testInvalidURI() {
    String input = "// input.js\n" +
        "require('x:/invalid');";

    CompilerOptions options = CompilerOptions.defaultOptions();
    CompilerInput inputNode = new CompilerInput(input, "input.js", null);
    Compiler compiler = new Compiler();

    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, ".");
    p.process(inputNode, null);
  }

  @Test
  public void testRelativeRequire() {
    String input = "// input.js\n" +
        "require('./output')";

    String expectedOutput = "// input.js\n" +
        "goog.provide('myapp.input');\n" +
        "goog.require('myapp.output');\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    CompilerInput inputNode = new CompilerInput(input, "input.js", null);
    Compiler compiler = new Compiler();

    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, ".");
    p.process(inputNode, null);
    Node output = compiler.getProgram();

    assertTrue(output.getText().equals(expectedOutput));
  }

  @Test
  public void testModuleWithExports() {
    String input = "// input.js\n" +
        "exports.a = 1;\n";

    String expectedOutput = "// input.js\n" +
        "goog.provide('myapp.input');\n" +
        "myapp.input.a = 1;\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    CompilerInput inputNode = new CompilerInput(input, "input.js", null);
    Compiler compiler = new Compiler();

    ProcessCommonJSModules p = new ProcessCommonJSModules(compiler, ".", true);
    p.process(inputNode, null);
    Node output = compiler.getProgram();

    assertTrue(output.getText().equals(expectedOutput));
  }

  @Test
  public void testModuleWithExports_and_dependency() {
    String input = "// input.js\n" +
        "exports.a = 1;\n" +
        "require('./output')";

    String expectedOutput = "// input.js\n" +
        "goog.provide('myapp.input');\n" +
        "goog.require('myapp.output');\n" +
        "myapp.input.a = 1;\n";

    String outputExpected = "// output.js\n" +
        "goog.provide('myapp.output');";

    CompilerOptions options = CompilerOptions.defaultOptions();
    CompilerInput inputNode1 = new CompilerInput(input, "input.js", null);
    CompilerInput inputNode2 = new CompilerInput("", "output.js", null);
    Compiler compiler = new Compiler();

    ProcessCommonJSModules p1 = new ProcessCommonJSModules(compiler, ".");
    p1.process(inputNode1, null);

    ProcessCommonJSModules p2 = new ProcessCommonJSModules(compiler, ".");
    p2.process(inputNode2, null);
    Node output1 = compiler.getProgram();
    Node output2 = p2.getModule().getCode();

    assertTrue(output1.getText().equals(expectedOutput));
    assertTrue(output2.getText().equals(outputExpected));
  }

  @Test
  public void testModuleWithRequire() {
    String input = "// input.js\n" +
        "var a = 1;\n" +
        "require('./output')";

    String expectedOutput = "// input.js\n" +
        "goog.provide('myapp.input');\n" +
        "goog.require('myapp.output');\n" +
        "myapp.input.a = 1;\n";

    String outputExpected = "// output.js\n" +
        "goog.provide('myapp.output');\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    CompilerInput inputNode1 = new CompilerInput(input, "input.js", null);
    CompilerInput inputNode2 = new CompilerInput("", "output.js", null);
    Compiler compiler = new Compiler();

    ProcessCommonJSModules p1 = new ProcessCommonJSModules(compiler, ".");
    p1.process(inputNode1, null);

    ProcessCommonJSModules p2 = new ProcessCommonJSModules(compiler, ".");
    p2.process(inputNode2, null);
    Node output1 = compiler.getProgram();
    Node output2 = p2.getModule().getCode();

    assertTrue(output1.getText().equals(expectedOutput));
    assertTrue(output2.getText().equals(outputExpected));
  }

  @Test
  public void testModuleWithRequire_and_dependency() {
    String input = "// input.js\n" +
        "var a = 1;\n" +
        "require('./output')";

    String expectedOutput = "// input.js\n" +
        "goog.provide('myapp.input');\n" +
        "goog.require('myapp.output');\n" +
        "myapp.input.a = 1;\n";

    String outputExpected = "// output.js\n" +
        "goog.provide('myapp.output');\n";

    String dependencyInput = "// output.js\n" +
        "exports.c = 2;\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    CompilerInput inputNode1 = new CompilerInput(input, "input.js", null);
    CompilerInput inputNode2 = new CompilerInput(dependencyInput, "output.js", null);
    Compiler compiler = new Compiler();

    ProcessCommonJSModules p1 = new ProcessCommonJSModules(compiler, ".");
    p1.process(inputNode1, null);

    ProcessCommonJSModules p2 = new ProcessCommonJSModules(compiler, ".");
    p2.process(inputNode2, null);
    Node output1 = compiler.getProgram();
    Node output2 = p2.getModule().getCode();

    assertTrue(output1.getText().equals(expectedOutput));
    assertTrue(output2.getText().equals(outputExpected));
  }

  @Test
  public void testModuleWithMultipleRequire() {
    String input = "// input.js\n" +
        "require('./output1')\n" +
        "require('./output2')";

    String outputExpected1 = "// output1.js\n" +
        "goog.provide('myapp.output1');\n";

    String outputExpected2 = "// output2.js\n" +
        "goog.provide('myapp.output2');\n";

    CompilerOptions options = CompilerOptions.defaultOptions();
    CompilerInput inputNode1 = new CompilerInput("", "output1.js", null);
    CompilerInput inputNode2 = new CompilerInput("", "output2.js", null);
    Compiler compiler = new Compiler();

    ProcessCommonJSModules p1 = new ProcessCommonJSModules(compiler, ".");
    p1.process(inputNode1, null);

    ProcessCommonJSModules p2 = new ProcessCommonJSModules(compiler, ".");
    p2.process(inputNode2, null);
    Node output1 = p1.getModule().getCode();
    Node output2 = p2.getModule().getCode();

    assertTrue(output1.getText().equals(outputExpected1));
    assertTrue(output2.getText().equals(outputExpected2));
  }
}