import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ParserRunner;
import com.google.javascript.rhino.SourceName;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class JsAstTest {
  private static final String TEST_FILE_NAME = "TestSourceFile.javascript";
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
    sourceFile = mock(SourceFile.class);

    when(sourceFile.getName()).thenReturn(TEST_FILE_NAME);
    when(sourceFile.getCode()).thenReturn("test code".getBytes());
    when(sourceFile.clearCachedSource()).thenAnswer(new Answer<Void>() {
      @Override
      public Void answer(InvocationOnMock invocation) throws Throwable {
        return null;
      }
    });

    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRootNull() {
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetAstRootNotNull() {
    Node expectedRoot = IR.script();
    when(parserRunner.parse(sourceFile, sourceFile.getCode(), parserConfig, compiler.getErrorReporter(), logger_))
        .thenReturn(ParserRunner.ParseResult.withAst(expectedRoot));

    ArgumentCaptor<AbstractCompiler> compilerArgumentCaptor = ArgumentCaptor.forClass(AbstractCompiler.class);
    when(parserRunner.parse(sourceFile, sourceFile.getCode(), parserConfig, compiler.getErrorReporter(), logger_))
        .thenAnswer(new Answer<ParserRunner.ParseResult>() {
          @Override
          public ParserRunner.ParseResult answer(InvocationOnMock invocation) throws IOException {
            ParserRunner.ParseResult result = new ParserRunner.ParseResult();
            result.ast = expectedRoot;
            return result;
          }
        });

    Node actualRoot = jsAst.getAstRoot(compiler);

    assertNotNull(actualRoot);
    assertNotNull(actualRoot.getStaticSourceFile());
    assertNotNull(actualRoot.getStaticSourceName());
    assertNotNull(actualRoot.getStaticInputId());

    Preconditions.checkArgument(actualRoot.getStaticSourceFile().getName().equals(TEST_FILE_NAME),
        "Source files don't match.");

    Preconditions.checkArgument(actualRoot.getStaticSourceName().getSourceName().equals(TEST_FILE_NAME),
        "Source names don't match.");

    Preconditions.checkArgument(actualRoot.getStaticInputId().getName().equals(TEST_FILE_NAME),
        "InputIds don't match.");

    Preconditions.checkArgument(expectedRoot.equals(actualRoot), "Root nodes don't match.");
  }

  @Test
  public void testClearAst() {
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    InputId expectedInputId = new InputId(TEST_FILE_NAME);
    when(sourceFile.getInputId()).thenReturn(expectedInputId);

    InputId actualInputId = jsAst.getInputId();

    assertNotNull(actualInputId);
    Preconditions.checkArgument(expectedInputId.equals(actualInputId), "InputIds don't match.");
  }

  @Test
  public void testGetSourceFile() {
    assertNotNull(jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = mock(SourceFile.class);

    jsAst.setSourceFile(newSourceFile);
    Preconditions.checkState(jsAst.getSourceFile().equals(newSourceFile), "Source files don't match.");
  }
}