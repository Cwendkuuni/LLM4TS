import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.NodeFactory;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    String source = "module.exports = 42;";
    CompilerInput input = new CompilerInput(new File("input.js"), source);
    CompilerPass compilerPass = new ProcessCommonJSModules(null, "");

    Node root = NodeFactory.createRoot();
    NodeTraversal nodeTraversal = new NodeTraversal(input, root, null);
    compilerPass.process(input, root);

    Node moduleExports = nodeTraversal.getRoot().getChildAtIndex(0);
    assertEquals(42, moduleExports.getNumberValue());
  }

  @Test
  public void testRequireCall() {
    String source = "require('./module.js');";
    CompilerInput input = new CompilerInput(new File("input.js"), source);
    CompilerPass compilerPass = new ProcessCommonJSModules(null, "");
    Node root = NodeFactory.createRoot();

    ProcessCommonJSModules callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal nodeTraversal = new NodeTraversal(input, root, callback);
    compilerPass.process(input, root);

    Node requireNode = nodeTraversal.getRoot().getChildAtIndex(0);
    Node moduleRef = requireNode.getChildAtIndex(0);
    assertEquals("module$module.js", moduleRef.getString());
  }

  @Test
  public void testRequireRelativePath() {
    String source = "require('./subfolder/module.js');";
    CompilerInput input = new CompilerInput(new File("input.js"), source);
    CompilerPass compilerPass = new ProcessCommonJSModules(null, "");
    Node root = NodeFactory.createRoot();

    ProcessCommonJSModules callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal nodeTraversal = new NodeTraversal(input, root, callback);
    compilerPass.process(input, root);

    Node requireNode = nodeTraversal.getRoot().getChildAtIndex(0);
    Node moduleRef = requireNode.getChildAtIndex(0);
    assertEquals("module$subfolder$module.js", moduleRef.getString());
  }

  @Test
  public void testRequireAbsolutePath() {
    String source = "require('/absolute/path/to/module.js');";
    CompilerInput input = new CompilerInput(new File("input.js"), source);
    CompilerPass compilerPass = new ProcessCommonJSModules(null, "");
    Node root = NodeFactory.createRoot();

    ProcessCommonJSModules callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal nodeTraversal = new NodeTraversal(input, root, callback);
    compilerPass.process(input, root);

    Node requireNode = nodeTraversal.getRoot().getChildAtIndex(0);
    Node moduleRef = requireNode.getChildAtIndex(0);
    assertEquals("/absolute/path/to/module.js", moduleRef.getString());
  }

  @Test
  public void testRequireMissingModule() {
    String source = "require('missing.js');";
    CompilerInput input = new CompilerInput(new File("input.js"), source);
    CompilerPass compilerPass = new ProcessCommonJSModules(null, "");
    Node root = NodeFactory.createRoot();

    ProcessCommonJSModules callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal nodeTraversal = new NodeTraversal(input, root, callback);
    compilerPass.process(input, root);

    Node requireNode = nodeTraversal.getRoot().getChildAtIndex(0);
    assertNull(requireNode.getChildAtIndex(0));
  }

  @Test
  public void testRequireCircularDependency() {
    String source1 = "module.exports = require('./module2');";
    String source2 = "module.exports = require('./module1');";
    CompilerInput input1 = new CompilerInput(new File("module1.js"), source1);
    CompilerInput input2 = new CompilerInput(new File("module2.js"), source2);
    CompilerPass compilerPass = new ProcessCommonJSModules(null, "");
    Node root1 = NodeFactory.createRoot();
    Node root2 = NodeFactory.createRoot();

    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback1 =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    ProcessCommonJSModules.ProcessCommonJsModulesCallback callback2 =
        new ProcessCommonJSModules.ProcessCommonJsModulesCallback();

    NodeTraversal nodeTraversal1 = new NodeTraversal(input1, root1, callback1);
    NodeTraversal nodeTraversal2 = new NodeTraversal(input2, root2, callback2);
    compilerPass.process(input1, root1);
    compilerPass.process(input2, root2);

    assertThrows(RuntimeException.class, () -> nodeTraversal1.getRoot().getChildAtIndex(0).getChildAtIndex(0));
    assertThrows(RuntimeException.class, () -> nodeTraversal2.getRoot().getChildAtIndex(0).getChildAtIndex(0));
  }

  @Test
  public void testProvideCall() {
    String source = "/** @suppress {checkTypes} */\n" +
        "goog.provide('my.module.name');";
    CompilerInput input = new CompilerInput(new File("input.js"), source);
    CompilerPass compilerPass = new ProcessCommonJSModules(null, "");
    Node root = NodeFactory.createRoot();

    ProcessCommonJSModules callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal nodeTraversal = new NodeTraversal(input, root, callback);
    compilerPass.process(input, root);

    Set<String> provides = Sets.newHashSet(callback.getModule().getProvides());
    assertEquals("my.module.name", provides.iterator().next());
  }

  @Test
  public void testProcessCommonJSModulesWithExistingProvides() {
    String source = "/** @suppress {checkTypes} */\n" +
        "goog.provide('goog.array');\n" +
        "goog.array.push = function() {}\n";
    CompilerInput input = new CompilerInput(new File("input.js"), source);
    CompilerPass compilerPass = new ProcessCommonJSModules(null, "");
    Node root = NodeFactory.createRoot();

    ProcessCommonJSModules callback = new ProcessCommonJSModules.ProcessCommonJsModulesCallback();
    NodeTraversal nodeTraversal = new NodeTraversal(input, root, callback);
    compilerPass.process(input, root);

    assertNotNull(callback.getModule().getProvides());
    assertEquals(1, callback.getModule().getProvides().size());
    assertEquals("goog.array", callback.getModule().getProvides().iterator().next());
  }
}