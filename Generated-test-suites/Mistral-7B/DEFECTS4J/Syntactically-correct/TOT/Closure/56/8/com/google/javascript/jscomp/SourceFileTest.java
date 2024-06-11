package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.rhino.jstype.StaticSourceFile;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Tests for {@link SourceFile} class.
 */
public class SourceFileTest {

  @Test
  public void testGetLine() throws IOException {
    String code = "class A {\n" +
        "  method1() {\n" +
        "    // some code\n" +
        "  }\n" +
        "}\n";

    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    assertEquals("class A {\n", sourceFile.getLine(1));
    assertEquals("  method1() {\n    // some code\n  }\n}", sourceFile.getLine(3));
    assertNull(sourceFile.getLine(0));
    assertNull(sourceFile.getLine(5));
  }

  @Test
  public void testGetRegion() throws IOException {
    String code = "class A {\n" +
        "  method1() {\n" +
        "    // some code\n" +
        "  }\n" +
        "}\n";

    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    String expected = "class A {\n  method1() {\n    // some code\n  }\n}";

    Region region = sourceFile.getRegion(3);
    assertEquals(expected, region.getCode());
    assertEquals(3, region.getLineNumber());
  }

  @Test
  public void testFromFile() throws IOException {
    String filePath = "test.js";
    String expectedCode = "function foo() {\n" +
        "  // some code\n" +
        "}";

    SourceFile sourceFile = SourceFile.fromFile(filePath, Charsets.UTF_8);

    assertEquals("test.js", sourceFile.getName());
    assertEquals(expectedCode, sourceFile.getCode());
  }

  @Test
  public void testFromInputStream() throws IOException {
    String filePath = "test.js";
    String expectedCode = "function foo() {\n" +
        "  // some code\n" +
        "}";

    File testFile = File.createTempFile("test", null);
    Files.write(testFile, expectedCode.getBytes(Charsets.UTF_8));

    InputStream inputStream = Files.newInputStream(testFile.toPath());

    SourceFile sourceFile = SourceFile.fromInputStream(filePath, inputStream);

    assertEquals("test.js", sourceFile.getName());
    assertEquals(expectedCode, sourceFile.getCode());

    Files.delete(testFile.toPath());
  }

  @Test
  public void testFromCode() {
    String fileName = "test.js";
    String code = "function foo() {\n" +
        "  // some code\n" +
        "}";

    SourceFile sourceFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    assertEquals(fileName, sourceFile.getName());
    assertEquals(code, sourceFile.getCode());

    assertEquals(StaticSourceFile.class, sourceFile.getClass());
  }

  @Test
  public void testClearCachedSource() throws IOException {
    String fileName = "test.js";
    String code = "function foo() {\n" +
        "  // some code\n" +
        "}";

    SourceFile sourceFile = new SourceFile(fileName) {
      @Override
      public String getCode() throws IOException {
        return code;
      }
    };

    assertEquals(code, sourceFile.getCode());

    sourceFile.clearCachedSource();

    assertNull(sourceFile.getCode());
  }
}