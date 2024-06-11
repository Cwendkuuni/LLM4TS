import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.Processor;
import com.google.javascript.jscomp.traverse.NodeTraversal;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String inputCode = "// Test file\n" +
        "require('./module1.js');\n" +
        "\n" +
        "function test() {\n" +
        "  var x = require('./module2.js');\n" +
        "}\n" +
        "\n" +
        "// module1.js\n" +
        "exports.foo = function() {\n" +
        "  console.log('module1');\n" +
        "};\n" +
        "\n" +
        "// module2.js\n" +
        "exports.bar = function() {\n" +
        "  console.log('module2');\n" +
        "};\n";

    CompilerInput input = new CompilerInput(new File("test.js"), inputCode);
    CompilerOptions options = new CompilerOptions();
    Processor processor = new Processor(options);
    ProcessCommonJSModules pass = new ProcessCommonJSModules(processor, "");

    processor.addPass(pass);
    Node ast = processor.process(input, null, null);

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback =
        (ProcessCommonJSModules.ProcessCommonJsModulesCallback) pass.callback;
    JSModule module = callback.getModule();

    Processor processor2 = new Processor(options);
    NodeTraversal traversal = NodeTraversal.createTraverser(processor2);
    CompilerInput input2 = new CompilerInput(new File("test.js"), null);
    Node root = processor2.parse(input2, null, null).getRootNode();

    traversal.traverse(root, new AssertGlobalVariablesRenamedCallback(
        module.getName(), callback.getScript()));

    Node testFunction = root.getFirstChild();
    traverseTestFunction(testFunction, traversal);

    Node testCallNode = testFunction.getFirstChild();
    Node requireCall = testCallNode.getFirstChild();
    Node expectedModuleReference = requireCall.getLastChild();

    String expectedModuleName = ProcessCommonJSModules.toModuleName(
        requireCall.getChildAtIndex(1).getString(),
        callback.getScript().getSourceFileName());
    String actualModuleName = expectedModuleReference.getString();

    assertEquals(expectedModuleName, actualModuleName);
  }

  private void traverseTestFunction(Node node, NodeTraversal traversal) {
    if (node.isFunction()) {
      for (int i = 0; i < node.getChildCount(); i++) {
        Node child = node.getChildAtIndex(i);
        if (child.isCall()) {
          Node requireCall = child;
          traverseRequireCall(requireCall, traversal);
        } else if (node instanceof Node) {
          traversal.traverse(node, this);
        }
      }
    }
  }

  private void traverseRequireCall(Node node, NodeTraversal traversal) {
    if (node.isCall() && node.getChildCount() == 2 &&
        "require".equals(node.getFirstChild().getQualifiedName())) {
      String moduleName = ProcessCommonJSModules.toModuleName(
          node.getChildAtIndex(1).getString(),
          node.getSourceFileName());

      Node moduleReference = traversal.getInput().getModuleByName(moduleName);
      Node expectedRequireCall = IR.call(
          IR.getprop(IR.name("goog"), IR.string("require")),
          IR.string(moduleName));

      // Check that the require call was replaced with the module reference
      assertEquals(expectedRequireCall, node);

      // Check that the require call's arguments have not changed
      assertEquals(node.getChildAtIndex(1).getString(),
          moduleReference.getString());
    }
  }

  /**
   * Verifies that all global variable names have been suffixed with the given
   * module name.
   */
  private static class AssertGlobalVariablesRenamedCallback extends
      AbstractPostOrderCallback {

    private final String moduleName;
    private final Node scriptNode;

    AssertGlobalVariablesRenamedCallback(String moduleName, Node scriptNode) {
      this.moduleName = moduleName;
      this.scriptNode = scriptNode;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isName()) {
        String name = n.getString();
        if (!name.equals(moduleName)) {
          String originalName = n.getProp(Node.ORIGINALNAME_PROP);
          assertTrue("Global variable '" + name + "' should have been renamed " +
              "to '" + originalName + "$$" + moduleName + "'",
              name.endsWith("$$" + moduleName));
        }
      }
    }
  }
}