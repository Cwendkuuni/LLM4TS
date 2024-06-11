package com.google.javascript.jscomp.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.CodeSnippet;
import com.google.javascript.jscomp.testing.FormattingTestBase;
import com.google.javascript.jscomp.testing.SourceExcerptProviderTestUtil;

import java.util.List;

public class LightweightMessageFormatterTest extends FormattingTestBase {

  private static final String LINE_NUMBER = "123";
  private static final String SOURCE_NAME = "MyFile.js";

  @Override
  public AbstractMessageFormatter createFormatter() {
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getSource(SOURCE_NAME)).thenReturn(new CodeSnippet("function foo() { }"));
    return new LightweightMessageFormatter(sourceExcerptProvider, LightweightMessageFormatter.LINE);
  }

  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError(null, null, null, null, null, null, null, null, CheckLevel.ERROR,
        "Error message", 1, 1);
    assertEquals("ERROR - Error message\n", formatter.formatError(error));
  }

  public void testFormatErrorWithSource() {
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getSource(SOURCE_NAME)).thenReturn(new CodeSnippet("function foo() { }"));

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider, LightweightMessageFormatter.LINE);
    JSError error = new JSError(SOURCE_NAME, null, null, null, null, null, null, null, CheckLevel.ERROR,
        "Error message", 1, 1);
    assertEquals(expectedErrorMessageWithSource(1), formatter.formatError(error));
  }

  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError(null, null, null, null, null, null, null, null, CheckLevel.WARNING,
        "Warning message", 1, 1);
    assertEquals("WARNING - Warning message\n", formatter.formatWarning(warning));
  }

  public void testFormatWarningWithSource() {
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getSource(SOURCE_NAME)).thenReturn(new CodeSnippet("function foo() { }"));

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider, LightweightMessageFormatter.LINE);
    JSError warning = new JSError(SOURCE_NAME, null, null, null, null, null, null, null, CheckLevel.WARNING,
        "Warning message", 1, 1);
    assertEquals(expectedWarningMessageWithSource(1), formatter.formatWarning(warning));
  }

  public void testFormatErrorWithLongSourceExcerpt() {
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    String source = "function foo(bar) {\n" +
        "  if (bar > 10) {\n" +
        "    baz();\n" +
        "  } else {\n" +
        "    qux();\n" +
        "  }\n" +
        "}";
    when(sourceExcerptProvider.getSource(SOURCE_NAME)).thenReturn(new CodeSnippet(source));

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider, LightweightMessageFormatter.LINE);
    JSError error = new JSError(SOURCE_NAME, null, null, null, null, null, null, null, CheckLevel.ERROR,
        "Error message", 5, 10);
    String expectedErrorMessage = "ERROR - Error message\n" +
        "5:10 | if (bar > 10) {\n" +
        "    ----------------^";
    assertEquals(expectedErrorMessage, formatter.formatError(error));
  }

  public void testFormatWarningWithLongSourceExcerpt() {
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    String source = "function foo(bar) {\n" +
        "  if (bar > 10) {\n" +
        "    baz();\n" +
        "  } else {\n" +
        "    qux();\n" +
        "  }\n" +
        "}";
    when(sourceExcerptProvider.getSource(SOURCE_NAME)).thenReturn(new CodeSnippet(source));

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider, LightweightMessageFormatter.LINE);
    JSError warning = new JSError(SOURCE_NAME, null, null, null, null, null, null, null, CheckLevel.WARNING,
        "Warning message", 5, 10);
    String expectedWarningMessage = "WARNING - Warning message\n" +
        "5:10 | if (bar > 10) {\n" +
        "    ----------------^";
    assertEquals(expectedWarningMessage, formatter.formatWarning(warning));
  }

  private String expectedErrorMessageWithSource(int lineNumber) {
    return String.format("ERROR - %s:%s:\n%s%s: ^", SOURCE_NAME, lineNumber,
        SOURCE_NAME, getLevelName(CheckLevel.ERROR));
  }

  private String expectedWarningMessageWithSource(int lineNumber) {
    return String.format("WARNING - %s:%s:\n%s%s: ^", SOURCE_NAME, lineNumber,
        SOURCE_NAME, getLevelName(CheckLevel.WARNING));
  }

  public static String getLevelName(CheckLevel level) {
    switch (level) {
      case ERROR:
        return "ERROR";
      case WARNING:
        return "WARNING";
      default:
        throw new AssertionError("Unexpected level: " + level);
    }
  }

  @Override
  protected SourceExcerptProvider createSourceExcerptProvider() {
    return SourceExcerptProviderTestUtil.createMockSourceExcerptProvider();
  }
}