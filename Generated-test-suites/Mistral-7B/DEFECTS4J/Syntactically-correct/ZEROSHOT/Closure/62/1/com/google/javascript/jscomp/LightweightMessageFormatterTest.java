package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.CodeSnippetTestLoader;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LightweightMessageFormatterTest {

  private static final String CLASS_NAME = LightweightMessageFormatter.class.getName();

  @org.junit.Test
  public void testFormatting() throws IOException {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourcePath, int lineNumber)
          throws IOException {
        File file = new File(sourcePath);
        return SourceExcerpt.create(file.getAbsolutePath(), lineNumber);
      }
    };

    // Test without source
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("Message", null, 1, 1, null, null);
    String output = formatter.formatError(error);
    assertEquals("ERROR - Message\n", output);

    // Test with source
    LightweightMessageFormatter formatterWithSource = new LightweightMessageFormatter(source);
    JSError warning = new JSError("Message", "test.js", 1, 1, null, null);
    warning.setCharno(5);
    String source = "function test() {\n" +
        "  // some code\n" +
        "  var x = 5;\n" +
        "  if (x < 10) {\n" +
        "    // warning\n" +
        "  }\n" +
        "}";
    SourceExcerpt excerpt = new SourceExcerpt("test.js", 1, null, null);
    excerpt.setSource(source);
    output = formatterWithSource.formatWarning(warning);
    List<String> lines = CodeSnippetTestLoader.loadCodeSnippet("test_line_numbering_formatter.js")
        .getLines();
    String expected = lines.get(0);
    assertEquals(expected, output);
  }

  @org.junit.Test
  public void testFormatErrorWithSource() throws IOException {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourcePath, int lineNumber)
          throws IOException {
        File file = new File(sourcePath);
        return SourceExcerpt.create(file.getAbsolutePath(), lineNumber);
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("Message", "test.js", 1, 1, null, null);
    error.setCharno(5);
    String output = formatter.formatError(error);
    List<String> lines = CodeSnippetTestLoader.loadCodeSnippet("test_line_numbering_formatter.js")
        .getLines();
    String expected = lines.get(1);
    assertEquals(expected, output);
  }

  @org.junit.Test
  public void testFormatWarningWithSource() throws IOException {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourcePath, int lineNumber)
          throws IOException {
        File file = new File(sourcePath);
        return SourceExcerpt.create(file.getAbsolutePath(), lineNumber);
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError("Message", "test.js", 1, 1, null, null);
    warning.setCharno(5);
    String output = formatter.formatWarning(warning);
    List<String> lines = CodeSnippetTestLoader.loadCodeSnippet("test_line_numbering_formatter.js")
        .getLines();
    String expected = lines.get(2);
    assertEquals(expected, output);
  }

  @org.junit.Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("Message", null, 1, 1, null, null);
    String output = formatter.formatError(error);
    assertEquals("ERROR - Message\n", output);
  }

  @org.junit.Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("Message", null, 1, 1, null, null);
    String output = formatter.formatWarning(warning);
    assertEquals("WARNING - Message\n", output);
  }

  @org.junit.Test
  public void testFormatErrorWithExcerpt() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(LINE);
    JSError error = new JSError("Message", null, 1, 1, null, null);
    String output = formatter.formatError(error);
    assertEquals("ERROR - Message\n", output);
  }

  @org.junit.Test
  public void testFormatWarningWithExcerpt() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(LINE);
    JSError warning = new JSError("Message", null, 1, 1, null, null);
    String output = formatter.formatWarning(warning);
    assertEquals("WARNING - Message\n", output);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testInvalidExcerpt() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testNullError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(LINE);
    formatter.formatError(null);
  }

  @org.junit.Test(expected = NullPointerException.class)
  public void testNullWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(LINE);
    formatter.formatWarning(null);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testNegativeLineNumber() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(LINE);
    JSError error = new JSError("Message", null, -1, 1, null, null);
    formatter.formatError(error);
  }

  @org.junit.Test(expected = IllegalArgumentException.class)
  public void testNegativeCharno() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(LINE);
    JSError error = new JSError("Message", null, 1, -1, null, null);
    formatter.formatError(error);
  }

  @org.junit.Test
  public void testEmptySource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourcePath, int lineNumber)
          throws IOException {
        return SourceExcerpt.EMPTY;
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("Message", null, 1, 1, null, null);
    String output = formatter.formatError(error);
    assertEquals("ERROR - Message\n", output);
  }

  @org.junit.Test
  public void testEmptySourceWithExcerpt() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourcePath, int lineNumber)
          throws IOException {
        return SourceExcerpt.EMPTY;
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(LINE);
    JSError error = new JSError("Message", null, 1, 1, null, null);
    String output = formatter.formatError(error);
    assertEquals("ERROR - Message\n", output);
  }

  @org.junit.Test
  public void testFormatErrorWithLongSourceLine() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourcePath, int lineNumber)
          throws IOException {
        return new SourceExcerpt("test.js", 1, "function longLine() {" +
            "  var longVariableName = longVariableName.substring(0, 10000);" +
            "}", null);
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("Message", "test.js", 1, 1, null, null);
    error.setCharno(5000);
    String output = formatter.formatError(error);
    assertEquals("ERROR - Message\n" +
        "test.js:1:5000: ", output);
  }

  @org.junit.Test
  public void testFormatWarningWithLongSourceLine() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourcePath, int lineNumber)
          throws IOException {
        return new SourceExcerpt("test.js", 1, "function longLine() {" +
            "  var longVariableName = longVariableName.substring(0, 10000);" +
            "}", null);
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError("Message", "test.js", 1, 1, null, null);
    warning.setCharno(5000);
    String output = formatter.formatWarning(warning);
    assertEquals("WARNING - Message\n" +
        "test.js:1:5000: ", output);
  }
}

class LineNumberingFormatterTest {
  @org.junit.Test
  public void testFormatLine() {
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    String line = "function test() {\n" +
        "  // some code\n" +
        "  var x = 5;\n" +
        "  if (x < 10) {\n" +
        "    // warning\n" +
        "  }\n" +
        "}";
    int lineNumber = 1;
    String expected = "  " + lineNumber + "| ";
    String output = formatter.formatLine(line, lineNumber);
    assertEquals(expected, output);
  }

  @org.junit.Test
  public void testFormatRegion() {
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    String source = "function test() {\n" +
        "  // some code\n" +
        "  var x = 5;\n" +
        "  if (x < 10) {\n" +
        "    // warning\n" +
        "  }\n" +
        "}";
    Region region = new Region(null, 1, null, null, null);
    region.setSourceExcerpt(source);
    String output = formatter.formatRegion(region);
    List<String> lines = CodeSnippetTestLoader.loadCodeSnippet("test_line_numbering_formatter.js")
        .getLines();
    String expected = lines.get(0);
    assertEquals(expected, output);
  }
}