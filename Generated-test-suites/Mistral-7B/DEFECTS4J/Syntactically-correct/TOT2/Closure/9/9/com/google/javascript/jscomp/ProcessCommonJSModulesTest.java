import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.newIdent;
import static com.google.javascript.rhino.NodeFactory.newString;
import static com.google.javascript.rhino.NodeFactory.newUri;

import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.jscomp.JSSourceFile;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.ProcessCommonJSModules;
import com.google.javascript.jscomp.ProcessCommonJSModules.ProcessCommonJsModulesCallback;
import com.google.javascript.jscomp.Scope;

import java.io.IOException;
import java.net.URI;
import java.util.Set;

public class ProcessCommonJSModulesTest extends CompilerTestCase {

  private static final String TEST_FILE = "TestProcessCommonJSModules.js";

  public ProcessCommonJSModulesTest() {
    super(ProcessCommonJSModules.class, TEST_FILE);
  }

  @Override
  protected CompilerPass getProcessor(AbstractCompiler compiler) {
    return new ProcessCommonJSModules(compiler, ".");
  }

  public void testProcessCommonJSModules() throws IOException {
    CompilerInput input = new CompilerInput(new JSSourceFile(TEST_FILE));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".");
    processor.process(input, getSourceRoot());

    JSSourceFile result = (JSSourceFile) input.getOutput();
    assertEquals(expectedOutput(), result.getBytecode());
  }

  private String expectedOutput() {
    // Expected output of the compiled test file
  }

  public void testProcessCommonJSModulesWithDependencies() throws IOException {
    CompilerInput input = new CompilerInput(new JSSourceFile("dependency.js"));
    Node dependencyNode = input.getTree();
    NodeTraversal.traverse(compiler, dependencyNode, new ProcessCommonJsModulesCallback());

    CompilerInput testInput = new CompilerInput(new JSSourceFile(TEST_FILE));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".", true);
    processor.process(testInput, getSourceRoot());

    JSSourceFile result = (JSSourceFile) testInput.getOutput();
    assertEquals(expectedOutputWithDependencies(), result.getBytecode());
  }

  private String expectedOutputWithDependencies() {
    // Expected output of the compiled test file with dependencies
  }

  public void testProcessCommonJSModulesWithRelativePaths() throws IOException {
    CompilerInput input = new CompilerInput(new JSSourceFile("relative/to/TestProcessCommonJSModules.js"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".");
    processor.process(input, getSourceRoot());

    JSSourceFile result = (JSSourceFile) input.getOutput();
    assertEquals(expectedOutputWithRelativePaths(), result.getBytecode());
  }

  private String expectedOutputWithRelativePaths() {
    // Expected output of the compiled test file with relative paths
  }

  public void testProcessCommonJSModulesWithExport() throws IOException {
    CompilerInput input = new CompilerInput(new JSSourceFile("moduleWithExport.js"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".");
    processor.process(input, getSourceRoot());

    JSSourceFile result = (JSSourceFile) input.getOutput();
    assertEquals(expectedOutputWithExport(), result.getBytecode());
  }

  private String expectedOutputWithExport() {
    // Expected output of the compiled test file with exports
  }

  public void testProcessCommonJSModulesWithError() throws IOException {
    CompilerInput input = new CompilerInput(new JSSourceFile("error.js"));
    ProcessCommonJSModules processor = new ProcessCommonJSModules(compiler, ".");
    assertThrows(RuntimeException.class, () -> {
      processor.process(input, getSourceRoot());
    });
  }

  private void assertThrows(Class<? extends Throwable> exceptionClass, Runnable runnable) {
    try {
      runnable.run();
      fail("Expected an exception of type " + exceptionClass);
    } catch (Throwable e) {
      if (exceptionClass.isInstance(e)) {
        return;
      }
      fail("Unexpected exception: " + e.getClass());
    }
  }
}