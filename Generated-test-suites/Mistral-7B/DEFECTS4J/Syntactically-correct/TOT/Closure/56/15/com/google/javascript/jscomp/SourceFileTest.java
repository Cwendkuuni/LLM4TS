package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.OnDisk;
import com.google.javascript.rhino.jstype.StaticSourceFile;

public class SourceFileTest {

  @Test
  public void testFromFile() throws IOException {
    String fileName = "testFile.js";
    File file = new File(fileName);
    Charset charset = Charsets.UTF_8;
    SourceFile sourceFile = SourceFile.fromFile(fileName, charset);
    assertEquals(fileName, sourceFile.getName());
    assertSame(charset, sourceFile.getCharset());
    assertEquals(Files.toString(file, charset), sourceFile.getCode());
  }

  @Test
  public void testFromCode() {
    String fileName = "testFile.js";
    String code = "function test() {}";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    assertEquals(fileName, sourceFile.getName());
    assertNull(sourceFile.getOriginalPath());
    assertEquals(code, sourceFile.getCode());
  }

  @Test
  public void testGetLine() throws IOException {
    String fileName = "testFile.js";
    File file = new File(fileName);
    Charset charset = Charsets.UTF_8;
    String code = "function test() {\n" +
        "  var a = 5;\n" +
        "  var b = 10;\n" +
        "  return a + b;\n" +
        "}";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    assertEquals("function test() {", sourceFile.getLine(1));
    assertEquals("  var a = 5;\n", sourceFile.getLine(2));
    assertEquals("  var b = 10;\n", sourceFile.getLine(3));
    assertEquals("return a + b;\n", sourceFile.getLine(4));
    assertNull(sourceFile.getLine(5));
  }

  @Test
  public void testGetRegion() throws IOException {
    String fileName = "testFile.js";
    File file = new File(fileName);
    Charset charset = Charsets.UTF_8;
    String code = "function test() {\n" +
        "  var a = 5;\n" +
        "  var b = 10;\n" +
        "  return a + b;\n" +
        "}";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    Region region = sourceFile.getRegion(4);
    assertEquals(1, region.getStartLine());
    assertEquals(4, region.getEndLine());
    assertEquals("return a + b;\n", region.getCode());
  }

  @Test
  public void testOnDisk() throws IOException {
    String fileName = "testFile.js";
    File file = new File(fileName);
    Charset charset = Charsets.UTF_8;
    SourceFile sourceFile = SourceFile.fromFile(fileName, charset);
    assertEquals(fileName, sourceFile.getName());
    assertSame(charset, sourceFile.getCharset());
    assertEquals(Files.toString(file, charset), sourceFile.getCode());

    Charset newCharset = Charsets.ISO_8859_1;
    sourceFile.setCharset(newCharset);
    assertEquals(newCharset, sourceFile.getCharset());
    assertEquals(Files.toString(file, charset), sourceFile.getCode());

    Files.delete(file);
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
  }
}