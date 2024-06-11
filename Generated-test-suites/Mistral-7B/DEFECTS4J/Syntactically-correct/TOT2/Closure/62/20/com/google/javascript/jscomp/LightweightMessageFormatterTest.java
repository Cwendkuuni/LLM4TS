
import com.google.javascript.jscomp.JSError;
import org.junit.Assert;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testConstructors() {
    // Testing no-args constructor
    LightweightMessageFormatter lmf1 = LightweightMessageFormatter.withoutSource();
    // Testing constructor with SourceExcerptProvider
    SourceExcerptProvider sourceProvider = new SourceExcerptProvider();
    LightweightMessageFormatter lmf2 = new LightweightMessageFormatter(sourceProvider, LightweightMessageFormatter.LINE);
    // Testing constructor with SourceExcerptProvider and SourceExcerpt
    LightweightMessageFormatter lmf3 = new LightweightMessageFormatter(sourceProvider, LightweightMessageFormatter.LINE);
  }

  @Test
  public void testFormatErrorAndWarning() {
    JSError error = new JSError("TestError", "testFile.js", 10, "TestMessage", null);

    LightweightMessageFormatter lmf = new LightweightMessageFormatter(null);
    String errorMessage = lmf.formatError(error);
    String warningMessage = lmf.formatWarning(error);

    String expectedErrorMessage = "ERROR - TestMessage\n";
    String expectedWarningMessage = "WARNING - TestMessage\n";

    assertEquals(expectedErrorMessage, errorMessage);
    assertEquals(expectedWarningMessage, warningMessage);
  }

  @Test(expected = NullPointerException.class)
  public void testFormatErrorWithNullJsError() {
    LightweightMessageFormatter lmf = new LightweightMessageFormatter(null);
    lmf.formatError(null);
  }

  // Testing other edge cases and exception handling scenarios
  // ...
}
