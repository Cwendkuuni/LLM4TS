package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceFile.OnDisk;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void testOnDiskSourceFile() throws IOException {
    String fileName = "test.js";
    File inputFile = new File(fileName);

    // Create an OnDisk SourceFile from a real file
    SourceFile sourceFile = new OnDisk(inputFile);

    // Verify that the source file has the correct name and content
    assertEquals(fileName, sourceFile.getName());
    assertEquals(Files.toString(inputFile), sourceFile.getCode());

    // Verify that the source file has the correct line and character information
    int lineNumber = 4;
    int characterNumber = 5;
    assertEquals("Test line.", sourceFile.getLine(lineNumber).substring(characterNumber));

    // Verify that a region around the line number contains the correct content
    Region region = sourceFile.getRegion(lineNumber);
    assertEquals("Test line 1\nTest line 2\nTest line 3\nTest line.", region.getCode());

    // Verify that an attempt to access a non-existent line number returns null
    assertNull(sourceFile.getLine(-1));

    // Verify that the source file can be cleared and regenerated
    sourceFile.clearCachedSource();
    assertEquals(null, sourceFile.getCode());
    sourceFile.setCode(Files.toString(inputFile));
    assertEquals(Files.toString(inputFile), sourceFile.getCode());
  }

  @Test
  public void testPreloadedSourceFile() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n return 42;\n}";

    // Create a Preloaded SourceFile from a string
    SourceFile sourceFile = new SourceFile.Preloaded(fileName, code);

    // Verify that the source file has the correct name and content
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());

    // Verify that the source file has the correct line and character information
    int lineNumber = 1;
    int characterNumber = 1;
    assertEquals("function test() {\n", sourceFile.getLine(lineNumber).substring(characterNumber));

    // Verify that a region around the line number contains the correct content
    Region region = sourceFile.getRegion(lineNumber);
    assertEquals("function test() {\n return 42;\n}", region.getCode());

    // Verify that an attempt to access a non-existent line number returns null
    assertNull(sourceFile.getLine(-1));
  }

  @Test
  public void testGeneratedSourceFile() throws IOException {
    String fileName = "test.js";
    String code = "function test() {\n return 42;\n}";

    // Create a Generated SourceFile from a string generator
    SourceFile sourceFile = new SourceFile.Generated(fileName, () -> code);

    // Verify that the source file has the correct name and content
    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());

    // Verify that the source file has the correct line and character information
    int lineNumber = 1;
    int characterNumber = 1;
    assertEquals("function test() {\n", sourceFile.getLine(lineNumber).substring(characterNumber));

    // Verify that a region around the line number contains the correct content
    Region region = sourceFile.getRegion(lineNumber);
    assertEquals("function test() {\n return 42;\n}", region.getCode());

    // Verify that an attempt to access a non-existent line number returns null
    assertNull(sourceFile.getLine(-1));

    // Verify that the source file can be cleared and regenerated
    sourceFile.clearCachedSource();
    assertEquals(null, sourceFile.getCode());
    sourceFile.setCode(() -> code);
    assertEquals(code, sourceFile.getCode());
  }
}