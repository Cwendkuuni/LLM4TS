import static org.junit.Assert.*;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.JSModule;
import com.google.common.collect.Sets;

public class ProcessCommonJSModulesTest {
  @org.junit.BeforeClass
  public static void setup() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(
        new Compiler(), "test_prefix/");
  }

  @org.junit.Test
  public void testProcess_ValidCommonJSModule() {
    Node root = Node.objectLiteral();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(
        new Compiler(), "test_prefix/", true);
    processor.process(Node.objectLiteral(), root);

    JSModule module = processor.getModule();
    assertEquals("test_prefix/module1.js", module.getName());

    assertTrue(module.getExports().size() > 0);
    assertEquals("module1.exports.func1", module.getExports().get(0).getName());
  }

  @org.junit.Test
  public void testProcess_ModuleWithExports() {
    Node root = Node.objectLiteral();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(
        new Compiler(), "test_prefix/", true);
    processor.process(Node.objectLiteral(), root);

    JSModule module = processor.getModule();
    assertEquals("test_prefix/module1.js", module.getName());

    assertTrue(module.getExports().size() > 0);
    assertEquals("module1.exports.func1", module.getExports().get(0).getName());

    Node exports = module.getExports().get(0);
    assertEquals("func1", exports.getString());
  }

  @org.junit.Test
  public void testProcess_MultipleModulesInOneFile() {
    Node root = Node.objectLiteral();
    ProcessCommonJSModules processor = new ProcessCommonJSModules(
        new Compiler(), "test_prefix/", true);
    processor.process(Node.objectLiteral(), root);

    JSModule module1 = processor.getModule();
    assertEquals("test_prefix/module1.js", module1.getName());

    JSModule module2 = processor.getModule();
    assertEquals("test_prefix/module2.js", module2.getName());

    assertTrue(module1.getExports().size() > 0);
    assertEquals("module1.exports.func1", module1.getExports().get(0).getName());

    assertTrue(module2.getExports().size() > 0);
    assertEquals("module2.exports.func2", module2.getExports().get(0).getName());
  }

  @org.junit.Test(expected = RuntimeException.class)
  public void testProcess_InvalidURI() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(
        new Compiler(), "test_prefix/", true);
    Node root = Node.objectLiteral();
    processor.process(Node.objectLiteral(), root);

    String invalidURI = "test_prefix/../invalidURI";
    try {
      String moduleName = processor.guessCJSModuleName(invalidURI);
      fail("Expected an exception");
    } catch (RuntimeException e) {
      // Expected: RuntimeException
    }
  }

  @org.junit.Test(expected = URISyntaxException.class)
  public void testProcess_InvalidURIWithSlashes() {
    ProcessCommonJSModules processor = new ProcessCommonJSModules(
        new Compiler(), "test_prefix/", true);
    Node root = Node.objectLiteral();
    processor.process(Node.objectLiteral(), root);

    String invalidURI = "test_prefix/module//name.js";
    try {
      String moduleName = processor.guessCJSModuleName(invalidURI);
      fail("Expected an exception");
    } catch (URISyntaxException e) {
      // Expected: URISyntaxException
    }
  }
}