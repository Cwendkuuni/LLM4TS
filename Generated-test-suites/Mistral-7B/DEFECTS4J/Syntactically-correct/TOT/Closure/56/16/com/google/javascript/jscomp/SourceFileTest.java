package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.OnDisk;

public class SourceFileTest {

  @Test
  public void testFromFile() throws IOException {
    String fileName = "testFile.js";
    File file = new File(fileName);
    Charset charset = Charsets.UTF_8;
    SourceFile sourceFile = SourceFile.fromFile(fileName, charset);
    String code = sourceFile.getCode();
    assertSame(file.getAbsolutePath(), sourceFile.getName());
    assertEquals(Files.toString(file, charset), code);
  }

  @Test
  public void testFromCode() {
    String fileName = "testFile.js";
    String code = "function test(){\n  return 1;\n}";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
  }

  @Test
  public void testFromInputStream() throws IOException {
    String fileName = "testFile.js";
    File file = new File(fileName);
    Charset charset = Charsets.UTF_8;
    String code = Files.toString(file, charset);
    InputStream inputStream = new FileInputStream(file);
    SourceFile sourceFile = SourceFile.fromInputStream(fileName, charset, inputStream);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
    inputStream.close();
  }

  @Test
  public void testFromReader() throws IOException {
    String fileName = "testFile.js";
    File file = new File(fileName);
    Charset charset = Charsets.UTF_8;
    String code = Files.toString(file, charset);
    Reader reader = new FileReader(file);
    SourceFile sourceFile = SourceFile.fromReader(fileName, charset, reader);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());
    reader.close();
  }

  @Test
  public void testFindLineOffsets() throws IOException {
    String fileName = "testFile.js";
    String code = "function test(){\n  return 1;\n}function test2(){\n  return 2;\n}";
    SourceFile sourceFile = new OnDisk(fileName, Charsets.UTF_8) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    assertEquals(2, sourceFile.getNumLines());
    assertEquals(0, sourceFile.getLineOffset(1));
    assertEquals(11, sourceFile.getLineOffset(2));
  }

  @Test
  public void testGetRegion() throws IOException {
    String fileName = "testFile.js";
    String code = "function test(){\n  return 1;\n}function test2(){\n  return 2;\n}";
    SourceFile sourceFile = new OnDisk(fileName, Charsets.UTF_8) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };
    assertEquals("function test(){\n  return 1;\n}", sourceFile.getRegion(1).toString());
    assertEquals("function test2(){\n  return 2;\n}", sourceFile.getRegion(2).toString());
  }

  @Test
  public void testIsExtern() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    assertFalse(sourceFile.isExtern());
    sourceFile.setIsExtern(true);
    assertTrue(sourceFile.isExtern());
  }

  @Test
  public void testName() {
    SourceFile sourceFile = new SourceFile("testFile.js");
    assertEquals("testFile.js", sourceFile.getName());
  }
}