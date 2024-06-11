import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ProcessCommonJSModules.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  private static AbstractCompiler createCompiler() {
    CompilerOptions options = new CompilerOptions();
    options.setCompilerFlag("--processCommonJsModules", true);
    Compiler compiler = new Compiler(options);
    return compiler;
  }

  private static Node createScriptNode(String source) {
    NodeFactory factory = new NodeFactory();
    Node root = factory.parseTree(source);
    Node script = factory.createScript(root);
    return script;
  }

  @org.junit.Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    AbstractCompiler compiler = createCompiler();
    CompilerInput input = new CompilerInput("input.js", new File("input.js"));
    Node root = createScriptNode("(function() {\n" +
        "// input.js\n" +
        "require('module1');\n" +
        "goog.provide('pkg.Main');\n" +
        "goog.require('module1.Module1');\n" +
        "pkg.Main.Main = function() {\n" +
        "};\n" +
        "})()");

    compiler.process(input, root);
    NodeTraversal traversal = new NodeTraversal(compiler, root);
    String expectedOutput = "(function() {\n" +
        "// input.js\n" +
        "goog.require('module1');\n" +
        "goog.provide('pkg.Main');\n" +
        "goog.require('module1$Module1');\n" +
        "pkg.Main.Main = function() {\n" +
        "};\n" +
        "goog.require('module1$Module1');\n" +
        "})()";

    List<Node> nodes = Arrays.asList(
        root.getFirstChild(),
        root.getLastChild()
    );

    for (Node node : nodes) {
      String nodeSource = node.toSource(new CompilerOptions());
      assertEquals(expectedOutput, nodeSource);
    }

    JSModule module = compiler.getPassResult(ProcessCommonJSModules.class).getModule();
    Set<String> dependencies = Sets.newHashSet(module.getDependencies());
    assertTrue("Expected at least one dependency", !dependencies.isEmpty());
    assertTrue("Unexpected dependency: " + dependencies.toString(),
        dependencies.contains("module1"));
  }
}