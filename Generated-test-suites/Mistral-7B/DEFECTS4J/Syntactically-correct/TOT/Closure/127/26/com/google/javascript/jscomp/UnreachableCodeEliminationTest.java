import static com.google.javascript.jscomp.NodeTraversal.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.CompilerPassPipeline;
import com.google.javascript.jscomp.Config;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;
import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private CompilerPassManager passManager;
  private CompilerPassPipeline passPipeline;

  @Before
  public void setup() {
    Config config = new Config();
    CompilerOptions options = new CompilerOptions(config);
    compiler = new Compiler(options);
    passManager = new CompilerPassManager(compiler, options);
    passPipeline = new CompilerPassPipeline(passManager);
  }

  @Test
  public void testSimpleReturn() {
    String sourceCode = "function test() { return; }";
    Node root = AbstractCompiler.parseAndCompile(compiler, sourceCode).getAST();
    passPipeline.runPasses(root);
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testReturnWithSideEffect() {
    String sourceCode = "function test() { document.write('Hello'); return; }";
    Node root = AbstractCompiler.parseAndCompile(compiler, sourceCode).getAST();
    passPipeline.runPasses(root);
    assertTrue(root.getFirstChild().isText());
    assertEquals("Hello", root.getFirstChild().toString());
  }

  @Test
  public void testSimpleBreak() {
    String sourceCode = "function test() { for (var i = 0; i < 10; i++) { break; } }";
    Node root = AbstractCompiler.parseAndCompile(compiler, sourceCode).getAST();
    passPipeline.runPasses(root);
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testSimpleContinue() {
    String sourceCode = "function test() { for (var i = 0; i < 10; i++) { continue; } }";
    Node root = AbstractCompiler.parseAndCompile(compiler, sourceCode).getAST();
    passPipeline.runPasses(root);
    assertEquals(0, root.getChildCount());
  }

  @Test
  public void testDeadCodeWithSideEffect() {
    String sourceCode = "function test() { document.write('Before'); if (true) {} document.write('After'); }";
    Node root = AbstractCompiler.parseAndCompile(compiler, sourceCode).getAST();
    passPipeline.runPasses(root);
    assertTrue(root.getFirstChild().isText());
    assertEquals("Before", root.getFirstChild().toString());
  }

  @Test
  public void testComplexControlFlow() {
    String sourceCode = "function test() { if (true) { switch (1) { case 1: case 2: break; } } else if (true) { break; } }";
    Node root = AbstractCompiler.parseAndCompile(compiler, sourceCode).getAST();
    passPipeline.runPasses(root);
    assertEquals(0, root.getChildCount());
  }

  @Test(expected = AssertionError.class)
  public void testErrorScenario() {
    String sourceCode = "function test() { return undefined; }";
    Node root = AbstractCompiler.parseAndCompile(compiler, sourceCode).getAST();
    passPipeline.runPasses(root);
    fail("Test should have removed the return statement.");
  }
}