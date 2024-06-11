package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.io.Charsets;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;
import com.google.javascript.rhino.jstype.StaticSourceFile;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Tests for {@link SourceFile} class.
 */
public class SourceFileTest {

  /**
   * Test for {@link SourceFile#SourceFile(String)} constructor.
   */
  @Test
  public void testSourceFile() throws IOException {
    SourceFile sourceFile = new SourceFile("test.js");

    assertEquals("test.js", sourceFile.getName());
    assertEquals(1, sourceFile.getNumLines());
    assertNull(sourceFile.getCode());
    assertFalse(sourceFile.isExtern());
    assertNull(sourceFile.getOriginalPath());
  }

  /**
   * Test for {@link SourceFile#getCode()} method.
   */
  @Test
  public void testGetCode() throws IOException {
    String code = "function test() {}";
    SourceFile sourceFile = new Preloaded("test.js", code);

    assertEquals(code, sourceFile.getCode());
  }

  /**
   * Test for {@link SourceFile#getCodeReader()} method.
   */
  @Test
  public void testGetCodeReader() throws IOException {
    String code = "function test() {}";
    SourceFile sourceFile = new Preloaded("test.js", code);

    Reader reader = sourceFile.getCodeReader();
    assertEquals(code, CharStreams.toString(reader));
  }

  /**
   * Test for {@link SourceFile#getLine(int)} method.
   */
  @Test
  public void testGetLine() throws IOException {
    String code = "function test() {\n" +
                  "  var x = 1;\n" +
                  "  return x;\n" +
                  "}";
    SourceFile sourceFile = new Preloaded("test.js", code);

    assertEquals("function test() {", sourceFile.getLine(1));
    assertEquals("  var x = 1;\n", sourceFile.getLine(2));
    assertEquals("  return x;\n", sourceFile.getLine(3));
    assertEquals("}", sourceFile.getLine(4));
    assertNull(sourceFile.getLine(5));
  }

  /**
   * Test for {@link SourceFile#getLineOffset(int)} method.
   */
  @Test
  public void testGetLineOffset() throws IOException {
    String code = "function test() {\n" +
                  "  var x = 1;\n" +
                  "  var y = 2;\n" +
                  "  return x + y;\n" +
                  "}";
    SourceFile sourceFile = new Preloaded("test.js", code);

    int[] expected = {0, 11, 16, 19, 26, 31, 35, 39, 42, 45, 49, 53, 56};
    assertArrayEquals(expected, sourceFile.getLineOffsets());
  }

  /**
   * Test for {@link SourceFile#getRegion(int)} method.
   */
  @Test
  public void testGetRegion() throws IOException {
    String code = "function test() {\n" +
                  "  var x = 1;\n" +
                  "  var y = 2;\n" +
                  "  return x + y;\n" +
                  "}";
    SourceFile sourceFile = new Preloaded("test.js", code);

    String expected = "function test() {\n" +
                      "  var x = 1;\n" +
                      "  var y = 2;\n" +
                      "  return x + y;\n" +
                      "}";
    Region region = sourceFile.getRegion(1);
    assertEquals(1, region.getStartLineNumber());
    assertEquals(1, region.getEndLineNumber());
    assertEquals(expected, region.getSource());

    expected = "  var y = 2;\n" +
               "  return x + y;\n";
    region = sourceFile.getRegion(3);
    assertEquals(2, region.getStartLineNumber());
    assertEquals(3, region.getEndLineNumber());
    assertEquals(expected, region.getSource());
  }

  /**
   * Test for {@link SourceFile#isExtern()} method.
   */
  @Test
  public void testIsExtern() throws IOException {
    SourceFile sourceFile = new Preloaded("test.js", "//extern");
    assertTrue(sourceFile.isExtern());

    sourceFile = new Preloaded("test.js");
    assertFalse(sourceFile.isExtern());
  }

  /**
   * Test for {@link SourceFile#setOriginalPath(String)} method.
   */
  @Test
  public void testSetOriginalPath() throws IOException {
    String code = "function test() {}";
    SourceFile sourceFile = new Preloaded("test.js", code);

    sourceFile.setOriginalPath("path/to/test.js");
    assertEquals("path/to/test.js", sourceFile.getOriginalPath());
  }

  /**
   * Test for {@link SourceFile#clearCachedSource()} method.
   */
  @Test
  public void testClearCachedSource() throws IOException {
    String code = "function test() {}";
    SourceFile sourceFile = new Preloaded("test.js", code);

    String cachedCode = sourceFile.getCode();
    assertNull(sourceFile.getCode());
    assertEquals(cachedCode, sourceFile.getCode());
  }

