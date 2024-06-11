import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceName;
import com.google.javascript.jscomp.testing.TestingAnnotationProcessor;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link LightweightMessageFormatter}.
 */
public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;
  private SourceExcerptProvider sourceExcerptProvider;

  @Before
  public void setUp() {
    sourceExcerptProvider = mock(SourceExcerptProvider.class);
    formatter = new LightweightMessageFormatter(sourceExcerptProvider);
  }

  @Test
  public void testFormatErrorWithSource() {
    String sourceCode = "function foo() {" + System.lineSeparator()
        + "  bar();" + System.lineSeparator() + "}";
    SourceName sourceName = new SourceName("Test.js");
    JSError error = new JSError("Error message", sourceName, 1, 5);

    when(sourceExcerptProvider.getSource(sourceName)).thenReturn(sourceCode);

    String expected = "Test.js:1:5 " + CheckLevel.ERROR + " - Error message\n"
        + "function foo() {\n" + "  bar();\n" + "}";

    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatErrorWithoutSource() {
    JSError error = new JSError("Error message", null, 1, 5);

    String expected = "Error message\n";

    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithSource() {
    String sourceCode = "function foo() {" + System.lineSeparator()
        + "  bar();" + System.lineSeparator() + "}";
    SourceName sourceName = new SourceName("Test.js");
    JSError warning = new JSError("Warning message", sourceName, 1, 5);

    when(sourceExcerptProvider.getSource(sourceName)).thenReturn(sourceCode);

    String expected = "Test.js:1:5 " + CheckLevel.WARNING + " - Warning message\n"
        + "function foo() {" + System.lineSeparator() + "  bar();\n" + "}";

    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatWarningWithoutSource() {
    JSError warning = new JSError("Warning message", null, 1, 5);

    String expected = "Warning message\n";

    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithNullSource() {
    JSError error = new JSError("Error message", null, 1, 5);

    String expected = "Error message\n";

    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithNullSource() {
    JSError warning = new JSError("Warning message", null, 1, 5);

    String expected = "Warning message\n";

    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test(expected = NullPointerException.class)
  public void testFormatWithNullError() {
    formatter.format(null, false);
  }
}