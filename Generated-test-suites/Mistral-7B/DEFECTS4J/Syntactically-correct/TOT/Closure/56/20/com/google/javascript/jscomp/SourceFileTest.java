package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.jstype.StaticSourceFile;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import org.junit.Test;

/**
 * Test for SourceFile class.
 */
public class SourceFileTest {

  @Test
  public void testSourceFileFromFile() throws IOException {
    String fileName = "TestSourceFile.java";
    File file = new File(fileName);

    SourceFile sourceFile = SourceFile.fromFile(fileName);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(file.getAbsolutePath(), sourceFile.getOriginalPath());
    assertEquals(StaticSourceFile.Kind.JAVA_SOURCE, sourceFile.getKind());

    String code = sourceFile.getCode();
    assertTrue(code.length() > 0);
    assertEquals(Files.toString(file), code);
  }

  @Test
  public void testSourceFileFromCode() {
    String fileName = "TestSourceFile.java";
    String originalPath = "/path/to/common/source";
    String code = "public class TestSourceFile {}";

    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    assertEquals(fileName, sourceFile.getName());
    assertEquals(originalPath, sourceFile.getOriginalPath());
    assertEquals(StaticSourceFile.Kind.JAVA_SOURCE, sourceFile.getKind());

    String testCode = sourceFile.getCode();
    assertEquals(code, testCode);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidFileName() {
    String fileName = "";
    SourceFile sourceFile = new SourceFile(fileName);
  }

  @Test
  public void testGetLine() throws IOException {
    String fileName = "TestSourceFile.java";
    String code = "line1\nline2\nline3";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    String line1 = sourceFile.getLine(1);
    assertEquals("line1", line1);
    String line2 = sourceFile.getLine(2);
    assertEquals("line2", line2);
    String line3 = sourceFile.getLine(3);
    assertEquals("line3", line3);

    String line4 = sourceFile.getLine(4);
    assertNull(line4);
  }

  @Test
  public void testGetRegion() throws IOException {
    String fileName = "TestSourceFile.java";
    String code = "line1\nline2\nline3\nline4";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    Region region = sourceFile.getRegion(3);
    assertEquals(2, region.getStartLineNumber());
    assertEquals(3, region.getEndLineNumber());
    assertEquals("line2\nline3", region.getCode());

    region = sourceFile.getRegion(1);
    assertEquals(1, region.getStartLineNumber());
    assertEquals(1, region.getEndLineNumber());
    assertEquals("line1", region.getCode());

    region = sourceFile.getRegion(5);
    assertNull(region);
  }

  @Test
  public void testGetLineOffset() throws IOException {
    String fileName = "TestSourceFile.java";
    String code = "line1\nline2";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    int lineNumber = 2;
    int lineOffset = sourceFile.getLineOffset(lineNumber);
    assertEquals(-1, lineOffset);

    lineNumber = 1;
    lineOffset = sourceFile.getLineOffset(lineNumber);
    assertEquals(0, lineOffset);

    lineNumber = 2;
    lineOffset = sourceFile.getLineOffset(lineNumber);
    assertEquals(code.length() + 1, lineOffset);
  }

  @Test
  public void testGetNumLines() throws IOException {
    String fileName = "TestSourceFile.java";
    String code = "line1\nline2\nline3";
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);

    int numLines = sourceFile.getNumLines();
    assertEquals(3, numLines);
  }
}