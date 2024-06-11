import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.check.Check;
import com.google.javascript.jscomp.check.SourceFile;
import com.google.javascript.jscomp.formatter.AbstractMessageFormatter;
import com.google.javascript.jscomp.formatter.ExcerptFormatter;
import com.google.javascript.jscomp.formatter.LightweightMessageFormatter;
import com.google.javascript.jscomp.formatter.LineNumberingFormatter;
import com.google.javascript.jscomp.formatter.Region;
import com.google.javascript.jscomp.testcases.TestSourceFile;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testErrorMessage() {
    // Setup
    SourceFile sourceFile = new TestSourceFile("TestFile.js", "var x = 1 + 1;");
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getExcerpt(anyString(), anyInt(), anyInt(),
        eq(LineNumberingFormatter.class)))
        .thenReturn("1| var x = 1 + 1;");

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        sourceExcerptProvider);
    JSError error = new JSError("TestFile.js", 1, 1, "TypeError", "1 + 1 is not a function");

    // Test
    String result = formatter.formatError(error);

    // Verify
    String expected = "TestFile.js:1: TypeError - 1 + 1 is not a function\n1| var x = 1 + 1;\n";
    assertEquals(expected, result);
  }

  @Test
  public void testWarningMessage() {
    // Setup
    SourceFile sourceFile = new TestSourceFile("TestFile.js", "var x = 1;");
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getExcerpt(anyString(), anyInt(), anyInt(),
        eq(LineNumberingFormatter.class)))
        .thenReturn("1| var x = 1;");

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        sourceExcerptProvider);
    JSError warning = new JSError("TestFile.js", 1, 1, "DeprecationWarning",
        "Use of deprecated feature: 'var'");

    // Test
    String result = formatter.formatWarning(warning);

    // Verify
    String expected = "TestFile.js:1: DeprecationWarning - Use of deprecated feature: 'var'\n1| var x = 1;\n";
    assertEquals(expected, result);
  }

  @Test
  public void testWithoutSourceMessage() {
    // Setup
    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("TestFile.js", 1, 1, "TypeError", "1 + 1 is not a function");

    // Test
    String result = formatter.formatError(error);

    // Verify
    String expected = "TestFile.js:1: TypeError - 1 + 1 is not a function";
    assertEquals(expected, result);
  }

  @Test
  public void testRegionMessage() {
    // Setup
    SourceFile sourceFile = new TestSourceFile("TestFile.js",
        "function foo() { if (x < 10) { alert('x is less than 10'); } }");
    SourceExcerptProvider sourceExcerptProvider = mock(SourceExcerptProvider.class);
    when(sourceExcerptProvider.getExcerpt(anyString(), anyInt(), anyInt(),
        eq(LineNumberingFormatter.class)))
        .thenReturn("3| function foo() {\n4|   if (x < 10) {\n5|     alert('x is less than 10');\n6|   }\n7| }");

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        sourceExcerptProvider);
    JSError error = new JSError("TestFile.js", 5, 5, "TypeError", "x is undefined");
    Region region = new Region("TestFile.js", 3, 4, 7);

    // Test
    String result = formatter.formatError(error, region);

    // Verify
    String expected = "TestFile.js:3:4: TypeError - x is undefined\n3| function foo() {\n"
        + " 4|   if (x < 10) {\n5|     alert('x is less than 10');\n"
        + " 6|   }\n7| }";
    assertEquals(expected, result);
  }
}