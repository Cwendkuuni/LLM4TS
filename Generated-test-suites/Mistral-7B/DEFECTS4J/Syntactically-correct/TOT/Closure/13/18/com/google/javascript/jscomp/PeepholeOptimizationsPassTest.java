import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AbstractPeepholeOptimization;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassTestBase;
import com.google.javascript.rhino.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PeepholeOptimizationsPassTest extends CompilerPassTestBase {

  private static AbstractCompiler compiler;
  private static PeepholeOptimizationsPass pass;

  @org.junit.BeforeClass
  public static void setUp() {
    compiler = createCompiler();
    List<AbstractPeepholeOptimization> optimizations = new ArrayList<>();
    optimizations.add(new ConstantFoldingOptimization());
    optimizations.add(new DeadCodeRemovalOptimization());
    pass = new PeepholeOptimizationsPass(compiler, optimizations.toArray(new AbstractPeepholeOptimization[0]));
  }

  @org.junit.Test
  public void testSimpleConstantFolding() {
    String input = "function foo(x) { return x + 1; } function bar() { return foo(2); }";
    Node root = parseAndCompile(input);
    Node output = pass.process(null, root).getRootNode();

    checkCompilationResult("function bar() { return 3; }", output);
  }

  @org.junit.Test
  public void testComplexConstantFolding() {
    String input = "function foo(x) { return (x + 1) * (x + 1); } function bar() { return foo(2); }";
    Node root = parseAndCompile(input);
    Node output = pass.process(null, root).getRootNode();

    checkCompilationResult("function bar() { return 17; }", output);
  }

  @org.junit.Test
  public void testDeadCodeRemoval() {
    String input = "function foo(x) { if (x < 0) { return 'negative number'; } else { return x * x; } } function bar() { foo(-1); }";
    Node root = parseAndCompile(input);
    Node output = pass.process(null, root).getRootNode();

    checkCompilationResult("function bar() { }", output);
  }

  @org.junit.Test
  public void testDeadCodeRemovalWithSideEffects() {
    String input = "function foo(x) { if (x < 0) { console.log('negative number'); return; } else { return x * x; } } function bar() { foo(-1); }";
    Node root = parseAndCompile(input);
    Node output = pass.process(null, root).getRootNode();

    checkCompilationResult("function bar() { console.log('negative number'); }", output);
  }

  @org.junit.Test
  public void testPeepholeOptimizationError() {
    String input = "function foo() { try { throw new Error('Test error'); } catch (e) { return e.message; } } function bar() { foo(); }";
    Node root = parseAndCompile(input);

    try {
      pass.process(null, root).getRootNode();
      fail("Expected a compilation error.");
    } catch (CompilerException e) {
      assertTrue("Expected error message to contain 'Error message is not a constant expression, and cannot be folded into a constant.', but got: " + e.getMessage(), e.getMessage().contains("Error message is not a constant expression, and cannot be folded into a constant."));
    }
  }

  private void checkCompilationResult(String expectedResult, Node actualResult) {
    String actualCode = getCodeFromNode(actualResult);
    assertEquals(expectedResult, actualCode);
  }

  @org.junit.AfterClass
  public static void tearDown() {
    pass = null;
    compiler = null;
  }
}

class ConstantFoldingOptimization implements AbstractPeepholeOptimization {
  // Implement the optimizeSubtree method here.
}

class DeadCodeRemovalOptimization implements AbstractPeepholeOptimization {
  // Implement the optimizeSubtree method here.
}