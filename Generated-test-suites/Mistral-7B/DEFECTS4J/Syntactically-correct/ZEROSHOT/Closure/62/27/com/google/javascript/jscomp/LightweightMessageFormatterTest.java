package com.google.javascript.jscomp.test;

import com.google.common.base.Preconditions;
import org.junit.Test;
import static org.junit.Assert.*;

import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.junit.TestSourceExcerptProvider;
import com.google.javascript.jscomp.testing.TestingUtil;

/**
 * Tests for {@link LightweightMessageFormatter}.
 */
public class LightweightMessageFormatterTest {
  private static final ExcerptFormatter EXCERPT_FORMATTER =
      new LightweightMessageFormatter.LineNumberingFormatter();
  private static final SourceExcerptProvider SOURCE_EXCERPT_PROVIDER =
      new TestSourceExcerptProvider();

  @Test
  public void testLightweightMessageFormatter() {
    // Test empty constructor
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    String errorMessage = formatter.formatError(createError());
    String warningMessage = formatter.formatWarning(createWarning());
    assertEquals("ERROR - unknown error\n", errorMessage);
    assertEquals("WARNING - unknown warning\n", warningMessage);

    // Test constructor with source
    LightweightMessageFormatter withSourceFormatter =
        new LightweightMessageFormatter(SOURCE_EXCERPT_PROVIDER, LINE);
    errorMessage = withSourceFormatter.formatError(createError());
    warningMessage = withSourceFormatter.formatWarning(createWarning());
    String source = "var x = 1;\nvar y = 2;\nfunction foo(){\n  x = 3;\n}";
    String expectedErrorMessage =
        "foo.js:3:1 - ERROR - Assignment\n" +
            "      x = 3;\n^\n";
    String expectedWarningMessage =
        "foo.js:3:1 - WARNING - Assignment\n" +
            "      x = 3;\n";
    assertTrue(expectedErrorMessage.startsWith(errorMessage));
    assertTrue(expectedWarningMessage.startsWith(warningMessage));

    // Test constructor with excerpt
    LightweightMessageFormatter withExcerptFormatter =
        new LightweightMessageFormatter(SOURCE_EXCERPT_PROVIDER, EXCERPT_FORMATTER);
    errorMessage = withExcerptFormatter.formatError(createError());
    warningMessage = withExcerptFormatter.formatWarning(createWarning());
    expectedErrorMessage =
        "foo.js:3:1 - ERROR - Assignment\n" +
            "  x = 3;\n^\n";
    expectedWarningMessage =
        "foo.js:3:1 - WARNING - Assignment\n" +
            "  x = 3;\n";
    assertTrue(expectedErrorMessage.startsWith(errorMessage));
    assertTrue(expectedWarningMessage.startsWith(warningMessage));

    // Test formatError
    errorMessage = formatter.formatError(createErrorWithSource("bar.js", 1, 5));
    expectedErrorMessage = "bar.js:1:5 - ERROR - unknown error\n";
    assertTrue(expectedErrorMessage.startsWith(errorMessage));

    errorMessage = withSourceFormatter.formatError(createErrorWithSource("bar.js", 1, 5));
    expectedErrorMessage = "bar.js:1:5 - ERROR - unknown error\n";
    assertTrue(expectedErrorMessage.startsWith(errorMessage));

    errorMessage = withExcerptFormatter.formatError(createErrorWithSource("bar.js", 1, 5));
    expectedErrorMessage = "bar.js:1:5 - ERROR - unknown error\n";
    assertTrue(expectedErrorMessage.startsWith(errorMessage));

    // Test formatWarning
    warningMessage = formatter.formatWarning(createWarningWithSource("bar.js", 1, 5));
    expectedWarningMessage = "bar.js:1:5 - WARNING - unknown warning\n";
    assertTrue(expectedWarningMessage.startsWith(warningMessage));

    warningMessage = withSourceFormatter.formatWarning(createWarningWithSource("bar.js", 1, 5));
    expectedWarningMessage = "bar.js:1:5 - WARNING - unknown warning\n";
    assertTrue(expectedWarningMessage.startsWith(warningMessage));

    warningMessage = withExcerptFormatter.formatWarning(createWarningWithSource("bar.js", 1, 5));
    expectedWarningMessage = "bar.js:1:5 - WARNING - unknown warning\n";
    assertTrue(expectedWarningMessage.startsWith(warningMessage));
  }

  private static JSError createError() {
    return TestingUtil.createError("unknown error");
  }

  private static JSError createErrorWithSource(String sourceName, int lineNumber, int charno) {
    return TestingUtil.createError("unknown error", sourceName, lineNumber, charno);
  }

  private static JSError createWarning() {
    return TestingUtil.createWarning("unknown warning");
  }

  private static JSError createWarningWithSource(String sourceName, int lineNumber, int charno) {
    return TestingUtil.createWarning("unknown warning", sourceName, lineNumber, charno);
  }
}