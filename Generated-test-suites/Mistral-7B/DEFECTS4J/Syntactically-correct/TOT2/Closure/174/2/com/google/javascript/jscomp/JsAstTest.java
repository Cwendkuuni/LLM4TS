// Combined Test File

import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.JsAst;

public class JsAstTest {
  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);
    Node root = jsAst.getAstRoot(new AbstractCompiler()); // Assuming AbstractCompiler is a mock or testable class
    assertNotNull(root);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(new AbstractCompiler()));
  }

  @Test
  public void testParseSuccess() {
    SourceFile sourceFile = new SourceFile("test.js", "valid code");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler(); // Assuming AbstractCompiler is a mock or testable class
    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }

  @Test(expected = IOException.class)
  public void testParseIOException() {
    SourceFile sourceFile = new SourceFile("test.js", "test code with errors");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler(); // Assuming AbstractCompiler is a mock or testable class
    try {
      jsAst.parse(compiler);
    } catch (IOException e) {
      // Expected
    }
  }

  @Test(expected = NullPointerException.class)
  public void testParseNullCompiler() {
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);
    try {
      jsAst.parse(null);
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @Test
  public void testParseEmptyCode() {
    SourceFile sourceFile = new SourceFile("test.js", "");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new AbstractCompiler(); // Assuming AbstractCompiler is a mock or testable class
    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    assertTrue(root instanceof IR.Block);
  }
}

//