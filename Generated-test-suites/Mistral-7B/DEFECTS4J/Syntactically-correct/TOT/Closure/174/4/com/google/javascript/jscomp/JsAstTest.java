import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.InputId;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {
  private AbstractCompiler compiler;
  private SourceFile sourceFile;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    sourceFile = new SourceFile("test.js", "test content");
  }

  @Test
  public void testGetAstRoot() {
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.getAstRoot(compiler); // Parse the code
    jsAst.clearAst();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  @Test
  public void testParseWithErrors() {
    when(compiler.getParserConfig()).thenReturn(new ParserConfig());
    when(compiler.getDefaultErrorReporter()).thenReturn(new ErrorReporter() {
      @Override
      public void error(JSError error) {
        // Do nothing
      }
    });
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  @Test
  public void testParseSuccessfully() {
    when(compiler.getParserConfig()).thenReturn(new ParserConfig());
    when(compiler.getDefaultErrorReporter()).thenReturn(new ErrorReporter() {
      @Override
      public void error(JSError error) {
        throw new RuntimeException("Should not reach here");
      }
    });

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }
}