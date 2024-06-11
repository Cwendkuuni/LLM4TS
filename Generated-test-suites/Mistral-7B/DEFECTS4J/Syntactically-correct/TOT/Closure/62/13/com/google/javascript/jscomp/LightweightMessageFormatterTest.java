import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.Checker.CompilationLevel;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    String sourceCode = "function foo() {"
        + "if (bar) {"
        + "  baz();"
        + "} else {"
        + "  qux();"
        + "}"
        + "}";

    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getSource(LightweightMessageFormatterTest.class, "Test.java"))
        .thenReturn(sourceCode);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider);

    JSError error = new JSError("Test.java", 1, 1, "Invalid Syntax", CompilationLevel.ADVANCED);

    String expected = "1:1: ERROR - ERROR - Invalid Syntax\n"
        + "function foo() {\n"
        + "  ^|\n";

    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarning() {
    String sourceCode = "function foo() {"
        + "if (bar) {"
        + "  baz();"
        + "} else {"
        + "  qux();"
        + "}"
        + "}";

    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getSource(LightweightMessageFormatterTest.class, "Test.java"))
        .thenReturn(sourceCode);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider);

    JSError warning = new JSError("Test.java", 1, 1, "Useless statement", CompilationLevel.SIMPLE_OPTIMIZATIONS);

    String expected = "1:1: WARNING - WARNING - Useless statement\n"
        + "function foo() {"
        + "  ^|\n";

    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithSource() {
    String sourceCode = "function foo() {"
        + "if (bar) {"
        + "  baz();"
        + "} else {"
        + "  qux();"
        + "}"
        + "}";

    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getSource(LightweightMessageFormatterTest.class, "Test.java"))
        .thenReturn(sourceCode);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider, LINE);

    JSError error = new JSError("Test.java", 1, 1, "Invalid Syntax", CompilationLevel.ADVANCED);

    String expected = "1:1: ERROR - ERROR - Invalid Syntax\n"
        + "function foo() {\n"
        + "  ^|\n";

    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarningWithSource() {
    String sourceCode = "function foo() {"
        + "if (bar) {"
        + "  baz();"
        + "} else {"
        + "  qux();"
        + "}"
        + "}";

    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getSource(LightweightMessageFormatterTest.class, "Test.java"))
        .thenReturn(sourceCode);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(sourceExcerptProvider, LINE);

    JSError warning = new JSError("Test.java", 1, 1, "Useless statement", CompilationLevel.SIMPLE_OPTIMIZATIONS);

    String expected = "1:1: WARNING - WARNING - Useless statement\n"
        + "function foo() {"
        + "  ^|\n";

    assertEquals(expected, formatter.formatWarning(warning));
  }
}