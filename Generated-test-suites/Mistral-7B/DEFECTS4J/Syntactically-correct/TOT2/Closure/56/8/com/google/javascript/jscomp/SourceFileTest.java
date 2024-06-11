import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.google.common.io.Charsets;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.jstype.StaticSourceFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Test class for SourceFile.
 */
public class SourceFileTest {

  /**
   * Test constructor with valid fileName.
   */
  @org.junit.Test
  public void testSourceFileConstructor() {
    String fileName = "testSourceFile.java";
    SourceFile sourceFile = new SourceFile(fileName);
    assertEquals(fileName, sourceFile.getName());
  }

  /**
   * Test getLine method with valid line number.
   */
  @org.junit.Test
  public void testGetLineValidLineNumber() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sourceFile = new Preloaded("testSourceFile.java", code);
    String line1 = sourceFile.getLine(1);
    assertEquals("line1", line1);
  }

  /**
   * Test getLine method with invalid line number.
   */
  @org.junit.Test
  public void testGetLineInvalidLineNumber() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sourceFile = new Preloaded("testSourceFile.java", code);
    assertNull(sourceFile.getLine(4));
  }

  /**
   * Test getRegion method with valid line number.
   */
  @org.junit.Test
  public void testGetRegionValidLineNumber() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sourceFile = new Preloaded("testSourceFile.java", code);
    Region region = sourceFile.getRegion(2);
    assertEquals(2, region.getStartLineNumber());
    assertEquals(2, region.getEndLineNumber());
    assertEquals("line2\nline3", region.getSourceCode());
  }

  /**
   * Test getRegion method with invalid line number.
   */
  @org.junit.Test
  public void testGetRegionInvalidLineNumber() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sourceFile = new Preloaded("testSourceFile.java", code);
    Region region = sourceFile.getRegion(4);
    assertNull(region);
  }

  /**
   * Test fromFile method with valid file path.
   */
  @org.junit.Test
  public void testFromFileValidFilePath() throws IOException {
    File file = new File("testSourceFile.java");
    String expectedCode =
        "public class Test {\n" +
        "  public static void main(String[] args) {\n" +
        "  }\n" +
        "}";
    SourceFile sourceFile = SourceFile.fromFile(file);
    assertEquals("testSourceFile.java", sourceFile.getName());
    assertEquals(expectedCode, sourceFile.getCode());
  }

  /**
   * Test fromFile method with invalid file path.
   */
  @org.junit.Test
  public void testFromFileInvalidFilePath() {
    File file = new File("nonExistentFile.java");
    SourceFile sourceFile = SourceFile.fromFile(file);
    assertNull(sourceFile);
  }

  /**
   * Test fromFile method with null file path.
   */
  @org.junit.Test
  public void testFromFileNullFilePath() {
    SourceFile sourceFile = SourceFile.fromFile(null);
    assertNull(sourceFile);
  }

  /**
   * Test fromCode method with valid code.
   */
  @org.junit.Test
  public void testFromCodeValidCode() throws IOException {
    String code = "public class Test {\n" +
        "  public static void main(String[] args) {\n" +
        "  }\n" +
        "}";
    SourceFile sourceFile = SourceFile.fromCode("testSourceFile.java", code);
    assertEquals("testSourceFile.java", sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
  }

  /**
   * Test fromCode method with null code.
   */
  @org.junit.Test
  public void testFromCodeNullCode() {
    SourceFile sourceFile = SourceFile.fromCode("testSourceFile.java", null);
    assertNull(sourceFile);
  }

  /**
   * Test getLineOffset method with valid line number.
   */
  @org.junit.Test
  public void testGetLineOffsetValidLineNumber() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sourceFile = new Preloaded("testSourceFile.java", code);
    int lineNumber = 2;
    int expectedLineOffset = 11;
    int actualLineOffset = sourceFile.getLineOffset(lineNumber);
    assertEquals(expectedLineOffset, actualLineOffset);
  }

  /**
   * Test getLineOffset method with invalid line number.
   */
  @org.junit.Test
  public void testGetLineOffsetInvalidLineNumber() throws IOException {
    String code = "line1\nline2\nline3";
    SourceFile sourceFile = new Preloaded("testSourceFile.java", code);
    int lineNumber = 4;
    assertThrows(IllegalArgumentException.class, () -> {
      sourceFile.getLineOffset(lineNumber);
    });
  }

  /**
   * Test hasSourceInMemory method with in-memory source.
   */
  @org.junit.Test
  public void testHasSourceInMemoryWithInMemorySource() throws IOException {
    String code = "public class Test {\n" +
        "  public static void main(String[] args) {\n" +
        "  }\n" +
        "}";
    SourceFile sourceFile = new Preloaded("testSourceFile.java", code);
    assertTrue(sourceFile.hasSourceInMemory());
  }

  /**
   * Test hasSourceInMemory method with non-in-memory source.
   */
  @org.junit.Test
  public void testHasSourceInMemoryWithNonInMemorySource() throws IOException {
    File file = new File("testSourceFile.java");
    SourceFile sourceFile = SourceFile.fromFile(file);
    assertFalse(sourceFile.hasSourceInMemory());
  }

  /**
   * Test toString method.
   */
  @org.junit.Test
  public void testToString() {
    String fileName = "testSourceFile.java";
    SourceFile sourceFile = new SourceFile(fileName);
    assertEquals(fileName, sourceFile.toString());
  }
}