package com.example.test;

import static org.junit.Assert.*;
import com.puppycrawl.tools.checkstyle.api.*;
import org.junit.Test;

public class FileContentsTest {

 private static final String TEST_FILE = "testFile.java";
 private static final String[] TEST_LINES = {"package com.example.test;",
         "/**",
         " * Test class for FileContents",
         " */",
         "public final class FileContentsTest {"};

 @Test
 public void testFileContentsConstructorWithFileNameAndLines() {
     FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);
     assertEquals(TEST_FILE, fileContents.getFileName());
     assertEquals(TEST_LINES.length, fileContents.getLines().length);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFileContentsConstructorWithInvalidFileName() {
     try {
         new FileContents(null, TEST_LINES);
         fail("IllegalArgumentException expected");
     } catch (IllegalArgumentException e) {
         // expected
     }
 }

 @Test
 public void testFileContentsConstructorWithFileText() {
     FileText fileText = new FileText(new String[]{"package com.example.test;",
             "/**",
             " * Test class for FileContents",
             " */",
             "public final class FileContentsTest {",
             "}"});
     FileContents fileContents = new FileContents(fileText);
     assertEquals(TEST_FILE, fileContents.getFileName());
     assertEquals(fileText.toLinesArray().length, fileContents.getLines().length);
 }

 @Test
 public void testReportSingleLineComment() {
     FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);
     int startLineNo = 0;
     int startColNo = 10;
     fileContents.reportSingleLineComment("cpp", startLineNo, startColNo);
     TextBlock comment = fileContents.cppComments.get(startLineNo);
     assertEquals(startColNo, comment.getStartColNo());
     assertEquals(startLineNo, comment.getStartLineNo());
 }

 @Test
 public void testReportBlockComment() {
     FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);
     int startLineNo = 1;
     int startColNo = 1;
     int endLineNo = 3;
     int endColNo = 12;
     fileContents.reportBlockComment("cpp", startLineNo, startColNo, endLineNo, endColNo);
     List<TextBlock> comments = fileContents.clangComments.get(startLineNo);
     assertEquals(1, comments.size());
     TextBlock comment = comments.get(0);
     assertEquals(startColNo, comment.getStartColNo());
     assertEquals(endLineNo, comment.getEndLineNo());
     assertEquals(endColNo, comment.getEndColNo());
 }

 @Test
 public void testGetCppComments() {
     FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);
     TextBlock cppComment = new TextBlock("Test comment", 1, 10, 1, 20);
     fileContents.reportCppComment(1, 10);
     ImmutableMap<Integer, TextBlock> cppComments = fileContents.getCppComments();
     TextBlock returnedComment = cppComments.get(1);
     assertEquals(cppComment, returnedComment);
 }

 @Test
 public void testExtractCComment() {
     FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);
     int startLineNo = 1;
     int startColNo = 1;
     int endLineNo = 3;
     int endColNo = 12;
     String[] extractedComment = fileContents.extractCComment(startLineNo, startColNo, endLineNo, endColNo);
     assertEquals(endLineNo - startLineNo + 2, extractedComment.length);
     assertEquals("package com.example.test;", extractedComment[0]);
     assertEquals("/**", extractedComment[1]);
     assertEquals(" * Test class for FileContents", extractedComment[2]);
     assertEquals(" */", extractedComment[endLineNo - startLineNo + 1]);
 }

 @Test
 public void testGetJavadocBefore() {
     FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);
     int lineNoBefore = 1;
     TextBlock javadocBefore = fileContents.getJavadocBefore(lineNoBefore);
     assertNull(javadocBefore);
 }

 @Test
 public void testGetJavadocBeforeWithJavadocPresent() {
     FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);
     int lineNoBefore = 3;
     TextBlock javadocBefore = fileContents.getJavadocBefore(lineNoBefore);
     assertEquals(" * Test class for FileContents", javadocBefore.getText());
 }

 @Test
 public void testHasIntersectionWithComment() {
     FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);
     int startLineNo = 1;
     int startColNo = 1;
     int endLineNo = 3;
     int endColNo = 12;
     assertTrue(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 }

 @Test
 public void testHasIntersectionWithCommentWithNoIntersection() {
     FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);
     int startLineNo = 0;
     int startColNo = 10;
     int endLineNo = 2;
     int endColNo = 20;
     assertFalse(fileContents.hasIntersectionWithComment(startLineNo, startColNo, endLineNo, endColNo));
 }

 @Test
 public void testInPackageInfo() {
     FileContents fileContents = new FileContents(TEST_FILE, TEST_LINES);
     assertFalse(fileContents.inPackageInfo());
 }

 @Test
 public void testInPackageInfoWithPackageInfoFile() {
     FileContents fileContents = new FileContents(new String[]{"package com.example.test;",
             "/**",
             " * Test package-info.java",
             " */"});
     assertTrue(fileContents.inPackageInfo());
 }
}