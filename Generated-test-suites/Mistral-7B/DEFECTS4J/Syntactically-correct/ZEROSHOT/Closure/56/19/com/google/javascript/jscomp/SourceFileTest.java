import static org.junit.Assert.*;

import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.OnDisk;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public class SourceFileTest {

  @org.junit.Test
  public void testGetLine() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    String expected = "function test() {\n  return 1;\n}";
    Files.write(new File("test.js"), expected.getBytes(), Charsets.UTF_8);

    String line = sourceFile.getLine(1);
    assertEquals("function test() {\n", line);

    line = sourceFile.getLine(3);
    assertNull(line);
  }

  @org.junit.Test
  public void testGetRegion() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    String expected = "function test() {\n  return 1;\n}";
    Files.write(new File("test.js"), expected.getBytes(), Charsets.UTF_8);

    Region region = sourceFile.getRegion(2);
    assertEquals(2, region.getBeginLineNumber());
    assertEquals(2, region.getEndLineNumber());
    assertEquals("  return 1;\n", region.getSource());
  }

  @org.junit.Test
  public void testGetLineOffset() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    String expected = "function test() {\n  return 1;\n}";
    Files.write(new File("test.js"), expected.getBytes(), Charsets.UTF_8);

    int lineOffset = sourceFile.getLineOffset(2);
    assertEquals(14, lineOffset);
  }

  @org.junit.Test
  public void testGetNumLines() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    String expected = "function test() {\n  return 1;\n}";
    Files.write(new File("test.js"), expected.getBytes(), Charsets.UTF_8);

    int numLines = sourceFile.getNumLines();
    assertEquals(2, numLines);
  }

  @org.junit.Test
  public void testFindLineOffsets() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    String expected = "function test() {\n  return 1;\n}";
    Files.write(new File("test.js"), expected.getBytes(), Charsets.UTF_8);

    sourceFile.findLineOffsets();

    int[] lineOffsets = sourceFile.lineOffsets;
    assertEquals(2, lineOffsets.length);
    assertEquals(12, lineOffsets[0]);
    assertEquals(24, lineOffsets[1]);
  }

  @org.junit.Test
  public void testGetOriginalPath() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    sourceFile.setOriginalPath("path/to/original/test.js");

    String originalPath = sourceFile.getOriginalPath();
    assertEquals("path/to/original/test.js", originalPath);
  }

  @org.junit.Test
  public void testClearCachedSource() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    String expected = "function test() {\n  return 1;\n}";
    Files.write(new File("test.js"), expected.getBytes(), Charsets.UTF_8);

    String code = sourceFile.getCode();
    assertNotNull(code);

    sourceFile.clearCachedSource();

    code = sourceFile.getCode();
    assertNull(code);
  }

  @org.junit.Test
  public void testHasSourceInMemory() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    String expected = "function test() {\n  return 1;\n}";
    Files.write(new File("test.js"), expected.getBytes(), Charsets.UTF_8);

    assertFalse(sourceFile.hasSourceInMemory());

    String code = sourceFile.getCode();
    assertNotNull(code);

    assertTrue(sourceFile.hasSourceInMemory());

    sourceFile.clearCachedSource();

    assertFalse(sourceFile.hasSourceInMemory());
  }

  @org.junit.Test
  public void testGetName() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    assertEquals("test.js", sourceFile.getName());
  }

  @org.junit.Test
  public void testIsExtern() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    assertFalse(sourceFile.isExtern());

    sourceFile.setIsExtern(true);

    assertTrue(sourceFile.isExtern());
  }

  @org.junit.Test
  public void testGetCode() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    String expected = "function test() {\n  return 1;\n}";
    Files.write(new File("test.js"), expected.getBytes(), Charsets.UTF_8);

    String code = sourceFile.getCode();
    assertEquals(expected, code);
  }

  @org.junit.Test
  public void testGetCodeReader() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    String expected = "function test() {\n  return 1;\n}";
    Files.write(new File("test.js"), expected.getBytes(), Charsets.UTF_8);

    Reader codeReader = sourceFile.getCodeReader();
    assertEquals(expected, CharStreams.toString(codeReader));
  }

  @org.junit.Test
  public void testToString() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    assertEquals("test.js", sourceFile.toString());
  }

  @org.junit.Test
  public void testFromFile() throws IOException {
    String expected = "function test() {\n  return 1;\n}";
    Files.write(new File("test.js"), expected.getBytes(), Charsets.UTF_8);

    SourceFile sourceFile = SourceFile.fromFile("test.js");

    assertEquals("test.js", sourceFile.getName());
    assertEquals(expected, sourceFile.getCode());
  }

  @org.junit.Test
  public void testFromGenerator() throws IOException {
    String expected = "function test() {\n  return 1;\n}";

    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return expected;
      }
    };

    assertEquals("test.js", sourceFile.getName());
    assertEquals(expected, sourceFile.getCode());
  }

  @org.junit.Test
  public void testFromInputStream() throws IOException {
    String expected = "function test() {\n  return 1;\n}";
    String input = "function test() {\n  return 1;\n}";

    InputStream inputStream = new ByteArrayInputStream(input.getBytes());

    SourceFile sourceFile = SourceFile.fromInputStream("test.js", inputStream);

    assertEquals("test.js", sourceFile.getName());
    assertEquals(expected, sourceFile.getCode());
  }

  @org.junit.Test
  public void testFromReader() throws IOException {
    String expected = "function test() {\n  return 1;\n}";
    String input = "function test() {\n  return 1;\n}";

    Reader reader = new StringReader(input);

    SourceFile sourceFile = SourceFile.fromReader("test.js", reader);

    assertEquals("test.js", sourceFile.getName());
    assertEquals(expected, sourceFile.getCode());
  }

  @org.junit.Test
  public void testFromCode() throws IOException {
    String expected = "function test() {\n  return 1;\n}";

    SourceFile sourceFile = SourceFile.fromCode("test.js", expected);

    assertEquals("test.js", sourceFile.getName());
    assertEquals(expected, sourceFile.getCode());
  }

  @org.junit.Test
  public void testFromFileWithCharset() throws IOException {
    String expected = "function test() {\n  return 1;\n}";
    Files.write(new File("test.js"), expected.getBytes(), Charsets.UTF_8);

    SourceFile sourceFile = SourceFile.fromFile("test.js", Charsets.UTF_8);

    assertEquals("test.js", sourceFile.getName());
    assertEquals(expected, sourceFile.getCode());
  }

  @org.junit.Test
  public void testSetOriginalPath() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));

    sourceFile.setOriginalPath("path/to/original/test.js");

    assertEquals("path/to/original/test.js", sourceFile.getOriginalPath());
  }

  @org.junit.Test
  public void testGetLineOffsetWithLastLineAndOffset() throws IOException {
    SourceFile sourceFile = new OnDisk(new File("test.js"));
    String expected = "function test() {\n  return 1;\n}";
    Files.write(new File("test.js"), expected.getBytes(), Charsets.UTF_8);

    int lastOffset = 12;
    int lastLine = 2;

    int lineOffset = sourceFile.getLineOffset(3);

    assertEquals(19, lineOffset);
  }
}