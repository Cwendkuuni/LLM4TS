import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.check.Checker;

import org.junit.Before;
import org.junit.Test;

public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;
  private SourceExcerptProvider mockSourceProvider;

  @Before
  public void setUp() {
    mockSourceProvider = mock(SourceExcerptProvider.class);
    formatter = new LightweightMessageFormatter(mockSourceProvider);
  }

  @Test
  public void testFormatError() {
    JSError error = new JSError("TestError", "testFile.js", 1, 10,
        "Expected error message", CheckLevel.ERROR);

    when(mockSourceProvider.getExcerpt(LINE, "testFile.js", 1, null))
        .thenReturn("testCode");

    String result = formatter.formatError(error);

    String expected = "testFile.js:1: ERROR - Expected error message\n" +
        "testCode:\n" +
        "^\n";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarning() {
    JSError warning = new JSError("TestWarning", "testFile.js", 1, 10,
        "Expected warning message", CheckLevel.WARNING);

    String result = formatter.formatWarning(warning);

    String expected = "testFile.js:1: WARNING - Expected warning message\n";

    assertEquals(expected, result);
  }

  @Test
  public void testFormatWithoutSource() {
    JSError error = new JSError("TestError", null, 1, 10,
        "Expected error message", CheckLevel.ERROR);

    String result = formatter.withoutSource().formatError(error);

    String expected = "1: ERROR - Expected error message\n";

    assertEquals(expected, result);
  }
}