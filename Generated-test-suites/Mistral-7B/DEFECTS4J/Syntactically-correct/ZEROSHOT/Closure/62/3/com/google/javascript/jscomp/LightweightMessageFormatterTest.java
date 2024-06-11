import static com.google.javascript.jscomp.CheckLevel.*;
import static com.google.javascript.jscomp.LightweightMessageFormatter.LineNumberingFormatter.*;
import static org.junit.Assert.*;

import com.google.common.base.Preconditions;
import com.google.javascript.jscomp.JSError;
import com.google.javascript.jscomp.SourceExcerpt;
import com.google.javascript.jscomp.SourceExcerptProvider;
import com.google.javascript.jscomp.SourceRange;
import com.google.javascript.jscomp.testing.CodeSnippetTestRunner;

import java.util.List;

public class LightweightMessageFormatterTest {

 private static final String SOURCE = "function foo() {" +
         "if (condition) {" +
         "  doSomething();" +
         "} else {" +
         "  doSomethingElse();" +
         "}" +
         "}";

 private static final SourceExcerptProvider sourceExcerptProvider =
         new SourceExcerptProvider() {
           @Override
           public List<SourceExcerpt> getExcerpts(SourceFile sourceFile) {
             return List.of(new SourceExcerpt(SOURCE, 0, SOURCE.length()));
           }
         };

 @org.junit.BeforeClass
 public static void setup() {
   CodeSnippetTestRunner.setupClasspath();
 }

 @org.junit.Test
 public void testWithoutSource() {
   LightweightMessageFormatter formatter = LightweightMessageFormatter.withoutSource();
   JSError error = new JSError("Test Error", null, 1, 1);
   String message = formatter.formatError(error);
   assertEquals("ERROR - Test Error\n", message);

   error.setSourceName("MyFile.js");
   error.setLineNumber(2);
   message = formatter.formatError(error);
   assertEquals("ERROR - Test Error: MyFile.js:2\n", message);
 }

 @org.junit.Test
 public void testWithSource() {
   LightweightMessageFormatter formatter =
         new LightweightMessageFormatter(sourceExcerptProvider);
   JSError error = new JSError("Test Error", null, 1, 1);
   String message = formatter.formatError(error);
   assertEquals("ERROR - Test Error: MyFile.js:1:1\nMyFile.js:1:1:\n", message);

   error.setSourceName("MyFile.js");
   error.setLineNumber(2);
   error.setColumnNumber(3);
   message = formatter.formatError(error);
   assertEquals("ERROR - Test Error: MyFile.js:1:3\nMyFile.js:\n1:3\n", message);

   error.setColumnNumber(3);
   message = formatter.formatError(error);
   assertEquals("ERROR - Test Error: MyFile.js:1:3\nMyFile.js:\n1:3\n", message);
 }

 @org.junit.Test
 public void testFormatWarning() {
   LightweightMessageFormatter formatter = new LightweightMessageFormatter(
         sourceExcerptProvider);
   JSError warning = new JSError("Test Warning", null, 1, 1);
   String message = formatter.formatWarning(warning);
   assertEquals("WARNING - Test Warning\n", message);

   warning.setSourceName("MyFile.js");
   warning.setLineNumber(2);
   message = formatter.formatWarning(warning);
   assertEquals("WARNING - Test Warning: MyFile.js:1:1\nMyFile.js:1:1:\n", message);
 }

 @org.junit.Test
 public void testFormatErrorWithRegion() {
   LightweightMessageFormatter formatter =
         new LightweightMessageFormatter(sourceExcerptProvider);
   JSError error = new JSError("Test Error", null, 1, 1);
   SourceRange sourceRange = new SourceRange(1, 0, 2, 2);
   error.setSourceName("MyFile.js");
   error.setLineNumber(1);
   error.setSourceExcerpt(SOURCE);
   error.setColumnNumber(1);
   String message = formatter.formatError(error);
   assertEquals(
       "ERROR - Test Error: MyFile.js:1:1\n" +
               "MyFile.js:\n" +
               "1 | function foo() {" +
               "2 |   if (condition) {" +
               "3 |     doSomething();" +
               "4 |   } else {" +
               "5 |     doSomethingElse();" +
               "6 |   }", message);
 }

 @org.junit.Test
 public void testFormatWarningWithRegion() {
   LightweightMessageFormatter formatter =
         new LightweightMessageFormatter(sourceExcerptProvider);
   JSError warning = new JSError("Test Warning", null, 1, 1);
   SourceRange sourceRange = new SourceRange(1, 0, 2, 2);
   warning.setSourceName("MyFile.js");
   warning.setLineNumber(1);
   warning.setSourceExcerpt(SOURCE);
   warning.setColumnNumber(1);
   String message = formatter.formatWarning(warning);
   assertEquals(
       "WARNING - Test Warning: MyFile.js:1:1\n" +
               "MyFile.js:\n" +
               "1 | function foo() {" +
               "2 |   if (condition) {" +
               "3 |     doSomething();" +
               "4 |   } else {" +
               "5 |     doSomethingElse();" +
               "6 |   }", message);
 }

 @org.junit.Test
 public void testFormatErrorWithRegionAndNoSourceName() {
   LightweightMessageFormatter formatter =
         new LightweightMessageFormatter(sourceExcerptProvider);
   JSError error = new JSError("Test Error", null, 1, 1);
   SourceRange sourceRange = new SourceRange(1, 0, 2, 2);
   error.setLineNumber(1);
   error.setSourceExcerpt(SOURCE);
   error.setColumnNumber(1);
   String message = formatter.formatError(error);
   assertEquals(
       "ERROR - Test Error:\n" +
               "MyFile.js:\n" +
               "1 | function foo() {" +
               "2 |   if (condition) {" +
               "3 |     doSomething();" +
               "4 |   } else {" +
               "5 |     doSomethingElse();" +
               "6 |   }", message);
 }

 @org.junit.Test
 public void testFormatWarningWithRegionAndNoSourceName() {
   LightweightMessageFormatter formatter =
         new LightweightMessageFormatter(sourceExcerptProvider);
   JSError warning = new JSError("Test Warning", null, 1, 1);
   SourceRange sourceRange = new SourceRange(1, 0, 2, 2);
   warning.setLineNumber(1);
   warning.setSourceExcerpt(SOURCE);
   warning.setColumnNumber(1);
   String message = formatter.formatWarning(warning);
   assertEquals(
       "WARNING - Test Warning:\n" +
               "MyFile.js:\n" +
               "1 | function foo() {" +
               "2 |   if (condition) {" +
               "3 |     doSomething();" +
               "4 |   } else {" +
               "5 |     doSomethingElse();" +
               "6 |   }", message);
 }
}