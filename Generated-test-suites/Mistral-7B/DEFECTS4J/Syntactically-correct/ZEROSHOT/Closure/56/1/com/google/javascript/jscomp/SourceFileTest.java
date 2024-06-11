import static org.junit.Assert.*;
import com.google.javascript.jscomp.SourceFile;

public class SourceFileTest {

  @org.junit.Test
  public void testGetLineOffset() {
    SourceFile sourceFile = new SourceFile("test.js");
    sourceFile.setCode("line1\nline2\nline3");
    int lineNumber = 2;
    int lineOffset = sourceFile.getLineOffset(lineNumber);
    int expectedLineOffset = 11; // length of line1 + 1
    assertEquals(expectedLineOffset, lineOffset);
  }

  @org.junit.Test
  public void testGetLine() {
    SourceFile sourceFile = new SourceFile("test.js");
    try {
      sourceFile.setCode("line1\nline2\nline3");
      String expectedLine = "line2";
      String line = sourceFile.getLine(2);
      assertEquals(expectedLine, line);
    } catch (IOException e) {
      fail("getLine should not throw an IOException");
    }
  }

  @org.junit.Test
  public void testGetRegion() {
    SourceFile sourceFile = new SourceFile("test.js");
    try {
      sourceFile.setCode("line1\nline2\nline3\nline4");
      int lineNumber = 3;
      Region region = sourceFile.getRegion(lineNumber);
      String expectedRegion = "line2\nline3\nline4";
      assertEquals(expectedRegion, region.getCode());
    } catch (IOException e) {
      fail("getRegion should not throw an IOException");
    }
  }

  @org.junit.Test
  public void testFromFile() {
    String filePath = "/test/SourceFileTest.java";
    SourceFile sourceFile = SourceFile.fromFile(filePath);
    assertNotNull(sourceFile);
    assertEquals(filePath, sourceFile.getName());
  }

  @org.junit.Test
  public void testFromCode() {
    String fileName = "test.js";
    String code = "function test() {}";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    assertNotNull(sourceFile);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
  }

  @org.junit.Test
  public void testGetLineOffsetWithExternFile() {
    SourceFile sourceFile = new SourceFile("extern.js");
    sourceFile.setIsExtern(true);
    int lineNumber = 2;
    int lineOffset = sourceFile.getLineOffset(lineNumber);
    int expectedLineOffset = 0; // extern files do not have line offsets
    assertEquals(expectedLineOffset, lineOffset);
  }

  @org.junit.Test
  public void testGetLineWithExternFile() {
    SourceFile sourceFile = new SourceFile("extern.js");
    sourceFile.setIsExtern(true);
    String expectedLine = ""; // extern files do not have lines
    String line = sourceFile.getLine(2);
    assertEquals(expectedLine, line);
  }

  @org.junit.Test
  public void testGetRegionWithExternFile() {
    SourceFile sourceFile = new SourceFile("extern.js");
    sourceFile.setIsExtern(true);
    Region region = sourceFile.getRegion(2);
    String expectedRegion = ""; // extern files do not have regions
    assertEquals(expectedRegion, region.getCode());
  }
}