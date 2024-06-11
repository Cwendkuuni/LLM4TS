import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.Scope;

import java.io.File;
import java.net.URI;

import org.junit.Before;
import org.junit.Test;

public class ProcessCommonJSModulesTest {

  private AbstractCompiler compiler;
  private ProcessCommonJSModules processor;
  private CompilerInput input;
  private NodeTraversal traversal;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    input = mock(CompilerInput.class);
    traversal = mock(NodeTraversal.class);
    when(compiler.getInput()).thenReturn(input);
    when(input.getTraversal()).thenReturn(traversal);
    processor = new ProcessCommonJSModules(compiler, ".");
  }

  @Test
  public void testProcessCommonJSModule() {
    String source = "var exports = module.exports = {};" +
        "require('moduleA');";

    Node root = IR.script(source).copyInformationFromForTree(null);
    processor.process(null, root);

    Node moduleExports = root.getChildAtIndex(0);
    assertEquals(".", moduleExports.getString());
    assertEquals(1, moduleExports.getChildren().size());
    assertNull(moduleExports.getChildAtIndex(0).getFirstChild());

    Node requireCall = root.getChildAtIndex(1);
    assertEquals("moduleA", requireCall.getChildAtIndex(1).getString());
  }

  @Test
  public void testProcessMultipleCommonJSModules() {
    String source = "var moduleA = {" +
        "exports: { foo: 'bar' }," +
        "require: function() {}" +
        "};" +
        "" +
        "var moduleB = {" +
        "exports: { baz: 'qux' }," +
        "require: function() {}" +
        "};" +
        "" +
        "var exports = module.exports = {};" +
        "require('moduleA').foo = 1;" +
        "exports.qux = require('moduleB').baz;"
        + "require('moduleB');";

    Node root = IR.script(source).copyInformationFromForTree(null);
    processor.process(null, root);

    Node moduleA = root.getChildAtIndex(0);
    Node exportsA = moduleA.getChildAtIndex(0);
    assertEquals("moduleA", exportsA.getString());
    assertEquals(1, exportsA.getChildren().size());
    assertEquals("foo", exportsA.getChildAtIndex(0).getString());
    assertEquals("exports", exportsA.getChildAtIndex(1).getString());

    Node moduleB = root.getChildAtIndex(1);
    Node exportsB = moduleB.getChildAtIndex(0);
    assertEquals("moduleB", exportsB.getString());
    assertEquals(1, exportsB.getChildren().size());
    assertEquals("exports", exportsB.getChildAtIndex(0).getString());

    Node requireCallModuleA = root.getChildAtIndex(3);
    assertEquals("moduleA", requireCallModuleA.getChildAtIndex(1).getString());

    Node requireCallModuleB = root.getChildAtIndex(5);
    assertEquals("moduleB", requireCallModuleB.getChildAtIndex(1).getString());
  }

  @Test
  public void testProcessWithDependenciesReporting() {
    String source = "var moduleA = {" +
        "exports: { foo: 'bar' }," +
        "require: function() {}" +
        "};" +
        "var moduleB = {" +
        "exports: { baz: 'qux' }," +
        "require: function() {}" +
        "};" +
        "" +
        "var exports = module.exports = {};" +
        "require('moduleA').foo = 1;" +
        "exports.qux = require('moduleB').baz;"
        + "require('moduleB');";

    Node root = IR.script(source).copyInformationFromForTree(null);
    ProcessCommonJSModules processorWithReporting = new ProcessCommonJSModules(
        compiler, ".", true);
    processorWithReporting.process(null, root);

    Set<String> dependencies = Sets.newHashSet();
    when(input.getDependencies()).thenReturn(dependencies);
    processorWithReporting.process(null, root);

    assertEquals(2, dependencies.size());
    assertEquals("moduleA", dependencies.iterator().next());
    assertEquals("moduleB", dependencies.iterator().next());
  }

  @Test
  public void testProcessWithRelativeRequire() {
    String source = "var exports = module.exports = {};" +
        "require('./moduleA');";

    Node root = IR.script(source).copyInformationFromForTree(null);
    String currentFileName = "src/main.js";
    String requiredFileName = "src/moduleA.js";
    processor.process(null, root);

    Node requireCall = root.getChildAtIndex(1);
    String expectedModuleName = toModuleName("src/moduleA.js", currentFileName);
    assertEquals(expectedModuleName, toModuleName(requireCall.getChildAtIndex(1).getString()));
  }

  @Test
  public void testProcessWithModuleExports() {
    String source = "var moduleA = {" +
        "exports: { foo: 'bar' }," +
        "require: function() {}" +
        "};" +
        "exports = moduleA.exports;" +
        "delete moduleA.exports;";

    Node root = IR.script(source).copyInformationFromForTree(null);
    processor.process(null, root);

    Node exports = root.getChildAtIndex(0);
    assertEquals("exports", exports.getString());
    assertEquals(1, exports.getChildren().size());
    assertEquals("foo", exports.getChildAtIndex(0).getString());

    Node moduleA = root.getChildAtIndex(1);
    assertNull(moduleA.getChildAtIndex(0));
  }

  private static String toModuleName(String requiredFilename,
      String currentFilename) {
    return ProcessCommonJSModules.toModuleName(requiredFilename, currentFilename);
  }
}