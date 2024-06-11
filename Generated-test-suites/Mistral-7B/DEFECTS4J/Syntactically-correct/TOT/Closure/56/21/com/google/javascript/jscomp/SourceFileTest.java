package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.SourceName;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testFromFile() throws IOException {
    SourceFile sourceFile = SourceFile.fromFile("test.js");
    String code = sourceFile.getCode();
    assertEquals("Expected test.js content", "var x = 1;\n", code);

    SourceName sourceName = sourceFile.getName();
    assertEquals("Expected source name", "test.js", sourceName.getSourceName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidFileName() {
    SourceFile sourceFile = new SourceFile("");
  }

  @Test
  public void testFromFileWithCharset() throws IOException {
    SourceFile sourceFile = SourceFile.fromFile(new File("test.js"), Charsets.UTF_8);
    String code = sourceFile.getCode();
    assertEquals("Expected test.js content", "var x = 1;\n", code);
  }

  @Test(expected = FileNotFoundException.class)
  public void testMissingFile() throws IOException {
    SourceFile sourceFile = SourceFile.fromFile("missingFile.js");
  }

  @Test
  public void testFromCode() {
    String code = "var x = 1;\n";
    SourceFile sourceFile = SourceFile.fromCode("test.js", code);
    String sourceCode = sourceFile.getCode();
    assertEquals("Expected test.js content", code, sourceCode);
  }

  @Test
  public void testFromInputStream() throws IOException {
    String testContent = "var x = 1;\n";
    File tempFile = File.createTempFile("test", null);
    Files.write(tempFile, testContent.getBytes());

    SourceFile sourceFile = SourceFile.fromInputStream("test.js", new FileInputStream(tempFile));
    String sourceCode = sourceFile.getCode();
    assertEquals("Expected test.js content", testContent, sourceCode);
  }

  @Test
  public void testGetLine() throws IOException {
    String code = "function foo() {\n" +
                  "  var x = 1;\n" +
                  "  var y = 2;\n" +
                  "}\n";
    SourceFile sourceFile = new Preloaded("test.js", code);

    String line1 = sourceFile.getLine(1);
    assertEquals("Expected line 1 content", "function foo() {", line1);

    String line3 = sourceFile.getLine(3);
    assertEquals("Expected line 3 content", "  var y = 2;\n", line3);

    String line5 = sourceFile.getLine(5); // Out of bounds
    assertNull("Expected null for out of bounds line number", line5);
  }

  @Test
  public void testGetRegion() throws IOException {
    String code = "function foo() {\n" +
                  "  var x = 1;\n" +
                  "  var y = 2;\n" +
                  "  var z = 3;\n" +
                  "}\n";
    SourceFile sourceFile = new Preloaded("test.js", code);

    Region region = sourceFile.getRegion(3);
    String expectedRegionContent = "  var y = 2;\n" +
                                  "  var z = 3;\n";
    assertEquals("Expected region content", expectedRegionContent, region.getCode());
    assertEquals("Expected region start line", 3, region.getStartLineNumber());
    assertEquals("Expected region end line", 5, region.getEndLineNumber());

    Region regionOutOfBounds = sourceFile.getRegion(5); // Out of bounds
    assertNull("Expected null for out of bounds region", regionOutOfBounds);
  }

  @Test
  public void testIsExtern() throws IOException {
    SourceFile sourceFile = new OnDisk("test.js");
    assertFalse("Expected source file not to be extern", sourceFile.isExtern());

    SourceFile sourceFileExtern = new Preloaded("test.js", "// EXTERN: test.js");
    assertTrue("Expected source file to be extern", sourceFileExtern.isExtern());
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    String code = "function foo() {\n" +
                  "  var x = 1;\n" +
                  "  var y = 2;\n" +
                  "  var z = 3;\n" +
                  "}\n";
    SourceFile sourceFile = new Preloaded("test.js", code);

    int line1 = sourceFile.getLineOffset(1);
    int line2 = sourceFile.getLineOffset(2);
    int line3 = sourceFile.getLineOffset(3);

    assertEquals("Expected line 1 offset", 0, line1);
    assertEquals("Expected line 2 offset", 1, line2);
    assertEquals("Expected line 3 offset", 3, line3);

    int line5 = sourceFile.getLineOffset(5); // Out of bounds
    assertEquals("Expected line 5 offset", -1, line5);
  }
}