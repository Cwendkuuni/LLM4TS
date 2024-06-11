import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.ParserRunner;
import com.google.javascript.rhino.SourceName;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Test class for JsAst.
 */
public class JsAstTest {

  private JsAst jsAst;
  private AbstractCompiler compiler;
  private ParserConfig parserConfig;
  private ErrorReporter errorReporter;
  private SourceFile sourceFile;

  @Before
  public void setup() {
    parserConfig = mock(ParserConfig.class);
    errorReporter = mock(ErrorReporter.class);
    compiler = new AbstractCompiler() {
      @Override
      public ParserConfig getParserConfig() {
        return parserConfig;
      }

      @Override
      public ErrorReporter getErrorReporter() {
        return errorReporter;
      }

      @Override
      public Logger getLogger() {
        return mock(Logger.class);
      }
    };
    sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() {
        return "function test() { return 42; }";
      }
    };
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    Node expectedRoot = IR.script();
    expectedRoot.setInputId(new InputId("test.js"));

    when(parserConfig.getOptParseTreeMode()).thenReturn(true);
    when(parserConfig.getOptKeepFreshAst()).thenReturn(true);
    ArgumentCaptor<ParserRunner.ParseResult> parseResultCaptor = ArgumentCaptor.forClass(ParserRunner.ParseResult.class);

    jsAst.parse(compiler);

    assertNotNull(jsAst.getAstRoot(compiler));
    ParserRunner.ParseResult result = parseResultCaptor.getValue();
    assertNotNull(result.oldAst);
    assertNull(result.ast);
    // Verify that the old AST was saved
    when(compiler.getOldParseTree(sourceFile.getName())).thenReturn(expectedRoot);
    Node actualRoot = jsAst.getAstRoot(compiler);
    assertEquals(expectedRoot, actualRoot);
  }

  @Test
  public void testClearAst() {
    Node root = IR.script();
    jsAst = new JsAst(sourceFile) {
      @Override
      public Node getAstRoot(AbstractCompiler compiler) {
        return root;
      }
    };

    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test(expected = AssertionError.class)
  public void testGetAstRootParseError() {
    when(parserConfig.getOptParseTreeMode()).thenReturn(true);
    when(parserConfig.getOptKeepFreshAst()).thenReturn(true);
    when(parserConfig.getErrorReporter()).thenReturn(errorReporter);
    when(errorReporter.fatal(java.util.Locale.US, SourceName.parse("test.js"), 0, 0, null, null)).thenReturn(null);

    jsAst.parse(compiler);

    jsAst.getAstRoot(compiler);
  }

  @Test
  public void testGetAstRootIOException() {
    when(parserConfig.getOptParseTreeMode()).thenReturn(true);
    when(parserConfig.getOptKeepFreshAst()).thenReturn(true);
    when(parserConfig.getErrorReporter()).thenReturn(errorReporter);

    when(errorReporter.fatal(java.util.Locale.US, SourceName.parse("test.js"), 0, 0, null, null))
        .thenThrow(new IOException("Error reading file"));

    jsAst.parse(compiler);

    Node root = IR.script();
    root.setInputId(new InputId("test.js"));
    when(compiler.getOldParseTree(sourceFile.getName())).thenReturn(root);
    Node actualRoot = jsAst.getAstRoot(compiler);
    assertEquals(root, actualRoot);
  }
}