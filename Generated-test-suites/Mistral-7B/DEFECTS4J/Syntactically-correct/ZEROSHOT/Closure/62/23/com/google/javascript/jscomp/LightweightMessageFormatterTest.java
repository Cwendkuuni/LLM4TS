package com.google.javascript.jscomp.test;

import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.test.mockito.MockitoAnnotations;
import org.junit.Test;

import java.util.Arrays;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter();
    JSError error = new JSError(CheckLevel.ERROR, "Invalid code", "Test.java", 1, 1);
    String result = messageFormatter.formatError(error);
    String expected = "ERROR - Invalid code\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithSource() {
    SourceExcerptProvider sourceExcerptProvider = MockitoAnnotations.mock(SourceExcerptProvider.class);
    LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(sourceExcerptProvider, LightweightMessageFormatter.LINE);
    JSError error = new JSError(CheckLevel.ERROR, "Invalid code", "Test.java", 1, 1);
    String sourceExcerpt = "int x = 5;\n";
    Preconditions.checkArgument(sourceExcerptProvider.getKind() == SourceExcerptProvider.Kind.JAVASCRIPT, "Unexpected source kind");
    Preconditions.checkArgument(sourceExcerptProvider.getSourceName().equals("Test.java"), "Unexpected source name");
    when(sourceExcerptProvider.getSource(eq("Test.java"), anyInt(), anyInt(), eq(LightweightMessageFormatter.LINE)))
        .thenReturn(sourceExcerpt);
    String result = messageFormatter.formatError(error);
    String expected = "Test.java:1: ERROR - Invalid code\nint x = 5;\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter();
    JSError warning = new JSError(CheckLevel.WARNING, "Possible issue", "Test.java", 1, 1);
    String result = messageFormatter.formatWarning(warning);
    String expected = "WARNING - Possible issue\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSource() {
    SourceExcerptProvider sourceExcerptProvider = MockitoAnnotations.mock(SourceExcerptProvider.class);
    LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(sourceExcerptProvider, LightweightMessageFormatter.LINE);
    JSError warning = new JSError(CheckLevel.WARNING, "Possible issue", "Test.java", 1, 1);
    String sourceExcerpt = "int x = 5;\n";
    Preconditions.checkArgument(sourceExcerptProvider.getKind() == SourceExcerptProvider.Kind.JAVASCRIPT, "Unexpected source kind");
    Preconditions.checkArgument(sourceExcerptProvider.getSourceName().equals("Test.java"), "Unexpected source name");
    when(sourceExcerptProvider.getSource(eq("Test.java"), anyInt(), anyInt(), eq(LightweightMessageFormatter.LINE)))
        .thenReturn(sourceExcerpt);
    String result = messageFormatter.formatWarning(warning);
    String expected = "Test.java:1: WARNING - Possible issue\nint x = 5;\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter messageFormatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError(CheckLevel.ERROR, "Invalid code", null, 1, 1);
    String result = messageFormatter.formatError(error);
    String expected = "ERROR - Invalid code\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter messageFormatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError(CheckLevel.WARNING, "Possible issue", null, 1, 1);
    String result = messageFormatter.formatWarning(warning);
    String expected = "WARNING - Possible issue\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithLongSourceLine() {
    SourceExcerptProvider sourceExcerptProvider = MockitoAnnotations.mock(SourceExcerptProvider.class);
    LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(sourceExcerptProvider, LightweightMessageFormatter.LINE);
    JSError error = new JSError(CheckLevel.ERROR, "Invalid code", "Test.java", 1, 1);
    String sourceExcerpt = "int x = 5;\nint y = 10 + 5 * 3;\n";
    Preconditions.checkArgument(sourceExcerptProvider.getKind() == SourceExcerptProvider.Kind.JAVASCRIPT, "Unexpected source kind");
    Preconditions.checkArgument(sourceExcerptProvider.getSourceName().equals("Test.java"), "Unexpected source name");
    when(sourceExcerptProvider.getSource(eq("Test.java"), anyInt(), anyInt(), eq(LightweightMessageFormatter.LINE)))
        .thenReturn(sourceExcerpt);
    String result = messageFormatter.formatError(error);
    String expected = "Test.java:1: ERROR - Invalid code\nint x = 5;\nint y = 10 + 5 * 3;\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithMultiLineRegion() {
    SourceExcerptProvider sourceExcerptProvider = MockitoAnnotations.mock(SourceExcerptProvider.class);
    LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(sourceExcerptProvider, LightweightMessageFormatter.LINE);
    JSError error = new JSError(CheckLevel.ERROR, "Invalid code", "Test.java", 1, 2);
    String sourceExcerpt = "int x = 5;\nint y = 10 + 5 * 3;\n";
    Region region = new Region("Test.java", 1, 1, 1, 3);
    Preconditions.checkArgument(sourceExcerptProvider.getKind() == SourceExcerptProvider.Kind.JAVASCRIPT, "Unexpected source kind");
    Preconditions.checkArgument(sourceExcerptProvider.getSourceName().equals("Test.java"), "Unexpected source name");
    when(sourceExcerptProvider.getSource(eq("Test.java"), anyInt(), anyInt(), eq(LightweightMessageFormatter.LINE)))
        .thenReturn(sourceExcerpt);
    when(sourceExcerptProvider.getRegion(eq("Test.java"), eq(1), anyInt(), anyInt()))
        .thenReturn(region);
    String result = messageFormatter.formatError(error);
    String expected = "Test.java:1:1: ERROR - Invalid code\nint x = 5;\nint y = 10 + 5 * 3;\nTest.java:1:3: ";
    assertEquals(expected, result.substring(0, expected.length() + 1));
    int pos = result.indexOf('\n');
    assertEquals(expected.length() + 1 + 1 + 1, pos);
    String line = result.substring(pos + 1, pos + 1 + 21);
    assertTrue(line.matches("^  \\d+|\\d+|\\s+|\\|$"));
    int charno = error.getCharno();
    int index = Arrays.binarySearch(line.split("\\s+|\\|"), Integer.toString(charno));
    assertTrue(index >= 0);
  }

  @Test
  public void testFormatWarningWithMultiLineRegion() {
    SourceExcerptProvider sourceExcerptProvider = MockitoAnnotations.mock(SourceExcerptProvider.class);
    LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(sourceExcerptProvider, LightweightMessageFormatter.LINE);
    JSError warning = new JSError(CheckLevel.WARNING, "Possible issue", "Test.java", 1, 2);
    String sourceExcerpt = "int x = 5;\nint y = 10 + 5 * 3;\n";
    Region region = new Region("Test.java", 1, 1, 1, 3);
    Preconditions.checkArgument(sourceExcerptProvider.getKind() == SourceExcerptProvider.Kind.JAVASCRIPT, "Unexpected source kind");
    Preconditions.checkArgument(sourceExcerptProvider.getSourceName().equals("Test.java"), "Unexpected source name");
    when(sourceExcerptProvider.getSource(eq("Test.java"), anyInt(), anyInt(), eq(LightweightMessageFormatter.LINE)))
        .thenReturn(sourceExcerpt);
    when(sourceExcerptProvider.getRegion(eq("Test.java"), eq(1), anyInt(), anyInt()))
        .thenReturn(region);
    String result = messageFormatter.formatWarning(warning);
    String expected = "Test.java:1:1: WARNING - Possible issue\nint x = 5;\nint y = 10 + 5 * 3;\nTest.java:1:3: ";
    assertEquals(expected, result.substring(0, expected.length() + 1));
    int pos = result.indexOf('\n');
    assertEquals(expected.length() + 1 + 1 + 1, pos);
    String line = result.substring(pos + 1, pos + 1 + 21);
    assertTrue(line.matches("^  \\d+|\\d+|\\s+|\\|$"));
    int charno = warning.getCharno();
    int index = Arrays.binarySearch(line.split("\\s+|\\|"), Integer.toString(charno));
    assertTrue(index >= 0);
  }
}