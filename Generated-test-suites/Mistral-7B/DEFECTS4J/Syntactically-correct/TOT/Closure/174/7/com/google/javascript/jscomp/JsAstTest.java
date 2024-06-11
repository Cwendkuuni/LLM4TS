import static org.junit.Assert.*;
import static com.google.javascript.jscomp.testing.CodeUtils.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.ExpectedError;
import com.google.javascript.jscomp.testing.TestSourceFile;
import com.google.javascript.rhino.Node;

import org.junit.Test;

public class JsAstTest {
  @Test
  public void testValidAstGeneration() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(compiler);
    options.setWarningLevel(CompilerOptions.WarningLevel.QUIET);

    SourceFile sourceFile = new TestSourceFile(
        "testValidAstGeneration.js",
        "function foo() {}"
    );

    JsAst ast = new JsAst(sourceFile);
    Node root = ast.getAstRoot(compiler);

    assertEquals("function foo() {}", toSource(root));
  }

  @Test
  public void testErrorReporting() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(compiler);

    SourceFile sourceFile = new TestSourceFile(
        "testErrorReporting.js",
        "function foo() { bar(); }"
    );

    JsAst ast = new JsAst(sourceFile);

    ExpectedError error = ExpectedError.at(1, 5, JSError.UNDEFINED, "bar");
    compiler.addTopLevelErrors(error);

    ast.parse(compiler);
    assertEquals(1, compiler.getDiagnostics().getNumErrors());
    assertEquals("ReferenceError: 'bar' is not defined", compiler.getDiagnostics().getError(0).getMessage());
  }

  @Test
  public void testParseErrorHandling() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(compiler);

    SourceFile sourceFile = new TestSourceFile(
        "testParseErrorHandling.js",
        "function foo() { x: 5; }" // this should cause a parse error
    );

    JsAst ast = new JsAst(sourceFile);

    // There was a parse error, so root should be an empty script node
    Node root = ast.getAstRoot(compiler);
    assertTrue(root instanceof com.google.javascript.rhino.script.Script);
    assertEquals(0, root.getChildList().length);
  }

  @Test
  public void testClearAst() {
    AbstractCompiler compiler = new AbstractCompiler();
    CompilerOptions options = new CompilerOptions(compiler);

    SourceFile sourceFile = new TestSourceFile(
        "testClearAst.js",
        "function foo() { return 1; }"
    );

    JsAst ast = new JsAst(sourceFile);
    Node root = ast.getAstRoot(compiler);

    // Make sure the initial parse was successful
    assertEquals("function foo() { return 1; }", toSource(root));

    // Clear the AST and verify that the root is null
    ast.clearAst();
    assertNull(ast.getAstRoot(compiler));
  }
}