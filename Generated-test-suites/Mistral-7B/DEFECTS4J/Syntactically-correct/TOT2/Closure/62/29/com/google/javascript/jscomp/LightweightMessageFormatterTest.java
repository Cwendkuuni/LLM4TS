import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.Configuration;
import com.google.javascript.jscomp.testing.JSCompilerTestRunner;
import com.google.javascript.jscomp.testing.MockSourceExcerptProvider;

public class LightweightMessageFormatterTest {
  private static final String SOURCE = "example.js";
  private static final int LINE_NUMBER = 10;

  @org.junit.Test
  public void testFormatError() {
    // Given
    SourceExcerptProvider source = new MockSourceExcerptProvider(SOURCE);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError(null, SOURCE, LINE_NUMBER, "Test Error", null);

    // When
    String message = formatter.formatError(error);

    // Then
    String expected = String.format(
        "example.js:10 %s - ERROR: Test Error%n%s^\n",
        getLevelName(CheckLevel.ERROR), SOURCE.substring(0, LINE_NUMBER));
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testFormatWarning() {
    // Given
    SourceExcerptProvider source = new MockSourceExcerptProvider(SOURCE);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError(null, SOURCE, LINE_NUMBER, "Test Warning", null);

    // When
    String message = formatter.formatWarning(warning);

    // Then
    String expected = String.format(
        "example.js:10 %s - WARNING: Test Warning%n%s^\n",
        getLevelName(CheckLevel.WARNING), SOURCE.substring(0, LINE_NUMBER));
    assertEquals(expected, message);
  }

  private static String getLevelName(CheckLevel level) {
    switch (level) {
      case ERROR:
        return "Error";
      case WARNING:
        return "Warning";
      default:
        throw new IllegalStateException("Unexpected level: " + level);
    }
  }

  @org.junit.BeforeClass
  public static void setup() {
    // Setup
    JSCompilerTestRunner.setupWithDefaults();
    Configuration.setOption("check.types", false);
  }
}