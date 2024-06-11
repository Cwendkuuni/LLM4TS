package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.testing.Regions;

import org.junit.Test;

/**
 * Unit tests for {@link LightweightMessageFormatter}.
 *
 */
public class LightweightMessageFormatterTest {

  private static final String LINE_EXCERPT = " 123| foo = 42;";

  @Test
  public void testFormatError() {
    SourceExcerptProvider sourceProvider = mock(SourceExcerptProvider.class);
    JSError error = new JSError("TestError", "test.js", 5, 10, "Error message");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        sourceProvider, LightweightMessageFormatter.LINE);
    when(sourceProvider.getExcerpt(LINE)).thenReturn(LINE_EXCERPT);

    String actual = formatter.formatError(error);
    String expected = String.format(
        "5: Error - Error message%n 123| foo = 42;", error.lineNumber);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarning() {
    SourceExcerptProvider sourceProvider = mock(SourceExcerptProvider.class);
    JSError warning = new JSError("TestWarning", "test.js", 5, 10, "Warning message");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        sourceProvider, LightweightMessageFormatter.LINE);
    when(sourceProvider.getExcerpt(LINE)).thenReturn(LINE_EXCERPT);

    String actual = formatter.formatWarning(warning);
    String expected = String.format(
        "5: Warning - Warning message%n 123| foo = 42;", error.lineNumber);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("TestError", null, 5, 10, "Error message");

    String actual = formatter.formatError(error);
    String expected = "5: Error - Error message";
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithEmptySource() {
    SourceExcerptProvider sourceProvider = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        sourceProvider, LightweightMessageFormatter.LINE);
    JSError error = new JSError("TestError", "test.js", 5, 10, "Error message");
    when(sourceProvider.getExcerpt(LINE)).thenReturn("");

    String actual = formatter.formatError(error);
    String expected = "5: Error - Error message\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithRegion() {
    SourceExcerptProvider sourceProvider = mock(SourceExcerptProvider.class);
    JSError error = new JSError("TestError", "test.js", 5, 10, "Error message") {
      @Override
      public int getSourceStartPosition() {
        return 20;
      }

      @Override
      public int getSourceEndPosition() {
        return 31;
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        sourceProvider, LightweightMessageFormatter.LINE);
    when(sourceProvider.getExcerpt(LINE)).thenReturn(LINE_EXCERPT);

    String actual = formatter.formatError(error);
    String expected = String.format(
        "5: Error - Error message%n 123| foo = 42;", error.lineNumber);
    assertEquals(expected, actual);

    // test region format
    String regionCode = " 12| function test() {\n" +
                        " 13|   if (x > 42) {\n" +
                        " 14|     alert('bar');\n" +
                        " 15|   }\n" +
                        " 16| }";
    Region region = Regions.region("test.js", 12, 20, 16, 32);
    when(sourceProvider.getExcerpt(region)).thenReturn(regionCode);

    actual = formatter.formatError(error);
    expected = String.format(
        "5: Error - Error message%n 12| function test() {\n" +
        " 12| function test() {\n" +
        " 13|   if (x > 42) {\n" +
        " 13|     alert('bar');\n" +
        " 14|   }\n" +
        " 15| }%n 123| foo = 42;", error.lineNumber);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithRegionLineMismatch() {
    SourceExcerptProvider sourceProvider = mock(SourceExcerptProvider.class);
    JSError error = new JSError("TestError", "test.js", 5, 10, "Error message") {
      @Override
      public int getSourceStartPosition() {
        return 20;
      }

      @Override
      public int getSourceEndPosition() {
        return 31;
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        sourceProvider, LightweightMessageFormatter.LINE);
    when(sourceProvider.getExcerpt(LINE)).thenReturn(LINE_EXCERPT);

    String actual = formatter.formatError(error);
    String expected = String.format(
        "5: Error - Error message%n 123| foo = 42;", error.lineNumber);
    assertEquals(expected, actual);

    // test region format with line number mismatch
    String regionCode = " 12| function test() {\n" +
                        " 13|   if (x > 42) {\n" +
                        " 14|     alert('bar');\n" +
                        " 15|   }\n" +
                        " 16| }";
    Region region = Regions.region("test.js", 12, 20, 15, 32);
    when(sourceProvider.getExcerpt(region)).thenReturn(regionCode);

    actual = formatter.formatError(error);
    expected = String.format(
        "5: Error - Error message%n 12| function test() {\n" +
        " 13|   if (x > 42) {\n" +
        " 13|     alert('bar');\n" +
        " 14|   }\n" +
        " 15| }%n 123| foo = 42;", error.lineNumber);
    assertEquals(expected, actual);
  }
}