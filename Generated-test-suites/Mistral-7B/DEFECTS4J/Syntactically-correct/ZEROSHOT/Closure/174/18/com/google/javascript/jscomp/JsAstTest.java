import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class JsAstTest {

  private SourceFile sourceFile;
  private AbstractCompiler compiler;
  private ParserConfig parserConfig;
  private ParserRunner parserRunner;
  private JsAst jsAst;

  @Before
  public void setUp() {
    parserConfig = mock(ParserConfig.class);
    compiler = mock(AbstractCompiler.class);
    parserRunner = mock(ParserRunner.class);
    sourceFile = new SourceFile("test.js", "test.js", new StringSource(""));
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRootWhenNullAst() {
    when(parserRunner.parse(sourceFile, "", parserConfig, compiler.getErrorReporter(), logger_))
        .thenReturn(new ParserRunner.ParseResult(null));
    when(compiler.getDefaultErrorReporter()).thenReturn(mock(ErrorReporter.class));

    Node astRoot = jsAst.getAstRoot(compiler);

    assertNull(astRoot);
  }

  @Test
  public void testGetAstRootWhenAstExists() {
    Node dummyAst = IR.script();
    when(parserRunner.parse(sourceFile, "", parserConfig, compiler.getErrorReporter(), logger_))
        .thenReturn(new ParserRunner.ParseResult(dummyAst));
    when(compiler.getDefaultErrorReporter()).thenReturn(mock(ErrorReporter.class));

    Node astRoot = jsAst.getAstRoot(compiler);

    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    Node dummyAst = IR.script();
    jsAst.root = dummyAst;
    jsAst.clearAst();

    assertNull(jsAst.root);
  }

  @Test
  public void testGetInputId() {
    InputId expectedInputId = new InputId("test.js");
    when(sourceFile.getName()).thenReturn("test.js");

    InputId actualInputId = jsAst.getInputId();

    assertNotNull(actualInputId);
    assertNotNull(actualInputId.getUserData());
    assertNotNull(actualInputId.getSourceName());
    assertEquals(expectedInputId, actualInputId);
  }

  @Test
  public void testGetSourceFile() {
    assertEquals(sourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = new SourceFile("test2.js", "test2.js", new StringSource(""));
    jsAst.setSourceFile(newSourceFile);

    assertEquals(newSourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testParseWhenNoErrors() {
    String sourceCode = "function test() {}";
    when(parserRunner.parse(sourceFile, sourceCode, parserConfig, compiler.getErrorReporter(), logger_))
        .thenReturn(new ParserRunner.ParseResult(IR.script(new TopLevel(IR.function(new Name("test")), null))));
    when(compiler.getDefaultErrorReporter()).thenReturn(mock(ErrorReporter.class));

    Node astRoot = jsAst.getAstRoot(compiler);

    assertNotNull(astRoot);
  }

  @Test
  public void testParseWhenErrorsThrown() {
    when(parserRunner.parse(sourceFile, "", parserConfig, compiler.getErrorReporter(), logger_))
        .thenThrow(new IOException());

    Node astRoot = jsAst.getAstRoot(compiler);

    assertNull(astRoot);
  }

  @Test
  public void testParseWhenProgramIsEmpty() {
    when(parserRunner.parse(sourceFile, "", parserConfig, compiler.getErrorReporter(), logger_))
        .thenReturn(new ParserRunner.ParseResult(IR.script()));

    Node astRoot = jsAst.getAstRoot(compiler);

    assertNotNull(astRoot);
  }

  @Test
  public void testParseWhenParseErrorOccurs() {
    when(parserRunner.parse(sourceFile, "", parserConfig, compiler.getErrorReporter(), logger_))
        .thenReturn(new ParserRunner.ParseResult(null));
    when(compiler.getDefaultErrorReporter()).thenReturn(mock(ErrorReporter.class));

    Node astRoot = jsAst.getAstRoot(compiler);

    assertNull(astRoot);

    ArgumentCaptor<String> errorMessageArgumentCaptor = ArgumentCaptor.forClass(String.class);
    verify(compiler.getErrorReporter()).report(errorMessageArgumentCaptor.capture());

    assertEquals("test.js:1:0 ", errorMessageArgumentCaptor.getValue());
  }

  @Test
  public void testSetOldParseTree() {
    Node oldAst = IR.script();
    jsAst.setOldParseTree("test.js", oldAst);

    assertEquals(oldAst, jsAst.getOldParseTree("test.js"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetOldParseTreeWithNullSourceFile() {
    jsAst.setOldParseTree(null, IR.script());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSetOldParseTreeWithNullAst() {
    jsAst.setOldParseTree("test.js", null);
  }
}