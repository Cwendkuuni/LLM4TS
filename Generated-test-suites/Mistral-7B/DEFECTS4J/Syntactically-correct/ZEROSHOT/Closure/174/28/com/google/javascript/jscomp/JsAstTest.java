import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserRunner;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class JsAstTest {

  private AbstractCompiler compiler;
  private ParserConfig parserConfig;
  private ParserRunner parserRunner;
  private SourceFile sourceFile;

  @Before
  public void setup() {
    parserConfig = mock(ParserConfig.class);
    parserRunner = mock(ParserRunner.class);
    compiler = mock(AbstractCompiler.class);

    when(parserRunner.parse(sourceFile, sourceFile.getCode(), parserConfig,
        compiler.getErrorReporter(), logger_))
        .thenReturn(new ParserRunner.ParseResult(IR.script(), null));

    sourceFile = new SourceFile("test.js", "test.js");
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
    jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  @Test
  public void testGetInputId() {
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertEquals(new InputId("test.js"), inputId);
  }

  @Test
  public void testGetSourceFile() {
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile sourceFile = jsAst.getSourceFile();
    assertEquals(sourceFile, jsAst.sourceFile);
  }

  @Test
  public void testSetSourceFile() {
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("newTest.js", "newTest.js");
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.sourceFile);
    assertEquals("newTest.js", jsAst.getInputId().getName());
  }

  @Test(expected = IllegalStateException.class)
  public void testSetSourceFileWithDifferentFileName() {
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("test2.js", "test.js");
    jsAst.setSourceFile(newSourceFile);
  }

  @Test
  public void testParse() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testParseWithIOException() {
    when(parserRunner.parse(sourceFile, sourceFile.getCode(), parserConfig,
        compiler.getErrorReporter(), logger_))
        .thenThrow(new IOException("Test IOException"));

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  @Test
  public void testParseWithErrors() {
    when(parserRunner.parse(sourceFile, sourceFile.getCode(), parserConfig,
        compiler.getErrorReporter(), logger_))
        .thenReturn(new ParserRunner.ParseResult(null, new Node[1]));

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  @Test
  public void testSetOldParseTree() {
    when(parserRunner.parse(sourceFile, sourceFile.getCode(), parserConfig,
        compiler.getErrorReporter(), logger_))
        .thenReturn(new ParserRunner.ParseResult(IR.script(), IR.script()));

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node oldParseTree = IR.script();
    jsAst.setOldParseTree(sourceFile.getName(), oldParseTree);
    Node expectedOldParseTree = jsAst.getOldParseTree(sourceFile.getName());
    assertEquals(oldParseTree, expectedOldParseTree);
  }

  @Test
  public void testGetOldParseTreeWithNullOldParseTree() {
    when(parserRunner.parse(sourceFile, sourceFile.getCode(), parserConfig,
        compiler.getErrorReporter(), logger_))
        .thenReturn(new ParserRunner.ParseResult(IR.script(), null));

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node oldParseTree = null;
    jsAst.setOldParseTree(sourceFile.getName(), oldParseTree);
    Node expectedOldParseTree = jsAst.getOldParseTree(sourceFile.getName());
    assertNull(expectedOldParseTree);
  }

  @Test
  public void testSetOldParseTreeWithDifferentFileName() {
    when(parserRunner.parse(sourceFile, sourceFile.getCode(), parserConfig,
        compiler.getErrorReporter(), logger_))
        .thenReturn(new ParserRunner.ParseResult(IR.script(), IR.script()));

    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    Node oldParseTree = IR.script();
    jsAst.setOldParseTree("differentFileName.js", oldParseTree);
    Node expectedOldParseTree = jsAst.getOldParseTree(sourceFile.getName());
    assertNull(expectedOldParseTree);
  }

  private void assertEquals(Node expected, Node actual) {
    ArgumentCaptor<Node> nodeCaptor = ArgumentCaptor.forClass(Node.class);
    when(compiler.setOldParseTree(eq(sourceFile.getName()), nodeCaptor.capture()))
        .thenReturn(null);
    assertEquals(expected, nodeCaptor.getValue());
  }

  private static <T> T eq(T expected) {
    return (T) expected;
  }
}