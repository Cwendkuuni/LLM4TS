import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceFile;

public class SourceFileTest {

  @org.junit.Test
  public void testGetLine() {
    SourceFile sourceFile = new SourceFile("test.js");
    String expectedLine = "function testFunction() {\n" +
                          "  var x = 1;\n" +
                          "}";
    try {
      sourceFile.setCode(expectedLine);
    } catch (IOException e) {
      fail("Failed to set code for test");
    }

    String actualLine = sourceFile.getLine(3);
    assertEquals("var x = 1;", actualLine);
  }

  @org.junit.Test
  public void testGetLineInvalidLineNumber() {
    SourceFile sourceFile = new SourceFile("test.js");
    try {
      sourceFile.getLine(0);
      fail("Expected IOException");
    } catch (IOException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testGetRegion() {
    SourceFile sourceFile = new SourceFile("test.js");
    String expectedCode = "function testFunction() {\n" +
                          "  var x = 1;\n" +
                          "  var y = 2;\n" +
                          "}";
    try {
      sourceFile.setCode(expectedCode);
    } catch (IOException e) {
      fail("Failed to set code for test");
    }

    Region region = sourceFile.getRegion(6);
    String actualRegion = "";
    if (region != null) {
      actualRegion = region.getCode();
    }
    assertEquals("  var y = 2;\n", actualRegion);
  }

  @org.junit.Test
  public void testGetRegionInvalidLineNumber() {
    SourceFile sourceFile = new SourceFile("test.js");
    Region region = sourceFile.getRegion(0);
    assertEquals(null, region);
  }

  @org.junit.Test
  public void testFindLineOffsets() {
    SourceFile sourceFile = new SourceFile("test.js");
    String code = "function testFunction() {\n" +
                  "  var x = 1;\n" +
                  "  var y = 2;\n" +
                  "}";
    try {
      sourceFile.setCode(code);
    } catch (IOException e) {
      fail("Failed to set code for test");
    }

    int[] lineOffsets = sourceFile.getLineOffsets();
    assertEquals(3, lineOffsets.length);
    assertEquals(1, lineOffsets[1]);
    assertEquals(14, lineOffsets[2]);
  }

  @org.junit.Test
  public void testGetLineOffsetInvalidLineNumber() {
    SourceFile sourceFile = new SourceFile("test.js");
    int[] lineOffsets = sourceFile.getLineOffsets();
    try {
      sourceFile.getLineOffset(0);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testGetNumLines() {
    SourceFile sourceFile = new SourceFile("test.js");
    String code = "function testFunction() {\n" +
                  "  var x = 1;\n" +
                  "}";
    try {
      sourceFile.setCode(code);
    } catch (IOException e) {
      fail("Failed to set code for test");
    }

    int numLines = sourceFile.getNumLines();
    assertEquals(2, numLines);
  }
}