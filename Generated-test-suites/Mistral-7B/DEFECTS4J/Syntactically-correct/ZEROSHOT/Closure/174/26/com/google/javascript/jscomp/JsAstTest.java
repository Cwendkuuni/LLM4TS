import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 * JsAst test file.
 */
public class JsAstTest {

  private AbstractCompiler compiler;
  private ParserConfig parserConfig;
  private SourceFile sourceFile;
  private JsAst jsAst;

  @Before
  public void setUp() {
    parserConfig = mock(ParserConfig.class);
    compiler = mock(AbstractCompiler.class);
    sourceFile = mock(SourceFile.class);
    when(sourceFile.getName()).thenReturn("test.js");
    when(sourceFile.getCode()).thenAnswer(new Answer<String>() {
      @Override
      public String answer(InvocationOnMock invocation) throws Throwable {
        return "test code";
      }
    });
    when(sourceFile.getCodeBytes()).thenAnswer(new Answer<byte[]>() {
      @Override
      public byte[] answer(InvocationOnMock invocation) throws Throwable {
        return new byte[0];
      }
    });
    when(sourceFile.clearCachedSource()).thenAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        return null;
      }
    });

    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    Node expectedAstRoot = IR.script();
    when(compiler.getParserConfig()).thenReturn(parserConfig);
    when(compiler.getDefaultErrorReporter()).thenReturn(mock(ErrorReporter.class));
    when(compiler.getLogger()).thenReturn(mock(Logger.class));

    jsAst.parse(compiler);
    Node actualAstRoot = jsAst.getAstRoot(compiler);
    assertNotNull(actualAstRoot);
    assertNull(actualAstRoot.getBody());
    assertNotNull(actualAstRoot.getStaticSourceFile());
    assertNotNull(actualAstRoot.getSourceName());
    assertNull(actualAstRoot.getSourcePositionStart());
    assertNull(actualAstRoot.getSourcePositionEnd());
    assertNotNull(actualAstRoot.getInputId());
    assertNotNull(actualAstRoot.getSourceIndex());
    assertNull(actualAstRoot.getChild(0));

    Preconditions.checkState(expectedAstRoot.equals(actualAstRoot));
  }

  @Test
  public void testClearAst() {
    Node expectedAstRoot = IR.script();
    jsAst.parse(compiler);
    jsAst.getAstRoot(compiler);
    jsAst.clearAst();

    Node actualAstRoot = jsAst.getAstRoot(compiler);
    assertNull(actualAstRoot);
  }

  @Test
  public void testGetInputId() {
    InputId expectedInputId = new InputId("test.js");

    assertNotNull(jsAst.getInputId());
    Preconditions.checkState(expectedInputId.equals(jsAst.getInputId()));
  }

  @Test
  public void testGetSourceFile() {
    assertNotNull(jsAst.getSourceFile());
    Preconditions.checkState(sourceFile.equals(jsAst.getSourceFile()));
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = mock(SourceFile.class);
    when(newSourceFile.getName()).thenReturn("newTest.js");

    jsAst.setSourceFile(newSourceFile);
    assertNull(jsAst.getInputId());
    Preconditions.checkState(newSourceFile.equals(jsAst.getSourceFile()));
  }
}