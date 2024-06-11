import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;
import com.google.javascript.jscomp.SourceFile.OnDisk;
import com.google.javascript.jscomp.SourceFile.Preloaded;
import com.google.javascript.jscomp.SourceFile.Generated;

public class SourceFileTest {

  @Test
  public void testPreloadedSourceFile() throws IOException {
    String expectedCode = "function foo() {}";
    String fileName = "TestFile.js";
    Preloaded preloadedSourceFile = new Preloaded(fileName, expectedCode);

    String actualCode = preloadedSourceFile.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testOnDiskSourceFile() throws IOException {
    String expectedCode = "function foo() {}";
    String fileName = "TestFile.js";
    File file = new File(fileName);
    OnDisk onDiskSourceFile = new OnDisk(file);

    String actualCode = onDiskSourceFile.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testFromGeneratorSourceFile() throws IOException {
    String expectedCode = "function foo() {}";
    String fileName = "TestFile.js";
    Generator generator = () -> expectedCode;
    Generated generatedSourceFile = new Generated(fileName, generator);

    String actualCode = generatedSourceFile.getCode();
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testFindLineOffset() throws IOException {
    String code = "function foo() {\n" +
                  "  // Some comments\n" +
                  "  var x = 1;\n" +
                  "  return x;\n" +
                  "}";
    SourceFile sourceFile = new Preloaded("TestFile.js", code);

    int lineNumber = 3;
    int offset = sourceFile.getLineOffset(lineNumber);
    int expectedOffset = 27;

    assertEquals(expectedOffset, offset);
  }

  @Test
  public void testGetRegion() throws IOException {
    String code = "function foo() {\n" +
                  "  // Some comments\n" +
                  "  var x = 1;\n" +
                  "  return x;\n" +
                  "}";
    SourceFile sourceFile = new Preloaded("TestFile.js", code);

    int lineNumber = 2;
    Region expectedRegion = new Region(1, 2, "  var x = 1;");

    Region region = sourceFile.getRegion(lineNumber);
    assertEquals(expectedRegion.getEndLine(), region.getEndLine());
    assertEquals(expectedRegion.getStartLine(), region.getStartLine());
    assertEquals(expectedRegion.getCode(), region.getCode());
  }

  @Test
  public void testEmptyFile() throws IOException {
    String fileName = "TestFile.js";
    SourceFile sourceFile = new OnDisk(fileName);

    String actualCode = sourceFile.getCode();
    assertNull(actualCode);
  }

  @Test
  public void testIsExtern() {
    SourceFile sourceFile = new Preloaded("TestFile.js", "function foo() {}");
    assertFalse(sourceFile.isExtern());

    sourceFile = new Preloaded("TestFile.js", "/** @extern */ function foo() {}");
    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String code = "function foo() {}";
    String fileName = "TestFile.js";
    SourceFile sourceFile = new Preloaded(fileName, code);

    String actualCode = sourceFile.getCode();
    assertEquals(code, actualCode);

    sourceFile.clearCachedSource();

    actualCode = sourceFile.getCode();
    assertNull(actualCode);
  }

  @Test
  public void testSetOriginalPath() throws IOException {
    String code = "function foo() {}";
    String fileName = "TestFile.js";
    String originalPath = "/path/to/testFile.js";
    SourceFile sourceFile = new Preloaded(fileName, code);

    sourceFile.setOriginalPath(originalPath);

    assertEquals(originalPath, sourceFile.getOriginalPath());
  }
}