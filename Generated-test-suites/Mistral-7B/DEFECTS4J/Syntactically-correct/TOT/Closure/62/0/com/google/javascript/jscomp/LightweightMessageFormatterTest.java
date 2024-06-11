import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TopLevelTestRunner;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName, int lineNumber) {
        return "Test file:\n" +
            "function testFunction() {\n" +
            "  var x = 1;\n" +
            "  var y = 2;\n" +
            "  x + y; // error\n" +
            "}";
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("TypeError", "Invalid operator", "testFunction.js", 4, 21);
    String expectedErrorMessage = "4:21 ERROR - TypeError: Invalid operator\n" +
        "testFunction.js:\n" +
        "function testFunction() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  x + y; // error\n" +
        "^\n";
    String actualErrorMessage = formatter.formatError(error);
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }

  @Test
  public void testFormatWarning() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName, int lineNumber) {
        return "Test file:\n" +
            "function testFunction() {\n" +
            "  var x = 1;\n" +
            "  var y = 2;\n" +
            "  x = y; // warning\n" +
            "}";
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError("UndefinedPropertyError", "Undeclared variable", "testFunction.js", 3, 6);
    warning.setIsWarning(true);
    String expectedWarningMessage = "3:6 WARNING - Undeclared variable\n" +
        "testFile.js:\n" +
        "function testFunction() {\n" +
        "  var x = 1;\n" +
        "  var y = 2;\n" +
        "  x = y; // warning\n" +
        "^\n";
    String actualWarningMessage = formatter.formatWarning(warning);
    assertEquals(expectedWarningMessage, actualWarningMessage);
  }

  @Test
  public void testFormatWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("TypeError", "Invalid operator", null, 4, 21);
    String expectedErrorMessage = "4: TypeError: Invalid operator";
    String actualErrorMessage = formatter.formatError(error);
    assertEquals(expectedErrorMessage, actualErrorMessage);
  }
}