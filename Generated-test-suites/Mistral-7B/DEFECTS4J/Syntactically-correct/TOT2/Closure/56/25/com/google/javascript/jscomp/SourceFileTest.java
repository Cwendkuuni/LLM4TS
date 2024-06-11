import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceFile.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testGetLine() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    sourceFile.setCode(code);

    assertEquals("function test() {\n", sourceFile.getLine(1));
    assertEquals("  var x = 1;\n", sourceFile.getLine(2));
    assertEquals("return x;\n", sourceFile.getLine(3));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineWithInvalidLineNumber() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    sourceFile.getLine(100);
  }

  @Test
  public void testGetRegion() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    sourceFile.setCode(code);

    Region region = sourceFile.getRegion(1);
    assertEquals(1, region.getStartLineNumber());
    assertEquals(1, region.getEndLineNumber());
    assertEquals("function test() {\n", region.getSourceCode());

    region = sourceFile.getRegion(3);
    assertEquals(1, region.getStartLineNumber());
    assertEquals(3, region.getEndLineNumber());
    assertEquals("return x;\n", region.getSourceCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionWithInvalidLineNumber() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    sourceFile.getRegion(100);
  }

  @Test
  public void testGetLineOffset() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    sourceFile.setCode(code);

    int offset = sourceFile.getLineOffset(1);
    assertEquals(1, offset);

    offset = sourceFile.getLineOffset(2);
    assertEquals(1 + code.lines().get(0).length() + 1, offset);

    offset = sourceFile.getLineOffset(3);
    assertEquals(1 + code.lines().get(0).length() + 1 + code.lines().get(1).length() + 1, offset);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineOffsetWithInvalidLineNumber() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    sourceFile.getLineOffset(100);
  }

  @Test
  public void testGetNumLines() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    sourceFile.setCode(code);

    assertEquals(3, sourceFile.getNumLines());
  }

  @Test
  public void testGetName() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    assertEquals("testFile.js", sourceFile.getName());
  }

  @Test
  public void testIsExtern() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    assertFalse(sourceFile.isExtern());

    sourceFile = new OnDisk("testFile.js");
    assertFalse(sourceFile.isExtern());

    sourceFile = new Preloaded("testFile.js", "testFile.js", "function test() { }");
    assertFalse(sourceFile.isExtern());

    sourceFile = new Generated("testFile.js", () -> "function test() { }");
    assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testSetIsExtern() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    assertFalse(sourceFile.isExtern());

    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  @Test(expected = NullPointerException.class)
  public void testGetCodeWithNullCode() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    sourceFile.getCode();
  }

  @Test
  public void testGetCodeReaderWithNullCode() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    assertNull(sourceFile.getCodeReader());

    sourceFile.setCode("function test() { }");
    assertEquals(new StringReader("function test() { }"), sourceFile.getCodeReader());
  }

  @Test(expected = IOException.class)
  public void testGetCodeWithIOException() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    throw new IOException();
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}\n" +
        "function test2() {\n" +
        "  var y = 2;\n" +
        "  return y;\n" +
        "}";
    sourceFile.setCode(code);

    int[] lineOffsets = sourceFile.lineOffsets;
    assertEquals(6, lineOffsets.length);

    assertEquals(1, lineOffsets[0]);
    assertEquals(2 + code.lines().get(0).length() + 1, lineOffsets[1]);
    assertEquals(3 + code.lines().get(0).length() + 1 + code.lines().get(1).length() + 1, lineOffsets[2]);
    assertEquals(4 + code.lines().get(0).length() + 1 + code.lines().get(1).length() + 1 + code.lines().get(2).length() + 1, lineOffsets[3]);
    assertEquals(5 + code.lines().get(0).length() + 1 + code.lines().get(1).length() + 1 + code.lines().get(2).length() + 1 + code.lines().get(3).length() + 1, lineOffsets[4]);
    assertEquals(6 + code.lines().get(0).length() + 1 + code.lines().get(1).length() + 1 + code.lines().get(2).length() + 1 + code.lines().get(3).length() + 1 + code.lines().get(4).length() + 1, lineOffsets[5]);
  }

  @Test
  public void testGetOriginalPath() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    assertEquals("testFile.js", sourceFile.getOriginalPath());

    sourceFile = new OnDisk("testFile.js");
    assertEquals("testFile.js", sourceFile.getOriginalPath());

    sourceFile = new Preloaded("testFile.js", "testFile.original.js", "function test() { }");
    assertEquals("testFile.original.js", sourceFile.getOriginalPath());

    sourceFile = new Generated("testFile.js", () -> "function test() { }");
    assertNull(sourceFile.getOriginalPath());
  }

  @Test
  public void testSetOriginalPath() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    sourceFile.setOriginalPath("testFile.original.js");
    assertEquals("testFile.original.js", sourceFile.getOriginalPath());
  }

  @Test
  public void testClearCachedSource() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function test() { }";
    sourceFile.setCode(code);

    assertEquals("function test() { }", sourceFile.getCode());

    sourceFile.clearCachedSource();

    assertNull(sourceFile.getCode());
  }

  @Test
  public void testHasSourceInMemory() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    assertFalse(sourceFile.hasSourceInMemory());

    sourceFile = new SourceFile("testFile.js") {
      private String code;

      @Override
      public String getCode() throws IOException {
        return code;
      }

      @Override
      public void setCode(String sourceCode) {
        this.code = sourceCode;
      }
    };
    sourceFile.setCode("function test() { }");
    assertTrue(sourceFile.hasSourceInMemory());
  }
}