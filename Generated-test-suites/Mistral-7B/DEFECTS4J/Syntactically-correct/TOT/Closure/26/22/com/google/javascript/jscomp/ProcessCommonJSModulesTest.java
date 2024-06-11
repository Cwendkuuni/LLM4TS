import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() throws Exception {
    String sourceName = "test.js";
    CompilerInput input = new CompilerInput(sourceName, null);
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, "");

    // Test empty input
    Node root = Node.root();
    processor.process(input, root);
    assertEquals(0, input.getNumErrors());
    assertNull(input.getModule());

    // Test simple require statement
    Node require = IR.call(IR.getprop(IR.name("require"), null),
        IR.string("module1"));
    Node script = IR.script(require);
    input.setSource(sourceName, script);
    processor.process(input, root);
    JSModule module1 = input.getModule();
    assertEquals("module1", module1.getName());

    // Test require statement with dependencies
    String module1Source = "var a = require('module2');";
    Node module1Script = IR.script(IR.parse(module1Source));
    input.setSource(sourceName, module1Script);
    processor.process(input, root);
    JSModule module2 = input.getModule();
    assertEquals("module2", module2.getName());
    assertEquals(module1, module2.getDependingModule());

    // Test multiple require statements
    String source2 = "var a = require('module1');\n" +
        "var b = require('module3');";
    Node script2 = IR.script(IR.parse(source2));
    input.setSource(sourceName, script2);
    processor.process(input, root);
    JSModule module3 = input.getModule();
    assertEquals("module3", module3.getName());
    assertEquals(module1, module3.getDependingModule());

    // Test require statement with circular dependency
    String source3 = "var a = require('module2');\n" +
        "require('module1');";
    Node script3 = IR.script(IR.parse(source3));
    input.setSource(sourceName, script3);
    try {
      processor.process(input, root);
      fail("Expected error due to circular dependency");
    } catch (RuntimeException e) {
      // Expected
    }

    // Test require statement with missing module
    String source4 = "var a = require('missingModule');";
    Node script4 = IR.script(IR.parse(source4));
    input.setSource(sourceName, script4);
    try {
      processor.process(input, root);
      fail("Expected error due to missing module");
    } catch (RuntimeException e) {
      // Expected
    }

    // Test require statement with relative path
    String source5 = "var a = require('./module1');";
    Node script5 = IR.script(IR.parse(source5));
    String currentFilename = "test2.js";
    input.setSource(currentFilename, script5);
    processor.process(input, root);
    JSModule module4 = input.getModule();
    assertEquals("test2.js$module1", module4.getName());
    JSModule module1 = input.getModule("module1");
    assertEquals("module1", module1.getName());

    // Test require statement with multiple dependencies
    String source6 = "var a = require('module1');\n" +
        "var b = require('module2');\n" +
        "var c = require('module3');";
    Node script6 = IR.script(IR.parse(source6));
    input.setSource(sourceName, script6);
    processor.process(input, root);
    Set<JSModule> modules = Sets.newHashSet();
    modules.addAll(input.getModules().values());
    assertEquals(3, modules.size());
    JSModule module1 = input.getModule("module1");
    assertEquals("module1", module1.getName());
    JSModule module2 = input.getModule("module2");
    assertEquals("module2", module2.getName());
    JSModule module3 = input.getModule("module3");
    assertEquals("module3", module3.getName());
    assertEquals(module1, module2.getDependingModule());
    assertEquals(module1, module3.getDependingModule());
  }
}