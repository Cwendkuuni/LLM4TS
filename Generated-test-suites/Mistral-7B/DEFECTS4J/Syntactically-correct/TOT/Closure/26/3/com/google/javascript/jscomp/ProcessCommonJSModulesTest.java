import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerResult;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String input = "// File: input.js\n" +
        "var a = require('moduleA');\n" +
        "exports.b = function() {\n" +
        "  return a.c;\n" +
        "};\n";

    String expectedOutput = "// File: input.js\n" +
        "goog.require('moduleA');\n" +
        "\n" +
        "goog.provide('test');\n" +
        "\n" +
        "test.b = function() {\n" +
        "  return moduleA.c;\n" +
        "};\n";

    CompilerOptions options = new CompilerOptions(CompilerOptions.Mode.ADVANCED);
    Compiler compiler = new Compiler(null, options);

    Node root = NodeFactory.createProgram();
    Node externs = NodeFactory.createScript(input);
    root.addChild(externs);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "");
    pass.process(externs, root);

    CompilerResult result = compiler.compile(root, new Compiler.NullOutputVisitor(), null);
    assertNotNull(result.errorMessages);
    assertNull(result.errorMessages.length);

    String output = result.code.toString();
    assertEquals(expectedOutput, output);
  }

  @Test
  public void testProcessCommonJSModulesWithDependenciesReporting() {
    String input = "// File: input.js\n" +
        "var a = require('moduleA');\n" +
        "exports.b = function() {\n" +
        "  return a.c;\n" +
        "};\n";
    String moduleAInput = "// File: moduleA.js\n" +
        "exports.c = 42;\n";

    CompilerOptions options = new CompilerOptions(CompilerOptions.Mode.ADVANCED);
    Compiler compiler = new Compiler(null, options);

    Node root = NodeFactory.createProgram();
    Node externs = NodeFactory.createScript(input);
    root.addChild(externs);

    Node moduleA = NodeFactory.createScript(moduleAInput);
    Node moduleANode = NodeTraversal.traverse(compiler, root, new NodeNameCallback("moduleA")).getParent();
    moduleANode.addChild(moduleA);

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "", true);
    pass.process(externs, root);

    CompilerResult result = compiler.compile(root, new Compiler.NullOutputVisitor(), null);
    assertNotNull(result.errorMessages);
    assertNull(result.errorMessages.length);

    Set<String> requires = Sets.newHashSet(result.inputs.getRequires());
    assertEquals(1, requires.size());
    assertEquals("moduleA", requires.iterator().next());
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequire() {
    String input = "// File: input.js\n" +
        "var a = require('./moduleA');\n" +
        "exports.b = function() {\n" +
        "  return a.c;\n" +
        "};\n";

    String moduleAInput = "// File: moduleA.js\n" +
        "exports.c = 42;\n";

    CompilerOptions options = new CompilerOptions(CompilerOptions.Mode.ADVANCED);
    Compiler compiler = new Compiler(null, options);

    Node root = NodeFactory.createProgram();
    Node externs = NodeFactory.createScript(input);
    root.addChild(externs);

    Node moduleA = NodeFactory.createScript(moduleAInput);
    String sourceName = externs.getSourceFileName();
    Node moduleANode = NodeTraversal.traverse(compiler, root, new NodeNameCallback("moduleA")).getParent();
    moduleANode.addChild(moduleA);
    moduleA.setSourceFileName(toModuleName(sourceName, externs.getSourceFileName()));

    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, "", false);
    pass.process(externs, root);

    CompilerResult result = compiler.compile(root, new Compiler.NullOutputVisitor(), null);
    assertNotNull(result.errorMessages);
    assertNull(result.errorMessages.length);

    String output = result.code.toString();
    String expectedOutput = "// File: input.js\n" +
        "goog.require('moduleA');\n" +
        "\n" +
        "goog.provide('test');\n" +
        "\n" +
        "test.b = function() {\n" +
        "  return moduleA.c;\n" +
        "};\n";
    assertEquals(expectedOutput, output);
  }

  private static String toModuleName(String requiredFilename, String currentFilename) {
    return ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
  }

  private static class NodeNameCallback extends AbstractPostOrderCallback {

    private final String name;

    NodeNameCallback(String name) {
      this.name = name;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isName() && n.getString().equals(name)) {
        t.foundNode(n);
      }
    }
  }
}