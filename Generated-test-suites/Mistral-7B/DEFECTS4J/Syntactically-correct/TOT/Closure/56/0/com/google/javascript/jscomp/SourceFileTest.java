package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Tests for {@link SourceFile} class.
 */
public class SourceFileTest {

  /**
   * Tests that a new SourceFile can be created.
   */
  @Test
  public void testCreateSourceFile() {
    String fileName = "test.js";
    SourceFile sourceFile = new SourceFile(fileName);
    assertEquals(fileName, sourceFile.getName());
  }

  /**
   * Tests that getLine method returns correct line text.
   */
  @Test
  public void testGetLine() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n  var x = 1;\n  return x;\n}";
    SourceFile sourceFile = new Preloaded(fileName, code);

    String expectedLine1 = "function test() {";
    String expectedLine2 = "  var x = 1;";
    String expectedLine3 = "  return x;";

    assertEquals(expectedLine1, sourceFile.getLine(1));
    assertEquals(expectedLine2, sourceFile.getLine(2));
    assertEquals(expectedLine3, sourceFile.getLine(3));
  }

  /**
   * Tests that getRegion method returns correct region of code.
   */
  @Test
  public void testGetRegion() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n  var x = 1;\n  return x;\n}";
    SourceFile sourceFile = new Preloaded(fileName, code);

    String expectedRegion = "function test() {\n  var x = 1;\n  return x;\n}";

    Region region = sourceFile.getRegion(1);
    assertEquals(expectedRegion, region.getCode());
  }

  /**
   * Tests that getLine method returns null if line number is out of bounds.
   */
  @Test
  public void testGetLineOutOfBounds() {
    String fileName = "test.js";
    String code = "function test() {\n  var x = 1;\n  return x;\n}";
    SourceFile sourceFile = new Preloaded(fileName, code);

    assertNull(sourceFile.getLine(5));
  }

  /**
   * Tests that getRegion method returns null if region length is out of bounds.
   */
  @Test
  public void testGetRegionOutOfBounds() {
    String fileName = "test.js";
    String code = "function test() {\n  var x = 1;\n  return x;\n}";
    SourceFile sourceFile = new Preloaded(fileName, code);

    assertNull(sourceFile.getRegion(5));
  }

  /**
   * Tests that isExtern method returns correct value based on constructor argument.
   */
  @Test
  public void testIsExtern() {
    String fileName = "test.js";
    SourceFile sourceFile1 = new Preloaded(fileName, "code");
    assertFalse(sourceFile1.isExtern());

    String externFileName = "extern.js";
    SourceFile sourceFile2 = new Preloaded(externFileName, "code");
    assertTrue(sourceFile2.isExtern());
  }

  /**
   * Tests that getLineOffset method returns correct offset.
   */
  @Test
  public void testGetLineOffset() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n  var x = 1;\n  return x;\n}";
    SourceFile sourceFile = new Preloaded(fileName, code);

    int expectedOffset1 = 0;
    int expectedOffset2 = 11;
    int expectedOffset3 = 23;

    assertEquals(expectedOffset1, sourceFile.getLineOffset(1));
    assertEquals(expectedOffset2, sourceFile.getLineOffset(2));
    assertEquals(expectedOffset3, sourceFile.getLineOffset(3));
  }

  /**
   * Tests that getNumLines method returns correct number of lines.
   */
  @Test
  public void testGetNumLines() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n  var x = 1;\n  return x;\n}";
    SourceFile sourceFile = new Preloaded(fileName, code);

    assertEquals(3, sourceFile.getNumLines());
  }

  /**
   * Tests that clearCachedSource method clears cached code.
   */
  @Test
  public void testClearCachedSource() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n  var x = 1;\n  return x;\n}";
    SourceFile sourceFile = new Preloaded(fileName, code);

    String expectedCode1 = "function test() {\n  var x = 1;\n  return x;\n}";
    String expectedCode2 = "";

    assertEquals(expectedCode1, sourceFile.getCode());

    sourceFile.clearCachedSource();

    assertEquals(expectedCode2, sourceFile.getCode());
  }

  /**
   * Tests that getCode method returns correct code.
   */
  @Test
  public void testGetCode() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n  var x = 1;\n  return x;\n}";
    SourceFile sourceFile = new Preloaded(fileName, code);

    assertEquals(code, sourceFile.getCode());
  }

  /**
   * Tests that fromFile method creates SourceFile from a file.
   */
  @Test
  public void testFromFile() throws IOException {
    String fileName = "test.js";
    File file = new File(fileName);
    String code = "function test() {\n  var x = 1;\n  return x;\n}";
    Files.write(file, code.getBytes(Charsets.UTF_8));

    SourceFile sourceFile = SourceFile.fromFile(fileName);

    assertArrayEquals(new String[] {"function test() {", "  var x = 1;",
        "  return x;", "}"}, sourceFile.getLines());
  }

  /**
   * Tests that fromFile method with custom charset creates SourceFile from
   * a file.
   */
  @Test
  public void testFromFileWithCustomCharset() throws IOException {
    String fileName = "test.js";
    File file = new File(fileName);
    String code = "function test() {\\n  var x = 1; \\n  return x; \\n}";
    Files.write(file, code.getBytes(Charsets.ISO_8859_1));

    SourceFile sourceFile = SourceFile.fromFile(fileName, Charsets.ISO_8859_1);

    assertArrayEquals(new String[] {"function test() {", "  var x = 1; ",
        "  return x; ", "}"}, sourceFile.getLines());
  }

  /**
   * Tests that fromInputStream method creates SourceFile from an input stream.
   */
  @Test
  public void testFromInputStream() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n  var x = 1;\n  return x;\n}";
    File file = new File(fileName);
    Files.write(file, code.getBytes(Charsets.UTF_8));
    InputStream inputStream = Files.newInputStream(file.toPath());

    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream);

    assertArrayEquals(new String[] {"function test() {", "  var x = 1;",
        "  return x;", "}"}, sourceFile.getLines());
  }

  /**
   * Tests that fromReader method creates SourceFile from a reader.
   */
  @Test
  public void testFromReader() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n  var x = 1;\n  return x;\n}";
    File file = new File(fileName);
    Files.write(file, code.getBytes(Charsets.UTF_8));
    Reader reader = new InputStreamReader(Files.newInputStream(file.toPath()),
        Charsets.UTF_8);

    SourceFile sourceFile = SourceFile.fromReader(fileName, reader);

    assertArrayEquals(new String[] {"function test() {", "  var x = 1;",
        "  return x;", "}"}, sourceFile.getLines());
  }

  /**
   * Tests that fromGenerator method creates SourceFile from a generator.
   */
  @Test
  public void testFromGenerator() {
    String fileName = "test.js";
    Generator generator = new Generator() {
      @Override
      public String getCode() {
        return "function test() {\n  var x = 1;\n  return x;\n}";
      }
    };

    SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);

    assertArrayEquals(new String[] {"function test() {", "  var x = 1;",
        "  return x;", "}"}, sourceFile.getLines());
  }

  /**
   * Tests that getCode method throws IOException when file is not found.
   */
  @Test(expected = IOException.class)
  public void testGetCodeIOException() throws IOException {
    String fileName = "non-existent.js";
    new SourceFile(fileName).getCode();
  }
}