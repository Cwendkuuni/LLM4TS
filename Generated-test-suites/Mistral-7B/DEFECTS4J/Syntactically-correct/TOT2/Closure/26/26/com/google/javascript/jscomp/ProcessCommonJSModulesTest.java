import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.NodeOperations;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class ProcessCommonJSModulesTest {

  private Compiler compiler;
  private CompilerOptions options;
  private ProcessCommonJSModules pass;

  @Before
  public void setup() {
    options = new CompilerOptions();
    compiler = new Compiler(options);
    pass = new ProcessCommonJSModules(compiler, "src/");
  }

  @Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    String input = "// @filename: src/main.js\n" +
        "var a = require('./submodule');\n" +
        "goog.provide('main');\n" +
        "goog.require('submodule');\n" +
        "main.doSomething = function() {\n" +
        "  a.doSomething();\n" +
        "};\n";

    Node externalSource = IR.parseCompilationUnit(input);
    Node root = NodeOperations.createTopLevelScript(externalSource.getSourceName(), input);
    pass.process(compiler.getExterns(), root);

    Node result = compiler.getOutput();
    String expectedOutput = "// @filename: src/main.js\n" +
        "goog.provide('main');\n" +
        "goog.require('submodule');\n" +
        "var submodule = module.submodule;\n" +
        "main.doSomething = function() {\n" +
        "  submodule.doSomething();\n" +
        "};\n";
    assertEquals(expectedOutput, NodeOperations.getSourceCode(result));
  }

  @Test
  public void testProcessCommonJSModulesWithRelPath() throws IOException, URISyntaxException {
    String input = "// @filename: src/main.js\n" +
        "var a = require('./submodule/subsubmodule');\n" +
        "goog.provide('main');\n" +
        "goog.require('submodule.subsubmodule');\n" +
        "main.doSomething = function() {\n" +
        "  a.doSomething();\n" +
        "};\n";

    Node externalSource = IR.parseCompilationUnit(input);
    Node root = NodeOperations.createTopLevelScript(externalSource.getSourceName(), input);
    pass.process(compiler.getExterns(), root);

    Node result = compiler.getOutput();
    String expectedOutput = "// @filename: src/main.js\n" +
        "goog.provide('main');\n" +
        "goog.require('submodule.subsubmodule');\n" +
        "var subsubmodule = module.submodule.subsubmodule;\n" +
        "main.doSomething = function() {\n" +
        "  subsubmodule.doSomething();\n" +
        "};\n";
    assertEquals(expectedOutput, NodeOperations.getSourceCode(result));
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModulesWithCycle() throws IOException, URISyntaxException {
    String input = "// @filename: src/a.js\n" +
        "goog.provide('a');\n" +
        "goog.require('b');\n" +
        "a.require = function() {\n" +
        "  throw new Error('Should not be called');\n" +
        "};\n" +
        "a.doSomething = function() {\n" +
        "  this.require('b').doSomething();\n" +
        "};\n";

    String inputB = "// @filename: src/b.js\n" +
        "goog.provide('b');\n" +
        "b.doSomething = function() {\n" +
        "};\n";

    Node externalSourceA = IR.parseCompilationUnit(input);
    Node externalSourceB = IR.parseCompilationUnit(inputB);
    Node rootA = NodeOperations.createTopLevelScript(externalSourceA.getSourceName(), input);
    Node rootB = NodeOperations.createTopLevelScript(externalSourceB.getSourceName(), inputB);
    Node root = Node.newCompilationUnit(rootA, rootB);

    pass.process(compiler.getExterns(), root);

    // The test should fail with a RuntimeException due to the cycle
  }

  @Test(expected = URISyntaxException.class)
  public void testProcessCommonJSModulesWithInvalidURI() throws IOException {
    String input = "// @filename: src/main.js\n" +
        "goog.provide('main');\n" +
        "goog.require('submodule://invalid/uri');\n" +
        "main.doSomething = function() {\n" +
        "};\n";

    Node externalSource = IR.parseCompilationUnit(input);
    Node root = NodeOperations.createTopLevelScript(externalSource.getSourceName(), input);
    pass.process(compiler.getExterns(), root);

    // The test should fail with a URISyntaxException
  }
}