import static org.junit.Assert.*;
import static com.google.javascript.jscomp.parser.testtype.SourceFileType.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.ParserRunner;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserConfig;
import com.google.javascript.rhino.IR;
import com.google.javascript.rhino.InputId;
import com.google.javascript.rhino.Node;

public class JsAstTest {
  private AbstractCompiler compiler;
  private SourceFile sourceFile;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {
      @Override
      public Node compile(Node root, CompilerOptions options) throws IOException {
        fail("Should not call compile method");
        return null;
      }

      @Override
      public void compileAll(Node root, CompilerOptions options, boolean useModuleMode)
          throws IOException {
        fail("Should not call compileAll method");
      }

      @Override
      public void checkSyntax(Node root, CompilerOptions options, boolean useModuleMode)
          throws IOException {
        fail("Should not call checkSyntax method");
      }

      @Override
      public void process(Node root, CompilerOptions options) throws IOException {
        fail("Should not call process method");
      }

      @Override
      public boolean hasHaltingErrors() {
        return false;
      }

      @Override
      public void report(JSError error) {
        fail("Should not call report method");
      }

      @Override
      public ParserConfig getParserConfig() {
        return new ParserConfig();
      }

      @Override
      public void setOldParseTree(String name, Node node) {
        // Do nothing
      }
    };

    sourceFile = new SourceFile("test.js", TEST_CASE);
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
    Node astRoot = jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParse() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
  }

  @Test(expected = IOException.class)
  public void testParseIOException() {
    JsAst jsAst = new JsAst(sourceFile);
    ParserRunner.ParseResult result = ParserRunner.parse(sourceFile, sourceFile.getCode(),
        compiler.getParserConfig(),
        compiler.getDefaultErrorReporter(),
        Logger.getLogger(JsAst.class.getName()));
    jsAst.parse(compiler);
  }

  @Test
  public void testParseNullAst() {
    JsAst jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testSetSourceFile() {
    InputId inputId1 = new InputId("test1.js");
    SourceFile sourceFile1 = new SourceFile("test1.js", TEST_CASE);
    JsAst jsAst1 = new JsAst(sourceFile1);

    InputId inputId2 = new InputId("test2.js");
    SourceFile sourceFile2 = new SourceFile("test2.js", TEST_CASE);
    JsAst jsAst2 = new JsAst(sourceFile2);

    jsAst1.setSourceFile(sourceFile2);
    assertEquals(inputId2, jsAst1.getInputId());
    assertEquals(sourceFile2, jsAst1.getSourceFile());
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
    assertEquals(sourceFile, jsAst.getSourceFile());
  }
}