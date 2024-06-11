import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.TestSourceFile;
import com.google.javascript.jscomp.testing.TopLevelTestRunner;
import com.google.javascript.jscomp.testing.config.CompilerOptions;

import java.io.IOException;

public class LightweightMessageFormatterTest {
  private SourceExcerptProvider source;

  @org.junit.Before
  public void setUp() throws IOException {
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(new String[]{"unused"});
    source = new TestSourceFile("test.js", options).getSourceProvider();
  }

  @org.junit.Test
  public void testErrorMessage() {
    JSError error = new JSError("TestError", "test.js", 1, 1, "Error Message",
        CheckLevel.ERROR, 1);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    String message = formatter.formatError(error);

    String expected = "1: Error - ERROR - Error Message\n"
        + "test.js:1:1: Error - ERROR - Error Message\n";
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testWarningMessage() {
    JSError warning = new JSError("TestWarning", "test.js", 1, 1, "Warning Message",
        CheckLevel.WARNING, 1);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    String message = formatter.formatWarning(warning);

    String expected = "1: WARNING - WARNING - Warning Message\n"
        + "test.js:1:1: Warning - WARNING - Warning Message\n";
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testErrorMessageWithSource() {
    JSError error = new JSError("TestError", "test.js", 1, 1, "Error Message",
        CHECK_ERROR, "someCode.js", 5, 10, null);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    String message = formatter.formatError(error);

    String expected = "someCode.js:5:10: Error - ERROR - Error Message\n"
        + "    int foo = 5;\n"
        + "    ^^^^^^^^";
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testWarningMessageWithSource() {
    JSError warning = new JSError("TestWarning", "test.js", 1, 1, "Warning Message",
        CHECK_WARNING, "someCode.js", 5, 10, null);

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    String message = formatter.formatWarning(warning);

    String expected = "someCode.js:5:10: Warning - WARNING - Warning Message\n"
        + "    int foo = 5;\n"
        + "    ^^^^^^^^";
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testErrorMessageWithoutSource() {
    JSError error = new JSError("TestError", null, 1, 1, "Error Message",
        CHECK_ERROR, null, 0, 0, null);

    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    String message = formatter.formatError(error);

    String expected = "1: Error - ERROR - Error Message\n";
    assertEquals(expected, message);
  }

  @org.junit.Test
  public void testWarningMessageWithoutSource() {
    JSError warning = new JSError("TestWarning", null, 1, 1, "Warning Message",
        CHECK_WARNING, null, 0, 0, null);

    LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    String message = formatter.formatWarning(warning);

    String expected = "1: WARNING - WARNING - Warning Message\n";
    assertEquals(expected, message);
  }
}