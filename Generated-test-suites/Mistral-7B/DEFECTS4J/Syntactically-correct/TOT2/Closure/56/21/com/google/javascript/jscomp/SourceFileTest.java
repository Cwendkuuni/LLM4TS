package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testSourceFileWithValidFileName() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    assertEquals("testFile.js", sourceFile.getName());
    assertFalse(sourceFile.isExtern());
    // Add more assertions based on the methods available in SourceFile
  }

  @Test
  public void testSourceFileWithExternFlag() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileWithNullFileName() {
    new SourceFile(null);
  }

  @Test
  public void testGetLine() throws IOException {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("testFile.js", code);
    String expectedLine1 = "function foo() {\n";
    String expectedLine2 = "  var x = 1;\n";
    String expectedLine3 = "  return x;\n";
    assertEquals(expectedLine1, sourceFile.getLine(1));
    assertEquals(expectedLine2, sourceFile.getLine(2));
    assertEquals(expectedLine3, sourceFile.getLine(3));
  }

  @Test
  public void testGetRegion() throws IOException {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("testFile.js", code);
    Region expectedRegion = new Region(1, 1, "function foo() {\n" +
        "  var x = 1;\n");
    Region region = sourceFile.getRegion(1);
    assertEquals(expectedRegion.getStartLine(), region.getStartLine());
    assertEquals(expectedRegion.getEndLine(), region.getEndLine());
    assertEquals(expectedRegion.getCode(), region.getCode());
  }

  @Test(expected = IOException.class)
  public void testGetLineWithNullCode() throws IOException {
    SourceFile sourceFile = new Generated("testFile.js", () -> null);
    sourceFile.getLine(1);
  }

  @Test
  public void testGetLineWithEmptyCode() throws IOException {
    SourceFile sourceFile = new Generated("testFile.js", () -> "");
    assertEquals("", sourceFile.getLine(1));
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("testFile.js", code);
    int[] lineOffsets = sourceFile.lineOffsets;
    assertEquals(3, lineOffsets.length);
    assertEquals(1, lineOffsets[0]);
    assertEquals(16, lineOffsets[1]);
    assertEquals(33, lineOffsets[2]);
  }

  @Test
  public void testGetNumLines() throws IOException {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("testFile.js", code);
    assertEquals(3, sourceFile.getNumLines());
  }

  @Test
  public void testGetCodeReader() throws IOException {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("testFile.js", code);
    Reader reader = sourceFile.getCodeReader();
    String expectedCode = "function foo() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    assertEquals(expectedCode, CharStreams.toString(reader));
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String code = "function foo() {\n" +
        "  var x = 1;\n" +
        "  return x;\n" +
        "}";
    SourceFile sourceFile = new Preloaded("testFile.js", code);
    String cachedCode = sourceFile.getCode();
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
    assertEquals(cachedCode, sourceFile.getCodeNoCache());
  }
}