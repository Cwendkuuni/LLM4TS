import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Logger;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private JsAst jsAst;
  private AbstractCompiler compiler;
  private ParserConfig parserConfig;
  private Logger logger;
  private SourceFile sourceFile;

  @Before
  public void setup() {
    parserConfig = mock(ParserConfig.class);
    compiler = mock(AbstractCompiler.class);
    logger = mock(Logger.class);
    sourceFile = new SourceFile("test.js", "test contents");
    jsAst = new JsAst(sourceFile);

    when(compiler.getParserConfig()).thenReturn(parserConfig);
    when(compiler.getDefaultErrorReporter()).thenReturn(logger);
  }

  @Test
  public void testGetAstRoot() {
    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }

  @Test
  public void testClearAst() {
    jsAst.parse(compiler);
    jsAst.clearAst();
    Node root = jsAst.getAstRoot(compiler);
    assertNull(root);
  }

  @Test
  public void testGetInputId() {
    InputId expectedInputId = new InputId("test.js");
    assert(jsAst.getInputId().equals(expectedInputId));
  }

  @Test
  public void testGetSourceFile() {
    assert(jsAst.getSourceFile().equals(sourceFile));
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("newTest.js", "new test contents");
    jsAst.setSourceFile(newSourceFile);
    assert(jsAst.getSourceFile().equals(newSourceFile));
  }
}