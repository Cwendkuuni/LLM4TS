import static org.junit.Assert.*;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parser.ParserConfig;
import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

public class JsAstTest {
  private AbstractCompiler compiler;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    sourceFile = new SourceFile("test.js", "test content");
  }

  @Test
  public void testGetAstRoot() {
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test(expected = NullPointerException.class)
  public void testGetAstRootNullSourceFile() {
    JsAst jsAst = new JsAst(null);
    jsAst.getAstRoot(compiler);
  }

  @Test
  public void testClearAst() {
    JsAst jsAst = new JsAst(sourceFile);
    Node astRoot = jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    Node newAstRoot = jsAst.getAstRoot(compiler);
    assertNull(newAstRoot);
  }

  @Test
  public void testGetInputId() {
    JsAst jsAst = new JsAst(sourceFile);
    InputId inputId = jsAst.getInputId();
    assertEquals("test.js", inputId.getName());
  }

  @Test
  public void testGetSourceFile() {
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile sourceFile = jsAst.getSourceFile();
    assertEquals("test.js", sourceFile.getName());
  }

  @Test
  public void testSetSourceFile() {
    JsAst jsAst = new JsAst(sourceFile);
    SourceFile newSourceFile = new SourceFile("newTest.js", "new test content");
    jsAst.setSourceFile(newSourceFile);
    SourceFile sourceFile = jsAst.getSourceFile();
    assertEquals("newTest.js", sourceFile.getName());
  }

  @Test
  public void testParseNullCompiler() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(null);
  }

  @Test
  public void testParseNullSourceFile() {
    JsAst jsAst = new JsAst(null);
    jsAst.parse(compiler);
  }

  @Test
  public void testParseEmptyFile() {
    JsAst jsAst = new JsAst(new SourceFile("emptyFile.js", ""));
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertTrue(astRoot instanceof Node);
    assertTrue(astRoot.isScript());
  }

  @Test
  public void testParseValidFile() {
    String validJsCode = "function test() { return 1; }";
    SourceFile validSourceFile = new SourceFile("validFile.js", validJsCode);
    JsAst jsAst = new JsAst(validSourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertTrue(astRoot instanceof Node);
    assertTrue(astRoot.isScript());
    Node functionDeclaration = astRoot.getFirstChild();
    assertTrue(functionDeclaration instanceof Node);
    assertTrue(functionDeclaration.isFunctionDeclaration());
  }

  @Test
  public void testParseWithErrors() {
    String invalidJsCode = "function test() { return }"; // missing return value
    SourceFile invalidSourceFile = new SourceFile("invalidFile.js", invalidJsCode);
    JsAst jsAst = new JsAst(invalidSourceFile);
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertTrue(astRoot instanceof Node);
    assertTrue(astRoot.isScript());
    // There should be an error reported, so the AST root should be null or empty.
    Node functionDeclaration = astRoot.getFirstChild();
    assertNull(functionDeclaration);
  }

  @Test
  public void testParseWithOldParseTree() {
    String validJsCode = "function test() { return 1; }";
    SourceFile validSourceFile = new SourceFile("validFile.js", validJsCode);
    JsAst jsAst = new JsAst(validSourceFile);
    Node oldAstRoot = IR.script();
    jsAst.setOldParseTree(validSourceFile.getName(), oldAstRoot);
    jsAst.parse(compiler);
    Node newAstRoot = jsAst.getAstRoot(compiler);
    // Since the old parse tree was set, the new parse tree should be the old one.
    assertEquals(oldAstRoot, newAstRoot);
  }

  @Test
  public void testParseWithParserConfig() {
    String validJsCode = "function test() { return 1; }";
    ParserConfig parserConfig = new ParserConfig();
    SourceFile validSourceFile = new SourceFile("validFile.js", validJsCode);
    JsAst jsAst = new JsAst(validSourceFile);
    jsAst.parse(compiler, parserConfig);
    Node astRoot = jsAst.getAstRoot(compiler);
    // The parserConfig should be used during parsing.
    assertTrue(parserConfig.isStrictMode());
  }

  @Test
  public void testParseWithErrorReporter() {
    String errorReportedJsCode = "function test() { return }"; // missing return value
    SourceFile errorReportedSourceFile = new SourceFile("errorReportedFile.js", errorReportedJsCode);
    JsAst jsAst = new JsAst(errorReportedSourceFile);
    CompilerErrorReporter errorReporter = new CompilerErrorReporter();
    jsAst.parse(compiler, errorReporter);
    // There should be an error reported, so the AST root should be null or empty.
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
    // Make sure the error was reported.
    assertEquals(1, errorReporter.getErrorCount());
  }

  @Test
  public void testParseWithLogger() {
    String validJsCode = "function test() { return 1; }";
    SourceFile validSourceFile = new SourceFile("validFile.js", validJsCode);
    JsAst jsAst = new JsAst(validSourceFile);
    Logger logger = Logger.getLogger("TestLogger");
    jsAst.parse(compiler, logger);
    // There should be no logger messages since the code is valid.
    assertTrue(logger.getLevel().intValue() >= Level.SEVERE.intValue());
    assertTrue(logger.getMessages().isEmpty());
  }
}