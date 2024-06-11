import static org.junit.Assert.*;
import static com.google.javascript.jscomp.JSError.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private static final String SOURCE_CODE = "function test() { return 1; }";
  private AbstractCompiler compiler;
  private ErrorReporter errorReporter;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    errorReporter = new ErrorReporter() {}; // empty error reporter
    sourceFile = new SourceFile("test.js", SOURCE_CODE);
  }

  @Test
  public void testParseValidCode() {
    JsAst ast = new JsAst(sourceFile);
    AbstractCompiler.CompilerOptions options = new AbstractCompiler.CompilerOptions();
    Node root = ast.getAstRoot(compiler);
    assertEquals("Expected valid AST", IR.functionDeclaration("test", IR.functionType(IR.types.VOID, new IR.NamedExpression("test")), IR.block(IR.returnStatement(IR.intLiteral(1)))), root);
    assertTrue("Expected parse to succeed", compiler.hasNoErrors());
  }

  @Test
  public void testParseInvalidCode() {
    sourceFile.setCode("function test() { });"); // missing return statement
    JsAst ast = new JsAst(sourceFile);
    AbstractCompiler.CompilerOptions options = new AbstractCompiler.CompilerOptions();
    Node root = ast.getAstRoot(compiler);
    // Since the parse failed, root will be null and compiler will have errors
    assertNull("Expected parse to fail", root);
    assertFalse("Expected parse to report errors", compiler.hasNoErrors());
    assertEquals("Expected parse error", 1, compiler.getNumErrors());
    assertEquals("Expected parse error message", "Missing return statement", compiler.getErrorMessage(0));
  }

  @Test
  public void testClearAst() {
    JsAst ast = new JsAst(sourceFile);
    assertNotNull("Expected AST to be set after parsing", ast.getAstRoot(compiler));
    ast.clearAst();
    assertNull("Expected AST to be cleared", ast.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    JsAst ast = new JsAst(sourceFile);
    assertEquals("Expected input id to match source file name", "test.js", ast.getInputId().getName());
  }

  @Test
  public void testSetSourceFile() {
    JsAst ast = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("newTest.js", SOURCE_CODE);
    ast.setSourceFile(newSourceFile);
    assertEquals("Expected source file to be set", "newTest.js", ast.getInputId().getName());
  }

  @Test
  public void testParseWithConfig() {
    ParserConfig config = new ParserConfig();
    config.setAllowReserved(false);
    JsAst ast = new JsAst(sourceFile);
    AbstractCompiler.CompilerOptions options = new AbstractCompiler.CompilerOptions();
    Node root = ast.getAstRoot(new AbstractCompiler(options, compiler, errorReporter, config));
    // Since 'test' is a reserved word in JavaScript, parsing with allowReserved false should fail
    assertNull("Expected parse to fail when parsing with reserved word as identifier", root);
    assertTrue("Expected parse to report errors when parsing with reserved word as identifier", compiler.hasErrors());
  }
}