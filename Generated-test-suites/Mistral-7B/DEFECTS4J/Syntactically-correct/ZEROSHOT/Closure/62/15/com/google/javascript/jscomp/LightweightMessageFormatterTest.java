package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.JSError.SourceType;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider.SourcePosition;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.testing.CodeSnippetSourceExcerptProvider;
import com.google.javascript.jscomp.testing.TestSourceFile;

import org.junit.Test;

/**
 * Test class for LightweightMessageFormatter.
 */
public class LightweightMessageFormatterTest {

  private static final String TEST_FILE = "TestLightweightMessageFormatter.java";
  private static final String TEST_CODE =
      "function foo() {"
      + "  if (true) {"
      + "    alert('bar');"
      + "  }"
      + "}";

  @Test
  public void testFormatErrorWithSource() {
    SourceExcerptProvider source = new CodeSnippetSourceExcerptProvider(
        new TestSourceFile(TEST_FILE, TEST_CODE));
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    JSError error = new JSError(
        1, SourceType.JS_SOURCE, "TestFile.java", 3, 15, "TypeError",
        "Expected identifier but found 'alert'",
        "expected 'identifer'", TEST_CODE);
    String actual = formatter.formatError(error);
    String expected = "3:15 TypeError - Expected identifier but found 'alert'\n"
        + "    if (true) {"
        + "      alert('bar');"
        + "    }";
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError(
        1, SourceType.JS_SOURCE, null, 3, 15, "TypeError",
        "Expected identifier but found 'alert'",
        "expected 'identifer'", TEST_CODE);
    String actual = formatter.formatError(error);
    String expected = "3: TypeError - Expected identifier but found 'alert'";
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningWithSource() {
    SourceExcerptProvider source = new CodeSnippetSourceExcerptProvider(
        new TestSourceFile(TEST_FILE, TEST_CODE));
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    JSError warning = new JSError(
        1, SourceType.JS_SOURCE, "TestFile.java", 3, 15, "DeprecationWarning",
        "Using 'alert' is deprecated.", null, TEST_CODE);
    String actual = formatter.formatWarning(warning);
    String expected = "3:15 DeprecationWarning - Using 'alert' is deprecated.\n"
        + "    if (true) {"
        + "      alert('bar');"
        + "    }";
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError(
        1, SourceType.JS_SOURCE, null, 3, 15, "DeprecationWarning",
        "Using 'alert' is deprecated.", null, TEST_CODE);
    String actual = formatter.formatWarning(warning);
    String expected = "3: DeprecationWarning - Using 'alert' is deprecated.";
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatErrorWithRegion() {
    SourceExcerptProvider source = new CodeSnippetSourceExcerptProvider(
        new TestSourceFile(TEST_FILE, TEST_CODE));
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    JSError error = new JSError(
        1, SourceType.JS_SOURCE, "TestFile.java", 3, 15, "TypeError",
        "Expected identifier but found 'alert'",
        "expected 'identifer'", TEST_CODE);
    Region region = new Region(new SourcePosition(3, 14), new SourcePosition(3, 20));
    error.setSourceRange(region);
    String actual = formatter.formatError(error);
    String expected = "3:15 TypeError - Expected identifier but found 'alert'\n"
        + "    if (tru";
    // The region does not include the 'e' in 'true'
    assertEquals(expected, actual.substring(0, actual.length() - 2));
  }

  @Test
  public void testFormatErrorWithLargeRegion() {
    SourceExcerptProvider source = new CodeSnippetSourceExcerptProvider(
        new TestSourceFile(TEST_FILE, TEST_CODE));
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    JSError error = new JSError(
        1, SourceType.JS_SOURCE, "TestFile.java", 3, 1, "TypeError",
        "Expected identifier but found 'alert'",
        "expected 'identifer'", TEST_CODE);
    Region region = new Region(new SourcePosition(1, 0), new SourcePosition(5, 30));
    error.setSourceRange(region);
    String actual = formatter.formatError(error);
    String expected = "1:\n"
        + "1: | function foo() {"
        + "2: |   if (true) {"
        + "3:1 |     alert('bar');"
        + "4: |   }"
        + "5: | }";
    assertEquals(expected, actual);
  }

  private static class Region {
    private final SourcePosition begin;
    private final SourcePosition end;

    public Region(SourcePosition begin, SourcePosition end) {
      this.begin = begin;
      this.end = end;
    }

    public int getBeginningLineNumber() {
      return begin.getLineNumber();
    }

    public int getEndingLineNumber() {
      return end.getLineNumber();
    }

    public String getSourceExcerpt() {
      Preconditions.checkState(begin.getFileNullOrSame(end.getFile()),
          "Unexpected different files for region start and end.");
      return getSource().getLineContent(begin.getFile(), begin.getLineNumber());
    }
  }

  private static SourceExcerptProvider getSource() {
    return mock(SourceExcerptProvider.class);
  }
}