import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceFile;

public class SourceFileTest {

  @org.junit.Test
  public void testGetLine() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() {\n" +
            "  var x = 1;\n" +
            "  return x;\n" +
            "}";
    try {
      sourceFile.setCode(code);
    } catch (IOException e) {
      e.printStackTrace();
    }

    String expectedLine = "  return x;";
    String actualLine = sourceFile.getLine(5);

    assertEquals("Expected line 5 to be: " + expectedLine, expectedLine, actualLine);
  }

  @org.junit.Test
  public void testGetRegion() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() {\n" +
            "  var x = 1;\n" +
            "  return x;\n" +
            "}";
    try {
      sourceFile.setCode(code);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Region expectedRegion = new Region(1, 3, "function test() {\n  var x = 1;\n");
    Region actualRegion = sourceFile.getRegion(2);

    assertEquals("Expected region 2 to be: " + expectedRegion, expectedRegion, actualRegion);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testGetLineInvalidLineNumber() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    try {
      sourceFile.getLine(100);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testGetLineNullSourceFile() {
    SourceFile sourceFile = null;
    try {
      sourceFile.getLine(1);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @org.junit.Test(expected = IOException.class)
  public void testGetCodeIOException() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    try {
      sourceFile.getCode();
    } catch (IOException e) {
      // Expected exception
    }
  }

  @org.junit.Test
  public void testGetCode() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() {\n" +
            "  var x = 1;\n" +
            "  return x;\n" +
            "}";
    try {
      sourceFile.setCode(code);
      String actualCode = sourceFile.getCode();
      assertEquals("Expected code to be: " + code, code, actualCode);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @org.junit.Test
  public void testFindLineOffsets() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() {\n" +
            "  var x = 1;\n" +
            "  return x;\n" +
            "}";
    try {
      sourceFile.setCode(code);
    } catch (IOException e) {
      e.printStackTrace();
    }

    int[] expectedLineOffsets = {0, 5, 11, 12};
    int[] actualLineOffsets = sourceFile.getLineOffsets();

    assertArrayEquals("Expected line offsets to be: " + Arrays.toString(expectedLineOffsets),
            expectedLineOffsets, actualLineOffsets);
  }
}