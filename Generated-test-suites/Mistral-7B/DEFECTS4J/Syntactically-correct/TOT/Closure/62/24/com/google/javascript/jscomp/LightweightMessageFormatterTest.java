import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TopLevelTestBase;

public class LightweightMessageFormatterTest extends TopLevelTestBase {

  @Test
  public void testFormatError() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName, int lineNumber) {
        return "function foo() {" + // line 1
            "  var x = 1;" + // line 2
            "  if (x > 10) {" + // line 4, error line
            "    alert('Error message');" + // line 5
            "  }" + // line 6
            "}";

        // return null for test without source
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError(
        "ErrorMessageKey", "Error message", "sourceName.js", 4, 5);
    String result = formatter.formatError(error);

    assertEquals(
        "sourceName.js:4:5 ERROR - ERRORMessageKey - if (x > 10) {" + // line 4, error line
        "  ^\n" + // caret at error position
        "5|   alert('Error message');", // error source code line
        result);
  }

  @Test
  public void testFormatWarning() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName, int lineNumber) {
        return "function foo() {" + // line 1
            "  var x = 1;" + // line 2
            "  if (x > 10) {" + // line 4, warning line
            "    alert('Warning message');" + // line 5
            "  }" + // line 6
            "}";
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError(
        "WarningMessageKey", "Warning message", "sourceName.js", 4, 5);
    String result = formatter.formatWarning(warning);

    assertEquals(
        "sourceName.js:4:5 WARNING - WARNINGMessageKey", result);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError(
        "ErrorMessageKey", "Error message", null, 4, 5);
    String result = formatter.formatError(error);

    assertEquals(
        "4:5 ERROR - ERRORMessageKey - Error message", result);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError(
        "WarningMessageKey", "Warning message", null, 4, 5);
    String result = formatter.formatWarning(warning);

    assertEquals(
        "4:5 WARNING - WARNINGMessageKey", result);
  }

  @Test
  public void testFormatErrorWithEmptySource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName, int lineNumber) {
        return null;
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError(
        "ErrorMessageKey", "Error message", "sourceName.js", 4, 5);
    String result = formatter.formatError(error);

    assertEquals(
        "sourceName.js:4:5 ERROR - ERRORMessageKey - Error message", result);
  }

  @Test
  public void testFormatWarningWithEmptySource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName, int lineNumber) {
        return null;
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError(
        "WarningMessageKey", "Warning message", "sourceName.js", 4, 5);
    String result = formatter.formatWarning(warning);

    assertEquals(
        "sourceName.js:4:5 WARNING - WARNINGMessageKey", result);
  }

  @Test(expected = NullPointerException.class)
  public void testNullProvider() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);
  }
}