import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.common.collect.Sets;

public class ProcessCommonJSModulesTest {

  @Test
  public void testProcessCommonJSModules() {
    // Given
    String inputFilename = "input.js";
    String outputFilename = "output.js";
    AbstractCompiler compiler = new Compiler();
    CompilerPass processCommonJSModules = new ProcessCommonJSModules(compiler, "");

    // When
    Node externs = Node.object();
    Node root = Node.script(inputFilename);
    processCommonJSModules.process(externs, root);
    Node processedRoot = root;

    // Then
    Node output = Node.object();
    assertEquals(processedRoot.toString(), output.toString());

    // Given - edge case: empty input
    root = Node.empty();
    processCommonJSModules.process(externs, root);
    processedRoot = root;

    // Then
    assertNotNull(processedRoot);
    assertTrue(processedRoot.isEmptyness());

    // Given - error scenario: invalid URI
    root = Node.script("input.js\0");
    try {
      processCommonJSModules.process(externs, root);
      fail("Expected CompilerException");
    } catch (Compiler.CompilerException e) {
      // Then
      assertEquals("Invalid input filename: input.js\0", e.getMessage());
    }
  }
}