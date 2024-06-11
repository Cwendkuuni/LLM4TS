import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeFilter.*;
import static com.google.javascript.jscomp.parser.js.JsParserTestUtil.*;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.config.ParserConfig;
import com.google.javascript.jscomp.parsing.parser.JsParser;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceName;

import org.junit.Before;
import org.junit.Test;

public class JsAstTest {

  private AbstractCompiler compiler;
  private ParserConfig parserConfig;
  private JsParser jsParser;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    parserConfig = new ParserConfig();
    jsParser = new JsParser(parserConfig, compiler.getErrorReporter(), null);
  }

  @Test
  public void testGetAstRoot() {
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);

    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.getAstRoot(compiler);

    jsAst.clearAst();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  @Test
  public void testGetInputId() {
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);

    InputId inputId = jsAst.getInputId();
    assertEquals(new InputId("test.js"), inputId);
  }

  @Test
  public void testGetSourceFile() {
    SourceFile sourceFile = new SourceFile("test.js", "test code");
    JsAst jsAst = new JsAst(sourceFile);

    SourceFile sourceFileFromJsAst = jsAst.getSourceFile();
    assertEquals(sourceFile, sourceFileFromJsAst);
  }

  @Test
  public void testSetSourceFile() {
    SourceFile sourceFile1 = new SourceFile("test1.js", "test code 1");
    SourceFile sourceFile2 = new SourceFile("test2.js", "test code 2");
    JsAst jsAst = new JsAst(sourceFile1);

    jsAst.setSourceFile(sourceFile2);
    SourceFile sourceFileFromJsAst = jsAst.getSourceFile();
    assertEquals(sourceFile2, sourceFileFromJsAst);
  }

  @Test(expected = IllegalStateException.class)
  public void testSetSourceFileWithDifferentFileName() {
    SourceFile sourceFile1 = new SourceFile("test1.js", "test code 1");
    SourceFile sourceFile2 = new SourceFile("test2.js", "test code 2");
    JsAst jsAst = new JsAst(sourceFile1);

    jsAst.setSourceFile(sourceFile2);
    SourceFile sourceFileFromJsAst = jsAst.getSourceFile();
    fail("Should throw an exception");
  }

  @Test
  public void testParse() {
    String sourceCode = "function foo() { return 42; }";
    SourceFile sourceFile = new SourceFile("test.js", sourceCode);
    JsAst jsAst = new JsAst(sourceFile);

    AbstractCompiler compiler = new AbstractCompiler();
    Node astRoot = jsAst.getAstRoot(compiler);
    Function<Node, Boolean> isFunctionDeclaration =
        Functions.forMap(isFunctionDeclarationNode, Boolean.TRUE);

    Node functionNode = findNodeWithDescendants(astRoot, isFunctionDeclaration);
    assertNotNull(functionNode);
    assertEquals("foo", functionNode.getIdent().getName());

    Node returnNode = findNodeWithDescendants(functionNode, isReturnStatement);
    assertNotNull(returnNode);
    assertEquals(NumberLiteral.make(42), returnNode.getFirstChild());
  }

  @Test
  public void testParseWithHaltingErrors() {
    String sourceCode = "function foo() { throw new Error(); }";
    SourceFile sourceFile = new SourceFile("test.js", sourceCode);
    JsAst jsAst = new JsAst(sourceFile);

    AbstractCompiler compiler = new AbstractCompiler();
    compiler.setCompilerOptions(new CompilerOptions());
    Node astRoot = jsAst.getAstRoot(compiler);

    // There was a parse error, so use a dummy block.
    assertNotNull(astRoot);
    assertEquals(IR.script(), astRoot);

    // Ensure there is an error reported.
    assertEquals(1, compiler.getDiagnostics().size());
    assertEquals(JSError.ErrorCode.THROW_ERROR,
        compiler.getDiagnostics().get(0).getCode());
  }
}