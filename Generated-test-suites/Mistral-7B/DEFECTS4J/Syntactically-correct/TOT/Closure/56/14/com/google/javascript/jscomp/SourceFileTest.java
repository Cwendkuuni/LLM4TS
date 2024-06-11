package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;

public class SourceFileTest {

  @Test
  public void testFromFile() throws IOException {
    String fileName = "testSourceFile.js";
    String code = "function test() {\n" + "  print('Hello World');\n" + "}";
    File file = new File(fileName);
    Files.write(file, code.getBytes(Charsets.UTF_8));

    SourceFile sourceFile = SourceFile.fromFile(fileName);
    assertNotNull(sourceFile);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());

    Files.delete(file);
  }

  @Test(expected = IOException.class)
  public void testFromFileWithInvalidFileName() throws IOException {
    String fileName = "/test/../SourceFileTest.java";
    SourceFile sourceFile = SourceFile.fromFile(fileName);
  }

  @Test
  public void testFromCode() {
    String fileName = "testSourceFile.js";
    String code = "function test() {\n" + "  print('Hello World');\n" + "}";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    assertNotNull(sourceFile);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFromCodeWithNullCode() {
    SourceFile sourceFile = SourceFile.fromCode(null, "testCode");
  }

  @Test
  public void testFromCodeWithEmptyCode() {
    String fileName = "testSourceFile.js";
    SourceFile sourceFile = SourceFile.fromCode(fileName, "");
    assertNotNull(sourceFile);
    assertEquals(fileName, sourceFile.getName());
    assertNull(sourceFile.getCode());
  }

  @Test
  public void testGetLine() throws IOException {
    String fileName = "testSourceFile.js";
    String code = "function test() {\n" + "  print('Hello World');\n" + "}";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    String line1 = sourceFile.getLine(1);
    String line3 = sourceFile.getLine(3);

    assertEquals("function test() {\n", line1);
    assertNull(line3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetLineWithInvalidLineNumber() throws IOException {
    String fileName = "testSourceFile.js";
    String code = "function test() {\n" + "  print('Hello World');\n" + "}";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    sourceFile.getLine(0);
  }

  @Test
  public void testGetRegion() throws IOException {
    String fileName = "testSourceFile.js";
    String code = "function test() {\n" +
        "  print('Hello World');\n" +
        "  print('Goodbye World');\n" +
        "}";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    Region region = sourceFile.getRegion(3);

    assertEquals(3, region.getStartLineNumber());
    assertEquals(3, region.getEndLineNumber());
    assertEquals("  print('Goodbye World');\n", region.getCode());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetRegionWithInvalidLineNumber() throws IOException {
    String fileName = "testSourceFile.js";
    String code = "function test() {\n" +
        "  print('Hello World');\n" +
        "  print('Goodbye World');\n" +
        "}";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    sourceFile.getRegion(0);
  }
}