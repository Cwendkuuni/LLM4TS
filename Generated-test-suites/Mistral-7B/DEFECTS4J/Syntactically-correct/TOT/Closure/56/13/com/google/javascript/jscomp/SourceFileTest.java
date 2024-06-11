package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;

import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Tests for the SourceFile class.
 */
public class SourceFileTest {

  /**
   * Test creating a SourceFile from a file.
   */
  @Test
  public void testFromFile() throws IOException {
    String fileName = "TestSourceFile.java";
    File testFile = new File(fileName);
    Files.write(testFile, "public class TestSourceFile {}", Charsets.UTF_8);

    SourceFile sf = SourceFile.fromFile(fileName);
    assertEquals("TestSourceFile", sf.getName());
    assertEquals("TestSourceFile.java", sf.getOriginalPath());
    assertEquals("public class TestSourceFile {\n", sf.getCode());

    Files.delete(testFile);
  }

  /**
   * Test creating a SourceFile from a preloaded string.
   */
  @Test
  public void testFromCode() {
    SourceFile sf = SourceFile.fromCode("TestSourceFile.java",
        "public class TestSourceFile {}", null);
    assertEquals("TestSourceFile", sf.getName());
    assertEquals("TestSourceFile.java", sf.getOriginalPath());
    assertEquals("public class TestSourceFile {\n", sf.getCode());
  }

  /**
   * Test getting the line number for a given line offset.
   */
  @Test
  public void testGetLineOffset() throws IOException {
    String code = "class Test {\n" +
        "  int a = 1;\n" +
        "  int b = 2;\n" +
        "  int c = a + b;\n" +
        "}";

    SourceFile sf = new SourceFile("Test.java") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    int lineNumber = sf.getLineOffset(3);
    assertEquals(3, lineNumber);
  }

  /**
   * Test getting the line for a given line number.
   */
  @Test
  public void testGetLine() throws IOException {
    String code = "class Test {\n" +
        "  int a = 1;\n" +
        "  int b = 2;\n" +
        "  int c = a + b;\n" +
        "}";

    SourceFile sf = new SourceFile("Test.java") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    String line = sf.getLine(3);
    assertEquals("  int c = a + b;\n", line);
  }

  /**
   * Test getting a region around a given line number.
   */
  @Test
  public void testGetRegion() throws IOException {
    String code = "class Test {\n" +
        "  int a = 1;\n" +
        "  int b = 2;\n" +
        "  int c = a + b;\n" +
        "}";

    SourceFile sf = new SourceFile("Test.java") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    Region region = sf.getRegion(3);
    assertEquals(1, region.getStartLineNumber());
    assertEquals(3, region.getEndLineNumber());
    assertEquals("  int c = a + b;\n", region.getSource());
  }

  /**
   * Test getting a region around a given line number with source offset.
   */
  @Test
  public void testGetRegionWithOffset() throws IOException {
    String code = "class Test {\n" +
        "  int a = 1;\n" +
        "  int b = 2;\n" +
        "  int c = a + b;\n" +
        "}";

    SourceFile sf = new SourceFile("Test.java") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    int startOffset = 10;
    int lineNumber = 3;
    Region region = sf.getRegion(lineNumber, startOffset);
    assertEquals(1, region.getStartLineNumber());
    assertEquals(3, region.getEndLineNumber());
    assertEquals("  int c = a + b;\n", region.getSource());
    assertEquals("  int c = a + b;\n".substring(startOffset), region.getSourceSubstring(0, 5));
  }
}