import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.testing.TestCompiler;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private static CompilerOptions getDefaultOptions() {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel("QUIET");
    options.setSourceMap(false);
    options.setOutputFile("out.js");
    options.setCompilationLevel(CompilerOptions.CompilationLevel.SIMPLE);
    return options;
  }

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException {
    String inputPath = "src/input/";
    String expectedOutputPath = "src/expected/";

    TestCompiler compiler = new TestCompiler();
    CompilerOptions options = getDefaultOptions();

    // Test simple CommonJS module
    String inputFile1 = inputPath + "input1.js";
    String expectedOutputFile1 = expectedOutputPath + "output1.js";
    Node rootNode1 = compiler.compile(inputFile1, options);
    Node expectedRootNode1 = compiler.load(new File(expectedOutputFile1));

    NodeTraversal nodeTraversal1 = new NodeTraversal(compiler, rootNode1);
    ProcessCommonJSModules pass1 = new ProcessCommonJSModules(compiler, "");
    pass1.process(null, rootNode1);

    assertCodeEqual(expectedRootNode1, rootNode1);

    // Test CommonJS module with dependencies
    String inputFile2 = inputPath + "input2.js";
    String expectedOutputFile2 = expectedOutputPath + "output2.js";
    Node rootNode2 = compiler.compile(inputFile2, options);
    Node expectedRootNode2 = compiler.load(new File(expectedOutputFile2));

    NodeTraversal nodeTraversal2 = new NodeTraversal(compiler, rootNode2);
    ProcessCommonJSModules pass2 = new ProcessCommonJSModules(compiler, "", true);
    pass2.process(null, rootNode2);

    Set<String> dependencies = Sets.newHashSet("module1.js", "module2.js");
    assertEquals(dependencies, pass2.getModule().getDependencies());

    assertCodeEqual(expectedRootNode2, rootNode2);

    // Test relative require calls
    String inputFile3 = inputPath + "input3.js";
    String expectedOutputFile3 = expectedOutputPath + "output3.js";
    Node rootNode3 = compiler.compile(inputFile3, options);
    Node expectedRootNode3 = compiler.load(new File(expectedOutputFile3));

    NodeTraversal nodeTraversal3 = new NodeTraversal(compiler, rootNode3);
    ProcessCommonJSModules pass3 = new ProcessCommonJSModules(compiler, "");
    pass3.process(null, rootNode3);

    assertCodeEqual(expectedRootNode3, rootNode3);

    // Test rewriting module.exports assignments
    String inputFile4 = inputPath + "input4.js";
    String expectedOutputFile4 = expectedOutputPath + "output4.js";
    Node rootNode4 = compiler.compile(inputFile4, options);
    Node expectedRootNode4 = compiler.load(new File(expectedOutputFile4));

    NodeTraversal nodeTraversal4 = new NodeTraversal(compiler, rootNode4);
    ProcessCommonJSModules pass4 = new ProcessCommonJSModules(compiler, "", false);
    pass4.process(null, rootNode4);

    assertCodeEqual(expectedRootNode4, rootNode4);
  }

  private void assertCodeEqual(Node expected, Node actual) {
    assertEquals("Unexpected code", expected.toSource(), actual.toSource());
  }
}