import static org.junit.Assert.*;
import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.*;
import static com.google.javascript.jscomp.LightweightMessageFormatter.withoutSource;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatErrorWithSource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName) {
        return "function foo() {\n" +
            "  bar();\n" +
            "}";
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    JSError error = new JSError("Error Message", "Error File", 1, 5, 3);

    String result = formatter.formatError(error);
    String expected = "Error File:1:5 Error - ERROR - Error Message\n" +
        "1:5 ^";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithoutSource() {
    LightweightMessageFormatter formatter = withoutSource();
    JSError error = new JSError("Error Message", null, 1, 5, 3);

    String result = formatter.formatError(error);
    String expected = "ERROR - Error Message\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName) {
        return "function foo() {\n" +
            "  bar();\n" +
            "}";
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    JSError warning = new JSError("Warning Message", "Warning File", 1, 5, 3);

    String result = formatter.formatWarning(warning);
    String expected = "Warning File:1:5 WARNING - Warning Message\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithoutSource() {
    LightweightMessageFormatter formatter = withoutSource();
    JSError warning = new JSError("Warning Message", null, 1, 5, 3);

    String result = formatter.formatWarning(warning);
    String expected = "WARNING - Warning Message\n";
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithInvalidSource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName) {
        throw new RuntimeException("Source not found");
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    JSError error = new JSError("Error Message", "Error File", 1, 5, 3);

    try {
      formatter.formatError(error);
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testFormatWarningWithInvalidSource() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName) {
        throw new RuntimeException("Source not found");
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, LINE);
    JSError warning = new JSError("Warning Message", "Warning File", 1, 5, 3);

    try {
      formatter.formatWarning(warning);
      fail("Expected an IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testFormatRegion() {
    SourceExcerptProvider source = new SourceExcerptProvider() {
      @Override
      public String getSource(String sourceName) {
        return "function foo() {\n" +
            "  bar();\n" +
            "  if (x > 5) {\n" +
            "    baz();\n" +
            "  } else {\n" +
            "    qux();\n" +
            "  }\n" +
            "}";
      }
    };

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(source, REGION);
    Region region = new Region("Error File", 1, 1, 1, 10);

    String result = formatter.formatRegion(region);
    String expected = "Error File:1:1-\n" +
        "1:1 | function foo() {\n" +
        "1:2 |   bar();\n" +
        "1:3 |   if (x > 5) {\n" +
        "1:4 |     baz();\n" +
        "1:5 |   } else {\n" +
        "1:6 |     qux();\n" +
        "1:7 |   }\n";
    assertEquals(expected, result);
  }
}