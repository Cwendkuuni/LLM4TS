package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.OnDisk;

public class SourceFileTest {

  private OnDisk sourceFile;

  @Before
  public void setUp() throws IOException {
    String filePath = "testSourceFile.js";
    File testFile = new File(filePath);
    Files.write(testFile, "function test() {\n" + "  // Test function.\n" + "}", Charsets.UTF_8);
    sourceFile = new OnDisk(testFile);
  }

  @Test
  public void testGetCode() throws IOException {
    assertEquals("function test() {\n" + "  // Test function.\n" + "}", sourceFile.getCode());
  }

  @Test(expected = IOException.class)
  public void testGetCodeWithException() throws IOException {
    File testFile = new File("nonexistentfile.js");
    SourceFile nonexistentFile = new OnDisk(testFile);
    nonexistentFile.getCode();
  }

  @Test
  public void testGetCodeReader() throws IOException {
    Reader reader = sourceFile.getCodeReader();
    String code = CharStreams.toString(reader);
    assertEquals("function test() {\n" + "  // Test function.\n" + "}", code);
  }

  @Test
  public void testGetLine() {
    String line1 = "function test() {\n";
    String line2 = "  // Test function.\n";
    assertEquals(line1, sourceFile.getLine(1));
    assertEquals(line2, sourceFile.getLine(2));
    assertNull(sourceFile.getLine(0));
    assertNull(sourceFile.getLine(3));
  }

  @Test
  public void testGetRegion() {
    String region = "function test() {\n" + "  // Test function.\n";
    Region expectedRegion = new SimpleRegion(1, 1, region);
    Region actualRegion = sourceFile.getRegion(1);
    assertSame(expectedRegion, actualRegion);
  }

  @Test
  public void testGetOriginalPath() {
    assertEquals("testSourceFile.js", sourceFile.getOriginalPath());
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String initialCode = sourceFile.getCode();
    sourceFile.clearCachedSource();
    assertNull(sourceFile.getCode());
    assertEquals(initialCode, Files.toString(new File("testSourceFile.js"), Charsets.UTF_8));
  }

  @Test
  public void testHasSourceInMemory() {
    assertTrue(sourceFile.hasSourceInMemory());
  }

  @Test
  public void testIsExtern() {
    assertFalse(sourceFile.isExtern());
  }

  @Test
  public void testFileName() {
    assertEquals("testSourceFile.js", sourceFile.getName());
  }

  @Test
  public void testGetLineOffset() {
    assertEquals(6, sourceFile.getLineOffset(1));
    assertEquals(12, sourceFile.getLineOffset(2));
  }

  @Test
  public void testGetNumLines() {
    assertEquals(2, sourceFile.getNumLines());
  }

  @Test
  public void testFindLineOffsets() {
    String code = "function test1() {\n" +
        "  // Test function 1.\n" +
        "}\n" +
        "\n" +
        "function test2() {\n" +
        "  // Test function 2.\n" +
        "}";
    SourceFile testFile = new SourceFile("testFile.js") {
      @Override
      String getCode() throws IOException {
        return code;
      }
    };
    int[] lineOffsets = testFile.lineOffsets;
    assertEquals(11, lineOffsets.length);
    assertEquals(3, lineOffsets[0]);
    assertEquals(7, lineOffsets[1]);
    assertEquals(11, lineOffsets[2]);
    assertEquals(15, lineOffsets[3]);
    assertEquals(18, lineOffsets[4]);
    assertEquals(22, lineOffsets[5]);
    assertEquals(26, lineOffsets[6]);
    assertEquals(30, lineOffsets[7]);
    assertEquals(34, lineOffsets[8]);
    assertEquals(38, lineOffsets[9]);
    assertEquals(42, lineOffsets[10]);
  }

  @After
  public void tearDown() throws IOException {
    Files.delete(new File("testSourceFile.js"));
  }
}