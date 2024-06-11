import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceExcerptProvider.ExcerptFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt;
import com.google.javascript.jscomp.withoutSideEffects.LightweightMessageFormatter;
import com.google.javascript.jscomp.withoutSideEffects.LightweightMessageFormatter.LineNumberingFormatter;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatErrorNoSource() {
    JSError error = new JSError("TestError", "Error message", 1, 1);
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    String result = formatter.formatError(error);
    String expected = "ERROR - Error message\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithSource() {
    JSError error = new JSError("TestError", "Error message", 1, 1, "test.js");
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String resourceName, int lineNumber) {
        return new SourceExcerpt("test.js", 1, "function test() {\n  // test code\n}", 1, null);
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    String result = formatter.formatError(error);
    String expected = "test.js:1: ERROR - Error message\nfunction test() {\n  // test code\n}^";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningNoSource() {
    JSError warning = new JSError("TestWarning", "Warning message", 1, 1);
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    String result = formatter.formatWarning(warning);
    String expected = "WARNING - Warning message\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSource() {
    JSError warning = new JSError("TestWarning", "Warning message", 1, 1, "test.js");
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String resourceName, int lineNumber) {
        return new SourceExcerpt("test.js", 1, "function test() {\n  // test code\n}", 1, null);
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    String result = formatter.formatWarning(warning);
    String expected = "test.js:1: WARNING - Warning message\nfunction test() {\n  // test code\n}^";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithCustomFormatter() {
    JSError error = new JSError("TestError", "Error message", 1, 1, "test.js");
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String resourceName, int lineNumber) {
        return new SourceExcerpt("test.js", 1, "function test() {\n  // test code\n}", 1, null);
      }

      @Override
      public ExcerptFormatter getExcerptFormatter() {
        return new LineNumberingFormatter();
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    String result = formatter.formatError(error);
    String expected = "  test.js:1: ERROR - Error message\nfunction test() {\n  // test code\n}^";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithCustomFormatter() {
    JSError warning = new JSError("TestWarning", "Warning message", 1, 1, "test.js");
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(String resourceName, int lineNumber) {
        return new SourceExcerpt("test.js", 1, "function test() {\n  // test code\n}", 1, null);
      }

      @Override
      public ExcerptFormatter getExcerptFormatter() {
        return new LineNumberingFormatter();
      }
    };
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    String result = formatter.formatWarning(warning);
    String expected = "  test.js:1: WARNING - Warning message\nfunction test() {\n  // test code\n}^";
    assertEquals(expected, result);
  }
}