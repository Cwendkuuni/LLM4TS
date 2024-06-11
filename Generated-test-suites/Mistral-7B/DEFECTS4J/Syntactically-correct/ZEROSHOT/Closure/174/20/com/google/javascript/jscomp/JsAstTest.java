import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.InputId;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.parsing.ParserConfig;
import com.google.javascript.rhino.Node;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class JsAstTest {
  private AbstractCompiler compiler;
  private SourceFile sourceFile;
  private JsAst jsAst;
  private ParserConfig parserConfig;

  @Before
  public void setUp() {
    parserConfig = Mockito.mock(ParserConfig.class);
    compiler = Mockito.spy(new AbstractCompiler() {
      @Override
      public ParserConfig getParserConfig() {
        return parserConfig;
      }

      @Override
      public void report(JSError error) {
        // Do nothing
      }

      @Override
      public void prepareAst(Node node) {
        // Do nothing
      }

      @Override
      public Node getOldParseTree(String name) {
        return null;
      }
    });

    sourceFile = Mockito.mock(SourceFile.class);
    jsAst = new JsAst(sourceFile);
  }

  @Test
  public void testGetAstRoot() {
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }

  @Test
  public void testClearAst() {
    jsAst.clearAst();
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNull(astRoot);
  }

  @Test
  public void testGetInputId() {
    InputId inputId = jsAst.getInputId();
    assertTrue(inputId.getName().equals(sourceFile.getName()));
  }

  @Test
  public void testGetSourceFile() {
    assertTrue(jsAst.getSourceFile() == sourceFile);
  }

  @Test
  public void testSetSourceFile() {
    String newFileName = "newFileName.js";
    SourceFile newSourceFile = Mockito.mock(SourceFile.class);
    Mockito.when(newSourceFile.getName()).thenReturn(newFileName);
    jsAst.setSourceFile(newSourceFile);
    InputId newInputId = jsAst.getInputId();
    assertTrue(newInputId.getName().equals(newFileName));
  }

  @Test
  public void testParse() {
    jsAst.parse(compiler);
    Node astRoot = jsAst.getAstRoot(compiler);
    assertNotNull(astRoot);
  }
}