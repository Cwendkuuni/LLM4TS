import static org.junit.Assert.*;
import static com.google.javascript.jscomp.JsCompiler.CompilerOptions.WarningLevel;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.JsAst;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.TestSourceFile;
import com.google.javascript.rhino.Node;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class JsAstTest {
  private static AbstractCompiler compiler;

  @BeforeClass
  public static void setUp() {
    compiler = new AbstractCompiler();
  }

  @AfterClass
  public static void tearDown() {
    compiler = null;
  }

  @Test
  public void testValidJsFile() {
    SourceFile sourceFile = new TestSourceFile("test.js", "function foo() {}");
    JsAst ast = new JsAst(sourceFile);

    Node root = ast.getAstRoot(compiler);
    assertEquals("function", root.getClass().getSimpleName());

    Node functionNode = root.getFirstChild();
    assertEquals("foo", functionNode.getName());

    compiler.dispose();
  }

  @Test
  public void testInvalidJsFile() {
    SourceFile sourceFile = new TestSourceFile("test.js", "function foo() {");
    JsAst ast = new JsAst(sourceFile);

    try {
      Node root = ast.getAstRoot(compiler);
      fail("Expected parse error");
    } catch (AssertionError e) {
      // Expected
    }

    compiler.getErrors().clear();

    // Make sure the compiler reported the error.
    assertEquals(1, compiler.getErrors().size());
    JSError error = compiler.getErrors().get(0);
    assertEquals(AbstractCompiler.SYNTAX_ERROR, error.getKind());
    assertEquals("Unexpected token {", error.getMessage());

    compiler.dispose();
  }

  @Test
  public void testParseErrorWithErrorsReported() {
    SourceFile sourceFile = new TestSourceFile("test.js", "function foo() {function bar() {");
    compiler.setWarningLevel(WarningLevel.QUIET);
    JsAst ast = new JsAst(sourceFile);

    try {
      Node root = ast.getAstRoot(compiler);
      fail("Expected parse error");
    } catch (AssertionError e) {
      // Expected
    }

    compiler.getErrors().clear();

    // Make sure the compiler reported the error.
    assertEquals(1, compiler.getErrors().size());
    JSError error = compiler.getErrors().get(0);
    assertEquals(AbstractCompiler.SYNTAX_ERROR, error.getKind());
    assertEquals("Unexpected token function", error.getMessage());

    compiler.dispose();
  }

  @Test
  public void testReuseOldAst() {
    SourceFile sourceFile = new TestSourceFile("test.js", "function foo() {}");
    JsAst ast = new JsAst(sourceFile);

    Node oldAst = ast.getOldParseTree(sourceFile.getName());
    assertNotNull(oldAst);

    Node root = ast.getAstRoot(compiler);
    assertEquals("function", root.getClass().getSimpleName());

    Node functionNode = root.getFirstChild();
    assertEquals("foo", functionNode.getName());

    compiler.dispose();
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new TestSourceFile("test.js", "function foo() {}");
    JsAst ast = new JsAst(sourceFile);

    Node oldAst = ast.getOldParseTree(sourceFile.getName());

    ast.clearAst();

    Node newAstRoot = ast.getAstRoot(compiler);
    assertNull(ast.getOldParseTree(sourceFile.getName()));
    assertNotNull(newAstRoot);
    assertEquals("script", newAstRoot.getClass().getSimpleName());

    compiler.dispose();
  }
}