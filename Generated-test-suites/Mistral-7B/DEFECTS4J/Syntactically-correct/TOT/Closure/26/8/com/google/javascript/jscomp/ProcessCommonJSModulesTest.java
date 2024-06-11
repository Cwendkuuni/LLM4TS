import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.JSModule;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    AbstractCompiler compiler = new Compiler(); // Assuming Compiler is an instance of AbstractCompiler
    String filenamePrefix = "test_";

    // Prepare input source files
    CompilerInput input = new CompilerInput(new File("input1.js"), null);
    input.setFileName(filenamePrefix + "input1.js");
    input.addSourceFile("input2.js");

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, filenamePrefix);
    processor.process(input, null);

    // Verify output
    JSModule module = processor.getModule();
    assertEquals("module$input1", module.getName());
    assertEquals("module$input2", module.getDependentModules().get(0));

    // Test rewritten code
    Node root = input.getTree();
    assertFalse(root.hasChildWithName("require"));
    Node exports = root.getFirstChildWithName("exports");
    assertTrue(exports.isObjectLiteral());
    assertEquals("module$exports", exports.getName());
  }

  @Test
  public void testProcessCommonJSModulesWithRelativeRequire() {
    AbstractCompiler compiler = new Compiler(); // Assuming Compiler is an instance of AbstractCompiler
    String filenamePrefix = "test_";

    // Prepare input source files
    CompilerInput input = new CompilerInput(new File("input1.js"), null);
    input.setFileName(filenamePrefix + "input1.js");
    input.addSourceFile("./input2.js"); // relative require

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, filenamePrefix);
    processor.process(input, null);

    // Verify output
    JSModule module = processor.getModule();
    assertEquals("module$input1", module.getName());
    assertEquals("module$input2", module.getDependentModules().get(0));

    // Test rewritten code
    Node root = input.getTree();
    assertFalse(root.hasChildWithName("require"));
    Node exports = root.getFirstChildWithName("exports");
    assertTrue(exports.isObjectLiteral());
    assertEquals("module$exports", exports.getName());
  }

  @Test
  public void testProcessCommonJSModulesWithMissingModule() {
    AbstractCompiler compiler = new Compiler(); // Assuming Compiler is an instance of AbstractCompiler
    String filenamePrefix = "test_";

    // Prepare input source files
    CompilerInput input = new CompilerInput(new File("input1.js"), null);
    input.setFileName(filenamePrefix + "input1.js");
    input.addSourceFile("missing_module.js");

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, filenamePrefix);
    processor.process(input, null);

    // Verify output
    JSModule module = processor.getModule();
    assertNull(module.getDependentModules());

    // Test rewritten code
    Node root = input.getTree();
    assertFalse(root.hasChildWithName("require"));
    Node exports = root.getFirstChildWithName("exports");
    assertTrue(exports.isObjectLiteral());
    assertEquals("module$exports", exports.getName());
  }

  @Test
  public void testProcessCommonJSModulesWithCyclicDependency() {
    AbstractCompiler compiler = new Compiler(); // Assuming Compiler is an instance of AbstractCompiler
    String filenamePrefix = "test_";

    // Prepare input source files
    CompilerInput input1 = new CompilerInput(new File("input1.js"), null);
    input1.setFileName(filenamePrefix + "input1.js");
    input1.addSourceFile("input2.js");

    CompilerInput input2 = new CompilerInput(new File("input2.js"), null);
    input2.setFileName(filenamePrefix + "input2.js");
    input2.addSourceFile("input1.js");

    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, filenamePrefix);
    processor.process(input1, null);
    processor.process(input2, null);

    // Verify output
    JSModule module1 = processor.getModule();
    JSModule module2 = processor.getModule();
    assertEquals("module$input1", module1.getName());
    assertEquals("module$input2", module2.getName());
    assertEquals(module1.getName(), module2.getDependentModules().get(0));
    assertEquals(module2.getName(), module1.getDependentModules().get(0));

    // Test rewritten code
    Node root1 = input1.getTree();
    assertFalse(root1.hasChildWithName("require"));
    Node exports1 = root1.getFirstChildWithName("exports");
    assertTrue(exports1.isObjectLiteral());
    assertEquals("module$exports", exports1.getName());

    Node root2 = input2.getTree();
    assertFalse(root2.hasChildWithName("require"));
    Node exports2 = root2.getFirstChildWithName("exports");
    assertTrue(exports2.isObjectLiteral());
    assertEquals("module$exports", exports2.getName());
  }
}