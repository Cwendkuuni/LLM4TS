package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.TestSourceFile;
import com.google.javascript.jscomp.testing.TypeCheckerTester;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

/**
 * Test for {@link LightweightMessageFormatter} class.
 */
public class LightweightMessageFormatterTest {

  private SourceExcerptProvider mockSourceProvider;
  private LightweightMessageFormatter formatter;

  @Before
  public void setup() {
    mockSourceProvider = mock(SourceExcerptProvider.class);
    formatter = new LightweightMessageFormatter(mockSourceProvider, LINE);
  }

  @Test
  public void testErrorMessageWithoutSource() {
    JSError error = new JSError("TypeError", "message", 1, null, 5, 10, 15);
    String expectedMessage = "ERROR - message\n";
    String actualMessage = formatter.formatError(error);
    assertThat(actualMessage).isEqualTo(expectedMessage);
  }

  @Test
  public void testErrorMessageWithSource() {
    SourceFile sourceFile = new TestSourceFile("test.js", "function foo() {\n"
        + "  bar();\n"
        + "}");
    when(mockSourceProvider.getSourceFor(anyString(), anyInt()))
        .thenReturn(sourceFile);

    JSError error = new JSError("TypeError", "message", 1, "test.js", 3, 15, 16);
    String expectedMessage = "test.js:3: ERROR - message\n"
        + "  function foo() {\n"
        + "    bar();\n"
        + "  }\n";
    String actualMessage = formatter.formatError(error);
    assertThat(actualMessage).isEqualTo(expectedMessage);
  }

  @Test
  public void testErrorMessageWithPartialSource() {
    SourceFile sourceFile = new TestSourceFile("test.js", "function foo() {\n"
        + "  bar(\n");
    when(mockSourceProvider.getSourceFor(anyString(), anyInt()))
        .thenReturn(sourceFile);

    JSError error = new JSError("TypeError", "message", 1, "test.js", 3, 15, 17);
    String expectedMessage = "test.js:3: ERROR - message\n"
        + "  function foo() {\n"
        + "    bar(\n";
    String actualMessage = formatter.formatError(error);
    assertThat(actualMessage).isEqualTo(expectedMessage);
  }

  @Test
  public void testWarningMessageWithoutSource() {
    JSError warning = new JSError("Deprecated", "message", 1, null, 5, 10, 15);
    String expectedMessage = "WARNING - message\n";
    String actualMessage = formatter.formatWarning(warning);
    assertThat(actualMessage).isEqualTo(expectedMessage);
  }

  @Test
  public void testWarningMessageWithSource() {
    SourceFile sourceFile = new TestSourceFile("test.js", "function foo() {\n"
        + "  //warning: message\n"
        + "  bar();\n"
        + "}");
    when(mockSourceProvider.getSourceFor(anyString(), anyInt()))
        .thenReturn(sourceFile);

    JSError warning = new JSError("Deprecated", "message", 1, "test.js", 3, 15, 20);
    String expectedMessage = "test.js:3: WARNING - message\n"
        + "  //warning: message\n"
        + "  bar();\n";
    String actualMessage = formatter.formatWarning(warning);
    assertThat(actualMessage).isEqualTo(expectedMessage);
  }

  @Test
  public void testWarningMessageWithPartialSource() {
    SourceFile sourceFile = new TestSourceFile("test.js", "function foo() {\n"
        + "  //warning: message\n"
        + "  bar(\n");
    when(mockSourceProvider.getSourceFor(anyString(), anyInt()))
        .thenReturn(sourceFile);

    JSError warning = new JSError("Deprecated", "message", 1, "test.js", 3, 15, 22);
    String expectedMessage = "test.js:3: WARNING - message\n"
        + "  //warning: message\n"
        + "  bar(\n";
    String actualMessage = formatter.formatWarning(warning);
    assertThat(actualMessage).isEqualTo(expectedMessage);
  }

  @Test
  public void testFormatRegion() {
    String code = "function foo() {\n"
        + "  bar(baz);\n"
        + "  qux();\n"
        + "}";
    Region region = new Region(1, 1, 1, 1, code);
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    String expectedMessage = " 1| function foo() {\n"
        + "    bar(baz);\n"
        + "    qux();\n"
        + "  }\n";
    String actualMessage = formatter.formatRegion(region);
    assertThat(actualMessage).isEqualTo(expectedMessage);
  }
}