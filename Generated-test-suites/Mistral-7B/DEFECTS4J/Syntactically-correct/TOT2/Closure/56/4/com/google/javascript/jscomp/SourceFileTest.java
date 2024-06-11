import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceFile.*;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class SourceFileTest {

  @Test
  public void testBasicFunctionality() throws IOException {
    String fileName = "TestSourceFile.java";
    SourceFile sourceFile = new OnDisk(new File(fileName));
    String code = sourceFile.getCode();
    assertEquals("Expected source file code to match file content",
        "public class TestSourceFile {}", code);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeLineNumber() {
    SourceFile sourceFile = new OnDisk(new File("TestSourceFile.java"));
    sourceFile.getLine(-1);
  }

  @Test
  public void testLineNumberOutOfRange() {
    SourceFile sourceFile = new OnDisk(new File("TestSourceFile.java"));
    String code = sourceFile.getCode();
    // Assuming the test source file is a single line long
    int numLines = code.split("\n").length;
    int lastLineNumber = numLines;
    for (int lineNumber = 1; lineNumber < lastLineNumber; lineNumber++) {
      String line = sourceFile.getLine(lineNumber);
      assertNotNull("Expected line " + lineNumber + " to exist", line);
      assertTrue("Expected line " + lineNumber + " to be non-empty", line.length() > 0);
    }
    // The last line should be empty
    String lastLine = sourceFile.getLine(lastLineNumber);
    assertNull("Expected last line to be empty", lastLine);
  }

  @Test(expected = IOException.class)
  public void testIOException() throws IOException {
    String fileName = "NonExistentFile.java";
    new OnDisk(new File(fileName)).getCode();
  }

  @Test
  public void testGetRegion() throws IOException {
    String fileName = "TestSourceFile.java";
    SourceFile sourceFile = new OnDisk(new File(fileName));
    Region region = sourceFile.getRegion(5);
    assertNotNull("Expected region to exist", region);
    String code = sourceFile.getCode();
    int lineNumber = 5;
    String expectedRegion = code.substring(sourceFile.getLineOffset(lineNumber),
        sourceFile.getLineOffset(lineNumber + 1));
    assertEquals("Expected region to match the code in the indicated line and the surrounding context",
        expectedRegion, region.getCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNegativeRegionLength() {
    SourceFile sourceFile = new OnDisk(new File("TestSourceFile.java"));
    sourceFile.getRegion(5, -1);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidRegionLength() {
    SourceFile sourceFile = new OnDisk(new File("TestSourceFile.java"));
    sourceFile.getRegion(5, SourceFile.SOURCE_EXCERPT_REGION_LENGTH + 1);
  }

  @Test
  public void testGetRegionWithInvalidLineNumber() {
    String fileName = "TestSourceFile.java";
    SourceFile sourceFile = new OnDisk(new File(fileName));
    Region region = sourceFile.getRegion(-1);
    assertNull("Expected null for invalid line number", region);
  }

  @Test
  public void testGetRegionWithInvalidStartLineNumber() {
    String fileName = "TestSourceFile.java";
    SourceFile sourceFile = new OnDisk(new File(fileName));
    Region region = sourceFile.getRegion(0);
    assertNull("Expected null for start line number less than 1", region);
  }

  @Test
  public void testGetRegionWithInvalidEndLineNumber() {
    String fileName = "TestSourceFile.java";
    SourceFile sourceFile = new OnDisk(new File(fileName));
    int numLines = sourceFile.getNumLines();
    Region region = sourceFile.getRegion(numLines + 1);
    assertNull("Expected null for end line number greater than the number of lines in the file", region);
  }
}