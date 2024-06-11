import static org.junit.Assert.*;
import static com.google.javascript.jscomp.testing.CodeSnippetTest.factory;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.CodeSnippetTest;
import com.google.javascript.rhino.Node;

public class JsAstTest {
  private AbstractCompiler compiler;
  private SourceFile sourceFile;
  private JsAst jsAst;
  private Node root;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {
      @Override
      public Node parseTreeForModule(SourceFile inputFile, CompilerOptions options) {
        return null; // Not used in this test
      }
    };
    sourceFile = factory.getSourceFile("Test.js", "test code");
  }

  @Test
  public void testGetAstRoot() {
    jsAst = new JsAst(sourceFile);
    root = jsAst.getAstRoot(compiler);
    assertNotNull(root);
  }

  @Test
  public void testClearAst() {
    jsAst = new JsAst(sourceFile);
    jsAst.getAstRoot(compiler);
    jsAst.clearAst();
    assertNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testGetInputId() {
    jsAst = new JsAst(sourceFile);
    assertEquals(sourceFile.getName(), jsAst.getInputId().getName());
  }

  @Test
  public void testGetSourceFile() {
    jsAst = new JsAst(sourceFile);
    assertEquals(sourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testSetSourceFile() {
    SourceFile newSourceFile = factory.getSourceFile("NewFile.js", "new test code");
    jsAst = new JsAst(sourceFile);
    jsAst.setSourceFile(newSourceFile);
    assertEquals(newSourceFile, jsAst.getSourceFile());
  }

  @Test
  public void testParse() {
    jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test(expected = AssertionError.class)
  public void testIOException() {
    compiler.setIOExceptionThrower(true);
    jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
  }

  @Test
  public void testParseError() {
    compiler.setDefaultErrorReporter(new ExceptionErrorReporter());
    jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));
  }

  @Test
  public void testParseNullAst() {
    compiler.setDefaultErrorReporter(new ExceptionErrorReporter());
    jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertNotNull(jsAst.getAstRoot(compiler));

    // Set the parse error reporter to report parse errors as failures.
    compiler.setDefaultErrorReporter(new FailureErrorReporter());
    jsAst = new JsAst(sourceFile);
    jsAst.parse(compiler);
    assertNull(jsAst.getAstRoot(compiler));
  }
}

class ExceptionErrorReporter implements AbstractCompiler.ErrorReporter {
  @Override
  public void report(JSError error) {
    throw new RuntimeException(error.getMessage());
  }
}

class FailureErrorReporter implements AbstractCompiler.ErrorReporter {
  @Override
  public void report(JSError error) {
    // Do nothing
  }
}