import static org.junit.Assert.*;
import static com.google.javascript.jscomp.parser.TestUtil.parsejs;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.parser.SourceFile;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class JsAstTest {

  @Test
  public void testCreateJsAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);
    assertNotNull(jsAst.getAstRoot(new Compiler()));
  }

  @Test
  public void testParseNullSourceFile() {
    JsAst jsAst = new JsAst(null);
    assertNull(jsAst.getAstRoot(new Compiler()));
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(new Compiler());
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(new Compiler()));
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertEquals("test.js", inputId.getName());
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile sourceFileResult = jsAst.getSourceFile();
    assertEquals(sourceFile, sourceFileResult);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile1 = new SourceFile("test1.js", "test code 1");
    SourceFile sourceFile2 = new SourceFile("test2.js", "test code 2");
    JsAst jsAst = new JsAst(sourceFile1);
    jsAst.setSourceFile(sourceFile2);
    assertEquals(sourceFile2, jsAst.getSourceFile());
  }

  @Test
  public void testGetAstRootNullCompiler() {
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);
    assertNull(jsAst.getAstRoot(null));
  }

  @Test
  public void testGetAstRootWithParseError() {
    SourceFile sourceFile = new SourceFile("test.js", "test code with error");
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new Compiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
    assertTrue(compiler.hasHaltingErrors());
    assertEquals(IR.script(), astRoot);
  }

  @Test
  public void testParse() throws IOException {
    String code = "function test() { return 1; }";
    SourceFile sourceFile = new SourceFile("test.js", code);
    JsAst jsAst = new JsAst(sourceFile);
    AbstractCompiler compiler = new Compiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    Node expectedAstRoot = parsejs(code);
    assertEquals(expectedAstRoot, astRoot);
  }

  @Test
  public void testSetSourceFileWithSameName() {
    SourceFile sourceFile1 = new SourceFile("test.js", "test code 1");
    SourceFile sourceFile2 = new SourceFile("test.js", "test code 2");
    JsAst jsAst = new JsAst(sourceFile1);
    jsAst.setSourceFile(sourceFile2);
    assertEquals(sourceFile2, jsAst.getSourceFile());
  }

  @Test (expected = IllegalArgumentException.class)
  public void testSetSourceFileWithDifferentName() {
    SourceFile sourceFile1 = new SourceFile("test1.js", "test code 1");
    SourceFile sourceFile2 = new SourceFile("test2.js", "test code 2");
    JsAst jsAst = new JsAst(sourceFile1);
    jsAst.setSourceFile(sourceFile2);
  }
}