import static com.google.javascript.jscomp.CheckLevel.ERROR;
import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.testing.CodeSnippetTestRunner;
import com.google.javascript.jscomp.testing.TestingSourceExcerptProvider;

import java.util.List;

public class LightweightMessageFormatterTest {

  @org.junit.Test
  public void testFormatErrorNoSource() {
    LightweightMessageFormatter messageFormatter = LightweightMessageFormatter.withoutSource();
    JSError error = new JSError("TestError", 1, 1, "test", null);
    String expected = "ERROR - TestError: test\n";
    String actual = messageFormatter.formatError(error);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorWithSource() {
    SourceExcerptProvider source = new TestingSourceExcerptProvider();
    LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(source, LINE);
    JSError error = new JSError("TestError", 1, 1, "test.js", 5, "test", null);
    String expected = "test.js:5: ERROR - TestError: test\n5 | test\n";
    String actual = messageFormatter.formatError(error);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningNoSource() {
    LightweightMessageFormatter messageFormatter = LightweightMessageFormatter.withoutSource();
    JSError warning = new JSError("TestWarning", 1, 1, "test", null);
    String expected = "WARNING - TestWarning: test\n";
    String actual = messageFormatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatWarningWithSource() {
    SourceExcerptProvider source = new TestingSourceExcerptProvider();
    LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(source, LINE);
    JSError warning = new JSError("TestWarning", 1, 1, "test.js", 5, "test", null);
    String expected = "test.js:5: WARNING - TestWarning: test\n5 | test\n";
    String actual = messageFormatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorAndWarningWithSource() {
    SourceExcerptProvider source = new TestingSourceExcerptProvider();
    LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(source, LINE);

    List<JSError> errors = ImmutableList.of(
        new JSError("TestError1", 1, 1, "test.js", 5, "test1", null),
        new JSError("TestError2", 1, 1, "test.js", 6, "test2", null),
        new JSError("TestWarning", 1, 1, "test.js", 7, "test3", null)
    );

    String expected = "test.js:5: ERROR - TestError1: test1\n" +
        "test.js:6: ERROR - TestError2: test2\n" +
        "test.js:7: WARNING - TestWarning: test3\n";

    for (JSError error : errors) {
      expected += messageFormatter.formatErrorOrWarning(error, false);
    }

    String actual = messageFormatter.formatErrorsAndWarnings(errors);
    assertEquals(expected, actual);
  }

  @org.junit.Test
  public void testFormatErrorAndWarningWithSourceAndVerbose() {
    SourceExcerptProvider source = new TestingSourceExcerptProvider();
    LightweightMessageFormatter messageFormatter = new LightweightMessageFormatter(source, LINE);

    List<JSError> errors = ImmutableList.of(
        new JSError("TestError1", 1, 1, "test.js", 5, "test1", null),
        new JSError("TestError2", 1, 1, "test.js", 6, "test2", null),
        new JSError("TestWarning", 1, 1, "test.js", 7, "test3", null)
    );

    String expected = "test.js:5: ERROR - TestError1: test1\n" +
        "test.js:5: test1:\n" +
        "5 | test1\n" +
        "^\n" +
        "test.js:6: ERROR - TestError2: test2\n" +
        "test.js:6: test2:\n" +
        "6 | test2\n" +
        "^\n" +
        "test.js:7: WARNING - TestWarning: test3\n";

    for (JSError error : errors) {
      expected += messageFormatter.formatErrorOrWarning(error, true);
    }

    String actual = messageFormatter.formatErrorsAndWarnings(errors, true);
    assertEquals(expected, actual);
  }

  public static void main(String[] args) {
    org.junit.runner.RunWith(CodeSnippetTestRunner.class);
  }
}