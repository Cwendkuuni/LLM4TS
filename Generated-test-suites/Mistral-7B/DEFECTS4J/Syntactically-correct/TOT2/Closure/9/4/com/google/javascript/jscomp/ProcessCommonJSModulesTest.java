import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.ProcessCommonJSModules;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    CompilerInput input = new CompilerInput();
    String filename = "test.js";
    String sourceCode = "require('module1');";
    input.setSourceFileName(filename);
    input.setSourceCode(sourceCode);

    ProcessCommonJSModules processor = new ProcessCommonJSModules(
        new CompilerPassAdapter(), ".");
    processor.process(input, null);

    Node root = input.getRootNode();
    Node requireCall = root.getFirstChild().getFirstChild();
    Node moduleRef = requireCall.getFirstChild();

    assertEquals("module1", moduleRef.getString());
  }

  @Test
  public void testProcessCommonJSModulesWithModulesExports() {
    CompilerInput input = new CompilerInput();
    String filename = "test.js";
    String sourceCode = "module.exports = 'hello';";
    input.setSourceFileName(filename);
    input.setSourceCode(sourceCode);

    ProcessCommonJSModules processor = new ProcessCommonJSModules(
        new CompilerPassAdapter(), ".", true);
    processor.process(input, null);

    Node root = input.getRootNode();
    Node moduleExports = root.getLastChild();

    assertEquals("hello", moduleExports.getString());
  }

  @Test(expected = RuntimeException.class)
  public void testProcessCommonJSModulesWithInvalidURI() {
    CompilerInput input = new CompilerInput();
    String filename = "test.js";
    String sourceCode = "require('../invalidURI/module1.js');";
    input.setSourceFileName(filename);
    input.setSourceCode(sourceCode);

    ProcessCommonJSModules processor = new ProcessCommonJSModules(
        new CompilerPassAdapter(), ".", true);
    processor.process(input, null);

    fail("Exception expected");
  }
}