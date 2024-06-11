import static org.junit.Assert.*;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.CodeSnippetTestRunner;

public class LightweightMessageFormatterTest {

  @org.junit.Test
  public void testFormatError() {
    // Basic use case
    SourceExcerptProvider source = new SourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("TestError", "TestSource.js", 1, 1, "Test message");
    String result = formatter.formatError(error);
    String expected = "TestSource.js:1: ERROR - Test message\n";
    assertEquals(expected, result);

    // Edge case: empty source excerpt
    error.lineNumber = 0;
    result = formatter.formatError(error);
    expected = "ERROR - Test message\n";
    assertEquals(expected, result);

    // Edge case: null source excerpt
    error.sourceName = null;
    formatter = new LightweightMessageFormatter();
    result = formatter.formatError(error);
    expected = "ERROR - Test message\n";
    assertEquals(expected, result);

    // Exception handling: null error
    try {
      formatter.formatError(null);
      fail("Should have thrown NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }

  @org.junit.Test
  public void testFormatWarning() {
    // Basic use case
    SourceExcerptProvider source = new SourceExcerptProvider();
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError("TestWarning", "TestSource.js", 1, 1, "Test message");
    String result = formatter.formatWarning(warning);
    String expected = "TestSource.js:1: WARNING - Test message\n";
    assertEquals(expected, result);

    // Edge case: empty source excerpt
    warning.lineNumber = 0;
    result = formatter.formatWarning(warning);
    expected = "WARNING - Test message\n";
    assertEquals(expected, result);

    // Edge case: null source excerpt
    warning.sourceName = null;
    formatter = new LightweightMessageFormatter();
    result = formatter.formatWarning(warning);
    expected = "WARNING - Test message\n";
    assertEquals(expected, result);

    // Exception handling: null warning
    try {
      formatter.formatWarning(null);
      fail("Should have thrown NullPointerException");
    } catch (NullPointerException e) {
      // Expected
    }
  }
}