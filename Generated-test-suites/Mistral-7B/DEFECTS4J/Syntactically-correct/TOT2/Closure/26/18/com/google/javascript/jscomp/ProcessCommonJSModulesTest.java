import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private static final String TEST_INPUT_FILE = "testInput.js";
  private static final String TEST_OUTPUT_FILE = "testOutput.js";

  @SuppressWarnings("ResultOfMethodCallIgnored")
  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    // Test with valid input
    CompilerOptions options = new CompilerOptions();
    options.setOutputFile(TEST_OUTPUT_FILE);

    Compiler compiler = new Compiler(options);
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler, "");

    CompilerInput input = new CompilerInput(new File(TEST_INPUT_FILE));
    compiler.addInput(input);
    compiler.runPasses();
    compiler.compileAll();

    Node rootNode = compiler.getRootNode();
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, rootNode);
    nodeTraversal.traverse(processCommonJSModules);

    // Check if the output is as expected
    String expectedOutput = getExpectedOutput();
    String actualOutput = new String(new File(TEST_OUTPUT_FILE).getBytes());
    assertEquals(expectedOutput, actualOutput);

    // Test with empty input
    input = new CompilerInput(new File(""));
    compiler = new Compiler(options);
    compiler.addInput(input);
    compiler.runPasses();
    compiler.compileAll();
    rootNode = compiler.getRootNode();
    NodeTraversal nodeTraversalEmptyInput = new NodeTraversal(compiler, rootNode);
    nodeTraversalEmptyInput.traverse(processCommonJSModules);
    assertNull(getModule());

    // Test with invalid input (missing file)
    input = new CompilerInput(new File("testInvalidInput.jsx"));
    compiler = new Compiler(options);
    compiler.addInput(input);
    compiler.runPasses();
    compiler.compileAll();
    rootNode = compiler.getRootNode();
    NodeTraversal nodeTraversalInvalidInput = new NodeTraversal(compiler, rootNode);
    nodeTraversalInvalidInput.traverse(processCommonJSModules);
    assertNull(getModule());

    // Test with invalid input (invalid uri syntax)
    input = new CompilerInput(new File("testInvalidUriSyntax.js"));
    compiler = new Compiler(options);
    compiler.addInput(input);
    try {
      compiler.runPasses();
      compiler.compileAll();
    } catch (URISyntaxException e) {
      // expected
    }
    rootNode = compiler.getRootNode();
    NodeTraversal nodeTraversalInvalidUriSyntax = new NodeTraversal(compiler, rootNode);
    nodeTraversalInvalidUriSyntax.traverse(processCommonJSModules);
    assertNull(getModule());

    // Test with invalid input (relative path with missing leading dot)
    input = new CompilerInput(new File("../testInvalidRelativePath.js"));
    compiler = new Compiler(options);
    compiler.addInput(input);
    try {
      compiler.runPasses();
      compiler.compileAll();
    } catch (RuntimeException e) {
      // expected
    }
    rootNode = compiler.getRootNode();
    NodeTraversal nodeTraversalInvalidRelativePath = new NodeTraversal(compiler, rootNode);
    nodeTraversalInvalidRelativePath.traverse(processCommonJSModules);
    assertNull(getModule());
  }

  private String getExpectedOutput() throws IOException {
    String expectedOutput = "";
    try (InputStream input = this.getClass().getResourceAsStream("/testOutput.expected")) {
      byte[] bytes = new byte[input.available()];
      input.read(bytes);
      expectedOutput = new String(bytes);
    }
    return expectedOutput;
  }

  private JSModule getModule() {
    return ProcessCommonJSModules.getModule();
  }
}