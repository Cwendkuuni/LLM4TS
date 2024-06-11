import static org.junit.Assert.*;
import static com.google.javascript.jscomp.LightweightMessageFormatter.withoutSource;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        return new SourceExcerpt(sourceName, lineNumber, "source code");
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("TypeError", "invalid 'in' operand", "foo.js", 1, 5);
    String expected = "TypeError: invalid 'in' operand\nsource code:  ^";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarning() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("DeprecationWarning", "This feature is deprecated", "bar.js", 2, 10);
    String expected = "WARNING - This feature is deprecated\n";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatErrorWithSource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        String source = "function foo() {\n" +
                       "  return bar;\n" +
                       "}";
        return new SourceExcerpt(sourceName, lineNumber, source);
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("TypeError", "invalid 'in' operand", "foo.js", 1, 5);
    String expected = "TypeError: invalid 'in' operand\n" +
                      "foo.js:1:5\n" +
                      "function foo() {\n" +
                      "  return bar;\n" +
                      "}";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningWithSource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String sourceName, int lineNumber) {
        String source = "function foo() {\n" +
                       "  if (x in y) {\n" +
                       "    alert('Hello');\n" +
                       "  }\n" +
                       "}";
        return new SourceExcerpt(sourceName, lineNumber, source);
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError("DeprecationWarning", "This feature is deprecated", "foo.js", 1, 12);
    String expected = "WARNING - This feature is deprecated\n" +
                      "foo.js:1:12\n" +
                      "function foo() {\n" +
                      "  if (x in y) {\n" +
                      "    alert('Hello');\n" +
                      "  }\n" +
                      "}";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("TypeError", "invalid 'in' operand", "foo.js", 1, 5);
    String expected = "TypeError: invalid 'in' operand";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("DeprecationWarning", "This feature is deprecated", "bar.js", 2, 10);
    String expected = "WARNING - This feature is deprecated";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }
}