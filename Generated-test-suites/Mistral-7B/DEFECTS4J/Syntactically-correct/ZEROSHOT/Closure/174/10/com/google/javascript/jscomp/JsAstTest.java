import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ParserRunner;
import com.google.javascript.rhino.SourceName;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

public class JsAstTest {
  private AbstractCompiler compiler;
  private ParserConfig parserConfig;
  private ParserRunner parserRunner;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setUp() {
    parserConfig = mock(ParserConfig.class);
    parserRunner = mock(ParserRunner.class);
    compiler = mock(AbstractCompiler.class);
    sourceFile = new SourceFile("test.js", "test code");
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRootNull() {
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetAstRootNonNull() {
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testClearAst() {
    jsAst.parse(compiler);
    jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    assertNotNull(jsAst.getInputId());
  }

  @Test
  public void testGetSourceFile() {
    assertNotNull(jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFileNew = new SourceFile("testNew.js", "test new code");
    jsAst.setSourceFile(sourceFileNew);
    assertNotNull(jsAst.getSourceFile());
    assertNotSame(sourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testParseNullCompiler() {
    jsAst.parse(null);
    verify(compiler).report(JSError.make(ParserConfig.PARSER_ERROR, "test.js"));
  }

  @Test
  public void testParseIOException() {
    Preconditions.checkState(parserRunner != null);
    when(parserRunner.parse(sourceFile, sourceFile.getCode(), parserConfig, eq(compiler.getErrorReporter()), eq(logger_)))
        .thenThrow(new IOException());

    jsAst.parse(compiler);
    verify(compiler).report(JSError.make(AbstractCompiler.READ_ERROR, "test.js"));
  }

  @Test
  public void testParseNullRoot() {
    when(parserRunner.parse(sourceFile, sourceFile.getCode(), parserConfig, eq(compiler.getErrorReporter()), eq(logger_)))
        .thenReturn(new ParserRunner.ParseResult(null, IR.script()));

    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseOldParseTreeSet() {
    when(parserRunner.parse(sourceFile, sourceFile.getCode(), parserConfig, eq(compiler.getErrorReporter()), eq(logger_)))
        .thenReturn(new ParserRunner.ParseResult(null, IR.script()));

    jsAst.parse(compiler);
    Node oldAst = IR.script();
    jsAst.setSourceFile(sourceFile);
    jsAst.parse(compiler);
    verify(compiler).setOldParseTree(eq(sourceFile.getName()), eq(oldAst));
  }

  @Captor
  ArgumentCaptor<Node> nodeCaptor;

  @Test
  public void testParseNewRootSet() {
    when(parserRunner.parse(sourceFile, sourceFile.getCode(), parserConfig, eq(compiler.getErrorReporter()), eq(logger_)))
        .thenReturn(new ParserRunner.ParseResult(null, IR.block(IR.list(IR.stringLiteral("test")))));

    jsAst.parse(compiler);
    Node root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
    verify(compiler).setOldParseTree(eq(sourceFile.getName()), isNull());
    verify(compiler).prepareAst(nodeCaptor.capture());
    assertSame(root, nodeCaptor.getValue());
  }
}