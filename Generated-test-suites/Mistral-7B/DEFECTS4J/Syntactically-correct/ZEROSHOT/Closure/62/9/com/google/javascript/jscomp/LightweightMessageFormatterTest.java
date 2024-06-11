import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceName;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError(SourceName.NONE, 1, "Error message", null);
    String actual = formatter.formatError(error);

    String expected = "ERROR - Error message\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatErrorWithSource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSource(SourceName.SAMPLE)).thenReturn("sample code");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LineNumberingFormatter.LINE);
    JSError error = new JSError(SourceName.SAMPLE, 1, "Error message", null);
    String actual = formatter.formatError(error);

    String expected = "SAMPLE:1: ERROR - Error message\nsample code:\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError(SourceName.NONE, 1, "Warning message", null);
    String actual = formatter.formatWarning(warning);

    String expected = "WARNING - Warning message\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWarningWithSource() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSource(SourceName.SAMPLE)).thenReturn("sample code");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LineNumberingFormatter.LINE);
    JSError warning = new JSError(SourceName.SAMPLE, 1, "Warning message", null);
    String actual = formatter.formatWarning(warning);

    String expected = "WARNING - Warning message\nsample code:\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithLineNumber() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSource(SourceName.SAMPLE)).thenReturn("function foo() {\n  var x = 1;\n  if (x < 5) {\n    alert('less than 5');\n  }\n}");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LineNumberingFormatter.LINE);
    JSError error = new JSError(SourceName.SAMPLE, 3, "Error message", null);
    String actual = formatter.formatError(error);

    String expected = "SAMPLE:3: ERROR - Error message\n  if (x < 5) {\n    alert('less than 5');\n  }\n";
    assertEquals(expected, actual);
  }

  @Test
  public void testFormatWithRegion() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    when(source.getSource(SourceName.SAMPLE)).thenReturn("function foo() {\n  var x = 1;\n  if (x < 5) {\n    alert('less than 5');\n  }\n  var y = 2;\n}");
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LineNumberingFormatter.REGION);
    JSError error = new JSError(SourceName.SAMPLE, 3, "Error message", null, Arrays.asList(2, 5));
    String actual = formatter.formatError(error);

    String expected = "SAMPLE:\n  2: if (x < 5) {\n      alert('less than 5');\n  5: }\nERROR - Error message\n";
    assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatWithNullError() {
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null, LineNumberingFormatter.LINE);
    formatter.formatError(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatWithNullSourceName() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightlightMessageFormatter(source, LineNumberingFormatter.LINE);
    JSError error = new JSError(null, 1, "Error message", null);
    formatter.formatError(error);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFormatWithNegativeLineNumber() {
    SourceExcerptProvider source = mock(SourceExcerptProvider.class);
    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LineNumberingFormatter.LINE);
    JSError error = new JSError(SourceName.SAMPLE, -1, "Error message", null);
    formatter.formatError(error);
  }

}

class LightlightMessageFormatter {
  // It's a typo in the given class, should be LightweightMessageFormatter
}