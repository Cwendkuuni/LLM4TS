package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

import com.google.common.io.Files;
import com.google.javascript.rhino.jstype.StaticSourceFile;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.OnDisk;

/**
 * Tests for the SourceFile class.
 */
public class SourceFileTest {

  @Test
  public void testGetLine() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function testFunction() {\n" +
        "  return \"Hello, World!\";\n" +
        "}\n";
    sourceFile.setCode(code);

    assertEquals("function testFunction() {\n", sourceFile.getLine(1));
    assertEquals("  return \"Hello, World!\";\n", sourceFile.getLine(3));
    assertNull(sourceFile.getLine(5));
  }

  @Test
  public void testGetRegion() throws IOException {
    SourceFile sourceFile = new SourceFile("testFile.js");
    String code = "function testFunction() {\n" +
        "  return \"Hello, World!\";\n" +
        "}\n";
    sourceFile.setCode(code);

    Region region = sourceFile.getRegion(3);

    assertEquals(3, region.getBeginLine());
    assertEquals(3, region.getEndLine());
    assertEquals("  return \"Hello, World!\";\n", region.getSource());
  }

  @Test
  public void testFromFile() throws IOException {
    String filePath = "testFile.js";
    String expectedCode = "function testFunction() {\n" +
        "  return \"Hello, World!\";\n" +
        "}\n";

    SourceFile sourceFile = SourceFile.fromFile(filePath);

    assertEquals(expectedCode, sourceFile.getCode());
    assertEquals(filePath, sourceFile.getName());
  }

  @Test
  public void testFromFileWithCharset() throws IOException {
    String filePath = "testFile.js";
    String expectedCode = "function testFunction() {\n" +
        "  return \"Hello, World!\";\n" +
        "}\n";

    Charset charset = Charsets.UTF_8;
    SourceFile sourceFile = SourceFile.fromFile(filePath, charset);

    assertEquals(expectedCode, sourceFile.getCode());
    assertEquals(filePath, sourceFile.getName());
  }

  @Test
  public void testFromCode() {
    String fileName = "testFile.js";
    String expectedCode = "function testFunction() {\n" +
        "  return \"Hello, World!\";\n" +
        "}\n";

    SourceFile sourceFile = new SourceFile(fileName);
    sourceFile.setCode(expectedCode);

    assertEquals(expectedCode, sourceFile.getCode());
    assertEquals(fileName, sourceFile.getName());
  }

  @Test
  public void testFromCodeWithOriginalPath() {
    String fileName = "testFile.js";
    String originalPath = "path/to/testFile.js";
    String expectedCode = "function testFunction() {\n" +
        "  return \"Hello, World!\";\n" +
        "}\n";

    SourceFile sourceFile = new SourceFile(fileName);
    sourceFile.setCode(expectedCode);
    sourceFile.setOriginalPath(originalPath);

    assertEquals(expectedCode, sourceFile.getCode());
    assertEquals(originalPath, sourceFile.getOriginalPath());
    assertEquals(fileName, sourceFile.getName());
  }

  @Test
  public void testFromInputStream() throws IOException {
    String fileName = "testFile.js";
    String expectedCode = "function testFunction() {\n" +
        "  return \"Hello, World!\";\n" +
        "}\n";

    File testFile = Files.createTempFile("testFile", ".js");
    Files.write(testFile, expectedCode.getBytes());

    InputStream inputStream = Files.newInputStream(testFile);
    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream);

    assertEquals(expectedCode, sourceFile.getCode());
    assertEquals(fileName, sourceFile.getName());
  }

  @Test
  public void testFromInputStreamWithOriginalPath() throws IOException {
    String fileName = "testFile.js";
    String originalPath = "path/to/testFile.js";
    String expectedCode = "function testFunction() {\n" +
        "  return \"Hello, World!\";\n" +
        "}\n";

    File testFile = Files.createTempFile("testFile", ".js");
    Files.write(testFile, expectedCode.getBytes());

    InputStream inputStream = Files.newInputStream(testFile);
    SourceFile sourceFile = SourceFile.fromInputStream(fileName, originalPath, inputStream);

    assertEquals(expectedCode, sourceFile.getCode());
    assertEquals(originalPath, sourceFile.getOriginalPath());
    assertEquals(fileName, sourceFile.getName());
  }

  @Test
  public void testFromReader() throws IOException {
    String fileName = "testFile.js";
    String expectedCode = "function testFunction() {\n" +
        "  return \"Hello, World!\";\n" +
        "}\n";

    File testFile = Files.createTempFile("testFile", ".js");
    Files.write(testFile, expectedCode.getBytes());

    Reader reader = new FileReader(testFile);
    SourceFile sourceFile = SourceFile.fromReader(fileName, reader);

    assertEquals(expectedCode, sourceFile.getCode());
    assertEquals(fileName, sourceFile.getName());
  }

  @Test
  public void testFromGenerator() {
    String fileName = "testFile.js";
    String expectedCode = "function testFunction() {\n" +
        "  return \"Hello, World!\";\n" +
        "}\n";

    SourceFile sourceFile = new SourceFile(fileName);
    SourceFile.Generator generator = () -> expectedCode;
    sourceFile.setCode(null);
    sourceFile.setGenerator(generator);

    assertEquals(expectedCode, sourceFile.getCode());
    assertEquals(fileName, sourceFile.getName());
  }

  @Test
  public void testOnDisk() throws IOException {
    String fileName = "testFile.js";
    String expectedCode = "function testFunction() {\n" +
        "  return \"Hello, World!\";\n" +
        "}\n";

    File testFile = Files.createTempFile("testFile", ".js");
    Files.write(testFile, expectedCode.getBytes());

    SourceFile sourceFile = SourceFile.fromFile(testFile);

    assertEquals(expectedCode, sourceFile.getCode());
    assertEquals(testFile.getPath(), sourceFile.getName());
  }

  @Test(expected = IOException.class)
  public void testOnDiskWithInvalidPath() throws IOException {
    SourceFile sourceFile = SourceFile.fromFile("/nonExistentFile.js");
  }
}