import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import org.junit.Test;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.CodeSnippetTestCompiler;
import com.google.javascript.jscomp.testing.TestSourceFile;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError error = new JSError(
        "TypeError", "test", 1, "test.js", 10, 5);
    String result = formatter.formatError(error);
    String expected = "10: ERROR - TypeError - test.js:^\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter();
    JSError warning = new JSError(
        "DeprecationWarning", "test", 1, "test.js", 10, 5);
    String result = formatter.formatWarning(warning);
    String expected = "10: WARNING - DeprecationWarning - test.js:^\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithSource() {
    SourceExcerptProvider source = new TestSourceFile("test.js");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        source, SourceExcerpt.LINE);
    JSError error = new JSError(
        "TypeError", "test", 1, "test.js", 10, 5, "test.js", 5);
    String result = formatter.formatError(error);
    String expected = "test.js:10: ERROR - TypeError - test.js:5: foo = bar;\n"
        + "5: ^^\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSource() {
    SourceExcerptProvider source = new TestSourceFile("test.js");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        source, SourceExcerpt.LINE);
    JSError warning = new JSError(
        "DeprecationWarning", "test", 1, "test.js", 10, 5, "test.js", 5);
    String result = formatter.formatWarning(warning);
    String expected = "test.js:10: WARNING - DeprecationWarning - test.js:5: var foo = bar;\n"
        + "5: ^^\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError(
        "TypeError", "test", 1, null, 10, 5);
    String result = formatter.formatError(error);
    String expected = "10: ERROR - TypeError:\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError(
        "DeprecationWarning", "test", 1, null, 10, 5);
    String result = formatter.formatWarning(warning);
    String expected = "10: WARNING - DeprecationWarning:\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithRegion() {
    SourceExcerptProvider source = new TestSourceFile("test.js");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        source, SourceExcerpt.REGION);
    JSError error = new JSError(
        "TypeError", "test", 1, "test.js", 1, 5, 1, 10, 6, 15);
    String result = formatter.formatError(error);
    String expected = "1:TypeError test.js:\n"
        + "1: | function foo() {\n"
        + "5: |   bar = baz;\n"
        + "6: | }\n"
        + "10: | alert(qux);\n"
        + "15: | }\n"
        + "^\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithRegion() {
    SourceExcerptProvider source = new TestSourceFile("test.js");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        source, SourceExcerpt.REGION);
    JSError warning = new JSError(
        "DeprecationWarning", "test", 1, "test.js", 1, 5, 1, 10, 6, 15);
    String result = formatter.formatWarning(warning);
    String expected = "1:WARNING DeprecationWarning test.js:\n"
        + "1: | function foo() {\n"
        + "5: |   bar = baz;\n"
        + "6: | }\n"
        + "10: | alert(qux);\n"
        + "^\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithRegionAndNoSource() {
    LightweightMessageFormatter formatter = new LightlightMessageFormatter();
    JSError error = new JSError(
        "TypeError", "test", 1, null, 1, 5, 1, 10, 6, 15);
    String result = formatter.formatError(error);
    String expected = "1:ERROR TypeError:\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithRegionAndNoSource() {
    LightweightMessageFormatter formatter = new LightlightMessageFormatter();
    JSError warning = new JSError(
        "DeprecationWarning", "test", 1, null, 1, 5, 1, 10, 6, 15);
    String result = formatter.formatWarning(warning);
    String expected = "1:WARNING DeprecationWarning:\n";
    assertEquals(expected, result);
  }

  private static CodeSnippetTestCompiler COMPILER = new CodeSnippetTestCompiler();

  @Test
  public void testFormatErrorWithSourceFromCompiledCode() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        COMPILER.getSourceProvider(), SourceExcerpt.LINE);
    String code = "function foo() {"
        + "  bar = baz;"
        + "  alert(qux);"
        + "}";
    String result = formatter.formatError(
        COMPILER.compile(code).getErrors().get(0));
    String expected = "9:ERROR TypeError - test.js:^\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSourceFromCompiledCode() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        COMPILER.getSourceProvider(), SourceExcerpt.LINE);
    String code = "function foo() {"
        + "  bar = baz;"
        + "  alert(qux);"
        + "}";
    JSError warning = COMPILER.compile(code).getWarnings().get(0);
    String result = formatter.formatWarning(warning);
    String expected = "9:WARNING DeprecationWarning - test.js:^\n";
    assertEquals(expected, result);
  }
}