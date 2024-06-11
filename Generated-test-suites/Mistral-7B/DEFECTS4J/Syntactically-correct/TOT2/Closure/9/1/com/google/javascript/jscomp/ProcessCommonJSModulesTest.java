import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.SuffixVarsCallback;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws IOException, URISyntaxException {
    String inputFilePath = "src/main/resources/com/google/javascript/jscomp/ProcessCommonJSModulesInput.js";
    String outputFilePath = "src/test/resources/com/google/javascript/jscomp/ProcessCommonJSModulesOutput.js";

    CompilerOptions options = new CompilerOptions(new File(inputFilePath));
    AbstractCompiler compiler = new AbstractCompiler(options);
    CompilerPassManager cpManager = new CompilerPassManager(compiler, null);

    // Add ProcessCommonJSModules pass
    ProcessCommonJSModules processCommonJSModules = new ProcessCommonJSModules(compiler, "");
    cpManager.addPass(ProcessCommonJSModules.class, processCommonJSModules);

    // Compile input file
    Node externs = null;
    Node root = null;
    cpManager.process(externs, root);

    // Check output file
    String outputCode = compiler.getSourceCode();
    Assert.assertEquals(new File(outputFilePath).getText(), outputCode);

    // Test getModule() method
    ProcessCommonJSModules processCommonJSModulesInstance = (ProcessCommonJSModules) cpManager.getPasses().get(0);
    JSModule module = processCommonJSModulesInstance.getModule();
    Assert.assertNotNull(module);
    Assert.assertEquals("ProcessCommonJSModulesOutput", module.getName());

    // Test guessCJSModuleName() method
    String filename = "test/test.js";
    Assert.assertEquals("test$module", processCommonJSModules.guessCJSModuleName(filename));

    // Test toModuleName() method
    String requiredFilename = "test/test.js";
    String currentFilename = "main.js";
    Assert.assertEquals("test$module", processCommonJSModules.toModuleName(requiredFilename, currentFilename));

    // Test normalizeSourceName() method
    Assert.assertEquals("test.js", processCommonJSModules.normalizeSourceName(filename));

    // Test ProcessCommonJsModulesCallback class
    NodeTraversal nodeTraversal = new NodeTraversal(root, new ProcessCommonJsModulesCallback());
    nodeTraversal.traverse();

    // Test visitRequireCall() method
    Node requireCall = root.getFirstChild().getFirstChild().getNextSibling();
    Assert.assertNotNull(requireCall);
    String moduleName = processCommonJSModules.toModuleName(requireCall.getChildAtIndex(1).getString(),
        normalizeSourceName(root.getSourceFileName()));
    Node moduleRef = requireCall.getChildAtIndex(1).getNextSibling();
    Assert.assertEquals(IR.name(moduleName).getString(), moduleRef.getString());

    // Test visitScript() method
    Node script = root.getLastChild();
    String moduleNameScript = processCommonJSModules.guessCJSModuleName(script.getSourceFileName());
    Assert.assertEquals(moduleNameScript, script.getChildAtIndex(0).getString());

    // Test emitOptionalModuleExportsOverride() method
    Assert.assertTrue(script.getChildCount() > 1);
    Node ifNode = script.getChildAtIndex(script.getChildCount() - 1);
    Assert.assertNotNull(ifNode);
    Node moduleExportsProp = ifNode.getChildAtIndex(1).getChildAtIndex(1);
    Assert.assertNotNull(moduleExportsProp);
    Assert.assertEquals("module$exports", moduleExportsProp.getString());

    // Test SuffixVarsCallback class
    Node nameNode = root.getChildAtIndex(1);
    String name = nameNode.getString();
    Assert.assertEquals(name + "$$" + processCommonJSModules.suffix, nameNode.getString());
  }

  /**
   * Visits require, every "script" and special module.exports assignments.
   */
  private static class ProcessCommonJsModulesCallback extends
          AbstractPostOrderCallback {

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
      if (n.isCall() && n.getChildCount() == 2 &&
          "require".equals(n.getFirstChild().getQualifiedName()) &&
          n.getChildAtIndex(1).isString()) {
        visitRequireCall(t, n, parent);
      }

      if (n.isScript()) {
        visitScript(t, n);
      }

      if (n.isGetProp() &&
          "module.exports".equals(n.getQualifiedName())) {
        visitModuleExports(n);
      }
    }

    // [...]
  }
}