import static org.junit.Assert.*;
import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.*;
import static com.google.javascript.jscomp.SourceFile.*;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.testing.TestSourceFile;

public class LightweightMessageFormatterTest {
  private SourceExcerptProvider sourceProvider;
  private LightweightMessageFormatter formatter;
  private ExcerptFormatter lineNumberFormatter;

  @Before
  public void setUp() {
    sourceProvider = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getSourceExcerpt(SourceFile sourceFile, int lineNumber,
          ExcerptFormatter excerptFormatter) {
        return formatRegion(sourceFile, lineNumber, excerptFormatter);
      }
    };
    formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    lineNumberFormatter = new LineNumberingFormatter();
  }

  @Test
  public void testErrorFormatWithoutSource() {
    String code = "function foo() { throw new Error('error message'); }";
    SourceFile sourceFile = new TestSourceFile("Test.js", code);
    JSError error = new JSError("Test.js", 1, "Error", null, null, null, 0, 0, null);
    String expected = "ERROR - error message\n";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testErrorFormatWithSource() {
    String code = "function foo() { throw new Error('error message'); }";
    SourceFile sourceFile = new TestSourceFile("Test.js", code);
    JSError error = new JSError("Test.js", 1, "Error", null, null, null, 0, 0, null);
    String expected = "Test.js:1: ERROR - error message\n" +
        "1 | function foo() {\n" +
        "   | throw new Error('error message');\n" +
        "   | ^";
    String actual = formatter.formatError(error);
    assertEquals(expected, actual);
  }

  @Test
  public void testWarningFormatWithoutSource() {
    String code = "function foo() { alert('warning message'); }";
    SourceFile sourceFile = new TestSourceFile("Test.js", code);
    JSError warning = new JSError("Test.js", 1, "Warning", null, null, null, 0, 0, null);
    String expected = "WARNING - warning message\n";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @Test
  public void testWarningFormatWithSource() {
    String code = "function foo() { alert('warning message'); }";
    SourceFile sourceFile = new TestSourceFile("Test.js", code);
    JSError warning = new JSError("Test.js", 1, "Warning", null, null, null, 0, 0, null);
    String expected = "Test.js:1: WARNING - warning message\n" +
        "1 | function foo() {\n" +
        "   | alert('warning message');\n" +
        "   | ^";
    String actual = formatter.formatWarning(warning);
    assertEquals(expected, actual);
  }

  @Test
  public void testErrorFormatWithMultipleErrors() {
    String code = "function foo() {" +
        "  var x = 1;" +
        "  if (false) {" +
        "    throw new Error('error1');" +
        "  }" +
        "  if (false) {" +
        "    throw new Error('error2');" +
        "  }" +
        "  var y = 2;" +
        "}"
        ;
    SourceFile sourceFile = new TestSourceFile("Test.js", code);
    JSError error1 = new JSError("Test.js", 3, "Error", null, null, null, 0, 0, null);
    JSError error2 = new JSError("Test.js", 5, "Error", null, null, null, 0, 0, null);
    List<JSError> errors = Lists.newArrayList(error1, error2);
    String expected = "Test.js:3: ERROR - error1\n" +
        "3 |     if (false) {" +
        "   |         ^" +
        "Test.js:5: ERROR - error2\n" +
        "5 |     if (false) {" +
        "   |         ^";
    String actual = formatter.formatErrors(errors);
    assertEquals(expected, actual);
  }

  @Test
  public void testWarningFormatWithMultipleWarnings() {
    String code = "function foo() {" +
        "  alert('warning1');" +
        "  alert('warning2');" +
        "}"
        ;
    SourceFile sourceFile = new TestSourceFile("Test.js", code);
    JSError warning1 = new JSError("Test.js", 1, "Warning", null, null, null, 0, 0, null);
    JSError warning2 = new JSError("Test.js", 2, "Warning", null, null, null, 0, 0, null);
    List<JSError> warnings = Lists.newArrayList(warning1, warning2);
    String expected = "Test.js:1: WARNING - warning1\n" +
        "1 | function foo() {" +
        "   | alert('warning1');" +
        "   | ^" +
        "Test.js:2: WARNING - warning2\n" +
        "2 | alert('warning2');" +
        "   | ^";
    String actual = formatter.formatWarnings(warnings);
    assertEquals(expected, actual);
  }

  private String formatRegion(SourceFile sourceFile, int lineNumber,
      ExcerptFormatter excerptFormatter) {
    try {
      return sourceFile.getSourceAsString().split("\n")[lineNumber - 1];
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}