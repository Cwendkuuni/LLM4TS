import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.ParserConfig;
import org.junit.Test;

public class JsAstTest {

  @Test
  public void testGetAstRoot() {
    // ... (same as before)
  }

  @Test
  public void testGetNullAbstractCompiler() {
    // ... (same as before)
  }

  @Test
  public void testGetEmptySourceCode() {
    // ... (same as before)
  }

  @Test(expected = IOException.class)
  public void testParseMalformedCode() {
    // ... (same as before)
  }

  @Test
  public void testClearAst() {
    // Arrange
    SourceFile sourceFile = new SourceFile("test.js");
    JsAst jsAst = new JsAst(sourceFile);

    // Act
    jsAst.clearAst();

    // Assert
    assertNull(jsAst.getAstRoot(null));
  }

  @Test
  public void testSetSourceFile() {
    // Arrange
    SourceFile oldSourceFile = new SourceFile("oldTest.js");
    SourceFile newSourceFile = new SourceFile("newTest.js");
    JsAst jsAst = new JsAst(oldSourceFile);

    // Act
    jsAst.setSourceFile(newSourceFile);

    // Assert
    assertEquals(newSourceFile.getName(), jsAst.getSourceFile().getName());
  }
}