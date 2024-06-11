import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserConfig;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.TestSourceFile;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the JsAst class.
 */
public class JsAstTest {

  private AbstractCompiler compiler;
  private ParserConfig parserConfig;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    parserConfig = new ParserConfig();
    sourceFile = new TestSourceFile("test.js", "test code");
  }

  @Test
  public void testGetAstRoot() {
    // Given: A new JsAst instance with a SourceFile
    JsAst jsAst = new JsAst(sourceFile);

    // When: The getAstRoot method is called with a compiler
    Node astRoot = jsAst.getAstRoot(compiler);

    // Then: The astRoot should not be null
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    // Given: A new JsAst instance with a SourceFile and a root node
    JsAst jsAst = new JsAst(sourceFile);
    Node root = IR.script();
    jsAst.root = root;

    // When: The clearAst method is called
    jsAst.clearAst();

    // Then: The root should be null
    assertNotNull(jsAst.root);
  }

  @Test
  public void testGetInputId() {
    // Given: A new JsAst instance with a SourceFile
    JsAst jsAst = new JsAst(sourceFile);

    // When: The getInputId method is called
    InputId inputId = jsAst.getInputId();

    // Then: The inputId should not be null and equal to the SourceFile name
    assertNotNull(inputId);
    assertEquals(sourceFile.getName(), inputId.getName());
  }

  @Test
  public void testGetSourceFile() {
    // Given: A new JsAst instance with a SourceFile
    JsAst jsAst = new JsAst(sourceFile);

    // When: The getSourceFile method is called
    SourceFile sourceFile = jsAst.getSourceFile();

    // Then: The SourceFile should not be null and equal to the one passed in
    assertNotNull(sourceFile);
    assertEquals(jsAst.sourceFile, sourceFile);
  }

  @Test
  public void testSetSourceFile() {
    // Given: A new JsAst instance with a SourceFile and a new name
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new TestSourceFile("new_test.js", "new test code");

    // When: The setSourceFile method is called with a new SourceFile
    jsAst.setSourceFile(newSourceFile);

    // Then: The fileName and sourceFile should be updated
    assertEquals(newSourceFile.getName(), jsAst.fileName);
    assertEquals(newSourceFile, jsAst.sourceFile);
  }

  @Test
  public void testParse() throws IOException {
    // Given: A new JsAst instance with a SourceFile and a compiler
    JsAst jsAst = new JsAst(sourceFile);
    when(compiler.getParserConfig()).thenReturn(parserConfig);

    // When: The parse method is called with a compiler
    jsAst.parse(compiler);

    // Then: The root node should not be null
    assertNotNull(jsAst.root);
  }

  @Test(expected = IOException.class)
  public void testParseWithIOException() throws IOException {
    // Given: A new JsAst instance with a SourceFile and a compiler with an IOException
    JsAst jsAst = new JsAst(sourceFile);
    when(compiler.getParserConfig()).thenReturn(parserConfig);
    when(compiler.getErrorReporter()).thenThrow(new IOException());

    // When: The parse method is called with a compiler
    jsAst.parse(compiler);

    // Then: An IOException should be thrown
  }

  @Test
  public void testParseWithNullAst() {
    // Given: A new JsAst instance with a SourceFile and a compiler
    JsAst jsAst = new JsAst(sourceFile);
    when(compiler.getParserConfig()).thenReturn(parserConfig);
    when(compiler.hasHaltingErrors()).thenReturn(true);

    // When: The parse method is called with a compiler
    jsAst.parse(compiler);

    // Then: The root node should be a dummy script block
    Node root = IR.script();
    assertNotNull(jsAst.root);
    assertEquals(root, jsAst.root);
  }

  @Test
  public void testSetStaticSourceFile() {
    // Given: A new JsAst instance with a SourceFile and a root node
    JsAst jsAst = new JsAst(sourceFile);
    Node root = IR.script();
    jsAst.root = root;

    // When: The setStaticSourceFile method is called with a SourceFile
    jsAst.setStaticSourceFile(sourceFile);

    // Then: The root node's static source file should be set to the SourceFile
    assertEquals(sourceFile, jsAst.root.getStaticSourceFile());
  }
}