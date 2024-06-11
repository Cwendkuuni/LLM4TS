package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.Generator;
import com.google.javascript.rhino.jstype.StaticSourceFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.Charset;

import org.junit.Test;

/**
 * Unit tests for SourceFile class.
 */
public class SourceFileTest {

  /**
   * Test constructor with file path.
   * @throws IOException
   */
  @Test
  public void testFromFile() throws IOException {
    String fileName = "test.js";
    File file = new File(fileName);
    Charset charset = Charsets.UTF_8;
    SourceFile sourceFile = SourceFile.fromFile(fileName, charset);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(file.getAbsolutePath(), sourceFile.getOriginalPath());
    String code = sourceFile.getCode();
    assertTrue(code.length() > 0);
  }

  /**
   * Test constructor with InputStream.
   * @throws IOException
   */
  @Test
  public void testFromInputStream() throws IOException {
    String fileName = "test.js";
    File file = new File(fileName);
    InputStream inputStream = Files.newInputStream(file);
    Charset charset = Charsets.UTF_8;
    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream, charset);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(file.getAbsolutePath(), sourceFile.getOriginalPath());
    String code = sourceFile.getCode();
    assertTrue(code.length() > 0);
  }

  /**
   * Test constructor with String code.
   */
  @Test
  public void testFromCode() {
    String fileName = "test.js";
    String code = "function test() { return 1; }";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(null, sourceFile.getOriginalPath());
    String sourceCode = sourceFile.getCodeNoCache();
    assertEquals(code, sourceCode);
  }

  /**
   * Test getLine and getRegion methods.
   * @throws IOException
   */
  @Test
  public void testGetLineAndRegion() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n" +
                  "  var x = 1;\n" +
                  "  var y = 2;\n" +
                  "  return x + y;\n" +
                  "}";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    int lineNumber = 3;
    String expectedRegion = "  var y = 2;\n" +
                            "  return x + y;";

    String line = sourceFile.getLine(lineNumber);
    Region region = sourceFile.getRegion(lineNumber);

    assertEquals("var y = 2;", line);
    assertEquals(new SimpleRegion(2, 3, expectedRegion), region);
  }

  /**
   * Test generator SourceFile.
   */
  @Test
  public void testGeneratedSourceFile() {
    String fileName = "test.js";
    String code = "function test() { return 1; }";
    Generator generator = () -> code;
    SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(null, sourceFile.getOriginalPath());
    String sourceCode = sourceFile.getCodeNoCache();
    assertEquals(code, sourceCode);
  }
}