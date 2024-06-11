import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TestingUtils;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider, LINE);

    String sourceCode = "function foo() {\n" +
        "  bar = 5;\n" +
        "}";

    when(sourceExcerptProvider.getSourceExcerpt(TestingUtils.TEST_SOURCE_FILE, 1, 1)).thenReturn(sourceCode);

    JSError error = new JSError(TestingUtils.TEST_SOURCE_FILE, 1, 5, "Variable declared with var is not allowed", null);
    String expectedErrorMessage = "TEST_SOURCE_FILE:1:5 Variable declared with var is not allowed\n" +
        "function foo() {\n" +
        "  bar = 5;\n" +
        "}^";

    assertEquals(expectedErrorMessage, formatter.formatError(error));
  }

  @Test
  public void testFormatWarning() {
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider, LINE);

    String sourceCode = "function foo() {\n" +
        "  alert('Hello World');\n" +
        "}";

    when(sourceExcerptProvider.getSourceExcerpt(TestingUtils.TEST_SOURCE_FILE, 1, 1)).thenReturn(sourceCode);

    JSError warning = new JSError(TestingUtils.TEST_SOURCE_FILE, 1, 0, "Use 'console.log' instead of 'alert'", null);
    String expectedWarningMessage = "TEST_SOURCE_FILE:1:1 Use 'console.log' instead of 'alert'\n" +
        "function foo() {\n" +
        "  alert('Hello World');\n" +
        "}";

    assertEquals(expectedWarningMessage, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

    JSError error = new JSError("AnonymousFile.java", 1, 5, "Variable declared with var is not allowed", null);
    String expectedErrorMessage = "Error - Variable declared with var is not allowed";

    assertEquals(expectedErrorMessage, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();

    JSError warning = new JSError("AnonymousFile.java", 1, 0, "Use 'console.log' instead of 'alert'", null);
    String expectedWarningMessage = "Warning - Use 'console.log' instead of 'alert'";

    assertEquals(expectedWarningMessage, formatter.formatWarning(warning));
  }
}