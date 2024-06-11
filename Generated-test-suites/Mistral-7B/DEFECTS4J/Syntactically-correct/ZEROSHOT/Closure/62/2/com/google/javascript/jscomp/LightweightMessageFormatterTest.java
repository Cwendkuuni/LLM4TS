import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.*;
import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.SourceRoot;
import com.google.javascript.jscomp.test.TestSourceFiles;

import org.junit.Test;

public class LightweightMessageFormatterTest {

  @Test
  public void testLightweightMessageFormatter() {
    SourceRoot sourceRoot = new SourceRoot(TestSourceFiles.getTestSourcesPath());
    SourceFile sourceFile = sourceRoot.getSourceFile("LightweightMessageFormatterTest.java");

    AbstractMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
    JSError error =
        new JSError(ErrorLevel.ERROR, sourceFile, 1, 1, "Invalid syntax.", null);
    String actual = formatter.formatError(error);
    String expected = "ERROR - Invalid syntax.\n";
    assertEquals(expected, actual);

    SourceExcerptProvider sourceProvider = new SourceExcerptProvider() {
      @Override
      public String getSourceCode(SourceFile sourceFile) {
        return sourceFile.getCode();
      }

      @Override
      public String getSourceExcerpt(SourceFile sourceFile, int lineNumber) {
        return sourceFile.getCode().substring(0, lineNumber);
      }

      @Override
      public Iterable<SourceExcerpt> getSourceExcerpts(SourceFile sourceFile) {
        return ImmutableList.of();
      }
    };

    formatter = new LightweightMessageFormatter(sourceProvider, LINE);
    error =
        new JSError(ERROR, sourceFile, 1, 1, "Invalid syntax.", null);
    actual = formatter.formatError(error);
    expected = "ERROR - Invalid syntax.\n";
    assertEquals(expected, actual);

    String sourceCode = "function foo() {\n  bar(); // comment\n  baz();\n}";
    SourceFile testSourceFile = new SourceFile("TestFile.js", sourceCode);
    error =
        new JSError(WARNING, testSourceFile, 2, 5, "Missing semicolon.", null);
    actual = formatter.formatWarning(error);
    expected = "WARNING - TestFile.js:2:5 - Missing semicolon.\n  baz();\n";
    assertEquals(expected, actual);

    sourceCode = "function foo() {\n  bar(); // comment\n  baz();\n}";
    SourceFile testSourceFileWithSource = new SourceFile("TestFile.js", sourceCode);
    error =
        new JSError(WARNING, testSourceFileWithSource, 2, 5, "Missing semicolon.", null);
    actual = formatter.formatWarning(error);
    expected = "WARNING - TestFile.js:2:5 - Missing semicolon.\n  baz();\n";
    assertEquals(expected, actual);

    String sourceExcerpt = "function foo() {\n  bar(); // comment\n  baz();\n}";
    error =
        new JSError(WARNING, testSourceFileWithSource, 3, 3, "Missing semicolon.", null);
    actual = formatter.formatWarning(error);
    expected = "WARNING - TestFile.js:3:3 - Missing semicolon.\n  ";
    assertEquals(expected, actual);
  }

  @Test
  public void testLineNumberingFormatter() {
    String sourceCode = "function foo() {\n  bar(); // comment\n  baz();\n}";
    Region region = new Region(1, 1, 1, 1, sourceCode, "TestFile.js");
    LineNumberingFormatter formatter = new LineNumberingFormatter();
    String actual = formatter.formatRegion(region);
    String expected =
        " 1| function foo() {\n  1|   bar(); // comment\n  1|   baz();\n";
    assertEquals(expected, actual);
  }
}