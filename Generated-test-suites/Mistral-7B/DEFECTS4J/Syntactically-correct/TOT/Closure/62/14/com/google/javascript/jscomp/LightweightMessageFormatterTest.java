import static org.junit.Assert.*;
import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.*;
import static com.google.javascript.jscomp.LightweightMessageFormatter.withoutSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceRoot;
import com.google.javascript.jscomp.testing.TestSourceFile;

public class LightweightMessageFormatterTest {

  @Test
  public void testFormatError() {
    SourceRoot sourceRoot = new SourceRoot(Paths.get("src"), StandardCharsets.UTF_8);
    SourceFile sourceFile = new TestSourceFile(sourceRoot, "Test.java",
        "package test;\n" +
        "class Test {\n" +
        "  function test() {\n" +
        "    var x = 1;\n" +
        "  }\n" +
        "}",
        new String[] {"test"});

    LightweightMessageFormatter formatter = withoutSource();

    JSError error = new JSError("Test.java", 2, "Missing semicolon.", null);
    String expected = "Test.java:2: ERROR - MISSING_SEMICOLON - Missing semicolon.\n";

    assertEquals(expected, formatter.formatError(error));
  }

  @Test
  public void testFormatWarning() {
    SourceRoot sourceRoot = new SourceRoot(Paths.get("src"), StandardCharsets.UTF_8);
    SourceFile sourceFile = new TestSourceFile(sourceRoot, "Test.java",
        "package test;\n" +
        "class Test {\n" +
        "  function test() {\n" +
        "    var x = 1;\n" +
        "    var y = 2;\n" + // warning: unused variable
        "  }\n" +
        "}",
        new String[] {"test"});

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(null);

    JSError warning = new JSError("Test.java", 3, "Unused variable 'y'.", null);
    String expected = "Test.java:3: WARNING - UNDEFINED - Unused variable 'y'.\n";

    assertEquals(expected, formatter.formatWarning(warning));
  }

  @Test
  public void testFormatErrorWithSource() throws IOException {
    SourceRoot sourceRoot = new SourceRoot(Paths.get("src"), StandardCharsets.UTF_8);
    SourceFile sourceFile = new TestSourceFile(sourceRoot, "Test.java",
        "package test;\n" +
        "class Test {\n" +
        "  function test() {\n" +
        "    var x = 1;\n" +
        "  }\n" +
        "}",
        new String[] {"test"});

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getExcerpt(String fileName, int lineNumber) {
            if (fileName.equals("Test.java")) {
              switch (lineNumber) {
                case 2:
                  return new SourceExcerpt(
                      "function test() {\n" +
                      "  var x = 1;\n",
                      null);
                case 3:
                  return new SourceExcerpt(
                      "  }\n",
                      null);
                default:
                  return null;
              }
            } else {
              return null;
            }
          }
        });

    JSError error = new JSError("Test.java", 2, "Missing semicolon.", null);
    String expected = "Test.java:2: ERROR - MISSING_SEMICOLON - Missing semicolon.\n" +
        "function test() {\n" +
        "  var x = 1;\n";

    String result = formatter.formatError(error);
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithSource() throws IOException {
    SourceRoot sourceRoot = new SourceRoot(Paths.get("src"), StandardCharsets.UTF_8);
    SourceFile sourceFile = new TestSourceFile(sourceRoot, "Test.java",
        "package test;\n" +
        "class Test {\n" +
        "  function test() {\n" +
        "    var x = 1;\n" +
        "    var y = 2;\n" + // warning: unused variable
        "  }\n" +
        "}",
        new String[] {"test"});

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getExcerpt(String fileName, int lineNumber) {
            if (fileName.equals("Test.java")) {
              switch (lineNumber) {
                case 1:
                  return new SourceExcerpt(
                      "package test;\n" +
                      "class Test {\n" +
                      "  function test() {\n",
                      null);
                case 2:
                  return new SourceExcerpt(
                      "    var x = 1;\n",
                      null);
                case 3:
                  return new SourceExcerpt(
                      "    var y = 2;\n",
                      null);
                case 4:
                  return new SourceExcerpt(
                      "  }\n",
                      null);
                case 5:
                  return new SourceExcerpt(
                      "}",
                      null);
                default:
                  return null;
              }
            } else {
              return null;
            }
          }
        });

    JSError warning = new JSError("Test.java", 3, "Unused variable 'y'.", null);
    String expected = "Test.java:3: WARNING - UNDEFINED - Unused variable 'y'.\n" +
        "var y = 2;\n";

    String result = formatter.formatWarning(warning);
    assertEquals(expected, result);
  }

  @Test
  public void testFormatErrorWithRegion() throws IOException {
    SourceRoot sourceRoot = new SourceRoot(Paths.get("src"), StandardCharsets.UTF_8);
    SourceFile sourceFile = new TestSourceFile(sourceRoot, "Test.java",
        "package test;\n" +
        "class Test {\n" +
        "  function test() {\n" +
        "    if (x > 5) {\n" +
        "      alert('x is greater than 5');\n" +
        "    } else {\n" +
        "      x = 1;\n" + // error: assignment in conditional expression
        "    }\n" +
        "  }\n" +
        "}",
        new String[] {"test"});

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getExcerpt(String fileName, int lineNumber) {
            if (fileName.equals("Test.java")) {
              switch (lineNumber) {
                case 4:
                  return new SourceExcerpt(
                      "      x = 1;\n",
                      ImmutableList.of(
                          new Region(4, 0, 4, 10)));
                default:
                  return null;
              }
            } else {
              return null;
            }
          }
        });

    JSError error = new JSError("Test.java", 4, "Assignment expression not allowed here.", null);
    String expected = "Test.java:4: ERROR - ASSIGNMENT_EXPRESSION_NOT_ALLOWED_HERE - Assignment expression not allowed here.\n" +
        "      x = 1;\n" +
        "           ^";

    String result = formatter.formatError(error);
    assertEquals(expected, result);
  }

  @Test
  public void testFormatWarningWithRegion() throws IOException {
    SourceRoot sourceRoot = new SourceRoot(Paths.get("src"), StandardCharsets.UTF_8);
    SourceFile sourceFile = new TestSourceFile(sourceRoot, "Test.java",
        "package test;\n" +
        "class Test {\n" +
        "  function test() {\n" +
        "    var x = 1;\n" +
        "    var y = 2;\n" + // warning: unused variable
        "  }\n" +
        "}",
        new String[] {"test"});

    LightweightMessageFormatter formatter = new LightweightMessageFormatter(
        new SourceExcerptProvider() {
          @Override
          public SourceExcerpt getExcerpt(String fileName, int lineNumber) {
            if (fileName.equals("Test.java")) {
              switch (lineNumber) {
                case 2:
                  return new SourceExcerpt(
                      "    var y = 2;\n",
                      ImmutableList.of(
                          new Region(2, 0, 2, 6)));
                default:
                  return null;
              }
            } else {
              return null;
            }
          }
        });

    JSError warning = new JSError("Test.java", 2, "Unused variable 'y'.", null);
    String expected = "Test.java:2: WARNING - UNDEFINED - Unused variable 'y'.\n" +
        "var y = 2;\n" +
        "   ^";

    String result = formatter.formatWarning(warning);
    assertEquals(expected, result);
  }
}