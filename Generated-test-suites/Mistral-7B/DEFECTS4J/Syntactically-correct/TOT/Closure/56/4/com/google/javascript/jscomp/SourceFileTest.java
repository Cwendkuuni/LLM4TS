package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.jstype.StaticSourceFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.junit.Test;

/**
 * Unit tests for {@link SourceFile} class.
 */
public class SourceFileTest {

  @Test
  public void testFromFile() throws IOException {
    String fileName = "SourceFileTest.java";
    Charset charset = Charsets.UTF_8;

    SourceFile sourceFile = SourceFile.fromFile(fileName, charset);

    String code = sourceFile.getCode();
    assertEquals("Expected SourceFile code to match original file content.",
        Files.toString(new File(fileName), charset), code);
  }

  @Test
  public void testFromInputStream() throws IOException {
    String fileName = "SourceFileTest.java";
    InputStream inputStream = Files.newInputStream(new File(fileName));

    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream);

    String code = sourceFile.getCode();
    assertEquals("Expected SourceFile code to match original file content.",
        Files.toString(new File(fileName), Charsets.UTF_8), code);
  }

  @Test
  public void testFromReader() throws IOException {
    String fileName = "SourceFileTest.java";
    Reader reader = Files.newReader(new File(fileName), Charsets.UTF_8);

    SourceFile sourceFile = SourceFile.fromReader(fileName, reader);

    String code = sourceFile.getCode();
    assertEquals("Expected SourceFile code to match original file content.",
        Files.toString(new File(fileName), Charsets.UTF_8), code);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullFileName() {
    SourceFile sourceFile = new SourceFile(null);
  }

  @Test
  public void testEmptyFileName() {
    SourceFile sourceFile = new SourceFile("");

    assertEquals("Expected empty SourceFile name.", "", sourceFile.getName());
  }

  @Test
  public void testGetLine() throws IOException {
    String fileName = "SourceFileTest.java";
    Charset charset = Charsets.UTF_8;

    SourceFile sourceFile = SourceFile.fromFile(fileName, charset);

    String code = sourceFile.getCode();
    String expectedLine = "public void testEmptyFileName() {";

    String line = sourceFile.getLine(18); // Line number 18 is arbitrary

    assertEquals("Expected line content.", expectedLine, line);
  }

  @Test
  public void testGetRegion() throws IOException {
    String fileName = "SourceFileTest.java";
    Charset charset = Charsets.UTF_8;

    SourceFile sourceFile = SourceFile.fromFile(fileName, charset);

    Region region = sourceFile.getRegion(20);

    String expectedRegionContent = "public void testGetLine() throws IOException {";

    assertEquals("Expected region content.", expectedRegionContent, region.getContent());
    assertEquals("Expected region start line.", 19, region.getStartLineNumber());
    assertEquals("Expected region end line.", 20, region.getEndLineNumber());
  }
}