package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.google.common.io.Files;
import com.google.common.io.Resources;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.OnDisk;

public class SourceFileTest {

  @Test
  public void testSourceFileFromFile() throws IOException {
    String fileName = "Sample.js";
    SourceFile sourceFile = SourceFile.fromFile(fileName);
    assertNotNull(sourceFile);
    String code = sourceFile.getCode();
    assertNotNull(code);
    assertEquals(Files.toString(new File(fileName), Charsets.UTF_8), code);
  }

  @Test
  public void testSourceFileFromCode() throws IOException {
    String fileName = "Sample.js";
    String code = Files.toString(new File("Sample.java"), Charsets.UTF_8);
    SourceFile sourceFile = SourceFile.fromCode(fileName, code);
    assertNotNull(sourceFile);
    String sourceCode = sourceFile.getCode();
    assertNotNull(sourceCode);
    assertEquals(code, sourceCode);
  }

  @Test
  public void testSourceFileFromInputStream() throws IOException {
    String fileName = "Sample.js";
    File file = new File("Sample.java");
    InputStream inputStream = Resources.asByteSource(file).openStream();
    SourceFile sourceFile = SourceFile.fromInputStream(fileName, inputStream);
    assertNotNull(sourceFile);
    String sourceCode = sourceFile.getCode();
    assertNotNull(sourceCode);
    assertEquals(Files.toString(new File("Sample.js"), Charsets.UTF_8), sourceCode);
  }

  @Test
  public void testGetLine() throws IOException {
    String fileName = "Sample.js";
    SourceFile sourceFile = SourceFile.fromFile(fileName);
    assertNotNull(sourceFile);
    String line1 = sourceFile.getLine(1);
    assertNotNull(line1);
    assertEquals("// This is line 1", line1);
    String line5 = sourceFile.getLine(5);
    assertNotNull(line5);
    assertEquals("// This is line 5", line5);
    String line11 = sourceFile.getLine(11);
    assertNull(line11);
  }

  @Test
  public void testGetRegion() throws IOException {
    String fileName = "Sample.js";
    SourceFile sourceFile = SourceFile.fromFile(fileName);
    assertNotNull(sourceFile);
    Region region = sourceFile.getRegion(5);
    assertNotNull(region);
    assertEquals(5, region.getStartLineNumber());
    assertEquals(5, region.getEndLineNumber());
    assertEquals("// This is line 4\n// This is line 5\n", region.getSourceCode());
  }

  @Test
  public void testSetOriginalPath() throws IOException {
    String fileName = "Sample.js";
    SourceFile sourceFile = SourceFile.fromFile(fileName);
    assertNotNull(sourceFile);
    sourceFile.setOriginalPath("Sample.java");
    assertEquals("Sample.java", sourceFile.getOriginalPath());
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String fileName = "Sample.js";
    SourceFile sourceFile = SourceFile.fromFile(fileName);
    assertNotNull(sourceFile);
    String code = sourceFile.getCode();
    assertNotNull(code);
    sourceFile.clearCachedSource();
    String codeAfterClear = sourceFile.getCode();
    assertNull(codeAfterClear);
  }
}

class Region {
  private final int startLineNumber;
  private final int endLineNumber;
  private final String sourceCode;

  public Region(int startLineNumber, int endLineNumber, String sourceCode) {
    this.startLineNumber = startLineNumber;
    this.endLineNumber = endLineNumber;
    this.sourceCode = sourceCode;
  }

  public int getStartLineNumber() {
    return startLineNumber;
  }

  public int getEndLineNumber() {
    return endLineNumber;
  }

  public String getSourceCode() {
    return sourceCode;
  }
}