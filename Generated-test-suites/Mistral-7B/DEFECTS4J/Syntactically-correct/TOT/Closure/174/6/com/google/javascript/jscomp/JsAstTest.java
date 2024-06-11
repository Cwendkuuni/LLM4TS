import static org.junit.Assert.*;
import static com.google.javascript.jscomp.JsAst.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.Node;

public class JsAstTest {

  @org.junit.Test
  public void testJsAst() {
    // Test creating a new JsAst instance
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst);

    // Test getting the AST root
    AbstractCompiler compiler = new Compiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);

    // Test parsing an valid JavaScript file
    String validCode = "function helloWorld() {'return \"Hello World!\";'}";
    sourceFile.setCode(validCode);
    jsAst = new JsAst(sourceFile);
    astRoot = jsAst.getAstRoot(compiler);
    assertEquals("function", astRoot.getClass().getSimpleName());

    // Test parsing an invalid JavaScript file
    String invalidCode = "function helloWorld() {'return \"Hello World!\";'";
    sourceFile.setCode(invalidCode);
    jsAst = new JsAst(sourceFile);
    astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);

    // Test clearing the AST
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @org.junit.Test
  public void testSetSourceFile() {
    // Test setting the source file
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);

    String newFileName = "newFile.js";
    SourceFile newSourceFile = new SourceFile(newFileName, "new test code");
    jsAst.setSourceFile(newSourceFile);

    assertEquals(newFileName, jsAst.getSourceFile().getName());
  }
}