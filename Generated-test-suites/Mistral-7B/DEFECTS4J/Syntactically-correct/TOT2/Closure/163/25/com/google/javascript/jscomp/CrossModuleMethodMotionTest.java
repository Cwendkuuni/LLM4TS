import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeTree.*;

import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.IdGenerator;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.JSModule;

public class CrossModuleMethodMotionTest {

  @org.junit.Test
  public void testCrossModuleMethodMotion() {
    // Set up the test environment
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion crossModuleMethodMotion =
        new CrossModuleMethodMotion(compiler, idGenerator, true);

    passManager.addPass(crossModuleMethodMotion);

    // Create sample source code for testing
    String sourceCode =
        "module A {" +
        "  function f() {" +
        "    return 'A';" +
        "  }" +
        "}" +
        "module B {" +
        "  import f from 'A';" +
        "  function g() {" +
        "    return f();" +
        "  }" +
        "}";

    Node root = compiler.parseTree(sourceCode, null, null);

    // Run the test
    passManager.runPasses(root, null);

    // Verify the result
    JSModule moduleA = moduleGraph.getModuleByName("A");
    JSModule moduleB = moduleGraph.getModuleByName("B");
    Node fA = moduleA.getEntryNode().getFirstChild();
    Node gB = moduleB.getEntryNode().getFirstChild();

    // Check that the method f() in module A is stubbed
    assertEquals("Expected function f in A to be stubbed",
        1, fA.getNumChildren());
    Node stubbedF = fA.getFirstChild();
    assertEquals("Expected stub function to have the correct name",
        STUB_METHOD_NAME, stubbedF.getFirstChild().getString());

    // Check that the method g() in module B uses the stubbed function f()
    assertEquals("Expected g in B to use the stubbed f",
        1, gB.getNumChildren());
    Node callToF = gB.getFirstChild();
    assertEquals("Expected call to f in g to use the stubbed version",
        STUB_METHOD_NAME, callToF.getFirstChild().getString());

    // Check that the unstubbed function body is in the deepest module
    JSModule deepestModule = moduleGraph.getDeepestModule();
    Node functionBody = null;
    for (Node child : deepestModule.getEntryNode().getChildren()) {
      if (child instanceof ExprStatement &&
          child.getFirstChild().isFunction()) {
        functionBody = child;
        break;
      }
    }
    assertNotNull("Expected to find the unstubbed function body", functionBody);
  }

  @org.junit.Test
  public void testCrossModuleMethodMotionWithException() {
    // Set up the test environment
    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    IdGenerator idGenerator = new IdGenerator();
    CrossModuleMethodMotion crossModuleMethodMotion =
        new CrossModuleMethodMotion(compiler, idGenerator, true);

    passManager.addPass(crossModuleMethodMotion);

    // Create sample source code for testing
    String sourceCode =
        "module A {" +
        "  function f() {" +
        "    throw new Error('Error in A');" +
        "  }" +
        "}" +
        "module B {" +
        "  import f from 'A';" +
        "  function g() {" +
        "    f();" +
        "  }" +
        "}";

    Node root = compiler.parseTree(sourceCode, null, null);

    // Run the test
    try {
      passManager.runPasses(root, null);
      fail("Expected an exception to be thrown");
    } catch (RuntimeException e) {
      // Verify that the exception is thrown from the unstubbed function body
      assertEquals("Expected error message to match the one in A",
          "Error in A", e.getMessage());
    }
  }
}