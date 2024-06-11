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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * Tests for the {@link SourceFile} class.
 */
public class SourceFileTest {

  /**
   * Tests loading source code from disk.
   */
  @Test
  public void testLoadSourceFromDisk() throws IOException {
    SourceFile sourceFile = SourceFile.fromFile("test.js");
    String code = sourceFile.getCode();
    assertEquals("test content", code);

    String line = sourceFile.getLine(10);
    assertEquals("test content line 10", line);

    Region region = sourceFile.getRegion(11);
    assertEquals("test content line 10, 11, 12, 13, 14", region.getCode());
  }

  /**
   * Tests loading source code from an input stream.
   */
  @Test
  public void testLoadSourceFromInputStream() throws IOException {
    File tmpFile = File.createTempFile("test", null);
    try (InputStream is = Files.newInputStream(tmpFile, Charsets.UTF_8);
         SourceFile sourceFile = SourceFile.fromInputStream("test.js", is)) {
      String code = sourceFile.getCode();
      assertEquals("test content", code);

      String line = sourceFile.getLine(10);
      assertEquals("test content line 10", line);

      Region region = sourceFile.getRegion(11);
      assertEquals("test content line 10, 11, 12, 13, 14", region.getCode());

      Files.delete(tmpFile);
    }
  }

  /**
   * Tests loading source code from a preloaded string.
   */
  @Test
  public void testLoadSourceFromCode() {
    String code = "test content";
    SourceFile sourceFile = SourceFile.fromCode("test.js", code);
    assertEquals(code, sourceFile.getCode());

    String line = sourceFile.getLine(10);
    assertNull(line);

    Region region = sourceFile.getRegion(11);
    assertNull(region);
  }

  /**
   * Tests loading source code from an {@link StaticSourceFile}.
   */
  @Test
  public void testLoadSourceFromStaticSourceFile() {
    String code = "test content";
    SourceFile sourceFile = new StaticSourceFile("test.js", code) {
      @Override
      public String getCode() {
        return super.getCode();
      }

      @Override
      public int getLineOffset(int line) {
        throw new UnsupportedOperationException();
      }

      @Override
      public int getNumLines() {
        throw new UnsupportedOperationException();
      }

      @Override
      public Region getRegion(int line) {
        throw new UnsupportedOperationException();
      }

      @Override
      public boolean isExtern() {
        return false;
      }

      @Override
      public String getName() {
        return "test.js";
      }
    };

    assertEquals(code, sourceFile.getCode());

    String line = sourceFile.getLine(10);
    assertNull(line);

    Region region = sourceFile.getRegion(11);
    assertNull(region);
  }

  /**
   * Tests setting the original path.
   */
  @Test
  public void testSetOriginalPath() {
    SourceFile sourceFile = new SourceFile("test.js") {
      @Override
      public String getCode() {
        throw new UnsupportedOperationException();
      }
    };

    sourceFile.setOriginalPath("path/to/test.js");

    assertEquals("path/to/test.js", sourceFile.getOriginalPath());
  }

  /**
   * Tests generating source code.
   */
  @Test
  public void testGenerateSourceCode() {
    String fileName = "test.js";
    String code = "test content";
    Generator generator = () -> code;

    SourceFile sourceFile = SourceFile.fromGenerator(fileName, generator);
    assertEquals(code, sourceFile.getCode());

    String line = sourceFile.getLine(10);
    assertNull(line);

    Region region = sourceFile.getRegion(11);
    assertNull(region);
  }
}