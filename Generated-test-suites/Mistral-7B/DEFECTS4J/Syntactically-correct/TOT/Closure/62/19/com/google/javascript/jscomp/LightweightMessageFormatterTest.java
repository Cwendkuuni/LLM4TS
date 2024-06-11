import static org.junit.Assert.*;
import static com.google.javascript.jscomp.SourceExcerptProvider.SourceExcerpt.*;

import com.google.javascript.jscomp.AbstractMessageFormatter;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.LightweightMessageFormatter;
import com.google.javascript.jscomp.LightweightMessageFormatter.ExcerptFormatter;
import com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter;
import com.google.javascript.jscomp.SourceExcerptProvider;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getExcerpt(String sourceText, int lineNumber) {
        return LINE;
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError error = new JSError("TypeError", "invalid 'in' operator",
        "foo.bar", 1, 1);
    String message = formatter.formatError(error);
    String expected = "TypeError: invalid 'in' operator\n1: ";
    assertEquals(expected, message);
  }

  @Test
  public void testFormatWarning() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getExcerpt(String sourceText, int lineNumber,
          String sourceName) {
        return new SourceExcerpt(sourceText, lineNumber, sourceName);
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    JSError warning = new JSError("DeprecatedFeatureWarning",
        "Use of 'with' statement is deprecated.", "myCode.js", 1, 1);
    String message = formatter.formatWarning(warning);
    String expected = "DeprecatedFeatureWarning: Use of 'with' statement is deprecated.\n1: myCode.js: ";
    assertEquals(expected, message);
  }

  @Test
  public void testFormatErrorWithSource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getExcerpt(String sourceText, int lineNumber,
          String sourceName) {
        return new SourceExcerpt(sourceText, lineNumber, sourceName);
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    String code = "function foo() {\n" +
        "  if (bar) {\n" +
        "    alert('Hello world');\n" +
        "  }\n" +
        "}";
    SourceExcerptProvider sourceForTest = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getExcerpt(String sourceText, int lineNumber,
          String sourceName) {
        return new SourceExcerpt(sourceText, lineNumber, sourceName);
      }

      @Override
      public String getSourceText() {
        return code;
      }
    };

    JSError error = new JSError("SyntaxError", "Unexpected token 'if'", "foo.js",
        1, 5);
    String message = formatter.formatError(error, sourceForTest);
    String expected = "SyntaxError: Unexpected token 'if'\n1: foo.js:\n  function foo() {\n  " +
        "    if (bar) {\n  " +
        "      alert('Hello world');\n  }\n";
    assertEquals(expected, message);
  }

  @Test
  public void testFormatWarningWithSource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getExcerpt(String sourceText, int lineNumber,
          String sourceName) {
        return new SourceExcerpt(sourceText, lineNumber, sourceName);
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source);
    String code = "function myFunction() {\n" +
        "  console.log('This is a warning message.');\n" +
        "}\n" +
        "myFunction();";
    SourceExcerptProvider sourceForTest = new SourceExcerptProvider() {
      @Override
      public SourceExcerpt getExcerpt(String sourceText, int lineNumber,
          String sourceName) {
        return new SourceExcerpt(sourceText, lineNumber, sourceName);
      }

      @Override
      public String getSourceText() {
        return code;
      }
    };

    JSError warning = new JSError("DeprecatedFeatureWarning",
        "Use of 'console.log' is deprecated.", "test.js", 1, 1);
    String message = formatter.formatWarning(warning, sourceForTest);
    String expected = "DeprecatedFeatureWarning: Use of 'console.log' is deprecated.\n1: test.js:\n" +
        "function myFunction() {\n  console.log('This is a warning message.');\n}" +
        "myFunction();";
    assertEquals(expected, message);
  }
}