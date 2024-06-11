import static com.google.javascript.jscomp.JsAst.JsAstFactory.*;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {
  private AbstractCompiler compiler;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    compiler = new Compiler();
    compiler.setOptions(new CompilerOptions());
    sourceFile = new SourceFile("Test.js", "testCode");
  }

  @Test
  public void testJsAstCreation() {
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst);
  }

  @Test
  public void testJsAstParseSuccess() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testJsAstParseError() {
    String invalidCode = "function() { syntaxError(); }";
    sourceFile.setCode(invalidCode);
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertNull(jsAst.getAstRoot(compiler));
    // Check if the error was reported
    assertEquals(1, compiler.getDiagnostics().length());
  }

  @Test
  public void testJsAstParseIOException() {
    String nonExistentFile = "nonExistentFile.js";
    sourceFile = new SourceFile(nonExistentFile, "testCode");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertNull(jsAst.getAstRoot(compiler));
    // Check if the error was reported
    assertEquals(1, compiler.getDiagnostics().length());
  }

  @Test
  public void testJsAstClearAst() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }
}