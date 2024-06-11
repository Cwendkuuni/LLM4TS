package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;

/**
 * Tests for the SourceFile class.
 */
public class SourceFileTest {

  @Test
  public void testSourceFileFromFile() throws IOException {
    String fileName = "testSourceFile.js";
    File file = new File(fileName);
    SourceFile sourceFile = new SourceFile(fileName);

    // Test getting code from file
    String expectedCode = Files.toString(file, Charsets.UTF_8);
    String actualCode = sourceFile.getCode();
    assertEquals(expectedCode, actualCode);

    // Test getting line number and offset
    int lineNumber = 5;
    int lineOffset = sourceFile.getLineOffset(lineNumber);
    int expectedLineOffset = 32; // hardcoded value based on the test file content
    assertEquals(expectedLineOffset, lineOffset);

    // Test getting region
    int regionLength = 3;
    Region region = sourceFile.getRegion(lineNumber);
    String expectedRegion = "function testFunction() {\n  var a = 1;\n  var b = 2;\n  var c = a + b;\n}"; // hardcoded value based on the test file content
    assertEquals(expectedRegion, region.getCode());
    assertEquals(lineNumber, region.getLineNumber());
  }

  @Test
  public void testSourceFileFromCode() {
    String fileName = "testSourceFile.js";
    String code = "function testFunction() {\n  var a = 1;\n  var b = 2;\n  var c = a + b;\n}";
    SourceFile sourceFile = new SourceFile(fileName, code);

    // Test getting code
    String actualCode = sourceFile.getCode();
    String expectedCode = code;
    assertEquals(expectedCode, actualCode);

    // Test getting line number and offset (not testing edge cases here)
    int lineNumber = 5;
    int lineOffset = sourceFile.getLineOffset(lineNumber);
    int expectedLineOffset = 24; // hardcoded value based on the test file content
    assertEquals(expectedLineOffset, lineOffset);

    // Test getting region
    int regionLength = 3;
    Region region = sourceFile.getRegion(lineNumber);
    String expectedRegion = "function testFunction() {\n  var a = 1;\n  var b = 2;\n  var c = a + b;\n}";
    assertEquals(expectedRegion, region.getCode());
    assertEquals(lineNumber, region.getLineNumber());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileWithEmptyFileName() {
    new SourceFile("");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileWithNullFileName() {
    new SourceFile(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSourceFileWithNullCode() {
    new SourceFile("testSourceFile.js", null);
  }
}