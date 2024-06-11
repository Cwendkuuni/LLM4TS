import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.Node;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String input = "// Input file\n" +
        "exports.a = function() {\n" +
        "  return 1;\n" +
        "};\n" +
        "exports.b = 2;\n" +
        "require('./dependency.js');\n";

    String filename = "input.js";
    String dependencyInput = "// Dependency file\n" +
        "exports.c = 3;\n";
    String dependencyFilename = "dependency.js";

    // Set up the test
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);
    Node root = compiler.parseTree(input, filename, null);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, filename);
    NodeTraversal traversal = new NodeTraversal(compiler, root, pass);

    // Traverse the tree
    traversal.traverse();

    // Check the output
    Node output = traversal.getOutput();

    // Test the result of the processCommonJSModules pass
    Node moduleNode = output.getFirstChild();
    assertNotNull(moduleNode);
    assertEquals("function(exports) {goog.provide(\"" + filename + "\");\n" +
        "  exports.a = function() {\n" +
        "    return 1;\n" +
        "  };\n" +
        "  goog.require('dependency.js');\n" +
        "  exports.b = 2;\n" +
        "}", moduleNode.toStringDeep());

    // Test the dependency processing
    Node dependencyModuleNode = null;
    for (Node child = moduleNode.getFirstChild(); child != null;
        child = child.getNextSibling()) {
      if (child.isCall() && child.getChildCount() == 2 &&
          "goog.require".equals(child.getFirstChild().getQualifiedName())) {
        dependencyModuleNode = child;
        break;
      }
    }
    assertNotNull(dependencyModuleNode);
    Node dependency = dependencyModuleNode.getChildAtIndex(1);
    assertEquals("function(exports) {goog.provide(\"" + dependencyFilename + "\");\n" +
        "  exports.c = 3;\n" +
        "}", dependency.toStringDeep());
  }

  @Test
  public void testProcessCommonJSModulesWithMultipleModules() throws Exception {
    String input = "// Input file 1\n" +
        "exports.a = function() {\n" +
        "  return 1;\n" +
        "};\n" +
        "exports.b = 2;\n" +
        "require('./dependency1.js');\n" +
        "require('./dependency2.js');\n";

    String filename = "input.js";

    String dependency1Input = "// Dependency file 1\n" +
        "exports.x = 3;\n";
    String dependency1Filename = "dependency1.js";

    String dependency2Input = "// Dependency file 2\n" +
        "exports.y = 4;\n";
    String dependency2Filename = "dependency2.js";

    // Set up the test
    CompilerOptions options = new CompilerOptions(null);
    Compiler compiler = new Compiler(options);
    Node root = compiler.parseTree(input, filename, null);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(compiler, filename);
    NodeTraversal traversal = new NodeTraversal(compiler, root, pass);

    // Traverse the tree
    traversal.traverse();

    // Check the output
    Node output = traversal.getOutput();

    // Test the result of the processCommonJSModules pass
    Node moduleNode = output.getFirstChild();
    assertNotNull(moduleNode);
    assertEquals("function(exports) {goog.provide(\"" + filename + "\");\n" +
        "  exports.a = function() {\n" +
        "    return 1;\n" +
        "  };\n" +
        "  goog.require('dependency1.js');\n" +
        "  goog.require('dependency2.js');\n" +
        "  exports.b = 2;\n" +
        "}", moduleNode.toStringDeep());

    // Test the dependency processing
    Node dependency1ModuleNode = null;
    for (Node child = moduleNode.getFirstChild(); child != null;
        child = child.getNextSibling()) {
      if (child.isCall() && child.getChildCount() == 2 &&
          "goog.require".equals(child.getFirstChild().getQualifiedName())) {
        if (child.getChildAtIndex(1).isString() &&
            child.getChildAtIndex(1).getString().equals(dependency1Filename)) {
          dependency1ModuleNode = child;
          break;
        }
      }
    }
    assertNotNull(dependency1ModuleNode);
    Node dependency1 = dependency1ModuleNode.getChildAtIndex(1);
    assertEquals("function(exports) {goog.provide(\"" + dependency1Filename + "\");\n" +
        "  exports.x = 3;\n" +
        "}", dependency1.toStringDeep());

    Node dependency2ModuleNode = null;
    for (Node child = moduleNode.getFirstChild(); child != null;
        child = child.getNextSibling()) {
      if (child.isCall() && child.getChildCount() == 2 &&
          "goog.require".equals(child.getFirstChild().getQualifiedName())) {
        if (child.getChildAtIndex(1).isString() &&
            child.getChildAtIndex(1).getString().equals(dependency2Filename)) {
          dependency2ModuleNode = child;
          break;
        }
      }
    }
    assertNotNull(dependency2ModuleNode);
    Node dependency2 = dependency2ModuleNode.getChildAtIndex(1);
    assertEquals("function(exports) {goog.provide(\"" + dependency2Filename + "\");\n" +
        "  exports.y = 4;\n" +
        "}", dependency2.toStringDeep());
  }
}