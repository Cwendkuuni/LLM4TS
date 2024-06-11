import static org.junit.Assert.*;
import static com.google.javascript.jscomp.AbstractCompiler.READ_ERROR;
import static com.google.javascript.jscomp.SourceFile.FILE_CONTENTS;

import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.test.TestSourceFile;
import com.google.javascript.rhino.Node;

public class JsAstTest {

  @Test
  public void testJsAst_ValidSourceFile() {
    CompilerOptions options = new CompilerOptions();
    SourceFile sourceFile = new TestSourceFile("test.js", "function foo() {}");
    JsAst jsAst = new JsAst(sourceFile);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Node astRoot = jsAst.getAstRoot(compiler);

    assertNotNull(astRoot);
    assertEquals(IR.script(), astRoot.asTopLevelDeclaration());

    // Check that the script node has a single function declaration as a child.
    Node functionDeclaration = assertNotNull(astRoot.getFirstChild());
    assertEquals(IR.functionDeclaration(), functionDeclaration.getNodeType());
    assertEquals("foo", functionDeclaration.getName());
    assertEquals(0, functionDeclaration.getNumChildren());
  }

  @Test
  public void testJsAst_InvalidSourceFile() {
    CompilerOptions options = new CompilerOptions();
    SourceFile sourceFile = new TestSourceFile("test.js", "function foo() {");
    JsAst jsAst = new JsAst(sourceFile);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Node astRoot = jsAst.getAstRoot(compiler);

    assertNull(astRoot);
    assertTrue(compiler.hasErrors());
    assertEquals(1, compiler.numErrors());
    assertEquals(READ_ERROR, compiler.getError(0).getErrorCode());
  }

  @Test
  public void testJsAst_ParseFailure() {
    CompilerOptions options = new CompilerOptions();
    SourceFile sourceFile = new TestSourceFile("test.js", "function foo() { SyntaxError }");
    JsAst jsAst = new JsAst(sourceFile);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Node astRoot = jsAst.getAstRoot(compiler);

    assertNull(astRoot);
    assertTrue(compiler.hasErrors());
    assertEquals(1, compiler.numErrors());
    assertEquals(AbstractCompiler.PARSE_ERROR, compiler.getError(0).getErrorCode());
  }

  @Test
  public void testJsAst_ClearAst() {
    CompilerOptions options = new CompilerOptions();
    SourceFile sourceFile = new TestSourceFile("test.js", "function foo() {}");
    JsAst jsAst = new JsAst(sourceFile);

    AbstractCompiler compiler = new AbstractCompiler(options);
    Node astRoot = jsAst.getAstRoot(compiler);

    // Verify that the AST was parsed successfully.
    assertNotNull(astRoot);
    assertEquals(IR.script(), astRoot.asTopLevelDeclaration());

    // Clear the AST and verify that it is now null.
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testJsAst_SetSourceFile() {
    CompilerOptions options = new CompilerOptions();
    SourceFile sourceFile1 = new TestSourceFile("test1.js", "function foo() {}");
    SourceFile sourceFile2 = new TestSourceFile("test2.js", "function bar() {}");
    JsAst jsAst = new JsAst(sourceFile1);

    jsAst.setSourceFile(sourceFile2);

    assertEquals(sourceFile2.getName(), jsAst.getSourceFile().getName());
  }

  @Test(expected = AssertionError.class)
  public void testJsAst_MismatchedSourceFileNames() {
    CompilerOptions options = new CompilerOptions();
    SourceFile sourceFile1 = new TestSourceFile("test1.js", "function foo() {}");
    SourceFile sourceFile2 = new TestSourceFile("test2.js", "function bar() {}");
    JsAst jsAst = new JsAst(sourceFile1);

    jsAst.setSourceFile(sourceFile2);

    fail("Setting a JsAst object to a source file with a different name should throw an exception.");
  }
}