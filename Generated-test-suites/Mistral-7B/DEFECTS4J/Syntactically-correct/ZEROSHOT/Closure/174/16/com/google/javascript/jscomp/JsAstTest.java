import static org.junit.Assert.*;
import static com.google.javascript.jscomp.testing.CodeSource.createCodeSource;
import static com.google.javascript.jscomp.testing.TopLevelCompiler.checkCompilesTo;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.TopLevelCompiler;

@RunWith(JUnit4.class)
public class JsAstTest {
  private SourceFile sourceFile;
  private JsAst jsAst;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    sourceFile = new SourceFile("test.js", createCodeSource("1 + 1;"));
    jsAst = new JsAst(sourceFile);
    compiler = new TopLevelCompiler(new CompilerOptions());
  }

  @Test
  public void testGetAstRoot() {
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    Node astRoot = jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node newAstRoot = jsAst.getAstRoot(compiler);
    assertNull(newAstRoot);
  }

  @Test
  public void testGetInputId() {
    assertEquals("test.js", jsAst.getInputId().getName());
  }

  @Test
  public void testGetSourceFile() {
    assertEquals("test.js", jsAst.getSourceFile().getName());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newTest.js", createCodeSource("1 + 1;"));
    jsAst.setSourceFile(newSourceFile);
    assertEquals("newTest.js", jsAst.getSourceFile().getName());
  }

  @Test
  public void testParse() {
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    checkCompilesTo(compiler, astRoot, "1 + 1;", "2");
  }

  @Test(expected = NullPointerException.class)
  public void testParseNullCompiler() {
    jsAst.parse(null);
  }
}