  /**
   * Test for {@link SourceFile#hasSourceInMemory()} method.
   */
  @Test
  public void testHasSourceInMemory() throws IOException {
    String code = "function test() {}";
    SourceFile sourceFile = new Preloaded("test.js", code);

    assertTrue(sourceFile.hasSourceInMemory());
    sourceFile.clearCachedSource();
    assertFalse(sourceFile.hasSourceInMemory());
  }

  /**
   * Test for {@link SourceFile#getName()} method.
   */
  @Test
  public void testName() throws IOException {
    String code = "function test() {}";
    SourceFile sourceFile = new Preloaded("test.js", code);

    assertEquals("test.js", sourceFile.getName());
  }

  /**
   * Test for {@link SourceFile#toString()} method.
   */
  @Test
  public void testToString() throws IOException {
    String code = "function test() {}";
    SourceFile sourceFile = new Preloaded("test.js", code);

    assertEquals("test.js", sourceFile.toString());
  }

  /**
   * Test for {@link SourceFile#fromFile(String, Charset)} method.
   */
  @Test
  public void testFromFile() throws IOException {
    String code = "function test() {}";
    File file = new File("test.js");
    Files.write(file, code.getBytes(Charsets.UTF_8));

    SourceFile sourceFile = SourceFile.fromFile("test.js", Charsets.UTF_8);
    assertEquals("test.js", sourceFile.getName());
    assertEquals(code, sourceFile.getCode());

    Files.delete(file);
  }

  /**
   * Test for {@link SourceFile#fromFile(String)} method.
   */
  @Test
  public void testFromFileNoCharset() throws IOException {
    String code = "function test() {}";
    File file = new File("test.js");
    Files.write(file, code.getBytes(Charsets.UTF_8));

    SourceFile sourceFile = SourceFile.fromFile("test.js");
    assertEquals("test.js", sourceFile.getName());
    assertEquals(code, sourceFile.getCode());

    Files.delete(file);
  }

  /**
   * Test for {@link SourceFile#fromCode(String, String)} method.
   */
  @Test
  public void testFromCode() throws IOException {
    String code = "function test() {}";
    SourceFile sourceFile = SourceFile.fromCode("test.js", code);
    assertEquals("test.js", sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
  }

  /**
   * Test for {@link SourceFile#fromCode(String, String, String)} method.
   */
  @Test
  public void testFromCodeWithOriginalPath() throws IOException {
    String code = "function test() {}";
    SourceFile sourceFile = SourceFile.fromCode("test.js", null, code);
    assertEquals("test.js", sourceFile.getName());
    assertEquals(code, sourceFile.getCode());

    sourceFile = SourceFile.fromCode("test.js", "path/to/file.js", code);
    assertEquals("path/to/file.js", sourceFile.getOriginalPath());
    assertEquals(code, sourceFile.getCode());
  }

  /**
   * Test for {@link SourceFile#fromInputStream(String, InputStream)} method.
   */
  @Test
  public void testFromInputStream() throws IOException {
    String code = "function test() {}";
    InputStream inputStream = new ByteArrayInputStream(code.getBytes(Charsets.UTF_8));

    SourceFile sourceFile = SourceFile.fromInputStream("test.js", inputStream);
    assertEquals("test.js", sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
  }

  /**
   * Test for {@link SourceFile#fromInputStream(String, String, InputStream)} method.
   */
  @Test
  public void testFromInputStreamWithOriginalPath() throws IOException {
    String code = "function test() {}";
    InputStream inputStream = new ByteArrayInputStream(code.getBytes(Charsets.UTF_8));

    SourceFile sourceFile = SourceFile.fromInputStream("test.js", "path/to/file.js", inputStream);
    assertEquals("path/to/file.js", sourceFile.getOriginalPath());
    assertEquals(code, sourceFile.getCode());
  }

  /**
   * Test for {@link SourceFile#fromReader(String, Reader)} method.
   */
  @Test
  public void testFromReader() throws IOException {
    String code = "function test() {}";
    Reader reader = new InputStreamReader(new ByteArrayInputStream(code.getBytes(Charsets.UTF_8)), Charsets.UTF_8);

    SourceFile sourceFile = SourceFile.fromReader("test.js", reader);
    assertEquals("test.js", sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
  }

  /**
   * Test for {@link SourceFile#fromGenerator(String, Generator)} method.
   */
  @Test
  public void testFromGenerator() throws IOException {
    String expectedCode = "function test() {}";
    final Generator generator = new Generator() {
      @Override
      public String getCode() {
        return expectedCode;
      }
    };

    SourceFile sourceFile = SourceFile.fromGenerator("test.js", generator);
    assertEquals("test.js", sourceFile.getName());
    assertEquals(expectedCode, sourceFile.getCode());
  }
}