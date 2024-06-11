import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.jscomp.SourceExcerpt.LINE;
import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TestSourceFile;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class LightweightMessageFormatterTest {

  private LightweightMessageFormatter formatter;
  private SourceExcerptProvider source;

  @Before
  public void setup() {
    source = new SourceExcerptProvider(new TestSourceFile("Test.js"));
    formatter = new LightweightMessageFormatter(source, LINE);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    JSError error = new JSError("Error message", "Test.js", 1, 1, 1);
    String result = formatter.formatError(error);

    assertEquals("ERROR - Error message\n", result);
  }

  @Test
  public void testFormatErrorWithSource() {
    JSError error = new JSError("Error message", "Test.js", 1, 1, 1);
    String expectedSourceExcerpt = "function foo() {\n  // source code\n}";
    SourceExcerpt sourceExcerpt = new SourceExcerpt("Test.js", expectedSourceExcerpt);

    try {
      source.setSourceExcerpt(sourceExcerpt);
      String result = formatter.formatError(error);

      String expectedResult = "Test.js:1: ERROR - Error message\n"
          + "function foo() {\n  // source code\n}^";
      assertEquals(expectedResult, result);
    } finally {
      source.clearSourceExcerpt();
    }
  }

  @Test
  public void testFormatWarningWithoutSource() {
    JSError warning = new JSError("Warning message", "Test.js", 1, 1, 1);
    String result = formatter.formatWarning(warning);

    assertEquals("WARNING - Warning message\n", result);
  }

  @Test
  public void testFormatWarningWithSource() {
    JSError warning = new JSError("Warning message", "Test.js", 1, 1, 1);
    String expectedSourceExcerpt = "function foo() {\n  // source code\n}";
    SourceExcerpt sourceExcerpt = new SourceExcerpt("Test.js", expectedSourceExcerpt);

    try {
      source.setSourceExcerpt(sourceExcerpt);
      String result = formatter.formatWarning(warning);

      String expectedResult = "Test.js:1: WARNING - Warning message\n"
          + "function foo() {\n  // source code\n}";
      assertEquals(expectedResult, result);
    } finally {
      source.clearSourceExcerpt();
    }
  }

  @Test
  public void testFormatErrorWithInvalidSourceName() {
    JSError error = new JSError("Error message", null, 1, 1, 1);
    String result = formatter.formatError(error);

    assertEquals("ERROR - Error message\n", result);
  }

  @Test
  public void testFormatWarningWithInvalidSourceName() {
    JSError warning = new JSError("Warning message", null, 1, 1, 1);
    String result = formatter.formatWarning(warning);

    assertEquals("WARNING - Warning message\n", result);
  }

  @Test
  public void testFormatErrorWithNegativeLineNumber() {
    JSError error = new JSError("Error message", "Test.js", -1, 1, 1);
    String result = formatter.formatError(error);

    assertEquals("ERROR - Error message\n", result);
  }

  @Test
  public void testFormatWarningWithNegativeLineNumber() {
    JSError warning = new JSError("Warning message", "Test.js", -1, 1, 1);
    String result = formatter.formatWarning(warning);

    assertEquals("WARNING - Warning message\n", result);
  }
}