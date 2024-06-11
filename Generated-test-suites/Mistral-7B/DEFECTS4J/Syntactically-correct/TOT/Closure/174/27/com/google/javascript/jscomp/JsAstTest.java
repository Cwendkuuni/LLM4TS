import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.test_util.AbstractTest;
import com.google.javascript.jscomp.test_util.TestBase;
import com.google.javascript.rhino.Node;

public class JsAstTest extends AbstractTest {

  public void testJsAst_ValidCode() throws Exception {
    SourceFile sourceFile = new SourceFile("Test.js", "function foo() { return 42; }");
    AbstractCompiler compiler = new Compiler();
    JsAst ast = new JsAst(sourceFile);

    CompilerOptions options = new CompilerOptions(false, null);
    compiler.init(options, null);

    Node root = ast.getAstRoot(compiler);
    assertEquals(SCRIPT, root.getType());
    assertEquals("function foo() { return 42; }", sourceFile.getCode());
    assertEquals(42, compiler.getGlobalVar("foo").asInt());
  }

  public void testJsAst_InvalidCode() throws Exception {
    SourceFile sourceFile = new SourceFile("Test.js", "function foo() { return syntaxError; }");
    AbstractCompiler compiler = new Compiler();
    JsAst ast = new JsAst(sourceFile);

    CompilerOptions options = new CompilerOptions(false, null);
    compiler.init(options, null);

    try {
      Node root = ast.getAstRoot(compiler);
      fail("Expected parse error");
    } catch (AssertionError e) {
      // Expected
    }

    assertEquals(JSError.SYNTAX_ERROR, compiler.getLastError().getCode());
    assertEquals("Function declarations cannot have a return type other than 'void'.", compiler.getLastError().getMessage());
  }

  public void testJsAst_ClearAst() throws Exception {
    SourceFile sourceFile = new SourceFile("Test.js", "function foo() { return 42; }");
    AbstractCompiler compiler = new Compiler();
    JsAst ast = new JsAst(sourceFile);

    CompilerOptions options = new CompilerOptions(false, null);
    compiler.init(options, null);

    Node root = ast.getAstRoot(compiler);
    assertEquals(SCRIPT, root.getType());
    assertEquals("function foo() { return 42; }", sourceFile.getCode());
    assertEquals(42, compiler.getGlobalVar("foo").asInt());

    ast.clearAst();

    root = ast.getAstRoot(compiler);
    assertNull(root);
    assertEquals("", sourceFile.getCode());
  }

  public void testJsAst_SetSourceFile() throws Exception {
    SourceFile sourceFile1 = new SourceFile("Test1.js", "function foo() { return 42; }");
    AbstractCompiler compiler = new Compiler();
    JsAst ast = new JsAst(sourceFile1);

    CompilerOptions options = new CompilerOptions(false, null);
    compiler.init(options, null);

    Node root = ast.getAstRoot(compiler);
    assertEquals(SCRIPT, root.getType());
    assertEquals("function foo() { return 42; }", sourceFile1.getCode());

    sourceFile1.setCode("function bar() { return 43; }");
    ast.setSourceFile(sourceFile1);

    root = ast.getAstRoot(compiler);
    assertEquals(SCRIPT, root.getType());
    assertEquals("function bar() { return 43; }", sourceFile1.getCode());
  }
}