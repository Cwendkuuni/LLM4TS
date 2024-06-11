package com.google.javascript.jscomp.test;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.check.CheckLevel;
import com.google.javascript.jscomp.check.LightweightMessageFormatter;
import com.google.javascript.jscomp.check.LineNumberingFormatter;
import com.google.common.base.Preconditions;
import java.util.Objects;

public class LightweightMessageFormatterTest {
  @Test
  public void testFormatError() {
    JSError error = new JSError("testErrorMessage", "TestFile.java", 1, 1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSource(String sourceName) {
            return null;
          }
        },
        SourceExcerptProvider.SourceExcerpt.LINE);

    String expected = String.format(
        "ERROR - testErrorMessage\n%s", "");
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatErrorWithNullJSError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSource(String sourceName) {
            return null;
          }
        },
        SourceExcerptProvider.SourceExcerpt.LINE);

    String expected = "";
    String actual = formatter.formatError(null);
    assertEquals(expected, actual);
  }

  // Add more test cases for other methods and edge cases

  @Test
  public void testFormatErrorWithEmptyErrorDescription() {
    JSError error = new JSError("", "TestFile.java", 1, 1);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getSource(String sourceName) {
            return null;
          }
        },
        SourceExcerptProvider.SourceExcerpt.LINE);

    String expected = "ERROR -\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  // Add more test cases for the remaining methods and edge cases
}